package login;

import java.sql.SQLException;

import database.GatewayException;
import database.LoginGateway;



public class AuthenticatorModel {

	private LoginGateway loginGateway;
	
	public AuthenticatorModel() {
		
		loginGateway = null;
		try {
			loginGateway = new LoginGateway();		
		} catch(GatewayException e) {
			System.out.println("Error creating DB connection: " + e.getMessage());
			System.exit(0);
		}
		
	}
	
	public SessionModel getSession(String login, String password){
		
		SessionModel found = null;
		
		try {
			found = loginGateway.loadSession(login, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		//found is null if credentials were incorrect (do not exist in table)
		return found;
	}
}
