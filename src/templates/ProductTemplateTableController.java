package templates;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import main.InventoryModel;
import main.InventoryView;

public class ProductTemplateTableController implements MouseListener {
	
	private InventoryModel model;
	private InventoryView view;
	
	////////////////
	// CONSTRUCTOR
	
	public ProductTemplateTableController(InventoryModel model, InventoryView view) {
		this.model = model;
		this.view = view;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getClickCount() == 2) {
			switch(view.prodTempOptionDialog()) {
				case 0: // edit
					ProductTemplateDetailView prodTempView = new ProductTemplateDetailView(model, view, view.getSelectedProdTempRow());
					prodTempView.registerListeners(new ProductTemplateController(model, view, prodTempView));
					break;
				case 1: // view
					view.addProductTemplateTab(view.getSelectedProdTempRow());
					break;
				default:
					break;
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
