package items;

import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Timestamp;
import java.text.NumberFormat;

import javax.swing.*;
import javax.swing.text.NumberFormatter;

import main.InventoryView;
import main.InventoryModel;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class InventoryItemDetailView extends JFrame {

	private JPanel formPanel; 
	private JTextField itemIdTextField;
	private JFormattedTextField itemQuantityTextField;
	private JComboBox<String> itemLocationComboBox, itemPartComboBox, itemTemplateComboBox, itemPartOrProduct;
	private InventoryModel model;
	private int itemIndex;
	private Timestamp itemTimeStamp;
	////////////////
	// CONSTRUCTOR
	
	public InventoryItemDetailView(InventoryModel model, InventoryView view, int itemIndex) {
		super("Item Details");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.itemIndex = itemIndex;
		this.model = model;
		
		MigLayout layout = new MigLayout("wrap 2", "[right]10[grow,fill]", "[]10[]");
		formPanel = new JPanel(layout);
				
		// Field: ITEMID
		itemIdTextField = new JTextField();
		itemIdTextField.setEditable(false); // just show id, not editable
		JLabel itemIdLabel = new JLabel("Item ID");
		itemIdLabel.setLabelFor(itemIdTextField);
		
		// Field: ITEM TYPE
		String[] options = {"Part", "Product"};
		itemPartOrProduct = new JComboBox<String>(options);
		JLabel itemPartOrProductLabel = new JLabel("Item Type");
		itemPartOrProductLabel.setLabelFor(itemPartOrProduct);
		
		// Field: PART
		itemPartComboBox = new JComboBox<String>(model.getPartNumbers()); 
		JLabel itemPartLabel = new JLabel("Part");
		itemPartLabel.setLabelFor(itemPartComboBox);
		
		// Field: TEMPLATE
		itemTemplateComboBox = new JComboBox<String>(model.getTemplateNumbers());
		itemTemplateComboBox.setEnabled(false);
		JLabel itemTemplateLabel = new JLabel("Product Template");
		itemTemplateLabel.setLabelFor(itemTemplateComboBox);
		
				
		// Field: LOCATION
		itemLocationComboBox = new JComboBox<String>(InventoryItemModel.LOCATIONS);
		JLabel itemLocationLabel = new JLabel("Location");
		itemLocationLabel.setLabelFor(itemLocationComboBox);
		
		// initialize number formatter for quantity field
		NumberFormat numberFormat = NumberFormat.getIntegerInstance();
		numberFormat.setGroupingUsed(false);
		NumberFormatter numberFormatter = new NumberFormatter(numberFormat);
		numberFormatter.setValueClass(Integer.class);
		numberFormatter.setAllowsInvalid(false);

		// Field: QUANTITY
		itemQuantityTextField = new JFormattedTextField(numberFormatter);
		itemQuantityTextField.setText("1"); // since initial must be 1 or greater anyway
		JLabel itemQuantityLabel = new JLabel("Quantity");
		itemPartLabel.setLabelFor(itemQuantityTextField);
		
		// add all labels and text fields
		formPanel.add(itemIdLabel, "");
		formPanel.add(itemIdTextField, "");
		formPanel.add(itemPartOrProductLabel, "");
		formPanel.add(itemPartOrProduct, "");
		formPanel.add(itemPartLabel, "");
		formPanel.add(itemPartComboBox, "");
		formPanel.add(itemTemplateLabel, "");
		formPanel.add(itemTemplateComboBox, "");
		formPanel.add(itemLocationLabel, "");
		formPanel.add(itemLocationComboBox, "");
		formPanel.add(itemQuantityLabel, "");
		formPanel.add(itemQuantityTextField, "");
		
		// get values for fields
		if(itemIndex != model.getLastItemID()) { // don't get values if new item			
			// get inventory item
			InventoryItemModel itemModel = model.getInventoryItemByIndex(itemIndex);
			itemTimeStamp = itemModel.getTimestamp();
			itemIdTextField.setText(String.valueOf(itemModel.getItemID()));
			itemPartComboBox.setSelectedItem(itemModel.getItemPart().getPartNumber());
			itemLocationComboBox.setSelectedIndex(itemModel.getItemLocationIndex());
			itemQuantityTextField.setText(String.valueOf(itemModel.getItemQuantity()));
			
			if(itemModel.getItemProductTemplateID() != 0) {
				itemPartOrProduct.setSelectedItem("Product");
				itemTemplateComboBox.setEnabled(true);
			} else {
				itemPartOrProduct.setSelectedItem("Part");
			}
			itemPartOrProduct.setEnabled(false); // can't change after added
			
			// initialize and add "edit/delete" buttons
			JButton editItemButton = new JButton("Edit Item");
			JButton deleteItemButton = new JButton("Delete Item");
			formPanel.add(editItemButton, "skip, split 2");
			formPanel.add(deleteItemButton, "");
		} else {			
			// sets item id to next available id
			itemIdTextField.setText(String.valueOf(model.getLastItemID()));
			
			// initialize and add "add" button
			JButton addItemButton = new JButton("Add Item");
			formPanel.add(addItemButton, "skip");
		}
			
		add(formPanel);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	public void registerListeners(InventoryItemController controller1) {
		Component[] components = formPanel.getComponents();
		for(Component component : components) {
			if(component instanceof AbstractButton) {
				AbstractButton button = (AbstractButton) component;
				button.addActionListener(controller1);
			}
		}
		
		itemPartOrProduct.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				if(arg0.getStateChange() == ItemEvent.SELECTED) {
					if(itemPartOrProduct.getSelectedItem().toString().equals("Product")) {
						itemPartComboBox.setEnabled(false);
						itemTemplateComboBox.setEnabled(true);
					} else {
						itemPartComboBox.setEnabled(true);
						itemTemplateComboBox.setEnabled(false);
					}
				}
			}
		});
	}
	
	public void close() {
		this.dispose();
	}
	
	public void reload(){
		InventoryItemModel itemModel = this.model.getInventoryItemByIndex(itemIndex);
		itemTimeStamp = itemModel.getTimestamp();
		itemIdTextField.setText(String.valueOf(itemModel.getItemID()));
		itemPartComboBox.setSelectedItem(itemModel.getItemPart().getPartNumber());
		itemLocationComboBox.setSelectedIndex(itemModel.getItemLocationIndex());
		itemQuantityTextField.setText(String.valueOf(itemModel.getItemQuantity()));
	}
	
	/////////////
	// GETTERS
	
	public int getItemID() {
		return Integer.parseInt(itemIdTextField.getText());
	}
	
	public String getItemPartNumber() {
		return itemPartComboBox.getSelectedItem().toString();
	}
	
	public int getItemLocationIndex() {
		return itemLocationComboBox.getSelectedIndex();
	}
	
	public int getItemQuantity() {
		return Integer.parseInt(itemQuantityTextField.getText());
	}
	
	public String getItemType() {
		return itemPartOrProduct.getSelectedItem().toString();
	}
	
	public String getItemTemplateNumber() {
		return itemTemplateComboBox.getSelectedItem().toString();
	}
	
	public Timestamp getTimestamp(){
		return this.itemTimeStamp;
	}
}
