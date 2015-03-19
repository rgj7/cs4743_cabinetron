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
		return this.inventoryModel.getProductTemplatePartsSize();
	}

	@Override
	public Object getValueAt(int row, int column) {
		switch(ProductTemplateModel.PARTFIELDS[column]) {
			case "PTID":
				return this.inventoryModel.getProdTempPartByIndex(row).getProductTemplateID();
			case "PART ID":
				return this.inventoryModel.getProdTempPartByIndex(row).getPartID();
			case "PART NAME":
				return this.inventoryModel.getPartByID(this.inventoryModel.getProdTempPartByIndex(row).getPartID()).getPartName();
			case "QUANTITY":
				return this.inventoryModel.getProdTempPartByIndex(row).getPartQuantity();
			default:
				return null;
		}
	}
	
	@Override
	public boolean isCellEditable(int row, int column) {
		return false; // disable the table cells to be editable
	}
}
