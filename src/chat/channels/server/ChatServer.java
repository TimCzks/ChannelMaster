package chat.channels.server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class ChatServer {

	public static void main(String[] args) {
		ArrayList<Socket> clientsForGroupchat = new ArrayList<>();
		HashMap<Socket, String> clientNameList = new HashMap<Socket, String>();
		try (ServerSocket serversocket = new ServerSocket(5000)) {
			System.out.println("Server for the group chat has started...");
			while (true) {
				Socket socket = serversocket.accept();
				clientsForGroupchat.add(socket);
				ThreadServer ThreadServer = new ThreadServer(socket, clientsForGroupchat, clientNameList);
				ThreadServer.start();
			}
		} catch (Exception e) {
			System.out.println(e.getStackTrace());
		}
	}
}
