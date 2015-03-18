package cabinetron3;
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

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class ItemTableGateway {
	
	private Connection conn;
	PreparedStatement sRS;
	private ResultSet rs;
	private PartTableGateway partGateway;
	private int lastID;

	

	public ItemTableGateway(PartTableGateway partGateway) throws GatewayException{
		
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
	
	
	public ArrayList<InventoryItemModel> loadItems() throws SQLException {
		sRS = null;
		rs = null;
		ArrayList<InventoryItemModel> items= new ArrayList<InventoryItemModel>();
     		
        sRS = conn.prepareStatement("select * from item");
        rs = sRS.executeQuery();
        rs.first();
       
    	boolean moreItems = false;
        do{
        	InventoryItemModel i = null;
        	PartModel p = null;
        	int id = rs.getInt("id");
        	String location = rs.getString("location");
        	int quant = rs.getInt("quantity");
        	int partId = rs.getInt("part_id");
        	p = partGateway.getPart(partId);
        	Timestamp time = rs.getTimestamp("time");
        	i = new InventoryItemModel(id, p, Arrays.asList(InventoryItemModel.LOCATIONS).indexOf(location), quant, time);
        	items.add(i);
		    moreItems = rs.next();
		    this.lastID = id;
        } while(moreItems);
        
        return items;
	}
	
	public int getLastID(){
		return this.lastID;
	}
	
	public Timestamp getTimeStamp(int id) throws SQLException{
		sRS = null;
		rs = null;
	    sRS = conn.prepareStatement("SELECT * from item " +
		                            "WHERE id = ?");
	    sRS.setInt(1, id);
	    rs = sRS.executeQuery();
	    rs.first();
	    Timestamp t = rs.getTimestamp("time");
	    return t;
	}
	
	public int addItem(String part_num, String location, int quantity) throws SQLException{
		sRS = null;
		rs = null;
		int id = -1;
	    int part_id = partGateway.getPartId(part_num);
	    sRS = conn.prepareStatement("insert into item (location, quantity, part_id) "
				+ " values (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
		sRS.setString(1, location);
		sRS.setInt(2, quantity);
		sRS.setInt(3, part_id);
		sRS.execute();
		rs = sRS.getGeneratedKeys();
		while(rs.next())
			id = rs.getInt(1);
	
		return id;
	}
	
	public void editItem(int id, String part_num, String location, int quantity,
			             Timestamp timestmp) throws SQLException, DatabaseLockException{
		int count = 0;
		sRS = null;
		
		System.out.println(timestmp.toString()); //TESTING
		System.out.println(this.getTimeStamp(id).toString()); //TESTING
		int part_id = partGateway.getPartId(part_num);
		sRS = conn.prepareStatement("UPDATE item " +
		                            "SET location = ?, quantity = ?, part_id = ?" + 
				                    " WHERE id = ? AND time = ?");
		sRS.setString(1, location);
		sRS.setInt(2, quantity);
		sRS.setInt(3, part_id);
		sRS.setInt(4, id);
		sRS.setTimestamp(5, timestmp);
		count = sRS.executeUpdate();
		
		if ( count < 1){
			throw new DatabaseLockException("Item out of Sync. Retry Item Edit");
		}
		
	}
	
	public void deleteItem(int id) throws SQLException{
		sRS = null;
		sRS = conn.prepareStatement("DELETE FROM item WHERE id = ?");
		sRS.setInt(1, id);
		sRS.execute();
	}
	
	
}