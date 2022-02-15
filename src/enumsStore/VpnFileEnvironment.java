package enumsStore;


/**
 * This is enum for storing the settings necessary to create a folder and a file name with settings for connecting to a VPN server.
 * <p>
 * @author Aleksander Demichev
 * @version 1.0.0.0
 */

public enum VpnFileEnvironment {
	
	VPN_FILENAME ("VPN_"),
	VPN_DIRECTORY ("VPN_Configs");


	public String value;
	
	
	VpnFileEnvironment (String name) {
		this.value = name;
	}
    
	public String getFilename(String circuit) {
		return this.value + circuit + ".txt";
	}
	
	public String getDirectory() {
		return this.value;
	}
}
