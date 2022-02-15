package templateWindow;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import java.io.File;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import daoPattern.CredentialsDAO;
import daoPattern.CredentialsStore;
import enumsStore.ButtonsSize;
import enumsStore.CredentialsSwitches;
import enumsStore.CredentialsTypes;
import enumsStore.MessageTypes;
import enumsStore.ModesOfOperation;
import enumsStore.VpnFileEnvironment;
import startWindow.ApplicationEnvironments;
import utilities.CreateErrorLog;
import utilities.GeneratorSettings;
import utilities.MessageBuilder;
import utilities.MessageDialog;
import utilities.ProcessExecutor;
import utilities.TshKbrFileWriter;
import utilities.ValuesValidator;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;



/**
 * This is a class for displaying and changing credentials in the database,
 * as well as for creating a file with settings for connecting to TSH KBR VPN server.
 * <p>
 * @author Aleksander Demichev
 * @version 1.0.0.0
 */

public final class TemplateClass {
	
	private Display display;
	private Shell shell;
	private ApplicationEnvironments appEnv;

	/* Instance for showing messages to user */
	private MessageDialog messages;
	
	/* Instance for writing errors to error.log */
	private CreateErrorLog errorWriter;
	
	private Image windowImage, buttonKey;
	private CredentialsDAO credentialsDAO;
	private CredentialsStore credentialsStore;
	private CredentialsSwitches credentialsSwitch;
	private ValuesValidator validator;
	private MessageBuilder messageBuilder;
	private ProcessExecutor processLuncher;

	/* Instance of the GeneratorSettings class for getting updated password length*/
	private GeneratorSettings passwordGenerator;
	
	private DateDifferenceCalculator dateDifferenceCalculator;
	
	private LinkedHashMap<String, Method[]> objectMethods;
	private String [] vpnServersIP;

	private Label labelKanalLogin;
	private Label labelKanalPassword;
	private Label labelPriklLogin;
	private Label labelPriklPassword;
	
	private Text textKanalLogin;
	private Text textKanalPassword;
	private Text textPriklLogin;
	private Text textPriklPassword;
	
	
	/* List for storing names of the transmission systems, which passwords must be changed 
	private List<String> namesOfTransmissionSystems = new ArrayList<String>(4);*/

	public TemplateClass(ApplicationEnvironments appEnv,
						 CredentialsStore credentialsStore,
						 LinkedHashMap<String, Method[]> objectMethods,
						 CredentialsSwitches credentialsSwitch,
						 String [] vpnServersIP) {
		
		this.appEnv = appEnv;
		this.display = appEnv.getDisplay();
		this.windowImage = appEnv.getWindowIcon();
		this.buttonKey = appEnv.getButtonKey();
		this.messages = appEnv.getMessages();
		this.errorWriter = appEnv.getErrorWriter();
		this.credentialsDAO = appEnv.getCredentialsDAO();
		this.dateDifferenceCalculator = appEnv.getDateDifferenceCalculator();
		this.credentialsStore = credentialsStore;
		this.setObjectMethodsMap(objectMethods);
		this.passwordGenerator = this.getUpdatedPasswordGenerator();
		this.validator = appEnv.getValuesValidator();
		this.messageBuilder = appEnv.getMessageBuilder();
		this.credentialsSwitch = credentialsSwitch;
		this.setVpnServersIP(vpnServersIP);
		this.processLuncher = appEnv.getProcessExecutor();
	}
    


	
	
	/**
	 * Open the window.
	 * @wbp.parser.entryPoint
	 */
	public void open() {


		
		shell = new Shell(display, SWT.BORDER | SWT.APPLICATION_MODAL | SWT.CLOSE);
		
		shell.addShellListener(new ShellAdapter() {
			@Override
			public void shellClosed(ShellEvent e) {
				closeResources();
			}
		});
		
		if(windowImage != null) shell.setImage(windowImage);
		
		shell.setSize(380, 450);
		shell.setText(credentialsSwitch.getTitle());
		
		
		Composite composite = new Composite(shell, SWT.NONE);
		composite.setBounds(0, 0, 360, 401);
		
		/***************************************************************************
		 * CHANNEL GROUP
		 ***************************************************************************/
		
		Group channelGroup = new Group(composite, SWT.NONE);
		channelGroup.setText("Канальные учетные данные");
		channelGroup.setBounds(10, 10, 340, 130);
		
		labelKanalLogin = new Label(channelGroup, SWT.NONE); //Index in parentGroup is [0]
		labelKanalLogin.setBounds(10, 20, 55, 15);
		labelKanalLogin.setText("Логин");
		
		textKanalLogin = new Text(channelGroup, SWT.BORDER); //Index in parentGroup is [1]
		textKanalLogin.setEditable(false);
		textKanalLogin.setTextLimit(20);
		textKanalLogin.setBounds(10, 41, 127, 21);
		textKanalLogin.setText(credentialsStore.getLoginKanal());
		
		labelKanalPassword = new Label(channelGroup, SWT.NONE); //Index in parentGroup is [2]
		labelKanalPassword.setBounds(10, 68, 55, 15);
		labelKanalPassword.setText("Пароль");
		
		textKanalPassword = new Text(channelGroup, SWT.BORDER); //Index in parentGroup is [3]
		textKanalPassword.setEditable(false);
		textKanalPassword.setTextLimit(20);
		textKanalPassword.setBounds(10, 89, 127, 21);
		textKanalPassword.setText(credentialsStore.getPasswordKanal());
		
		Label labelKanalDate = new Label(channelGroup, SWT.NONE);
		labelKanalDate.setBounds(203, 20, 127, 15);
		labelKanalDate.setText("Дата смены пароля");

		Label labelShowKanalDate = new Label(channelGroup, SWT.NONE);
		labelShowKanalDate.setBounds(203, 41, 116, 15);
		labelShowKanalDate.setText(convertMillisecondsToDate(credentialsStore.getDateSetKanal()));

		Label labelKanalOldPassword = new Label(channelGroup, SWT.NONE);
		labelKanalOldPassword.setBounds(203, 68, 127, 15);
		labelKanalOldPassword.setText("Предыдущий пароль");
		
		Text textKanalOldPassword = new Text(channelGroup, SWT.BORDER);
		textKanalOldPassword.setEditable(false);
		textKanalOldPassword.setTextLimit(20);
		textKanalOldPassword.setBounds(203, 89, 127, 21);
		textKanalOldPassword.setText(credentialsStore.getPasswordKanalOld());
		
		Button buttonKanalGenerator = new Button(channelGroup, SWT.NONE);
		buttonKanalGenerator.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				textKanalPassword.setText(passwordGenerator.getPassword());
				textKanalPassword.getParent().redraw();
			}
		});
		
		buttonKanalGenerator.setBounds(143, 89, 55, 21);
		buttonKanalGenerator.setImage(buttonKey);
		buttonKanalGenerator.setToolTipText("Левая кнопка: сгенерировать новый пароль\nПравая кнопка: Генератор пароля");

		/************
		 * VPN GROUP
		 ************/
		Group groupVPN = new Group(composite, SWT.NONE);
		groupVPN.setToolTipText("Если чекбокс активен будет сформирован файл\nдля подключения к выбранному серверу VPN");
		groupVPN.setText("Формирование файла настроек для подключения к VPN");
		groupVPN.setBounds(10, 146, 340, 78);
		
		Button checkboxCreateVPNFile = new Button(groupVPN, SWT.CHECK);
		checkboxCreateVPNFile.setText("Формировать файл");
		checkboxCreateVPNFile.setBounds(10, 23, 320, 16);
	
		Button radioMainIP = new Button(groupVPN, SWT.RADIO);
		radioMainIP.setSelection(getVPNRadioState(credentialsSwitch)[0]);
		radioMainIP.setBounds(10, 45, 127, 21);
		radioMainIP.setText("Основной сервер");
		
		Button radioRezervIP = new Button(groupVPN, SWT.RADIO);
		radioRezervIP.setSelection(getVPNRadioState(credentialsSwitch)[1]);
		radioRezervIP.setText("Резервный сервер");
		radioRezervIP.setBounds(203, 45, 127, 21);
		
		checkBoxActions(checkboxCreateVPNFile, ModesOfOperation.GET_MODE);
		
		radioMainIP.setEnabled(checkboxCreateVPNFile.getSelection());
		radioRezervIP.setEnabled(checkboxCreateVPNFile.getSelection());
		
		checkboxCreateVPNFile.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				checkBoxActions(checkboxCreateVPNFile, ModesOfOperation.SET_MODE);
				
				radioMainIP.setEnabled(checkboxCreateVPNFile.getSelection());
				radioRezervIP.setEnabled(checkboxCreateVPNFile.getSelection());

			}
		});
		
		/***************************************************************************
		 * APPLICATION GROUP
		 ***************************************************************************/
		
		Group applicationGroup = new Group(composite, SWT.NONE);
		applicationGroup.setText("Прикладные учетные данные");
		applicationGroup.setBounds(10, 230, 340, 130);
		
		labelPriklLogin = new Label(applicationGroup, SWT.NONE); //Index in parentGroup is [0]
		labelPriklLogin.setText("Логин");
		labelPriklLogin.setBounds(10, 20, 55, 15);
		
		textPriklLogin = new Text(applicationGroup, SWT.BORDER); //Index in parentGroup is [1]
		textPriklLogin.setEditable(false);
		textPriklLogin.setTextLimit(20);
		textPriklLogin.setBounds(10, 41, 127, 21);
		textPriklLogin.setText(credentialsStore.getLoginPrikl());
		
		labelPriklPassword = new Label(applicationGroup, SWT.NONE); //Index in parentGroup is [2]
		labelPriklPassword.setText("Пароль");
		labelPriklPassword.setBounds(10, 68, 55, 15);
		
		textPriklPassword = new Text(applicationGroup, SWT.BORDER); //Index in parentGroup is [3]
		textPriklPassword.setEditable(false);
		textPriklPassword.setTextLimit(20);
		textPriklPassword.setBounds(10, 89, 127, 21);
		textPriklPassword.setText(credentialsStore.getPasswordPrikl());
		
		Label labelPriklDate = new Label(applicationGroup, SWT.NONE);
		labelPriklDate.setText("Дата смены пароля");
		labelPriklDate.setBounds(203, 20, 127, 15);
		
		Label labelShowPriklDate = new Label(applicationGroup, SWT.NONE);
		labelShowPriklDate.setText(convertMillisecondsToDate(credentialsStore.getDateSetPrikl()));
		labelShowPriklDate.setBounds(203, 41, 116, 15);

		Label labelPriklOldPassword = new Label(applicationGroup, SWT.NONE);
		labelPriklOldPassword.setText("Предыдущий пароль");
		labelPriklOldPassword.setBounds(203, 68, 127, 15);
		
		Text textPriklOldPassword = new Text(applicationGroup, SWT.BORDER);
		textPriklOldPassword.setEditable(false);
		textPriklOldPassword.setTextLimit(20);
		textPriklOldPassword.setBounds(203, 89, 127, 21);
		textPriklOldPassword.setText(credentialsStore.getPasswordPriklOld());
		
		Button buttonPriklGenerator = new Button(applicationGroup, SWT.NONE);
		buttonPriklGenerator.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				textPriklPassword.setText(passwordGenerator.getPassword());
				textPriklPassword.getParent().redraw();
			}
		});
		buttonPriklGenerator.setBounds(144, 89, 53, 21);
			
		buttonPriklGenerator.setImage(buttonKey);
		buttonPriklGenerator.setToolTipText("Левая кнопка: сгенерировать новый пароль\nПравая кнопка: Генератор пароля");

		
		
		Button buttonOK = new Button(composite, SWT.NONE);	
		buttonOK.setBounds(90, 366, ButtonsSize.BUTTON_WIDTH.size(), ButtonsSize.BUTTON_HEIGHT.size());
		buttonOK.setText("OK");
		
		Button buttonCancel = new Button(composite, SWT.NONE);
		buttonCancel.setText("Отмена");
		buttonCancel.setBounds(195, 366, ButtonsSize.BUTTON_WIDTH.size(), ButtonsSize.BUTTON_HEIGHT.size());
		
		
		/* Get password generator buttons and set on the mouse right click listeners */
		Button [] passwordGeneratorButtons = {
				buttonKanalGenerator,
				buttonPriklGenerator
		};
		
		for (Button passwordGeneratorButton : passwordGeneratorButtons) {
			
			passwordGeneratorButton.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseDown(MouseEvent e) {
					if (e.button == 3) {
						passwordGenerator = getUpdatedPasswordGenerator();
						passwordGenerator.open();
					}
				}
			});
		}
		
		
		
		/*
		 * Iterate thru "textFieldsArray" array of Text Items objects
		 * and set every Text item same actions
		 */
		Text[] textFieldsArray = { 
				textKanalLogin,
				textKanalPassword,
				textPriklLogin,
				textPriklPassword
		};

	
		for (Text textField : textFieldsArray) {
		
			if (!textField.isDisposed()) {

				textField.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseDoubleClick(MouseEvent e) {
						textField.setEditable(true);
						textField.getParent().redraw();
					}
				});

				textField.addFocusListener(new FocusAdapter() {
					@Override
					public void focusLost(FocusEvent e) {
						textField.setEditable(false);
					}
				});

				textField.addMouseTrackListener(new MouseTrackAdapter() {
					public void mouseHover(MouseEvent e) {
						textField.setToolTipText("Двойной щелчок редактирование поля");
					}
				});
			}
		}
		
		
		/*
		 * 1. Get days count from last password changed date.
		 * 2. Call the method to compare the number of days that have passed since the password was changed.
		 * 3. If number of days more then 35 - show warning message and set date label color to DARK RED
		 * 4. If number of days more then 45 - show error message and set date label color to RED
		 */

		 dateDifferenceCalculator.checkDaysCountOfPasswordChanged(credentialsStore.getDateSetKanal(), labelShowKanalDate, CredentialsTypes.CHANNEL, null, false);
		 dateDifferenceCalculator.checkDaysCountOfPasswordChanged(credentialsStore.getDateSetPrikl(), labelShowPriklDate, CredentialsTypes.APPLICATION, null, false);
		 
		 if (!dateDifferenceCalculator.getTransmitionSystemStore().isEmpty()) {

			messageBuilder.setStringToNotifierMessage(messageBuilder.getStringSeparator(78) + "Пароль нужно менять не реже 1 раза в 45 дней");

			messages.showMessage(
					messageBuilder.getNotifierMessage().toString(),
					messageBuilder.getNotifierMessageType());

			dateDifferenceCalculator.clearTransmitionSystemStore();
			messageBuilder.clearNotifierMessage();
		 }

			

			
			/*
			 * 1. Check if the Text fields in selected Group is blank
			 * 2. Check if the Text fields in selected Group contains whitespace char
			 * 3. Check if the Login Text field and Password Text field is modified and:
			 * 	3.1 Add String with transmission system to StringBuilder notifier message;
			 * 	3.2 Add Label names that correspond to the text field to StringBuilder notifier message;
			 * 	3.3 Ask user for save modified values to database.
			 * 4. If user accept changes, update changed values in CredentialsStore object and put it to CredentialsDAO for save in Database.
			 */
			buttonOK.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					
					if(validator.checkEmptyText(channelGroup) || validator.checkEmptyText(applicationGroup)) {
						messages.showMessage("Поле не может быть пустым!", MessageTypes.WARNING);
						return;
					}
					
					if(validator.checkWhiteSpaces(channelGroup) || validator.checkWhiteSpaces(applicationGroup)) {
						messages.showMessage("Нельзя использовать пробелы!", MessageTypes.WARNING);
						return;
					}

				
					String[][] storedValues = {
							{credentialsStore.getLoginKanal(), credentialsStore.getPasswordKanal()},
							{credentialsStore.getLoginPrikl(), credentialsStore.getPasswordPrikl()}
						};

					/* Checking if the values in the text fields differ from the values stored in the storage credentialsStore */
					isTextValueChanged(composite, storedValues);

					
					/* 
					 * If the values of the text fields have changed,
					 * then we give the user a request to save the changed fields,
					 * and if the checkbox for generating a file with VPN settings is activated,
					 * then we save changed data and generate a new file with settings for connecting to the VPN server.
					 */
					
					if (!messageBuilder.getNotifierMessage().isEmpty()) {
						messageBuilder.setNotifierMessageType(MessageTypes.WARNING_QUESTION);
						if (messages.showMessage(
										"Следующие поля изменены:\n\n"
										+ messageBuilder.getNotifierMessage().toString()
										+ "\n\nСохранить изменения?", 
										messageBuilder.getNotifierMessageType()) == SWT.YES) {
							
							credentialsDAO.updateData(credentialsStore);
							
							if(checkboxCreateVPNFile.getSelection()) writeVpnConfigFile(groupVPN);
							
						}
						
						messageBuilder.clearNotifierMessage();
						
					} else {
						
						/* 
						 * If the credentials have not been changed,
						 * but the checkbox for creating a VPN file is activated,
						 * we issue a request to the user to generate a new VPN file
						 * with the current credentials.
						 */
						
						if(checkboxCreateVPNFile.getSelection()) {
						
							messageBuilder.setNotifierMessageType(MessageTypes.WARNING_QUESTION);
							messageBuilder.setStringToNotifierMessage(
									messageBuilder.getStringSeparator(50)
									+ "\nУчетные данные не были изменены!\n\nСформировать НОВЫЙ файл\nс настройками для подключения к VPN\nс текущими учетными данными?\n"
									+ messageBuilder.getStringSeparator(50));
							
							if(messages.showMessage(
									
								messageBuilder.getNotifierMessage().toString(), 
								messageBuilder.getNotifierMessageType()) == SWT.YES) {
							
								writeVpnConfigFile(groupVPN);
								
							}
							
							messageBuilder.clearNotifierMessage();
						}
						
						credentialsDAO.updateVPNCheckboxState(
								(String) getObjectMethodsMap().keySet().toArray()[0],
								checkboxCreateVPNFile.getSelection(),
								credentialsStore.getId());
						
					}
					
					
					closeResources();
				}
			});
			
			
			buttonCancel.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					closeResources();
				}
			});
		

		
			
		Monitor primary = display.getPrimaryMonitor();
		Rectangle bounds = primary.getBounds();
		Rectangle rect = shell.getBounds();
		int x = bounds.x + (bounds.width - rect.width) / 2;
		int y = bounds.y + (bounds.height - rect.height) / 2;
		shell.setLocation(x, y);		
		shell.open();
		shell.layout();
	}


	
	/* Methods block for checking if the values of text fields have changed */
	private void isTextValueChanged(Control composite, String[][] storedValues) {
		
		/* Checking CHANNEL group if the Text fields values changed */
		Group checkedGroup = (Group) ((Composite) composite).getChildren()[0];
		List<String> changedFieldsNames = getModifiedTextsNames(getTextsAndLabelsMap(checkedGroup), storedValues[0]);
		
		if(changedFieldsNames != null && !changedFieldsNames.isEmpty()) {
			setValuesToStorage(changedFieldsNames, CredentialsTypes.CHANNEL);
			messageBuilder.setStringToNotifierMessage(messageBuilder.getStringSeparator(50) + "\n***  " + checkedGroup.getText() + "  ***\n");
			addFieldsNamesToNotifierMessage(changedFieldsNames);
		}
		
		/* Checking APPLICATION group if the Text fields values changed */
		checkedGroup = (Group) ((Composite) composite).getChildren()[2];
		changedFieldsNames = getModifiedTextsNames(getTextsAndLabelsMap(checkedGroup), storedValues[1]);
		
		if(changedFieldsNames != null && !changedFieldsNames.isEmpty()) {
			setValuesToStorage(changedFieldsNames, CredentialsTypes.APPLICATION);
			messageBuilder.setStringToNotifierMessage(messageBuilder.getStringSeparator(50) + "\n***  " + checkedGroup.getText() + "  ***\n");
			addFieldsNamesToNotifierMessage(changedFieldsNames);
		}
	}
	
	

	/* Create a map of matching Text fields and its Labels */
	private LinkedHashMap<Text, Label> getTextsAndLabelsMap(Group checkedGroup) {

		LinkedHashMap<Text, Label> textsAndLabelsMatchStore = new LinkedHashMap<Text, Label>(2);

		textsAndLabelsMatchStore.put((Text) checkedGroup.getChildren()[1], (Label) checkedGroup.getChildren()[0]);
		textsAndLabelsMatchStore.put((Text) checkedGroup.getChildren()[3], (Label) checkedGroup.getChildren()[2]);
		
		return textsAndLabelsMatchStore;
	}
	
	
	
	/* Adding the modified fields names to Notifier Message */
	private void addFieldsNamesToNotifierMessage(List<String> modifiedExpandsNames) {
		for (String fieldName : modifiedExpandsNames) {
			messageBuilder.setStringToNotifierMessage(fieldName);
		}
	}
	

	/* Create and return the List which contains names of the modified fields */
	private List<String> getModifiedTextsNames(LinkedHashMap<Text, Label> textsAndLabelsMatchStore, String[] etalonValues) {
		List<String> changedFieldsNames = new ArrayList<>(2);

		for(int i = 0; i < etalonValues.length; i++) {			
			if(!etalonValues[i].equals(((Text) textsAndLabelsMatchStore.keySet().toArray()[i]).getText())) {		
				changedFieldsNames.add("\t" + ((Label) textsAndLabelsMatchStore.values().toArray()[i]).getText() + "\n");
			}
		}
		
		return changedFieldsNames;
	}
	

	/* Check if the List contains Labels names of the modified fields and update proper values in credentialsStore */
	private void setValuesToStorage(List<String> changedFieldsNames, CredentialsTypes credentials) {
		
		for (String labelName : changedFieldsNames) {
			
			switch (credentials) {
			case CHANNEL:
				
				if (labelName.trim().toLowerCase().equals(labelKanalLogin.getText().trim().toLowerCase())) 
					credentialsStore.setLoginKanal(textKanalLogin.getText());
				
				
				if (labelName.trim().toLowerCase().equals(labelKanalPassword.getText().trim().toLowerCase())) {
					credentialsStore.setPasswordKanalOld(credentialsStore.getPasswordKanal());
					credentialsStore.setPasswordKanal(textKanalPassword.getText());
					credentialsStore.setDateSetKanal(Instant.now().getEpochSecond());
				}

				break;
				
			case APPLICATION:
				if (labelName.trim().toLowerCase().equals(labelPriklLogin.getText().trim().toLowerCase()))
					credentialsStore.setLoginPrikl(textPriklLogin.getText());
				
				if (labelName.trim().toLowerCase().equals(labelPriklPassword.getText().trim().toLowerCase())) {
					credentialsStore.setPasswordPriklOld(credentialsStore.getPasswordPrikl());
					credentialsStore.setPasswordPrikl(textPriklPassword.getText());
					credentialsStore.setDateSetPrikl(Instant.now().getEpochSecond());
				}

				break;
			}
		}
	}
	
	
	private void writeVpnConfigFile(Control groupVPN) {
		
			String selectedServerIP = (boolean) ((Button) ((Group) groupVPN).getChildren()[1]).getSelection() ? getVPNServersIP(0) : getVPNServersIP(1);
			
			setVPNRadioState(credentialsSwitch,
					new boolean [] {(boolean) ((Button) ((Group) groupVPN).getChildren()[1]).getSelection(),
									(boolean) ((Button) ((Group) groupVPN).getChildren()[2]).getSelection()});
			
			credentialsDAO.updateVPNRadioButtonsState(credentialsStore);
			
			String vpnCircuit = credentialsSwitch.getEnumByString(credentialsSwitch.getTitle());
			String filePath = appEnv.getApplicationRootDirectory() + File.separator + VpnFileEnvironment.VPN_DIRECTORY.getDirectory() + File.separator + VpnFileEnvironment.VPN_FILENAME.getFilename(vpnCircuit); 
			String message = "connect " + selectedServerIP + "\ny\n"+ getVPNCircuit(credentialsSwitch) +"\n" + textKanalLogin.getText() + "\n" + textKanalPassword.getText() + "\ny\nexit";
			
			/*	Write VPN config file */
			new TshKbrFileWriter(display, filePath, message, "UTF-8", false, true);
			
			if (messages.showMessage(
					"Файл с настройками для подключения к VPN создан успешно!\n\n"
					+ filePath
					+ "\n\nОткрыть каталог хранения файла?", MessageTypes.QUESTION) == SWT.YES) {

				processLuncher.runProcess(appEnv.getApplicationRootDirectory() + File.separator + VpnFileEnvironment.VPN_DIRECTORY.getDirectory());

			}
	}
	
	
	/* 
	 * Get type of the selected circuit for writing to the VPN config file:
	 * IF PROM circuit THEN "0"
	 * IF TEST circuit THEN "1"
	 */
	private String getVPNCircuit (CredentialsSwitches credentialsSwitch) {
		return (credentialsSwitch == CredentialsSwitches.PSBR_PROM || credentialsSwitch == CredentialsSwitches.SPFS_PROM) ? "0" : "1";
	}
	
	
	/* Set Array which contains VPN servers IP */
	private void setVpnServersIP(String [] vpnIP) {
		vpnServersIP = new String[vpnIP.length];
		System.arraycopy(vpnIP, 0, vpnServersIP, 0, vpnIP.length);
		
	}
	
	/* Get Array which contains VPN servers IP */
	public String getVPNServersIP(int index) {
		
		String[] vpnServersIPCopy = new String[vpnServersIP.length];
		System.arraycopy(vpnServersIP, 0, vpnServersIPCopy, 0, vpnServersIPCopy.length);
		
		return vpnServersIPCopy[index];
	}

	
	private GeneratorSettings getUpdatedPasswordGenerator() {		
		return new GeneratorSettings(appEnv);
	}

	
	
	
	/* Get milliseconds and return formatted date like a dd.MM.yyyy */
	private String convertMillisecondsToDate(long unixSeconds) {
		
		// Convert seconds to milliseconds
		Date date = new Date(unixSeconds * 1000L); 
		
		// Formatting date
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
		
	    //Get Calendar instance
	    Calendar now = Calendar.getInstance();
	    
	    //Get current TimeZone using getTimeZone method of Calendar class
	    TimeZone timeZone = now.getTimeZone();
	    
		//Give a timeZone reference for formatting
		dateFormat.setTimeZone(timeZone);
		
		//Return formatted date
		return new String (dateFormat.format(date));
	}
	

	
	
	/* Invoke setter or getter methods when checkbox Button is pressed */
	private void checkBoxActions(Button checkBoxButton, ModesOfOperation modeType) {

		for (Map.Entry<String, Method[]> entry : getObjectMethodsMap().entrySet()) {

			try {
				switch (modeType) {
				case GET_MODE:
					// Get boolean value of the checkbox Button state from the credentialsStore object
					checkBoxButton.setSelection((boolean) entry.getValue()[0].invoke(credentialsStore, (Object[]) null));
					break;
				case SET_MODE:
					// Set boolean value of the checkbox Button state to the credentialsStore object
					entry.getValue()[1].invoke(credentialsStore, checkBoxButton.getSelection());
					break;
				}

			} catch (Exception e1) {

				errorWriter.printErrorToFile(e1);
				messages.showMessage(
						"\tОшибка обновления данных!\n\nСмотри детали ошибки в лог файле:\n\n"
								+ errorWriter.getErrorFileName() + "\n\nТекст ошибки:\n\n" + e1.getMessage(),
						MessageTypes.ERROR);
				return;
			}

			break;
		}

	}

	
	/* Get array of the boolean values for setting VPN server radio buttons state */ 
	private boolean [] getVPNRadioState(CredentialsSwitches credentialsSwitch) {

		boolean [] radioState = new boolean[2];
			
		if(credentialsSwitch == CredentialsSwitches.PSBR_PROM || credentialsSwitch == CredentialsSwitches.SPFS_PROM) {
			radioState[0] = credentialsStore.getMainVpnIpPromState();
			radioState[1] = credentialsStore.getRezervVpnIpPromState();
		} else {
			radioState[0] = credentialsStore.getMainVpnIpPromState();
			radioState[1] = credentialsStore.getRezervVpnIpPromState();
		}

		return radioState;
	}
	
	
	/* Get array of the boolean values for setting VPN server radio buttons state */ 
	private void setVPNRadioState(CredentialsSwitches credentialsSwitch, boolean [] radioButtonsState) {
		
		if (radioButtonsState == null) return;
		
		if(credentialsSwitch == CredentialsSwitches.PSBR_PROM || credentialsSwitch == CredentialsSwitches.SPFS_PROM) {
			credentialsStore.setMainVpnIpPromState(radioButtonsState[0]);
			credentialsStore.setRezervVpnIpPromState(radioButtonsState[1]);
		} else {
			credentialsStore.setMainVpnIpTestState(radioButtonsState[0]);
			credentialsStore.setRezervVpnIpTestState(radioButtonsState[1]);
		}
	}
	

	
	private void setObjectMethodsMap(LinkedHashMap<String, Method[]> objectMethods) {
		this.objectMethods = objectMethods;
	}
	
	private LinkedHashMap<String, Method[]> getObjectMethodsMap() {
		return new LinkedHashMap<String, Method[]>(objectMethods);
	}
	
	/* Dispose resources on closing the window */
	public void closeResources() {
		if (shell != null) shell.dispose();
	}
}
