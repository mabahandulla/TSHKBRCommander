package daoPattern;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;

import enumsStore.CredentialsFields;
import enumsStore.CredentialsSwitches;

/**
 * This is a class for storing data obtained from a database from a "credentials_store" table.
 * <p>
 * @author Aleksander Demichev
 * @version 1.0.0.0
 */

public final class CredentialsStore {

	private int Id;
	
	private String  login_kanal,
					login_prikl,
					pwd_kanal,
					pwd_prikl,
					pwd_kanal_old,
					pwd_prikl_old;
	
	private long	dateset_kanal,
					dateset_prikl;
	

	private Boolean psbr_work_vpn_file,
					psbr_test_vpn_file,
					spfs_work_vpn_file,
					spfs_test_vpn_file,
					main_vpn_ip_prom,
					rezerv_vpn_ip_prom,
					main_vpn_ip_test,
					rezerv_vpn_ip_test;

	

	public Object clone() {
		
		CredentialsStore clonedObject = new CredentialsStore();
		
		clonedObject.Id = this.Id;
		clonedObject.login_kanal = this.login_kanal;
		clonedObject.login_prikl = this.login_prikl;
		clonedObject.pwd_kanal = this.pwd_kanal;
		clonedObject.pwd_prikl = this.pwd_prikl;
		clonedObject.pwd_kanal_old = this.pwd_kanal_old;
		clonedObject.pwd_prikl_old = this.pwd_prikl_old;
		clonedObject.dateset_kanal = this.dateset_kanal;
		clonedObject.dateset_prikl = this.dateset_prikl;
		clonedObject.psbr_work_vpn_file = this.psbr_work_vpn_file;
		clonedObject.psbr_test_vpn_file = this.psbr_test_vpn_file;
		clonedObject.spfs_work_vpn_file = this.spfs_work_vpn_file;
		clonedObject.spfs_test_vpn_file = this.spfs_test_vpn_file;
		clonedObject.main_vpn_ip_prom = this.main_vpn_ip_prom;
		clonedObject.rezerv_vpn_ip_prom = this.rezerv_vpn_ip_prom;
		clonedObject.main_vpn_ip_test = this.main_vpn_ip_test;
		clonedObject.rezerv_vpn_ip_test = this.rezerv_vpn_ip_test;
		
		return clonedObject;
	 }

	
	
	/*
	 * Set Id
	 */
	public void setId(int Id) {
		this.Id = Id;
	}
	
	/*
	 * Get Id
	 */
	public int getId() {
		return this.Id;
	}
	
	
	/*
	 * Set login_kanal
	 */
	public void setLoginKanal(String login_kanal) {
		this.login_kanal = login_kanal;
	}
	
	/*
	 * Get login_kanal
	 */
	public String getLoginKanal() {
		return this.login_kanal;
	}
	
	
	/*
	 * Set login_prikl
	 */
	public void setLoginPrikl(String login_prikl) {
		this.login_prikl = login_prikl;
	}
	
	/*
	 * Get login_prikl
	 */
	public String getLoginPrikl() {
		return this.login_prikl;
	}
	
	/*
	 * Set pwd_kanal
	 */
	public void setPasswordKanal(String pwd_kanal) {
		this.pwd_kanal = pwd_kanal;
	}
	
	/*
	 * Get pwd_kanal
	 */
	public String getPasswordKanal() {
		return this.pwd_kanal;
	}
	
	/*
	 * Set pwd_prikl
	 */
	public void setPasswordPrikl(String pwd_prikl) {
		this.pwd_prikl = pwd_prikl;
	}
	
	/*
	 * Get pwd_prikl
	 */
	public String getPasswordPrikl() {
		return this.pwd_prikl;
	}
	
	
	/*
	 * Set dateset_kanal
	 */
	public void setDateSetKanal(long dateset_kanal) {
		this.dateset_kanal = dateset_kanal;
	}
	
	/*
	 * Get dateset_kanal
	 */
	public long getDateSetKanal() {
		return this.dateset_kanal;
	}
	
	/*
	 * Set dateset_prikl
	 */
	public void setDateSetPrikl(long dateset_prikl) {
		this.dateset_prikl = dateset_prikl;
	}
	
	/*
	 * Get dateset_prikl
	 */
	public long getDateSetPrikl() {
		return this.dateset_prikl;
	}
	
	
	/*
	 * Set pwd_kanal_old
	 */
	public void setPasswordKanalOld(String pwd_kanal_old) {
		this.pwd_kanal_old = pwd_kanal_old;
	}
	
	/*
	 * Get pwd_kanal_old
	 */
	public String getPasswordKanalOld() {
		return this.pwd_kanal_old;
	}
	
	/*
	 * Set pwd_prikl_old
	 */
	public void setPasswordPriklOld(String pwd_prikl_old) {
		this.pwd_prikl_old = pwd_prikl_old;
	}
	
	/*
	 * Get pwd_prikl_old
	 */
	public String getPasswordPriklOld() {
		return this.pwd_prikl_old;
	}
	
	/*
	 * Set psbr_work_vpn_file
	 */
	public void setPsbrWorkVpnFileState(Boolean psbr_work_vpn_file) {
		this.psbr_work_vpn_file = this.isBooleanNotNull(psbr_work_vpn_file);
	}

	/*
	 * Get psbr_work_vpn_file
	 */
	public boolean getPsbrWorkVpnFileState() {
		return this.psbr_work_vpn_file;
	}
	
	/*
	 * Set psbr_test_vpn_file
	 */
	public void setPsbrTestVpnFileState(Boolean psbr_test_vpn_file) {
		this.psbr_test_vpn_file = this.isBooleanNotNull(psbr_test_vpn_file);
	}

	/*
	 * Get psbr_test_vpn_file
	 */
	public boolean getPsbrTestVpnFileState() {
		return this.psbr_test_vpn_file;
	}
	
	/*
	 * Set spfs_work_vpn_file
	 */
	public void setSpfsWorkVpnFileState(Boolean spfs_work_vpn_file) {
		this.spfs_work_vpn_file = this.isBooleanNotNull(spfs_work_vpn_file);
	}

	/*
	 * Get spfs_work_vpn_file
	 */
	public boolean getSpfsWorkVpnFileState() {
		return this.spfs_work_vpn_file;
	}
	
	/*
	 * Set spfs_test_vpn_file
	 */
	public void setSpfsTestVpnFileState(Boolean spfs_test_vpn_file) {
		this.spfs_test_vpn_file = this.isBooleanNotNull(spfs_test_vpn_file);
	}

	/*
	 * Get spfs_test_vpn_file
	 */
	public boolean getSpfsTestVpnFileState() {
		return this.spfs_test_vpn_file;
	}

	/*
	 * Set main_vpn_ip_prom
	 */
	public void setMainVpnIpPromState(Boolean main_vpn_ip_prom) {
		this.main_vpn_ip_prom = this.isBooleanNotNull(main_vpn_ip_prom);
	}

	/*
	 * Get main_vpn_ip_prom
	 */
	public boolean getMainVpnIpPromState() {
		return this.main_vpn_ip_prom;
	}
	
	/*
	 * Set rezerv_vpn_ip_prom
	 */
	public void setRezervVpnIpPromState(Boolean rezerv_vpn_ip_prom) {
		this.rezerv_vpn_ip_prom = this.isBooleanNotNull(rezerv_vpn_ip_prom);
	}

	/*
	 * Get rezerv_vpn_ip_prom
	 */
	public boolean getRezervVpnIpPromState() {
		return this.rezerv_vpn_ip_prom;
	}

	
	/*
	 * Set main_vpn_ip_test
	 */
	public void setMainVpnIpTestState(Boolean main_vpn_ip_test) {
		this.main_vpn_ip_test = this.isBooleanNotNull(main_vpn_ip_test);
	}

	/*
	 * Get main_vpn_ip_test
	 */
	public boolean getMainVpnIpTestState() {
		return this.main_vpn_ip_test;
	}
	
	/*
	 * Set rezerv_vpn_ip_test
	 */
	public void setRezervVpnIpTestState(Boolean rezerv_vpn_ip_test) {
		this.rezerv_vpn_ip_test = this.isBooleanNotNull(rezerv_vpn_ip_test);
	}

	/*
	 * Get rezerv_vpn_ip_test
	 */
	public boolean getRezervVpnIpTestState() {
		return this.rezerv_vpn_ip_test;
	}
	
		
	/*
	 * Check if Boolean value not null
	 */
	private Boolean isBooleanNotNull(Boolean checkedValue) {
		return checkedValue.equals(null) ? false : checkedValue;
	}
	
	
	/* Get this.class Boolean methods */ 
	public LinkedHashMap<String, Method[]> getCheckBoxActions(CredentialsSwitches credentialsSwitch, CredentialsStore credentialsStore) throws NoSuchMethodException, SecurityException {

		
		LinkedHashMap<String, Method[]> columnsAndMethodsMatchStore = new LinkedHashMap<String, Method[]>(1);
		
		//No parameters
		Class<?> noparams[] = {};
			
		//String parameter
		Class<?>[] paramBoolean = new Class[1];	
		paramBoolean[0] = Boolean.class;
		
		Method[] definedMethods = new Method[2];
		
			
			switch (credentialsSwitch) {
			case PSBR_PROM:
				// Get PSBR WORK methods for get data from checkbox				
				definedMethods[0] = credentialsStore.getClass().getMethod("getPsbrWorkVpnFileState", noparams);
				definedMethods[1] = credentialsStore.getClass().getMethod("setPsbrWorkVpnFileState", paramBoolean);
				columnsAndMethodsMatchStore.put(CredentialsFields.PSBR_WORK_VPN_FILE.toString(), definedMethods);
				break;
			case PSBR_TEST:
				// Get PSBR TEST methods for get data from checkbox
				definedMethods[0] = credentialsStore.getClass().getMethod("getPsbrTestVpnFileState", noparams);
				definedMethods[1] = credentialsStore.getClass().getMethod("setPsbrTestVpnFileState", paramBoolean);
				columnsAndMethodsMatchStore.put(CredentialsFields.PSBR_TEST_VPN_FILE.toString(), definedMethods);
				break;
			case SPFS_PROM:
				// Get SPFS WORK methods for get data from checkbox
				definedMethods[0] = credentialsStore.getClass().getMethod("getSpfsWorkVpnFileState", noparams);
				definedMethods[1] = credentialsStore.getClass().getMethod("setSpfsWorkVpnFileState", paramBoolean);
				columnsAndMethodsMatchStore.put(CredentialsFields.SPFS_WORK_VPN_FILE.toString(), definedMethods);
				break;
			case SPFS_TEST:
				// Get SPFS TEST methods for get data from checkbox
				definedMethods[0] = credentialsStore.getClass().getMethod("getSpfsTestVpnFileState", noparams);
				definedMethods[1] = credentialsStore.getClass().getMethod("setSpfsTestVpnFileState", paramBoolean);
				columnsAndMethodsMatchStore.put(CredentialsFields.SPFS_TEST_VPN_FILE.toString(), definedMethods);
				break;
			}
		
		return columnsAndMethodsMatchStore;
	}
	
}
