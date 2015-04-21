import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.SystemColor;
import javax.swing.SwingConstants;


public class inter1 extends JFrame 
/*Aplic��o que faz a aprendizagem.
 * O utilizador pesquisa a localiza��o do ficheiro .csv que vai gerar a amostra,
 * introduz o numero m�ximo de pais por parametro e o numero de grafos aleatorios
 * com que pretende realizar a aprendizagem. Obtido o melhor grafo, cria-se a Rede de Bayes,
 * que vai ser guardada no disco para ser utilizada posteriormente na 2a aplica��o.
 * (o grafo utilizado, o caminho para o ficheiro .csv, e o nome da amostra tamb�m sao guardados no disco)
 * 
 * Finalmente, estima-se a efic�cia da Rede de Bayes criada, recorrendo aos pacientes da amostra, dos quais j� � conhecido
 * o valor do classificador (0 - nao tem a doen�a; 1 - tem a doen�a). Analisando os parametros de um paciente da amostra, 
 * do qual se sabe que o seu classificador apresenta, por exemplo, o valor 1, j� se sabe � priori que o a probabilidade de ter a 
 * doen�a deveria ser superior a 50%. 
 * Analisando todos os elementos da amostra, contamos o numero de vezes que os pacientes foram analisados correctamente,
 * estimando desta forma a efic�cia do diagn�stico de futuros pacientes cujos classificadores s�o desconhecidos.
 * (o valor da efic�cia � apresentado na consola, quando o projecto est� a ser corrido no ambiente Eclipse. 
 *  Tamb�m � apresentado na consola o grafo que o "greedy" devolve e que foi utilizado para gerar a Rede de Bayes.)
 */
{

	private JPanel contentPane;
	private JTextField txtPath;
	private JTextField txtPais;
	private JTextField txtGrafs;
	private JButton btnNewButton_1;
	String caminho;
	String doen�a;
	
	amostra a = new amostra();
	grafoOr g;
	BN bn;
	RW rw = new RW();
	
	//Inicializa��o de variaveis.
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					inter1 frame = new inter1();
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
	public inter1() 
	{
		setFont(new Font("Stencil", Font.PLAIN, 18));
		setTitle("Aprendizagem");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBackground(SystemColor.activeCaption);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		//Caixa de texto onde se vai apresentar o caminho para o ficheiro .csv
		txtPath = new JTextField();
		txtPath.setFont(new Font("Arial", Font.PLAIN, 12));
		txtPath.setBounds(26, 51, 270, 29);
		contentPane.add(txtPath);
		txtPath.setColumns(10);
		
		//Caixa de texto onde introduzir o n�mero de pais
		txtPais = new JTextField();
		txtPais.setHorizontalAlignment(SwingConstants.CENTER);
		txtPais.setBounds(115, 145, 50, 29);
		contentPane.add(txtPais);
		txtPais.setColumns(10);
		
		//Caixa de texto onde introduzir o numero de grafos com que se vai realizar a aprendizagem 
		txtGrafs = new JTextField();
		txtGrafs.setHorizontalAlignment(SwingConstants.CENTER);
		txtGrafs.setColumns(10);
		txtGrafs.setBounds(243, 145, 50, 29);
		contentPane.add(txtGrafs);
		
		//Botao "Procurar"
		JButton btnNewButton = new JButton("Procurar");
		btnNewButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e)
			//Ao clicar no bot�o, abre o file chooser, para se seleccionar o ficheiro .csv que vai gerar a amostra.
			{
				JFileChooser chooser = new JFileChooser();
				File F = new File("C:/");
				File Path;
				int Checker;
				chooser.setCurrentDirectory(F);
				chooser.setDialogTitle("Escolha a amostra a analisar");
				Checker=chooser.showOpenDialog(null);
								
				if(Checker==JFileChooser.APPROVE_OPTION)
				{
					Path=chooser.getSelectedFile();
					caminho = Path.getAbsolutePath();
					//guarda-se o caminho do ficheiro .csv
					
					String[] split = caminho.replaceAll("\\.", "\\\\").split("\\\\");
					//separa as componentes do caminho e coloca-os num array de strings.
					doen�a = split[split.length-2];
					//guarda o nome do ficheiro .csv
					
					
					if (split[split.length-1].equals("csv"))
						//Guarda: se a extens�o do ficheiro escolhido � .csv... 
					{
						txtPath.setText(caminho);
						//escreve o caminho na caixa de texto.
					}
					else
					{
						JOptionPane.showMessageDialog(null,"Ficheiro n�o suportado.");
						//caso contrario, mostra uma mensagem de erro.
					}
					
					
				}
			}
		});
		
		
		//Botao Aprender
		btnNewButton.setBounds(318, 54, 89, 23);
		contentPane.add(btnNewButton);
		
		btnNewButton_1 = new JButton("Aprender");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			//Ao clicar no bot�o "Aprender"..
			{

				if (txtPath.getText().equals(""))
				//Guarda: se a caixa de texto do caminho estiver vazia (porque o utilizador nao escolheu o ficheiro da amostra)..
				{
					JOptionPane.showMessageDialog(null,"Escolha a amostra a analisar.");
					//mostra uma mensagem de erro.
				}
				else if (txtPais.getText().equals(""))
				//Guarda: se a caixa de texto dos pais estiver vazia (porque o utilizador nao introduziu o n� m�x de pais)..

				{
					JOptionPane.showMessageDialog(null,"Introduza o n�mero m�ximo de pais");
					//mostra uma mensagem de erro.
				}
				else if (Integer.parseInt(txtPais.getText())>4)
				//Guarda: se o n�mero de pais introduzido f�r superior a 4 (sem contar com o classificador)
				{
					JOptionPane.showMessageDialog(null,"O n�mero m�ximo de pais n�o dever� exceder 4.");
					//mostra uma mensagem de erro.
				}
				else if (txtGrafs.getText().equals(""))
				//Guarda: se a caixa de texto do n� de grafos estiver vazia (porque o utilizador nao introduziu o n� de grafos)..
				{
					JOptionPane.showMessageDialog(null,"Introduza o n�mero de grafos aleat�rios");
					//mostra uma mensagem de erro.
				}
				
				else
				//se nenhuma das situa��es anteriores ocorrer..
				{
					try {
						a = rw.Reader(caminho);
						//O m�todo "Reader" cria a amostra a partir do caminho indicado (ficheiro .csv) e guarda-a.
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					
					long startTime = System.nanoTime(); //(auxiliar para contagem de tempo)
				
					g = a.greedy(Integer.parseInt(txtPais.getText()), Integer.parseInt(txtGrafs.getText()));
					//Aprendizagem dos grafos aleatorios e escolha do melhor grafo. O melhor grafo � guardado.
					
					g.printGraf();//(auxiliar para impressao do grafo para a consola)
					
					bn = new BN (a, g, 0.5);
					//cria��o da rede de bayes com a amostra e grafo guardados.
					
					try {
						rw.SaveBN("documento", bn, g, caminho, doen�a);
						//Guarda-se para o disco os seguintes componentes: rede de bayes, grafo, caminho do ficheiro .csv que gerou a amostra, e o nome desse ficheiro.
						//Esta informa�ao � guardada num documento documentos.File.
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					
					long endTime = System.nanoTime();//(auxiliar para contagem de tempo)
					System.out.println((endTime - startTime)*0.000000001 + " segundos");//(auxiliar para contagem de tempo)
					
					/*C�lculo da efic�cia da analise dos pacientes (saber se t�m ou nao a doen�a), depois de feita a aprendizagem.
					 * Este calculo � feito fazendo a an�lise dos pacientes que constituem a amostra, uma vez que os seus classificadores
					 * j� sao conhecidos. Ou seja, como j� se sabe se eles t�m a doen�a ou n�o, � possivel estimar a efic�cia da an�lise.
					 * 
					 */
						
						int c=0;
						int[] v0 = new int[a.tLength];
						int[] v1 = new int[a.tLength];
						Double f = 0.5; 
						Double prob;
						for (int i=0; i<a.amostraLength; i++)
							//Para cada elemento da amostra
						{
							if (a.element(i)[a.tLength-1]==1)
								//se o valor do seu classificador for =1..
							{
								//cria-se um vector auxiliar, igual ao elemento da amostra que estamos a analisar, mas com o classificador =0.
								//(Necess�rio para o c�lculo da probabilidade de ter a doen�a) => (normalizar a probabilidade)
								for (int j=0; j<a.tLength; j++)
								{
									v0[j]=a.element(i)[j];
								}
								
								v0[a.tLength-1]=0;
								
								prob = bn.prob(a.element(i))/(bn.prob(a.element(i))+bn.prob(v0));
								
								if(prob >= f)
									//se a probabilidade de ter a doen�a for superior a 0.5, incrementa-se o contador.
								{
									c++;
								}
							}
							
							else
								//se o valor do seu classificador for =0..
							{
								//cria-se um vector auxiliar, igual ao elemento da amostra que estamos a analisar, mas com o classificador =1.
								//(Necess�rio para o c�lculo da probabilidade de ter a doen�a) => (normalizar a probabilidade)
								for (int j=0; j<a.tLength; j++)
								{
									v1[j]=a.element(i)[j];
								}
								v1[a.tLength-1]=1;
								
								prob = bn.prob(a.element(i))/(bn.prob(a.element(i))+bn.prob(v1));
											
								if( prob >= f)
									//se a probabilidade de n�o ter a doen�a for superior a 0.5, incrementa-se o contador.
								{
									c++;
								}
							}
						}
						
						System.out.println("Efic�cia");
						System.out.println(((double) c / a.amostraLength)*100);
						//Impress�o para a consola da efic�cia da rede de bayes gerada.
				}
			}
		});
		btnNewButton_1.setBounds(159, 198, 109, 39);
		contentPane.add(btnNewButton_1);
		
		
		//Etiqueta da caixa de texto do caminho.
		JLabel lblNewLabel = new JLabel("Escolha a amostra a analisar");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblNewLabel.setBounds(26, 29, 230, 22);
		contentPane.add(lblNewLabel);
		
		//Etiqueta da caixa de texto onde se introduz o n� maximo de pais.
		JLabel lblNMxPais = new JLabel("N\u00BA m\u00E1x pais");
		lblNMxPais.setBounds(104, 106, 96, 44);
		contentPane.add(lblNMxPais);
		
		//Etiqueta da caixa de texto onde se introduz o n� de grafos.
		JLabel lblNDeGrafos = new JLabel("N\u00BA de grafos");
		lblNDeGrafos.setBounds(233, 106, 99, 44);
		contentPane.add(lblNDeGrafos);
		
		
	}
}
