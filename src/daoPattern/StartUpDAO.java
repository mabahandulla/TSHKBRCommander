package daoPattern;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

import enumsStore.CredentialsFields;
import enumsStore.InformationFields;
import enumsStore.SettingsFields;
import utilities.CreateErrorLog;
import enumsStore.AdminFields;

/**
 * This is a class for creating a new database filled with default values
 * in case the database file is not found in the directory from which the application is launched.
 * <p>
 * @author Aleksander Demichev
 * @version 1.0.0.0
 */

public final class StartUpDAO {

	private CreateErrorLog errorWriter;
    private DataSource dataSource;

    
    /*
     * Get DataSource and error writer class.
     */
    public StartUpDAO (DataSource dataSource, CreateErrorLog errorWriter) {
        this.dataSource = dataSource;
    	this.errorWriter = errorWriter;
    }

   
    
    /*
     * For INSERT, UPDATE or DELETE use the executeUpdate() method
     * For SELECT use the executeQuery() method which returns the ResultSet.
     */
    
    public void createDefaultDatabase() {
    	
    	
    	String [] createTablesQueryStore = {
    	
    	"CREATE TABLE IF NOT EXISTS admin_login ( "
				+ "ID INTEGER PRIMARY KEY NOT NULL UNIQUE ON CONFLICT ROLLBACK,"
    			+ "login VARCHAR (20) NOT NULL,"
				+ "password VARCHAR (20))",
    	
    	"CREATE TABLE IF NOT EXISTS credentials_store ("
				+ "ID INTEGER NOT NULL PRIMARY KEY UNIQUE ON CONFLICT ROLLBACK,"
				+ "login_kanal TEXT (20) NOT NULL,"
				+ "login_prikl TEXT (20) NOT NULL,"
				+ "pwd_kanal VARCHAR (20),"
				+ "pwd_prikl VARCHAR (20),"
				+ "dateset_kanal INTEGER,"
				+ "dateset_prikl INTEGER,"
				+ "pwd_kanal_old VARCHAR (20),"
				+ "pwd_prikl_old VARCHAR (20),"    	
				+ "psbr_work_vpn_file BOOLEAN (5),"
				+ "psbr_test_vpn_file BOOLEAN (5),"
				+ "spfs_work_vpn_file BOOLEAN (5),"
				+ "spfs_test_vpn_file BOOLEAN (5),"
				+ "main_vpn_ip_prom BOOLEAN (5),"
				+ "rezerv_vpn_ip_prom BOOLEAN (5),"
				+ "main_vpn_ip_test BOOLEAN (5),"
				+ "rezerv_vpn_ip_test BOOLEAN (5))",
				
    	"CREATE TABLE IF NOT EXISTS tsh_kbr_info ("
				+ "ID INTEGER NOT NULL PRIMARY KEY UNIQUE ON CONFLICT ROLLBACK,"
    			+ "url_send_wrk TEXT (50),"
				+ "url_recive_wrk TEXT (50),"
    			+ "url_send_tst TEXT (50),"
				+ "url_recive_tst TEXT (50),"
				+ "main_ip_wrk TEXT (20),"
				+ "reserved_ip_wrk TEXT (20),"
				+ "main_ip_tst TEXT (20),"
				+ "reserved_ip_tst TEXT (20),"
				+ "url_lk_wrk TEXT (50),"
				+ "url_lk_tst TEXT (50))",
    	
    	"CREATE TABLE settings ("
				+ "ID INTEGER PRIMARY KEY NOT NULL UNIQUE ON CONFLICT ROLLBACK,"
				+ "pwd_length INTEGER (20) NOT NULL,"
				+ "psbr_visible BOOLEAN (5),"
				+ "spfs_visible BOOLEAN (5),"
				+ "interface_selected BOOLEAN (5),"
				+ "check_test_dates BOOLEAN (5))"
    	
    	};
		
    	
		String[][] defaultCredentialsValues = {
				
				{ "00000000000000", "000000000000" }, 
				{ "00000000000000", "000000000000" }, 
				{ "00000000000000", "000000000000" }, 
				{ "00000000000000", "000000000000" }, 
		};
    	
    	
		try (Connection connection = this.dataSource.getConnection()) {

			
			/* Creating empty tables in database */
			for (String query : createTablesQueryStore) {

				try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
					preparedStatement.executeUpdate();
				}
			}
			
			
			/* INSERT default values to the admin_login table */
			try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO '" + AdminFields.TABLE_NAME
					+ "' (" + this.getTableColumnsNames(AdminFields.names()) + ") VALUES ("
					+ " 'admin',"
					+ " 'admin'); ");
			) {
				preparedStatement.executeUpdate();
			}	

			/* INSERT default values to the credentials_store table */
			for (int i = 0; i <= defaultCredentialsValues.length - 1; i++) {
				
				try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO '" + CredentialsFields.TABLE_NAME
						+ "' (" + this.getTableColumnsNames(CredentialsFields.names()) + ") VALUES ("
						+ " '" + defaultCredentialsValues[i][0] + "',"
						+ " '" + defaultCredentialsValues[i][1] + "',"
						+ " '11zz22xx33cc',"
						+ " '11zz22xx33cc',"
						+ " strftime('%s', 'now'),"
						+ " strftime('%s', 'now'),"
						+ " '11zz22xx33cc',"
						+ " '11zz22xx33cc',"
						+ " 'false',"
						+ " 'false',"
						+ " 'false',"
						+ " 'false',"
						+ " 'true',"
						+ " 'false',"
						+ " 'true',"
						+ " 'false'); ");
				) {
					preparedStatement.executeUpdate();
				}
			}
			
			/* INSERT default values to the tsh_kbr_info table */
			try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO '" + InformationFields.TABLE_NAME
					+ "' (" + this.getTableColumnsNames(InformationFields.names()) + ") VALUES ("
					+ " 'http://172.16.18.211:7777/in',"
					+ " 'http://172.16.18.211:7777/get',"
					+ " 'http://172.16.19.211:7777/in',"
					+ " 'http://172.16.19.211:7777/get',"
					+ " '172.16.20.34',"
					+ " '172.16.20.66',"
					+ " '172.16.20.42',"
					+ " '172.16.20.74',"
					+ " 'https://172.16.18.211:9697',"
					+ " 'https://172.16.19.211:9697'); ");
			) {
				preparedStatement.executeUpdate();
			}
			
			
			/* INSERT default values to the settings table */
			try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO '" + SettingsFields.TABLE_NAME
					+ "' (" + this.getTableColumnsNames(SettingsFields.names()) + ") VALUES ("
					+ " '12',"
					+ " 'true',"
					+ " 'true',"
					+ " 'false',"
					+ " 'false'); ");
			) {
				preparedStatement.executeUpdate();
			}			
			
			
		} catch (SQLException e) {
			this.errorWriter.showSQLExceptionMessage(e);
		}
    }
    
    
    
    /* Get formatted string with columns of the selected table like a: "column1, column2, column3" */
    private String getTableColumnsNames(String[] tableColumns) {
    	
    	if(tableColumns == null || tableColumns.length == 0) return "";
    	
    	StringBuilder columnsNames = new StringBuilder();
    	
    	for (String tableField : tableColumns) {
			columnsNames.append(" '" + tableField + "', ");
		}

		/* Delete last comma in StringBuilder for building proper query string */
		columnsNames.deleteCharAt(columnsNames.length() - 2);
		
		return columnsNames.toString();
    	
    }
	

	
}
