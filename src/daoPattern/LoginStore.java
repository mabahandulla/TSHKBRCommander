package daoPattern;

/**
 * This is a class for storing data obtained from a database from a "admin_login" table.
 * <p>
 * @author Aleksander Demichev
 * @version 1.0.0.0
 */

public final class LoginStore {

	
	private String	login,
					password; 


	public Object clone() {
		
		LoginStore clonedObject = new LoginStore();
		
		clonedObject.login = this.login;
		clonedObject.password = this.password;
		
		return clonedObject;
	 }	
	
	/* Get login */
	public String getLogin() {
		return this.login;
	}

	/* Set login */
	public void setLogin(String login) {
		this.login = login;
	}
	

	/* Get password */
	public String getPassword() {
		return this.password;
	}

	/* Set password */
	public void setPassword(String password) {
		this.password = password;
	}
}
