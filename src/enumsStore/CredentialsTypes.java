package enumsStore;

/**
 * The enum class for storing switches for choosing the type of credentials.
 * <p>
 * @author Aleksander Demichev
 * @version 1.0.0.0
 */

public enum CredentialsTypes {

	CHANNEL("КАНАЛЬНЫй"),
	APPLICATION("ПРИКЛАДНОЙ");
	
	public final String CREDENTIALS_TYPE;
	
	CredentialsTypes (String name) {
		this.CREDENTIALS_TYPE = name;
	}
    
	public String getName() {
		return this.CREDENTIALS_TYPE;
	}
	
}
