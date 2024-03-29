package parts;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import main.InventoryView;
import main.InventoryModel;

public class PartController implements ActionListener {
	private InventoryModel model;
	private InventoryView view;
	private PartDetailView partView;
	
	////////////////
	// CONSTRUCTOR
	
	public PartController(InventoryModel model, InventoryView view, PartDetailView partView) {
		this.model = model;
		this.view = view;
		this.partView = partView;
	}
	
	///////////////
	// ActionLister Methods
	
	@Override
	public void actionPerformed(ActionEvent event) {
		String command = event.getActionCommand();
		if(command.equals("Add Part")) {
			
			try {
				model.addPart(partView.getPartNumber(), partView.getPartName(), partView.getUnitIndex(), partView.getPartExternalNumber(), partView.getPartVendor());
				partView.close();
				view.update();
				view.showMessage("Part was added successfully.");
			} catch(IllegalArgumentException | SQLException e) {
				view.showMessage(e.getMessage());
			}
			
		} else if(command.equals("Edit Part")) {
			
			try {
				model.editPart(partView.getPartID(), partView.getPartNumber(), partView.getPartName(), partView.getUnitIndex(), partView.getPartExternalNumber(), partView.getPartVendor());
				partView.close();
				view.update();
				view.showMessage("Part was edited successfully.");
			} catch(IllegalArgumentException | SQLException e) {
				view.showMessage(e.getMessage());
			}
			
		} else if(command.equals("Delete Part")) {
			if(!model.getSession().canDeleteParts()){
				view.showMessage("Access Denied. \n You do not have access to this action");
			} else {
				if(view.showWarningMsg("Are you sure you want to delete this part?") == 0) {
					try {
						model.deletePart(partView.getPartID());
					} catch (IllegalArgumentException | SQLException e) {
						view.showMessage(e.getMessage());
					}
				}
			}
			partView.close();
			view.update();
		}
		
	}
}
