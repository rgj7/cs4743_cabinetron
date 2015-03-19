package templates;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JOptionPane;

import templateparts.ProductTemplatePartController;
import templateparts.ProductTemplatePartDetailView;
import main.InventoryModel;
import main.InventoryView;

public class ProductTemplateTableController implements MouseListener {
	
	private InventoryModel model;
	private InventoryView view;
	private ProductTemplateModel prodTempModel;
	
	////////////////
	// CONSTRUCTOR
	
	public ProductTemplateTableController(InventoryModel model, InventoryView view, ProductTemplateModel prodTempModel) {
		this.model = model;
		this.view = view;
		this.prodTempModel = prodTempModel;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getClickCount() == 2) {
			if(model.getProductTemplatePartsSize() == 0) {
				String[] options = {"Add Part", "Cancel"};
				if(JOptionPane.showOptionDialog(null, "This Product Template is empty. Would you like to add a part?", 
						"New Product Template", 0, JOptionPane.QUESTION_MESSAGE, null, options, "Add Part") == 0) {
					ProductTemplatePartDetailView partView = new ProductTemplatePartDetailView(model, prodTempModel, -1);
					partView.registerListeners(new ProductTemplatePartController(model, view, prodTempModel, partView));
				}
			} else {
				String[] options = {"Edit PT", "View PT Parts"};
				int result = JOptionPane.showOptionDialog(null, "Would you like to edit the Product Template or view its parts?", 
						"Product Template Options", 0, JOptionPane.QUESTION_MESSAGE, null, options, "Edit PT");
				if(result == 0) {
					// edit
					ProductTemplateDetailView prodTempView = new ProductTemplateDetailView(model, view, view.getSelectedProdTempRow());
					prodTempView.registerListeners(new ProductTemplateController(model, view, prodTempView));
				} else {
					// view
					view.addProductTemplateTab(view.getSelectedProdTempRow());
				}
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
