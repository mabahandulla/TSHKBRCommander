package utilities;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import enumsStore.MessageTypes;

/**
 * This is the class for displaying messages to the user.
 * <p>
 * @author Aleksander Demichev
 * @version 1.0.0.0
 */

public final class MessageDialog {

	private Display display;
	
	public MessageDialog(Display display){
		this.display = display;
	}
	
	public final int showMessage(String msgText, MessageTypes msgType) {

		Shell shell = new Shell(this.display);
		int style = this.getMessageStyle(msgType);
		MessageBox messageBox = new MessageBox(shell, style);
		messageBox.setText(msgType.getTitle());
		messageBox.setMessage(msgText);
		int response = messageBox.open();
		
		return response;
	}

	
	
	public final int getMessageStyle(MessageTypes msgType) {

		int style = 0;

		switch (msgType) {
		case QUESTION:
			style = SWT.APPLICATION_MODAL | SWT.ICON_QUESTION | SWT.YES | SWT.NO;
			break;
		case ERROR_QUESTION:
			style = SWT.APPLICATION_MODAL | SWT.ICON_ERROR | SWT.YES | SWT.NO;
			break;
		case WARNING_QUESTION:
			style = SWT.APPLICATION_MODAL | SWT.ICON_WARNING | SWT.YES | SWT.NO;
			break;
		case INFORMATION:
			style = SWT.APPLICATION_MODAL | SWT.ICON_INFORMATION | SWT.OK;
			break;
		case ERROR:
			style = SWT.APPLICATION_MODAL | SWT.ICON_ERROR | SWT.OK;
			break;
		case WARNING:
			style = SWT.APPLICATION_MODAL | SWT.ICON_WARNING | SWT.OK;
			break;
		default:
			style = SWT.APPLICATION_MODAL | SWT.ICON_INFORMATION | SWT.OK;
			break;			
		}
		
		return style;
	}
	
}
