package parts;

import javax.swing.table.AbstractTableModel;

import main.InventoryModel;

@SuppressWarnings("serial")
public class PartTableModel extends AbstractTableModel {
	
	private InventoryModel model;
	
	public PartTableModel(InventoryModel model) {
		this.model = model;
	}
	
	@Override
	public String getColumnName(int column){
		return InventoryModel.PARTFIELDS[column];
	}
	
	@Override
	public int getColumnCount() {
		return InventoryModel.PARTFIELDS.length;
	}

	@Override
	public int getRowCount() {
		return this.model.getPartsSize();
	}

	@Override
	public Object getValueAt(int row, int column) {
		switch(InventoryModel.PARTFIELDS[column]) {
			case "PARTID":
				return this.model.getPartByIndex(row).getPartID();
			case "NUMBER":
				return this.model.getPartByIndex(row).getPartNumber();
			case "NAME":
				return this.model.getPartByIndex(row).getPartName();
			case "UNIT":
				return this.model.getPartByIndex(row).getPartUnit();
			case "EXTERNAL":
				return this.model.getPartByIndex(row).getExternalPartNumber();
			case "VENDOR":
				return this.model.getPartByIndex(row).getPartVendor();		
			default:
				return null;
		}
	}
	
	@Override
	public boolean isCellEditable(int row, int column) {
		return false; // disable the table cells to be editable
	}
}
