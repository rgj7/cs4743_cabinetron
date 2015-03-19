package main;

import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class InventoryTableModel extends AbstractTableModel {
	
	private InventoryModel model;
	
	public InventoryTableModel(InventoryModel model) {
		this.model = model;
	}
	
	@Override
	public String getColumnName(int column){
		return InventoryModel.ITEMFIELDS[column];
	}
	
	@Override
	public int getColumnCount() {
		return InventoryModel.ITEMFIELDS.length;
	}

	@Override
	public int getRowCount() {
		return this.model.getInventorySize();
	}

	@Override
	public Object getValueAt(int row, int column) {
		switch(InventoryModel.ITEMFIELDS[column]) {
			case "ITEMID":
				return this.model.getInventoryItemByIndex(row).getItemID();
			case "PART NUMBER":
				return this.model.getInventoryItemByIndex(row).getItemPart().getPartNumber();
			case "PART NAME":
				return this.model.getInventoryItemByIndex(row).getItemPart().getPartName();
			case "LOCATION":
				return this.model.getInventoryItemByIndex(row).getItemLocation();
			case "QUANTITY":
				return this.model.getInventoryItemByIndex(row).getItemQuantity();
			default:
				return null;
		}
	}
	
	@Override
	public boolean isCellEditable(int row, int column) {
		return false; // disable the table cells to be editable
	}
}
