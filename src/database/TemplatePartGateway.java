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

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import database.DatabaseLockException;

public class TemplatePartGateway {
	
	private Connection conn;
	PreparedStatement sRS;
	private ResultSet rs;
	private PartTableGateway partGateway;
	

	public TemplatePartGateway() throws GatewayException{
		
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
		this.partGateway = partGateway;
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
	
	
	public ArrayList<ProductTemplatePartModel> loadTemplateParts(int id) throws SQLException {
		sRS = null;
		rs = null;
		ArrayList<ProductTemplatePartModel> templateParts = new ArrayList<ProductTemplatePartModel>();
     		
        sRS = conn.prepareStatement("SELECT * from template_part "
        		                  + "WHERE template_id = ?");
        sRS.setInt(1,id);
        rs = sRS.executeQuery();
        rs.first();
       
    	boolean moreTParts = false;
        do{
        	ProductTemplatePartModel i = null;
        	int temp_id = rs.getInt("template_id");     
        	int quant = rs.getInt("quantity");
        	int partId = rs.getInt("part_id");
        	i = new ProductTemplatePartModel(temp_id, partId, quant);
        	templateParts.add(i);
		    moreTParts = rs.next();
        } while(moreTParts);
        
        return templateParts;
	}
	

	public void addTemplatePart(int temp_id, int part_id, int quant) throws SQLException{
		sRS = null;
		rs = null;
	    sRS = conn.prepareStatement("insert into template_part (template_id, quantity, part_id) "
				+ " values (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
		sRS.setInt(1, temp_id);
		sRS.setInt(2, quant);
		sRS.setInt(3, part_id);
		sRS.execute();
	}
	
	public void editTemplatePart(int temp_id, int part_id, int quant) throws SQLException, DatabaseLockException{

		sRS = null;
		sRS = conn.prepareStatement("UPDATE  template_part" +
		                            "SET quantity = ?, part_id = ?" + 
				                    " WHERE template_id = ?");
		sRS.setInt(1, quant);
		sRS.setInt(2, part_id);
		sRS.setInt(3, temp_id);
		sRS.executeUpdate();			
	}
	
	public void deleteTemplatePart(int temp_id) throws SQLException{
		sRS = null;
		sRS = conn.prepareStatement("DELETE FROM template_part WHERE template_id = ?");
		sRS.setInt(1, temp_id);
		sRS.execute();
	}
	
	
}