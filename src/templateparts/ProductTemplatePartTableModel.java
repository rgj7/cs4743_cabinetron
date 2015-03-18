package templateparts;

import javax.swing.table.AbstractTableModel;

import templates.ProductTemplateModel;
import main.InventoryModel;

@SuppressWarnings("serial")
public class ProductTemplatePartTableModel extends AbstractTableModel {

	private ProductTemplateModel model;
	private InventoryModel inventoryModel;
	
	public ProductTemplatePartTableModel(InventoryModel inventoryModel, ProductTemplateModel model){
		this.model = model;
		this.inventoryModel = inventoryModel;
	}
	
	@Override
	public String getColumnName(int column){
		return ProductTemplateModel.PARTFIELDS[column];
	}
	
	@Override
	public int getColumnCount() {
		return ProductTemplateModel.PARTFIELDS.length;
	}

	@Override
	public int getRowCount() {
		return this.model.getProductTemplatePartsSize();
	}

	@Override
	public Object getValueAt(int row, int column) {
		switch(ProductTemplateModel.PARTFIELDS[column]) {
			case "PTID":
				return this.model.getProdTempPartByIndex(row).getProductTemplateID();
			case "PART ID":
				return this.model.getProdTempPartByIndex(row).getPartID();
			case "PART NAME":
				return this.inventoryModel.getPartByID(this.model.getProdTempPartByIndex(row).getPartID()).getPartName();
			case "QUANTITY":
				return this.model.getProdTempPartByIndex(row).getPartQuantity();
			default:
				return null;
		}
	}
	
	@Override
	public boolean isCellEditable(int row, int column) {
		return false; // disable the table cells to be editable
	}
}
