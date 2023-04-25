package chat.channels.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Thread for Server
 */
public class ThreadServer extends Thread {

	private Socket socket;
	private ArrayList<Socket> clients;
	private HashMap<Socket, String> clientNameList;

	/**
	 * Starts a new Threadserver.
	 * 
	 * @param socket
	 * @param clients
	 * @param clientNameList
	 */
	public ThreadServer(Socket socket, ArrayList<Socket> clients, HashMap<Socket, String> clientNameList) {
		this.socket = socket;
		this.clients = clients;
		this.clientNameList = clientNameList;
	}

	/**
	 * Through this thread, messages are sent to all the other clients, that are
	 * connected to the server.
	 */
	@Override
	public void run() {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			while (true) {
				String outputString = reader.readLine();
				if (outputString.equals("logout")) {
					throw new SocketException();
				}

				if (!clientNameList.containsKey(socket)) {
					String[] messageString = outputString.split(":", 2);
					clientNameList.put(socket, messageString[0]);
					System.out.println(messageString[0] + messageString[1]);
					showMessageToAllClients(socket, messageString[0] + messageString[1]);
				} else {
					System.out.println(outputString);
					showMessageToAllClients(socket, outputString);
				}
			}
		} catch (SocketException e) {
			String printMessage = clientNameList.get(socket) + " left the chat room";
			System.out.println(printMessage);
			showMessageToAllClients(socket, printMessage);
			clients.remove(socket);
			clientNameList.remove(socket);
		} catch (Exception e) {
			System.out.println(e.getStackTrace());
		}
	}

	private void showMessageToAllClients(Socket sender, String outputString) {
		Socket socket;
		PrintWriter printWriter;
		int i = 0;
		while (i < clients.size()) {
			socket = clients.get(i);
			i++;
			try {
				if (socket != sender) {
					printWriter = new PrintWriter(socket.getOutputStream(), true);
					printWriter.println(outputString);
				}
			} catch (IOException ex) {
				System.out.println(ex);
			}
		}
	}
}
