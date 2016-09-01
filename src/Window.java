import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;

public class Window extends JFrame implements Runnable {

	// Auto-generated UID
	private static final long serialVersionUID = -4975301576111113381L;
	
	// Constants
	private static final Dimension DEFAULT_IMGPAN_DIM = new Dimension(50, 50);
	private static final Insets insets = new Insets(3, 3, 3, 3);
	private static final String helpText = "Don't leave large images without downscaling. Ask Teo for assistance.";

	// Title constructor
	public Window(Persistence p) throws HeadlessException {
		super ("Google Deep Dream Implementor");
		persistence = p;
	}
	
	// Variables needed for the business to work
	private Persistence persistence;
	private File base, ref, out;
	private int complexities;

	// Method-accessible components
	private JLabel		base_msg = new JLabel("Base image:");
	private ImgPanel	base_img = new ImgPanel(DEFAULT_IMGPAN_DIM);
	private JButton		base_browse = new JButton("Browse");
	private JButton		base_del = new JButton("X");
	private JCheckBox	base_auto = new JCheckBox("Auto downscale to (width)");
	private JTextField	base_reso = new JTextField("640", 6);
	private JLabel		ref_msg = new JLabel("Reference image:");
	private ImgPanel	ref_img = new ImgPanel(DEFAULT_IMGPAN_DIM);
	private JButton		ref_browse = new JButton("Browse");
	private JButton		ref_del = new JButton("X");
	private JCheckBox	ref_auto = new JCheckBox("Auto downscale");
	private JLabel		out_msg1 = new JLabel("Output directory:");
	private JTextField	out_dir = new JTextField(20);
	private JButton		out_browse = new JButton("Browse");
	private JLabel		out_msg2 = new JLabel("Output file name:");
	private JTextField	out_name = new JTextField(20);
	private JLabel		iter_msg = new JLabel("Iterations:");
	private JSlider		iter_sld = new JSlider(JSlider.HORIZONTAL, 5, 12, 10);
	private JLabel		complex_msg = new JLabel("Complexity:");
	private JSlider		complex_sld = new JSlider(JSlider.HORIZONTAL); // TODO find sensible values & load past state from save file
	private JButton		ok = new JButton("Go!");
	private JButton		help = new JButton("Help!");
	
	// Window setup
	public void run() {
		out = persistence.outDir();
		
		// Get the amount of complexity names from the model
		try {
			complexities = Integer.parseInt(Main.endingsIn.readLine());
		}
		catch (Exception e) {
			System.err.println("Fatal error!");
			e.printStackTrace();
			System.exit(-1);
		}
		
		// Set up some components
		out_dir.setEditable(false);
		out_dir.setText(out.getPath());
		
		base_auto.setSelected(true);
		ref_auto.setSelected(true);
		
		complex_sld.setMinimum(1);
		complex_sld.setMaximum(complexities);
		complex_sld.setValue(19);
		
		// TODO not yet implemented
		iter_sld.setEnabled(false);
		
		// Add components
		setLayout(new GridBagLayout());
		
		add (base_msg,		new GridBagConstraints(0, 0, 1, 2, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, insets, 0, 0));
		add (base_img,		new GridBagConstraints(1, 0, 1, 2, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, insets, 0, 0));
		add (base_browse,	new GridBagConstraints(2, 0, 1, 1, 0.0, 0.5, GridBagConstraints.SOUTH, GridBagConstraints.NONE, insets, 0, 0));
		add (base_del,		new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.SOUTH, GridBagConstraints.NONE, insets, 0, 0));
		add (base_auto,		new GridBagConstraints(2, 1, 1, 1, 0.0, 0.5, GridBagConstraints.NORTH, GridBagConstraints.NONE, insets, 0, 0));
		add (base_reso,		new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.NONE, insets, 0, 0));
		add (ref_msg,		new GridBagConstraints(0, 2, 1, 2, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, insets, 0, 0));
		add (ref_img,		new GridBagConstraints(1, 2, 1, 2, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, insets, 0, 0));
		add (ref_browse,	new GridBagConstraints(2, 2, 1, 1, 0.0, 0.5, GridBagConstraints.SOUTH, GridBagConstraints.NONE, insets, 0, 0));
		add (ref_del,		new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0, GridBagConstraints.SOUTH, GridBagConstraints.NONE, insets, 0, 0));
		add (ref_auto,		new GridBagConstraints(2, 3, 2, 1, 0.0, 0.5, GridBagConstraints.NORTH, GridBagConstraints.NONE, insets, 0, 0));
		add (out_msg1,		new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, insets, 0, 0));
		add (out_dir,		new GridBagConstraints(1, 4, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, insets, 0, 0));
		add (out_browse,	new GridBagConstraints(3, 4, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, insets, 0, 0));
		add (out_msg2,		new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, insets, 0, 0));
		add (out_name,		new GridBagConstraints(1, 5, 3, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, insets, 0, 0));
		add (iter_msg,		new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, insets, 0, 0));
		add (iter_sld,		new GridBagConstraints(1, 6, 3, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, insets, 0, 0));
		add (complex_msg,	new GridBagConstraints(0, 7, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, insets, 0, 0));
		add (complex_sld,	new GridBagConstraints(1, 7, 3, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, insets, 0, 0));
		add (help,			new GridBagConstraints(0, 8, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, insets, 0, 0));
		add (ok,			new GridBagConstraints(0, 8, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, insets, 0, 0));
		
		
		// Action listeners
		
		base_browse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				File[] file = new File[1];
				chooseImage(base_img, file, persistence.baseDir());
				base = file[0];
				if (base != null)
					persistence.setBaseDir(base.getParentFile());
			}
		});
		
		base_del.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				base_img.setImage(null);
				base = null;
			}
		});
		
		ref_browse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				File[] file = new File[1];
				chooseImage(ref_img, file, persistence.refDir());
				ref = file[0];
				if (ref != null)
					persistence.setRefDir(ref.getParentFile());
			}
		});
		
		ref_del.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ref_img.setImage(null);
				ref = null;
			}
		});
		
		out_browse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser(out);
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				fc.setMultiSelectionEnabled(false);
				fc.showOpenDialog(Window.this);
				File file = fc.getSelectedFile();
				if (file != null) {
					out = file.getAbsoluteFile();
					out_dir.setText(out.getPath());
					persistence.setOutDir(out);
				}
			}
		});
		
		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doBusiness();
			}
		});
		
		help.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				inform (helpText);
			}
		});
		
		// Show the window
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		
		// Dimension game to play nicely with Grid Bag Layout
		Dimension d = getPreferredSize();
		d.width += 10;
		d.height += 10;
		setMinimumSize(d);
		
		// Show help if program is run for the first time
		if (persistence.showHelp())
			inform (helpText);
	}
	
	/* Creates a File Chooser to choose an image, sets the given Image Panel to that image
	 * and the given File to the corresponding path
	 */
	private void chooseImage (ImgPanel panel, File[] outFile, File directory) {
		JFileChooser fc = new JFileChooser(directory);
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc.setMultiSelectionEnabled(false);
		fc.showOpenDialog(this);
		File file = fc.getSelectedFile();
		try {
			file = file.getAbsoluteFile();
			BufferedImage image = ImageIO.read(file);
			image.hashCode(); // Touch the image see it's not null
			panel.setImage(image);
			outFile[0] = file;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// The actual business of checking everything and invoking Python code
	private void doBusiness () {
		
		// Check various $hit exists
		if (base == null) {
			complain ("You must select a base image!");
			return;
		}
		if (out == null) {
			complain ("You must select an output directory!");
			return;
		}
		
		String out_name = this.out_name.getText();
		if (out_name.length() < 1) {
			complain ("You must provide a file name!");
			return;
		}
		
		// Downscale base image to a temporary file if necessary
		File base = this.base;
		if (base_auto.isSelected()) {
			try {
				base = new File("temp_b", base.getName());
				Image downscaled = base_img.getImage().getScaledInstance(Integer.parseInt(base_reso.getText()), -1, Image.SCALE_DEFAULT);
				BufferedImage buffered = new BufferedImage(downscaled.getWidth(null), downscaled.getHeight(null), BufferedImage.TYPE_INT_RGB);
				buffered.createGraphics().drawImage(downscaled, 0, 0, null);
				String[] name = base.getName().split("\\.");
				
				base.mkdirs();
				ImageIO.write(buffered, name[name.length-1], base);
			}
			catch (Exception e) {
				complain ("Error downscaling base image:\n%s", e);
				e.printStackTrace();
				return;
			}
		}
		
		// Downscale reference image to a temporary file if necessary
		File ref = this.ref;
		if (ref_auto.isSelected() && ref != null) {
			try {
				ref = new File("temp_r", ref.getName());
				Image downscaled = ref_img.getImage().getScaledInstance(322, -1, Image.SCALE_DEFAULT);
				BufferedImage buffered = new BufferedImage(downscaled.getWidth(null), downscaled.getHeight(null), BufferedImage.TYPE_INT_RGB);
				buffered.createGraphics().drawImage(downscaled, 0, 0, null);
				String[] name = ref.getName().split("\\.");
				
				ref.mkdirs();
				ImageIO.write(buffered, name[name.length-1], ref);
			}
			catch (Exception e) {
				complain ("Error downscaling reference image:\n%s", e);
				e.printStackTrace();
				return;
			}
		}
		
		// Check file extension and add it if missing
		if (!Pattern.matches(".*(\\.jpe?g|\\.png|\\.bmp)", out_name)) {
			out_name += ".png";
		}
		
		// Get the denomination of the chosen complexity
		String chosenComplexity = "";
		try {
			for (int i = complex_sld.getValue(); i>0 ;i--) {
				chosenComplexity = Main.endingsIn.readLine();
			}
		} catch (IOException e) {
			complain ("An error has occurred!\n%s", e);
			e.printStackTrace();
			return;
		}
		
		File out = new File(this.out, out_name);
		
		exec (	"python",
				Main.pythonScript,
				base.getPath(),
				ref == null ? "0" : ref.getPath(),
				out.getPath(),
				"0", // TODO Iterations hard-coded for now
				chosenComplexity);
		
		if (base_auto.isSelected())
			base.delete();
		
		if (ref_auto.isSelected() && ref != null) {
			ref.delete();
		}
		
		inform ("Done");
	}
	
	public int exec (String... arg) {
		try {
//			Process p = Runtime.getRuntime().exec(arg);
			ProcessBuilder pb = new ProcessBuilder(arg);
			pb.redirectError(ProcessBuilder.Redirect.INHERIT);
			pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
			
			Process p = pb.start();
			
//			BufferedReader in = new BufferedReader(new InputStreamReader(p.getErrorStream()));
//			while (p.isAlive()) {
//				System.out.println(in.readLine());
//			}
			return p.waitFor();
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -11;
	}
	
	/* Given the original dimension of an image, calculate a smaller dimension which preserves aspect ratio
	 * and fits the total number of pixels within the given limit
	 *
	public Dimension calculateDownscale (Dimension original, int limit) {
		int w = original.width;
		int h = original.height;
		
		Not completed
	}*/
	
	// Helpful messages
	public void complain (String str, Object... arg) {
		if (str == null) str = "%s"; // It is possible to pass only one object of any type
		JOptionPane.showMessageDialog(this, String.format(str, arg), "Error", JOptionPane.ERROR_MESSAGE);
	}

	public void inform (String str, Object... arg) {
		if (str == null) str = "%s"; // It is possible to pass only one object of any type
		JOptionPane.showMessageDialog(this, String.format(str, arg), "Attention", JOptionPane.INFORMATION_MESSAGE);
	}
}

class ImgPanel extends JPanel {

	// Auto-generated UID
	private static final long serialVersionUID = 8936074522181756377L;
	
	// Add a rigid area when constructing the panel
	public ImgPanel (Dimension d) {
		setLayout(new FlowLayout());
		add (Box.createRigidArea(d));
	}
	
	// Image to display; getter & setter for it
	private BufferedImage image;
	public BufferedImage getImage() { return image; }

	public void setImage(BufferedImage image) {
		this.image = image;
		this.repaint();
	}


	@Override
	public void paint (Graphics g) {
		super.paint(g);
		Dimension d = this.getSize();
		
		// Scale the image nicely in the panel
		if (image != null) {
			if (d.width * image.getHeight(null) > d.height * image.getWidth(null)){
				d.width = -1;
			}
			else {
				d.height = -1;
			}
			
			Image i =  image.getScaledInstance(d.width, d.height, Image.SCALE_DEFAULT);
			g.drawImage(i, 0, 0, null);
		}
		
		// If no image, at least draw a box
		else {
			g.drawRect(0, 0, d.width-1, d.height-1);
			g.setColor(Color.red);
			g.drawString("No image", 10, 20);
		}
	}
}