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

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import database.DatabaseLockException;

public class ItemTableGateway {
	
	private Connection conn;
	private PartTableGateway partGateway;
	private int lastID;
	
	public ItemTableGateway(PartTableGateway partGateway) throws GatewayException {
		// read the properties file to establish the db connection
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
	
	
	public ArrayList<InventoryItemModel> loadItems() throws SQLException {
		PreparedStatement q;
		ResultSet rs;

		ArrayList<InventoryItemModel> items = new ArrayList<InventoryItemModel>();
     		
        q = conn.prepareStatement("SELECT * FROM item");
        rs = q.executeQuery();
        
        while(rs.next()) {
        	PartModel part = null;
        	// get values from db
        	int id = rs.getInt("id");
        	String location = rs.getString("location");
        	int quantity = rs.getInt("quantity");
        	int part_id = rs.getInt("part_id");
        	if(part_id != 0) {
        		part = partGateway.getPart(part_id);
        	}
        	int template_id = rs.getInt("template_id");
        	Timestamp time = rs.getTimestamp("time");
        	// create model objects
        	InventoryItemModel item = new InventoryItemModel(id, part, template_id, Arrays.asList(InventoryItemModel.LOCATIONS).indexOf(location), quantity, time);
        	items.add(item);
		    this.lastID = id;
        }
        return items;
	}
	
	public int getLastID() {
		return this.lastID;
	}
	
	public Timestamp getTimeStamp(int id) throws SQLException {
		PreparedStatement q;
		ResultSet rs;
		
	    q = conn.prepareStatement("SELECT * FROM item WHERE id = ?");
	    q.setInt(1, id);
	    rs = q.executeQuery();
	    
	    rs.first();
	    Timestamp ts = rs.getTimestamp("time");
	    return ts;
	}
	
	public int addItem(String part_num, String location, int quantity) throws SQLException {
		PreparedStatement q;
		ResultSet rs;
		
		int id = -1;
	    int part_id = partGateway.getPartId(part_num);
	    
	    q = conn.prepareStatement("insert into item (location, quantity, part_id) "
				+ " values (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
		q.setString(1, location);
		q.setInt(2, quantity);
		q.setInt(3, part_id);
		q.execute();
		
		rs = q.getGeneratedKeys();
		
		while(rs.next())
			id = rs.getInt(1);
	
		return id;
	}
	
	public int addItemProduct(int template_id, String location, int quantity) throws SQLException {
		PreparedStatement q;
		ResultSet rs;
		
		int id = -1;
	    
	    q = conn.prepareStatement("insert into item (location, quantity, template_id) "
				+ " values (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
		q.setString(1, location);
		q.setInt(2, quantity);
		q.setInt(3, template_id);
		q.execute();
		
		rs = q.getGeneratedKeys();
		
		while(rs.next())
			id = rs.getInt(1);
	
		return id;
	}
	
	public void editItem(int id, String part_num, String location, int quantity,
			             Timestamp timestmp) throws SQLException, DatabaseLockException{
		int count = 0;
		PreparedStatement q;
		
		System.out.println(timestmp.toString()); //TESTING
		System.out.println(this.getTimeStamp(id).toString()); //TESTING
		int part_id = partGateway.getPartId(part_num);
		q = conn.prepareStatement("UPDATE item " +
		                            "SET location = ?, quantity = ?, part_id = ?" + 
				                    " WHERE id = ? AND time = ?");
		q.setString(1, location);
		q.setInt(2, quantity);
		q.setInt(3, part_id);
		q.setInt(4, id);
		q.setTimestamp(5, timestmp);
		count = q.executeUpdate();
		
		if ( count < 1){
			throw new DatabaseLockException("Item out of Sync. Retry Item Edit");
		}
		
	}
	
	public void deleteItem(int id) throws SQLException{
		PreparedStatement q;

		q = conn.prepareStatement("DELETE FROM item WHERE id = ?");
		q.setInt(1, id);
		q.execute();
	}
	
	
}