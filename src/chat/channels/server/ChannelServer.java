package chat.channels.server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class ChannelServer {

	/**
	 * The main method of the ChannelServer-class starts the server for all the
	 * channels by creating a new serversocket, which clients can connect to.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		ArrayList<Socket> clientsForChannels = new ArrayList<>();
		HashMap<Socket, String> clientNameList = new HashMap<Socket, String>();
		try (ServerSocket serversocket = new ServerSocket(4000)) {
			System.out.println("Server for the channels has started...");
			while (true) {
				Socket socket = serversocket.accept();
				clientsForChannels.add(socket);
				ThreadServer ThreadServer = new ThreadServer(socket, clientsForChannels, clientNameList);
				ThreadServer.start();
			}
		} catch (Exception e) {
			System.out.println(e.getStackTrace());
		}
	}
}
