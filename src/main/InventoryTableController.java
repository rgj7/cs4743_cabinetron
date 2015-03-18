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
			InventoryItemDetailView itemView = new InventoryItemDetailView(model, view, view.getSelectedInventoryItemRow());
			itemView.registerListeners(new InventoryItemController(model, view, itemView));
			/* else if(view.getCurrentView() == 1) { // parts
				PartDetailView partView = new PartDetailView(model, view, view.getSelectedPartRow());
				partView.registerListeners(new PartController(model, view, partView));
			} else if(view.getCurrentView() == 2) { // product templates
				view.addProductTemplateTab(view.getSelectedProdTempRow());
				//ProductTemplateDetailView prodTempView = new ProductTemplateDetailView(model, view, view.getSelectedProdTempRow());
				//prodTempView.registerListeners(new ProductTemplateController(model, view, prodTempView));
			}*/
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