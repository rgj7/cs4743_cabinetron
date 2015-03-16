package cabinetron3;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ProductTemplateController implements ActionListener {
	
	private InventoryModel model;
	private InventoryListView view;
	private ProductTemplateDetailView prodTempView;
	
	public ProductTemplateController(InventoryModel model, InventoryListView view, ProductTemplateDetailView prodTempView) {
		this.model = model;
		this.view = view;
		this.prodTempView = prodTempView;
	}
	
	///////////////
	// ActionLister Methods
	
	@Override
	public void actionPerformed(ActionEvent event) {
		String command = event.getActionCommand();
		if(command.equals("Add")) {
			
			try {
				model.addProductTemplate(prodTempView.getProductTemplateNumber(), prodTempView.getProductTemplateDescription());
				prodTempView.close();
				view.update();
				view.showMessage("Product template was added successfully.");
			} catch(IllegalArgumentException e) {
				view.showMessage(e.getMessage());
			}
			
		} else if(command.equals("Edit")) {
			
			try {
				//model.editProductTemplate(prodTempView.getProductTemplateID(), prodTempView.getProductTemplateNumber(), prodTempView.getProductTemplateDescription());
				prodTempView.close();
				view.update();
				view.showMessage("Product template was edited successfully.");
			} catch(IllegalArgumentException e) {
				view.showMessage(e.getMessage());
			}
			
		} else if(command.equals("Delete")) {
			if(view.showWarningMsg("Are you sure you want to delete this product template?") == 0) {
				//model.deleteProductTemplate(prodTempView.getProductTemplateID());
			}
			prodTempView.close();
			view.update();
		}
	}
}
