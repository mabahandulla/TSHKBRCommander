package utilities;

import java.io.InputStream;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Display;

import enumsStore.ImagesStore;
import enumsStore.MessageTypes;

/**
 * This class is used to store the images required by the application.
 * <p>
 * @author Aleksander Demichev
 * @version 1.0.0.0
 */

public class ImageСontainer {
	
	
	private Display display;
	/* Images for setting application icons */
	private Image windowImage, cbLogo, buttonKey, errorIcon;
	
	
	public ImageСontainer (Display display) {
		this.display = display;
		this.windowImage = this.getImage(ImagesStore.LOGO.getImageIcon());
		this.errorIcon = this.getImage(ImagesStore.ERROR_ICON.getImageIcon());
		this.cbLogo = this.getImage(ImagesStore.CB_LOGO.getImageIcon());
		this.buttonKey = this.getImage(ImagesStore.BUTTON_KEY.getImageIcon());
	}
	
	
	
	
	
	/* Get icon image from Resource storage and return Image to set it on tray */
	public final Image getImage(String imageFileName) {

		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(imageFileName);

        if (inputStream == null) return null;
        
        ImageData imageData = null;
        Image returnedImage = null;
        
		try {
			imageData = new ImageData(inputStream);
			returnedImage = new Image(this.display, imageData);
			inputStream.close();
		} catch (Exception ex) {
			
			new MessageDialog(display).showMessage(
					"Не удается загрузить изображение иконки!"
					+ "\n\nТекст ошибки:\n\nCan`t load icon image "
					+ imageFileName
					+ " from resource store!\n\n"
					+ ex.getMessage(),
					MessageTypes.ERROR);
		}
		
		return returnedImage;
	}

	
	
	/* Get window icon Image */
	public Image getWindowIcon() {
		return this.windowImage;
	}
	
	/* Get CB Logo Image*/
	public Image getCbLogo() {
		return this.cbLogo;
	}
	
	/* Get button Key Image */
	public Image getButtonKey() {
		return this.buttonKey;
	}
	
	/* Get error icon Image */
	public Image getErrorIcon() {
		return this.errorIcon;
	}	
	
}
