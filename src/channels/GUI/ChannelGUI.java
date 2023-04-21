package channels.GUI;

import java.awt.BorderLayout;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import chat.channels.server.ThreadClientChannel;
import chat.domain.logic.ReadAndSaveData;
import chat.domain.logic.User;

public class ChannelGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private String username;
	private String channelname;
	private JTextArea chatArea;
	private JTextField inputField;
	private JButton sendButton, saveButton;
	private List<String> allMessages = new LinkedList<>();
	private ReadAndSaveData readAndSaveData = new ReadAndSaveData();
	private PrintWriter writer;

	public ChannelGUI(User user, String channelName, List<String> oldMessages) {
		this.username = user.getUsername();
		this.channelname = channelName;
		this.allMessages.addAll(oldMessages);

		setTitle("Channel: " + channelName);
		setSize(400, 300);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.addWindowListener(new java.awt.event.WindowAdapter() {
			@SuppressWarnings("unlikely-arg-type")
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				user.getOpenChannels().remove(this);
			}
		});

		chatArea = new JTextArea();
		getChatArea().setEditable(false);
		JScrollPane scrollPane = new JScrollPane(getChatArea());
		add(scrollPane, BorderLayout.CENTER);

		inputField = new JTextField();
		sendButton = new JButton("Send");
		saveButton = new JButton("Save all messages");

		JPanel inputPanel = new JPanel();
		inputPanel.setLayout(new BorderLayout());
		inputPanel.add(saveButton, BorderLayout.NORTH);
		inputPanel.add(inputField, BorderLayout.CENTER);
		inputPanel.add(sendButton, BorderLayout.EAST);
		add(inputPanel, BorderLayout.SOUTH);

		this.setVisible(true);

		for (String msg : allMessages) {
			getChatArea().append(channelname + ": " + msg + "\n");
		}

		if (!user.getOwnChannels().contains(this.channelname)) {
			inputField.setEnabled(false);
			sendButton.setEnabled(false);
			saveButton.setEnabled(false);
		}
		try {
			Socket socket = new Socket("localhost", 4000);
			writer = new PrintWriter(socket.getOutputStream(), true);

			ThreadClientChannel threadChannel = new ThreadClientChannel(socket, user, this);
			new Thread(threadChannel).start();

			writer.println(username + ": has joined the channel " + channelname);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Failed to connect to server: " + e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}

		sendButton.addActionListener(e -> {
			String message = inputField.getText();

			if (!message.isEmpty()) {
				allMessages.add(message);
				getChatArea().append(channelname + ": " + message + "\n");
				writer.println(channelname + ": " + message);
				inputField.setText("");
			}
		});

		saveButton.addActionListener(e -> {
			readAndSaveData.saveFileWith(allMessages, "Channels/" + channelName);
		});

	}

	public JTextArea getChatArea() {
		return chatArea;
	}
}
