package database;
import login.SessionModel;
import login.UserModel;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

import javax.sql.DataSource;

import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class LoginGateway {
	
	private Connection conn;
	PreparedStatement sRS;
	private ResultSet rs;
	
	

	public LoginGateway() throws GatewayException{
		
		//read the properties file to establish the db connection
		DataSource ds = getDataSource();
		if(ds == null) {
        	throw new GatewayException("Datasource is null!");
        }
		try {
        	conn = ds.getConnection();
		} catch (SQLException e) {
			throw new GatewayException("SQL Error: " + e.getMessage());
		}
	}
	
	public DataSource getDataSource() {
		Properties props = new Properties();
		FileInputStream fis = null;
        try {
        	fis = new FileInputStream("db.properties");
            props.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        try {
        	MysqlDataSource mysqlDS = new MysqlDataSource();
        	mysqlDS.setURL(props.getProperty("MYSQL_DB_URL"));
        	mysqlDS.setUser(props.getProperty("MYSQL_DB_USER"));
        	mysqlDS.setPassword(props.getProperty("MYSQL_DB_PW"));
        	return mysqlDS;
        } catch(RuntimeException e) {
            e.printStackTrace();
            return null;
        }
	}
	
	public SessionModel loadSession(String login, String password) throws SQLException {
		sRS = null;
		rs = null;
		SessionModel found = null;
  		
        sRS = conn.prepareStatement("SELECT * from user "
        		                  + "WHERE email = ? AND password = ?");
        sRS.setString(1,login);
        sRS.setString(2, password);;
        rs = sRS.executeQuery();
        
        while(rs.next()) {
        	
        	String tempName = rs.getString("name");     
        	String tempEmail = rs.getString("email");
        	boolean tempInvMgr = rs.getBoolean("inv_mgr");
        	boolean tempProdMgr = rs.getBoolean("prod_mgr");
        	boolean tempAdmin = rs.getBoolean("admin");
        	UserModel user = new UserModel(tempName, tempEmail);
        	found = new SessionModel(user, tempInvMgr, tempProdMgr, tempAdmin);
        	
        }
        
        return found;
	}
	
}
	
