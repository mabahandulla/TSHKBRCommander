package enumsStore;

/**
 * The enum class for storing database constants: the name of the database directory and the name of the database file.
 * <p>
 * @author Aleksander Demichev
 * @version 1.0.0.0
 */

public enum DatabaseEnvironment {

	DATABASE_FILENAME ("TSHKBRDB.s3db"),
	DATABASE_DIRECTORY ("Database");


	public final String ENVIRONMENT_VALUE;
	
	
	DatabaseEnvironment (String name) {
		this.ENVIRONMENT_VALUE = name;
	}
    
	public String getValue() {
		return this.ENVIRONMENT_VALUE;
	}
	

}
