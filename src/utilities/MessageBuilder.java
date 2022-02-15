package utilities;

import java.util.Arrays;

import enumsStore.MessageTypes;


/**
 * This is a class for building a message that is shown to the user.
 * <p>
 * @author Aleksander Demichev
 * @version 1.0.0.0
 */

public final class MessageBuilder {

	/* 
	 * StringBuilders for building messages:
	 * 	TSH KBR and Notifier warning message
	 *  the Tray Notifier Tool Tip Text.
	 */
	private StringBuilder notifierMessage, trayToolTipText;
	private MessageTypes notifierMessageType; 

	
	public MessageBuilder () {
		this.notifierMessage = new StringBuilder();
		this.trayToolTipText = new StringBuilder();
		this.notifierMessageType = MessageTypes.INFORMATION;
	}
	
	
	
	
	
	/* Get notifierMessage */
	public StringBuilder getNotifierMessage() {
		return new StringBuilder(this.notifierMessage);
	}
	
	/* Get Tray Notifier StringBuilder */
	public StringBuilder getTrayNotifierToolTip() {
		return new StringBuilder(this.trayToolTipText);
	}
	
	/* Delete all Strings from StringBuilder notifierMessage */
	public void clearNotifierMessage() {
		this.notifierMessage.delete(0, this.notifierMessage.length());
	}
	
	/* Add String to notifierMessage */
	public void setStringToNotifierMessage(String message) {
		this.notifierMessage.append(message);
	}

	/* Get notifierMessageType */
	public MessageTypes getNotifierMessageType() {
		return this.notifierMessageType;
	}
	
	/* Set notifierMessageType */
	public void setNotifierMessageType(MessageTypes messageType) {
		this.notifierMessageType = messageType;
	}
	
	/* Add String to StringBuilder for building Tray Notifier Tool Tip text */
	public void setNotifierToolTip(String message) {
		this.trayToolTipText.append(message);
	}
	
	/* Delete all Strings from Tray Notifier StringBuilder */
	public void clearTrayNotifierToolTip() {
		this.trayToolTipText.delete(0, this.trayToolTipText.length());
	}
	
	/* Replace the STATE String in StringBuilder for building Tray Notifier Tool Tip text */
	public void updateTrayNotifierToolTip(String notifierState) {
		this.trayToolTipText.replace(24, 34, notifierState);
	}
	
	/* Get the correct declension of the word "days" */
	public String getProperDayDeclination(int daysCount) {
		
	    int preLastDigit = daysCount % 100 / 10;

	    if (preLastDigit == 1) {
	        return "дней";
	    }

	    switch (daysCount % 10) {
	        case 1:
	            return "день";
	        case 2:
	        case 3:
	        case 4:
	            return "дня";
	        default:
	            return "дней";
	    }
	}
	
	
	/* Repeat char n times and returns new String */
	private final String repeatChar(char c, int length) {
	    char[] data = new char[length];
	    Arrays.fill(data, c);
	    return new String(data);
	}
	
	
	/* Get String separator count of DASH char */
	public String getStringSeparator(int charCount) {
		return this.repeatChar('-', charCount);
	}
	
	
	
}
