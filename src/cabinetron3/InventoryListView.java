package cabinetron3;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.TableColumn;


@SuppressWarnings("serial")
public class InventoryListView extends JFrame {	

	private InventoryModel model;
	private JTable inventoryTable, partsTable;
	private JScrollPane inventoryScrollPane, partsScrollPane;
	private JPanel inventoryButtonPanel, partsButtonPanel;
	private JMenu inventoryMenu, partsMenu;
	private JLabel titleLabel;
	private int currentView;
	
	////////////////
	// CONSTRUCTOR
	
	public InventoryListView(InventoryModel model)  {
		// set window title
		super("Cabinentron Inventory");

		this.model = model;
		
		// initialize all GUI components
		initMenu();
		initTitle();
		initInventoryComponents();
		initPartComponents();
		
		// default view
		showInventory();
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
	}
	
	public void initTitle() {
		// initialize label for title
		titleLabel = new JLabel();
		titleLabel.setFont(new Font("Verdana", Font.BOLD, 14));
		Border titlePadding = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		titleLabel.setBorder(titlePadding);
		add(titleLabel, BorderLayout.PAGE_START);
	}
	
	private void initInventoryComponents() {				
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
		JButton viewPartsButton = new JButton("View All Parts");
		
		inventoryButtonPanel = new JPanel();
		inventoryButtonPanel.add(addItemButton);
		inventoryButtonPanel.add(viewPartsButton);
	}
	
	private void initPartComponents() {
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
		JButton viewInventoryButton = new JButton("View Inventory");
		
		partsButtonPanel = new JPanel();
		partsButtonPanel.add(addPartButton);
		partsButtonPanel.add(viewInventoryButton);
	}
	
	public void showInventory() {
		titleLabel.setText("Displaying Current Inventory");
		add(inventoryScrollPane, BorderLayout.CENTER);
		add(inventoryButtonPanel, BorderLayout.PAGE_END);
		remove(partsScrollPane);
		remove(partsButtonPanel);
		currentView = 1;
	}
	
	public void showParts() {
		titleLabel.setText("Displaying Parts List");
		add(partsScrollPane, BorderLayout.CENTER);
		add(partsButtonPanel, BorderLayout.PAGE_END);
		remove(inventoryScrollPane);	
		remove(inventoryButtonPanel);
		currentView = 2;
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
		
		// add mouse listener for inventory table
		inventoryTable.addMouseListener(controller2);
		partsTable.addMouseListener(controller2);
	}
		
	public int getSelectedItemRow() {
		return this.inventoryTable.getSelectedRow();
	}

	public int getSelectedPartRow() {
		return this.partsTable.getSelectedRow();
	}
	
	public int getCurrentView() {
		return this.currentView;
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
		this.revalidate();
		this.repaint();
	}
}
