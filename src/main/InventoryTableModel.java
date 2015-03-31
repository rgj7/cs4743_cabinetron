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
			case "PART #":
				if(this.model.getInventoryItemByIndex(row).getItemProductTemplateID() == 0) {
					return this.model.getInventoryItemByIndex(row).getItemPart().getPartNumber();
				} else {
					return "----";
				}
			case "PROD #":
				if(this.model.getInventoryItemByIndex(row).getItemProductTemplateID() != 0) {
					return this.model.getInventoryItemByIndex(row).getItemProductTemplateID();
				} else {
					return "----";
				}
			case "PART/PROD NAME":
				if(this.model.getInventoryItemByIndex(row).getItemProductTemplateID() != 0) {
					return model.getProductByID(model.getInventoryItemByIndex(row).getItemProductTemplateID()).getProductTemplateNumber();
				} else {
					return model.getInventoryItemByIndex(row).getItemPart().getPartName();
				}
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
