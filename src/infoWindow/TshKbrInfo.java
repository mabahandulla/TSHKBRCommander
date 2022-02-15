package infoWindow;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.swt.widgets.Text;

import daoPattern.InformationDAO;
import daoPattern.InformationStore;
import enumsStore.ButtonsSize;
import enumsStore.CircuitTypes;
import enumsStore.MessageTypes;
import enumsStore.ModeSwitches;
import enumsStore.ModesOfOperation;
import startWindow.ApplicationEnvironments;
import utilities.CreateErrorLog;
import utilities.MessageBuilder;
import utilities.MessageDialog;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.ExpandAdapter;
import org.eclipse.swt.events.ExpandEvent;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.MouseTrackAdapter;




/**
 * This is a Class for displaying and editing reference information about TSH KBR retrieved from the database.
 * <p>
 * @author Aleksander Demichev
 * @version 1.0.0.0
 */

public final class TshKbrInfo {

	private Display display;
	private Shell shell;
	private Image windowImage;
	private Button closeAllExpandsButton;
	private ExpandBar expandBar;
	
	/* DAO for get / set Information values from / to database */
	private InformationDAO informationDAO;
	
	/* The values storage */
	private InformationStore informationStore;
	
	/* Cloned informationStore object for storing values changed in the application. */
	private InformationStore temporaryStore;
	
	/* Instance for showing messages to user */
	private MessageDialog messages;
	
	/* Instance for writing errors to error.log */
	private CreateErrorLog errorWriter;
	
	/* Instance for building messages for user */
	private MessageBuilder messageBuilder;
	
	/* Set Expand Items State. When window opens, it`s set to false. */	
	private boolean expandsState = false;

	/* Set count of Expanded Items. By default, all Expands set to expanded. */ 
	private int expandedItemsCount = 5;

	/* List for storing modified fields of the InformationStore object */
	private List<Field> changedFields;
	

	/* 
	 * Enum for storing state of the selected Radio Button.
	 * By default, when application starts setting for CircuitTypes.PROM.
	 */
	private CircuitTypes selectedCircuit = CircuitTypes.PROM;
	
	
	
	public TshKbrInfo(ApplicationEnvironments applicationEnvironments) {
		this.display = applicationEnvironments.getDisplay();
		this.windowImage = applicationEnvironments.getWindowIcon();
		this.informationDAO = applicationEnvironments.getInformationDAO();
		this.informationStore = applicationEnvironments.getInformationStore();
		this.temporaryStore = applicationEnvironments.getInformationStore();
		this.messages = applicationEnvironments.getMessages();
		this.errorWriter = applicationEnvironments.getErrorWriter();
		this.messageBuilder = applicationEnvironments.getMessageBuilder();
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
		shell.setSize(308, 490);
		shell.setText("ТШ КБР Commander - Информация");
		
		
		Composite composite = new Composite(shell, SWT.NONE);
		composite.setBounds(10, 5, 270, 445);
		composite.setLayout(null);
		
		
		/*
		 * Group that contains Radio Buttons
		 * 
		 * ************************************************
		 * **** RADIO BUTTONS CONTAINER
		 * ************************************************
		 */
		
		Group selectGroup = new Group(composite, SWT.NONE);
		selectGroup.setText("Выбор контура");
		selectGroup.setBounds(9, 10, 252, 78);
		
		Button selectPromRadioButton = new Button(selectGroup, SWT.RADIO);
		selectPromRadioButton.setSelection(true);
		selectPromRadioButton.setBounds(10, 26, 222, 16);
		selectPromRadioButton.setText(CircuitTypes.PROM.getLowerCaseName());
		
		Button selectTestRadioButton = new Button(selectGroup, SWT.RADIO);
		selectTestRadioButton.setBounds(10, 48, 222, 16);
		selectTestRadioButton.setText(CircuitTypes.TEST.getLowerCaseName());
		
		/*
		 * Group that contains Expands Items and Text Items
		 * 
		 * **************************************************
		 * **** EXPANDS AND TEXT CONTAINER
		 * **************************************************
		 */
		
		Group informationGroup = new Group(composite, SWT.NONE);
		informationGroup.setBounds(9, 94, 252, 311);
		informationGroup.setText(selectPromRadioButton.getText());
		informationGroup.setLayout(null);
		
		expandBar = new ExpandBar(informationGroup, SWT.BORDER | SWT.V_SCROLL);
		expandBar.setToolTipText("");
		expandBar.setBounds(7, 20, 237, 253);
		
		ExpandItem urlSendExpand = new ExpandItem(expandBar, SWT.NONE);
		urlSendExpand.setExpanded(true);
		urlSendExpand.setText("URL отправки сообщений");
		
		Text urlSendText = new Text(expandBar, SWT.BORDER | SWT.READ_ONLY);
		urlSendText.setTextLimit(50);
		urlSendExpand.setControl(urlSendText);
		urlSendExpand.setHeight(urlSendExpand.getControl().computeSize(SWT.DEFAULT, SWT.DEFAULT).y);

		ExpandItem urlReciveExpand = new ExpandItem(expandBar, SWT.NONE);
		urlReciveExpand.setExpanded(true);
		urlReciveExpand.setText("URL приема сообщений");		
		
		Text urlReciveText = new Text(expandBar, SWT.BORDER | SWT.READ_ONLY);
		urlReciveText.setTextLimit(50);
		urlReciveExpand.setControl(urlReciveText);
		urlReciveExpand.setHeight(urlReciveExpand.getControl().computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
		
		ExpandItem mainIPExpand = new ExpandItem(expandBar, SWT.NONE);
		mainIPExpand.setExpanded(true);
		mainIPExpand.setText("Основной сервер доступа к ТШ КБР");
		
		Text mainIPText = new Text(expandBar, SWT.BORDER | SWT.READ_ONLY);
		mainIPText.setTextLimit(20);
		mainIPExpand.setControl(mainIPText);
		mainIPExpand.setHeight(mainIPExpand.getControl().computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
		
		ExpandItem backupIPExpand = new ExpandItem(expandBar, SWT.NONE);
		backupIPExpand.setExpanded(true);
		backupIPExpand.setText("Резервный сервер доступа к ТШ КБР");
		
		Text backupIPText = new Text(expandBar, SWT.BORDER | SWT.READ_ONLY);
		backupIPText.setTextLimit(20);
		backupIPExpand.setControl(backupIPText);
		backupIPExpand.setHeight(backupIPExpand.getControl().computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
		
		ExpandItem urlLKExpand = new ExpandItem(expandBar, SWT.NONE);
		urlLKExpand.setExpanded(true);
		urlLKExpand.setText("Вход в личный кабинет ТШ КБР");
		
		Text urlLKText = new Text(expandBar, SWT.BORDER | SWT.READ_ONLY);
		urlLKText.setTextLimit(50);		
		urlLKExpand.setControl(urlLKText);
		urlLKExpand.setHeight(urlLKExpand.getControl().computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
		
		this.closeAllExpandsButton = new Button(informationGroup, SWT.NONE);
		this.closeAllExpandsButton.setBounds(79, 279, 93, 25);
		this.closeAllExpandsButton.setText("Свернуть");
		
		
		/* Add all Text fields to Array */
		Text[] textFieldsArray = { 
				urlSendText,
				urlReciveText,
				mainIPText,
				backupIPText,
				urlLKText
		};
		
		
		
		
		Button buttonOK = new Button(composite, SWT.CENTER);	
		buttonOK.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				
				switch (TshKbrInfo.this.getSelectedCircuit()) {
				case PROM:
					//Update PROM values in temporaryStore storage
					workingWithTheStoreOfValues(textFieldsArray, ModesOfOperation.SET_MODE, ModeSwitches.SET_PROM);
					break;
				case TEST:
					//Update TEST values in temporaryStore storage
					workingWithTheStoreOfValues(textFieldsArray, ModesOfOperation.SET_MODE, ModeSwitches.SET_TEST);
					break;
				}
						
				
				try {
					
					/* Get a list of fields whose values have changed and save them to the List object */
					setChangedFieldsList(getChangedFields(informationStore, temporaryStore));
					
					if(getChangedFieldsList() != null && !getChangedFieldsList().isEmpty()) {
						
						getChangedFiledsNames(expandBar.getItems());

						messageBuilder.setNotifierMessageType(MessageTypes.WARNING_QUESTION);

						if (messages.showMessage(
										"Следующие поля изменены:\n\n"
										+ messageBuilder.getNotifierMessage().toString()
										+ messageBuilder.getStringSeparator(50)
										+ "\n\nСохранить изменения?", 
										messageBuilder.getNotifierMessageType()) == SWT.YES) {

							informationDAO.updateData(temporaryStore);

						}
					}

				} catch (Exception e1) {
					errorWriter.printErrorToFile(e1);
					messages.showMessage(
									"\tОшибка при сохранения данных!\n\nСмотри детали ошибки в лог файле:\n\n"
									+ errorWriter.getErrorFileName()
									+ "\n\nТекст ошибки:\n\n"
									+ e1.getMessage(), 
									MessageTypes.ERROR);
				} finally {
					messageBuilder.clearNotifierMessage();
					closeResources();
				}
			}
		});
		
		buttonOK.setText("OK");
		buttonOK.setBounds(53, 411, ButtonsSize.BUTTON_WIDTH.size(), ButtonsSize.BUTTON_HEIGHT.size());
		
		Button buttonCancel = new Button(composite, SWT.CENTER);
		buttonCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				closeResources();
			}
		});
		buttonCancel.setText("Отмена");
		buttonCancel.setBounds(148, 411, ButtonsSize.BUTTON_WIDTH.size(), ButtonsSize.BUTTON_HEIGHT.size());
		


		

		
		/*
		 * Iterate thru "textFieldsArray" and add event listeners to all Text Items:
		 * double click and focus lost events
		 */
		for (Text textField : textFieldsArray) {
			textField.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseDoubleClick(MouseEvent e) {
					textField.setEditable(true);
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
		
		

		
		
		/* By default when open window, get PROM values */
		workingWithTheStoreOfValues(textFieldsArray, ModesOfOperation.GET_MODE, ModeSwitches.GET_PROM);

		/* Adding Selection Listeners for Radio Buttons */
		for (Control radioButton : selectGroup.getChildren()) {

			((Button) radioButton).addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					boolean isSelected = ((Button) e.getSource()).getSelection();
					
					if(e.getSource().equals(selectPromRadioButton) && isSelected) {
						
						if(selectedCircuit == CircuitTypes.PROM) return;
						
						selectedCircuit = CircuitTypes.PROM;
						
						informationGroup.setText(CircuitTypes.PROM.getLowerCaseName());
							
						//Update TEST values in temporaryStore storage
						workingWithTheStoreOfValues(textFieldsArray, ModesOfOperation.SET_MODE, ModeSwitches.SET_TEST);
						//Get PROM Text values from temporaryStore storage
						workingWithTheStoreOfValues(textFieldsArray, ModesOfOperation.GET_MODE, ModeSwitches.GET_PROM);

						setDefaultExpandsState(expandBar.getItems());

					} else if(e.getSource().equals(selectTestRadioButton) && isSelected) {
						
						if(selectedCircuit == CircuitTypes.TEST) return;
						
						selectedCircuit = CircuitTypes.TEST;
						
						informationGroup.setText(CircuitTypes.TEST.getLowerCaseName());
						
						//Update PROM values in temporaryStore storage
						workingWithTheStoreOfValues(textFieldsArray, ModesOfOperation.SET_MODE, ModeSwitches.SET_PROM);
						//Get TEST Text values from temporaryStore storage
						workingWithTheStoreOfValues(textFieldsArray, ModesOfOperation.GET_MODE, ModeSwitches.GET_TEST);

						setDefaultExpandsState(expandBar.getItems());
					}
				}
			});			
		}
		
	
		
		/* Add event listener to collapse or expand all Expand Items when Button is clicked */
		closeAllExpandsButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {				
				for (ExpandItem expandItem : expandBar.getItems()) {
					expandItem.setExpanded(getExpandsState());
				}
				
				toggleExpandItemsState(closeAllExpandsButton);
			}
		});
	

		

		/* Add event listener to Expand Bar, when child Expand Items collapsed or expanded */
		expandBar.addExpandListener(new ExpandAdapter() {
			@Override
			public void itemExpanded(ExpandEvent e) {
				updateCountExpanded(true);
				updateButtonText(closeAllExpandsButton);
			}
			
			@Override
			public void itemCollapsed(ExpandEvent e) {
				updateCountExpanded(false);
				updateButtonText(closeAllExpandsButton);
			}
		});
		

		
		Monitor primary = display.getPrimaryMonitor();
		Rectangle bounds = primary.getBounds();
		Rectangle rect = shell.getBounds();
		int x = bounds.x + (bounds.width - rect.width) / 2;
		int y = bounds.y + (bounds.height - rect.height) / 2;
		shell.setLocation(x, y);
		shell.setLayout(null);

		shell.open();
		shell.layout();
	}

	
	
	
	
	
	
	/* Get Array of Information class methods and invoke them for getting or setting Text values */
	private void workingWithTheStoreOfValues(Text[] textFieldsArray,
											 ModesOfOperation modeType,
											 ModeSwitches modeSwitch) {
		
		try {

			Method[] objectMethods = temporaryStore.getInformationMethods(modeSwitch);

			for (int i = 0; i < textFieldsArray.length; i++) {

				switch (modeType) {
				case GET_MODE:
					// Get values from temporaryStore object
					textFieldsArray[i].setText((String) objectMethods[i].invoke(temporaryStore, (Object[]) null));
					break;
				case SET_MODE:
					// Set values to temporaryStore object
					objectMethods[i].invoke(temporaryStore, new String(textFieldsArray[i].getText()));
					break;
				}
			}

		} catch (Exception e1) {
			errorWriter.printErrorToFile(e1);
			messages.showMessage(
					"\tОшибка получения данных!\n\nСмотри детали ошибки в лог файле:\n\n"
							+ errorWriter.getErrorFileName() + "\n\nТекст ошибки:\n\n" + e1.getMessage(),
					MessageTypes.ERROR);
			
			closeResources();
		}
	}
	


	
	
	
	
	/*
	 * *********************************************************
	 * **** START OF THE CHEKING MODIFIED FIELDS BLOCK
	 * *********************************************************
	 */
	
	/* Compare two objects and find changed fields */
	private List<Field> getChangedFields(Object originalStore, Object modifiedStore) throws IllegalAccessException {
	     
		 List<Field> changedFields = new ArrayList<>(10);
		 
	     for (Field field : originalStore.getClass().getDeclaredFields()) {

	        field.setAccessible(true);
	        
	        Object originalValue = field.get(originalStore);
	        Object modifiedValue = field.get(modifiedStore); 
	        
	        if (originalValue != null && modifiedValue != null && !Objects.equals(originalValue, modifiedValue)) {
	            changedFields.add(field);
	        }
	    }
	     
	    return changedFields;
	 }
	
	
	 
	/* Getting names of the modified fields */	 
	private void getChangedFiledsNames (ExpandItem[] allExpandBars) throws IllegalAccessException, NoSuchFieldException, SecurityException {

		List<String> modifiedExpandsNames = getModifiedExpandsNames(getFieldsAndExpandsMap(informationStore.getInformationFields(CircuitTypes.PROM), allExpandBars));
		
		if(modifiedExpandsNames != null && !modifiedExpandsNames.isEmpty()) {
			messageBuilder.setStringToNotifierMessage(messageBuilder.getStringSeparator(50) + "\n***  " + getCircuitName(CircuitTypes.PROM) + "  ***\n\n");
			addFieldsNamesToNotifierMessage(modifiedExpandsNames);
		}
			
		modifiedExpandsNames = getModifiedExpandsNames(getFieldsAndExpandsMap(informationStore.getInformationFields(CircuitTypes.TEST), allExpandBars));

		if(modifiedExpandsNames != null && !modifiedExpandsNames.isEmpty()) {
			messageBuilder.setStringToNotifierMessage(messageBuilder.getStringSeparator(50) + "\n***  " + getCircuitName(CircuitTypes.TEST) + "  ***\n\n");
			addFieldsNamesToNotifierMessage(modifiedExpandsNames);
		}
	}
	
	/* Adding the modified fields names to Notifier Message */
	private void addFieldsNamesToNotifierMessage(List<String> modifiedExpandsNames) {
		for (String fieldName : modifiedExpandsNames) {
			messageBuilder.setStringToNotifierMessage(fieldName);
		}
	}
	
	/* Create and return List which contains names of the modified fields */
	private List<String> getModifiedExpandsNames(LinkedHashMap<Field, String> fieldsAndExpandsMatchStore) {
		List<String> changedFieldsNames = new ArrayList<>(5);
		for (Map.Entry<Field, String> entry : fieldsAndExpandsMatchStore.entrySet()) {
			for (Field changedField : getChangedFieldsList()) {
				if (changedField.equals(entry.getKey())) {
					changedFieldsNames.add(entry.getValue() + "\n");
					break;
				}
			}
		}
		
		return changedFieldsNames;
	}
	

	/* Create a map of matching fields and Expands Names */
	private LinkedHashMap<Field, String> getFieldsAndExpandsMap(Field[] circuitFields, ExpandItem[] allExpandBars) throws NoSuchFieldException, SecurityException {
		LinkedHashMap<Field, String> fieldsAndExpandsMatchStore = new LinkedHashMap<Field, String>(5);
		
		for(int i = 0; i < circuitFields.length; i++) {
			fieldsAndExpandsMatchStore.put(circuitFields[i], allExpandBars[i].getText());
		}
		
		return fieldsAndExpandsMatchStore;
	}
	
	

	/* Get name of changed circuit */
	private String getCircuitName(CircuitTypes circuitType) {
		return circuitType == CircuitTypes.PROM ? CircuitTypes.PROM.getUpperCaseName() : CircuitTypes.TEST.getUpperCaseName();
	}
	
	
	/* Get state of the selected circuit*/
	private CircuitTypes getSelectedCircuit() {
		return this.selectedCircuit;
	}
	
	/*
	 * *********************************************************
	 * **** END OF THE CHEKING MODIFIED FIELDS BLOCK
	 * *********************************************************
	 */
	
	
	
	
	
	
	
	
	
	
	
	/*
	 * *********************************************************
	 * **** START EXPANDS CONTROL BLOCK
	 * *********************************************************
	 */
	
	/* Change Text on Button when Expand Item change state */	 
	private void updateButtonText(Button pressedButton) {
		if(getExpandsCount() > 0 && getExpandsCount() < 5) {
			pressedButton.setText("Развернуть");
			setExpandsState(true);
		} else if (getExpandsCount() == 5){ 
			pressedButton.setText("Свернуть");
			setExpandsState(false);
		}
	}
	
	/* Toggle the state of the Expand Items when Collapse/Expand All Expands Items Button pressed and set the Button Text */  
	private void toggleExpandItemsState(Button pressedButton) {
		setExpandsState(!getExpandsState());
		if(getExpandsState()) {
			setExpandsCount(0);
			pressedButton.setText("Развернуть");
		} else {
			setExpandsCount(5);
			pressedButton.setText("Свернуть");
		}
	}	
	
	/* When Expand Item expanded or collapsed, update count of expanded */
	private void updateCountExpanded(boolean operation) {		
		if(operation && getExpandsCount() < 5) {
			setExpandsCount(getExpandsCount() + 1);
		} else if (!operation && getExpandsCount() > 0) {
			setExpandsCount(getExpandsCount() - 1);
		}
	}

	/* Set default state for expands items */
	private void setDefaultExpandsState(ExpandItem[] allExpandBarsStore) {
		
		if(getExpandsCount() < 5) {
			for (ExpandItem expandItem : allExpandBarsStore) {
				expandItem.setExpanded(true);
			}
			
			closeAllExpandsButton.setText("Свернуть");
			setExpandsState(false);
			setExpandsCount(5);
		}
	}
	
	/* Setters and Getters to manipulate Expands state */

	private void setExpandsState(boolean state) {
		this.expandsState = this.isBooleanNotNull(state);
	}
	
	private boolean getExpandsState() {
		return this.expandsState;
	}
	
	private void setExpandsCount(int count) {
		this.expandedItemsCount = count;
	}
	
	private int getExpandsCount() {
		return this.expandedItemsCount;
	}
	
	private void setChangedFieldsList(List<Field> changedFieldsList) {
		this.changedFields = changedFieldsList;
	}
	
	private List<Field> getChangedFieldsList() {
		return new ArrayList<Field>(this.changedFields);
	}
	
	/*
	 * Check if Boolean value not null
	 */
	private Boolean isBooleanNotNull(Boolean checkedValue) {
		return checkedValue.equals(null) ? false : checkedValue;
	}
	
	/*
	 * *********************************************************
	 * **** END EXPANDS CONTROL BLOCK
	 * *********************************************************
	 */

	

	
	/* Dispose resources on closing the window */
	private void closeResources() {
		if (shell != null) shell.dispose();
	}
	
}
