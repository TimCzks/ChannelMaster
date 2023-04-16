package channels.GUI;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import chat.channels.server.ThreadClientChannel;
import chat.domain.logic.User;

public class ChannelGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private String username;
	private String channelname;
	private JTextArea chatArea;
	private JTextField inputField;
	private JButton sendButton;
	private PrintWriter writer;

	public ChannelGUI(User user, String channelName) {
		this.username = user.getUsername();
		this.channelname = channelName;

		setTitle("Channel: " + channelName);
		setSize(400, 300);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		chatArea = new JTextArea();
		chatArea.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(chatArea);
		add(scrollPane, BorderLayout.CENTER);

		inputField = new JTextField();
		sendButton = new JButton("Send");

		JPanel inputPanel = new JPanel();
		inputPanel.setLayout(new BorderLayout());
		inputPanel.add(inputField, BorderLayout.CENTER);
		inputPanel.add(sendButton, BorderLayout.EAST);
		add(inputPanel, BorderLayout.SOUTH);
		this.setVisible(true);

		if (!user.getOwnChannels().contains(this.channelname)) {
			inputField.setEnabled(false);
			sendButton.setEnabled(false);
		}
		try {

			Socket socket = new Socket("localhost", 4000);
			writer = new PrintWriter(socket.getOutputStream(), true);

			ThreadClientChannel threadChannel = new ThreadClientChannel(socket, chatArea, user);
			new Thread(threadChannel).start();

			writer.println(username + ": has joined the channel.");
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		sendButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String message = inputField.getText();
				if (!message.isEmpty()) {
					chatArea.append(channelname + ": " + message + "\n");
					writer.println(channelname + ": " + message);
					inputField.setText("");
				}
			}
		});

	}
}
