package cabinetron3;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InventoryItemController implements ActionListener {

	private InventoryModel model;
	private InventoryListView view;
	private InventoryItemDetailView itemView;
	
	////////////////
	// CONSTRUCTOR
	
	public InventoryItemController(InventoryModel model, InventoryListView view, InventoryItemDetailView itemView) {
		this.model = model;
		this.view = view;
		this.itemView = itemView;
	}
	
	///////////////
	// ActionLister Methods
	
	@Override
	public void actionPerformed(ActionEvent event) {
		String command = event.getActionCommand();
		if(command.equals("Add Item")) {
			
			try {
				model.addItem(itemView.getItemPartNumber(), itemView.getItemLocationIndex(), itemView.getItemQuantity());
				itemView.close();
				view.update();
				view.showMessage("Item was added successfully.");
			} catch(IllegalArgumentException e) {
				view.showMessage(e.getMessage());
			}
			
		} else if(command.equals("Edit Item")) {
			
			try {
				model.editItem(itemView.getItemID(), itemView.getItemPartNumber(), itemView.getItemLocationIndex(), itemView.getItemQuantity());
				itemView.close();
				view.update();
				view.showMessage("Item was edited successfully.");
			} catch(IllegalArgumentException e) {
				view.showMessage(e.getMessage());
			}
			
		} else if(command.equals("Delete Item")) {
			if(view.showWarningMsg("Are you sure you want to delete this item?") == 0) {
				model.deleteItem(itemView.getItemID());
			}
			itemView.close();
			view.update();
		}
	}

}
