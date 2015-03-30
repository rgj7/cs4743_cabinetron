package login;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;


public class LoginController implements ActionListener {

	private AuthenticatorModel authenticator;
    private LoginView loginView;
    private boolean actionOccured;
	////////////////
	// CONSTRUCTOR
	public LoginController(AuthenticatorModel auth, LoginView lview) {
       this.authenticator = auth;
       this.loginView = lview;
       actionOccured = false; 
	}
	
	///////////////
	// ActionLister Methods
	public boolean hasActionOccured(){
		return this.actionOccured;
	}
	@Override
	public void actionPerformed(ActionEvent event) {
		String command = event.getActionCommand();
		if(command.equals("Login")) {
			
				authenticator.setLogin(loginView.getLogin());
				authenticator.setPass(loginView.getPass());
				authenticator.setSession(authenticator.loadSession());
				this.actionOccured = true;
				loginView.close();

		}
	}
}