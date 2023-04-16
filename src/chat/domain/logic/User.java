package chat.domain.logic;

import java.util.ArrayList;
import java.util.List;

public class User {
	private String username;
	private List<String> openChannels;
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

	public List<String> getOpenChannels() {
		return openChannels;
	}

	public void setOpenChannel(List<String> openChannel) {
		this.openChannels = openChannel;
	}

	public List<String> getOwnChannels() {
		return ownChannels;
	}

	public void setOwnChannels(List<String> ownChannels) {
		this.ownChannels = ownChannels;
	}

}
