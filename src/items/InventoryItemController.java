package items;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import database.DatabaseLockException;
import main.InventoryView;
import main.InventoryModel;

public class InventoryItemController implements ActionListener {

	private InventoryModel model;
	private InventoryView view;
	private InventoryItemDetailView itemView;
	
	////////////////
	// CONSTRUCTOR
	
	public InventoryItemController(InventoryModel model, InventoryView view, InventoryItemDetailView itemView) {
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
			} catch(IllegalArgumentException | SQLException e) {
				view.showMessage(e.getMessage());
			}
			
		} else if(command.equals("Edit Item")) {
			try {
				model.editItem(itemView.getItemID(), itemView.getItemPartNumber(), itemView.getItemLocationIndex(), 
			    		      itemView.getItemQuantity(), itemView.getTimestamp());
				itemView.close();
				view.update();
				view.showMessage("Item was edited successfully.");
			} catch(IllegalArgumentException e) {
				view.showMessage(e.getMessage());
			} catch(DatabaseLockException e){
				view.showMessage(e.getMessage());
		   		model.reloadInventory();
		   		view.update();
		   		itemView.reload();				  
			} catch (SQLException e) {
				view.showMessage(e.getMessage());
			} 	
	       
		} else if(command.equals("Delete Item")) {
			if(!model.getSession().canDeleteInventory()){
				view.showMessage("Access Denied. \n You do not have access to this action");
			} else {
				if(view.showWarningMsg("Are you sure you want to delete this item?") == 0) {
					try {
						model.deleteItem(itemView.getItemID());
					} catch(SQLException e) {
						view.showMessage(e.getMessage());
					} catch(IllegalArgumentException e) {
						view.showMessage(e.getMessage());
					}
				}
			}
			itemView.close();
			view.update();
		}
	}

}
