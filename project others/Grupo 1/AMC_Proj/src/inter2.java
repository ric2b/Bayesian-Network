import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.awt.Font;
import java.awt.Color;
import java.awt.SystemColor;
import javax.swing.SwingConstants;


public class inter2 extends JFrame
/*
 * Aplica��o que faz a an�lise dos parametros de um paciente e devolve a probabilidade de este ter ou n�o a doen�a.
 * A aplica��o recupera as informa��es guardadas em disco pela 1a aplica�ao, e calcula e apresenta essas probabilidades.
 */

{

	private JPanel contentPane;
	private JTextField txtPac;
	private JTextField txtT;
	private JTextField txtNT;
	private JLabel lblTer;
	private JLabel lblNoTer;
	private JLabel lblNewLabel;
	
	RW rw = new RW();
	BN bn = new BN();
	amostra a;
	grafoOr g;
	String nParametros;
	String amostra;
	
	//Inicializa��o de vari�veis
	
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					inter2 frame = new inter2();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public inter2() {
		setBackground(Color.RED);
		setTitle("An\u00E1lise do paciente");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 500, 350);
		contentPane = new JPanel();
		contentPane.setBackground(SystemColor.activeCaption);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		//Caixa de texto onde se v�o introduzir os valores dos parametros do paciente a analisar.
		txtPac = new JTextField();
		txtPac.setForeground(Color.BLACK);
		txtPac.setHorizontalAlignment(SwingConstants.CENTER);
		txtPac.setBounds(107, 115, 265, 27);
		contentPane.add(txtPac);
		txtPac.setColumns(10);
		
		//Caixa de txto que apresenta a probabilidade de o paciente ter a doen�a.
		txtT = new JTextField();
		txtT.setBounds(58, 188, 143, 27);
		contentPane.add(txtT);
		txtT.setColumns(10);
		
		//Caixa de txto que apresenta a probabilidade de o paciente n�o ter a doen�a.
		txtNT = new JTextField();
		txtNT.setColumns(10);
		txtNT.setBounds(260, 188, 143, 27);
		contentPane.add(txtNT);
		
		JLabel lblIntroduzaOPaciente = new JLabel("Introduza o paciente (na forma X1,X2,..,Xn)");
		lblIntroduzaOPaciente.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblIntroduzaOPaciente.setBounds(95, 59, 309, 23);
		contentPane.add(lblIntroduzaOPaciente);
		
		//Etiqueta da caixa de texto que apresenta a probabilidade de ter a doen�a.
		lblTer = new JLabel("Probabilidade de ter a doen\u00E7a:");
		lblTer.setBounds(58, 163, 182, 14);
		contentPane.add(lblTer);
		
		//Etiqueta da caixa de texto que apresenta a probabilidade de n�o ter a doen�a.
		lblNoTer = new JLabel("Probabilidade de n\u00E3o ter a doen\u00E7a:");
		lblNoTer.setBounds(250, 163, 224, 14);
		contentPane.add(lblNoTer);
		
		
		
		try {
			rw.LoadBN("documento");
			//Leitura das informa��es guardadas em disco pela primeira aplica��o. 
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		
		
		try {
			a = rw.Reader(rw.path);
			//Reconstru��o da amostra utilizada para criar a rede de bayes na 1a aplica��o. 
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		bn = rw.r; //Apenas atribui os ArrayList's (Rede Bayes e Lista de combina�oes)
		bn.graf = rw.g;
		bn.am = a;
		//Atribui�ao das variaveis globais da rede de bayes.
		
		nParametros = Integer.toString(a.tLength-1);
		//variavel que guarda o numero de parametros dos pacientes da amostra.
		
		amostra = rw.doen�a;
		//variavel que guarda o nome do ficheiro .csv (nome da doen�a).
		
		//Etiqueta que apresenta o nome da doen�a em an�lise.
		lblNewLabel = new JLabel( amostra );
		lblNewLabel.setForeground(Color.WHITE);
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Arial Black", Font.BOLD, 28));
		lblNewLabel.setBounds(124, 21, 224, 27);
		contentPane.add(lblNewLabel);
		
		//Etiqueta que informa o numero de parametros a introduzir pelo utilizador.
		JLabel label = new JLabel("Dever� introduzir " + nParametros + " par�metros");
		label.setFont(new Font("Tahoma", Font.BOLD, 13));
		label.setBounds(136, 77, 224, 27);
		contentPane.add(label);
		
		//Botao "Avaliar"
		JButton btnNewButton_1 = new JButton("Avaliar");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				
				String[] items = txtPac.getText().split(",");
				//Array de strings que guarda os parametros introduzidos pelo utilizador. (Separa a string pelas virgulas e insere no vector)				
				
				if (items.length != a.tLength-1)
				//Guarda: se for introduzido um paciente com um numero de parametros diferente do esperado..
				{
					JOptionPane.showMessageDialog(null,"N�mero de par�metros incorrecto");
					//mostra uma mensagem de erro.
				}
				else
				{
					int[] pac = new int[items.length];

					for (int i = 0; i < items.length; i++) 
					//convers�o do Array de Strings "items" num Array de inteiros "pac" (paciente).
					{
					    try {
					        pac[i] = Integer.parseInt(items[i]);
					    } catch (NumberFormatException nfe) {};
					}
					
					int comp = pac.length;
					
					int[] pac0 = new int[comp+1];
					//Inicializa�ao de um vector igual ao "pac", ao qual ser� acrescentada uma posi��o no final contendo um "0" (classificador), 
					//para posteriormente introduzir no m�todo "probs" da Rede de Bayes.
					int[] pac1 = new int[comp+1];
					//Inicializa�ao de um vector igual ao "pac", ao qual ser� acrescentada uma posi��o no final contendo um "1" (classificador), 
					//para posteriormente introduzir no m�todo "probs" da Rede de Bayes.
					
					//Constru��o dos vectores pac0 e pac1
					for (int j=0; j<comp; j++)
					{
						pac0[j]=pac[j];
						pac1[j]=pac[j];
					}
					
					pac0[comp]=0;
					pac1[comp]=1;
					
					//"Probs" de p0 e p1.
					Double p0 = bn.prob(pac0);
					Double p1 = bn.prob(pac1);
		
					
					if (p0==null || p1==null)
					/*Guarda: se o m�todo "probs" retornar null (por alguns dos parametros introduzidos pelo utilizador 
					*	exceder o dominio desse paramatro na amostra)...
					*/		
					{
						JOptionPane.showMessageDialog(null,"Par�metros fora do dominio");
						//apresenta-se uma mensagem de erro.
					}
					else
					//caso contrario, os valores das probabilidades (de ter e n�o ter a doen�a) s�o normalizados e apresentados nas respectivas caixas de texto.
					{
						txtT.setText(Double.toString((p1/(p1+p0))*100));
						txtNT.setText(Double.toString((p0/(p1+p0))*100));
					}
				}

				
			}
		});
		btnNewButton_1.setBounds(190, 243, 102, 36);
		contentPane.add(btnNewButton_1);
		
	}
}
