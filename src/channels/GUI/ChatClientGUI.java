package channels.GUI;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import chat.channels.server.ThreadClientChat;
import chat.masterApp.ChannelMasterApplication;

public class ChatClientGUI {
	private JFrame frame;
	private JTextArea chatTextArea;
	private JTextField inputTextField;
	private JButton sendButton;
	private PrintWriter writer;
	public String username;

	public void initializeGroupChatClient(String username, ChannelMasterApplication channelMasterApp) {
		frame = new JFrame("Chat Client from " + username);
		this.username = username;
		getFrame().setLocationRelativeTo(null);
		getFrame().setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		chatTextArea = new JTextArea();
		getChatTextArea().setEditable(false);
		JScrollPane chatScrollPane = new JScrollPane(getChatTextArea());
		getFrame().add(chatScrollPane, BorderLayout.CENTER);

		inputTextField = new JTextField();
		sendButton = new JButton("Send");
		sendButton.addActionListener(new SendButtonListener());
		JPanel inputPanel = new JPanel(new BorderLayout());
		inputPanel.add(inputTextField, BorderLayout.CENTER);
		inputPanel.add(sendButton, BorderLayout.EAST);
		getFrame().add(inputPanel, BorderLayout.SOUTH);

		getFrame().addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				channelMasterApp.getGroupChatButton().setEnabled(true);
			}
		});

		getFrame().setSize(400, 300);
		getFrame().setVisible(true);

		try {
			Socket socket = new Socket("localhost", 5000);
			writer = new PrintWriter(socket.getOutputStream(), true);

			ThreadClientChat threadClient = new ThreadClientChat(socket, this);
			new Thread(threadClient).start();

			writer.println(username + ": has joined the chat-room.");

		} catch (Exception e) {
			JOptionPane.showMessageDialog(getFrame(), "Failed to connect to server: " + e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}
	}

	public JFrame getFrame() {
		return frame;
	}

	public JTextArea getChatTextArea() {
		return chatTextArea;
	}

	private class SendButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String message = inputTextField.getText();
			if (!message.isEmpty()) {
				getChatTextArea().append("Ich: " + message + "\n");
				writer.println(username + ": " + message);
				inputTextField.setText("");
			}
		}
	}

}
