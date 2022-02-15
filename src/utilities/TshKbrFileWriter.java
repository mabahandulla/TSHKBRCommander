package utilities;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import org.eclipse.swt.widgets.Display;

/**
 * This is the class for writing files.
 * <p>
 * @author Aleksander Demichev
 * @version 1.0.0.0
 */

public final class TshKbrFileWriter {
    
	
    public TshKbrFileWriter(Display display, String filePath, String message, String charset, boolean append, boolean autoFlush) {
    	
    	File file = new File(filePath);

		try {
			if (!file.getParentFile().exists())	file.getParentFile().mkdirs();
			FileOutputStream fileOutputStream = new FileOutputStream(file, append);
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, charset);
			BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
			PrintWriter printWriter = new PrintWriter(bufferedWriter, autoFlush);
			printWriter.write(message);
			printWriter.close();
		} catch (IOException e) {
			new ShowErrorDialog (display, e, "Ошибка записи файла! Для просмотра ошибки нажмите кнопку [<<< Подробней]", false).open();
		}
    }
}
