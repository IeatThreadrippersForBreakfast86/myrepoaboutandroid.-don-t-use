package org.xbill.DNS.config;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.WString;
import com.sun.jna.platform.win32.Guid;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.win32.W32APIOptions;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import org.xbill.DNS.WKSRecord;

/* loaded from: classes8.dex */
interface IPHlpAPI extends Library {
    public static final int AF_INET = 2;
    public static final int AF_INET6 = 23;
    public static final int AF_UNSPEC = 0;
    public static final int GAA_FLAG_INCLUDE_ALL_COMPARTMENTS = 512;
    public static final int GAA_FLAG_INCLUDE_ALL_INTERFACES = 256;
    public static final int GAA_FLAG_INCLUDE_GATEWAYS = 128;
    public static final int GAA_FLAG_INCLUDE_PREFIX = 16;
    public static final int GAA_FLAG_INCLUDE_TUNNEL_BINDINGORDER = 1024;
    public static final int GAA_FLAG_INCLUDE_WINS_INFO = 64;
    public static final int GAA_FLAG_SKIP_ANYCAST = 2;
    public static final int GAA_FLAG_SKIP_DNS_SERVER = 8;
    public static final int GAA_FLAG_SKIP_FRIENDLY_NAME = 32;
    public static final int GAA_FLAG_SKIP_MULTICAST = 4;
    public static final int GAA_FLAG_SKIP_UNICAST = 1;
    public static final IPHlpAPI INSTANCE = (IPHlpAPI) Native.load("IPHlpAPI", IPHlpAPI.class, W32APIOptions.ASCII_OPTIONS);

    @Structure.FieldOrder({"Length", "Reserved", "Next", "Address"})
    public static class IP_ADAPTER_ANYCAST_ADDRESS_XP extends Structure {
        public SOCKET_ADDRESS Address;
        public int Length;
        public IP_ADAPTER_DNS_SERVER_ADDRESS_XP.ByReference Next;
        public int Reserved;

        public static class ByReference extends IP_ADAPTER_ANYCAST_ADDRESS_XP implements Structure.ByReference {
        }
    }

    @Structure.FieldOrder({"Length", "Reserved", "Next", "Address"})
    public static class IP_ADAPTER_DNS_SERVER_ADDRESS_XP extends Structure {
        public SOCKET_ADDRESS Address;
        public int Length;
        public ByReference Next;
        public int Reserved;

        public static class ByReference extends IP_ADAPTER_DNS_SERVER_ADDRESS_XP implements Structure.ByReference {
        }
    }

    @Structure.FieldOrder({"Next", "_String"})
    public static class IP_ADAPTER_DNS_SUFFIX extends Structure {
        public ByReference Next;
        public char[] _String = new char[256];

        public static class ByReference extends IP_ADAPTER_DNS_SUFFIX implements Structure.ByReference {
        }
    }

    @Structure.FieldOrder({"Length", "Reserved", "Next", "Address"})
    public static class IP_ADAPTER_MULTICAST_ADDRESS_XP extends Structure {
        public SOCKET_ADDRESS Address;
        public int Length;
        public IP_ADAPTER_DNS_SERVER_ADDRESS_XP.ByReference Next;
        public int Reserved;

        public static class ByReference extends IP_ADAPTER_MULTICAST_ADDRESS_XP implements Structure.ByReference {
        }
    }

    @Structure.FieldOrder({"Length", "IfIndex", "Next", "Address", "PrefixOrigin", "SuffixOrigin", "DadState", "ValidLifetime", "PreferredLifetime", "LeaseLifetime", "OnLinkPrefixLength"})
    public static class IP_ADAPTER_UNICAST_ADDRESS_LH extends Structure {
        public SOCKET_ADDRESS Address;
        public int DadState;
        public int IfIndex;
        public int LeaseLifetime;
        public int Length;
        public ByReference Next;
        public byte OnLinkPrefixLength;
        public int PreferredLifetime;
        public int PrefixOrigin;
        public int SuffixOrigin;
        public int ValidLifetime;

        public static class ByReference extends IP_ADAPTER_UNICAST_ADDRESS_LH implements Structure.ByReference {
        }
    }

    @Structure.FieldOrder({"LowPart", "HighPart"})
    public static class LUID extends Structure {
        public int HighPart;
        public int LowPart;
    }

    int GetAdaptersAddresses(int i, int i2, Pointer pointer, Pointer pointer2, IntByReference intByReference);

    @Structure.FieldOrder({"sin_family", "sin_port", "sin_addr", "sin_zero"})
    public static class sockaddr_in extends Structure {
        public byte[] sin_addr;
        public short sin_family;
        public short sin_port;
        public byte[] sin_zero;

        public sockaddr_in(Pointer p) {
            super(p);
            this.sin_addr = new byte[4];
            this.sin_zero = new byte[8];
            read();
        }
    }

    @Structure.FieldOrder({"sin6_family", "sin6_port", "sin6_flowinfo", "sin6_addr", "sin6_scope_id"})
    public static class sockaddr_in6 extends Structure {
        public byte[] sin6_addr;
        public short sin6_family;
        public int sin6_flowinfo;
        public short sin6_port;
        public int sin6_scope_id;

        public sockaddr_in6(Pointer p) {
            super(p);
            this.sin6_addr = new byte[16];
            read();
        }
    }

    @Structure.FieldOrder({"lpSockaddr", "iSockaddrLength"})
    public static class SOCKET_ADDRESS extends Structure {
        public int iSockaddrLength;
        public Pointer lpSockaddr;

        InetAddress toAddress() throws UnknownHostException {
            switch (this.lpSockaddr.getShort(0L)) {
                case 2:
                    sockaddr_in in4 = new sockaddr_in(this.lpSockaddr);
                    return InetAddress.getByAddress(in4.sin_addr);
                case 23:
                    sockaddr_in6 in6 = new sockaddr_in6(this.lpSockaddr);
                    return Inet6Address.getByAddress("", in6.sin6_addr, in6.sin6_scope_id);
                default:
                    return null;
            }
        }
    }

    @Structure.FieldOrder({"Length", "IfIndex", "Next", "AdapterName", "FirstUnicastAddress", "FirstAnycastAddress", "FirstMulticastAddress", "FirstDnsServerAddress", "DnsSuffix", "Description", "FriendlyName", "PhysicalAddress", "PhysicalAddressLength", "Flags", "Mtu", "IfType", "OperStatus", "Ipv6IfIndex", "ZoneIndices", "FirstPrefix", "TransmitLinkSpeed", "ReceiveLinkSpeed", "FirstWinsServerAddress", "FirstGatewayAddress", "Ipv4Metric", "Ipv6Metric", "Luid", "Dhcpv4Server", "CompartmentId", "NetworkGuid", "ConnectionType", "TunnelType", "Dhcpv6Server", "Dhcpv6ClientDuid", "Dhcpv6ClientDuidLength", "Dhcpv6Iaid", "FirstDnsSuffix"})
    public static class IP_ADAPTER_ADDRESSES_LH extends Structure {
        public String AdapterName;
        public int CompartmentId;
        public int ConnectionType;
        public WString Description;
        public SOCKET_ADDRESS Dhcpv4Server;
        public byte[] Dhcpv6ClientDuid;
        public int Dhcpv6ClientDuidLength;
        public int Dhcpv6Iaid;
        public SOCKET_ADDRESS Dhcpv6Server;
        public WString DnsSuffix;
        public IP_ADAPTER_ANYCAST_ADDRESS_XP.ByReference FirstAnycastAddress;
        public IP_ADAPTER_DNS_SERVER_ADDRESS_XP.ByReference FirstDnsServerAddress;
        public IP_ADAPTER_DNS_SUFFIX.ByReference FirstDnsSuffix;
        public Pointer FirstGatewayAddress;
        public IP_ADAPTER_MULTICAST_ADDRESS_XP.ByReference FirstMulticastAddress;
        public Pointer FirstPrefix;
        public IP_ADAPTER_UNICAST_ADDRESS_LH.ByReference FirstUnicastAddress;
        public Pointer FirstWinsServerAddress;
        public int Flags;
        public WString FriendlyName;
        public int IfIndex;
        public int IfType;
        public int Ipv4Metric;
        public int Ipv6IfIndex;
        public int Ipv6Metric;
        public int Length;
        public LUID Luid;
        public int Mtu;
        public Guid.GUID NetworkGuid;
        public ByReference Next;
        public int OperStatus;
        public byte[] PhysicalAddress;
        public int PhysicalAddressLength;
        public long ReceiveLinkSpeed;
        public long TransmitLinkSpeed;
        public int TunnelType;
        public int[] ZoneIndices;

        public static class ByReference extends IP_ADAPTER_ADDRESSES_LH implements Structure.ByReference {
        }

        public IP_ADAPTER_ADDRESSES_LH(Pointer p) {
            super(p);
            this.PhysicalAddress = new byte[8];
            this.ZoneIndices = new int[16];
            this.Dhcpv6ClientDuid = new byte[WKSRecord.Service.CISCO_FNA];
            read();
        }

        public IP_ADAPTER_ADDRESSES_LH() {
            this.PhysicalAddress = new byte[8];
            this.ZoneIndices = new int[16];
            this.Dhcpv6ClientDuid = new byte[WKSRecord.Service.CISCO_FNA];
        }
    }
}
