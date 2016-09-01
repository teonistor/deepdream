import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Persistence implements Serializable {

	// Auto-generated UID
	private static final long serialVersionUID = 7758400748873296328L;
	
	private static final String fileName = "persistence";
	
	
	private File baseDir, refDir, outDir;
	private transient boolean showHelp;

	
	public File baseDir() { return baseDir; }
	public File refDir() { return refDir; }
	public File outDir() { return outDir; }

	public void setBaseDir(File baseDir) {
		this.baseDir = baseDir;
		persist();
	}

	public void setRefDir(File refDir) {
		this.refDir = refDir;
		persist();
	}

	public void setOutDir(File outDir) {
		this.outDir = outDir;
		persist();
	}

	public boolean showHelp() {
		if (showHelp) {
			showHelp = false;
			return true;
		}
		return false;
	}

	
	private Persistence() {
		baseDir = new File(Main.homeDir);
		refDir = new File(Main.homeDir);
		outDir = new File(Main.homeDir);
		showHelp = true;
	}	
	
	private void fix () {
		if (!baseDir.exists())
			baseDir = new File(Main.homeDir);
		if (!refDir.exists())
			refDir = new File(Main.homeDir);
		if (!outDir.exists())
			outDir = new File(Main.homeDir);
		showHelp = false;
	}
	
	public void persist() {
		try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName))) {
			out.writeObject(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Persistence init() {
		Persistence p;
		try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName))) {
			p = (Persistence) in.readObject();
			
			p.fix();
		}
		catch (ClassNotFoundException | IOException | ClassCastException | NullPointerException e) {
			e.printStackTrace();
			System.out.println("Starting fresh Persistence...");
			p = new Persistence();
		}
		return p;
	}
}
