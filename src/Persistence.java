/*  Persistence.java
    GUI for an extension of the Deep Dream project
    Copyright (C) 2016  Teodor Nistor

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.
    
    See <http://www.gnu.org/licenses/>.
*/

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
	
	// Nailed constant
	private static final String fileName = "persistence";
	
	// What we save and what we don't save
	private File baseDir, refDir, outDir;
	private transient boolean showHelp;

	// Getters
	public File baseDir() { return baseDir; }
	public File refDir() { return refDir; }
	public File outDir() { return outDir; }

	// Setters which also write the file to disc
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

	/* You only ever auto-show help when the program is run for the first time (persistence
	 * file was not found)
	 */
	public boolean showHelp() {
		if (showHelp) {
			showHelp = false;
			return true;
		}
		return false;
	}

	// Private constructor called when an instance cannot be loaded from the file
	private Persistence() {
		baseDir = new File(Main.homeDir);
		refDir = new File(Main.homeDir);
		outDir = new File(Main.homeDir);
		showHelp = true;
	}	
	
	/* Make sure previously saved directories still exist
	 * Make sure transient fields have a value
	 */
	private void fix () {
		if (!baseDir.exists())
			baseDir = new File(Main.homeDir);
		if (!refDir.exists())
			refDir = new File(Main.homeDir);
		if (!outDir.exists())
			outDir = new File(Main.homeDir);
		showHelp = false;
	}
	
	// Write data to file
	public void persist() {
		try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName))) {
			out.writeObject(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/* Call this from outside to get an instance
	 * Attempts to load the previous instance from a file, and if it can't, creates a
	 * fresh one.
	 */
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
