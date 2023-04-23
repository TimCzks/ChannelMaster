package channels.GUI;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JFrame;

import chat.domain.logic.ReadAndSaveData;
import chat.domain.logic.User;
import chat.masterApp.ChannelMasterApplication;

public class ChannelOverviewGUI {
	private JFrame frame;
	private User user;
	private ReadAndSaveData readAndSaveData = new ReadAndSaveData();

	public ChannelOverviewGUI(User user, ChannelMasterApplication channelMasterApp) {
		this.user = user;
		frame = new JFrame("Overview of all Channels");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setSize(400, 300);

		frame.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = new Insets(10, 0, 10, 0);

		setUpButtons(gbc, channelMasterApp);

		gbc.gridy--;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.HORIZONTAL;

		frame.setVisible(true);
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				channelMasterApp.getSearchButton().setEnabled(true);
			}
		});
	}

	private void setUpButtons(GridBagConstraints gbc, ChannelMasterApplication channelMasterApp) {
		String[] allChannels = readAndSaveData.getAllChannelNames();
		for (String channelname : allChannels) {
			String channelnameWithoutFileEnding = channelname.replace(".txt", "");
			JButton button = new JButton(channelnameWithoutFileEnding);
			button.addActionListener(e -> {
				joinChannel(channelnameWithoutFileEnding);
				channelMasterApp.getSearchButton().setEnabled(true);
			});
			frame.add(button, gbc);
			gbc.gridy++;
		}
	}

	private void joinChannel(String channelname) {
		ChannelGUI channel = new ChannelGUI(user, channelname,
				readAndSaveData.readDataFromFile("Channels/" + channelname));
		user.getOpenChannels().add(channel);
		frame.dispose();
	}
}
