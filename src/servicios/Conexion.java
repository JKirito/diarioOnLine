package servicios;

import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;

public class Conexion {
	private static String dirWeb = "www.google.com";
	private static int port = 80;
	public static int TIMEOUT_MS_S = 5000;
	public static int TIMEOUT_MS_M = 15000;
	public static int TIMEOUT_MS_L = 30000;

	public static boolean isOnline() {
		boolean status = false;
		Socket sock = new Socket();
		InetSocketAddress address = new InetSocketAddress(dirWeb, port);
		try {
			sock.connect(address, 5000);
			if (sock.isConnected()) {
				status = true;
			}
		} catch (Exception e) {

		} finally {
			try {
				sock.close();
			} catch (Exception e) {

			}
		}
		return status;
	}
	
	public static boolean isNetworkInterfacesAvailable() throws SocketException
	{
		Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
		while (interfaces.hasMoreElements()) {
		  NetworkInterface interf = interfaces.nextElement();
		  System.out.println("interf: "+interf);
		  if (interf.isUp() && !interf.isLoopback())
		    return true;
		}
		return false;
	}

}
