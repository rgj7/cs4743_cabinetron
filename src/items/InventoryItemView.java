package items;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumn;

import main.InventoryController;
import main.InventoryModel;
import main.InventoryTableController;
import main.InventoryTableModel;

@SuppressWarnings("serial")
public class InventoryItemView extends JPanel {

	JTable inventoryTable;
	JPanel inventoryButtonPanel;
	
	public InventoryItemView(InventoryModel model) {
		this.setLayout(new BorderLayout());
				
		// initialize main inventory table
		inventoryTable = new JTable(new InventoryTableModel(model));
		JScrollPane inventoryScrollPane = new JScrollPane(inventoryTable); // for scrolling
		//inventoryTable.setFillsViewportHeight(true);
		
		// set column widths
		TableColumn column = null;
		for (int i = 0; i < InventoryModel.ITEMFIELDS.length; i++) {
		    column = inventoryTable.getColumnModel().getColumn(i);
		    if(InventoryModel.ITEMFIELDS[i].equals("ITEMID") || InventoryModel.ITEMFIELDS[i].equals("QUANTITY")) {
		        column.setPreferredWidth(50);
		    } else {
		        column.setPreferredWidth(200);
		    }
		}
		
		// initialize buttons
		JButton addItemButton = new JButton("Add Item to Inventory");
		inventoryButtonPanel = new JPanel();
		inventoryButtonPanel.add(addItemButton);
		
		// add to main panel
		add(inventoryScrollPane, BorderLayout.CENTER);
		add(inventoryButtonPanel, BorderLayout.SOUTH);
	}
	
	public void registerListeners(InventoryController controller1, InventoryTableController controller2) {
		Component[] components = inventoryButtonPanel.getComponents();
		for(Component component : components) {
			if(component instanceof AbstractButton) {
				AbstractButton button = (AbstractButton) component;
				button.addActionListener(controller1);
			}
		}
		
		inventoryTable.addMouseListener(controller2);
	}
	
	public int getSelectedTableRow() {
		return this.inventoryTable.getSelectedRow();
	}
	
	public void update() {
		inventoryTable.revalidate();
	}
}
