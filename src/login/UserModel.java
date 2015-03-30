package login;

public class UserModel {

	private String name;
	private String email;
	
	public UserModel(String n, String e){
		this.setName(n);
		this.setEmail(e);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
