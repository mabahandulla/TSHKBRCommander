package daoPattern;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import enumsStore.AdminFields;
import utilities.CreateErrorLog;

/**
 * This is a class for getting data from a table "admin_login".
 * <p>
 * @author Aleksander Demichev
 * @version 1.0.0.0
 */

public final class LoginDAO implements Dao <LoginStore> {

	private CreateErrorLog errorWriter;
    private DataSource dataSource;

    
    /* Get DataSource and error writer class. */
    public LoginDAO (DataSource dataSource, CreateErrorLog errorWriter) {
        this.dataSource = dataSource;
    	this.errorWriter = errorWriter;
    }

    
    
    
    
    /* Get all values from table row by selected ID */
	public LoginStore getData(int id) {

        LoginStore loginStore = null;
		
		try (Connection connection = this.dataSource.getConnection()) {
			try (PreparedStatement preparedStatement = connection.prepareStatement(
					"SELECT * FROM "
					+ AdminFields.TABLE_NAME
					+ " WHERE ID=" + id)) {
				try (ResultSet resultSet = preparedStatement.executeQuery()) {

					loginStore = new LoginStore();

					while (resultSet.next()) {
						loginStore.setLogin(resultSet.getString(AdminFields.LOGIN.toString()));
						loginStore.setPassword(resultSet.getString(AdminFields.PASSWORD.toString()));
					}
				}
			}
		} catch (SQLException e) {
			this.errorWriter.showSQLExceptionMessage(e);
		}
		
		return loginStore;
    }
	

	/* Update Date value in Database */
	public void updateData(LoginStore loginCredentials) {
		try (Connection connection = this.dataSource.getConnection()) {
			try (PreparedStatement preparedStatement = connection.prepareStatement(
					"UPDATE "
				    + AdminFields.TABLE_NAME
					+ " SET "
					+ AdminFields.PASSWORD.toString()
					+ "=\"" + loginCredentials.getPassword() + "\" WHERE ID=1;")) {
				
				preparedStatement.executeUpdate();
			}
		} catch (SQLException e) {
			this.errorWriter.showSQLExceptionMessage(e);
		}
	}
	
	
}
