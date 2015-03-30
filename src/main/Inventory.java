package main;

import javax.swing.JFrame;
import javax.swing.Timer;

import login.AuthenticatorModel;
import login.LoginController;
import login.LoginView;
import login.SessionModel;
import database.GatewayException;
import database.ItemTableGateway;
import database.PartTableGateway;
import database.ProductTemplateGateway;
import database.TemplatePartGateway;

public class Inventory {
	public static void main(String[] args) {
	
		SessionModel session = null;

		while(session == null){
		   boolean actionOccured = false;
		   AuthenticatorModel authenticator = new AuthenticatorModel(session);
		   LoginView loginview = new LoginView();
		   LoginController loginController = new LoginController(authenticator, loginview);
		   loginview.registerListeners(loginController);
		   while(!loginController.hasActionOccured()){
			   try {
				   Thread.sleep(1000);
			   } catch (InterruptedException e) {
				   e.printStackTrace();
			   }
		   }
           session = authenticator.getSession();
	//TESTING TESTING
           if(session == null){
		      System.out.println("session is null");
		   }
		   else{
		      System.out.println(session.getUser().getName());
		   }
		}
        
		
		PartTableGateway ptg = null;
		try {
			ptg = new PartTableGateway();		
		} catch(GatewayException e) {
			System.out.println("Error creating DB connection: " + e.getMessage());
			System.exit(0);
		}
		
		ItemTableGateway itg = null;
		try {
			itg = new ItemTableGateway(ptg);		
		} catch(GatewayException e) {
			System.out.println("Error creating DB connection: " + e.getMessage());
			System.exit(0);
		}
		
		ProductTemplateGateway pg = null;
		try{
			pg = new ProductTemplateGateway();
		} catch(GatewayException e){
			System.out.println("Error creating DB connection: " + e.getMessage());
			System.exit(0);
		}
		
		TemplatePartGateway tpg = null;
		try{
			tpg = new TemplatePartGateway();
		} catch(GatewayException e){
			System.out.println("Error creating DB connection: " + e.getMessage());
			System.exit(0);
		}
		
		// model
		InventoryModel model = new InventoryModel(ptg, itg, pg, tpg);
		// view
		InventoryView view = new InventoryView(model);
		// controllers
		InventoryController menuController = new InventoryController(model, view);
		InventoryTableController tableController = new InventoryTableController(model, view);
		ReloadController reloadController = new ReloadController(model, view);

		// register controllers
		view.registerListeners(menuController, tableController);
		
		// configure view
		view.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		view.setSize(800, 480);
		view.setLocationRelativeTo(null);
		view.setVisible(true);
         
		Timer timer = new Timer(2000, reloadController);
	    timer.start();
				
	}
}
