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

import templateparts.ProductTemplatePartModel;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class TemplatePartGateway {
	
	private Connection conn;

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
		PreparedStatement query;
		ResultSet rs;	
		
		ArrayList<ProductTemplatePartModel> templateParts = new ArrayList<ProductTemplatePartModel>();
     		
		query = conn.prepareStatement("SELECT * from template_part "
        		                  + "WHERE template_id = ?");
		query.setInt(1,id);
        rs = query.executeQuery();

        while(rs.next()) {
        	int temp_id = rs.getInt("template_id");     
        	int quant = rs.getInt("quantity");
        	int partId = rs.getInt("part_id");
        	ProductTemplatePartModel i = new ProductTemplatePartModel(temp_id, partId, quant);
        	templateParts.add(i);
        }
        return templateParts;
	}
	
	public ArrayList<ProductTemplatePartModel> loadAllTemplateParts() throws SQLException {
		PreparedStatement query;
		ResultSet rs;	
		
		ArrayList<ProductTemplatePartModel> templateParts = new ArrayList<ProductTemplatePartModel>();
     		
		query = conn.prepareStatement("SELECT * from template_part");
        rs = query.executeQuery();

        while(rs.next()) {
        	int temp_id = rs.getInt("template_id");     
        	int quant = rs.getInt("quantity");
        	int partId = rs.getInt("part_id");
        	ProductTemplatePartModel i = new ProductTemplatePartModel(temp_id, partId, quant);
        	templateParts.add(i);
        }
        return templateParts;
	}
	

	public void addTemplatePart(int temp_id, int part_id, int quant) throws SQLException{
		PreparedStatement query;
		
		query = conn.prepareStatement("insert into template_part (template_id, quantity, part_id) "
				+ " values (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
		query.setInt(1, temp_id);
		query.setInt(2, quant);
		query.setInt(3, part_id);
		query.execute();
	}
	
	public void editTemplatePart(int temp_id, int part_id, int quant) throws SQLException {
		PreparedStatement query;
		query = conn.prepareStatement("UPDATE  template_part" +
		                            "SET quantity = ?, part_id = ?" + 
				                    " WHERE template_id = ?");
		query.setInt(1, quant);
		query.setInt(2, part_id);
		query.setInt(3, temp_id);
		query.executeUpdate();			
	}
	
	public void deleteTemplatePart(int temp_id, int part_id) throws SQLException{
		PreparedStatement query;
		query = conn.prepareStatement("DELETE FROM template_part " +
										"WHERE template_id = ? AND part_id = ?");
		query.setInt(1, temp_id);
		query.setInt(2, part_id);
		query.execute();
	}
	
	
}