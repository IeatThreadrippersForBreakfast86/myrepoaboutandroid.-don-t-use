package com.ninjatech.minecraftlanbridge.relay;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.UByte;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Charsets;

/* compiled from: HandshakeRewriter.kt */
@Metadata(m145d1 = {"\u0000P\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0012\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\bÆ\u0002\u0018\u00002\u00020\u0001:\u0001\u001fB\u0007\b\u0002¢\u0006\u0002\u0010\u0002J(\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\u00042\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u00042\u0006\u0010\r\u001a\u00020\u0004H\u0002J\u0018\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\bH\u0002J\u001c\u0010\u0013\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\b0\u00142\u0006\u0010\u0010\u001a\u00020\u0011H\u0002J>\u0010\u0015\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u000b2\u0006\u0010\u0019\u001a\u00020\u00042\u0016\b\u0002\u0010\u001a\u001a\u0010\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\u000f\u0018\u00010\u001bJ\u0014\u0010\u001c\u001a\u00020\u000f*\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\u0004H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000¨\u0006 "}, m146d2 = {"Lcom/ninjatech/minecraftlanbridge/relay/HandshakeRewriter;", "", "()V", "HANDSHAKE_PACKET_ID", "", "MAX_HANDSHAKE_BYTES", "MAX_VARINT_BYTES", "buildHandshakePacket", "", "protocolVersion", "serverAddress", "", "serverPort", "nextState", "readFully", "", "input", "Ljava/io/InputStream;", "buf", "readVarIntWithRaw", "Lkotlin/Pair;", "rewriteHandshake", "output", "Ljava/io/OutputStream;", "targetHost", "targetPort", "log", "Lkotlin/Function1;", "writeVarInt", "Ljava/io/ByteArrayOutputStream;", "value", "Cursor", "app_debug"}, m147k = 1, m148mv = {1, 9, 0}, m150xi = 48)
/* loaded from: classes3.dex */
public final class HandshakeRewriter {
    private static final int HANDSHAKE_PACKET_ID = 0;
    public static final HandshakeRewriter INSTANCE = new HandshakeRewriter();
    private static final int MAX_HANDSHAKE_BYTES = 8192;
    private static final int MAX_VARINT_BYTES = 5;

    private HandshakeRewriter() {
    }

    public static /* synthetic */ void rewriteHandshake$default(HandshakeRewriter handshakeRewriter, InputStream inputStream, OutputStream outputStream, String str, int i, Function1 function1, int i2, Object obj) throws IOException {
        Function1 function12;
        if ((i2 & 16) == 0) {
            function12 = function1;
        } else {
            function12 = null;
        }
        handshakeRewriter.rewriteHandshake(inputStream, outputStream, str, i, function12);
    }

    /* JADX WARN: Removed duplicated region for block: B:42:0x0121  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x0145  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x0149  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x0155  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void rewriteHandshake(InputStream input, OutputStream output, String targetHost, int targetPort, Function1<? super String, Unit> log) throws IOException {
        byte[] bArrBuildHandshakePacket;
        byte[] rewritten;
        int avail;
        Intrinsics.checkNotNullParameter(input, "input");
        Intrinsics.checkNotNullParameter(output, "output");
        Intrinsics.checkNotNullParameter(targetHost, "targetHost");
        try {
            Pair<Integer, byte[]> varIntWithRaw = readVarIntWithRaw(input);
            int value = varIntWithRaw.component1().intValue();
            byte[] raw = varIntWithRaw.component2();
            if (value <= 0 || value > 8192) {
                if (log != null) {
                    log.invoke("First packet length " + value + " doesn't look like a handshake; relaying unmodified");
                }
                output.write(raw);
                output.flush();
                return;
            }
            byte[] packetBody = new byte[value];
            try {
                readFully(input, packetBody);
                Cursor cursor = new Cursor(packetBody);
                try {
                    int packetId = cursor.readVarInt();
                    if (packetId != 0) {
                        if (log != null) {
                            try {
                                log.invoke("First packet id " + packetId + " is not a handshake; relaying unmodified");
                            } catch (Exception e) {
                                e = e;
                                if (log != null) {
                                    log.invoke("Could not parse handshake (" + e.getMessage() + "); relaying unmodified");
                                }
                                bArrBuildHandshakePacket = null;
                                rewritten = bArrBuildHandshakePacket;
                                if (rewritten != null) {
                                }
                                avail = input.available();
                                if (avail > 0) {
                                }
                                output.flush();
                            }
                        }
                        bArrBuildHandshakePacket = null;
                    } else {
                        int protocolVersion = cursor.readVarInt();
                        String originalAddress = cursor.readString();
                        int originalPort = cursor.readU16();
                        int nextState = cursor.readVarInt();
                        try {
                            if (Intrinsics.areEqual(originalAddress, targetHost) && originalPort == targetPort) {
                                if (log != null) {
                                    log.invoke("Handshake already targets " + targetHost + ":" + targetPort + "; relaying unmodified");
                                }
                                bArrBuildHandshakePacket = null;
                            } else {
                                if (log != null) {
                                    log.invoke("Rewriting handshake server address: \"" + originalAddress + ":" + originalPort + "\" -> \"" + targetHost + ":" + targetPort + "\" (nextState=" + nextState + ", protocol=" + protocolVersion + ")");
                                }
                                bArrBuildHandshakePacket = buildHandshakePacket(protocolVersion, targetHost, targetPort, nextState);
                            }
                        } catch (Exception e2) {
                            e = e2;
                            if (log != null) {
                            }
                            bArrBuildHandshakePacket = null;
                            rewritten = bArrBuildHandshakePacket;
                            if (rewritten != null) {
                            }
                            avail = input.available();
                            if (avail > 0) {
                            }
                            output.flush();
                        }
                    }
                } catch (Exception e3) {
                    e = e3;
                }
                rewritten = bArrBuildHandshakePacket;
                if (rewritten != null) {
                    output.write(rewritten);
                } else {
                    output.write(raw);
                    output.write(packetBody);
                }
                avail = input.available();
                if (avail > 0) {
                    byte[] extra = new byte[avail];
                    int off = 0;
                    while (off < avail) {
                        int r = input.read(extra, off, avail - off);
                        if (r <= 0) {
                            break;
                        } else {
                            off += r;
                        }
                    }
                    if (off > 0) {
                        output.write(extra, 0, off);
                    }
                }
                output.flush();
            } catch (EOFException e4) {
                throw new IOException("Client disconnected during handshake", e4);
            }
        } catch (Exception e5) {
            throw new IOException("No handshake received from client", e5);
        }
    }

    private final byte[] buildHandshakePacket(int protocolVersion, String serverAddress, int serverPort, int nextState) {
        byte[] addressBytes = serverAddress.getBytes(Charsets.UTF_8);
        Intrinsics.checkNotNullExpressionValue(addressBytes, "getBytes(...)");
        ByteArrayOutputStream body = new ByteArrayOutputStream();
        writeVarInt(body, 0);
        writeVarInt(body, protocolVersion);
        writeVarInt(body, addressBytes.length);
        body.write(addressBytes);
        body.write((serverPort >>> 8) & 255);
        body.write(serverPort & 255);
        writeVarInt(body, nextState);
        byte[] bodyBytes = body.toByteArray();
        ByteArrayOutputStream packet = new ByteArrayOutputStream();
        writeVarInt(packet, bodyBytes.length);
        packet.write(bodyBytes);
        byte[] byteArray = packet.toByteArray();
        Intrinsics.checkNotNullExpressionValue(byteArray, "toByteArray(...)");
        return byteArray;
    }

    private final void writeVarInt(ByteArrayOutputStream $this$writeVarInt, int value) {
        int v = value;
        while ((v & (-128)) != 0) {
            $this$writeVarInt.write((v & 127) | 128);
            v >>>= 7;
        }
        $this$writeVarInt.write(v);
    }

    /* compiled from: HandshakeRewriter.kt */
    @Metadata(m145d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\b\u0003\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0006\u0010\u000b\u001a\u00020\fJ\u0006\u0010\r\u001a\u00020\u0006J\u0006\u0010\u000e\u001a\u00020\u0006R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u0005\u001a\u00020\u0006X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0007\u0010\b\"\u0004\b\t\u0010\n¨\u0006\u000f"}, m146d2 = {"Lcom/ninjatech/minecraftlanbridge/relay/HandshakeRewriter$Cursor;", "", "data", "", "([B)V", "pos", "", "getPos", "()I", "setPos", "(I)V", "readString", "", "readU16", "readVarInt", "app_debug"}, m147k = 1, m148mv = {1, 9, 0}, m150xi = 48)
    private static final class Cursor {
        private final byte[] data;
        private int pos;

        public Cursor(byte[] data) {
            Intrinsics.checkNotNullParameter(data, "data");
            this.data = data;
        }

        public final int getPos() {
            return this.pos;
        }

        public final void setPos(int i) {
            this.pos = i;
        }

        public final int readVarInt() throws IOException {
            int result = 0;
            int shift = 0;
            while (this.pos < this.data.length) {
                byte[] bArr = this.data;
                int i = this.pos;
                this.pos = i + 1;
                int b = bArr[i];
                result |= (b & 127) << shift;
                if ((b & 128) != 0) {
                    shift += 7;
                    if (shift >= 32) {
                        throw new IOException("VarInt too long");
                    }
                } else {
                    return result;
                }
            }
            throw new EOFException("Truncated VarInt");
        }

        public final String readString() throws IOException {
            int len = readVarInt();
            if (len < 0 || len > this.data.length - this.pos) {
                throw new IOException("Bad string length " + len);
            }
            String s = new String(this.data, this.pos, len, Charsets.UTF_8);
            this.pos += len;
            return s;
        }

        public final int readU16() throws EOFException {
            if (this.pos + 2 > this.data.length) {
                throw new EOFException("Truncated u16");
            }
            int v = ((this.data[this.pos] & UByte.MAX_VALUE) << 8) | (this.data[this.pos + 1] & UByte.MAX_VALUE);
            this.pos += 2;
            return v;
        }
    }

    private final Pair<Integer, byte[]> readVarIntWithRaw(InputStream input) throws IOException {
        ByteArrayOutputStream raw = new ByteArrayOutputStream(5);
        int result = 0;
        int shift = 0;
        for (int i = 0; i < 5; i++) {
            int b = input.read();
            if (b < 0) {
                throw new EOFException("Stream ended inside VarInt");
            }
            raw.write(b);
            result |= (b & 127) << shift;
            if ((b & 128) == 0) {
                return TuplesKt.m153to(Integer.valueOf(result), raw.toByteArray());
            }
            shift += 7;
        }
        throw new IOException("VarInt too long");
    }

    private final void readFully(InputStream input, byte[] buf) throws IOException {
        int off = 0;
        while (off < buf.length) {
            int r = input.read(buf, off, buf.length - off);
            if (r < 0) {
                throw new EOFException("Stream ended mid-packet (" + off + "/" + buf.length + " bytes)");
            }
            off += r;
        }
    }
}
