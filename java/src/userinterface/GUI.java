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
import javax.swing.JProgressBar;
import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.UIManager;
import bayessian.RandomVariable;
import bayessian.TransitionBayessianNetwork;
import java.io.File;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GUI {

	private JFrame frame;
	private JTextField txtHola;
	private JTextField txtScore;
	private JTextField txtTestFile;
	private JTextField txtRandomRestarts;
	private JTextField txtInferir;
	private JTextField txtTotalTime;
	private JTextField textField;
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
		btnStart = new JButton("start");
		btnStart.setEnabled(false);	
		txtSeconds = new JTextField();
		txtSeconds.setBorder(null);
		txtSeconds.setVisible(false);
		spinner_1 = new JSpinner();
		spinner_1.setEnabled(false);
		textField_1 = new JTextField();
		textField_1.setText("filename.csv");
		textField_2 = new JTextField();
		textField_2.setText("filename.csv");
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
				
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.getContentPane().setFont(new Font("Segoe UI Light", Font.PLAIN, 12));
		frame.setLocationByPlatform(true);
		frame.setMaximumSize(new Dimension(390, 435));
		frame.setMinimumSize(new Dimension(390, 425));
		frame.getContentPane().setLayout(null);
		
		JRadioButton rdbtnLl = new JRadioButton("LL");
		rdbtnLl.setSelected(true);
		buttonGroup.add(rdbtnLl);
		rdbtnLl.setToolTipText("log-likelyhood scoring");
		rdbtnLl.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 12));
		rdbtnLl.setRequestFocusEnabled(false);
		rdbtnLl.setBounds(75, 56, 43, 23);
		rdbtnLl.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				LL = true;
				MDL = false;
			}
		});
		frame.getContentPane().add(rdbtnLl);
		
		JRadioButton rdbtnMdl = new JRadioButton("MDL");
		buttonGroup.add(rdbtnMdl);
		rdbtnMdl.setToolTipText("minimum description length scoring");
		rdbtnMdl.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 12));
		rdbtnMdl.setRequestFocusEnabled(false);
		rdbtnMdl.setBounds(120, 56, 55, 23);
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
		spinner.setBounds(304, 58, 43, 18);
		frame.getContentPane().add(spinner);
		
		JRadioButton rdbtnTodas = new JRadioButton("all variables");
		rdbtnTodas.setSelected(true);
		buttonGroup_1.add(rdbtnTodas);
		rdbtnTodas.setToolTipText("infer about all random variables");
		rdbtnTodas.setOpaque(false);
		rdbtnTodas.setRequestFocusEnabled(false);
		rdbtnTodas.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 14));
		rdbtnTodas.setBounds(94, 236, 100, 23);
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
		spinner_1.setBounds(172, 265, 43, 18);
		frame.getContentPane().add(spinner_1);
		
		txtRandomRestarts = new JTextField();
		txtRandomRestarts.setOpaque(false);
		txtRandomRestarts.setRequestFocusEnabled(false);
		txtRandomRestarts.setText("random restarts: ");
		txtRandomRestarts.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 14));
		txtRandomRestarts.setColumns(10);
		txtRandomRestarts.setBorder(null);
		txtRandomRestarts.setBackground(SystemColor.activeCaptionBorder);
		txtRandomRestarts.setBounds(194, 51, 118, 29);
		frame.getContentPane().add(txtRandomRestarts);
		
		JRadioButton rdbtnEscolher = new JRadioButton("choose:");
		buttonGroup_1.add(rdbtnEscolher);
		rdbtnEscolher.setToolTipText("infer about a specific random variable");
		rdbtnEscolher.setOpaque(false);
		rdbtnEscolher.setRequestFocusEnabled(false);
		rdbtnEscolher.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 14));
		rdbtnEscolher.setBounds(94, 261, 83, 23);
		rdbtnEscolher.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				spinner_1.setEnabled(true);
				allVars = false;
			}
		});
		frame.getContentPane().add(rdbtnEscolher);
		
		txtInferir = new JTextField();
		txtInferir.setFocusable(false);
		txtInferir.setOpaque(false);
		txtInferir.setText("infer: ");
		txtInferir.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 14));
		txtInferir.setColumns(10);
		txtInferir.setBorder(null);
		txtInferir.setBackground(SystemColor.activeCaptionBorder);
		txtInferir.setBounds(29, 233, 55, 29);
		frame.getContentPane().add(txtInferir);
		
		txtScore = new JTextField();
		txtScore.setOpaque(false);
		txtScore.setRequestFocusEnabled(false);
		txtScore.setText("score: ");
		txtScore.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 14));
		txtScore.setColumns(10);
		txtScore.setBorder(null);
		txtScore.setBackground(SystemColor.activeCaptionBorder);
		txtScore.setBounds(29, 51, 55, 29);
		frame.getContentPane().add(txtScore);
		
		txtHola = new JTextField();
		txtHola.setFocusable(false);
		txtHola.setOpaque(false);
		txtHola.setRequestFocusEnabled(false);
		txtHola.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 14));
		txtHola.setBorder(null);
		txtHola.setText("train file: ");
		txtHola.setBounds(29, 11, 61, 29);
		frame.getContentPane().add(txtHola);
		txtHola.setColumns(10);
		
		txtTestFile = new JTextField();
		txtTestFile.setOpaque(false);
		txtTestFile.setRequestFocusEnabled(false);
		txtTestFile.setText("test file: ");
		txtTestFile.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 14));
		txtTestFile.setColumns(10);
		txtTestFile.setBorder(null);
		txtTestFile.setBackground(SystemColor.activeCaptionBorder);
		txtTestFile.setBounds(29, 196, 55, 29);
		frame.getContentPane().add(txtTestFile);
		
		JButton button = new JButton("open");
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
		button.setBounds(287, 200, 61, 22);
		frame.getContentPane().add(button);
		
		JButton button_1 = new JButton("open");
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
		button_1.setBounds(286, 14, 61, 22);
		frame.getContentPane().add(button_1);
		
		JButton btnBuild = new JButton("build");
		btnBuild.setToolTipText("build the bayesian network");
		btnBuild.setDebugGraphicsOptions(DebugGraphics.NONE_OPTION);
		btnBuild.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnBuild.setRequestFocusEnabled(false);
		btnBuild.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 16));
		btnBuild.setBounds(152, 140, 79, 38);
		btnBuild.setActionCommand("enable");
		btnBuild.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					SaveToFile out = null;	
					boolean toFile = true;
					if(toFile)
						out = new SaveToFile("transitionBN.out");
					
					if(LL == true) {
						out.println("Parameters: " + textField_2.getText() + " LL " + (Integer)spinner.getValue(), toFile);	
						transitionBN = Main.buildDBN(textField_2.getText(), "LL", (Integer)spinner.getValue(), out);			
					}
					else if(MDL == true) {
						out.println("Parameters: " + textField_2.getText() + " MDL " + (Integer)spinner.getValue(), toFile);
						transitionBN = Main.buildDBN(textField_2.getText(), "MDL", (Integer)spinner.getValue(), out);	
					}
					if ("enable".equals(e.getActionCommand())) {
						btnStart.setEnabled(true);
						txtSeconds.setVisible(true);
						txtSeconds.setText(Double.toString((Main.elapsedTimeBN/1000000)*Math.pow(10, -9)));
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
		btnStart.setBounds(152, 347, 79, 38);
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					SaveToFile out = null;	
					boolean toFile = true;
					if(toFile)
						out = new SaveToFile("inferedValues.out");	
					if(allVars == true) {
						Main.infereValue(textField_1.getText(), true, 0, transitionBN, out);		
					}
					else {
						Main.infereValue(textField_1.getText(), false, (Integer)spinner_1.getValue(), transitionBN, out);	
					}
					txtSeconds_1.setVisible(true);
					txtSeconds_1.setText(String.format("%s",  Main.elapsedTimeInfere*Math.pow(10, -9)));					
					out.close();
			} 
		});
		frame.getContentPane().add(btnStart);
		
		JProgressBar progressBar = new JProgressBar();
		progressBar.setValue(30);
		progressBar.setBounds(29, 103, 146, 14);
		frame.getContentPane().add(progressBar);
		
		txtTotalTime = new JTextField();
		txtTotalTime.setToolTipText("time to calculate (seconds)");
		txtTotalTime.setText("total time: ");
		txtTotalTime.setRequestFocusEnabled(false);
		txtTotalTime.setOpaque(false);
		txtTotalTime.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 14));
		txtTotalTime.setFocusable(false);
		txtTotalTime.setColumns(10);
		txtTotalTime.setBorder(null);
		txtTotalTime.setBounds(185, 97, 79, 20);
		frame.getContentPane().add(txtTotalTime);
		
		JProgressBar progressBar_1 = new JProgressBar();
		progressBar_1.setValue(70);
		progressBar_1.setBounds(29, 309, 146, 14);
		frame.getContentPane().add(progressBar_1);
		
		textField = new JTextField();
		textField.setToolTipText("time to calculate (seconds)");
		textField.setText("total time: ");
		textField.setRequestFocusEnabled(false);
		textField.setOpaque(false);
		textField.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 14));
		textField.setFocusable(false);
		textField.setColumns(10);
		textField.setBorder(null);
		textField.setBounds(185, 303, 79, 20);
		frame.getContentPane().add(textField);
		
		textField_2.setOpaque(false);
		textField_2.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 14));
		textField_2.setEditable(false);
		textField_2.setColumns(10);
		textField_2.setBackground(SystemColor.activeCaptionBorder);
		textField_2.setBounds(89, 15, 189, 20);
		frame.getContentPane().add(textField_2);
		
		textField_1.setOpaque(false);
		textField_1.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 14));
		textField_1.setEditable(false);
		textField_1.setColumns(10);
		textField_1.setBackground(SystemColor.activeCaptionBorder);
		textField_1.setBounds(90, 201, 189, 20);
		frame.getContentPane().add(textField_1);
		
		txtSeconds.setOpaque(false);
		txtSeconds.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 14));
		txtSeconds.setEditable(false);
		txtSeconds.setColumns(10);
		txtSeconds.setBackground(SystemColor.activeCaptionBorder);
		txtSeconds.setBounds(253, 97, 94, 20);
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
		txtSeconds_1.setBounds(253, 303, 94, 20);
		frame.getContentPane().add(txtSeconds_1);
	}
}
