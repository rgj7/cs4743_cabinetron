package parts;

import java.awt.Component;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import main.InventoryView;
import main.InventoryModel;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class PartDetailView extends JFrame {
	
	private JPanel formPanel;
	private JTextField partIdTextField, partNumberTextField, partNameTextField, partVendorTextField, partExternalNumberTextField;
	private JComboBox<String> partUnitComboBox;
	
	////////////////
	// CONSTRUCTOR
	
	public PartDetailView(InventoryModel model, InventoryView view, int partIndex) {
		super("Part Details");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		MigLayout layout = new MigLayout("wrap 2", "[right]10[grow,fill]", "[]10[]");
		formPanel = new JPanel(layout);
		
		// Field: ITEMID
		partIdTextField = new JTextField();
		partIdTextField.setEditable(false); // just show id, not editable
		JLabel partIdLabel = new JLabel("Part ID");
		partIdLabel.setLabelFor(partIdTextField);
		
		// Field: NUMBER
		partNumberTextField = new JTextField();
		JLabel partNumberLabel = new JLabel("Part Number");
		partNumberLabel.setLabelFor(partNumberTextField);

		// Field: NAME
		partNameTextField = new JTextField();
		JLabel partNameLabel = new JLabel("Part Name");
		partNameLabel.setLabelFor(partNameTextField);

		// Field: VENDOR
		partVendorTextField = new JTextField();
		JLabel partVendorLabel = new JLabel("Part Vendor");
		partVendorLabel.setLabelFor(partVendorTextField);
		
		// Field: UNIT
		partUnitComboBox = new JComboBox<String>(PartModel.UNITS);
		JLabel partUnitLabel = new JLabel("Unit of Quantity");
		partUnitLabel.setLabelFor(partUnitComboBox);
				
		// Field: EXTERNAL
		partExternalNumberTextField = new JTextField();
		JLabel partExternalNumberLabel = new JLabel("External Number");
		partExternalNumberLabel.setLabelFor(partExternalNumberTextField);
		
		// add all labels and text fields
		formPanel.add(partIdLabel, "");
		formPanel.add(partIdTextField, "");
		formPanel.add(partNumberLabel, "");
		formPanel.add(partNumberTextField, "");
		formPanel.add(partNameLabel, "");
		formPanel.add(partNameTextField, "");
		formPanel.add(partVendorLabel, "");
		formPanel.add(partVendorTextField, "");
		formPanel.add(partUnitLabel, "");
		formPanel.add(partUnitComboBox, "");
		formPanel.add(partExternalNumberLabel, "");
		formPanel.add(partExternalNumberTextField, "");

		// get values for fields
		if(partIndex != model.getLastPartID()) { // don't get values if new item
			// get inventory item
			PartModel partModel = model.getPartByIndex(partIndex);
			partIdTextField.setText(String.valueOf(partModel.getPartID()));
			partNumberTextField.setText(partModel.getPartNumber());
			partNameTextField.setText(partModel.getPartName());
			partVendorTextField.setText(partModel.getPartVendor());
			partUnitComboBox.setSelectedIndex(partModel.getPartUnitIndex());
			partExternalNumberTextField.setText(partModel.getExternalPartNumber());
			
			// initialize and add "edit/delete" buttons
			JButton editPartButton = new JButton("Edit Part");
			JButton deletePartButton = new JButton("Delete Part");
			formPanel.add(editPartButton, "skip, split 2");
			formPanel.add(deletePartButton, "");
		} else {
			// sets item id to next available id
			partIdTextField.setText(String.valueOf(model.getLastPartID()));
			
			// initialize and add "add" button
			JButton addPartButton = new JButton("Add Part");
			formPanel.add(addPartButton, "skip");
		}
		
		add(formPanel);
		pack(); // sizes window to fit components
		setLocationRelativeTo(null); // centers window
		setVisible(true);
	}
	
	public void registerListeners(PartController controller1) {
		Component[] components = formPanel.getComponents();
		for(Component component : components) {
			if(component instanceof AbstractButton) {
				AbstractButton button = (AbstractButton) component;
				button.addActionListener(controller1);
			}
		}
	}
	
	public void close() {
		this.dispose();
	}
	
	/////////////
	// GETTERS

	public int getPartID() {
		return Integer.parseInt(partIdTextField.getText());
	}
	
	public String getPartNumber() {
		return partNumberTextField.getText();
	}

	public String getPartName() {
		return partNameTextField.getText();
	}

	public String getPartVendor() {
		return partVendorTextField.getText();
	}
	
	public String getPartExternalNumber() {
		return partExternalNumberTextField.getText();
	}
	
	public int getUnitIndex() {
		return partUnitComboBox.getSelectedIndex();
	}
}
