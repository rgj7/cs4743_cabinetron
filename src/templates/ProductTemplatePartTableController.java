package templates;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import main.InventoryModel;
import main.InventoryView;

public class ProductTemplatePartTableController implements MouseListener {
	private InventoryModel model;
	private InventoryView view;
	private ProductTemplateModel prodTempModel;
	
	////////////////
	// CONSTRUCTOR
	
	public ProductTemplatePartTableController(InventoryModel model, InventoryView view, ProductTemplateModel prodTempModel) {
		this.model = model;
		this.view = view;
		this.prodTempModel = prodTempModel;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getClickCount() == 2) {
			ProductTemplatePartDetailView prodTempPartView = new ProductTemplatePartDetailView(prodTempModel, view.getCurrentTable().getSelectedRow());
			prodTempPartView.registerListeners(new ProductTemplatePartController(model, view, prodTempModel, prodTempPartView));
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
