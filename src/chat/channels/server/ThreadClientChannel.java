package chat.channels.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.List;

import channels.GUI.ChannelGUI;
import chat.domain.logic.ReadAndSaveData;
import chat.domain.logic.User;

public class ThreadClientChannel implements Runnable {
	private Socket socket;
	private BufferedReader reader;
	private ChannelGUI currentChannel;
	private ReadAndSaveData readAndSaveData = new ReadAndSaveData();
	private List<ChannelGUI> channelsOfUser;

	/**
	 * Creates a new Thread for a new channel client, after connecting to the
	 * server.
	 * 
	 * @param socket         from the server
	 * @param user
	 * @param currentchannel where the user is in
	 */
	public ThreadClientChannel(Socket socket, User user, ChannelGUI currentchannel) {
		this.socket = socket;
		this.currentChannel = currentchannel;
		this.channelsOfUser = user.getOpenChannels();
		try {
			this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Contains the implementation for receiving messages by reading what is sent
	 * onto the server (into this channel).
	 */
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

				if (senderChannelName.equals((currentChannel.getTitle().replace("Channel: ", "")))) {
					currentChannel.getChatArea().append(message + "\n");
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