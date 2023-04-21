package chat.masterApp;

import java.awt.BorderLayout;
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

public class RegisterGUI {
	private JFrame frame;
	private JLabel usernameLabel;
	private JTextField usernameField;
	private JLabel passwordLabel;
	private JPasswordField passwordField;
	private JButton loginButton;
	private ReadAndSaveData readAndSaveData = new ReadAndSaveData();

	public RegisterGUI() {
		frame = new JFrame();
		frame.setTitle("Register at ChannelMaster");
		frame.setSize(300, 200);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setLayout(new BorderLayout());
		JPanel grid = new JPanel(new GridLayout(2, 2, 5, 5));

		usernameLabel = new JLabel("Username:");
		usernameField = new JTextField();
		passwordLabel = new JLabel("Password:");
		passwordField = new JPasswordField();

		loginButton = new JButton("Register");
		loginButton.addActionListener(e -> {
			register();
		});

		grid.add(usernameLabel);
		grid.add(usernameField);
		grid.add(passwordLabel);
		grid.add(passwordField);
		frame.add(grid, BorderLayout.NORTH);
		frame.add(loginButton, BorderLayout.SOUTH);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	private void register() {
		String username = usernameField.getText();
		String password = new String(passwordField.getPassword());
		usernameField.setText("");
		passwordField.setText("");
		boolean userDoesExistAlready = readAndSaveData.doesFileExistAlready("Users/" + username);
		if (userDoesExistAlready) {
			JOptionPane.showMessageDialog(frame, "User exists already. Choose another one and try again.");
		} else {
			List<String> passwordInList = new ArrayList<>();
			passwordInList.add(password);
			readAndSaveData.saveFileWith(passwordInList, "Users/" + username);
			frame.dispose();
			new ChannelMasterApplication(new User(username));
		}
	}
}
