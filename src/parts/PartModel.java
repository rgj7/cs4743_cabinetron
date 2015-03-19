package parts;

public class PartModel {
	public static final String[] UNITS = {"Unknown", "Linear Feet", "Pieces"};
	
	private int partID, partUnitIndex;
	private String partNumber, partName, partExternalNumber, partVendor;

	////////////////
	// CONSTRUCTOR
	
	public PartModel(int id, String number, String name, int unitIndex, String externalNumber, String vendor) {
		this.setPartID(id);
		this.setPartNumber(number);
		this.setPartName(name);
		this.setPartUnitIndex(unitIndex);
		this.setExternalPartNumber(externalNumber);
		this.setPartVendor(vendor);
		
		
		
	}
	
	///////////
	// GETTERS
	
    public int getPartID() {
    	return this.partID;
    }
	public String getPartNumber() {
		return this.partNumber;
	}
	
	public String getExternalPartNumber(){
		return this.partExternalNumber;
	}
	
	public String getPartName() {
		return this.partName;
	}
	
	public String getPartVendor() {
		return this.partVendor;
	}
	
	public String getPartUnit() {
		return UNITS[this.partUnitIndex];
	}
	
	public int getPartUnitIndex() {
		return this.partUnitIndex;
	}
		
	///////////
	// SETTERS
	
	public void setPartID(int id) {
			this.partID = id;
	}
	
	public void setPartNumber(String number) {
		if(number == null) {
			throw new NullPointerException("Part number is null");
		} else if(number.isEmpty()) {
			throw new IllegalArgumentException("Part number is required");
		} else if(number.length() > 20) {
			throw new IllegalArgumentException("Part number is too long");
		} else if(!number.startsWith("P")) {
			throw new IllegalArgumentException("Part number must begin with 'P'");
		}
		// TODO: check for alpha/symbols
		this.partNumber = number;
	}
	
	public void setExternalPartNumber(String externalNumber) {
		if(externalNumber == null){
			externalNumber = "";
		} else if(externalNumber.length() > 50){
			throw new IllegalArgumentException("External part number is too long");
		}
		this.partExternalNumber = externalNumber;
	}
	
	public void setPartName(String name) {
		if(name == null) {
			throw new NullPointerException("Part name is null");
		} else if(name.isEmpty()) {
			throw new IllegalArgumentException("Part name is required");
		} else if(name.length() > 255) {
			throw new IllegalArgumentException("Part name is too long");
		}
		// TODO: check for alpha/symbols
		this.partName = name;
	}
	
	public void setPartVendor(String vendor) {
		if(vendor == null) {
			vendor = "";
		} else if(vendor.length() > 255) {
			throw new IllegalArgumentException("Part vendor is too long");
		}
		// TODO: check for alpha/symbols
		this.partVendor = vendor;
	}
	
	public void setPartUnitIndex(int unitIndex) {
		if(unitIndex == 0) {
			throw new IllegalArgumentException("Unit of quantity must be set. Cannot be Unknown.");
		} else if(unitIndex < 0 || unitIndex >= UNITS.length) {
			throw new IndexOutOfBoundsException("Unit of quantity index is out of bounds.");
		}
		this.partUnitIndex = unitIndex;
	}
	
	///////////
	// UTILITY
	
	public String toString(){
		return String.format("{%1$d, '%2$s', '%3$s', '%4$s', '%5$s', '%6$s'}", this.getPartID(), this.getPartNumber(), this.getPartName(), this.getExternalPartNumber(), this.getPartUnit(), this.getPartVendor());
	}
}
