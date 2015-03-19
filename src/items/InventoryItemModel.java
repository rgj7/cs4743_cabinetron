package items;

import java.sql.Timestamp;

import parts.PartModel;

public class InventoryItemModel {
	public static final String[] LOCATIONS = {"Unknown", "Facility 1 Warehouse 1", "Facility 1 Warehouse 2", "Facility 2"};
	
	private int itemId, itemLocationIndex, itemQuantity;
	private PartModel itemPart;
	private Timestamp timestmp;
	
	////////////////
	// CONSTRUCTOR
	
	public InventoryItemModel(int id, PartModel part, int locationIndex, int quantity) {
		this.setItemID(id);
		this.setItemPart(part);
		this.setItemLocationIndex(locationIndex);
		this.setItemQuantity(quantity); // calls initial set method
	}
	
	public InventoryItemModel(int id, PartModel part, int locationIndex, int quantity, Timestamp timestmp) {
		this.setItemID(id);
		this.setItemPart(part);
		this.setItemLocationIndex(locationIndex);
		this.setItemQuantity(quantity); // calls initial set method
		this.timestmp = timestmp;
	}
	
	///////////
	// GETTERS
	
	public int getItemID() {
		return this.itemId;
	}
	
	public PartModel getItemPart() {
		return this.itemPart;
	}
	
	public int getItemLocationIndex() {
		return this.itemLocationIndex;
	}
	
	public String getItemLocation() {
		return LOCATIONS[this.itemLocationIndex];
	}
	
	public int getItemQuantity() {
		return this.itemQuantity;
	}
	
	public Timestamp getTimestamp(){
		return this.timestmp;
	}
	
	///////////
	// SETTERS
	
	public void setItemID(int id) {
			this.itemId = id;
	}
	
	public void setItemPart(PartModel part) {
		if(part == null) {
			throw new NullPointerException("Item's part is null.");
		}
		this.itemPart = part;
	}

	public void setItemLocationIndex(int locationIndex) {
		if(locationIndex == 0) {
			throw new IllegalArgumentException("Location must be set. Cannot be Unknown.");
		} else if(locationIndex < 0 || locationIndex >= LOCATIONS.length) {
			throw new IndexOutOfBoundsException("Location index is out of bounds.");
		}
		this.itemLocationIndex = locationIndex;
	}
	
	public void setItemQuantity(int quantity) {
		if(quantity < 0) {
			throw new IllegalArgumentException("Item quantity must be zero or greater.");
		}
		this.itemQuantity = quantity;
	}

	public void setTimestamp(Timestamp t) {
		this.timestmp = t;
		
	}
}
