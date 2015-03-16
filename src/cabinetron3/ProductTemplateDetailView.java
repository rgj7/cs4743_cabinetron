package cabinetron3;

import java.awt.Color;
import java.awt.Component;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class ProductTemplateDetailView extends JFrame {

	private JPanel formPanel;
	private JTextField prodTempIdTextField, prodTempNumberTextField;
	private JTextArea prodTempDescriptionTextArea;
	
	public ProductTemplateDetailView(InventoryModel model, InventoryListView view, int prodTempIndex) {
		super("Product Template Details");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		MigLayout layout = new MigLayout("wrap 2", "[right]10[grow,fill]", "[]10[]");
		formPanel = new JPanel(layout);
		
		// Field: PTID
		prodTempIdTextField = new JTextField();
		prodTempIdTextField.setEditable(false); // just show id, not editable
		JLabel prodTempIdLabel = new JLabel("ID");
		prodTempIdLabel.setLabelFor(prodTempIdTextField);
		
		// Field: NUMBER
		prodTempNumberTextField = new JTextField();
		JLabel prodTempNumberLabel = new JLabel("Number");
		prodTempNumberLabel.setLabelFor(prodTempNumberTextField);

		// Field: DESCRIPTION
		prodTempDescriptionTextArea = new JTextArea(5, 20);
		prodTempDescriptionTextArea.setLineWrap(true);
		prodTempDescriptionTextArea.setWrapStyleWord(true);
		prodTempDescriptionTextArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		JLabel prodTempDescriptionLabel = new JLabel("Description");
		prodTempDescriptionLabel.setLabelFor(prodTempDescriptionTextArea);
		
		// add all labels and text fields
		formPanel.add(prodTempIdLabel, "");
		formPanel.add(prodTempIdTextField, "");
		formPanel.add(prodTempNumberLabel, "");
		formPanel.add(prodTempNumberTextField, "");
		formPanel.add(prodTempDescriptionLabel, "");
		formPanel.add(prodTempDescriptionTextArea, "");
		
		// get values for fields
		if(prodTempIndex != model.getLastProductTemplateID()) { // don't get values if new item
			// get inventory item
			ProductTemplateModel prodTempModel = model.getProdTempByIndex(prodTempIndex);
			prodTempIdTextField.setText(String.valueOf(prodTempModel.getProductTemplateID()));
			prodTempNumberTextField.setText(prodTempModel.getProductTemplateNumber());
			prodTempDescriptionTextArea.setText(prodTempModel.getProductTemplateDescription());
			
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
		
		add(formPanel);
		pack(); // sizes window to fit components
		setLocationRelativeTo(null); // centers window
		setVisible(true);
	}
	
	public void registerListeners(ProductTemplateController controller1) {
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
	
	public int getProductTemplateID() {
		return Integer.parseInt(prodTempIdTextField.getText());
	}
	
	public String getProductTemplateNumber() {
		return prodTempNumberTextField.getText();
	}

	public String getProductTemplateDescription() {
		return prodTempDescriptionTextArea.getText();
	}
}
