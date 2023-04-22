package chat.channels.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import javax.swing.JOptionPane;

import channels.GUI.ChatClientGUI;

public class ThreadClientChat implements Runnable {
	private Socket socket;
	private BufferedReader cin;
	private ChatClientGUI chatClientGUI;

	public ThreadClientChat(Socket socket, ChatClientGUI chatClientGUI) throws IOException {
		this.socket = socket;
		this.chatClientGUI = chatClientGUI;
		this.cin = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}

	@Override
	public void run() {
		try {
			while (true) {
				String message = cin.readLine();
				if (message == null) {
					break;
				}
				chatClientGUI.getChatTextArea().append(message + "\n");
			}
		} catch (IOException exception) {
			JOptionPane.showMessageDialog(chatClientGUI.getFrame(),
					"Failed to receive message: " + exception.getMessage());
			exception.printStackTrace();
		} finally {
			try {
				cin.close();
			} catch (Exception exception) {
				JOptionPane.showMessageDialog(chatClientGUI.getFrame(),
						"Failed to close input stream: " + exception.getMessage());
				exception.printStackTrace();
			}
		}
	}
}