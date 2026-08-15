package org.xbill.DNS;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedSelectorException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xbill.DNS.utils.hexdump;

/* loaded from: classes8.dex */
public abstract class NioClient {
    static final String REGISTER_SHUTDOWN_HOOK_PROPERTY = "dnsjava.nio.register_shutdown_hook";
    static final String SELECTOR_TIMEOUT_PROPERTY = "dnsjava.nio.selector_timeout";
    private static volatile boolean closeDone;
    private static Thread closeThread;
    private static volatile boolean run;
    private static volatile Selector selector;
    private static Thread selectorThread;
    private static Consumer<Selector> tcpRegistrationsTask;
    private static Consumer<Selector> udpRegistrationsTask;
    private static final Logger log = LoggerFactory.getLogger((Class<?>) NioClient.class);
    private static final Object NIO_CLIENT_LOCK = new Object();
    private static PacketLogger packetLogger = null;
    private static final Runnable[] TIMEOUT_TASKS = new Runnable[2];
    private static final Runnable[] CLOSE_TASKS = new Runnable[2];

    interface KeyProcessor {
        void processReadyKey(SelectionKey selectionKey);
    }

    static Selector selector() throws IOException {
        if (selector == null) {
            synchronized (NIO_CLIENT_LOCK) {
                if (selector == null) {
                    selector = Selector.open();
                    log.debug("Starting dnsjava NIO selector thread");
                    run = true;
                    selectorThread = new Thread(new Runnable() { // from class: org.xbill.DNS.NioClient$$ExternalSyntheticLambda0
                        @Override // java.lang.Runnable
                        public final void run() throws InterruptedException, IOException {
                            NioClient.runSelector();
                        }
                    });
                    selectorThread.setDaemon(true);
                    selectorThread.setName("dnsjava NIO selector");
                    selectorThread.start();
                    closeThread = new Thread(new Runnable() { // from class: org.xbill.DNS.NioClient$$ExternalSyntheticLambda1
                        @Override // java.lang.Runnable
                        public final void run() {
                            NioClient.close(true);
                        }
                    });
                    closeThread.setName("dnsjava NIO shutdown hook");
                    if (Boolean.parseBoolean(System.getProperty(REGISTER_SHUTDOWN_HOOK_PROPERTY, "true"))) {
                        Runtime.getRuntime().addShutdownHook(closeThread);
                    }
                }
            }
        }
        return selector;
    }

    public static void close() {
        close(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void close(boolean fromHook) {
        log.debug("Closing dnsjava NIO selector, fromHook={}", Boolean.valueOf(fromHook));
        run = false;
        Selector localSelector = selector;
        if (localSelector != null) {
            selector.wakeup();
        }
        if (!fromHook) {
            synchronized (NIO_CLIENT_LOCK) {
                if (closeThread != null) {
                    try {
                        Runtime.getRuntime().removeShutdownHook(closeThread);
                    } catch (Exception ex) {
                        log.warn("Failed to remove shutdown hook, ignoring and continuing close", (Throwable) ex);
                    }
                }
            }
        }
        if (localSelector == null) {
            return;
        }
        synchronized (NIO_CLIENT_LOCK) {
            while (!closeDone) {
                try {
                    try {
                        NIO_CLIENT_LOCK.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        closeDone = false;
                    }
                } finally {
                    closeDone = false;
                }
            }
        }
    }

    static void runSelector() throws InterruptedException, IOException {
        int numSelects;
        int timeout = Integer.getInteger(SELECTOR_TIMEOUT_PROPERTY, 1000).intValue();
        if (timeout <= 0 || timeout > 1000) {
            throw new IllegalArgumentException("Invalid selector_timeout, must be between 1 and 1000");
        }
        while (run) {
            try {
                numSelects = selector.select(timeout);
            } catch (IOException e) {
                log.error("A selection operation failed", (Throwable) e);
            } catch (ClosedSelectorException e2) {
            }
            if (Thread.currentThread().isInterrupted()) {
                log.debug("Sector thread was interrupted, stopping");
                close();
                break;
            } else {
                if (numSelects == 0) {
                    runTasks(TIMEOUT_TASKS);
                }
                if (run) {
                    runRegistrationTasks();
                    processReadyKeys();
                }
            }
        }
        runClose();
        log.debug("dnsjava NIO selector thread stopped");
    }

    private static void runClose() throws InterruptedException, IOException {
        try {
            runTasks(CLOSE_TASKS);
        } catch (Exception e) {
            log.warn("Failed to execute shutdown task, ignoring and continuing close", (Throwable) e);
        }
        Selector localSelector = selector;
        Thread localSelectorThread = selectorThread;
        synchronized (NIO_CLIENT_LOCK) {
            selector = null;
            selectorThread = null;
            closeThread = null;
            closeDone = true;
            NIO_CLIENT_LOCK.notifyAll();
        }
        if (localSelector != null) {
            try {
                localSelector.close();
            } catch (IOException e2) {
                log.warn("Failed to properly close selector, ignoring and continuing close", (Throwable) e2);
            }
        }
        if (localSelectorThread != null) {
            try {
                localSelectorThread.join();
            } catch (InterruptedException e3) {
                Thread.currentThread().interrupt();
            }
        }
    }

    static void setTimeoutTask(Runnable r, boolean isTcpClient) {
        addTask(TIMEOUT_TASKS, r, isTcpClient);
    }

    static void setRegistrationsTask(Consumer<Selector> r, boolean isTcpClient) {
        if (isTcpClient) {
            tcpRegistrationsTask = r;
        } else {
            udpRegistrationsTask = r;
        }
    }

    static void setCloseTask(Runnable r, boolean isTcpClient) {
        addTask(CLOSE_TASKS, r, isTcpClient);
    }

    private static void addTask(Runnable[] tasks, Runnable r, boolean isTcpClient) {
        if (isTcpClient) {
            tasks[0] = r;
        } else {
            tasks[1] = r;
        }
    }

    private static void runTasks(Runnable[] runnables) {
        Runnable r0 = runnables[0];
        if (r0 != null) {
            r0.run();
        }
        Runnable r1 = runnables[1];
        if (r1 != null) {
            r1.run();
        }
    }

    private static void runRegistrationTasks() {
        Consumer<Selector> tcpTask = tcpRegistrationsTask;
        if (tcpTask != null) {
            tcpTask.accept(selector);
        }
        Consumer<Selector> udpTask = udpRegistrationsTask;
        if (udpTask != null) {
            udpTask.accept(selector);
        }
    }

    private static void processReadyKeys() {
        Iterator<SelectionKey> it = selector.selectedKeys().iterator();
        while (it.hasNext()) {
            SelectionKey key = it.next();
            it.remove();
            KeyProcessor t = (KeyProcessor) key.attachment();
            t.processReadyKey(key);
        }
    }

    static void verboseLog(String prefix, SocketAddress local, SocketAddress remote, ByteBuffer data) {
        if (log.isTraceEnabled() || packetLogger != null) {
            byte[] dst = new byte[data.remaining()];
            int pos = data.position();
            data.get(dst, 0, data.remaining());
            data.position(pos);
            verboseLog(prefix, local, remote, dst);
        }
    }

    static void verboseLog(String prefix, SocketAddress local, SocketAddress remote, byte[] data) {
        if (log.isTraceEnabled()) {
            log.trace(hexdump.dump(prefix, data));
        }
        if (packetLogger != null) {
            packetLogger.log(prefix, local, remote, data);
        }
    }

    static void setPacketLogger(PacketLogger logger) {
        packetLogger = logger;
    }
}
