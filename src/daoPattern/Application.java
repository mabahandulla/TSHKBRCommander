package daoPattern;

import java.io.File;
import javax.sql.DataSource;

import org.sqlite.SQLiteDataSource;

import enumsStore.DatabaseEnvironment;

/**
 * The enum class that provides access to a data source to connect to a database.
 * <p>
 * @author Aleksander Demichev
 * @version 1.0.0.0
 */

public enum Application {
	
	INSTANCE;

    private DataSource dataSource;

    public DataSource dataSource(String rootDirectory) {
    	
        if (this.dataSource == null) {
    		SQLiteDataSource dataSource = new SQLiteDataSource();
    		dataSource.setUrl("jdbc:sqlite:" 
	    		+ rootDirectory
	    		+ File.separator 
	    		+ DatabaseEnvironment.DATABASE_DIRECTORY.getValue() 
	    		+ File.separator 
	    		+ DatabaseEnvironment.DATABASE_FILENAME.getValue());
    		
            this.dataSource = dataSource;
        }

        return this.dataSource;
    }

}
