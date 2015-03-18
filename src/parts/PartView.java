package parts;

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

@SuppressWarnings("serial")
public class PartView extends JPanel {

	private JPanel partsButtonPanel;
	private JTable partsTable;
	
	public PartView(InventoryModel model) {
		this.setLayout(new BorderLayout());
		
		// initialize main parts table
		partsTable = new JTable(new PartsTableModel(model));
		JScrollPane partsScrollPane = new JScrollPane(partsTable); // for scrolling
		//partsTable.setFillsViewportHeight(true);
		
		// set column widths
		TableColumn column = null;
		for (int i = 0; i < InventoryModel.ITEMFIELDS.length; i++) {
		    column = partsTable.getColumnModel().getColumn(i);
		    if(InventoryModel.PARTFIELDS[i].equals("PARTID")) {
		        column.setPreferredWidth(25);
		    } else {
		        column.setPreferredWidth(100);
		    }
		}
		
		// initialize buttons
		JButton addPartButton = new JButton("Add Part to Parts List");
		partsButtonPanel = new JPanel();
		partsButtonPanel.add(addPartButton);
		
		// add to main panel
		add(partsScrollPane, BorderLayout.CENTER);
		add(partsButtonPanel, BorderLayout.SOUTH);
	}
	
	public void registerListeners(InventoryController controller1, PartTableController controller2) {
		Component[] components = partsButtonPanel.getComponents();
		for(Component component : components) {
			if(component instanceof AbstractButton) {
				AbstractButton button = (AbstractButton) component;
				button.addActionListener(controller1);
			}
		}
		
		partsTable.addMouseListener(controller2);
	}
	
	public int getSelectedTableRow() {
		return this.partsTable.getSelectedRow();
	}
	
	public void update() {
		partsTable.revalidate();
	}
}
