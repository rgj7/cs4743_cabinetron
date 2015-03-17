package cabinetron3;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.TableColumn;


@SuppressWarnings("serial")
public class InventoryListView extends JFrame {	

	private InventoryModel model;
	private JTable inventoryTable, partsTable, prodTempTable;
	private JScrollPane inventoryScrollPane, partsScrollPane, prodTempScrollPane;
	private JPanel inventoryMainPanel, partsMainPanel, prodTempMainPanel, inventoryButtonPanel, partsButtonPanel, prodTempButtonPanel;
	private JMenu inventoryMenu, partsMenu, prodTempMenu;
	private JTabbedPane tabbedPane;
	
	////////////////
	// CONSTRUCTOR
	
	public InventoryListView(InventoryModel model)  {
		// set window title
		super("Cabinentron Inventory");

		this.model = model;
		
		// initialize all GUI components
		initMenu();
		initInventoryComponents();
		initPartComponents();
		initProdTempComponents();
		initTabs();
	}
	
	public void initTabs() {
		// initialize tab panel
		tabbedPane = new JTabbedPane();
		
		// add main panels to tab
		tabbedPane.addTab("Inventory", null, inventoryMainPanel, "View inventory");
		tabbedPane.addTab("Parts", null, partsMainPanel, "View parts list");
		tabbedPane.addTab("Product Templates", null, prodTempMainPanel, "View product template list");
		
		tabbedPane.setTabPlacement(JTabbedPane.BOTTOM);
		
		// add tab panel to frame
		add(tabbedPane, BorderLayout.CENTER);
	}
	
	public void initMenu() {
		// initialize menu bar
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		// inventory
		inventoryMenu = new JMenu("Inventory");
		menuBar.add(inventoryMenu);
		
		JMenuItem addItemMenuItem = new JMenuItem("Add Item to Inventory");
		JMenuItem viewInventoryMenuItem = new JMenuItem("View Inventory");
		JMenuItem exitMenuItem = new JMenuItem("Exit");
		
		inventoryMenu.add(addItemMenuItem);
		inventoryMenu.add(viewInventoryMenuItem);
		inventoryMenu.add(exitMenuItem);
		
		// parts
		partsMenu = new JMenu("Parts");
		menuBar.add(partsMenu);
		
		JMenuItem addPartMenuItem = new JMenuItem("Add Part to Parts List");
		JMenuItem viewPartsMenuItem = new JMenuItem("View All Parts");

		partsMenu.add(addPartMenuItem);
		partsMenu.add(viewPartsMenuItem);
		
		// product template
		prodTempMenu = new JMenu("Product Templates");
		menuBar.add(prodTempMenu);
		
		JMenuItem addProdTempMenuItem = new JMenuItem("Add Product Template");
		JMenuItem viewProdTempsMenuItem = new JMenuItem("View All Product Templates");
		
		prodTempMenu.add(addProdTempMenuItem);
		prodTempMenu.add(viewProdTempsMenuItem);
	}
	
	private void initInventoryComponents() {
		// initialize main panel for inventory
		inventoryMainPanel = new JPanel(new BorderLayout());
		
		// initialize main inventory table
		inventoryTable = new JTable(new InventoryTableModel(model));
		inventoryScrollPane = new JScrollPane(inventoryTable); // for scrolling
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
		inventoryMainPanel.add(inventoryScrollPane, BorderLayout.CENTER);
		inventoryMainPanel.add(inventoryButtonPanel, BorderLayout.SOUTH);
	}
	
	private void initPartComponents() {
		// initialize main panel for parts
		partsMainPanel = new JPanel(new BorderLayout());
		
		// initialize main parts table
		partsTable = new JTable(new PartsTableModel(model));
		partsScrollPane = new JScrollPane(partsTable); // for scrolling
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
		partsMainPanel.add(partsScrollPane, BorderLayout.CENTER);
		partsMainPanel.add(partsButtonPanel, BorderLayout.SOUTH);
	}
	
	private void initProdTempComponents() {
		// initialize main panel for parts
		prodTempMainPanel = new JPanel(new BorderLayout());
		
		// initialize main parts table
		prodTempTable = new JTable(new ProductTemplateTableModel(model));
		prodTempScrollPane = new JScrollPane(prodTempTable); // for scrolling
		
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
		prodTempMainPanel.add(prodTempScrollPane, BorderLayout.CENTER);
		prodTempMainPanel.add(prodTempButtonPanel, BorderLayout.SOUTH);
	}
	
	public void addProductTemplateTab(int prodTempIndex) {
		ProductTemplateModel prodTempModel = model.getProdTempByIndex(prodTempIndex);
		JPanel mainPanel = new JPanel(new BorderLayout());
		
		//JTable table = new JTable(new ProductTemplatePartTableModel(model, prodTempIndex));
		JTable table = new JTable(new PartsTableModel(model));
		JScrollPane scrollPane = new JScrollPane(table);
		
		JButton addPartToProdTemp = new JButton("Add Part to Product Template");
		JButton editProdTemp = new JButton("Edit Product Template");
		JButton closeProdTemp = new JButton("Save/Close Product Template");
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(addPartToProdTemp);
		buttonPanel.add(editProdTemp);
		buttonPanel.add(closeProdTemp);
		
		mainPanel.add(scrollPane, BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);
		
		tabbedPane.add(prodTempModel.getProductTemplateNumber(), mainPanel);
		tabbedPane.setSelectedComponent(mainPanel);
	}
	
	public void registerListeners(InventoryController controller1, InventoryTableController controller2) {	
		Component[] components = inventoryMenu.getMenuComponents();
		for(Component component : components) {
			if(component instanceof AbstractButton) {
				AbstractButton button = (AbstractButton) component;
				button.addActionListener(controller1);
			}
		}
		
		components = partsMenu.getMenuComponents();
		for(Component component : components) {
			if(component instanceof AbstractButton) {
				AbstractButton button = (AbstractButton) component;
				button.addActionListener(controller1);
			}
		}
		
		components = inventoryButtonPanel.getComponents();
		for(Component component : components) {
			if(component instanceof AbstractButton) {
				AbstractButton button = (AbstractButton) component;
				button.addActionListener(controller1);
			}
		}

		components = partsButtonPanel.getComponents();
		for(Component component : components) {
			if(component instanceof AbstractButton) {
				AbstractButton button = (AbstractButton) component;
				button.addActionListener(controller1);
			}
		}
		
		components = prodTempButtonPanel.getComponents();
		for(Component component : components) {
			if(component instanceof AbstractButton) {
				AbstractButton button = (AbstractButton) component;
				button.addActionListener(controller1);
			}
		}
		
		// add mouse listener to tables
		inventoryTable.addMouseListener(controller2);
		partsTable.addMouseListener(controller2);
		prodTempTable.addMouseListener(controller2);
	}
		
	public int getSelectedItemRow() {
		return this.inventoryTable.getSelectedRow();
	}

	public int getSelectedPartRow() {
		return this.partsTable.getSelectedRow();
	}
	
	public int getSelectedProdTempRow() {
		return this.prodTempTable.getSelectedRow();
	}
	
	public int getCurrentView() {
		return tabbedPane.getSelectedIndex();
	}

	// show small pop-up message
	public void showMessage(String msg) {
		JOptionPane.showMessageDialog(null, msg);
	}
	
	public int showWarningMsg(String msg) {
		return JOptionPane.showConfirmDialog(null, msg, "Warning", JOptionPane.YES_NO_OPTION);
	}
		
	public void update() {
		partsTable.revalidate();
		inventoryTable.revalidate();
		prodTempTable.revalidate();
		this.revalidate();
		this.repaint();
	}
}
