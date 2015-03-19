package cabinetron3;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ReloadController implements ActionListener {

	private InventoryModel model;
	private InventoryListView view;

	
	////////////////
	// CONSTRUCTOR
	
	public ReloadController(InventoryModel model, InventoryListView view) {
		this.model = model;
		this.view = view;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		this.model.reloadInventory();
		this.view.update();
		
	}

}
