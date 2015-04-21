
import java.util.ArrayList;
	
	/*A dimens�o dos grafos vai ter de ser sempre dim(T)-1,
	 * sendo T um elemento da amostra
	 *  (para nao incluir o classificador)
	 */

public class grafoOr {
	
	int[][] ma;
	
	public grafoOr (int n) 
		//M�todo constructor que inicializa uma matrix quadrada de dimens�o n.

	{
		ma = new int[n][n];
	}
	
	public void add_edge (int i, int j)
		//Adiciona uma aresta ao grafo.

	{
		if (i!=j && this.cycleQ(i, j)==false)
			/*Guarda, porque um parametro nunca poder� ser pai de si proprio;
			*e porque o grafo nao pode ser ciclico.
			*/
		{
			ma[i][j] = 1;
		}
	}
	
	

	public void rem_edgev (int i, int j)
		//Remove uma aresta ao gr�fo.
	{
		ma[i][j] = 0;
	}
	
	public ArrayList<Integer> parents (int j)
		//Retorna um ArrayList com os pais do parametro j.
	{
		ArrayList<Integer> l = new ArrayList<Integer>();
		
		for (int i=0; i<ma.length; i++)
			//Percorre a coluna j.
		{
			if (ma[i][j] != 0)
				//Se na linha i encontrar 1, i � pai de j.
			{
				l.add(i);
			}
		}
		return l;
	}
	
	protected void printGraf ()  
		//M�todo auxiliar a utilizar em testes, para "imprimir" (por System.out.println) grafos orientados (matriz adjacencia).
	{
		for (int i=0; i<ma.length; i++)
		{
			for (int j=0; j<ma.length; j++) 
			{
				System.out.print(ma[i][j] + " ");
			}
	    System.out.print("\n");
		}
	}

	public double MDL (amostra a)
		//Retorna o MDL score da amostra
	{
		double x = (Math.log10(a.amostraLength)/2)*a.theta(this);
		double y = 0;
		
		for (int i=0; i<a.tLength-1; i++)
			//c�lculo do somatorio de It's (somatorio de It(Xi))
		{
			y += a.It(i, this);
		}
		
		return x-y*a.amostraLength;
	}
	
	protected double sumItDelta (amostra a, int[] doms, int[] pos, int prod, int l)
		//Gera todas as combina�oes de valores do vector doms e
		//retorna o somatorios das suas probabilidades. 
		//A utilizar para o c�lculo do It do MDLdelta.
		//Vai permitir calcular o somatorio de Y' e o somatorio de Y, da formula matem�tica da fun��o DeltaMDL.
	{
		int[] comb;
		double sum = 0;
		
		for (int i=0; i<prod; i++)
		{
			comb = a.fComb(i,doms);
			double x = a.PrXYC(comb,pos,l);
			double y = a.PrYC(comb,pos,l);
			
			if (x==0 || y==0)
				//Guarda, para quando log(x) = NaN (not a number), retornar 0.
			{
				sum += 0;
			}
			else
			{
				sum += x*Math.log10(x/y);
			}
		}	
		return sum;
	}
	
	public double MDLdelta (amostra a, int p, int f)
		//Retorna a varia��o de MDL causada por retirar ou adicionar uma aresta entre os n�s p e f.
	{
		if (p==f || this.cycleQ(p, f)==true)
			//MDL Delta retorna 0 se se tentar introduzir uma aresta tal que um parametro seja pai de si proprio, ou se se fosse estudar a varia��o de MDL para a adi��o de uma aresta que iria tornar o grafo c�clico.
		{
			return 0;
		}
		else
		{
			//Cria��o de um novo grafo ng, inicialmente igual a "this", ao qual ser� adicionada ou retirada uma aresta
			int[][] aux = new int[this.ma.length][this.ma.length];
			
			grafoOr ng = new grafoOr (this.ma.length);
			
			for (int contadori = 0; contadori<this.ma.length; contadori++)
			{
				for (int contadorj=0; contadorj<this.ma.length; contadorj++)
				{
					aux[contadori][contadorj] = this.ma[contadori][contadorj];
				}
			}
			
			ng.ma = aux;
			double thetaDelta = 0;
			
			if (ng.ma[p][f]==0)
			 	//Analisar se o grafo ja tem uma aresta no posi��o (p,f);
			{
				//caso nao tenha, adiciona-se a aresta e calcula-se thetaDelta para o grafo sem a aresta (grafo inicial "this")
				ng.add_edge(p, f);
				thetaDelta = a.Qi(this, f)*(a.domainsArray.get(f)-1)*(a.domainsArray.get(p)-1);
			}
			else
			{			
				//caso ja tenha a aresta nesse local, remove-se e calcula-se thetaDelta para o grafo sem a aresta (grafo final "ng")
				ng.rem_edgev(p, f);
				thetaDelta = -a.Qi(ng, f)*(a.domainsArray.get(f)-1)*(a.domainsArray.get(p)-1);
			}
			
			int xi = f;
			double sum1 = 0;
			double sum2 = 0;
			
			//	GRAFO ANTIGO (THIS):
			//constru�ao vector dos dominios de Xi,Pais e C e respectivo vector das posi��es.
		
			ArrayList<Integer> pais = this.parents(xi);
		
			int l = pais.size()+2;
			int[] doms = new int[l];
			int[] pos = new int[l];
			
			doms[0] = a.domainsArray.get(xi);
			pos[0]=xi;
			
			for (int m=1; m<l-1; m++)
			{
				doms[m]=a.domainsArray.get(pais.get(m-1));
				pos[m]=pais.get(m-1);
			}
			
			doms[l-1] = a.domainsArray.get(a.tLength-1);
			pos[l-1]=a.tLength-1;
			
			//Defini��o produt�rio dos elementos do vector dominios.
			int prod = 1;
			for (int k = 0; k<l; k++)
			{
				prod*=doms[k];
			}
			
			//C�lculo do somat�rio respectivo, com recurso ao metodo sumItDelta (somat�rio de Y).
			sum1=this.sumItDelta(a, doms, pos, prod, l);
			
			
			//		GRAFO NOVO (ng):
			//(N=>new)
			//constru�ao vector dos dominios de Xi,Pais e C e respectivo vector das posi��es.
			
			ArrayList<Integer> paisN = ng.parents(xi);	
			
			int lN = paisN.size()+2;
			int[] domsN = new int[lN];
			int[] posN = new int[lN];
			
			domsN[0] = a.domainsArray.get(xi);
			posN[0]=xi;
			
			for (int n=1; n<lN-1; n++)
			{
				domsN[n]=a.domainsArray.get(paisN.get(n-1));
				posN[n]=paisN.get(n-1);
			}
			
			domsN[lN-1] = a.domainsArray.get(a.tLength-1);
			posN[lN-1]=a.tLength-1;
			
			//Defini��o produt�rio dos elementos do vector dominios.
			int prodN = 1;
			for (int c = 0; c<lN; c++)
			{
				prodN*=domsN[c];
			}
			
			//C�lculo do somat�rio respectivo, com recurso ao metodo sumItDelta (somat�rio de Y').
			sum2=ng.sumItDelta(a, domsN, posN, prodN, lN);
			
			return ((Math.log10(a.amostraLength)*thetaDelta)/2)-(a.amostraLength*(sum2-sum1));
	
		}
	}
	
	protected boolean cycleQ (int i, int j)
	//verifica se a adi�ao de uma aresta vai tornar o grafo ciclico.
	{
		boolean cycle = false;
		
		if (this.parents(i).size() == 0 )
			//se o pai "i" nao tiver pais, a fun�ao retorna false (a adi�ao da aresta nao vai tornar o grafo ciclico)
		{
			cycle = false;
		}
		else
			//se o pai "i" tiver pais...
		{
			int x=0;
			while (x<this.parents(i).size())
				//percorre a lista dos pais de "i"
			{
				if (this.parents(i).get(x)==j)
					//se alguns dos pais de "i" f�r o filho "j", a fun�ao retorna true (a adi�ao da aresta vai tornar o grafo ciclico)
				{
					return true;
				}
				else
				{
					x++;
				}
			}
			
			x = 0;
			
			while (x<this.parents(i).size())
				//percorre novamente a lista dos pais de "i"
			{
				if (this.cycleQ(this.parents(i).get(x),j)==true)
					/*Vai verificar recursivamente os pais dos pais de "i", e assim sucessivamente,
					 * at� encontrar o filho "j" ou ate que ja nao existam mais pais.
					 */
				{
					return true; 
				}
				else
				{
					x++;
				}
			}
		}
		return cycle;
	}
	
}