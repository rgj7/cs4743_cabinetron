package login;

public class SessionModel {

    private UserModel user;
	private boolean inventoryManager;
	private boolean productManager;
	private boolean admin;
	
	public SessionModel(UserModel u, boolean i, boolean p, boolean a){
		this.setUser(u);
		this.inventoryManager = i;
		this.productManager = p;
		this.admin = a;
	}
	
	
	public boolean canViewProductTemplates(){
		if(productManager || admin){
			return true;
		}
		else 
			return false;
	}
	
	public boolean canAddProductTemplates(){
		if(productManager || admin){
			return true;
		}
		else 
			return false;
	}
	
	public boolean canDeleteProductTemplates(){
		if(productManager || admin){
			return true;
		}
		else 
			return false;
	}
	
	public boolean canCreateProducts(){
		if(productManager || admin){
			return true;
		}
		else 
			return false;
	}
	
	public boolean canViewInventory(){
		return true;
	}
	
	public boolean canAddInventory(){
		if(inventoryManager || admin){
			return true;
		}
		else 
			return false;
	}
	
	public boolean canViewParts(){
		return true;
	}
	
	public boolean canAddParts(){
		if(inventoryManager || admin){
			return true;
		}
		else 
			return false;
	}
	
	public boolean canDeleteParts(){
		if(admin){
			return true;
		}
		else 
			return false;
	}
	
	public boolean canDeleteInventory(){
		if(admin){
			return true;
		}
		else 
			return false;
	}


	public UserModel getUser() {
		return user;
	}


	public void setUser(UserModel user) {
		this.user = user;
	}
	
	
}
