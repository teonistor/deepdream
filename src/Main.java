import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import javax.swing.SwingUtilities;

public class Main {

	// Nailed constants
	public static final String homeDir = System.getProperty("user.home");
	public static final String pythonScript = "dream.py";
	public static final File endingsFile = new File("model", "endings");
	
	public static BufferedReader endingsIn;

	/* Read previous settings from persistence file
	 * (If settings are not found, create them fresh)
	 * Open the file containing the names of all possible model endings
	 * Launch the GUI
	 */
	public static void main(String[] args) throws FileNotFoundException {
		
		Persistence p = Persistence.init();

		endingsIn = new BufferedReader(new FileReader(endingsFile));
		
		SwingUtilities.invokeLater(new Window(p));
	}

}
