package chat.masterApp;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import channels.GUI.ChannelGUI;
import channels.GUI.ChannelOverviewGUI;
import channels.GUI.ChatClientGUI;
import chat.domain.logic.ReadAndSaveData;
import chat.domain.logic.User;

public class ChannelMasterApplication {

	private JFrame frame;
	private User user;
	private JButton searchForChannelsButton, groupChatButton, createChannelButton;
	private String username;
	private ReadAndSaveData readAndSaveData = new chat.domain.logic.ReadAndSaveData();

	/**
	 * By this constructor the main window is started and shown to the user, through
	 * which they can access all channels (others and their own). Also the group
	 * chat can be entered here.
	 * 
	 * @param user that is created after the login / registration.
	 */
	public ChannelMasterApplication(User user) {
		this.user = user;
		this.username = user.getUsername();
		frame = new JFrame("ChannelMasterApp | Logged in as " + username);
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
				save(user);
			}
		});
	}

	/**
	 * Setting up all the buttons that are shown and giving them actual
	 * functionality.
	 */
	private void setUpButtons() {
		getSearchButton().addActionListener(e -> {
			new ChannelOverviewGUI(user, this);
			getSearchButton().setEnabled(false);
		});
		getCreateButton().addActionListener(e -> {
			createOrOpenOwnChannel();
		});
		getGroupChatButton().addActionListener(e -> {
			ChatClientGUI chatClientGUI = new ChatClientGUI();
			chatClientGUI.initializeGroupChatClient(username, this);
			getGroupChatButton().setEnabled(false);
		});
	}

	/**
	 * Checks, if the entered channelname belongs to the current user and if it
	 * exists in general. According to those results, a new channel is created and
	 * saved OR the channel that is owned by the current user is opened.
	 */
	private void createOrOpenOwnChannel() {
		String channelname = JOptionPane.showInputDialog(frame, "Enter the name of your channel: ");
		String pathToChannel = "Channels/" + channelname;
		boolean userIsOwnerAndFileExistsAlready = (!user.getOwnChannels().contains(channelname) || channelname == null)
				&& readAndSaveData.doesFileExistAlready(pathToChannel);
		if (userIsOwnerAndFileExistsAlready) {
			JOptionPane.showMessageDialog(frame, "Channel is already owned by someone else, please try again.");
			return;
		} else if (channelname == null) {
			JOptionPane.showMessageDialog(frame, "Channelname cannot be empty, please try again.");
			return;
		}
		if (!readAndSaveData.doesFileExistAlready(pathToChannel))
			readAndSaveData.saveFileWith(new ArrayList<String>(), pathToChannel);
		if (!user.getOwnChannels().contains(channelname)) {
			user.getOwnChannels().add(channelname);
		}
		ChannelGUI channel = new ChannelGUI(user, channelname, readAndSaveData.readDataFromFile(pathToChannel));
		user.getOpenChannels().add(channel);
	}

	/**
	 * Saves the user-account with all their own channels as information and their
	 * password on the disk ("Database").
	 * 
	 * @param user that is saved
	 */
	private void save(User user) {
		user.getOwnChannels().add(0, user.getPassword());
		readAndSaveData.saveFileWith(user.getOwnChannels(), "Users/" + username);
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
}
