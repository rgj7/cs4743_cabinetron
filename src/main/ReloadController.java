package main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ReloadController implements ActionListener {

	private InventoryModel model;
	private InventoryView view;

	////////////////
	// CONSTRUCTOR
	
	public ReloadController(InventoryModel model, InventoryView view) {
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
