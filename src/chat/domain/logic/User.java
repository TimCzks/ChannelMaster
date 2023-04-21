package chat.domain.logic;

import java.util.ArrayList;
import java.util.List;

import channels.GUI.ChannelGUI;

public class User {
	private String username;
	private String password;
	private List<ChannelGUI> openChannels;
	private List<String> ownChannels;

	public User(String username) {
		this.username = username;
		openChannels = new ArrayList<>();
		ownChannels = new ArrayList<>();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<ChannelGUI> getOpenChannels() {
		return openChannels;
	}

	public void setOpenChannel(List<ChannelGUI> openChannel) {
		this.openChannels = openChannel;
	}

	public List<String> getOwnChannels() {
		return ownChannels;
	}

	public void setOwnChannels(List<String> ownChannels) {
		this.ownChannels = ownChannels;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}