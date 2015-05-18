package userinterface;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;
import java.awt.Font;
import javax.swing.SwingConstants;
import java.awt.GridLayout;
import javax.swing.BoxLayout;
//import com.jgoodies.forms.layout.FormLayout;
//import com.jgoodies.forms.layout.ColumnSpec;
//import com.jgoodies.forms.layout.RowSpec;
import java.awt.Dimension;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.SystemColor;
import javax.swing.JRadioButton;
import javax.swing.JToggleButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import java.awt.Component;
import javax.swing.DebugGraphics;

public class GUI {

	private JFrame frame;
	private JTextField txtHola;
	private JTextField txtFilenamecsv;
	private JTextField txtScore;
	private JTextField txtTestFile;
	private JTextField textField_1;
	private JTextField txtRandomRestarts;
	private JTextField txtInferir;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
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
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setFont(new Font("Segoe UI Light", Font.PLAIN, 12));
		frame.setLocationByPlatform(true);
		frame.setMaximumSize(new Dimension(400, 400));
		frame.setMinimumSize(new Dimension(400, 400));
		frame.getContentPane().setLayout(null);
		
		txtFilenamecsv = new JTextField();
		txtFilenamecsv.setEditable(false);
		txtFilenamecsv.setOpaque(false);
		txtFilenamecsv.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 14));
		txtFilenamecsv.setBackground(SystemColor.activeCaptionBorder);
		txtFilenamecsv.setText("filename.csv");
		txtFilenamecsv.setBounds(94, 17, 206, 20);
		frame.getContentPane().add(txtFilenamecsv);
		txtFilenamecsv.setColumns(10);
		
		JRadioButton rdbtnLl = new JRadioButton("LL");
		rdbtnLl.setToolTipText("log-likelyhood scoring");
		rdbtnLl.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 12));
		rdbtnLl.setRequestFocusEnabled(false);
		rdbtnLl.setBounds(94, 56, 43, 23);
		frame.getContentPane().add(rdbtnLl);
		
		JRadioButton rdbtnMdl = new JRadioButton("MDL");
		rdbtnMdl.setToolTipText("minimum description length scoring");
		rdbtnMdl.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 12));
		rdbtnMdl.setRequestFocusEnabled(false);
		rdbtnMdl.setBounds(139, 56, 55, 23);
		frame.getContentPane().add(rdbtnMdl);
		
		JButton btnOpen = new JButton("open");
		btnOpen.setToolTipText("select the csv file for training");
		btnOpen.setRequestFocusEnabled(false);
		btnOpen.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 12));
		btnOpen.setBounds(310, 16, 61, 23);
		frame.getContentPane().add(btnOpen);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(-19, 189, 403, 2);
		frame.getContentPane().add(separator);
		
		textField_1 = new JTextField();
		textField_1.setEditable(false);
		textField_1.setOpaque(false);
		textField_1.setText("filename.csv");
		textField_1.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 14));
		textField_1.setColumns(10);
		textField_1.setBackground(SystemColor.activeCaptionBorder);
		textField_1.setBounds(94, 202, 206, 20);
		frame.getContentPane().add(textField_1);
		
		JButton button = new JButton("open");
		button.setToolTipText("select the csv file for testing");
		button.setRequestFocusEnabled(false);
		button.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 12));
		button.setBounds(310, 201, 61, 23);
		frame.getContentPane().add(button);
		
		JSpinner spinner = new JSpinner();
		spinner.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 12));
		spinner.setModel(new SpinnerNumberModel(new Integer(10), new Integer(0), null, new Integer(1)));
		spinner.setBounds(328, 58, 43, 18);
		frame.getContentPane().add(spinner);
		
		JRadioButton rdbtnTodas = new JRadioButton("all variables");
		rdbtnTodas.setToolTipText("infer about all random variables");
		rdbtnTodas.setOpaque(false);
		rdbtnTodas.setRequestFocusEnabled(false);
		rdbtnTodas.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 14));
		rdbtnTodas.setBounds(94, 236, 100, 23);
		frame.getContentPane().add(rdbtnTodas);
		
		JSpinner spinner_1 = new JSpinner();
		spinner_1.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 12));
		spinner_1.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1), null, new Integer(1)));
		spinner_1.setBounds(172, 265, 43, 18);
		frame.getContentPane().add(spinner_1);
		
		JButton btnStart = new JButton("start");
		btnStart.setToolTipText("start the inference computation");
		btnStart.setRequestFocusEnabled(false);
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btnStart.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 16));
		btnStart.setBounds(152, 312, 79, 38);
		frame.getContentPane().add(btnStart);
		
		txtRandomRestarts = new JTextField();
		txtRandomRestarts.setOpaque(false);
		txtRandomRestarts.setRequestFocusEnabled(false);
		txtRandomRestarts.setText("random restarts: ");
		txtRandomRestarts.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 14));
		txtRandomRestarts.setColumns(10);
		txtRandomRestarts.setBorder(null);
		txtRandomRestarts.setBackground(SystemColor.activeCaptionBorder);
		txtRandomRestarts.setBounds(218, 51, 118, 29);
		frame.getContentPane().add(txtRandomRestarts);
		
		JRadioButton rdbtnEscolher = new JRadioButton("choose:");
		rdbtnEscolher.setToolTipText("infer about a specific random variable");
		rdbtnEscolher.setOpaque(false);
		rdbtnEscolher.setRequestFocusEnabled(false);
		rdbtnEscolher.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 14));
		rdbtnEscolher.setBounds(94, 261, 83, 23);
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
		
		JButton btnBuild = new JButton("build");
		btnBuild.setToolTipText("build the bayesian network");
		btnBuild.setDebugGraphicsOptions(DebugGraphics.NONE_OPTION);
		btnBuild.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnBuild.setRequestFocusEnabled(false);
		btnBuild.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btnBuild.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 16));
		btnBuild.setBounds(152, 140, 79, 38);
		frame.getContentPane().add(btnBuild);
	}
}
