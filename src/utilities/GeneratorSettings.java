package utilities;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Spinner;

import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.widgets.Text;

import daoPattern.SettingsDAO;
import daoPattern.SettingsStore;
import enumsStore.ButtonsSize;
import enumsStore.MessageTypes;
import startWindow.ApplicationEnvironments;

/**
 * This is a class for generating a new password, copying it to the clipboard, and setting the length of the password.
 * <p>
 * @author Aleksander Demichev
 * @version 1.0.0.0
 */

public final class GeneratorSettings {

	private Display display;
	private Shell shell;
	private Image windowImage;
	
	private SettingsDAO settingsDAO;
	private SettingsStore settingsStore;
	private MessageDialog messages;
	private CreateErrorLog errorWriter;

	private boolean isPasswordUpdated = false;
	
	public GeneratorSettings(ApplicationEnvironments appEnv) {
		this.display = appEnv.getDisplay();
		this.settingsDAO = appEnv.getSettingsDAO();
		this.settingsStore = appEnv.getSettingsStore();
		this.windowImage = appEnv.getWindowIcon();
		this.messages = appEnv.getMessages();
		this.errorWriter = appEnv.getErrorWriter();
	}
	




	/**
	 * Open the window.
	 * @wbp.parser.entryPoint
	 */
	public void open() {
	
		shell = new Shell(display, SWT.BORDER | SWT.APPLICATION_MODAL | SWT.CLOSE);
		if (windowImage != null) shell.setImage(windowImage);
		shell.setText("Генератор пароля");
		shell.setSize(294, 280);
		
		Composite composite = new Composite(this.shell, SWT.NONE);
		composite.setBounds(0, 0, 277, 235);
		
		Group group = new Group(composite, SWT.NONE);
		group.setText("Настройка генерации пароля");
		group.setBounds(10, 131, 256, 60);
		
		Label label = new Label(group, SWT.NONE);
		label.setBounds(52, 29, 93, 15);
		label.setText("Длина пароля: ");
		
		Spinner spinner = new Spinner(group, SWT.BORDER | SWT.READ_ONLY);
		spinner.setToolTipText("Укажите длину пароля\nМинимум 12 символов\nМаксимум 20 символов");
		spinner.setTextLimit(20);
		spinner.setBounds(151, 26, 47, 22);
		spinner.setValues(settingsStore.getPasswordLength(), 12, 20, 0, 1, 10);
		
		spinner.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				settingsStore.setPasswordLength(Integer.parseInt(spinner.getText()));
				setPasswordUpdatedState(true);
			}
		});
	
		
		Group group_1 = new Group(composite, SWT.NONE);
		group_1.setText("Сгенерировать пароль");
		group_1.setBounds(10, 10, 256, 115);
		
		Button buttonGeneratePassword = new Button(group_1, SWT.NONE);
		buttonGeneratePassword.setBounds(55, 31, 145, 25);
		buttonGeneratePassword.setText("Сгенерировать пароль");
		
		Label labelMessageInfo = new Label(group_1, SWT.NONE);
		//Dark grey color
		labelMessageInfo.setForeground(new Color(this.display, 102, 102, 102));
		labelMessageInfo.setAlignment(SWT.CENTER);
		labelMessageInfo.setBounds(6, 89, 243, 15);
		
		Text textShowPassword = new Text(group_1, SWT.READ_ONLY | SWT.CENTER);
		textShowPassword.setForeground(new Color(this.display, 102, 102, 102));
		textShowPassword.setText("Нажмите кнопку \"Сгенерировать пароль\"");
		textShowPassword.setBounds(6, 62, 243, 21);
		
		Button button = new Button(composite, SWT.NONE);
		
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				if(getPasswordUpdatedState()) {
					settingsDAO.updatePasswordLenght(spinner.getText());
				}
				
				closeResources();
			}
		});
		
		button.setBounds(101, 197, ButtonsSize.BUTTON_WIDTH.size(), ButtonsSize.BUTTON_HEIGHT.size());
		button.setText("Закрыть");
		
		buttonGeneratePassword.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//Black color
				textShowPassword.setForeground(new Color(display, 0, 0, 0));
				textShowPassword.setText(getPassword());
				try {
					copyToClipboard(textShowPassword.getText());
				} catch (IllegalStateException ex) {
					errorWriter.printErrorToFile(ex);
					messages.showMessage(
									"Не удалось скопировать пароль в буфер обмена!.\nСмотри детали ошибки в лог файле:\n\n"
									+ errorWriter.getErrorFileName()
									+ "\n\nТекст ошибки:\n\n" + ex.getMessage(),
									MessageTypes.ERROR);
				}
				
				labelMessageInfo.setText("Пароль скопирован в буфер обмена");
			}
		});

		Monitor primary = display.getPrimaryMonitor();
		Rectangle bounds = primary.getBounds();
		Rectangle rect = shell.getBounds();
		int x = bounds.x + (bounds.width - rect.width) / 2;
		int y = bounds.y + (bounds.height - rect.height) / 2;
		shell.setLocation(x, y);
		
		shell.addShellListener(new ShellAdapter() {
			@Override
			public void shellClosed(ShellEvent e) {
				closeResources();
			}
		});

		shell.open();
		shell.layout();
		

	}
	


	/* Generate and return random password string */
	public String getPassword() {
		PasswordGenerator passwordGenerator = new PasswordGenerator.PasswordGeneratorBuilder()
		        .useDigits(true)
		        .useLower(true)
		        .useUpper(true)
		        .usePunctuation(true)
		        .setPasswordLength(settingsStore.getPasswordLength())
		        .build();
	
		return new String(passwordGenerator.generate());
	}
	
	

	
	/* Get the String value and write it to System OS Clip board */
	private void copyToClipboard(String textToCopy) throws IllegalStateException {
		StringSelection stringSelection = new StringSelection(textToCopy);
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(stringSelection, null);
	}
	
	/* Setter and Getter of password state field */
	private void setPasswordUpdatedState(boolean passwordState) {
		this.isPasswordUpdated = passwordState;
	}
	
	private boolean getPasswordUpdatedState() {
		return this.isPasswordUpdated;
	}
	
	private void closeResources() {
		if (this.shell != null) this.shell.dispose();
	}
	
}
