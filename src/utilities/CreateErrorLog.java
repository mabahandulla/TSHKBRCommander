package utilities;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.sql.SQLException;

import org.eclipse.swt.widgets.Display;

import enumsStore.MessageTypes;


/**
 * This is the class for writing exceptions to the log file.
 * <p>
 * @author Aleksander Demichev
 * @version 1.0.0.0
 */

public final class CreateErrorLog {

	//private ApplicationEnvironments appEnv;
	private Display display;
	private MessageDialog messages;
	private DateTimeClass dateTimeBuilder = new DateTimeClass();
    final private String ERROR_FILE_NAME;
    
    
    public CreateErrorLog(Display display, MessageDialog messages, String rootDirectory){
    	this.display = display;
		this.ERROR_FILE_NAME = rootDirectory + File.separator + "error.log";
        this.messages = messages;
    }
	

	public void printErrorToFile(Exception e) {
	    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	    PrintStream printStream = new PrintStream(byteArrayOutputStream);
	    e.printStackTrace(printStream);
	    printStream.close();
		String message = this.dateTimeBuilder.getFormattedDate() + " - " + this.dateTimeBuilder.getFormattedTime() + "\t---- Error message: ----\n" + byteArrayOutputStream.toString() + "\n\n";
		new TshKbrFileWriter(display, this.ERROR_FILE_NAME, message, "UTF-8", true, false);
	}
	
	
	
	
	/*
	 * Get ERROR_FILE_NAME
	 */
	public String getErrorFileName() {
		return this.ERROR_FILE_NAME;
	}
	
	
    /*
     * If error occurred, write SQLError to error log file and show to user error message
     */
	public void showSQLExceptionMessage(SQLException e) {
		
		this.printErrorToFile(e);
		
		this.messages.showMessage(
						"Ошибка при работе с базой данных.\nСмотри детали ошибки в лог файле:\n\n"
						+ this.getErrorFileName()
						+ "\n\nТекст ошибки:\n\n"
						+ e.getMessage(), 
						MessageTypes.ERROR);
	}
	
	
}
