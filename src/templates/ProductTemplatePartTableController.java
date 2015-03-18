package templates;

import items.InventoryItemController;
import items.InventoryItemDetailView;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import main.InventoryModel;
import main.InventoryView;

public class ProductTemplatePartTableController implements MouseListener {
	private InventoryModel model;
	private InventoryView view;
	
	////////////////
	// CONSTRUCTOR
	
	public ProductTemplatePartTableController(InventoryModel model, InventoryView view) {
		this.model = model;
		this.view = view;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getClickCount() == 2) {
			ProductTemplatePartDetailView prodTempPartView = new ProductTemplatePartDetailView(1);
			//prodTempPartView.registerListeners(new ProductTemplatePartController(model, view, prodTempPartView));
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
