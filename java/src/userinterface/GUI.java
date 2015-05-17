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
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import java.awt.Dimension;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.SystemColor;
import javax.swing.JRadioButton;
import javax.swing.JToggleButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

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
		frame.setLocationByPlatform(true);
		frame.setMaximumSize(new Dimension(400, 360));
		frame.setMinimumSize(new Dimension(400, 360));
		frame.getContentPane().setLayout(null);
		
		txtFilenamecsv = new JTextField();
		txtFilenamecsv.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 11));
		txtFilenamecsv.setBackground(SystemColor.activeCaptionBorder);
		txtFilenamecsv.setText("filename.csv");
		txtFilenamecsv.setBounds(94, 17, 206, 20);
		frame.getContentPane().add(txtFilenamecsv);
		txtFilenamecsv.setColumns(10);
		
		JRadioButton rdbtnLl = new JRadioButton("LL");
		rdbtnLl.setBounds(94, 56, 43, 23);
		frame.getContentPane().add(rdbtnLl);
		
		JRadioButton rdbtnMdl = new JRadioButton("MDL");
		rdbtnMdl.setBounds(139, 56, 55, 23);
		frame.getContentPane().add(rdbtnMdl);
		
		JButton btnBuild = new JButton("build");
		btnBuild.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 12));
		btnBuild.setBounds(170, 95, 61, 23);
		frame.getContentPane().add(btnBuild);
		
		JButton btnOpen = new JButton("open");
		btnOpen.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 12));
		btnOpen.setBounds(310, 16, 61, 23);
		frame.getContentPane().add(btnOpen);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(0, 143, 403, 2);
		frame.getContentPane().add(separator);
		
		textField_1 = new JTextField();
		textField_1.setText("filename.csv");
		textField_1.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 11));
		textField_1.setColumns(10);
		textField_1.setBackground(SystemColor.activeCaptionBorder);
		textField_1.setBounds(94, 162, 206, 20);
		frame.getContentPane().add(textField_1);
		
		JButton button = new JButton("open");
		button.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 12));
		button.setBounds(310, 161, 61, 23);
		frame.getContentPane().add(button);
		
		JSpinner spinner = new JSpinner();
		spinner.setModel(new SpinnerNumberModel(new Integer(10), null, null, new Integer(1)));
		spinner.setBounds(139, 218, 37, 18);
		frame.getContentPane().add(spinner);
		
		JRadioButton rdbtnTodas = new JRadioButton("todas");
		rdbtnTodas.setBounds(239, 216, 61, 23);
		frame.getContentPane().add(rdbtnTodas);
		
		JSpinner spinner_1 = new JSpinner();
		spinner_1.setBounds(310, 249, 37, 18);
		frame.getContentPane().add(spinner_1);
		
		JButton btnStart = new JButton("start");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btnStart.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 12));
		btnStart.setBounds(170, 290, 61, 23);
		frame.getContentPane().add(btnStart);
		
		txtRandomRestarts = new JTextField();
		txtRandomRestarts.setText("random restarts: ");
		txtRandomRestarts.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 14));
		txtRandomRestarts.setColumns(10);
		txtRandomRestarts.setBorder(null);
		txtRandomRestarts.setBackground(SystemColor.activeCaptionBorder);
		txtRandomRestarts.setBounds(29, 211, 118, 29);
		frame.getContentPane().add(txtRandomRestarts);
		
		JRadioButton rdbtnEscolher = new JRadioButton("escolher");
		rdbtnEscolher.setBounds(239, 247, 65, 23);
		frame.getContentPane().add(rdbtnEscolher);
		
		txtInferir = new JTextField();
		txtInferir.setText("inferir: ");
		txtInferir.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 14));
		txtInferir.setColumns(10);
		txtInferir.setBorder(null);
		txtInferir.setBackground(SystemColor.activeCaptionBorder);
		txtInferir.setBounds(186, 211, 55, 29);
		frame.getContentPane().add(txtInferir);
		
		txtScore = new JTextField();
		txtScore.setText("score: ");
		txtScore.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 14));
		txtScore.setColumns(10);
		txtScore.setBorder(null);
		txtScore.setBackground(SystemColor.activeCaptionBorder);
		txtScore.setBounds(29, 51, 55, 29);
		frame.getContentPane().add(txtScore);
		
		txtHola = new JTextField();
		txtHola.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 14));
		txtHola.setBorder(null);
		txtHola.setBackground(SystemColor.activeCaptionBorder);
		txtHola.setText("train file: ");
		txtHola.setBounds(29, 11, 55, 29);
		frame.getContentPane().add(txtHola);
		txtHola.setColumns(10);
		
		txtTestFile = new JTextField();
		txtTestFile.setText("test file: ");
		txtTestFile.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 14));
		txtTestFile.setColumns(10);
		txtTestFile.setBorder(null);
		txtTestFile.setBackground(SystemColor.activeCaptionBorder);
		txtTestFile.setBounds(29, 156, 55, 29);
		frame.getContentPane().add(txtTestFile);
	}
}
