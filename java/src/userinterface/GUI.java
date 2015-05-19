package userinterface;

import main.*;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.awt.Dimension;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import java.awt.SystemColor;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import java.awt.Component;
import javax.swing.DebugGraphics;
import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.UIManager;
import bayessian.RandomVariable;
import bayessian.TransitionBayessianNetwork;
import java.io.File;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JCheckBox;
import java.awt.Toolkit;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.awt.Color;
import javax.swing.SwingConstants;

public class GUI {

	private JFrame frame;
	private JTextField textField_2;
	private JTextField textField_1;
	private JTextField txtSeconds;
	private JTextField txtSeconds_1;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private final ButtonGroup buttonGroup_1 = new ButtonGroup();
	
	private static boolean LL;
	private static boolean allVars;
	private static boolean MDL;
	
	private JButton btnStart;
	private JSpinner spinner_1;
	private JCheckBox chckbxSaveToFile;
	private JCheckBox checkBox;
	private JCheckBox chckbxTurboMode;
	
	TransitionBayessianNetwork<RandomVariable> transitionBN = null;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		LL = true;
		MDL = false;
		allVars = true;
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Throwable e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI window = new GUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUI() {
		btnStart = new JButton("");
		btnStart.setDisabledIcon(new ImageIcon(GUI.class.getResource("/userinterface/start.grey.png")));
		btnStart.setIcon(new ImageIcon(GUI.class.getResource("/userinterface/start.png")));
		btnStart.setOpaque(false);
		btnStart.setBorder(null);
		btnStart.setEnabled(false);	
		txtSeconds = new JTextField();
		txtSeconds.setVisible(false);
		txtSeconds.setText("2 seconds");
		txtSeconds.setBorder(null);
		spinner_1 = new JSpinner();
		spinner_1.setEnabled(false);
		textField_1 = new JTextField();
		textField_1.setForeground(Color.DARK_GRAY);
		textField_1.setBorder(null);
		textField_1.setText("filename.csv");
		textField_2 = new JTextField();
		textField_2.setAutoscrolls(false);
		textField_2.setHorizontalAlignment(SwingConstants.LEFT);
		textField_2.setForeground(Color.DARK_GRAY);
		textField_2.setBorder(null);
		textField_2.setText("filename.csv");
		chckbxSaveToFile = new JCheckBox("");
		chckbxSaveToFile.setToolTipText("save the results to the output file \"transition.txt\"");
		chckbxSaveToFile.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 13));
		checkBox = new JCheckBox("");
		chckbxTurboMode = new JCheckBox("turbo mode");
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
				
		frame = new JFrame();
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(GUI.class.getResource("/userinterface/icon.png")));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.getContentPane().setFont(new Font("Segoe UI Light", Font.PLAIN, 12));
		frame.setLocationByPlatform(true);
		frame.setMaximumSize(new Dimension(390, 425));
		frame.setMinimumSize(new Dimension(390, 425));
		frame.getContentPane().setLayout(null);
		
		JRadioButton rdbtnLl = new JRadioButton("");
		rdbtnLl.setOpaque(false);
		rdbtnLl.setFocusable(false);
		rdbtnLl.setSelected(true);
		buttonGroup.add(rdbtnLl);
		rdbtnLl.setToolTipText("log-likelyhood scoring");
		rdbtnLl.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 12));
		rdbtnLl.setRequestFocusEnabled(false);
		rdbtnLl.setBounds(79, 66, 43, 23);
		rdbtnLl.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				LL = true;
				MDL = false;
			}
		});
		frame.getContentPane().add(rdbtnLl);
		
		JRadioButton rdbtnMdl = new JRadioButton("");
		rdbtnMdl.setOpaque(false);
		buttonGroup.add(rdbtnMdl);
		rdbtnMdl.setToolTipText("minimum description length scoring");
		rdbtnMdl.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 12));
		rdbtnMdl.setRequestFocusEnabled(false);
		rdbtnMdl.setBounds(122, 66, 55, 23);
		rdbtnMdl.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				LL = false;
				MDL = true;
			}
		});
		frame.getContentPane().add(rdbtnMdl);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(-19, 189, 403, 2);
		frame.getContentPane().add(separator);
		
		final JSpinner spinner = new JSpinner();
		spinner.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 12));
		spinner.setModel(new SpinnerNumberModel(new Integer(0), new Integer(0), null, new Integer(1)));
		spinner.setBounds(305, 68, 43, 18);
		frame.getContentPane().add(spinner);
		
		JRadioButton rdbtnTodas = new JRadioButton("");
		rdbtnTodas.setSelected(true);
		buttonGroup_1.add(rdbtnTodas);
		rdbtnTodas.setToolTipText("infer about all random variables");
		rdbtnTodas.setOpaque(false);
		rdbtnTodas.setRequestFocusEnabled(false);
		rdbtnTodas.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 14));
		rdbtnTodas.setBounds(99, 247, 100, 23);
		rdbtnTodas.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				spinner_1.setEnabled(false);
				allVars = true;
			}
		});
		frame.getContentPane().add(rdbtnTodas);
		
		spinner_1.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 12));
		spinner_1.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1), null, new Integer(1)));
		spinner_1.setBounds(170, 275, 43, 18);
		frame.getContentPane().add(spinner_1);
		
		JRadioButton rdbtnEscolher = new JRadioButton("");
		buttonGroup_1.add(rdbtnEscolher);
		rdbtnEscolher.setToolTipText("infer about a specific random variable");
		rdbtnEscolher.setOpaque(false);
		rdbtnEscolher.setRequestFocusEnabled(false);
		rdbtnEscolher.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 14));
		rdbtnEscolher.setBounds(99, 273, 83, 23);
		rdbtnEscolher.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				spinner_1.setEnabled(true);
				allVars = false;
			}
		});
		frame.getContentPane().add(rdbtnEscolher);
		
		JButton button = new JButton("");
		button.setBorder(null);
		button.setOpaque(false);
		button.setIcon(new ImageIcon(GUI.class.getResource("/userinterface/open.png")));
		final JFileChooser fileChooserTest = new JFileChooser();
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				fileChooserTest.setCurrentDirectory(new File(System.getProperty("user.home")));
			      int rVal = fileChooserTest.showOpenDialog(fileChooserTest);
			      if (rVal == JFileChooser.APPROVE_OPTION) {
			        textField_1.setText(fileChooserTest.getSelectedFile().getName());
			      }
			}
		});
		
		button.setToolTipText("select the csv file for testing");
		button.setRequestFocusEnabled(false);
		button.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 12));
		button.setBounds(320, 206, 43, 29);
		frame.getContentPane().add(button);
		
		JButton button_1 = new JButton("");
		button_1.setBorder(null);
		button_1.setOpaque(false);
		button_1.setIcon(new ImageIcon(GUI.class.getResource("/userinterface/open.png")));
		final JFileChooser fileChooserTrain = new JFileChooser();
		button_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				fileChooserTrain.setCurrentDirectory(new File(System.getProperty("user.home")));
			      int rVal = fileChooserTrain.showOpenDialog(fileChooserTrain);
			      if (rVal == JFileChooser.APPROVE_OPTION) {
			    	  textField_2.setText(fileChooserTrain.getSelectedFile().getName());
			      }
			}
		});
		button_1.setToolTipText("select the csv file for training");
		button_1.setRequestFocusEnabled(false);
		button_1.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 12));
		button_1.setBounds(320, 21, 44, 29);
		frame.getContentPane().add(button_1);
		
		
		chckbxSaveToFile.setOpaque(false);
		chckbxSaveToFile.setFocusable(false);
		chckbxSaveToFile.setBounds(32, 109, 97, 23);
		frame.getContentPane().add(chckbxSaveToFile);
		
		JButton btnBuild = new JButton("");
		btnBuild.setBorder(null);
		btnBuild.setOpaque(false);
		btnBuild.setIcon(new ImageIcon(GUI.class.getResource("/userinterface/build.png")));
		btnBuild.setToolTipText("build the bayesian network");
		btnBuild.setDebugGraphicsOptions(DebugGraphics.NONE_OPTION);
		btnBuild.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnBuild.setRequestFocusEnabled(false);
		btnBuild.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 16));
		btnBuild.setBounds(138, 145, 109, 38);
		btnBuild.setActionCommand("enable");
		btnBuild.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					SaveToFile out = new SaveToFile("transition.txt", chckbxSaveToFile.isSelected());
					
					if(LL == true) {
						if(chckbxTurboMode.isSelected()) {
							out.println("Parameters: " + textField_2.getText() + " LL " + (Integer)spinner.getValue(), chckbxSaveToFile.isSelected());	
							transitionBN = Main.buildDBN(textField_2.getText(), "FLL", (Integer)spinner.getValue(), out, chckbxSaveToFile.isSelected());
						} else{
							out.println("Parameters: " + textField_2.getText() + " LL " + (Integer)spinner.getValue(), chckbxSaveToFile.isSelected());	
							transitionBN = Main.buildDBN(textField_2.getText(), "LL", (Integer)spinner.getValue(), out, chckbxSaveToFile.isSelected());
						}
					}
					else if(MDL == true) {
						if(chckbxTurboMode.isSelected()) {
							out.println("Parameters: " + textField_2.getText() + " MDL " + (Integer)spinner.getValue(), chckbxSaveToFile.isSelected());
							transitionBN = Main.buildDBN(textField_2.getText(), "FMDL", (Integer)spinner.getValue(), out, chckbxSaveToFile.isSelected());
						} else {
							out.println("Parameters: " + textField_2.getText() + " MDL " + (Integer)spinner.getValue(), chckbxSaveToFile.isSelected());
							transitionBN = Main.buildDBN(textField_2.getText(), "MDL", (Integer)spinner.getValue(), out, chckbxSaveToFile.isSelected());
						}	
					}
					if ("enable".equals(e.getActionCommand())) {
						btnStart.setEnabled(true);
						txtSeconds.setVisible(true);
						txtSeconds.setText(Double.toString(Main.elapsedTimeBN / 1000000000.0).substring(0,9)+'s');
				    } else {
				    	btnStart.setEnabled(false);
				    }
					
					out.close();
			} 
		});
		frame.getContentPane().add(btnBuild);
		
		btnStart.setToolTipText("start the inference computation");
		btnStart.setRequestFocusEnabled(false);
		btnStart.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 16));
		btnStart.setBounds(147, 356, 100, 38);
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					SaveToFile out = new SaveToFile("inferedValues.txt", checkBox.isSelected());
					
					if(allVars == true) {
						Main.infereValue(textField_1.getText(), true, 0, transitionBN, out, checkBox.isSelected());		
					}
					else {
						Main.infereValue(textField_1.getText(), false, (Integer)spinner_1.getValue(), transitionBN, out, checkBox.isSelected());	
					}
					txtSeconds_1.setVisible(true);
					txtSeconds_1.setText(Double.toString(Main.elapsedTimeInfere / 1000000000.0).substring(0,9)+'s');					
					out.close();
			} 
		});
		frame.getContentPane().add(btnStart);
		
		textField_2.setOpaque(false);
		textField_2.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 28));
		textField_2.setEditable(false);
		textField_2.setColumns(10);
		textField_2.setBackground(SystemColor.activeCaptionBorder);
		textField_2.setBounds(138, 11, 177, 38);
		frame.getContentPane().add(textField_2);
		
		textField_1.setOpaque(false);
		textField_1.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 28));
		textField_1.setEditable(false);
		textField_1.setColumns(10);
		textField_1.setBackground(SystemColor.activeCaptionBorder);
		textField_1.setBounds(140, 199, 167, 33);
		frame.getContentPane().add(textField_1);
		
		txtSeconds.setOpaque(false);
		txtSeconds.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 14));
		txtSeconds.setEditable(false);
		txtSeconds.setColumns(10);
		txtSeconds.setBackground(SystemColor.activeCaptionBorder);
		txtSeconds.setBounds(265, 107, 83, 20);
		frame.getContentPane().add(txtSeconds);
		
		txtSeconds_1 = new JTextField();
		txtSeconds_1.setVisible(false);
		txtSeconds_1.setText("2 seconds");
		txtSeconds_1.setOpaque(false);
		txtSeconds_1.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 14));
		txtSeconds_1.setBorder(null);
		txtSeconds_1.setEditable(false);
		txtSeconds_1.setColumns(10);
		txtSeconds_1.setBackground(SystemColor.activeCaptionBorder);
		txtSeconds_1.setBounds(265, 313, 83, 20);
		frame.getContentPane().add(txtSeconds_1);		
		
		checkBox.setToolTipText("save the results to the output file \"inferedValues.txt\"");
		checkBox.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 13));
		checkBox.setOpaque(false);
		checkBox.setFocusable(false);
		checkBox.setBounds(29, 315, 97, 23);
		frame.getContentPane().add(checkBox);
		
		
		chckbxTurboMode.setToolTipText("lots of memory used, might crash");
		chckbxTurboMode.setFont(new Font("Tahoma", Font.PLAIN, 12));
		chckbxTurboMode.setOpaque(false);
		chckbxTurboMode.setBounds(253, 146, 97, 23);
		frame.getContentPane().add(chckbxTurboMode);
		
		JLabel lblNewLabel = new JLabel("New label");
		lblNewLabel.setIcon(new ImageIcon(GUI.class.getResource("/userinterface/gui.png")));
		lblNewLabel.setBounds(0, 11, 384, 400);
		frame.getContentPane().add(lblNewLabel);
		
		JCheckBox chckbxSuperSpeed = new JCheckBox("super speed");
		chckbxSuperSpeed.setBounds(255, 158, 129, 23);
		frame.getContentPane().add(chckbxSuperSpeed);
	}
}
