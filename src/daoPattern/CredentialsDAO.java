package daoPattern;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;

import enumsStore.CredentialsFields;
import utilities.CreateErrorLog;

/**
 * This is a class for getting data from a table "credentials_store".
 * <p>
 * @author Aleksander Demichev
 * @version 1.0.0.0
 */

public final class CredentialsDAO implements Dao <CredentialsStore> {

	private CreateErrorLog errorWriter;
    private DataSource dataSource;

    
    /* Get DataSource and error writer class. */
    public CredentialsDAO(DataSource dataSource, CreateErrorLog errorWriter) {
        this.dataSource = dataSource;
    	this.errorWriter = errorWriter;
    }

   
    
    
    /* Get all values from table row by selected ID */
    public CredentialsStore getData(int id) {
    	
    	CredentialsStore credentialsStore = null;		
		
		try (Connection connection = this.dataSource.getConnection()) {			
		    try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM " + CredentialsFields.TABLE_NAME + " WHERE ID=" + id)) {
		      try (ResultSet resultSet = preparedStatement.executeQuery()) {
		    	  
		    	  credentialsStore = new CredentialsStore();
	               
	               while (resultSet.next()) {
	            	   credentialsStore.setId(resultSet.getInt("ID"));
	            	   credentialsStore.setLoginKanal(resultSet.getString(CredentialsFields.CHANNEL_LOGIN.toString()));
	            	   credentialsStore.setLoginPrikl(resultSet.getString(CredentialsFields.APPLICATION_LOGIN.toString()));
	            	   credentialsStore.setPasswordKanal(resultSet.getString(CredentialsFields.CHANNEL_PASSWORD.toString()));
	            	   credentialsStore.setPasswordPrikl(resultSet.getString(CredentialsFields.APPLICATION_PASSWORD.toString()));
	            	   credentialsStore.setDateSetKanal(resultSet.getLong(CredentialsFields.CHANNEL_PASSWORD_CHANGE_DATE.toString()));
	            	   credentialsStore.setDateSetPrikl(resultSet.getLong(CredentialsFields.APPLICATION_PASSWORD_CHANGE_DATE.toString()));
	            	   credentialsStore.setPasswordKanalOld(resultSet.getString(CredentialsFields.OLD_CHANNEL_PASSWORD.toString()));
	            	   credentialsStore.setPasswordPriklOld(resultSet.getString(CredentialsFields.OLD_APPLICATION_PASSWORD.toString()));
	            	   credentialsStore.setPsbrWorkVpnFileState(Boolean.parseBoolean(resultSet.getString(CredentialsFields.PSBR_WORK_VPN_FILE.toString())));
	            	   credentialsStore.setPsbrTestVpnFileState(Boolean.parseBoolean(resultSet.getString(CredentialsFields.PSBR_TEST_VPN_FILE.toString())));
	            	   credentialsStore.setSpfsWorkVpnFileState(Boolean.parseBoolean(resultSet.getString(CredentialsFields.SPFS_WORK_VPN_FILE.toString())));
	            	   credentialsStore.setSpfsTestVpnFileState(Boolean.parseBoolean(resultSet.getString(CredentialsFields.SPFS_TEST_VPN_FILE.toString())));
	            	   credentialsStore.setMainVpnIpPromState(Boolean.parseBoolean(resultSet.getString(CredentialsFields.MAIN_VPN_IP_PROM.toString())));
	            	   credentialsStore.setRezervVpnIpPromState(Boolean.parseBoolean(resultSet.getString(CredentialsFields.REZERV_VPN_IP_PROM.toString())));
	            	   credentialsStore.setMainVpnIpTestState(Boolean.parseBoolean(resultSet.getString(CredentialsFields.MAIN_VPN_IP_TEST.toString())));
	            	   credentialsStore.setRezervVpnIpTestState(Boolean.parseBoolean(resultSet.getString(CredentialsFields.REZERV_VPN_IP_TEST.toString())));
					}
				}
			}
		    
		} catch (SQLException e) {
			
			this.errorWriter.showSQLExceptionMessage(e);
		}
		
		return credentialsStore;
    }
    
    
    /* Get ONLY dates values when the password was changed from table row by selected ID */
    public CredentialsStore getPasswordChangedDates (int id) {
    	
    	CredentialsStore credentialsStore = null;		
		
		try (Connection connection = this.dataSource.getConnection()) {			
		    try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM " + CredentialsFields.TABLE_NAME + " WHERE ID=" + id)) {
		      try (ResultSet resultSet = preparedStatement.executeQuery()) {
		    	  
		    	  credentialsStore = new CredentialsStore();
	               
	               while (resultSet.next()) {
	            	   credentialsStore.setDateSetKanal(resultSet.getLong(CredentialsFields.CHANNEL_PASSWORD_CHANGE_DATE.toString()));
	            	   credentialsStore.setDateSetPrikl(resultSet.getLong(CredentialsFields.APPLICATION_PASSWORD_CHANGE_DATE.toString()));
					}
				}
			}
		    
		} catch (SQLException e) {
			
			this.errorWriter.showSQLExceptionMessage(e);
		}
		
		return credentialsStore;
    }
    
    
    
    
    /* Save all values from CredentialsStore Class to the credentials_store table by selected ID */
    public void updateData(CredentialsStore credentialsStore) {
    	
		try (Connection connection = this.dataSource.getConnection()) {

		    try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE "
					+ CredentialsFields.TABLE_NAME + " SET "
					+ CredentialsFields.CHANNEL_LOGIN.toString() + "='" + credentialsStore.getLoginKanal() + "', "
					+ CredentialsFields.APPLICATION_LOGIN.toString() + "='" + credentialsStore.getLoginPrikl() + "', "
					+ CredentialsFields.CHANNEL_PASSWORD.toString() + "='" + credentialsStore.getPasswordKanal() + "', "
					+ CredentialsFields.APPLICATION_PASSWORD.toString() + "='" + credentialsStore.getPasswordPrikl() + "', "
		    		+ CredentialsFields.CHANNEL_PASSWORD_CHANGE_DATE.toString() + "=" + credentialsStore.getDateSetKanal() + ", "
					+ CredentialsFields.APPLICATION_PASSWORD_CHANGE_DATE.toString() + "=" + credentialsStore.getDateSetPrikl() + ", "
		    		+ CredentialsFields.OLD_CHANNEL_PASSWORD.toString() + "='" + credentialsStore.getPasswordKanalOld() + "', "
				    + CredentialsFields.OLD_APPLICATION_PASSWORD.toString() + "='" + credentialsStore.getPasswordPriklOld() + "', "
					+ CredentialsFields.PSBR_WORK_VPN_FILE.toString() + "='" + credentialsStore.getPsbrWorkVpnFileState() + "', "
					+ CredentialsFields.PSBR_TEST_VPN_FILE.toString() + "='" + credentialsStore.getPsbrTestVpnFileState() + "', "
					+ CredentialsFields.SPFS_WORK_VPN_FILE.toString() + "='" + credentialsStore.getSpfsWorkVpnFileState() + "', "
					+ CredentialsFields.SPFS_TEST_VPN_FILE.toString() + "='" + credentialsStore.getSpfsTestVpnFileState() + "', "
					+ CredentialsFields.MAIN_VPN_IP_PROM.toString() + "='" + credentialsStore.getMainVpnIpPromState() + "', "
					+ CredentialsFields.REZERV_VPN_IP_PROM.toString() + "='" + credentialsStore.getRezervVpnIpPromState() + "', "
					+ CredentialsFields.MAIN_VPN_IP_TEST.toString() + "='" + credentialsStore.getMainVpnIpTestState() + "', "
					+ CredentialsFields.REZERV_VPN_IP_TEST.toString() + "='" + credentialsStore.getRezervVpnIpTestState() + "' "
					+ " WHERE ID='" + credentialsStore.getId() + "';")) {
				
		    	preparedStatement.executeUpdate();
		    }

		} catch (SQLException e) {
			
			this.errorWriter.showSQLExceptionMessage(e);
		}
    }
    
    
    
	/* Return Integer value of Unix time from database like 1622814615 or zero */
	public int getIntegerDateFromDatabase(String column, int id) {
		
		int intUnixTime = 0;
		
		try (Connection connection = this.dataSource.getConnection()) {
			try (PreparedStatement preparedStatement = connection.prepareStatement(
					"SELECT " + column + " FROM " + CredentialsFields.TABLE_NAME + " WHERE ID=" + id + ";")) {
				try (ResultSet resultSet = preparedStatement.executeQuery()) {
					intUnixTime = resultSet.getInt(1);
				}
			}
			
		} catch (SQLException e) {
			
			this.errorWriter.showSQLExceptionMessage(e);
		}
		
		return intUnixTime;
	}
    
	
	
    /* Update values from SettingsStore in the settings table */
    public void updateVPNCheckboxState(String column, boolean checkboxState, int id) {
    	
		try (Connection connection = this.dataSource.getConnection()) {
		    try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE "
					+ CredentialsFields.TABLE_NAME + " SET "
					+ column + "='" + checkboxState + "' "
					+ " WHERE ID=" + id + ";")) {
				
		    	preparedStatement.executeUpdate();
		    }

		} catch (SQLException e) {
			
			this.errorWriter.showSQLExceptionMessage(e);
		}
    }
	
    /* Update VPN Radio Buttons State in Database */
    public void updateVPNRadioButtonsState(CredentialsStore credentialsStore) {
    	
		try (Connection connection = this.dataSource.getConnection()) {

		    try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE "
					+ CredentialsFields.TABLE_NAME + " SET "
					+ CredentialsFields.MAIN_VPN_IP_PROM.toString() + "='" + credentialsStore.getMainVpnIpPromState() + "', "
					+ CredentialsFields.REZERV_VPN_IP_PROM.toString() + "='" + credentialsStore.getRezervVpnIpPromState() + "', "
					+ CredentialsFields.MAIN_VPN_IP_TEST.toString() + "='" + credentialsStore.getMainVpnIpTestState() + "', "
					+ CredentialsFields.REZERV_VPN_IP_TEST.toString() + "='" + credentialsStore.getRezervVpnIpTestState() + "' "
					+ " WHERE ID='" + credentialsStore.getId() + "';")) {
				
		    	preparedStatement.executeUpdate();
		    }

		} catch (SQLException e) {
			
			this.errorWriter.showSQLExceptionMessage(e);
		}
    }
	
	
}
