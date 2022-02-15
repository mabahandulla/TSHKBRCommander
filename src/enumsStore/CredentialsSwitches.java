package enumsStore;

/**
 * The enum class for storing switches for the type of credentials
 * that are used for the credentials window title and for generating the name
 * of the file with settings for connecting to the VPN server.
 * <p>
 * @author Aleksander Demichev
 * @version 1.0.0.0
 */

public enum CredentialsSwitches {
	
    PSBR_PROM ("Промышленная ПС БР"),
	PSBR_TEST ("Тестовая ПС БР"),
	SPFS_PROM ("Промышленная СПФС"),
    SPFS_TEST ("Тестовая СПФС");
    
	private String credentials;

    private CredentialsSwitches(String credentials) {
        this.credentials = credentials;
    }
    
	public String getTitle() {
		return this.credentials;
	}
	
    /**
     * This method looks for the enum value and returns the name of this enum constant.
     *
     * @param String of the enum value.
     * @return the name of this enum constant.
     */
    public String getEnumByString(String code){
        for(CredentialsSwitches enumValue : CredentialsSwitches.values()){
            if(enumValue.credentials.equals(code)) return enumValue.name();
        }
        return null;
    }
}
