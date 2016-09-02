import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.SwingUtilities;

public class Main {

	// Nailed constants
	public static final String homeDir = System.getProperty("user.home");
	public static final String pythonScript = "dream.py";
	public static final File endingsFile = new File("model", "endings");

	/* Read previous settings from persistence file
	 * (If settings are not found, create them fresh)
	 * 
	 * Launch the GUI
	 */
	public static void main(String[] args) throws FileNotFoundException {
		
		Persistence p = Persistence.init();
		
		SwingUtilities.invokeLater(new Window(p));
	}

}
