package templates;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import main.InventoryModel;
import main.InventoryView;

public class ProductTemplatePartController implements ActionListener {
	
	private InventoryModel model;
	private InventoryView view;
	private ProductTemplateModel prodTempModel;
	private ProductTemplatePartDetailView prodTempView;
	
	public ProductTemplatePartController(InventoryModel model, InventoryView view, ProductTemplateModel prodTempModel, ProductTemplatePartDetailView prodTempView) {
		this.model = model;
		this.view = view;
		this.prodTempModel = prodTempModel;
		this.prodTempView = prodTempView;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		String command = event.getActionCommand();
		if(command.equals("Close Product Template")) {
			if(view.showWarningMsg("Are you sure you want to close this product template?") == 0) {
				view.getTabbedPane().remove(view.getTabbedPane().getSelectedIndex());
				view.setCurrentTable(null);
			}
		} else if(command.equals("Add Part to Product Template")) {
			ProductTemplatePartDetailView partView = new ProductTemplatePartDetailView(prodTempModel, -1);
			partView.registerListeners(new ProductTemplatePartController(model, view, prodTempModel, partView));
		} if(command.equals("Add")) {
			
			try {
				// add part to ptmodel
				prodTempView.close();
				view.update();
				view.showMessage("Part was added successfully.");
			} catch(IllegalArgumentException e) {
				view.showMessage(e.getMessage());
			}
			
		} else if(command.equals("Edit")) {
			
			try {
				// edit part in ptmodel
				prodTempView.close();
				view.update();
				view.showMessage("Part was edited successfully.");
			} catch(IllegalArgumentException e) {
				view.showMessage(e.getMessage());
			}
			
		} else if(command.equals("Delete")) {
			if(view.showWarningMsg("Are you sure you want to delete this part?") == 0) {
				// delete part in ptmodel
			}
			prodTempView.close();
			view.update();
		}
	}
}
