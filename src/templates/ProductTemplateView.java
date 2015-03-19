package templates;

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

@SuppressWarnings("serial")
public class ProductTemplateView extends JPanel {

	JTable prodTempTable;
	JPanel prodTempButtonPanel;
	
	public ProductTemplateView(InventoryModel model) {
		this.setLayout(new BorderLayout());
		
		// initialize main parts table
		prodTempTable = new JTable(new ProductTemplateTableModel(model));
		JScrollPane prodTempScrollPane = new JScrollPane(prodTempTable); // for scrolling
		
		// set column widths
		TableColumn column = null;
		for (int i = 0; i < InventoryModel.PRODTEMPFIELDS.length; i++) {
		    column = prodTempTable.getColumnModel().getColumn(i);
		    if(InventoryModel.PRODTEMPFIELDS[i].equals("PTID")) {
		    	column.setPreferredWidth(100);
		    } else if(InventoryModel.PRODTEMPFIELDS[i].equals("NUMBER")) {
		        column.setPreferredWidth(200);
		    } else {
		        column.setPreferredWidth(500);
		    }
		}
		
		// initialize buttons
		JButton addProdTempButton = new JButton("Add Product Template");
		prodTempButtonPanel = new JPanel();
		prodTempButtonPanel.add(addProdTempButton);
		
		// add to main panel
		add(prodTempScrollPane, BorderLayout.CENTER);
		add(prodTempButtonPanel, BorderLayout.SOUTH);
	}
	
	public void registerListeners(InventoryController controller1, ProductTemplateTableController controller2) {
		Component[] components = prodTempButtonPanel.getComponents();
		for(Component component : components) {
			if(component instanceof AbstractButton) {
				AbstractButton button = (AbstractButton) component;
				button.addActionListener(controller1);
			}
		}
		
		prodTempTable.addMouseListener(controller2);
	}
	
	public int getSelectedTableRow() {
		return this.prodTempTable.getSelectedRow();
	}
	
	public void update() {
		prodTempTable.revalidate();
	}
}
