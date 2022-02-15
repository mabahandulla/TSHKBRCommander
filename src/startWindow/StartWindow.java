package startWindow;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import daoPattern.CredentialsStore;
import daoPattern.InformationStore;
import daoPattern.LoginStore;
import daoPattern.SettingsDAO;
import daoPattern.SettingsStore;
import templateWindow.TemplateClass;
import daoPattern.CredentialsDAO;
import infoWindow.AboutWindow;
import infoWindow.TshKbrInfo;
import notifierModule.TrayNotifireClass;
import enumsStore.MessageTypes;
import enumsStore.ApplicationMode;
import enumsStore.ButtonsSize;
import enumsStore.CredentialsSwitches;
import utilities.CreateErrorLog;
import utilities.GeneratorSettings;
import utilities.MessageDialog;
import utilities.PasswordChanger;
import utilities.ProcessExecutor;
import utilities.ShowErrorDialog;

import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;


/**
 * This is the main class of the application.
 * <p>
 * The application is intended for storing, changing credentials and generating passwords
 * for connecting to the Bank of Russia Client's Transport Gateway, as well as for generating
 * a settings file for automatically connecting to the VPN server of the Bank of Russia.
 * <p>
 * This is the main application class, which includes two subclasses:
 * the Login class and the class for changing the interface of the main application window.
 * <p>
 * @author Aleksander Demichev
 * @version 1.0.0.0
 */



public final class StartWindow {
	
	private final Display DISPLAY = Display.getDefault();
	private ApplicationEnvironments appEnv;
	
	private Shell shell;
	private String rootDirectory;
  
	private CreateErrorLog errorWriter;
	private MessageDialog messages;
	
	private CredentialsDAO credentialsDAO;
	private SettingsDAO settingsDAO;
	
	private CredentialsStore psbrTest, psbrProm, spfsTest, spfsProm;
	private InformationStore informationStore;

	private SettingsStore settingsStore;
	private ProcessExecutor processLuncher;

	private boolean usePsbr, useSpfs;
	
	/* Images for setting application icons */
	private Image windowImage, cbLogo;
   


	
	
	private StartWindow() {
		this.appEnv = new ApplicationEnvironments(this.DISPLAY, ApplicationMode.COMMANDER);
		this.rootDirectory = this.appEnv.getApplicationRootDirectory();
		this.messages = this.appEnv.getMessages();
		this.errorWriter = this.appEnv.getErrorWriter();
		this.windowImage = this.appEnv.getWindowIcon();
		this.cbLogo = this.appEnv.getCbLogo();	
		this.settingsDAO = this.appEnv.getSettingsDAO();
		this.settingsStore = this.appEnv.getSettingsStore();
		this.credentialsDAO = this.appEnv.getCredentialsDAO();
		this.informationStore = this.appEnv.getInformationStore();
		this.processLuncher = this.appEnv.getProcessExecutor();
	}
	


	
	
	
	
	 /*********************************
	  * Login Window Class
	  ********************************/
		private final class ShowLoginWindow {

			private Display DISPLAY;
			private Shell shell;
			private Image windowImage;
			private Text textLogin;
			private Text textPassword;
			private LoginStore loginStore;

			private ShowLoginWindow(Display DISPLAY) {

				this.DISPLAY = DISPLAY;
				this.shell = new Shell(this.DISPLAY, SWT.BORDER | SWT.APPLICATION_MODAL | SWT.CLOSE);
				this.windowImage = StartWindow.this.windowImage;

				this.shell.addShellListener(new ShellAdapter() {
					@Override
					public void shellClosed(ShellEvent e) {
						StartWindow.this.appEnv.shutdownApplication();
					}
				});

				this.shell.setSize(246, 185);
				this.shell.setText("Вход в программу");
				if (this.windowImage != null)
					this.shell.setImage(this.windowImage);

				Composite composite = new Composite(this.shell, SWT.NONE);
				composite.setBounds(1, 0, 229, 146);
				composite.setLayout(null);

				Group group = new Group(composite, SWT.NONE);
				group.setText("Вход в программу");
				group.setBounds(14, 10, 200, 93);

				Label labelLogin = new Label(group, SWT.NONE);
				labelLogin.setAlignment(SWT.RIGHT);
				labelLogin.setBounds(10, 33, 44, 15);
				labelLogin.setText("Логин");

				Label labelPassword = new Label(group, SWT.NONE);
				labelPassword.setAlignment(SWT.RIGHT);
				labelPassword.setBounds(10, 61, 44, 15);
				labelPassword.setText("Пароль");

				this.textLogin = new Text(group, SWT.BORDER);
				this.textLogin.setBounds(60, 30, 125, 21);

				this.textLogin.addKeyListener(new KeyAdapter() {
					@Override
					public void keyPressed(KeyEvent e) {
						pressedKeyParser(e);
					}
				});

				this.textPassword = new Text(group, SWT.BORDER | SWT.PASSWORD);
				this.textPassword.setBounds(60, 58, 125, 21);

				this.textPassword.addKeyListener(new KeyAdapter() {
					@Override
					public void keyPressed(KeyEvent e) {
						pressedKeyParser(e);
					}
				});

				Button btnOk = new Button(composite, SWT.NONE);

				btnOk.addListener(SWT.Selection, new Listener() {
					public void handleEvent(Event event) {
						isloginSuccess(textLogin.getText(),
								textPassword.getText());
					}
				});


				btnOk.setBounds(30, 109, ButtonsSize.BUTTON_WIDTH.size(), ButtonsSize.BUTTON_HEIGHT.size());
				btnOk.setText("OK");

				Button buttonCancel = new Button(composite, SWT.NONE);
				buttonCancel.setLocation(130, 109);
				buttonCancel.setSize(ButtonsSize.BUTTON_WIDTH.size(), ButtonsSize.BUTTON_HEIGHT.size());
				buttonCancel.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						StartWindow.this.appEnv.shutdownApplication();
					}
				});
				buttonCancel.setText("Отмена");

				Monitor primary = this.DISPLAY.getPrimaryMonitor();
				Rectangle bounds = primary.getBounds();
				Rectangle rect = this.shell.getBounds();
				int x = bounds.x + (bounds.width - rect.width) / 2;
				int y = bounds.y + (bounds.height - rect.height) / 2;
				this.shell.setLocation(x, y);

				this.shell.open();
				this.shell.layout();
			}

			/* Check if Login success */
			private void isloginSuccess(String login, String password) {
				
				this.loginStore = StartWindow.this.appEnv.getLoginDAO().getData(1);
				
				if (this.loginStore.getLogin().trim().toLowerCase().equals(login.trim().toLowerCase()) &&
						this.loginStore.getPassword().equals(password)) {

					this.closeResources();
					StartWindow.this.open();

				} else {
					StartWindow.this.messages.showMessage("Неверный логин или пароль!", MessageTypes.ERROR);
					return;
				}
			}

			/*
			 * Parse key pressed:
			 * 
			 * 13 (ENTER) or 16777296 (DIGITAL ENTER) call check credentials method.
			 * 27 (ESC) exit application
			 * 
			 */
			private void pressedKeyParser(KeyEvent e) {
				int keyPressed = e.keyCode;

				if (keyPressed == 13 || keyPressed == 16777296) {
					this.isloginSuccess(this.textLogin.getText(), this.textPassword.getText());
				}

				if (keyPressed == 27) {
					StartWindow.this.appEnv.shutdownApplication();
				}
			}

			public void closeResources() {
				this.loginStore = null;
				if (this.shell != null)	this.shell.dispose();
			}

		}

		/*********************************
		 * Login Window Class END
		 ********************************/
	 
	 
	 
	 

	 /*********************************
	  * Choose Interface Class
	  ********************************/
	 
	 private final class ChooseInterface {
		 
			/*
			 * Flag which indicates that this window was called from StartWindow class menu
			 */
			private boolean calledFromApplication = false;
			
			private Display display;
			private Shell shell;
			private Image windowImage;
			private SettingsDAO settingsDAO;
			private SettingsStore settingsStore;


			private ChooseInterface(Display display){
				this.display = display;
				this.windowImage = StartWindow.this.windowImage;
				this.settingsDAO = StartWindow.this.settingsDAO;
				this.settingsStore = StartWindow.this.settingsStore;
			}

			
			

			/**
			 * Open the window of the ChooseInterface class.
			 */
			public void open() {
			
				shell = new Shell(SWT.BORDER | SWT.APPLICATION_MODAL | SWT.CLOSE);
				
				shell.addShellListener(new ShellAdapter() {
					@Override
					public void shellClosed(ShellEvent e) {
						closeResources();
					}
				});
				
				if(windowImage != null) shell.setImage(windowImage);
				shell.setSize(300, 200);
				shell.setText("Выбор интерфейса");
				
				Group group = new Group(shell, SWT.NONE);
				group.setText("Выберите интерфейс программы");
				group.setBounds(11, 10, 262, 110);
				
				Button onlyPsbrButton = new Button(group, SWT.RADIO);
				onlyPsbrButton.setBounds(79, 37, 104, 16);
				onlyPsbrButton.setText("Только ПС БР");

				
				Button onlySpfsButton = new Button(group, SWT.RADIO);
				onlySpfsButton.setBounds(79, 59, 104, 16);
				onlySpfsButton.setText("Только СПФС");

				
				Button bothButton = new Button(group, SWT.RADIO);
				bothButton.setBounds(79, 84, 104, 16);
				bothButton.setText("ПС БР и СПФС");
				
				setRadioButtonsState(group.getChildren());
				
				Button btnOk = new Button(shell, SWT.NONE);
				btnOk.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {

						setSelectedInterfaceState(group.getChildren());
						settingsDAO.updateData(settingsStore);
						closeResources();

						if (calledFromApplication) {

							StartWindow.this.shell.dispose();
							StartWindow.this.open();
						}
					}
				});
				
				
				btnOk.setBounds(50, 126, ButtonsSize.BUTTON_WIDTH.size(), ButtonsSize.BUTTON_HEIGHT.size());
				btnOk.setText("OK");

				
				Button buttonCancel = new Button(shell, SWT.NONE);
				buttonCancel.setLocation(155, 126);
				buttonCancel.setSize(ButtonsSize.BUTTON_WIDTH.size(), ButtonsSize.BUTTON_HEIGHT.size());
				buttonCancel.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						closeResources();
					}
				});
				buttonCancel.setText("Отмена");
				
				
				Monitor primary = display.getPrimaryMonitor();
				Rectangle bounds = primary.getBounds();
				Rectangle rect = shell.getBounds();
				int x = bounds.x + (bounds.width - rect.width) / 2;
				int y = bounds.y + (bounds.height - rect.height) / 2;
				shell.setLocation(x, y);
				
				
				shell.open();
				shell.layout();
			}

			/*
			 * 1. Get parent Group object.
			 * 2. If PSBR and SPFS radio buttons are not selected, that mean`s that selected third radio "BOTH BLOCKS".
			 * 3. Update boolean values in settingsStore, set it TRUE, that means when the StartWindow opens, all blocks of interfaces will be shown.
			 * 4. Otherwise, if one of radio button selected, PSBR or SPFS, then update boolean value in settingsStore.
			 * 5. And finally, set boolean flag to TRUE in the settingsStore, to indicate that interface of application is selected.
			 */
			private void setSelectedInterfaceState(Control[] radioButtons) {

				if(!((Button) radioButtons[0]).getSelection() && !((Button) radioButtons[1]).getSelection()) {
					settingsStore.setPsbrState(true);
					settingsStore.setSpfsState(true);
				} else {
					settingsStore.setPsbrState(((Button) radioButtons[0]).getSelection());
					settingsStore.setSpfsState(((Button) radioButtons[1]).getSelection());
				}

				settingsStore.setIterfaceState(true);
			}
			
			
			/* Get values from settingsStore and set radio buttons state */
			private void setRadioButtonsState(Control[] radioButtons) {
				if(settingsStore.getPsbrState() && settingsStore.getSpfsState()) {
					((Button) radioButtons[2]).setEnabled(false);
				} else if (settingsStore.getPsbrState()) {
					((Button) radioButtons[0]).setEnabled(false);
				} else if (settingsStore.getSpfsState()) {
					((Button) radioButtons[1]).setEnabled(false);
				}				
			}
			
			/* Set Boolean flag which indicate is this window called from StartWindow or on application startup */
			public void setWindowCallState(boolean state) {
				calledFromApplication = state;
			}
			
			
			public void closeResources() {
				if (shell != null) shell.dispose();
			}
			
		}
	 
	 /*********************************
	  * Choose Interface Class END
	  ********************************/ 
	 

    

	
	/**
	 * Open the window of the StartWindow class.
	 * @wbp.parser.entryPoint
	 */
	public void open() {
		
		//Update the interface blocks state  
        usePsbr = settingsStore.getPsbrState();
        useSpfs = settingsStore.getSpfsState();
		
        shell = new Shell(DISPLAY, SWT.BORDER | SWT.MIN | SWT.CLOSE);
		
        shell.addShellListener(new ShellAdapter() {
			@Override
			public void shellClosed(ShellEvent e) {
				appEnv.shutdownApplication();
			}
		});
		
		if(windowImage != null) shell.setImage(windowImage);
		shell.setToolTipText("ТШ КБР Commander");
		shell.setText("ТШ КБР Commander");
		shell.setSize(339, 191);
		
		Composite composite = new Composite(shell, SWT.NONE);		
		composite.setBounds(0, 0, 317, 128);
		composite.setLayout(null);

		
		/*
		 * =============================
		 * INFO Group contents generate
		 * =============================
		 */
			
		
		Group infoGroup = new Group(composite, SWT.NONE);
		infoGroup.setText("ТШ КБР ИНФО");

		Button tshkbrButtonInfo = new Button(infoGroup, SWT.NONE);
		tshkbrButtonInfo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				new TshKbrInfo(appEnv).open();
			}
		});

		tshkbrButtonInfo.setText("Открыть");		
		
		/*
		 * ==============================
		 * IMAGE Group contents generate
		 * ==============================
		 */
		Group groupImg = new Group(composite, SWT.NONE);
		groupImg.setBounds(110, 10, 94, 103);
		
		Label labelImg = new Label(groupImg, SWT.NONE);
		labelImg.setBounds(1,10, 91, 91);
		labelImg.setToolTipText("Банк России");
		labelImg.setAlignment(SWT.CENTER);
		
		if (cbLogo == null) {
			labelImg.setText(labelImg.getToolTipText());
		} else {
			labelImg.setImage(cbLogo);	
		}
		
		groupImg.setVisible(false);
		groupImg.setEnabled(false);
		
		labelImg.setVisible(false);
		labelImg.setEnabled(false);
		

		/*
		 * =============================
		 * PSBR Group contents generate
		 * =============================
		 */
		
		if (usePsbr) {

			Group psbrGroup = new Group(composite, SWT.NONE);
			psbrGroup.setText("ПС БР");
			psbrGroup.setBounds(10, 10, 94, 103);

			Button psbrButtonProm = new Button(psbrGroup, SWT.NONE);
			psbrButtonProm.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					
					psbrProm = credentialsDAO.getData(2);
					
					LinkedHashMap<String, Method[]> columnsAndMethodsMatchStore = getColumnsAndMethodsMatchStore(CredentialsSwitches.PSBR_PROM, psbrProm);

					new TemplateClass(appEnv,
							(CredentialsStore) psbrProm.clone(),
							columnsAndMethodsMatchStore,
							CredentialsSwitches.PSBR_PROM,
							new String[] { informationStore.getMainIPProm(),
									       informationStore.getRezervIPProm() }).open();
				}
			});
			psbrButtonProm.setBounds(10, 21, 75, 25);
			psbrButtonProm.setText("ПРОМ");

			Button psbrButtonTest = new Button(psbrGroup, SWT.NONE);
			psbrButtonTest.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					psbrTest = credentialsDAO.getData(1);
					
					LinkedHashMap<String, Method[]> columnsAndMethodsMatchStore = getColumnsAndMethodsMatchStore(CredentialsSwitches.PSBR_TEST, psbrTest);

					new TemplateClass(
							appEnv,
							(CredentialsStore) psbrTest.clone(),
							columnsAndMethodsMatchStore,
							CredentialsSwitches.PSBR_TEST,
							new String[] { informationStore.getMainIPTest(),
									       informationStore.getRezervIPTest() }).open();

				}
			});
			psbrButtonTest.setBounds(10, 63, 75, 25);
			psbrButtonTest.setText("ТЕСТ");

			
			/*
			 * If selected only PSBR interface, set new size of the window
			 */
			if (!useSpfs) {
				
				infoGroup.setBounds(213, 10, 94, 103); // move INFO Group to SPFS group place
				tshkbrButtonInfo.setBounds(10, 21, 75, 67); // move INFO button to SPFS group place

				groupImg.setEnabled(true);
				labelImg.setEnabled(true);
				groupImg.setVisible(true); // Show Image group
				labelImg.setVisible(true); // Show Label with image
				

			} else {
		
				infoGroup.setBounds(110, 10, 94, 103); // move to ORIGIN INFO group place
				tshkbrButtonInfo.setBounds(10, 21, 75, 67); // move to ORIGIN INFO group place
				
			}

		}

		/*
		 * =============================
		 * SPFS Group contents generate
		 * =============================
		 */		
		
		if (useSpfs) {

			Group spfsGroup = new Group(composite, SWT.NONE);
			spfsGroup.setText("СПФС");

			Button spfsButtonProm = new Button(spfsGroup, SWT.NONE);
			spfsButtonProm.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					spfsProm = credentialsDAO.getData(4);
					
					LinkedHashMap<String, Method[]> columnsAndMethodsMatchStore = getColumnsAndMethodsMatchStore(CredentialsSwitches.SPFS_PROM, spfsProm);

					new TemplateClass(appEnv,
							(CredentialsStore) spfsProm.clone(),
							columnsAndMethodsMatchStore,
							CredentialsSwitches.SPFS_PROM,
							new String[] { informationStore.getMainIPProm(),
									       informationStore.getRezervIPProm() }).open();
				}
			});

			spfsButtonProm.setText("ПРОМ");

			Button spfsButtonTest = new Button(spfsGroup, SWT.NONE);
			spfsButtonTest.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					spfsTest = credentialsDAO.getData(3);
					
					LinkedHashMap<String, Method[]> columnsAndMethodsMatchStore = getColumnsAndMethodsMatchStore(CredentialsSwitches.SPFS_TEST, spfsTest);

					new TemplateClass(appEnv,
							(CredentialsStore) spfsTest.clone(),
							columnsAndMethodsMatchStore,
							CredentialsSwitches.SPFS_TEST,
							new String[] { informationStore.getMainIPTest(),
									       informationStore.getRezervIPTest() }).open();
				}
			});

			spfsButtonTest.setText("ТЕСТ");

			
			/*
			 * If selected only SPFS interface,
			 * set new size of the window and
			 * move SPFS group and buttons to PSBR items place.
			 */
			if (!usePsbr) {
				
				infoGroup.setBounds(213, 10, 94, 103); // move INFO Group to SPFS group place
				tshkbrButtonInfo.setBounds(10, 21, 75, 67); // move INFO button to SPFS group place

				spfsGroup.setBounds(10, 10, 94, 103); // MOVE SPFS Group to PSBR Group Place
				spfsButtonProm.setBounds(10, 21, 75, 25); // MOVE spfsButtonProm to PSBR Button Place
				spfsButtonTest.setBounds(10, 63, 75, 25); // MOVE spfsButtonTest to PSBR Button Place
				
				groupImg.setEnabled(true);
				labelImg.setEnabled(true);
				groupImg.setVisible(true); // Show Image group and Label with image
				labelImg.setVisible(true); // Show Image group and Label with image
				
			} else {
				
				infoGroup.setBounds(110, 10, 94, 103); // move to ORIGIN place
				tshkbrButtonInfo.setBounds(10, 21, 75, 67); // move to ORIGIN place
				
				spfsGroup.setBounds(213, 10, 94, 103); // ORIGIN PLACE
				spfsButtonProm.setBounds(10, 21, 75, 25); // ORIGIN PLACE
				spfsButtonTest.setBounds(10, 63, 75, 25); // ORIGIN PLACE				
			}

		}

		
		
		/*
		 * =======================
		 * MENU contents generate
		 * =======================
		 */
		
		Menu menu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu);

		MenuItem menuFile = new MenuItem(menu, SWT.CASCADE);
		menuFile.setText("Файл");

		Menu cascadeMenuFile = new Menu(menuFile);
		menuFile.setMenu(cascadeMenuFile);

		MenuItem subOpenDir = new MenuItem(cascadeMenuFile, SWT.NONE);
		subOpenDir.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				processLuncher.runProcess(rootDirectory);
			}
		});
		
		subOpenDir.setText("Открыть каталог программы");

		MenuItem subExitApp = new MenuItem(cascadeMenuFile, SWT.NONE);
		subExitApp.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				appEnv.shutdownApplication();
			}
		});
		subExitApp.setText("Выход");

		MenuItem menuSettings = new MenuItem(menu, SWT.CASCADE);
		menuSettings.setText("Настройки");

		Menu cascadeMenuSettings = new Menu(menuSettings);
		menuSettings.setMenu(cascadeMenuSettings);

		MenuItem subPasswordChange = new MenuItem(cascadeMenuSettings, SWT.NONE);
		subPasswordChange.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				new PasswordChanger(appEnv).open();
			}
		});
		subPasswordChange.setText("Изменить мастер пароль");

		MenuItem subPasswordGenerator = new MenuItem(cascadeMenuSettings, SWT.NONE);
		subPasswordGenerator.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				new GeneratorSettings(appEnv).open();
			}
		});
		subPasswordGenerator.setText("Генератор паролей");
		
		MenuItem menuItem = new MenuItem(cascadeMenuSettings, SWT.NONE);
		menuItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ChooseInterface selectInterfaceWindow = new ChooseInterface(DISPLAY);
				selectInterfaceWindow.setWindowCallState(true);
				selectInterfaceWindow.open();
			}
		});
		menuItem.setText("Выбор интерефейса");
		
		MenuItem menuHelp = new MenuItem(menu, SWT.CASCADE);
		menuHelp.setText("Помощь");
		
		Menu cascadeHelp = new Menu(menuHelp);
		menuHelp.setMenu(cascadeHelp);
		
		MenuItem subAbout = new MenuItem(cascadeHelp, SWT.NONE);
		subAbout.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				new AboutWindow(appEnv).open();
			}
		});
		subAbout.setText("О программе");
		
		Monitor primary = DISPLAY.getPrimaryMonitor();
		Rectangle bounds = primary.getBounds();
		Rectangle rect = shell.getBounds();
		int x = bounds.x + (bounds.width - rect.width) / 2;
		int y = bounds.y + (bounds.height - rect.height) / 2;
		shell.setLocation(x, y);
		
	
		shell.pack();
		shell.open();
		shell.layout();
	}



	private LinkedHashMap<String, Method[]> getColumnsAndMethodsMatchStore (CredentialsSwitches credentialsSwitch, CredentialsStore credentialsStore) {
		LinkedHashMap<String, Method[]> columnsAndMethodsMatchStore = new LinkedHashMap<String, Method[]>(1);
		try {
			columnsAndMethodsMatchStore = credentialsStore.getCheckBoxActions(credentialsSwitch, credentialsStore);
		} catch (Exception e1) {
			errorWriter.printErrorToFile(e1);
			messages.showMessage(
							"\tОшибка при получении данных!\n\nСмотри детали ошибки в лог файле:\n\n"
							+ errorWriter.getErrorFileName()
							+ "\n\nТекст ошибки:\n\n"
							+ e1.getLocalizedMessage(), 
							MessageTypes.ERROR);
		}
		
		return columnsAndMethodsMatchStore;
	}

	

	
	
	 /*********************************
	  * Start application main method
	  ********************************/
	 
	 
	private void startApplication() {
		
		new ShowLoginWindow(DISPLAY);

	     while (!DISPLAY.isDisposed()) {

			try {

				if (!DISPLAY.readAndDispatch()) {
					DISPLAY.sleep();
				}
				
			} catch (Exception e) {
				errorWriter.printErrorToFile(e);
				messages.showMessage(
								"ОШИБКА. Возникла ошибка во время работы программы.\nСмотри детали ошибки в лог файле:\n\n"
								+ errorWriter.getErrorFileName()
								+ "\n\nТекст ошибки:\n\n"
								+ e.getMessage(), 
								MessageTypes.ERROR);
				return;
			}
		}
	}
	
	


	
	
	
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {

		
		if (System.getProperty("os.name").toLowerCase().startsWith("windows")) {

			try {

				if (args.length != 0 && args[0].trim().toLowerCase().equals("-startn")) {

					new TrayNotifireClass(Display.getDefault()).open();

				} else {

					new StartWindow().startApplication();

				}

			} catch (Exception e) {
				new ShowErrorDialog(Display.getDefault(), e, "Ошибка запуска программы! Для просмотра ошибки нажмите кнопку [<<< Подробней]", true).open();
				Runtime.getRuntime().halt(2);
			}

		} else {
			new ShowErrorDialog(Display.getDefault(),
					new RuntimeException("Error starting TSH KBR Commander! ERROR: OS not supported."),
					"Операционная система не поддерживается!", true).open();
			Runtime.getRuntime().exit(3);
		}
	}
}
