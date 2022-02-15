package enumsStore;

import java.util.Arrays;

/**
 * The enum class for storing the names of the columns of the database table "credentials_store".
 * <p>
 * @author Aleksander Demichev
 * @version 1.0.0.0
 */

public enum CredentialsFields {

	CHANNEL_LOGIN("login_kanal"),
	APPLICATION_LOGIN("login_prikl"),
	CHANNEL_PASSWORD("pwd_kanal"),
	APPLICATION_PASSWORD("pwd_prikl"),
	CHANNEL_PASSWORD_CHANGE_DATE("dateset_kanal"),
	APPLICATION_PASSWORD_CHANGE_DATE("dateset_prikl"),
	OLD_CHANNEL_PASSWORD("pwd_kanal_old"),
	OLD_APPLICATION_PASSWORD("pwd_prikl_old"),
	PSBR_WORK_VPN_FILE ("psbr_work_vpn_file"),
	PSBR_TEST_VPN_FILE ("psbr_test_vpn_file"),
	SPFS_WORK_VPN_FILE ("spfs_work_vpn_file"),
	SPFS_TEST_VPN_FILE ("spfs_test_vpn_file"),	
	MAIN_VPN_IP_PROM ("main_vpn_ip_prom"),
	REZERV_VPN_IP_PROM ("rezerv_vpn_ip_prom"),
	MAIN_VPN_IP_TEST ("main_vpn_ip_test"),
	REZERV_VPN_IP_TEST ("rezerv_vpn_ip_test");

	public static final String TABLE_NAME = ("credentials_store");

	public final String COLUMN_NAME;
	
	CredentialsFields (String name) {
		this.COLUMN_NAME = name;
	}
    
	/* Get table column name */
	public String toString() {
		return this.COLUMN_NAME;
	}
	
	/* Get an array of table column names */
    public static String[] names () {
        return Arrays.toString(values()).replaceAll("\\[|]", "").split(", ");
    }
    
    
}
