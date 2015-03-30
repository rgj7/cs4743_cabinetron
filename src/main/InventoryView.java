package main;

import items.InventoryItemView;

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

import parts.PartTableController;
import parts.PartView;
import templateparts.ProductTemplatePartController;
import templateparts.ProductTemplatePartTableController;
import templateparts.ProductTemplatePartTableModel;
import templates.ProductTemplateModel;
import templates.ProductTemplateTableController;
import templates.ProductTemplateView;

@SuppressWarnings("serial")
public class InventoryView extends JFrame {	

	private InventoryModel model;
	private JMenu inventoryMenu, partsMenu, prodTempMenu;
	private JTabbedPane tabbedPane;
	private JTable currentTable;
	
	private InventoryItemView inventoryItemView;
	private PartView partView;
	private ProductTemplateView prodTempView;
	
	////////////////
	// CONSTRUCTOR
	
	public InventoryView(InventoryModel model)  {
		// set window title
		super("Cabinentron Inventory");

		this.model = model;
		
		initMenu();

		// initialize tab panel
		tabbedPane = new JTabbedPane();
		tabbedPane.setTabPlacement(JTabbedPane.BOTTOM);
		
		// initialize views
		inventoryItemView = new InventoryItemView(model);
		partView = new PartView(model);
		prodTempView = new ProductTemplateView(model);
				
		// add views as tabs
		tabbedPane.addTab("Inventory", null, inventoryItemView, "View inventory");
		tabbedPane.addTab("Parts", null, partView, "View parts list");
		tabbedPane.addTab("Product Templates", null, prodTempView, "View product template list");
		
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
		
	public void addProductTemplateTab(int prodTempIndex) {
		ProductTemplateModel prodTempModel = model.getProdTempByIndex(prodTempIndex);
		JPanel mainPanel = new JPanel(new BorderLayout());
		
		JTable table = new JTable(new ProductTemplatePartTableModel(model, prodTempModel));
		setCurrentTable(table);
		//JTable table = new JTable(new PartsTableModel(model));
		JScrollPane scrollPane = new JScrollPane(table);
		
		JButton addPartToProdTemp = new JButton("Add Part to Product Template");
		JButton closeProdTemp = new JButton("Close Product Template");
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(addPartToProdTemp);
		buttonPanel.add(closeProdTemp);
		
		mainPanel.add(scrollPane, BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);
		
		tabbedPane.add("ProductTemplate: " + prodTempModel.getProductTemplateNumber(), mainPanel);
		tabbedPane.setSelectedComponent(mainPanel);
		
		// disable other tabs
		tabbedPane.setEnabledAt(0, false);
		tabbedPane.setEnabledAt(1, false);
		tabbedPane.setEnabledAt(2, false);
		
		// add mouse listener to table
		table.addMouseListener(new ProductTemplatePartTableController(model, this, prodTempModel));
		// add action listeners to buttons
		ProductTemplatePartController controller1 = new ProductTemplatePartController(model, this, prodTempModel, null);
		Component[] components = buttonPanel.getComponents();
		for(Component component : components) {
			if(component instanceof AbstractButton) {
				AbstractButton button = (AbstractButton) component;
				button.addActionListener(controller1);
			}
		}
	}
	
	public void registerListeners(InventoryController controller1, InventoryTableController controller2) {	
		// register action listeners for menu items
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
		components = prodTempMenu.getMenuComponents();
		for(Component component : components) {
			if(component instanceof AbstractButton) {
				AbstractButton button = (AbstractButton) component;
				button.addActionListener(controller1);
			}
		}
		// register listeners
		// TODO: move from InventoryController to respective XController
		inventoryItemView.registerListeners(controller1, controller2);
		partView.registerListeners(controller1, new PartTableController(model, this));
		prodTempView.registerListeners(controller1, new ProductTemplateTableController(model, this));
	}
	
	// GETTERS
	
	public int getSelectedInventoryItemRow() {
		return inventoryItemView.getSelectedTableRow();
	}
	
	public int getSelectedPartRow() {
		return partView.getSelectedTableRow();
	}
	
	public int getSelectedProdTempRow() {
		return prodTempView.getSelectedTableRow();
	}
	
	public JTabbedPane getTabbedPane() {
		return tabbedPane;
	}
	
	public JTable getCurrentTable() {
		return currentTable;
	}
	
	public void setCurrentTable(JTable table) {
		currentTable = table;
	}
	
	// show small pop-up message
	public void showMessage(String msg) {
		JOptionPane.showMessageDialog(null, msg);
	}
	
	public int showWarningMsg(String msg) {
		return JOptionPane.showConfirmDialog(null, msg, "Warning", JOptionPane.YES_NO_OPTION);
	}
			
	public void update() {
		partView.update();
		inventoryItemView.update();
		prodTempView.update();
		
		if(currentTable != null)
			currentTable.revalidate();
		
		this.revalidate();
		this.repaint();
	}
}
