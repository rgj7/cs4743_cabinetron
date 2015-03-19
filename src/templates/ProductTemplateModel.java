package templates;

import java.util.ArrayList;

import templateparts.ProductTemplatePartModel;

public class ProductTemplateModel {

	public static final String[] PARTFIELDS = {"PTID", "PART ID", "PART NAME", "QUANTITY"};
	
	// TODO: create supplemental fields if needed (date_added, last_modified, etc.)
	private int prodTempID;
	private String prodTempNumber, prodTempDesc;
	
	public ProductTemplateModel(int id, String number, String desc) {
		setProductTemplateID(id);
		setProductTemplateNumber(number);
		setProductTemplateDescription(desc);
	}
		
	///////////
	// GETTERS
	
	public int getProductTemplateID() {
		return this.prodTempID;
	}
	
	public String getProductTemplateNumber() {
		return this.prodTempNumber;
	}
	
	public String getProductTemplateDescription() {
		return this.prodTempDesc;
	}
	
	///////////
	// SETTERS
	
	public void setProductTemplateID(int id) {
		this.prodTempID = id;
	}
	
	public void setProductTemplateNumber(String number) {
		if(number == null) {
			throw new NullPointerException("Product template number is null");
		} else if(number.isEmpty()) {
			throw new IllegalArgumentException("Product template number is required");
		} else if(number.length() > 20) {
			throw new IllegalArgumentException("Product template number is too long");
		} else if(!number.startsWith("A")) {
			throw new IllegalArgumentException("Product template number must begin with 'A'");
		}
		this.prodTempNumber = number;
	}
	
	public void setProductTemplateDescription(String desc) {
		if(desc == null) {
			desc = "";
		} else if(desc.length() > 255) {
			throw new IllegalArgumentException("Product template description is too long");
		}
		this.prodTempDesc = desc;
	}
}
