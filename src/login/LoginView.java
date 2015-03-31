package login;

import java.awt.Component;
import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class LoginView extends JFrame {
	
	private JPanel formPanel;
	private JTextField userLogin, pass;
	private JComboBox<String> partUnitComboBox;
	
	////////////////
	// CONSTRUCTOR
	
	
	//TO DO: NEED TO MAKE SO IF WINDOW MANUALLY CLOSED, EXITS PROGRAM
	public LoginView() {
		super("Login");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		MigLayout layout = new MigLayout("wrap 2", "[right]10[fill, 150]", "[]10[]");
		formPanel = new JPanel(layout);
		
		
		// Field: USER LOGIN
		userLogin = new JTextField();
		JLabel userLoginLabel = new JLabel("User Login");
		userLoginLabel.setLabelFor(userLogin);

		// Field: PASSWORD
		pass = new JTextField();
		JLabel password = new JLabel("Password");
		password.setLabelFor(pass);

		
		// add all labels and text fields
		formPanel.add(userLoginLabel, "");
		formPanel.add(userLogin, "");
		formPanel.add(password, "");
		formPanel.add(pass, "");

		JButton loginButton = new JButton("Login");
		formPanel.add(loginButton, "skip, split 2");
        
		JButton guestButton = new JButton("Guest");
		formPanel.add(guestButton, "skip, split 2");
		
		add(formPanel);
		pack(); // sizes window to fit components
		setLocationRelativeTo(null); // centers window
		setVisible(true);
	}
	
	public void registerListeners(LoginController controller1) {
		Component[] components = formPanel.getComponents();
		for(Component component : components) {
			if(component instanceof AbstractButton) {
				AbstractButton button = (AbstractButton) component;
				button.addActionListener(controller1);
			}
		}
	}
	
	public void close() {
		this.dispose();
	}
	
	/////////////
	// GETTERS

	public String getLogin() {
		return userLogin.getText();
	}

	public String getPass() {
		return pass.getText();
	}

}
