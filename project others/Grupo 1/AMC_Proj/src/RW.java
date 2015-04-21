import java .io .*;
//import java . util . Arrays ;
import java.util.ArrayList;

public class RW implements java.io.Serializable 
{
	
	private static final long serialVersionUID = 1L;
	private ObjectOutputStream out ;
	private ObjectInputStream in;

	BN r = new BN();
	grafoOr g;
	String path;
	String doen�a;

	public amostra Reader (String path) throws Exception
		//metodo que constroi uma amostra a partir de um ficheiro .csv
	{
		amostra a = new amostra();
		//inicializa�ao de uma nova amostra vazia.
		
		BufferedReader CSVFile = new BufferedReader (new FileReader (path));
		
		String dataRow = CSVFile . readLine (); 
		// Le a primeira linha .
		
		while ( dataRow != null )
		{
				// Qd chega ao fim o dataRow fica a null
			String [] dataArray = dataRow . split (",");
				// split () metodo que separa os dados da string num array
			int [] intArray =new int[ dataArray . length ];
				// variavel intArray vai guardar os dados como inteiros ;
			int i=0;
			while (i< dataArray . length ) 
			{
				intArray [i]= Integer . parseInt ( dataArray [i]);
					// parseInt transforma uma string num inteiro
				i++;
			}
			
		a.add(intArray);
			//adiciona o array � amostra.
		
		dataRow = CSVFile . readLine (); 
			// Le a proxima linha
		}
		
		CSVFile . close ();
		// Fechar o ficheiro
		return a;
	}	
	
	public void SaveBN (String s, BN r, grafoOr g, String p, String d) throws IOException
	//m�todo que guarda uma rede de bayes, um grafo, e uma string (caminho para o .csv) no disco, e uma string com o nome da amostra analisada.
	{
	FileOutputStream fos = new FileOutputStream (s);
	out = new ObjectOutputStream (fos);
	
	out.write(r.rede.size());
		//escreve para o disco o tamanho do ArrayList que constitui a rede

	for(int i=0; i<r.rede.size(); i++)
	{
		int t1 = r.rede.get(i).length;
		out.write(t1);
			//escreve para o disco o tamanho do elemento "i" da rede de bayes.
		
		for (int j=0; j<t1; j++)
		{
			out.writeDouble(r.rede.get(i)[j]);
				//escreve para o disco os valores das probabilidades contidos no elemento "i" da rede de bayes. 
		}
	}
	
	out.write(r.listaComb.size());
		//escreve para o disco o tamanho do ArrayList paralelo � rede de bayes que vai guardar as combina�oes que originam as probabilidades.
	
	for(int i=0; i<r.listaComb.size(); i++)
	{
		int t1 = r.listaComb.get(i).size();
		out.write(t1);
		//escreve para o disco o tamanho do elemento "i" do ArrayList paralelo � rede de bayes que vai guardar as combina�oes que originam as probabilidades.
		
		for (int j=0; j<t1; j++)
		{
			int t2 = r.listaComb.get(i).get(j).length;
			out.write(t2);
			//escreve para o disco o tamanho da combina��o "j" do elemento "i" do ArrayList paralelo � rede de bayes que vai guardar as combina�oes que originam as probabilidades.
			
	
			for (int k=0; k<t2; k++)
			{
				out.write(r.listaComb.get(i).get(j)[k]);
				//escreve para o disco os elementos da combina��o "j".
			}
		}
	}
	
	out.write(g.ma.length);
	//escreve para o disco a dimens�o do grafo utilizado para criar a Rede de Bayes
	
	for (int i=0; i<g.ma.length; i++)
	{
		for (int j=0; j<g.ma.length; j++)
		//ciclos encaixados para percorrerem todas as entradas da matrix.
		{
			out.write(g.ma[i][j]);
			//escreve para o disco o valor de cada entrada do grafo utilizado para criar a Rede de Bayes
		}
	}
	
	 
	out.writeUTF(p);
	//grava o caminho para o ficheiro .csv que gerou a amostra.
	
	out.writeUTF(d);
	//grava o nome do ficheiro .csv que gerou a amostra.
	
	out. close ();
	fos. close ();
	}

	public void LoadBN ( String s) throws IOException
	//m�todo que l� a informa��o guardada em disco pelo m�todo "save".
	//reconstroi a rede de bayes e o grafo guardados, e recupera a string caminho para o .csv e a string com o nome da amostra analisada.
	{
		FileInputStream fis = new FileInputStream (s);
		in = new ObjectInputStream (fis);
		
		int tRede = in.read();
			//l� o tamanho da rede de bayes
		
		for (int i=0; i<tRede; i++)
			//ciclo que controi o elemento i da rede de bayes (vector de probabilidades "i").
		{
			int txi = in.read();
			//l� o tamanho do vector de probabilidades.
			double[] x = new double[txi];
			//inicializa�ao do vector de probabilidades "i".
			
			for (int j=0; j<txi; j++)
				//constru��o do vector de probabilidades.
			{
				x[j] = in.readDouble();
				//l� as probabilidades e insere-as no vector.
			}
			
			r.rede.add(x);
			//insere o vector probabilidades na rede de bayes.
		}
	
		in.read();
		//l� o tamanho do ArrayList paralelo � rede de bayes que vai guardar as combina�oes que originam as probabilidades.

		
		for (int i=0; i<tRede; i++)
		{
			int txi = in.read();
			//l� e guarda o tamanho do elemento "i" do ArrayList paralelo � rede de bayes que vai guardar as combina�oes que originam as probabilidades.
			ArrayList<int[]> a = new ArrayList<int[]>();
			//inicializa��o do ArrayList de combina�oes (elemento "i" do ArrayList paralelo � rede de bayes)

			
			for (int j=0; j<txi; j++)
			{
				int tcomb = in.read();
				//l� o tamanho da combina��o
				int[] y = new int[tcomb];
				//inicializa o vector combina��o
				
				for (int k=0; k<tcomb; k++)//y
				{
					y[k]=in.read();
					//l� os elementos da combina��o e insere-os no vector.
				}
				
				a.add(y);
				//adiciona a combina�ao ao ArrayList "a" (elemento "i" do ArrayList paralelo � rede de bayes que vai guardar as combina�oes que originam as probabilidades.)
			}
			r.listaComb.add(a);
			//adiciona o ArrayList "a" ao ArrayList paralelo � rede de bayes que vai guardar as combina�oes que originam as probabilidades.
		}
		
		this.g = new grafoOr(in.read());
		//l� a dimens�o do grafo utilizado para criar a Rede de Bayes e inicializa esse grafo (grafo de zeros).
		
		for (int i=0; i<g.ma.length; i++)
		{
			for (int j=0; j<g.ma.length; j++)
			//ciclos encaixados para percorrerem todas as entradas da matrix.
			{
				g.ma[i][j] = in.read();
				//l� o valor de cada entrada da matrix que constitui o grafo e constroi-o.
			}
		}
		
		path = in.readUTF();
		//l� e guarda o caminho para o ficheiro .csv que gerou a amostra.
		
		doen�a = in.readUTF();
		//l� e guarda o nome do ficheiro .csv que gerou a amostra.
		
		
		in. close ();
		fis. close ();
		

	}
}
