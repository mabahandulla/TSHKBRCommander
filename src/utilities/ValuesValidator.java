package utilities;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;

/**
 * This is a class for various checks on text values entered by the user.
 * <p>
 * @author Aleksander Demichev
 * @version 1.0.0.0
 */

public final class ValuesValidator {

	private Display display;

	public ValuesValidator (Display display) {
			this.display = display;
		}

	
	
	
	
	/* Check Text items if they BLANK or contain a white spaces */
	public boolean checkEmptyText(Group parentGroup) {
		for (Control textItem : parentGroup.getChildren()) {
			if (textItem instanceof Text && ((Text) textItem).getText().isBlank()) {
				this.highlightTextBorder(parentGroup, textItem);
				return true;
			}
		}

		return false;
	}

	/* Check Text items if text value contain a white spaces */
	public boolean checkWhiteSpaces(Group parentGroup) {
		for (Control textItem : parentGroup.getChildren()) {
			if (textItem instanceof Text && this.isWhiteSpaceMatch(((Text) textItem).getText())) {
				this.highlightTextBorder(parentGroup, textItem);
				return true;
			}
		}

		return false;
	}
	
	/* Parse the given String to find if it contains a white spaces */
	public boolean isWhiteSpaceMatch(String password) {
		if (password != null) {
			for (int i = 0; i < password.length(); i++) {
				if (Character.isWhitespace(password.charAt(i))) {
					return true;
				}
			}
		}
		return false;
	}


	/* Paint RED Rectangle over the Text item to highlight a text field containing an error */
	private void highlightTextBorder(Group group, Control text) {

		GC gc = new GC(group);
		gc.setLineWidth(4);
		gc.setForeground(this.display.getSystemColor(SWT.COLOR_RED));
		gc.drawRectangle(text.getBounds());

	}
	
	
	/* Is String is present in List */
	public Boolean isStringPresentInList(List<String> stringsStore, String stringForAdding) {
		return stringsStore.stream().anyMatch(s -> s.replaceAll("\\s", "").toLowerCase().equals(stringForAdding.replaceAll("\\s", "").toLowerCase()));
	}
	

	/* Are two string values the same */
	public Boolean isStringsAreEqual(String originalString, String stringForCompare) {
		return originalString.replaceAll("\\s", "").toLowerCase().equals(stringForCompare.replaceAll("\\s", "").toLowerCase());
	}

}
