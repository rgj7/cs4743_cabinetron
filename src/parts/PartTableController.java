package parts;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import main.InventoryModel;
import main.InventoryView;

public class PartTableController implements MouseListener {
	private InventoryModel model;
	private InventoryView view;
	
	public PartTableController(InventoryModel model, InventoryView view) {
		this.model = model;
		this.view = view;
	}
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		if(arg0.getClickCount() == 2) {
			if(!model.getSession().canAddParts()){
				view.showMessage("Access Denied. \n You do not have access to this action");
			} else {
				PartDetailView partView = new PartDetailView(model, view, view.getSelectedPartRow());
				partView.registerListeners(new PartController(model, view, partView));
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
