package chat.signin;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import channels.GUI.ChannelGUI;
import chat.domain.logic.ReadAndSaveData;
import chat.domain.logic.User;

public class StartMenu {

	private JFrame frame;
	private User user;
	private JButton searchForChannelsButton, groupChatButton, createChannelButton, logintoChannelButton;
	private String username;
	private List<String> usersOwnChannels;
	private ReadAndSaveData readAndSaveData = new chat.domain.logic.ReadAndSaveData();

	public StartMenu() {
		frame = new JFrame("Startmenu");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(400, 300);
		frame.setLocationRelativeTo(null);

		searchForChannelsButton = new JButton("Search for Channels");
		createChannelButton = new JButton("(Re-)Open own Channel");
		groupChatButton = new JButton("Group Chat");
		setUpButtons();

		frame.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = new Insets(10, 0, 10, 0); // Add padding between buttons

		frame.add(getSearchButton(), gbc);
		gbc.gridy++;
		frame.add(getCreateButton(), gbc);
		gbc.gridy++;
		frame.add(getGroupChatButton(), gbc);
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.HORIZONTAL;

		frame.setVisible(true);
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				readAndSaveData.saveFileWith(usersOwnChannels, "Users/" + username);
			}
		});

		username = JOptionPane.showInputDialog(frame, "Enter your name to Login or Register: ");
		while (username.isEmpty() || username == null) {
			JOptionPane.showMessageDialog(frame, "Username invalid. Please try again.");
		}
		manageLogin();

	}

	private void manageLogin() {
		if (readAndSaveData.doesFileExistAlready("Users/" + username)) {
			usersOwnChannels = readAndSaveData.readDataFromFile("Users/" + username);
		} else {
			readAndSaveData.saveFileWith(new ArrayList<String>(), "Users/" + username);
			usersOwnChannels = readAndSaveData.readDataFromFile("Users/" + username);
		}
		user = new User(username);
		user.setOwnChannels(usersOwnChannels);
		frame.setTitle("Startmenu from " + username);
	}

	private void setUpButtons() {
		getSearchButton().addActionListener(e -> {
			// Open overview of existing channels
		});
		getCreateButton().addActionListener(e -> {
			createOrOpenOwnChannel();
		});
		getGroupChatButton().addActionListener(e -> {
			// Open Groupchat
		});
	}

	private void createOrOpenOwnChannel() {
		String channelName = JOptionPane.showInputDialog(frame, "Enter the name of your channel: ");
		// Solange der Kanal schon existiert, aber zu einem anderen User gehört,
		// verlange neuen Namen
		boolean constraint = true;
		while (constraint) {
			if (user.getOwnChannels().contains(channelName) || channelName == null) {
				constraint = false;
			} else if (readAndSaveData.doesFileExistAlready("Channels/" + channelName)) {
				JOptionPane.showMessageDialog(frame,
						"Channel is already owned by someone else, please use another name.");
				channelName = JOptionPane.showInputDialog(frame, "Enter the name of your channel: ");
			}
		}

		if (channelName == null)
			return;
		readAndSaveData.saveFileWith(new ArrayList<String>(), "Channels/" + channelName);
		// Adde Kanal zum User, wenn er noch nicht existiert
		if (!user.getOwnChannels().contains(channelName)) {
			user.getOwnChannels().add(channelName);
		}
		user.getOpenChannels().add(channelName);
		ChannelGUI channel = new ChannelGUI(user, channelName);
	}

	public JButton getGroupChatButton() {
		return groupChatButton;
	}

	public JButton getCreateButton() {
		return createChannelButton;
	}

	public JButton getSearchButton() {
		return searchForChannelsButton;
	}

	public static void main(String[] args) {
		StartMenu startMenu = new StartMenu();
	}
}
