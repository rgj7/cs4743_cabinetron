package templateparts;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.NumberFormat;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.NumberFormatter;

import templates.ProductTemplateModel;
import main.InventoryModel;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class ProductTemplatePartDetailView extends JFrame {
	
	private InventoryModel inventoryModel;
	private JPanel formPanel;
	private JTextField partIDTextField, prodTempIDTextField;
	private JComboBox<String> itemPartComboBox;
	private JFormattedTextField quantityTextField;
	
	public ProductTemplatePartDetailView(InventoryModel inventoryModel, ProductTemplateModel model, int prodTempPartIndex) {
		super("Product Template Details");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		this.inventoryModel = inventoryModel;
		
		MigLayout layout = new MigLayout("wrap 2", "[right]10[grow,fill]", "[]10[]");
		formPanel = new JPanel(layout);
		
		// Field: PTID
		prodTempIDTextField = new JTextField();
		prodTempIDTextField.setEditable(false); // just show id, not editable
		JLabel prodTempIdLabel = new JLabel("Product Template ID");
		prodTempIdLabel.setLabelFor(prodTempIDTextField);
		
		// Field: PART ID
		partIDTextField = new JTextField();
		partIDTextField.setEditable(false);
		JLabel partIdLabel = new JLabel("Part ID");
		partIdLabel.setLabelFor(partIDTextField);
		
		// Field: PART NAME
		itemPartComboBox = new JComboBox<String>(inventoryModel.getPartNames()); 
		JLabel itemPartLabel = new JLabel("Part Name");
		itemPartLabel.setLabelFor(itemPartComboBox);

		// initialize number formatter for quantity field
		NumberFormat numberFormat = NumberFormat.getIntegerInstance();
		numberFormat.setGroupingUsed(false);
		NumberFormatter numberFormatter = new NumberFormatter(numberFormat);
		numberFormatter.setValueClass(Integer.class);
		numberFormatter.setAllowsInvalid(false);
		// Field: QUANTITY
		quantityTextField = new JFormattedTextField(numberFormatter);
		quantityTextField.setText("1"); // since initial must be 1 or greater anyway
		JLabel itemQuantityLabel = new JLabel("Quantity");
		itemQuantityLabel.setLabelFor(quantityTextField);
		
		// add all labels and text fields
		formPanel.add(prodTempIdLabel, "");
		formPanel.add(prodTempIDTextField, "");
		formPanel.add(partIdLabel, "");
		formPanel.add(partIDTextField, "");
		formPanel.add(itemPartLabel, "");
		formPanel.add(itemPartComboBox, "");
		formPanel.add(itemQuantityLabel, "");
		formPanel.add(quantityTextField, "");
		
		if(prodTempPartIndex >= 0) { // don't get values if new item
			// get inventory item
			ProductTemplatePartModel prodTempPartModel = model.getProdTempPartByIndex(prodTempPartIndex);
			prodTempIDTextField.setText(String.valueOf(prodTempPartModel.getProductTemplateID()));
			partIDTextField.setText(String.valueOf(prodTempPartModel.getPartID()));
			itemPartComboBox.setSelectedItem(inventoryModel.getPartByID(prodTempPartModel.getPartID()).getPartName());
			quantityTextField.setText(String.valueOf(prodTempPartModel.getPartQuantity()));
			
			// initialize and add "edit/delete" buttons
			JButton editProdTempButton = new JButton("Edit");
			JButton deleteProdTempButton = new JButton("Delete");
			formPanel.add(editProdTempButton, "skip, split 2");
			formPanel.add(deleteProdTempButton, "");
		} else {
			// sets item id to next available id
			prodTempIDTextField.setText(String.valueOf(model.getProductTemplateID()));
			
			// initialize and add "add" button
			JButton addProdTempButton = new JButton("Add");
			formPanel.add(addProdTempButton, "skip");
		}
		
		add(formPanel);
		pack(); // sizes window to fit components
		setLocationRelativeTo(null); // centers window
		setVisible(true);
	}
	
	public void registerListeners(ProductTemplatePartController controller1) {
		Component[] components = formPanel.getComponents();
		for(Component component : components) {
			if(component instanceof AbstractButton) {
				AbstractButton button = (AbstractButton) component;
				button.addActionListener(controller1);
			}
		}
		
		itemPartComboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				if(arg0.getStateChange() == ItemEvent.SELECTED) {
					partIDTextField.setText(String.valueOf(inventoryModel.getPartByName(itemPartComboBox.getSelectedItem().toString()).getPartID()));
				}
			}
		});
	}
	
	public void close() {
		this.dispose();
	}
	
	/////////////
	// GETTERS
	
	public int getProductTemplateID() {
		return Integer.parseInt(prodTempIDTextField.getText());
	}
	
	public int getProductTemplatePartID() {
		return Integer.parseInt(partIDTextField.getText());
	}
	
	public int getItemQuantity() {
		return Integer.parseInt(quantityTextField.getText());
	}
}
