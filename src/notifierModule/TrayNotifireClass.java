package notifierModule;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tray;
import org.eclipse.swt.widgets.TrayItem;

import daoPattern.CredentialsDAO;
import daoPattern.CredentialsStore;
import daoPattern.SettingsDAO;
import daoPattern.SettingsStore;
import enumsStore.ApplicationMode;
import enumsStore.CredentialsSwitches;
import enumsStore.CredentialsTypes;
import enumsStore.MessageTypes;
import infoWindow.AboutWindow;
import startWindow.ApplicationEnvironments;
import templateWindow.DateDifferenceCalculator;
import utilities.CreateErrorLog;
import utilities.MessageBuilder;
import utilities.MessageDialog;
import utilities.ShowErrorDialog;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

/**
 * This application is used to notify the user if it is time to change the TSH KBR password.
 * <p>
 * This class runs the module that periodically check the number of days
 * that have passed since the password was changed
 * and form the text of the warning message displayed to the user.
 * The main application window, where you can change the credentials, does not start.
 * <p>
 * This module is started by the command line switch "-startn".
 * <p>
 * @author Aleksander Demichev
 * @version 1.0.0.0
 */

public final class TrayNotifireClass {

	
	
	private Display display;
	private Shell shell;
	private ApplicationEnvironments appEnv;
	
	private Menu menu;
	
	/* Get system tray */
	private Tray tray;
	
	/* Get new tray item */
	private TrayItem trayItem;

	/* Image for setting a system tray icon */
	private Image windowImage;
	  
	private CreateErrorLog errorWriter;
	private MessageDialog messages;
	
	private CredentialsDAO credentialsDAO;
	private SettingsDAO settingsDAO;
	
	private CredentialsStore psbrTest, psbrProm, spfsTest, spfsProm;
	private SettingsStore settingsStore;
	
	private MessageBuilder messageBuilder;
	private DateDifferenceCalculator dateDifferenceCalculator;
	
	/* Create Scheduled Executor Service for run notifier task */
	private ScheduledExecutorService scheduledExecutorService;
	
	
    public TrayNotifireClass(Display display) {
    	
		this.display = display;
		this.appEnv = new ApplicationEnvironments(display, ApplicationMode.NOTIFIER);
		this.tray = display.getSystemTray();	
		this.messages = appEnv.getMessages();
		this.errorWriter = appEnv.getErrorWriter();
		this.messageBuilder = appEnv.getMessageBuilder();
		this.windowImage = appEnv.getWindowIcon();
		this.settingsDAO = appEnv.getSettingsDAO();
		this.settingsStore = appEnv.getSettingsStore();
		this.credentialsDAO = appEnv.getCredentialsDAO();
		this.dateDifferenceCalculator = appEnv.getDateDifferenceCalculator();
    }
  
    


    
	
    /* The Notifier task to be performed */
	private Runnable notifierTask = () -> {

		display.syncExec(new Runnable() {

			public void run() {

				messageBuilder.clearTrayNotifierToolTip();
				messageBuilder.setNotifierToolTip("ТШ КБР Notifier\n");
				messageBuilder.setNotifierToolTip("Статус: " + getNotifierStateText() + "\n\n");

				if (settingsStore.getPsbrState()) {

					psbrProm = credentialsDAO.getPasswordChangedDates(2);
					messageBuilder.setNotifierToolTip("PSBRW-> ");
					
					dateDifferenceCalculator.checkDaysCountOfPasswordChanged (
							psbrProm.getDateSetKanal(),
							null,
							CredentialsTypes.CHANNEL,
							CredentialsSwitches.PSBR_PROM,
							true);
					
				    dateDifferenceCalculator.checkDaysCountOfPasswordChanged (
				    		psbrProm.getDateSetPrikl(),
				    		null,
				    		CredentialsTypes.APPLICATION,
				    		CredentialsSwitches.PSBR_PROM,
				    		true);

					if (settingsStore.getCheckTestDatesState()) {

						psbrTest = credentialsDAO.getPasswordChangedDates(1);
						messageBuilder.setNotifierToolTip("PSBRT-> ");
						
						dateDifferenceCalculator.checkDaysCountOfPasswordChanged (
								psbrTest.getDateSetKanal(),
								null,
								CredentialsTypes.CHANNEL,
								CredentialsSwitches.PSBR_TEST,
								true);
						
					    dateDifferenceCalculator.checkDaysCountOfPasswordChanged (
					    		psbrTest.getDateSetPrikl(),
					    		null,
					    		CredentialsTypes.APPLICATION,
					    		CredentialsSwitches.PSBR_TEST,
					    		true);
					}
				}

				if (settingsStore.getSpfsState()) {

					spfsProm = credentialsDAO.getPasswordChangedDates(4);
					messageBuilder.setNotifierToolTip("SPFSW-> ");

					dateDifferenceCalculator.checkDaysCountOfPasswordChanged (
							spfsProm.getDateSetKanal(),
							null,
							CredentialsTypes.CHANNEL,
							CredentialsSwitches.SPFS_PROM,
							true);
					
				    dateDifferenceCalculator.checkDaysCountOfPasswordChanged (
				    		spfsProm.getDateSetPrikl(),
				    		null,
				    		CredentialsTypes.APPLICATION,
				    		CredentialsSwitches.SPFS_PROM,
				    		true);

					if (settingsStore.getCheckTestDatesState()) {

						spfsTest = credentialsDAO.getPasswordChangedDates(3);
						messageBuilder.setNotifierToolTip("SPFST-> ");
						
						dateDifferenceCalculator.checkDaysCountOfPasswordChanged (
								spfsTest.getDateSetKanal(),
								null,
								CredentialsTypes.CHANNEL,
								CredentialsSwitches.SPFS_TEST,
								true);
						
					    dateDifferenceCalculator.checkDaysCountOfPasswordChanged (
					    		spfsTest.getDateSetPrikl(),
					    		null,
					    		CredentialsTypes.APPLICATION,
					    		CredentialsSwitches.SPFS_TEST,
					    		true);
					}
				}

				trayItem.setToolTipText(messageBuilder.getTrayNotifierToolTip().toString());

				/* If List with transmission systems in class DateDifferenceCalculator is not empty, show warning message to user */
				if (!dateDifferenceCalculator.getTransmitionSystemStore().isEmpty()) {

					shutdownScheduledExecutorService();

					messageBuilder.setStringToNotifierMessage(messageBuilder.getStringSeparator(78)
							+ "Пароль нужно менять не реже 1 раза в 45 дней.\n\nОбратитесь в Отдел ИТ для смены паролей ТШ КБР!");

					messages.showMessage(messageBuilder.getNotifierMessage().toString(), messageBuilder.getNotifierMessageType());

					messageBuilder.clearNotifierMessage();
					dateDifferenceCalculator.clearTransmitionSystemStore();
					
					/* Starts a periodic action */
					startScheduledExecutorService(this, false);
				}
			}

		});
	};
    
	
	

	
	
	
	
	public void open() {
		
		createContents();
		
	     while (!display.isDisposed()) {

			try {

				if (!display.readAndDispatch()) {
					display.sleep();
				}
				
			} catch (Exception e) {
				errorWriter.printErrorToFile(e);
				messages.showMessage(
								"Возникла ошибка во время работы программы.\nСмотри детали ошибки в лог файле:\n\n"
								+ errorWriter.getErrorFileName()
								+ "\n\nТекст ошибки:\n\n"
								+ e.getMessage(), 
								MessageTypes.ERROR);
				return;
			}
		}
		
	}
	

	private void createContents() {

		if (tray == null) {
			
			new ShowErrorDialog (display, new SWTException("ERROR - System Tray unavailable!"), "Ошибка запуска программы! Для просмотра ошибки нажмите кнопку [<<< Подробней]", false).open();
			Runtime.getRuntime().halt(2);
			
		} else {		

			scheduledExecutorService = Executors.newScheduledThreadPool(1);
			trayItem = new TrayItem(tray, SWT.NONE);
			shell = new Shell(display);
			menu = new Menu(shell, SWT.POP_UP);
			
			final MenuItem menuStart = new MenuItem(menu, SWT.PUSH);
			menuStart.setText("Старт");

			menu.setDefaultItem(menuStart);

			final MenuItem menuStop = new MenuItem(menu, SWT.PUSH);
			menuStop.setText("Стоп");
			menuStop.setEnabled(false);

			new MenuItem(menu, SWT.SEPARATOR);

			final MenuItem menuCheckTestDates = new MenuItem(menu, SWT.CHECK);
			menuCheckTestDates.setSelection(settingsStore.getCheckTestDatesState());			
			menuCheckTestDates.setText("Проверять Тестовый контур");
			
			new MenuItem(menu, SWT.SEPARATOR);
			
			final MenuItem menuAbout = new MenuItem(menu, SWT.PUSH);
			menuAbout.setText("О программе");
			
			new MenuItem(menu, SWT.SEPARATOR);
			
			final MenuItem menuExit = new MenuItem(menu, SWT.PUSH);
			menuExit.setText("Выход");

			
			menuStop.setEnabled(true);
			menuStart.setEnabled(false);
			menu.setDefaultItem(menuStop);
			
			
			restartScheduledExecutorService();
			
			
			
			/* Adding actions on menu items */
			
		
			/* The START MenuItem pressed */
			menuStart.addListener(SWT.Selection, new Listener() {
				public void handleEvent(Event event) {
					menuStop.setEnabled(true);
					menuStart.setEnabled(false);
					menu.setDefaultItem(menuStop);
					restartScheduledExecutorService();
				}
			});

			
			/* The STOP MenuItem pressed */
			menuStop.addListener(SWT.Selection, new Listener() {
				public void handleEvent(Event event) {
					shutdownScheduledExecutorService();
					menuStop.setEnabled(false);
					menuStart.setEnabled(true);
					menu.setDefaultItem(menuStart);
					messageBuilder.updateTrayNotifierToolTip(getNotifierStateText());
					trayItem.setToolTipText(messageBuilder.getTrayNotifierToolTip().toString());					
				}
			});
			
			
			/* The CheckTEstDates MenuItem pressed */
			menuCheckTestDates.addListener(SWT.Selection, new Listener() {
				public void handleEvent(Event event) {
					settingsDAO.updateCheckTestDatesState(menuCheckTestDates.getSelection());
					settingsStore.setCheckTestDatesState(menuCheckTestDates.getSelection());
					restartScheduledExecutorService();
				}
			});
			
			/* The About MenuItem pressed */
			menuAbout.addListener(SWT.Selection, new Listener() {
				public void handleEvent(Event event) {
					new AboutWindow(appEnv).open();
				}
			});
			
			/* The EXIT MenuItem pressed */
			menuExit.addListener(SWT.Selection, new Listener() {
				public void handleEvent(Event event) {
					appEnv.shutdownApplication();
				}
			});
			
			shell.addListener(SWT.CLOSE, new Listener() {
				public void handleEvent(Event event) {
					appEnv.shutdownApplication();
				}
			});
			
			trayItem.addListener(SWT.MenuDetect, new Listener() {
				public void handleEvent(Event event) {
					menu.setVisible(true);
				}
			});
			
			if (windowImage != null) trayItem.setImage(windowImage);
		}    
	}
	
	

	/*
	 *  Starts a one-shot or periodic task in Scheduled Executor Service
	 */
	private void startScheduledExecutorService(Runnable task, boolean isQuickStart) {
		if(scheduledExecutorService.isShutdown()) {
			scheduledExecutorService = Executors.newScheduledThreadPool(1);
		}

		if(isQuickStart) {
			/* Starts a one-shot task after 1 second for updating trayItem ToolTipText */
			scheduledExecutorService.schedule(task, 1, TimeUnit.SECONDS);
		} else {
			/* Starts a periodic action after 3300 seconds */
			scheduledExecutorService.scheduleAtFixedRate(task, 3300, 300, TimeUnit.SECONDS);
		}
	}
	
	
	/*
	 *  Shutdown ScheduledExecutorService
	 */
	private void shutdownScheduledExecutorService() {
		if (!scheduledExecutorService.isShutdown()) this.scheduledExecutorService.shutdownNow();
	}
	
	/*
	 *  Restart ScheduledExecutorService
	 */
	private void restartScheduledExecutorService() {
		shutdownScheduledExecutorService();
		startScheduledExecutorService(notifierTask, true);
		startScheduledExecutorService(notifierTask, false);
		
	}
	

	/*
	 * Get a string representing the state of the ScheduledExecutorService
	 */
	private String getNotifierStateText() {
		return scheduledExecutorService.isShutdown() ? "остановлен" : "работает  ";
	}
	
}
