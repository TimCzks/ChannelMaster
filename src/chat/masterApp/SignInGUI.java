package chat.masterApp;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * @author Tim Czerkas
 * @author Monika Grigorova
 *
 */
public class SignInGUI {
	private JFrame frame;
	private JLabel welcomeLabel;
	private JButton loginButton;
	private JButton registerButton;

	public static void main(String[] args) {
		new SignInGUI();
	}

	/**
	 * The SignInGUI starts the program and displays two options for the user: to
	 * login or to register themself. According to the chosen option, the user gets
	 * the corresponding window shown.
	 *
	 */
	public SignInGUI() {
		frame = new JFrame();
		frame.setTitle("Sign in at ChannelMaster");
		frame.setSize(300, 200);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setLocationRelativeTo(null);

		frame.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = new Insets(10, 0, 10, 0);

		welcomeLabel = new JLabel("Welcome! Do you have an Account already?");
		welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		loginButton = new JButton("Login");
		loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		loginButton.addActionListener(e -> {
			frame.dispose();
			showLoginWindow();
		});
		registerButton = new JButton("Register");
		registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		registerButton.addActionListener(e -> {
			frame.dispose();
			showRegisterWindow();
		});

		frame.add(welcomeLabel, gbc);
		gbc.gridy++;
		frame.add(loginButton, gbc);
		gbc.gridy++;
		frame.add(registerButton, gbc);
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.HORIZONTAL;

		frame.setVisible(true);
	}

	private void showLoginWindow() {
		new LoginGUI();
	}

	private void showRegisterWindow() {
		new RegisterGUI();
	}
}