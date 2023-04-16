package chat.channels.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.List;

import javax.swing.JTextArea;

import chat.domain.logic.User;

public class ThreadClientChannel implements Runnable {
	private Socket socket;
	private BufferedReader reader;
	private JTextArea chatArea;
	private List<String> channelnamesOfUser;

	public ThreadClientChannel(Socket socket, JTextArea chatArea, User user) {
		this.socket = socket;
		this.chatArea = chatArea;
		this.channelnamesOfUser = user.getOpenChannels();
		try {
			this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			while (true) {
				String message = reader.readLine();
				if (message == null) {
					break;
				}
				String[] parts = message.split(":", 2);
				String senderChannelName = parts[0];
				// Solution 1: Nur ein Channel kann gleichzeitig vom User geöffnet sein
				// Solution 2: Irgendwie dem Thread mitteilen, welche chatArea genau gemeint ist
				// (oder z.b. if-constraint anpassen/verbessern)
				if (channelnamesOfUser.contains(senderChannelName)) {
					chatArea.append(message + "\n");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}