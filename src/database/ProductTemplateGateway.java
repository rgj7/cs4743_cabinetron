package database;
import items.InventoryItemModel;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

import javax.sql.DataSource;

import parts.PartModel;
import templateparts.ProductTemplatePartModel;
import templates.ProductTemplateModel;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import database.DatabaseLockException;

public class ProductTemplateGateway {
	
	private Connection conn;
	PreparedStatement sRS;
	private ResultSet rs;
	private int lastID;
	
	public ProductTemplateGateway() throws GatewayException{
		
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
	
	
	public ArrayList<ProductTemplateModel> loadProducts() throws SQLException {
		sRS = null;
		rs = null;
		ArrayList<ProductTemplateModel> templates = new ArrayList<ProductTemplateModel>();
     		
        sRS = conn.prepareStatement("SELECT * from template");
        rs = sRS.executeQuery();
        rs.first();
       
    	boolean moreTemplates = false;
        do{
        	ProductTemplateModel i = null;
        	int id = rs.getInt("id");     
        	String number= rs.getString("product_number");
        	String description = rs.getString("description");
        	i = new ProductTemplateModel(id, number, description);
        	templates.add(i);
		    moreTemplates = rs.next();
		    this.lastID = id;
        } while(moreTemplates);
        
        return templates;
	}
	
	public int getLastID(){
		return this.lastID;
	}
	

	public int addTemplate(String number, String desc) throws SQLException{
		sRS = null;
		rs = null;
		int id = -1;
	    sRS = conn.prepareStatement("insert into template (product_number, description) "
				+ " values (?, ?)", Statement.RETURN_GENERATED_KEYS);
		sRS.setString(1, number);
		sRS.setString(2, desc);
		sRS.execute();
		rs = sRS.getGeneratedKeys();
		while(rs.next())
			id = rs.getInt(1);
		return id;
	}
	
	public void editTemplate(int temp_id, String number, String desc) throws SQLException{

		sRS = null;
		sRS = conn.prepareStatement("UPDATE  template " +
		                            "SET product_number = ?, description = ?" + 
				                    " WHERE id = ?");
		sRS.setString(1, number);
		sRS.setString(2, desc);
		sRS.setInt(3, temp_id);
		sRS.executeUpdate();			
	}
	
	public void deleteTemplate(int temp_id) throws SQLException{
		sRS = null;
		sRS = conn.prepareStatement("DELETE FROM template WHERE id = ?");
		sRS.setInt(1, temp_id);
		sRS.execute();
	}
}
	