package database;
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

import parts.PartModel;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class PartTableGateway {
	
	private Connection conn;
	PreparedStatement sRS;
	private ResultSet rs;
	private int lastID;
	

	public PartTableGateway() throws GatewayException{
		
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
	
	public ArrayList<PartModel> loadParts() throws SQLException {
		sRS = null;
		rs = null;
		ArrayList<PartModel> parts= new ArrayList<PartModel>();
     
        sRS = conn.prepareStatement("select * from part");
        rs = sRS.executeQuery();
        rs.first();

    	boolean moreParts = false;
        do{
        	PartModel p = null;
        	int unitIndex = -1;
        	int id = rs.getInt("id");
        	String num = rs.getString("part_number");
        	String name = rs.getString("part_name");
        	String unit = rs.getString("unit_quantity");
        	String exnum = rs.getString("external_part_num");
        	String vend = rs.getString("vendor");
        	if(unit.equals("Unknown")) unitIndex = 0;
        	if(unit.equals("Linear Feet")) unitIndex = 1;
        	if(unit.equals("Pieces")) unitIndex = 2;
        	p = new PartModel(id, num, name, unitIndex, exnum, vend);
        	parts.add(p);
		    moreParts = rs.next();
		    this.lastID = id;
        } while(moreParts);
        
        return parts;
	}
	
	public int getLastID(){
		return this.lastID;
	}
	
	public PartModel getPart(int part_id) throws SQLException {
		sRS = null;
		rs = null;
		PartModel p = null; 
		int unitIndex = -1;
		sRS = conn.prepareStatement("select * from part where id = ?");
		sRS.setInt(1, part_id);
		rs = sRS.executeQuery();
		rs.first();
		String num = rs.getString("part_number");
    	String name = rs.getString("part_name");
    	String unit = rs.getString("unit_quantity");
    	String exnum = rs.getString("external_part_num");
    	String vend = rs.getString("vendor");
    	if(unit.equals("Unknown")) unitIndex = 0;
    	if(unit.equals("Linear Feet")) unitIndex = 1;
    	if(unit.equals("Pieces")) unitIndex = 2;
    	p = new PartModel(part_id, num, name, unitIndex, exnum, vend);
		return p;
		
	}
	
	public int getPartId(String part_num) throws SQLException{
		sRS = null;
		rs = null;
		sRS = conn.prepareStatement("select * from part where part_number = ?");
		sRS.setString(1,part_num);
		rs = sRS.executeQuery();
		rs.first();
		int id = rs.getInt("id");
		System.out.println("getPartID = " + id);
		return id;
	}
		
	public int addPart(String number,String name,String unit, String externalNumber, String vendor) throws SQLException{
		sRS = null;
		rs = null;
		int id = -1;
		sRS = conn.prepareStatement("insert into part (part_number, part_name, unit_quantity, external_part_num, vendor) "
				+ " values (?, ? , ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
		sRS.setString(1, number);
		sRS.setString(2, name);
		sRS.setString(3, unit);
		sRS.setString(4, externalNumber);
		sRS.setString(5, vendor);
		sRS.execute();
		rs = sRS.getGeneratedKeys();
		while(rs.next())
			id = rs.getInt(1);
	
		return id;
	}
	
	public void editPart(int id, String num, String name, String unit, String exnum, String vendor) throws SQLException{
		sRS = null;
		sRS = conn.prepareStatement("UPDATE part " +
		                            "SET part_number = ?, part_name = ?,unit_quantity = ?," 
				                     + " external_part_num = ?, vendor = ?" +
		                            " WHERE id = ?");
		sRS.setString(1, num);
		sRS.setString(2, name);
		sRS.setString(3, unit);
		sRS.setString(4, exnum);
		sRS.setString(5, vendor);
		sRS.setInt(6, id);
		sRS.execute();
		
	}
	
	public void deletePart(int id) throws SQLException{
		sRS = null;
		sRS = conn.prepareStatement("DELETE FROM part WHERE id = ?");
		sRS.setInt(1, id);
		sRS.execute();
	}
	
}
