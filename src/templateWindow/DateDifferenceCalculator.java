package templateWindow;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

import enumsStore.CredentialsSwitches;
import enumsStore.CredentialsTypes;
import enumsStore.MessageTypes;
import utilities.MessageBuilder;
import utilities.MessageDialog;
import utilities.ValuesValidator;


/**
 * This class is used to check the number of days
 * that have passed since the password was changed
 * and to form the text of the message displayed to the user.
 * <p>
 * @author Aleksander Demichev
 * @version 1.0.0.0
 */

public final class DateDifferenceCalculator {

	private Display display;
	private MessageDialog messages;
	private MessageBuilder messageBuilder;
	private ValuesValidator validator;
	
	
	public DateDifferenceCalculator (Display display, MessageDialog messages, MessageBuilder messageBuilder, ValuesValidator validator) {
		this.display = display;
		this.messages = messages;
		this.messageBuilder = messageBuilder;
		this.validator = validator;
	}
	
	
	/* List for storing names of the transmission systems, which passwords must be changed */
	private List<String> namesOfTransmissionSystems = new ArrayList<String>(4);
	
	

	
	
	
	
	/* Calculate difference between two dates, return number of days (int) */
	public int getDateDifference(long databaseTime) {
		
		long currentTime = System.currentTimeMillis() / 1000L;		

		Date databaseDate = new Date ();
		databaseDate.setTime(databaseTime * 1000);

		Date currentDate = new Date ();
		currentDate.setTime(currentTime * 1000);

		return (int)( (currentDate.getTime() - databaseDate.getTime()) / (1000 * 60 * 60 * 24) );
	}
	
	
	/*
	 * 1. Get days count from last password changed date.
	 * 2. Call the method to compare the number of days that have passed since the last date the password was changed.
	 * 3. If number of days more then 35 - show warning message and set label text color to YELLOW
	 * 4. If number of days more then 45 - show error message and set label text color to RED
	 */

	public void checkDaysCountOfPasswordChanged (
			long databaseTime,
			Label labelToColorize,
			CredentialsTypes credentialsType,
			CredentialsSwitches credentialsName,
			boolean calledFromNotifier) {
		
		
		int daysPast = 0;
		
		switch (credentialsType) {
			case CHANNEL:
				daysPast = getDateDifference(databaseTime);
				if (calledFromNotifier) messageBuilder.setNotifierToolTip("CH: " + daysPast);
				break;
			case APPLICATION:
				daysPast = getDateDifference(databaseTime);
				if (calledFromNotifier) messageBuilder.setNotifierToolTip(" AP: " + daysPast + "\n");
				break;
		}
		
		if(daysPast >= 35 && daysPast <= 45) {
	
			if (labelToColorize != null) labelToColorize.setForeground(new Color(display, 153, 0, 0)); //Very dark red color
			if (credentialsName != null) {
				addTransmissionSystemToNotifierMessage(credentialsName.getTitle().toUpperCase());
			} else {
				addTransmissionSystemToNotifierMessage(credentialsType.getName());
			}
			

			/* 
			 * MessageDialog.WARNING_MESSAGE Integer value: 65576
			 * MessageDialog.ERROR_MESSAGE Integer value: 65569
			 */
			if(messages.getMessageStyle(messageBuilder.getNotifierMessageType()) != 65569) {
				messageBuilder.setNotifierMessageType(MessageTypes.WARNING);
			}
			
			messageBuilder.setStringToNotifierMessage("Нужно ИЗМЕНИТЬ " + getAccountTypeName(credentialsType) + " пароль! Последний раз пароль меняли " + daysPast + " " + this.messageBuilder.getProperDayDeclination(daysPast) + " назад!\n");
			
		} else if (daysPast > 45) {

			if (labelToColorize != null) labelToColorize.setForeground(new Color(display, 255, 0, 0)); 	//Red color
			if (credentialsName != null) {
				addTransmissionSystemToNotifierMessage(credentialsName.getTitle().toUpperCase());
			} else {
				addTransmissionSystemToNotifierMessage(credentialsType.getName());
			}
						
			messageBuilder.setNotifierMessageType(MessageTypes.ERROR);
			messageBuilder.setStringToNotifierMessage(getAccountTypeName(credentialsType) + " пароль ПРОСРОЧЕН! Последний раз пароль меняли " + daysPast + " " + this.messageBuilder.getProperDayDeclination(daysPast) + " назад!\n");
		}
		
	}
	
	
	/* Getting account name */
	public String getAccountTypeName(CredentialsTypes credentials) {
		return credentials == CredentialsTypes.CHANNEL ? CredentialsTypes.CHANNEL.getName() : CredentialsTypes.APPLICATION.getName();
	}
	
	/* Adding name of the transmission system to the List notifierMessageType and to the StringBuilder */
	public void addTransmissionSystemToNotifierMessage(String transmissionSystem) {
		if(!validator.isStringPresentInList(getTransmitionSystemStore(), transmissionSystem)) {									
			setStringToTransmitionSystemStore(transmissionSystem);
			messageBuilder.setStringToNotifierMessage(messageBuilder.getStringSeparator(78) + "\t\t***  " + transmissionSystem + "  ***\n\n");			
		}
	}
	
	/* Get List namesOfTransmissionSystems */
	public List<String> getTransmitionSystemStore() {
		return new ArrayList<String>(namesOfTransmissionSystems);
	}
	
	
	/* Add String value to List namesOfTransmissionSystems */
	public void setStringToTransmitionSystemStore(String transmissionSystem) {
		this.namesOfTransmissionSystems.add(transmissionSystem);
	}
	
	/* Clear the List namesOfTransmissionSystems */
	public void clearTransmitionSystemStore() {
		this.namesOfTransmissionSystems.clear();
	}
	
}
