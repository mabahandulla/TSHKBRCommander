package infoWindow;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import enumsStore.ApplicationMode;
import enumsStore.ButtonsSize;
import startWindow.ApplicationEnvironments;
import utilities.ProcessExecutor;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Group;

/**
 * This is the class for displaying the About window.
 * Shows the contact information of the app creator, the current version of the app, and the SQLight database version.
 * <p>
 * @author Aleksander Demichev
 * @version 1.0.0.0
 */

public final class AboutWindow {

	private Display display;
	private ApplicationMode appMode;
	private Shell shell;
	private String databaseVersion;
	private Image windowImage;
	private ProcessExecutor processLuncher;

	
	public AboutWindow(ApplicationEnvironments appEnv) {
		this.display = appEnv.getDisplay();
		this.appMode = appEnv.getAppMode();
		this.databaseVersion = appEnv.getSettingsStore().getDatabaseVersion();
		this.windowImage = appEnv.getWindowIcon();
		this.processLuncher = appEnv.getProcessExecutor();
	}

	/**
	 * Open the window.
	 * @wbp.parser.entryPoint
	 */
	public void open() {
		

		
		this.shell = new Shell(this.display, SWT.BORDER | SWT.APPLICATION_MODAL | SWT.CLOSE);
		
		this.shell.addShellListener(new ShellAdapter() {
			@Override
			public void shellClosed(ShellEvent e) {
				AboutWindow.this.closeResources();
			}
		});
		
		if(this.windowImage != null) this.shell.setImage(this.windowImage);
		this.shell.setSize(325, 265);
		this.shell.setText("О программе " + appMode.getName());
		


		Composite composite = new Composite(this.shell, SWT.NONE);

		Group group = new Group(composite, SWT.NONE);
		group.setBounds(10, 9, 290, 207);

		Label lblNewLabel = new Label(group, SWT.NONE);
		lblNewLabel.setLocation(7, 14);
		lblNewLabel.setSize(52, 52);
		lblNewLabel.setAlignment(SWT.CENTER);
		
		/* Icon taken from here: https://findicons.com/icon/133187/matrix_smart_folder */
		if(this.windowImage != null) lblNewLabel.setImage(this.windowImage);

		Button btnOk = new Button(group, SWT.NONE);
		btnOk.setLocation(107, 161);
		btnOk.setSize(ButtonsSize.BUTTON_WIDTH.size(), ButtonsSize.BUTTON_HEIGHT.size());
		btnOk.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				AboutWindow.this.closeResources();
			}
		});
		btnOk.setText("OK");

		Label lblNewLabel_1 = new Label(group, SWT.NONE);
		lblNewLabel_1.setLocation(65, 14);
		lblNewLabel_1.setSize(124, 15);
		lblNewLabel_1.setText(appMode.getName());

		Label lblCopyright = new Label(group, SWT.NONE);
		lblCopyright.setLocation(65, 33);
		lblCopyright.setSize(215, 15);
		lblCopyright.setText("Copyright @ 2021 Александр Демичев");

		Link link = new Link(group, SWT.NONE);
		link.setLocation(65, 56);
		link.setSize(148, 15);
		link.setToolTipText("Напишите мне :)");
		link.setForeground(new Color(this.display, 0, 0, 255));
		link.setText("<a href=\"mailto:mabahandulla@gmail.com\">mabahandulla@gmail.com</a>");
		link.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				processLuncher.runProcess(e.text);
			}
		});

		Table table = new Table(group, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
		table.setLocation(45, 80);
		table.setSize(199, 67);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		TableColumn tblclmnNewColumn = new TableColumn(table, SWT.CENTER);
		tblclmnNewColumn.setResizable(false);
		tblclmnNewColumn.setWidth(135);
		tblclmnNewColumn.setText("Программа");

		TableColumn tblclmnNewColumn_1 = new TableColumn(table, SWT.CENTER);
		tblclmnNewColumn_1.setResizable(false);
		tblclmnNewColumn_1.setWidth(60);
		tblclmnNewColumn_1.setText("Версия");

		TableItem tableItem1 = new TableItem(table, SWT.NONE);
		tableItem1.setText(0, appMode.getName());
		tableItem1.setText(1, "1.0.0.0");
		TableItem tableItem2 = new TableItem(table, SWT.NONE);
		tableItem2.setText(0, "БД SQLight");
		tableItem2.setText(1, this.databaseVersion);

		Monitor primary = this.display.getPrimaryMonitor();
		Rectangle bounds = primary.getBounds();
		Rectangle rect = this.shell.getBounds();
		int x = bounds.x + (bounds.width - rect.width) / 2;
		int y = bounds.y + (bounds.height - rect.height) / 2;
		this.shell.setLocation(x, y);
		this.shell.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		this.shell.open();
		this.shell.layout();

	}
	
	
	
	
	public void closeResources() {
		if (this.shell != null) this.shell.dispose();
	}
	
}
