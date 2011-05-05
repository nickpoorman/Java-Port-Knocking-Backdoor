/*
 * Nicholas Poorman 2011
 * 
 * The arguments for this program are as follows:
 *  -i<interface number>
 *  -
 * 
 */

package server;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.jnetpcap.JCaptureHeader;
import org.jnetpcap.Pcap;
import org.jnetpcap.PcapBpfProgram;
import org.jnetpcap.PcapIf;
import org.jnetpcap.packet.JPacket;
import org.jnetpcap.packet.JPacketHandler;
import org.jnetpcap.packet.Payload;
import org.jnetpcap.protocol.network.Ip4;
import org.jnetpcap.protocol.tcpip.Udp;

public class Server {

	private static final boolean USE_FILTER = true;
	private static final String PARAMS_ERROR = "Please use the following format for running the program: \n " + "  java Server <interface number>";
	private static final String INTERFACE_SWITCH = "-i";

	/*
	 * This method returns the first argument it finds that matches the supplied
	 * switch.
	 */
	private static String findArgument(String sw, String[] args) {
		for (String arg : args) {
			if (arg.startsWith(sw)) {
				return arg.substring(sw.length());
			}
		}
		return "";
	}

	public static void main(String[] args) {
		// I was thinking about using a properties file for this but I decided
		// to use the command line params instead

		List<PcapIf> alldevs = new ArrayList<PcapIf>(); // Will be filled with
		// NICs
		StringBuilder errbuf = new StringBuilder(); // For any error msgs

		// look for interface
		String interfaceNum = Server.findArgument(Server.INTERFACE_SWITCH, args);

		if (interfaceNum == "") {
			System.out.println(Server.PARAMS_ERROR);

			/***************************************************************************
			 * First get a list of devices on this system
			 **************************************************************************/
			int r = Pcap.findAllDevs(alldevs, errbuf);
			if (r == Pcap.NOT_OK || alldevs.isEmpty()) {
				System.err.printf("Can't read list of devices, error is %s", errbuf.toString());
				return;
			}

			System.out.println("Network devices found:");

			int i = 0;
			for (PcapIf device : alldevs) {
				System.out.printf("#%d: %s [%s]\n", i++, device.getName(), device.getDescription());
			}
		}

		PcapIf device = alldevs.get(Integer.parseInt(interfaceNum)); // Pick one
		System.out.printf("\nUsing interface 0 '%s':\n", device.getDescription());

		/***************************************************************************
		 * Second we open up the selected device
		 **************************************************************************/
		int snaplen = 64 * 1024; // Capture all packets
		int flags = Pcap.MODE_PROMISCUOUS; // capture all packets
		int timeout = 10 * 1000; // 10 seconds in millis
		Pcap pcap = Pcap.openLive(device.getName(), snaplen, flags, timeout, errbuf);

		if (pcap == null) {
			System.err.printf("Error while opening device for capture: " + errbuf.toString());
			return;
		}

		/***************************************************************************
		 * Create a filter so we only get packets that we want. Filter syntax is
		 * here: http://www.tcpdump.org/tcpdump_man.html
		 */
		if (USE_FILTER) {
			PcapBpfProgram program = new PcapBpfProgram();
			String expression = "udp";
			int optimize = 0; // 0 = false
			int netmask = 0xFFFFFF00; // 255.255.255.0
			// int netmask = 0;

			if (pcap.compile(program, expression, optimize, netmask) != Pcap.OK) {
				System.err.println(pcap.getErr());
				return;
			}

			if (pcap.setFilter(program) != Pcap.OK) {
				System.err.println(pcap.getErr());
				return;
			}
		}

		/***************************************************************************
		 * Third we create a packet handler which will receive packets from the
		 * libpcap loop.
		 **************************************************************************/
		JPacketHandler<String> jpacketHandler = new JPacketHandler<String>() {

			public void nextPacket(JPacket packet, String user) {
				final JCaptureHeader header = packet.getCaptureHeader();
				// System.out.println("Number of packet headers: " +
				// packet.getHeaderCount());
				Payload payload = new Payload();
				payload = packet.getHeaderByIndex(packet.getHeaderCount() - 1, payload);

				// System.out.printf("Packet caplen=%d wirelen=%d\n",
				// header.caplen(), header.wirelen());

				// System.out.println(packet.toString());

				// System.out.println(new String(payload.data()));
				if (packet.hasHeader(new Ip4())) {
					Ip4 ip4 = packet.getHeader(new Ip4());

					if (packet.hasHeader(new Udp())) {
						Udp udp = packet.getHeader(new Udp());
						int port = udp.destination();

						try {
							KnockStateMachine ksm = StateMachineManager.getInstance().addMachineIfNotExists(ip4.sourceToInt(), ip4.source());
							// now check the port on that state machine
							if (ksm.checkAndIncState(port)) {
								// the port sequence is complete
								// open a reverse connection to the source host
								ReverseConnection rc = new ReverseConnection(ksm.getSource());
								Thread t = new Thread(rc);
								t.start();
							} else {
								System.out.println("Packet is Not a new connection");
							}
						} catch (UnknownHostException e) {
							e.printStackTrace();
						}
					}
				}
			}
		};

		/***************************************************************************
		 * Fourth we enter the loop and tell it to capture unlimited(-1)
		 * packets.
		 **************************************************************************/
		pcap.loop(-1, jpacketHandler, "jNetPcap rocks!");

		/***************************************************************************
		 * Last thing to do is close the pcap handle
		 **************************************************************************/
		pcap.close();
	}

}
