package enumsStore;

import java.util.Arrays;

/**
 * The enum class for storing the names of the columns of the database table "settings".
 * <p>
 * @author Aleksander Demichev
 * @version 1.0.0.0
 */

public enum SettingsFields {

	PASSWORD_LEHGTH ("pwd_length"),
	PSBR_BLOCK ("psbr_visible"),
	SPFS_BLOCK ("spfs_visible"),
	INTERFACE_SELECTED ("interface_selected"),
	CHECK_TEST_DATES ("check_test_dates");

	public static final String TABLE_NAME = "settings";

	public final String COLUMN_NAME;
	
	
	SettingsFields (String name) {
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
