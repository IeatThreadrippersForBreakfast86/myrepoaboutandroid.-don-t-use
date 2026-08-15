package org.xbill.DNS.tools;

/* loaded from: classes8.dex */
public class Tools {
    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    /* JADX WARN: Removed duplicated region for block: B:28:0x0057  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static void main(String[] args) throws Exception {
        char c = 1;
        if (args == null || args.length == 0) {
            System.out.println("Usage: <command> <options>");
            System.out.println("  Commands:");
            System.out.println("    dig");
            System.out.println("    jnamed");
            System.out.println("    lookup");
            System.out.println("    primary");
            System.out.println("    update");
            System.out.println("    xfrin");
            System.exit(1);
            return;
        }
        String program = args[0];
        String[] programArgs = new String[args.length - 1];
        System.arraycopy(args, 1, programArgs, 0, args.length - 1);
        switch (program.hashCode()) {
            case -1155692273:
                if (!program.equals("jnamed")) {
                    c = 65535;
                    break;
                }
                break;
            case -1097094790:
                if (program.equals("lookup")) {
                    c = 2;
                    break;
                }
                break;
            case -838846263:
                if (program.equals("update")) {
                    c = 4;
                    break;
                }
                break;
            case -314765822:
                if (program.equals("primary")) {
                    c = 3;
                    break;
                }
                break;
            case 99458:
                if (program.equals("dig")) {
                    c = 0;
                    break;
                }
                break;
            case 113974121:
                if (program.equals("xfrin")) {
                    c = 5;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                dig.main(programArgs);
                break;
            case 1:
                jnamed.main(programArgs);
                break;
            case 2:
                lookup.main(programArgs);
                break;
            case 3:
                primary.main(programArgs);
                break;
            case 4:
                update.main(programArgs);
                break;
            case 5:
                xfrin.main(programArgs);
                break;
            default:
                System.out.println("invalid command");
                break;
        }
    }
}
