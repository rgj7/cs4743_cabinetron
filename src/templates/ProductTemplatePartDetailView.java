package templates;

import java.text.NumberFormat;

import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import javax.swing.JTextField;
import javax.swing.text.NumberFormatter;

import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class ProductTemplatePartDetailView extends JFrame {
	
	private JPanel formPanel;
	private JTextField partIDTextField, prodTempIDTextField;
	private JFormattedTextField quantityTextField;
	
	public ProductTemplatePartDetailView(int prodTempPartIndex) {
		super("Product Template Details");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		MigLayout layout = new MigLayout("wrap 2", "[right]10[grow,fill]", "[]10[]");
		formPanel = new JPanel(layout);
		
		// Field: PTID
		prodTempIDTextField = new JTextField();
		prodTempIDTextField.setEditable(false); // just show id, not editable
		JLabel prodTempIdLabel = new JLabel("Product Template ID");
		prodTempIdLabel.setLabelFor(prodTempIDTextField);
		
		// Field: PART ID
		partIDTextField = new JTextField();
		JLabel partIdLabel = new JLabel("Part ID");
		partIdLabel.setLabelFor(partIDTextField);

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
		formPanel.add(itemQuantityLabel, "");
		formPanel.add(quantityTextField, "");
		
		// get values for fields
		/*
		if(prodTempPartIndex != model.getLastProductTemplateID()) { // don't get values if new item
			// get inventory item
			ProductTemplateModel prodTempModel = model.getProdTempByIndex(prodTempIndex);
			prodTempIDTextField.setText();
			partIDTextField.setText();
			quantityTextField.setText();
			
			// initialize and add "edit/delete" buttons
			JButton editProdTempButton = new JButton("Edit");
			JButton deleteProdTempButton = new JButton("Delete");
			formPanel.add(editProdTempButton, "skip, split 2");
			formPanel.add(deleteProdTempButton, "");
		} else {
			// sets item id to next available id
			prodTempIdTextField.setText(String.valueOf(model.getLastProductTemplateID()));
			
			// initialize and add "add" button
			JButton addProdTempButton = new JButton("Add");
			formPanel.add(addProdTempButton, "skip");
		}
		*/
		
		add(formPanel);
		pack(); // sizes window to fit components
		setLocationRelativeTo(null); // centers window
		setVisible(true);
	}
}
