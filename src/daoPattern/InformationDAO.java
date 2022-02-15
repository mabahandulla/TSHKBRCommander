package daoPattern;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;

import enumsStore.InformationFields;
import utilities.CreateErrorLog;

/**
 * This is a class for getting data from a table "tsh_kbr_info".
 * <p>
 * @author Aleksander Demichev
 * @version 1.0.0.0
 */

public final class InformationDAO implements Dao <InformationStore> {

	private CreateErrorLog errorWriter;
    private DataSource dataSource;

    
    /* Get DataSource and error writer class. */
    public InformationDAO(DataSource dataSource, CreateErrorLog errorWriter) {
        this.dataSource = dataSource;
    	this.errorWriter = errorWriter;
    }
	
    
    /* Find by ID method implementation */
    public InformationStore getData(int id) {
    	
   	
    	InformationStore informationStore = null;
    	
		
		try (Connection connection = this.dataSource.getConnection()) {
		  try (PreparedStatement preparedStatement = connection.prepareStatement(
				  "SELECT * FROM "
				  + InformationFields.TABLE_NAME
				  + " WHERE ID=" + id)) {
		    try (ResultSet resultSet = preparedStatement.executeQuery()) {

		    	informationStore = new InformationStore();
   
	               while (resultSet.next()) {
	            	   informationStore.setUrlSendProm(resultSet.getString(InformationFields.URL_SEND_WORK.toString()));
	            	   informationStore.setUrlReciveProm(resultSet.getString(InformationFields.URL_RECIVE_WORK.toString()));
	            	   informationStore.setUrlSendTest(resultSet.getString(InformationFields.URL_SEND_TEST.toString()));
	            	   informationStore.setUrlReciveTest(resultSet.getString(InformationFields.URL_RECIVE_TEST.toString()));
	            	   informationStore.setMainIPProm(resultSet.getString(InformationFields.MAIN_IP_WORK.toString()));
	            	   informationStore.setRezervIPProm(resultSet.getString(InformationFields.BACKUP_IP_WORK.toString()));
	            	   informationStore.setMainIPTest(resultSet.getString(InformationFields.MAIN_IP_TEST.toString()));
	            	   informationStore.setRezervIPTest(resultSet.getString(InformationFields.BACKUP_IP_TEST.toString()));
	            	   informationStore.setUrlLKProm(resultSet.getString(InformationFields.URL_LK_WORK.toString()));
	            	   informationStore.setUrlLKTest(resultSet.getString(InformationFields.URL_LK_TEST.toString()));
	       		}	               
		      }
		    }
		} catch (SQLException e) {
			this.errorWriter.showSQLExceptionMessage(e);
		}
		
		return informationStore;
    } 
    
    
    
    
    
    /*
     * Save all values from InformationStore Class to the "tsh_kbr_info" table
     */
    public void updateData(InformationStore informationStore) {
    	
		try (Connection connection = this.dataSource.getConnection()) {

		    try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE "
					+ InformationFields.TABLE_NAME + " SET "
					+ InformationFields.URL_SEND_WORK.toString() + "='" + informationStore.getUrlSendProm() + "', "
					+ InformationFields.URL_RECIVE_WORK.toString() + "='" + informationStore.getUrlReciveProm() + "', "
					+ InformationFields.URL_SEND_TEST.toString() + "='" + informationStore.getUrlSendTest() + "', "
					+ InformationFields.URL_RECIVE_TEST.toString() + "='" + informationStore.getUrlReciveTest() + "', "
		    		+ InformationFields.MAIN_IP_WORK.toString() + "='" + informationStore.getMainIPProm() + "', "
					+ InformationFields.BACKUP_IP_WORK.toString() + "='" + informationStore.getRezervIPProm() + "', "
		    		+ InformationFields.MAIN_IP_TEST.toString() + "='" + informationStore.getMainIPTest() + "', "
				    + InformationFields.BACKUP_IP_TEST.toString() + "='" + informationStore.getRezervIPTest() + "', "
				    + InformationFields.URL_LK_WORK.toString() + "='" + informationStore.getUrlLKProm() + "', "
					+ InformationFields.URL_LK_TEST.toString() + "='" + informationStore.getUrlLKTest() + "' "
					+ " WHERE ID=1;")) {
				
		    	preparedStatement.executeUpdate();
		    }

		} catch (SQLException e) {		
			this.errorWriter.showSQLExceptionMessage(e);
		}
    }
  
    
    
}
