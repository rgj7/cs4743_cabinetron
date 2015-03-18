package cabinetron3;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

public class InventoryModel {
	public static final String[] ITEMFIELDS = {"ITEMID", "PART NUMBER", "PART NAME", "LOCATION", "QUANTITY"};
	public static final String[] PARTFIELDS = {"PARTID", "NUMBER", "NAME", "UNIT", "EXTERNAL", "VENDOR"};
	
	PartTableGateway partGateway = null; 
	ItemTableGateway itemGateway = null;
	private ArrayList<InventoryItemModel> inventory;
	private ArrayList<PartModel> parts;
	private int lastItemID, lastPartID;
	PartModel testPart, testPart2, testPart3;
	
	public InventoryModel(PartTableGateway ptg, ItemTableGateway itg) {
		this.partGateway = ptg;
		this.itemGateway = itg;
		this.inventory = new ArrayList<InventoryItemModel>();
		
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
		this.lastItemID = itemGateway.getLastID() + 1; // TODO: get last item id from database
		this.lastPartID = partGateway.getLastID() + 1;
		
		// Debug Loop
		for(InventoryItemModel item : inventory) {
			System.out.format("{%1$d, %2$s, '%3$s', %4$d}\n", item.getItemID(), item.getItemPart().toString(), item.getItemLocation(), item.getItemQuantity());
		}
	}
	
	///////////
	// METHODS
	
	public void addItem(String partNumber, int locationIndex, int quantity) {
		
		int id = -1;
		Timestamp t = null;
		
		if(this.isUniqueItem(partNumber, locationIndex)) {
			PartModel part = getPartByNumber(partNumber);
			InventoryItemModel inventoryItem = new InventoryItemModel(lastItemID, part, locationIndex, quantity);
			
			try {
				id = itemGateway.addItem(partNumber,InventoryItemModel.LOCATIONS[locationIndex], quantity);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			try {
				t = this.itemGateway.getTimeStamp(id);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
			inventoryItem.setTimestamp(t);
			
			inventoryItem.setItemID(id);
			inventory.add(inventoryItem);
	
			this.lastItemID++;
			
		} else {
			throw new IllegalArgumentException("ITEM in same LOCATION already exists in inventory.");
		}

	}
	
	//needs to throw exception to be caught by controller, and passed on to view, to let them know when item timestamp mismatch (redo)
	public void editItem(int itemID, String partNumber, int locationIndex, int quantity, Timestamp itemTime) throws DatabaseLockException {
		
		Timestamp t = null;
		
		if(this.isUniqueItem(partNumber, locationIndex)) {
			PartModel part = getPartByNumber(partNumber);
			InventoryItemModel editItem = getItemByID(itemID);
			editItem.setItemPart(part);
			editItem.setItemLocationIndex(locationIndex);
			editItem.setItemQuantity(quantity);
			
			try {
				itemGateway.editItem(itemID, partNumber, InventoryItemModel.LOCATIONS[locationIndex], quantity, itemTime);
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (DatabaseLockException e){
				throw new DatabaseLockException("Item out of Sync. Retry Edit");
			}
			  
			try {
				t = this.itemGateway.getTimeStamp(itemID);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			editItem.setTimestamp(t);
			
		} else {
			throw new IllegalArgumentException("ITEM in same LOCATION already exists in inventory.");
		}
		
				
	}
	
	public void deleteItem(int itemID) {
		
		try {
			itemGateway.deleteItem(itemID);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		InventoryItemModel delItem = getItemByID(itemID);
		inventory.remove(delItem);
	}
	
	public void addPart(String number, String name, int unitIndex, String externalNumber, String vendor) {

		int id = -1;
		
		if(this.isUniquePart(number)) {
			PartModel part = new PartModel(id, number, name, unitIndex, externalNumber, vendor);		
			
			try {
				id = partGateway.addPart(number,name,PartModel.UNITS[unitIndex],externalNumber, vendor);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			part.setPartID(id);
			parts.add(part);
			this.lastPartID++;
		} else {
			throw new IllegalArgumentException("PART NUMBER already exists in part list.");
		}
	}
	
	public void editPart(int id, String number, String name, int unitIndex, String externalNumber, String vendor) {
		
		if(this.isUniquePart(number)) {
			
			PartModel editPart = getPartByID(id);
			editPart.setPartNumber(number);
			editPart.setPartName(name);
			editPart.setPartUnitIndex(unitIndex);
			editPart.setExternalPartNumber(externalNumber);
			editPart.setPartVendor(vendor);
			
			try {
				partGateway.editPart(id,  number,  name, PartModel.UNITS[unitIndex], externalNumber, vendor);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		} else {
			throw new IllegalArgumentException("PART NUMBER already exists in part list.");
		}
		
	}
	
	public void deletePart(int partId) {
		
		try {
			partGateway.deletePart(partId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		// TODO: need to deal with deleting part that is referenced in inventory
		// ( db may handle it on its own but would need to resync with db)
		try {
			parts = partGateway.loadParts();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			inventory = itemGateway.loadItems();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
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
	
	public int getLastItemID() {
		return this.lastItemID;
	}
	
	public int getLastPartID() {
		return this.lastPartID;
	}
	
	public int getInventorySize() {
		return this.inventory.size();
	}
		
	public int getPartsSize() {
		return this.parts.size();
	}
	
	public InventoryItemModel getItemByID(int id) {
		for(InventoryItemModel item : inventory) {
			if(item.getItemID() == id) {
				return item;
			}
		}
		return null;
	}
	
	public PartModel getPartByIndex(int index) {
		return this.parts.get(index);
	}

	public PartModel getPartByID(int partID) {
		for(PartModel part : parts) {
			if(part.getPartID() == partID) {
				return part;
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
				this.lastItemID = itemGateway.getLastID() + 1; // TODO: get last item id from database
				this.lastPartID = partGateway.getLastID() + 1;
	}
}
