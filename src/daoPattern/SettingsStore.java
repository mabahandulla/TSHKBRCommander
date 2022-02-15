package daoPattern;

/**
 * This is a class for storing data obtained from a database from a "settings" table.
 * <p>
 * @author Aleksander Demichev
 * @version 1.0.0.0
 */

public final class SettingsStore {

	/* Data loaded from Database */
	private int pwd_length;
	private String databaseVersion;
	private boolean psbr_visible,
					spfs_visible,
					interface_selected,
					check_test_dates;

	
	
	public Object clone() {
		
		SettingsStore clonedObject = new SettingsStore();
		
		clonedObject.pwd_length = this.pwd_length;
		clonedObject.databaseVersion = this.databaseVersion;
		clonedObject.psbr_visible = this.psbr_visible;
		clonedObject.spfs_visible = this.spfs_visible;
		clonedObject.interface_selected = this.interface_selected;
		clonedObject.check_test_dates = this.check_test_dates;
		
		return clonedObject;
	 }
	

	/*
	 * Set pwd_length
	 */
	public void setPasswordLength(int pwd_length) {
		this.pwd_length = pwd_length;
	}

	/*
	 * Get pwd_length
	 */
	public int getPasswordLength() {
		return this.pwd_length;
	}
	
	/*
	 * Set psbr_visible
	 */
	public void setPsbrState(boolean psbr_visible) {
		this.psbr_visible = this.isBooleanNotNull(psbr_visible);
	}

	/*
	 * Get psbr_visible
	 */
	public boolean getPsbrState() {
		return this.psbr_visible;
	}

	/*
	 * Set spfs_visible
	 */
	public void setSpfsState(boolean spfs_visible) {
		this.spfs_visible = this.isBooleanNotNull(spfs_visible);
	}

	/*
	 * Get spfs_visible
	 */
	public boolean getSpfsState() {
		return this.spfs_visible;
	}

	/*
	 * Get interface_selected
	 */
	public boolean getIterfaceState() {
		return this.interface_selected;
	}
	
	/*
	 * Set interface_selected
	 */
	public void setIterfaceState(boolean interface_selected) {
		this.interface_selected = this.isBooleanNotNull(interface_selected);
	}

	/*
	 * Get check_test_dates
	 */
	public boolean getCheckTestDatesState() {
		return this.check_test_dates;
	}

	/*
	 * Set check_test_dates
	 */
	public void setCheckTestDatesState(boolean check_test_dates) {
		this.check_test_dates = this.isBooleanNotNull(check_test_dates);
	}
	
	/*
	 * Set databaseVersion
	 */
	public void setDatabaseVersion(String databaseVersion) {
		this.databaseVersion = databaseVersion;
	}

	/*
	 * Get databaseVersion
	 */
	public String getDatabaseVersion() {
		return this.databaseVersion;
	}
	
	
	/*
	 * Check if Boolean value not null
	 */
	private Boolean isBooleanNotNull(Boolean checkedValue) {
		return checkedValue.equals(null) ? false : checkedValue;
	}
	
}
