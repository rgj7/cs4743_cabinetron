package main;

import items.InventoryItemController;
import items.InventoryItemDetailView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import parts.PartController;
import parts.PartDetailView;
import templates.ProductTemplateController;
import templates.ProductTemplateDetailView;

public class InventoryController implements ActionListener {
	private InventoryModel model;
	private InventoryView view;
	
	////////////////
	// CONSTRUCTOR
	
	public InventoryController(InventoryModel model, InventoryView view) {
		this.model = model;
		this.view = view;
	}
	
	public void actionPerformed(ActionEvent event) {
		String command = event.getActionCommand();
		if(command.equals("Exit")) {
			System.out.println("Exit");
			view.dispose();
			System.exit(0);
						
		} else if(command.equals("Add Item to Inventory")) {
			
			if(!model.getSession().canAddInventory() && !model.getSession().canCreateProducts()){
				view.showMessage("Access Denied. \n You do not have access to this action");
			}
			else{
				InventoryItemDetailView itemView = new InventoryItemDetailView(model, view, model.getLastItemID()); // sets item id to next available id
				itemView.registerListeners(new InventoryItemController(model, view, itemView));
			}
			
		} else if(command.equals("Add Part to Parts List")) {
			
			if(!model.getSession().canAddParts()){
				view.showMessage("Access Denied. \n You do not have access to this action");
			}
			else{
				PartDetailView partView = new PartDetailView(model, view, model.getLastPartID()); // sets part id to next available id
				partView.registerListeners(new PartController(model, view, partView));
			}
			
		} else if(command.equals("Add Product Template")) {
			if(!model.getSession().canAddProductTemplates()){
				view.showMessage("Access Denied. \n You do not have access to this action");
			}
			else{
				ProductTemplateDetailView prodTempView = new ProductTemplateDetailView(model, view, model.getLastProductTemplateID());
				prodTempView.registerListeners(new ProductTemplateController(model, view, prodTempView));
			}
			
		}else if(command.equals("View Inventory")) {
			if(!model.getSession().canViewInventory()){
				view.showMessage("Access Denied. \n You do not have access to this action");
			}
			else{
				view.getTabbedPane().setSelectedIndex(0);
				view.update();
			}
			
		} else if(command.equals("View All Parts")) {
			if(!model.getSession().canViewParts()){
				view.showMessage("Access Denied. \n You do not have access to this action");
			}
			else{
				view.getTabbedPane().setSelectedIndex(1);
				view.update();
			}
		
		} else if(command.equals("View All Product Templates")) {
			if(!model.getSession().canViewProductTemplates()){
				view.showMessage("Access Denied. \n You do not have access to this action");	
			}
			else{
			view.getTabbedPane().setSelectedIndex(2);
			view.update();
			}
		}
	}
}