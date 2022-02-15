package utilities;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Text;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;

/**
 * This class is used to display error text in a dialog box.
 * <p>
 * @author Aleksander Demichev
 * @version 1.0.0.0
 */

public final class ShowErrorDialog {

	private Display display;	
	private Shell shell;
	private ImageСontainer imageСontainer;
	private Image windowImage, errorIcon;
	private String exceptionMessage, exceptionText, userMessage;
	private Text errorTitleText;
	private Text messageExceptionText;
	private Label emptyLabel;
	private Button buttonDetails;
	private boolean errorMessageTextExpanded;
	private boolean errorOnStartup;



	public ShowErrorDialog(Display display, Exception e, String userMessage, boolean errorOnStartup) {
		this.display = display;
		this.imageСontainer = new ImageСontainer(display);
		this.windowImage = this.imageСontainer.getWindowIcon();
		this.errorIcon = this.imageСontainer.getErrorIcon();
		this.errorOnStartup = errorOnStartup;
		this.exceptionMessage = e.getLocalizedMessage();
		this.exceptionText = this.getExceptionString(e);
		this.userMessage = userMessage;
	}



	
	
	/**
	 * @wbp.parser.entryPoint
	 */
	public void open() {
		
		createContents();
		
		shell.open();
		shell.layout();

		if(errorOnStartup) {
			while (!display.isDisposed()) {

				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
		}
	}

	
	
	public void createContents() {
		

		errorMessageTextExpanded = false;
		
		shell = new Shell(display);
		shell.addControlListener(new ControlAdapter() {
			@Override
			public void controlResized(ControlEvent e) {
				if (!errorMessageTextExpanded) {
					shell.setMinimumSize(new Point(450, 175));
					shell.setSize(450, 175);
					shell.redraw();
				}
			}
		});


		if (windowImage != null) shell.setImage(windowImage);
		
		shell.setMinimumSize(new Point(450, 175));
		shell.setSize(450, 175);
		shell.setText("Ошибка");
		shell.addShellListener(new ShellAdapter() {
			@Override
			public void shellClosed(ShellEvent e) {
				closeResources();
			}
		});

		Monitor primary = display.getPrimaryMonitor();
		Rectangle bounds = primary.getBounds();
		Rectangle rect = shell.getBounds();
		int x = bounds.x + (bounds.width - rect.width) / 2;
		int y = bounds.y + (bounds.height - rect.height) / 2;
		shell.setLocation(x, y);
		shell.setLayout(new GridLayout(4, false));

		emptyLabel = new Label(shell, SWT.NONE);
		
		GridData gd_label = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_label.widthHint = 20;
		emptyLabel.setLayoutData(gd_label);

		Label imgLabel = new Label(shell, SWT.CENTER);
		
		/* Icon taken from here: https://findicons.com/icon/544381/error  */
		if (errorIcon != null)	imgLabel.setImage(errorIcon);

		GridData gd_imgLabel = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_imgLabel.widthHint = 32;
		gd_imgLabel.heightHint = 32;
		imgLabel.setLayoutData(gd_imgLabel);

		errorTitleText = new Text(shell, SWT.READ_ONLY | SWT.WRAP | SWT.MULTI);
		errorTitleText.setText(userMessage + "\n" + exceptionMessage);
		errorTitleText.setEditable(false);
		GridData gd_errorTitleText = new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1);
		gd_errorTitleText.widthHint = 310;
		gd_errorTitleText.heightHint = 81;
		errorTitleText.setLayoutData(gd_errorTitleText);
		new Label(shell, SWT.NONE);

		buttonDetails = new Button(shell, SWT.NONE);

		GridData gd_buttonDetails = new GridData(SWT.RIGHT, SWT.CENTER, true, false, 2, 1);
		gd_buttonDetails.widthHint = 100;
		buttonDetails.setLayoutData(gd_buttonDetails);
		buttonDetails.setText("<<< Подробней");

		Button buttonOk = new Button(shell, SWT.NONE);
		buttonOk.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				closeResources();
			}
		});

		GridData gd_buttonOk = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_buttonOk.widthHint = 80;
		buttonOk.setLayoutData(gd_buttonOk);
		buttonOk.setText("OK");

		messageExceptionText = new Text(shell, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		messageExceptionText.setEditable(false);
		messageExceptionText.setText(exceptionText);

		GridData gd_messageExceptionText = new GridData(SWT.FILL, SWT.FILL, true, true, 4, 1);
		gd_messageExceptionText.heightHint = 150;
		messageExceptionText.setLayoutData(gd_messageExceptionText);
		messageExceptionText.setVisible(false);

		buttonDetails.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				toggleMessageTextState();
			}
		});
	}

	
	
	
	
	/* Toggle boolean field for show or hide Exception Message Text */
	private void toggleMessageTextState() {
		errorMessageTextExpanded = !errorMessageTextExpanded;
		if (errorMessageTextExpanded) {
			shell.setMinimumSize(new Point(450, 170));
			shell.setSize(450, 550);
			messageExceptionText.setVisible(true);
			buttonDetails.setText(">>> Подробней");
		} else {
			shell.setMinimumSize(new Point(450, 175));
			shell.setSize(450, 175);
			messageExceptionText.setVisible(false);
			buttonDetails.setText("<<< Подробней");
		}
	}

	
	
	
	public String getExceptionString(Exception e) {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		PrintStream printStream = new PrintStream(byteArrayOutputStream);
		e.printStackTrace(printStream);
		printStream.close();

		return new String(byteArrayOutputStream.toString());
	}

	
	
	public void closeResources() {
		
		if (errorOnStartup) {
			if (display != null) display.dispose();
		} else {
			if (shell != null) shell.dispose();
		}
	}

}
