package login;

import java.sql.SQLException;

import database.GatewayException;
import database.LoginGateway;



public class AuthenticatorModel {

	private LoginGateway loginGateway;
	private SessionModel session;
	private String login;
	private String pass;
	
	public AuthenticatorModel(SessionModel s) {
		
		this.login = null;
		this.pass = null;
		loginGateway = null;
		this.setSession(s);
		try {
			loginGateway = new LoginGateway();		
		} catch(GatewayException e) {
			System.out.println("Error creating DB connection: " + e.getMessage());
			System.exit(0);
		}
		
	}
	
	public SessionModel loadSession() throws IllegalArgumentException{
		
		
		SessionModel found = null;
		if(this.login == null || this.pass == null){
			throw new IllegalArgumentException("login and pass are not set");
		}
		
		try {
			found = loginGateway.loadSession(this.login, this.pass);
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		//found is null if credentials were incorrect (do not exist in table)
		return found;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public void setSession(SessionModel session) {
		this.session = session;
		
	}
	
	public SessionModel getSession(){
		return this.session;
	}
	
}
