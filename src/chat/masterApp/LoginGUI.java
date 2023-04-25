package chat.masterApp;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import chat.domain.logic.ReadAndSaveData;
import chat.domain.logic.User;

public class LoginGUI {
	private JFrame frame;
	private JLabel usernameLabel;
	private JTextField usernameField;
	private JLabel passwordLabel;
	private JPasswordField passwordField;
	private JButton loginButton;
	private ReadAndSaveData readAndSaveData = new ReadAndSaveData();

	/**
	 * Shows the GUI for the login process, meaning 2 textfields for the username
	 * and password and one button to submit the login data.
	 * 
	 */
	public LoginGUI() {
		frame = new JFrame();
		frame.setTitle("Login at ChannelMaster");
		frame.setSize(300, 200);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setLayout(new BorderLayout());
		JPanel grid = new JPanel(new GridLayout(2, 2, 5, 5));

		usernameLabel = new JLabel("Username:");
		usernameField = new JTextField();
		usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		usernameField.setAlignmentX(Component.CENTER_ALIGNMENT);

		passwordLabel = new JLabel("Password:");
		passwordField = new JPasswordField();
		passwordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);

		loginButton = new JButton("Login");
		loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		loginButton.addActionListener(e -> {
			login();
		});

		grid.add(usernameLabel);
		grid.add(usernameField);
		grid.add(passwordLabel);
		grid.add(passwordField);
		frame.add(grid, BorderLayout.NORTH);
		frame.add(loginButton, BorderLayout.SOUTH);
		frame.setVisible(true);
	}

	/**
	 * Validates the data that is given, when submitting by pressing the button. If
	 * the user does not exist or the password is wrong, the user gets informed and
	 * can try again. If everything succeeds, a user object with the data from the
	 * disk is created, which is passed to the ChannelMasterApplication then.
	 * 
	 */
	private void login() {
		String username = usernameField.getText();
		String password = new String(passwordField.getPassword());
		usernameField.setText("");
		passwordField.setText("");
		boolean userDoesNotExist = !readAndSaveData.doesFileExistAlready("Users/" + username);
		if (userDoesNotExist) {
			JOptionPane.showMessageDialog(frame,
					"User does not exist! Please pay attention to the spelling and try again.");
		} else {
			List<String> userInfos = new ArrayList<>();
			userInfos.addAll(readAndSaveData.readDataFromFile("Users/" + username));
			if (!userInfos.get(0).equals(password)) {
				passwordField.setText("");
				JOptionPane.showMessageDialog(frame, "Password is incorrect. Please try again.");
			} else {
				User user = new User(username);
				user.getOwnChannels().addAll(userInfos);
				user.setPassword(password);
				user.getOwnChannels().remove(password);
				new ChannelMasterApplication(user);
				frame.dispose();
			}
		}
	}
}
