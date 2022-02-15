package utilities;

import java.io.IOException;

import enumsStore.MessageTypes;

/**
 * This is the class for opening various objects in Windows Explorer.
 * <p>
 * @author Aleksander Demichev
 * @version 1.0.0.0
 */

public final class ProcessExecutor {
	
	/* Instance for showing messages to user */
	private MessageDialog messages;
	
	/* Instance for writing errors to error.log */
	private CreateErrorLog errorWriter;

	
	public ProcessExecutor(MessageDialog messages, CreateErrorLog errorWriter) {
		this.messages = messages;
		this.errorWriter = errorWriter;
	}
	
	

	
	
	
	/* Run process with selected string */
	public final void runProcess(String objectToRun) {

		if (System.getProperty("os.name").toLowerCase().startsWith("windows")) {
			
			try {
				Process process = Runtime.getRuntime().exec("explorer.exe " + objectToRun);
				process.waitFor();
			} catch (IOException | InterruptedException e) {
				this.errorWriter.printErrorToFile(e);
				this.messages.showMessage(
								"Ошибка запуска процесса!\nСмотри детали ошибки в лог файле:\n"
								+ this.errorWriter.getErrorFileName()
								+ "\nТекст ошибки:\n"
								+ e.getMessage(), 
								MessageTypes.ERROR);
				return;
			}
			
		} else {
			this.messages.showMessage(
					"Операционная система не поддерживается!",
					MessageTypes.ERROR);
			return;
		}
	}
	
	
}
