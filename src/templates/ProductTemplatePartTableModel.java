package templates;

import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class ProductTemplatePartTableModel extends AbstractTableModel {

	private ProductTemplateModel model;
	
	public ProductTemplatePartTableModel(ProductTemplateModel model){
		this.model = model;
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
