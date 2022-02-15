package enumsStore;

import java.util.Arrays;

/**
 * The enum class for storing the names of the columns of the database table "admin_login".
 * <p>
 * @author Aleksander Demichev
 * @version 1.0.0.0
 */

public enum AdminFields {
	
	LOGIN ("login"),
	PASSWORD ("password");


	public static final String TABLE_NAME = ("admin_login");

	public final String COLUMN_NAME;
	
	
	AdminFields (String name) {
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
