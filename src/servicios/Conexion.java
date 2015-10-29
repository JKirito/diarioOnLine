package servicios;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Conexion {
	private static String dirWeb = "www.google.com";
	private static int port = 80;
	
	public static boolean isOnline()
	{
		Socket s = null;
		 try {
			s = new Socket(dirWeb, port);
			return s.isConnected();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 return false;
	}
}
