package daoPattern;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;

import enumsStore.SettingsFields;
import utilities.CreateErrorLog;

/**
 * This is a class for getting data from a "settings" table.
 * <p>
 * @author Aleksander Demichev
 * @version 1.0.0.0
 */

public final class SettingsDAO implements Dao <SettingsStore>{

	private CreateErrorLog errorWriter;
    private DataSource dataSource;

    
    /*
     * Get DataSource and error writer class.
     */
    public SettingsDAO(DataSource dataSource, CreateErrorLog errorWriter) {
        this.dataSource = dataSource;
    	this.errorWriter = errorWriter;
    }

    
    /* Find by ID method implementation */
    public SettingsStore getData(int id) {
    	
    	SettingsStore settingsStore = null;
    	StringBuilder columnsNames = new StringBuilder();

		for (String tableField : SettingsFields.names()) {
			columnsNames.append(tableField + ", ");
		}

    	
		try (Connection connection = this.dataSource.getConnection()) {
			try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT " + columnsNames.toString()
					+ " sqlite_version() AS dbVersion FROM " + SettingsFields.TABLE_NAME + " WHERE ID=" + id)) {
		    	
		      try (ResultSet resultSet = preparedStatement.executeQuery()) {
		    	  
		    	  settingsStore = new SettingsStore();

					while (resultSet.next()) {
						settingsStore.setPasswordLength(resultSet.getInt(SettingsFields.PASSWORD_LEHGTH.toString()));
						settingsStore.setPsbrState(Boolean.parseBoolean(resultSet.getString(SettingsFields.PSBR_BLOCK.toString())));
						settingsStore.setSpfsState(Boolean.parseBoolean(resultSet.getString(SettingsFields.SPFS_BLOCK.toString())));
						settingsStore.setIterfaceState(Boolean.parseBoolean(resultSet.getString(SettingsFields.INTERFACE_SELECTED.toString())));
						settingsStore.setCheckTestDatesState(Boolean.parseBoolean(resultSet.getString(SettingsFields.CHECK_TEST_DATES.toString())));
						settingsStore.setDatabaseVersion(resultSet.getString("dbVersion"));
					}
		      }
		    }
		    
		} catch (SQLException e) {
			this.errorWriter.showSQLExceptionMessage(e);
		}
		
		return settingsStore;
    }
    
    
    
    /* Update values from SettingsStore in the settings table */
    public void updateData(SettingsStore settingsStore) {
    	
		try (Connection connection = this.dataSource.getConnection()) {

		    try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE "
					+ SettingsFields.TABLE_NAME + " SET "
					+ SettingsFields.PASSWORD_LEHGTH.toString() + "='" + settingsStore.getPasswordLength() + "', "
					+ SettingsFields.PSBR_BLOCK.toString() + "='" + settingsStore.getPsbrState() + "', "
					+ SettingsFields.SPFS_BLOCK.toString() + "='" + settingsStore.getSpfsState() + "', "
				    + SettingsFields.INTERFACE_SELECTED.toString() + "='" + settingsStore.getIterfaceState() + "', "
				    + SettingsFields.CHECK_TEST_DATES.toString() + "='" + settingsStore.getCheckTestDatesState() + "' "
					+ " WHERE ID=1;")) {
				
		    	preparedStatement.executeUpdate();
		    }

		} catch (SQLException e) {
			
			this.errorWriter.showSQLExceptionMessage(e);
		}
    }
    
    
    /* Update password length in the settings table */
    public void updatePasswordLenght(String passwordLenght) {
    	
		try (Connection connection = this.dataSource.getConnection()) {
		    try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE "
					+ SettingsFields.TABLE_NAME + " SET "
					+ SettingsFields.PASSWORD_LEHGTH.toString() + "='" + passwordLenght + "' "
					+ " WHERE ID=1;")) {
				
		    	preparedStatement.executeUpdate();
		    }

		} catch (SQLException e) {
			
			this.errorWriter.showSQLExceptionMessage(e);
		}
    }
    
    
    /* Update check_test_dates column value in the settings table */
    public void updateCheckTestDatesState(boolean checkTestDatesState) {
    	
		try (Connection connection = this.dataSource.getConnection()) {
		    try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE "
					+ SettingsFields.TABLE_NAME + " SET "
					 + SettingsFields.CHECK_TEST_DATES.toString() + "='" + checkTestDatesState + "' "
					+ " WHERE ID=1;")) {
				
		    	preparedStatement.executeUpdate();
		    }

		} catch (SQLException e) {
			
			this.errorWriter.showSQLExceptionMessage(e);
		}
    }

}
