package cabinetron3;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InventoryController implements ActionListener {
	private InventoryModel model;
	private InventoryListView view;
	
	////////////////
	// CONSTRUCTOR
	
	public InventoryController(InventoryModel model, InventoryListView view) {
		this.model = model;
		this.view = view;
	}
	
	public void actionPerformed(ActionEvent event) {
		String command = event.getActionCommand();
		if(command.equals("Exit")) {
			view.dispose();
			System.exit(0);
		} else if(command.equals("Add Item to Inventory")) {
			InventoryItemDetailView itemView = new InventoryItemDetailView(model, view, model.getLastItemID()); // sets item id to next available id
			itemView.registerListeners(new InventoryItemController(model, view, itemView));
		} else if(command.equals("Add Part to Parts List")) {
			PartDetailView partView = new PartDetailView(model, view, model.getLastPartID()); // sets part id to next available id
			partView.registerListeners(new PartController(model, view, partView));
		} else if(command.equals("View All Parts")) {
			view.showParts();
			view.update();
		} else if(command.equals("View Inventory")) {
			view.showInventory();
			view.update();
		}
	}
	
	
}