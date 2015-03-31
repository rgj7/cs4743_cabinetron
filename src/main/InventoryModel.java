package main;

import items.InventoryItemModel;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import login.SessionModel;
import database.DatabaseLockException;
import database.ItemTableGateway;
import database.PartTableGateway;
import database.ProductTemplateGateway;
import database.TemplatePartGateway;
import parts.PartModel;
import templateparts.ProductTemplatePartModel;
import templates.ProductTemplateModel;

public class InventoryModel {
	public static final String[] ITEMFIELDS = {"ITEMID", "PART NUMBER", "PART NAME", "LOCATION", "QUANTITY"};
	public static final String[] PARTFIELDS = {"PARTID", "NUMBER", "NAME", "UNIT", "EXTERNAL", "VENDOR"};
	public static final String[] PRODTEMPFIELDS = {"PTID", "NUMBER", "DESCRIPTION"};
	
	private PartTableGateway partGateway = null; 
	private ItemTableGateway itemGateway = null;
	private ProductTemplateGateway productGateway = null;
	private TemplatePartGateway templatePartGateway = null;
	
	private ArrayList<InventoryItemModel> inventory;
	private ArrayList<PartModel> parts;
	private ArrayList<ProductTemplateModel> productTemplates;
	private ArrayList<ProductTemplatePartModel> prodTempParts;
	private int lastItemID, lastPartID, lastProdTempID;
	private SessionModel session;
	

	public InventoryModel(PartTableGateway ptg, ItemTableGateway itg, ProductTemplateGateway pg, TemplatePartGateway tpg, SessionModel s) {
		this.partGateway = ptg;
		this.itemGateway = itg;
		this.productGateway = pg;
		this.templatePartGateway = tpg;
		this.setSession(s);
		this.prodTempParts = new ArrayList<ProductTemplatePartModel>();
		//prodTempParts.add(new ProductTemplatePartModel(1,1,10));
		
		//loads parts from database
		try {
			this.parts = partGateway.loadParts();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// load inventory from database
		try {
			this.inventory = itemGateway.loadItems();
		} catch (SQLException e) {
			System.out.println("load items exception");
			e.printStackTrace();
		}
		
		try {
			this.productTemplates = productGateway.loadProducts();
		} catch (SQLException e){
			System.out.println("load product templates exception");
			e.printStackTrace();
		}
		

		// get last id
		this.lastItemID = itemGateway.getLastID() + 1;
		this.lastPartID = partGateway.getLastID() + 1;
		this.lastProdTempID = productGateway.getLastID() + 1;
	}
	
	///////////
	// METHODS
	
	// ITEMS
	
	public void addItem(String partNumber, int locationIndex, int quantity) throws SQLException {
		int id = -1;
		Timestamp t = null;
		if(quantity == 0) {
			throw new IllegalArgumentException("Item quantity must be zero or greater.");
		}
		if(this.isUniqueItem(partNumber, locationIndex)) {
			PartModel part = getPartByNumber(partNumber);
			InventoryItemModel inventoryItem = new InventoryItemModel(lastItemID, part, locationIndex, quantity);
			
			id = itemGateway.addItem(partNumber,InventoryItemModel.LOCATIONS[locationIndex], quantity);
			t = this.itemGateway.getTimeStamp(id);
            
			inventoryItem.setTimestamp(t);
			
			inventoryItem.setItemID(id);
			inventory.add(inventoryItem);
			
			// TODO: This could screw up if two items are added at same time??
			this.lastItemID++;
		} else {
			throw new IllegalArgumentException("ITEM in same LOCATION already exists in inventory.");
		}

	}
	
	//needs to throw exception to be caught by controller, and passed on to view, to let them know when item timestamp mismatch (redo)
	public void editItem(int itemID, String partNumber, int locationIndex, int quantity, Timestamp itemTime) throws DatabaseLockException, SQLException {
		Timestamp t = null;
		InventoryItemModel editItem = getItemByID(itemID);
		if(!editItem.getItemPart().getPartNumber().equals(partNumber) || editItem.getItemLocationIndex() != locationIndex) {	
			if(this.isUniqueItem(partNumber, locationIndex)) {
				PartModel part = getPartByNumber(partNumber);
				editItem.setItemPart(part);
				editItem.setItemLocationIndex(locationIndex);
			} else {
				throw new IllegalArgumentException("ITEM in same LOCATION already exists in inventory.");
			}	
		}
		editItem.setItemQuantity(quantity);
		try {
			itemGateway.editItem(itemID, partNumber, InventoryItemModel.LOCATIONS[locationIndex], quantity, itemTime);
		} catch (DatabaseLockException e){
			throw new DatabaseLockException("Item out of Sync. Retry Edit");
		}
		  
		t = this.itemGateway.getTimeStamp(itemID);
		editItem.setTimestamp(t);	
	}
	
	public void deleteItem(int itemID) throws SQLException {
		InventoryItemModel delItem = getItemByID(itemID);
		if(delItem.getItemQuantity() == 0) {
			itemGateway.deleteItem(itemID);
			inventory.remove(delItem);
		} else {
			throw new IllegalArgumentException("QUANTITY must be set to 0 for deletion.");
		}	
	}
	
	// PARTS
	
	public void addPart(String number, String name, int unitIndex, String externalNumber, String vendor) throws SQLException {
		int id = -1;
		if(this.isUniquePart(number)) {
			PartModel part = new PartModel(id, number, name, unitIndex, externalNumber, vendor);		
			id = partGateway.addPart(number,name,PartModel.UNITS[unitIndex],externalNumber, vendor);
			part.setPartID(id);
			parts.add(part);
			// TODO: This could screw up if two parts are added at same time??
			this.lastPartID++;
		} else {
			throw new IllegalArgumentException("PART NUMBER already exists in part list.");
		}
	}
	
	public void editPart(int id, String number, String name, int unitIndex, String externalNumber, String vendor) throws SQLException {
		PartModel editPart = getPartByID(id);
		if(!editPart.getPartNumber().equals(number)) {
			if(isUniquePart(number)) {
				editPart.setPartNumber(number);
			} else {
				throw new IllegalArgumentException("PART NUMBER already exists in part list.");
			}
		}
		editPart.setPartName(name);
		editPart.setPartUnitIndex(unitIndex);
		editPart.setExternalPartNumber(externalNumber);
		editPart.setPartVendor(vendor);
		partGateway.editPart(id, number, name, PartModel.UNITS[unitIndex], externalNumber, vendor);
	}
	
	public void deletePart(int partId) throws SQLException {
		
		partGateway.deletePart(partId);
		
		// TODO: need to deal with deleting part that is referenced in inventory
		// ( db may handle it on its own but would need to resync with db)
		try {
			parts = partGateway.loadParts();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			inventory = itemGateway.loadItems();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	// PRODUCT TEMPLATES
	
	public void addProductTemplate(String name, String desc) {
		int id = -1;
		
		ProductTemplateModel newProdTemp = new ProductTemplateModel(id, name, desc);
		
		try{
			id = productGateway.addTemplate(name, desc);		
		} catch (SQLException e){
			e.printStackTrace();
		}
	    
		newProdTemp.setProductTemplateID(id);
		productTemplates.add(newProdTemp);
	    this.lastProdTempID++;
	}
	
	
	public void editProductTemplate(int prodTempId, String number, String desc) {
		ProductTemplateModel editTemplate = getProductByID(prodTempId);
		editTemplate.setProductTemplateNumber(number);
		editTemplate.setProductTemplateDescription(desc);
		try {
			productGateway.editTemplate(prodTempId, number, desc);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void deleteProductTemplate(int prodTempId) throws SQLException  {
		try {
			productGateway.deleteTemplate(prodTempId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		productTemplates = productGateway.loadProducts();
		
	}
	
	public void addTemplatePart(int temp_id, int part_id, int quant) throws SQLException {
		templatePartGateway.addTemplatePart(temp_id, part_id, quant);
		this.prodTempParts = templatePartGateway.loadTemplateParts(temp_id);
	}
	
	public void editTemplatePart(int temp_id, int part_id, int quant) throws SQLException {
		templatePartGateway.editTemplatePart(temp_id, part_id, quant);
		this.prodTempParts = templatePartGateway.loadTemplateParts(temp_id);
	}
	
	public void deleteTemplatePart(int temp_id, int part_id) throws SQLException {
		templatePartGateway.deleteTemplatePart(temp_id, part_id);
		loadTemplateParts(temp_id);
	}
	
	// UTILITY METHODS
	
	private boolean isUniqueItem(String partNumber, int locationIndex) { // checks for same part/location
		for(InventoryItemModel item : inventory) {
			if(item.getItemPart().getPartNumber().equals(partNumber) && item.getItemLocationIndex() == locationIndex) {
				return false;
			}
		}
		return true;
	}

	private boolean isUniquePart(String partNumber) { // checks for existing part number
		for(PartModel part : parts) {
			if(part.getPartNumber().equals(partNumber)) {
				return false;
			}
		}
		return true;
	}
	
	///////////
	// GETTERS
	
	public InventoryItemModel getInventoryItemByIndex(int index) {
		return this.inventory.get(index);
	}
	
	public PartModel getPartByIndex(int index) {
		return this.parts.get(index);
	}
	
	public ProductTemplateModel getProdTempByIndex(int index) {
		return this.productTemplates.get(index);
	}
	
	public void loadTemplateParts(int index) throws SQLException {
		this.prodTempParts = templatePartGateway.loadTemplateParts(index);
		System.out.println(this.prodTempParts.toString());
	}
	
	public ProductTemplatePartModel getProdTempPartByIndex(int index) {
		return this.prodTempParts.get(index);
	}
	
	public int getProductTemplatePartsSize() {
		if(this.prodTempParts.isEmpty())
			return 0;
		else
			return this.prodTempParts.size();
	}
	
	public void clearProductTemplateParts() {
		this.prodTempParts.clear();
	}
	
	public int getLastItemID() {
		return this.lastItemID;
	}
	
	public int getLastPartID() {
		return this.lastPartID;
	}
	
	public int getLastProductTemplateID() {
		return this.lastProdTempID;
	}
	
	public int getInventorySize() {
		return this.inventory.size();
	}
		
	public int getPartsSize() {
		return this.parts.size();
	}
	
	public int getProductTemplateSize() {
		return this.productTemplates.size();
	}
	
	public InventoryItemModel getItemByID(int id) {
		for(InventoryItemModel item : inventory) {
			if(item.getItemID() == id) {
				return item;
			}
		}
		return null;
	}

	public PartModel getPartByID(int partID) {
		for(PartModel part : parts) {
			if(part.getPartID() == partID) {
				return part;
			}
		}
		return null;
	}
	
	public ProductTemplateModel getProductByID(int productID) {
		for(ProductTemplateModel product : productTemplates) {
			if(product.getProductTemplateID() == productID) {
				return product;
			}
		}
		return null;
	}
	
	
	
	public PartModel getPartByName(String name) {
		for(PartModel part : parts) {
			if(part.getPartName().equals(name)) {
				return part;
			}
		}
		return null;
	}

	public PartModel getPartByNumber(String number) {
		for(PartModel part : parts) {
			if(part.getPartNumber().equals(number)) {
				return part;
			}
		}
		return null;
	}
	
	public String[] getPartNames() {
		String[] partNames = new String[parts.size()];
		for(int i = 0; i<parts.size(); i++) {
			partNames[i] = parts.get(i).getPartName();
		}
		return partNames;
	}
	
	public String[] getPartNumbers() {
		String[] partNumbers = new String[parts.size()];
		for(int i = 0; i<parts.size(); i++) {
			partNumbers[i] = parts.get(i).getPartNumber();
		}
		return partNumbers;
	}
	
	public void reloadInventory(){
		//loads parts from database
				try {
					this.parts = partGateway.loadParts();
					parts.toString();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
				try {
					this.inventory = itemGateway.loadItems();
				} catch (SQLException e) {
					System.out.println("load items exception");
					e.printStackTrace();
				}
				
				try {
					this.productTemplates = productGateway.loadProducts();
				} catch (SQLException e){
					e.printStackTrace();
				}
				
				this.lastItemID = itemGateway.getLastID() + 1; // TODO: get last item id from database
				this.lastPartID = partGateway.getLastID() + 1;
	}

	public SessionModel getSession() {
		return session;
	}

	public void setSession(SessionModel session) {
		this.session = session;
	}
}
