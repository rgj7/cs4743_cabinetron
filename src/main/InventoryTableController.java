package main;

import items.InventoryItemController;
import items.InventoryItemDetailView;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class InventoryTableController implements MouseListener {
	private InventoryModel model;
	private InventoryView view;
	
	////////////////
	// CONSTRUCTOR
	
	public InventoryTableController(InventoryModel model, InventoryView view) {
		this.model = model;
		this.view = view;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getClickCount() == 2) {
			if(!model.getSession().canAddInventory()){
				view.showMessage("Access Denied. \n You do not have access to this action");
			} else {
			   InventoryItemDetailView itemView = new InventoryItemDetailView(model, view, view.getSelectedInventoryItemRow());
			   itemView.registerListeners(new InventoryItemController(model, view, itemView));
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
	
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
	}
}