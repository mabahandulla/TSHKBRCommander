package startWindow;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import daoPattern.Application;
import daoPattern.CredentialsDAO;
import daoPattern.InformationDAO;
import daoPattern.InformationStore;
import daoPattern.LoginDAO;
import daoPattern.SettingsDAO;
import daoPattern.SettingsStore;
import daoPattern.StartUpDAO;
import enumsStore.ApplicationMode;
import enumsStore.DatabaseEnvironment;
import enumsStore.MessageTypes;
import templateWindow.DateDifferenceCalculator;
import utilities.CreateErrorLog;
import utilities.ImageСontainer;
import utilities.MessageBuilder;
import utilities.MessageDialog;
import utilities.ProcessExecutor;
import utilities.ValuesValidator;

/**
 * This is a class for storing all the resources necessary for the application to work.
 * <p>
 * @author Aleksander Demichev
 * @version 1.0.0.0
 */

public final class ApplicationEnvironments {

	private Display display;
	private String rootDirectory;

	
	private CreateErrorLog errorWriter;
	private MessageDialog messages;

	private ImageСontainer imageСontainer;
	
	/* Images for setting application icons */
	private Image windowImage, cbLogo, buttonKey;

	private StartUpDAO startUpDAO;
	
	private MessageBuilder messageBuilder;
	
	private LoginDAO loginDAO;
	private CredentialsDAO credentialsDAO;
	private InformationDAO informationDAO;
	private SettingsDAO settingsDAO;

	private InformationStore informationStore;
	private SettingsStore settingsStore;
	private ValuesValidator validator;
	private ProcessExecutor processLuncher;
	private DateDifferenceCalculator dateDifferenceCalculator;
	private ApplicationMode appMode;
	

	public ApplicationEnvironments(Display display, ApplicationMode appMode) {
		this.display = display;		
		this.appMode = appMode;
		this.rootDirectory = this.getRootDirectory();
		this.messages = new MessageDialog(display);
		this.errorWriter = new CreateErrorLog(display, this.messages, this.rootDirectory);
		this.imageСontainer = new ImageСontainer(display);
		this.windowImage = this.imageСontainer.getWindowIcon();
		this.cbLogo = this.imageСontainer.getCbLogo();
		this.buttonKey = this.imageСontainer.getButtonKey();
		this.messageBuilder = new MessageBuilder();
		this.validator = new ValuesValidator(display);
		this.dateDifferenceCalculator = new DateDifferenceCalculator(display, this.messages, this.messageBuilder, this.validator);
		
		
		this.startUpDAO = new StartUpDAO(Application.INSTANCE.dataSource(this.rootDirectory), this.errorWriter);
		this.checkDatabase(appMode);
		
		this.credentialsDAO = new CredentialsDAO(Application.INSTANCE.dataSource(this.rootDirectory), this.errorWriter);
		this.settingsDAO = new SettingsDAO(Application.INSTANCE.dataSource(this.rootDirectory), this.errorWriter);
		this.settingsStore = this.settingsDAO.getData(1);

		

		if (appMode == ApplicationMode.COMMANDER) {			
			this.loginDAO = new LoginDAO(Application.INSTANCE.dataSource(this.rootDirectory), this.errorWriter);
			this.informationDAO = new InformationDAO(Application.INSTANCE.dataSource(this.rootDirectory), this.errorWriter);
			this.processLuncher = new ProcessExecutor(this.messages, this.errorWriter);
		}
	}
	
	

	
	
	
	/* Get path where from the JAR file running */
	private String getRootDirectory() {
		
		String applicationPath = null;
		
		try {
			applicationPath = new File(".").getCanonicalPath();
		} catch (IOException e) {
			this.errorWriter.printErrorToFile(e);
			
			if (System.getProperty("os.name").toLowerCase().startsWith("windows") && System.getenv("APPDATA") != null)
				applicationPath = System.getenv("APPDATA") + File.separator + "TSHKBRCommander";
			
			this.messages.showMessage("Не удалось получить рабочий каталог программы!\nСмотри детали ошибки в лог файле:\n\n"
							+ this.errorWriter.getErrorFileName()
							+ "\n\nТекст ошибки:\n\n"
							+ e.getMessage()
							+ "\n\nНазначаем каталог программы по умолчанию:\n\n"
							+ applicationPath, 
							MessageTypes.ERROR);
		}
		
		return applicationPath;
		
	}
	
	
	
	/* Check the database directory and the database filename and create it if not exists */
	public void checkDatabase(ApplicationMode appMode)  {
		
    	File databaseFile = new File(getApplicationRootDirectory()
    			+ File.separator
    			+ DatabaseEnvironment.DATABASE_DIRECTORY.getValue()
    			+ File.separator
    			+ DatabaseEnvironment.DATABASE_FILENAME.getValue());

		switch (appMode) {
			case COMMANDER:
				
				if (!databaseFile.exists() && !databaseFile.isDirectory()) {
					
					if(databaseFile.getParentFile().mkdirs()) {
						createDefaultDatabase();
					} else {
						errorWriter.printErrorToFile(new SecurityException("Error creating database directory!"));
						messages.showMessage(
								"Не удается создать каталог базы данных!\nСмотри детали ошибки в лог файле:\n\n"
								+ errorWriter.getErrorFileName()
								+ "\n\nТекст ошибки:\n\nError creating database directory!",
								MessageTypes.ERROR);
						Runtime.getRuntime().halt(1);
					}
				}
				
				break;
				
			case NOTIFIER:
				
				if (!databaseFile.exists() && !databaseFile.isDirectory()) {					
					
					errorWriter.printErrorToFile(new SQLException("Error starting TSH KBR Notifier! ERROR: Database not found."));
					messages.showMessage(
							"Ошибка запуска Notifier: НЕ НАЙДЕНА БАЗА ДАННЫХ!\n\nСмотри детали ошибки в лог файле:\n\n"
							+ errorWriter.getErrorFileName()
							+ "\n\nТекст ошибки:\n\nERROR: Database not found.\n\nДля создание БД запустите вначале ТШ КБР Commander.",
							MessageTypes.ERROR);
							
					
					Runtime.getRuntime().halt(2);
				}
				
				break;
		}
	}
	
	
	/* Create default SQLight database with default values */
	public void createDefaultDatabase() {
		this.startUpDAO.createDefaultDatabase();
		this.messages.showMessage("Файл базы данных не обнаружен.\n\nСоздаем пустую БД и заполняем ее значениями по умолчанию:\n\n\tЛогин:   admin\n\tПароль: admin",
				MessageTypes.WARNING);
	}
	
	
	
	public void shutdownApplication() {
		System.gc();		
		for(Image icon : new Image[] {this.windowImage, this.cbLogo, this.buttonKey}) {
			if(icon != null) icon.dispose();
		}
		if (this.display != null) this.display.dispose();
		System.exit(0);
	}
	
	
	/* Get Display */
	public Display getDisplay() {
		return this.display;
	}
	
	public ApplicationMode getAppMode() {
		return this.appMode;
	}
	
	/* Get window icon Image */
	public Image getWindowIcon() {
		return this.windowImage;
	}
	
	/* Get CB Logo Image*/
	public Image getCbLogo() {
		return this.cbLogo;
	}
	
	/* Get button Key Image */
	public Image getButtonKey() {
		return this.buttonKey;
	}
	
	/* Get LoginDAO */
	public LoginDAO getLoginDAO() {
		return this.loginDAO;
	}
	
	/* Get CredentialsDAO */
	public CredentialsDAO getCredentialsDAO() {
		return this.credentialsDAO;
	}
	
	/* Get MessageDialog */
	public MessageDialog getMessages() {
		return this.messages;
	}
	
	/* Get SettingsDAO */
	public SettingsDAO getSettingsDAO() {
		return this.settingsDAO;
	}	
	
	/* Get SettingsStore */
	public SettingsStore getSettingsStore() {
		this.settingsStore = this.settingsDAO.getData(1);
		return (SettingsStore) this.settingsStore.clone();
	}
	
	/* Get InformationDAO */
	public InformationDAO getInformationDAO() {
		return this.informationDAO;
	}
	
	/* Get InformationStore */
	public InformationStore getInformationStore() {
		this.informationStore = this.informationDAO.getData(1);
		return (InformationStore) this.informationStore.clone();
	}

	/* Get ValuesValidator */
	public ValuesValidator getValuesValidator() {
		return this.validator;
	}

	/* Get ProcessExecutor */
	public ProcessExecutor getProcessExecutor() {
		return this.processLuncher;
	}
	
	/* Get DateDifferenceCalculator */
	public DateDifferenceCalculator getDateDifferenceCalculator() {
		return this.dateDifferenceCalculator;
	}
	
	/* Get MessageBuilder */
	public MessageBuilder getMessageBuilder() {
		return this.messageBuilder;
	}
	
	/* get Application Root Directory */
	public String getApplicationRootDirectory() {
		return this.rootDirectory;
	}
	
	/* Get CreateErrorLog */
	public CreateErrorLog getErrorWriter() {
		return this.errorWriter;
	}
	
	
	
}
