/*  Main.java
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
