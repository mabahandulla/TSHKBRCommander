package utilities;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Composite;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Text;

import daoPattern.LoginDAO;
import daoPattern.LoginStore;
import enumsStore.ButtonsSize;
import enumsStore.MessageTypes;
import startWindow.ApplicationEnvironments;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;

/**
 * This is the class for changing the admin password to login to the application.
 * <p>
 * @author Aleksander Demichev
 * @version 1.0.0.0
 */

public final class PasswordChanger {

	private Display display;
	private Shell shell;
	private Image windowImage;
	private MessageDialog messages;
	private LoginDAO loginDAO;
	private LoginStore loginStore;
	
	
	
	public PasswordChanger(ApplicationEnvironments appEnv) {
		this.display = appEnv.getDisplay();
		this.windowImage = appEnv.getWindowIcon();
		this.messages = appEnv.getMessages();
		this.loginDAO = appEnv.getLoginDAO();
		this.loginStore = this.loginDAO.getData(1);
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
		
		shell.setToolTipText("Изменить мастер пароль");
		shell.setSize(350, 245);
		shell.setText("Изменить мастер пароль");
		
		Composite composite = new Composite(shell, SWT.NONE);
		composite.setBounds(0, 0, 334, 206);
		
		Group group = new Group(composite, SWT.NONE);
		group.setText("Смена пароля для входа в программу");
		group.setBounds(10, 10, 314, 184);
		
		Label oldPasswordLabel = new Label(group, SWT.NONE);
		oldPasswordLabel.setBounds(47, 34, 100, 15);
		oldPasswordLabel.setText("Текущий пароль");
		
		Text oldPasswordText = new Text(group, SWT.BORDER | SWT.PASSWORD);
		oldPasswordText.setTextLimit(20);
		oldPasswordText.setToolTipText("Текущий пароль.\nУже подставлен в поле из БД");
		oldPasswordText.setBounds(153, 31, 151, 21);
		oldPasswordText.setText(loginStore.getPassword());

		
		Label newPasswordLabel = new Label(group, SWT.NONE);
		newPasswordLabel.setBounds(58, 69, 89, 15);
		newPasswordLabel.setText("Новый пароль");
		
		Text newPasswordText = new Text(group, SWT.BORDER | SWT.PASSWORD);
		newPasswordText.setToolTipText("Новый пароль от 6 до 20 символов\nЗАГЛАВНЫЕ и строчные буквы, цифры");
		newPasswordText.setTextLimit(20);
		newPasswordText.setBounds(153, 66, 151, 21);
		
		Label newPasswordConfirmLabel = new Label(group, SWT.NONE);
		newPasswordConfirmLabel.setBounds(12, 96, 135, 15);
		newPasswordConfirmLabel.setText("Подтверждение пароля");
		
		Text newPasswordConfirmText = new Text(group, SWT.BORDER | SWT.PASSWORD);
		newPasswordConfirmText.setToolTipText("Подтвердите новый пароль");
		newPasswordConfirmText.setTextLimit(20);
		newPasswordConfirmText.setBounds(153, 93, 151, 21);
		
		Label passwordsSeparatorLine = new Label(group, SWT.SEPARATOR | SWT.HORIZONTAL);
		passwordsSeparatorLine.setBounds(10, 58, 294, 2);
		
		Label buttonSeparatorLine = new Label(group, SWT.SEPARATOR | SWT.HORIZONTAL);
		buttonSeparatorLine.setBounds(11, 126, 292, 2);
		
		Button buttonOK = new Button(group, SWT.NONE);
		
		buttonOK.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (passwordValidated(oldPasswordText.getText(), newPasswordText.getText(),	newPasswordConfirmText.getText())) {
					closeResources();
				}
			}
		});
		buttonOK.setBounds(70, 149, ButtonsSize.BUTTON_WIDTH.size(), ButtonsSize.BUTTON_HEIGHT.size());
		buttonOK.setText("ОК");
		
		Button buttonCancel = new Button(group, SWT.NONE);
		buttonCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				closeResources();
			}
		});
		buttonCancel.setBounds(172, 149, ButtonsSize.BUTTON_WIDTH.size(), ButtonsSize.BUTTON_HEIGHT.size());
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


	
	/* Password validator */
	private boolean passwordValidated(String oldPassword, String newPassword, String confirmNewPassword) {
		
		if (oldPassword.isBlank() && newPassword.isBlank() && confirmNewPassword.isBlank()) {
			messages.showMessage("Поля не могут быть пустые!\nВведите данные для смены пароля.", MessageTypes.WARNING);
			return false;
		}

		if (oldPassword.isBlank()) {
			messages.showMessage("Введите текущий пароль!", MessageTypes.WARNING);
			return false;
		}
		
		if (newPassword.isBlank() && confirmNewPassword.isBlank()) {
			messages.showMessage("Поля не могут быть пустые!\nВведите данные для смены пароля.", MessageTypes.WARNING);
			return false;
		}


		if (!confirmNewPassword.equals(newPassword)) {
			messages.showMessage("Введенные пароли не совпадают!", MessageTypes.WARNING);
			return false;
		}

	    if (!loginStore.getPassword().equals(oldPassword)) {
	    	messages.showMessage("Текущий пароль введен неверно!", MessageTypes.WARNING);
			return false;
	    }
	
		
		if (isValidPassword(confirmNewPassword)) {
			loginStore.setPassword(confirmNewPassword);
			loginDAO.updateData(loginStore);
			messages.showMessage("Пароль для входа в программу успешно изменен!", MessageTypes.INFORMATION);
			return true;
		} else {
			messages.showMessage("Пароль должен содержать ЗАГЛАВНЫЕ и строчные буквы, цифры, не менее 6 символов и БЕЗ пробелов!",	MessageTypes.WARNING);
			return false;
		}			

	}
	
	
	
	

	

	/* Method to validate the password. */
	private boolean isValidPassword(String password) {

		if (password == null) return false;
	
		/*
		 * Regex to check valid password. where:
		 * ^ represents starting character of the string.
		 * (?=.*[0-9]) represents a digit must occur at least once.
		 * (?=.*[a-z]) represents a lower case alphabet must occur at least once.
		 * (?=.*[A-Z]) represents an upper case alphabet that must occur at least once.
		 * (?=.*[@#$%^&-+=()] represents a special character that must occur at least once.
		 * (?=\\S+$) white spaces don’t allowed in the entire string.
		 * .{6, 20} represents at least 6 characters and at most 20 characters.
		 * $ represents the end of the string.
		 */

		//RegEx without special symbols
		String regex = "^(?=.*[0-9])" + "(?=.*[a-z])(?=.*[A-Z])" + "(?=\\S+$).{6,20}$";

		/* Compile the ReGex */
		Pattern pattern = Pattern.compile(regex);

		/* Pattern class contains matcher() method to find matching between given password and regular expression. */
		Matcher matcher = pattern.matcher(password);

		/* Return boolean if the password matched or not the RegEx */
		return matcher.matches();
	}
	
	
	/* Dispose resources on closing the window */
	private void closeResources() {
		if (shell != null) shell.dispose();
	}
	
}
