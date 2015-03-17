package cabinetron3;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class InventoryTableController implements MouseListener {
	private InventoryModel model;
	private InventoryListView view;
	
	////////////////
	// CONSTRUCTOR
	
	public InventoryTableController(InventoryModel model, InventoryListView view) {
		this.model = model;
		this.view = view;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getClickCount() == 2) {
			if(view.getCurrentView() == 0) { // inventory
				InventoryItemDetailView itemView = new InventoryItemDetailView(model, view, view.getSelectedItemRow());
				itemView.registerListeners(new InventoryItemController(model, view, itemView));
			} else if(view.getCurrentView() == 1) { // parts
				PartDetailView partView = new PartDetailView(model, view, view.getSelectedPartRow());
				partView.registerListeners(new PartController(model, view, partView));
			} else if(view.getCurrentView() == 2) { // product templates
				view.addProductTemplateTab(view.getSelectedProdTempRow());
				//ProductTemplateDetailView prodTempView = new ProductTemplateDetailView(model, view, view.getSelectedProdTempRow());
				//prodTempView.registerListeners(new ProductTemplateController(model, view, prodTempView));
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