package cabinetron3;

import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class ProductTemplateTableModel extends AbstractTableModel {
	
	private InventoryModel model;
	
	public ProductTemplateTableModel(InventoryModel model) {
		this.model = model;
	}
	
	@Override
	public String getColumnName(int column){
		return InventoryModel.PRODTEMPFIELDS[column];
	}
	
	@Override
	public int getColumnCount() {
		return InventoryModel.PRODTEMPFIELDS.length;
	}

	@Override
	public int getRowCount() {
		return this.model.getProductTemplateSize();
	}

	@Override
	public Object getValueAt(int row, int column) {
		switch(InventoryModel.PRODTEMPFIELDS[column]) {
			case "PTID":
				return this.model.getProdTempByIndex(row).getProductTemplateID();
			case "NUMBER":
				return this.model.getProdTempByIndex(row).getProductTemplateNumber();
			case "DESCRIPTION":
				return this.model.getProdTempByIndex(row).getProductTemplateDescription();
			default:
				return null;
		}
	}
	
	@Override
	public boolean isCellEditable(int row, int column) {
		return false; // disable the table cells to be editable
	}
}
