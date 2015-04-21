
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

	/*A dimensão dos grafos vai ter de ser sempre dim(T)-1,
	 * sendo T um elemento da amostra
	 *  (para nao incluir o classificador)
	 */

public class amostra 
{
	/*Inicialização de uma amostra vazia;
	 *Inicialização de uma variavel global para guardar o tamanho da amostra;
	 *Inicialização de uma variavel global para guardar os dominios e cada uma das variaveis dos elementos da amostra. 
	 *Inicialização de uma variavel global para guardar o tamanho dos vectores T a adicionar à amostra;	
	 */
ArrayList<int[]>  a = new ArrayList <int[]> ();
int amostraLength = 0;
ArrayList<Integer> domainsArray = new ArrayList<Integer>();
int tLength = 0;



	public void add (int[] i)
		/*Adiciona um array de inteiros (vector T) à amostra;
		 *Actualiza e guarda o tamanho da amostra;
		 *Guarda o tamanho dos T's;
		 *Actualiza e guarda os dominios de cada uma das variaveis dos elementos da amostra. 
		 */
	{
		
		if (a.size()==0)
			//se fôr a 1º adiçao à amostra, memoriza o tamanho dos T's e cria o vector dos dominios.
		{
			tLength = i.length;
			
			for (int j=0; j<this.tLength; j++)
			{
				domainsArray.add(i[j]+1);
			}
		}
		
		a.add(i);
		//Adiciona o array de inteiros à amostra.
		amostraLength++;
		//Actualiza a dimensao da amostra.
		
			for (int j=0; j<this.tLength; j++)
				//Actualização do vector que guarda os dominios.
			{
				if (i[j]+1>domainsArray.get(j))
				{
					domainsArray.remove(j);
					domainsArray.add(j, i[j]+1);
				}
			}	
			
	}
	
	public int length ()
		//Devolve o tamanho da amostra.
	{
		return (this.amostraLength);
	}
	
	public int[] element (int i)
		//Retorna o elemento i da amostra.
	{
		return (a.get(i));
	}
	
	
	public int count (int[] p, int[] v)
		//Retorna o numero de elementos da amostra que apresentam nas posições p[i] os valores v[i].
	{
		int c = 0;
		
		for (int i=0; i<amostraLength; i++)
		{
			int[] aux = new int[p.length];
			
			for (int j=0; j<p.length; j++)
				//Criaçao de um vector auxiliar (elementos de T das posiçoes p[i]), a comparar com o vector v.
			{
				aux[j] = this.element(i)[p[j]];
			}
			
			if (Arrays.toString(aux).equals(Arrays.toString(v)))
				//Comparação por conversão para string dos vectores aux e v.
			{
				c++;
			}
			
		}
		
		return c;
	}
	
	public ArrayList<int[]> join (amostra x)
		//Recebe uma amostra e junta-a à amostra já existente.
	{
		for (int i=0; i<x.length(); i++)
		{
			this.add(x.element(i));
		}
		return a;
	}
	
	
	protected ArrayList<Integer> domainP (grafoOr g, int i) 
		// Retorna um ArrayList com o dominio dos pais de i. 
	{
		ArrayList<Integer> x = g.parents(i);
		//ArrayList com os pais de "i";
		ArrayList<Integer> z = new ArrayList<Integer>();
		
		for (int j=0; j<x.size(); j++)
			//Construção do ArrayList com os dominios dos pais de "i".
		{
			z.add(domainsArray.get(x.get(j)));
		}
		return z;
	}
	
	
	protected int Qi (grafoOr g, int i)
		//Retorna a multiplicação do dominio de C pela multiplicação dos dominios dos pais de i. 
		//(A usar na funçao theta)
	{
		int c=1;
		ArrayList<Integer> x = this.domainP(g, i);
		//ArrayList com os dominios dos pais de "i".
		
		if (x.size() == 0)
		//Se i nao tiver pais, retorna o dominio de C.
		{
			return c=domainsArray.get(this.tLength-1);
		}
		else
		//caso contrario..
		{
			for (int j=0; j<x.size(); j++)
			//calculo do produtorio dos dominios dos pais de "i" (sem contar com o classificador, o grafo nao considera o classificador)
			{
				c*=x.get(j);
			}
			
			c*=domainsArray.get(this.tLength-1);
			//Produto do produtorio dos dominios dos pais de "i" pelo dominio do classificador.
			
			return c;
		}
		
	}
	
	protected int theta (grafoOr g)
		//Retorna o parametro theta de um dado grafo orientado, a usar na função MDL.
	{
		int c=0;
		for (int i=0; i<this.tLength-1; i++)
			//somatório de: (dominio de cada parametro - 1) * Qi desse parametro
		{
			c+=(domainsArray.get(i)-1)*this.Qi(g, i);
		}
		c+=domainsArray.get(this.tLength-1)-1;
		//ao resultado do ciclo "for" anterior soma-se o dominio do classificador - 1
		
		return c;
	}
	
	
	
	 protected int[] fComb (int i, int[] q)
		 /*Inserida num ciclo que corre até ao produtório dos elementos de q
		 * devolve todas as combinaçoes possiveis de valores para esses parametros , 
		 * tendo em conta os seus dominios.
		 */
	 {
         int j = 0;
         int[] v = new int[q.length];
         while (j<q.length)
         {
               v[j] = i%q[j];
               i = (i-v[j])/q[j];
               j++;
         }
         return v;
	 }
	 
	 protected double PrC (int[] comb, int[] pos, int l)
	 	//Retorna Pt(c) 
	 {
		 int[] v = {comb[l-1]};
		 int[] p = {pos[l-1]};
		 double pr = ((double)this.count(p, v))/this.amostraLength;
		 return pr; 
	 }
	 
	 protected double PrXC (int[] comb, int[] pos, int l)
	 	//Retorna Pt(XC)
	 {
		 int[] v = {comb[0],comb[l-1]};
		 int[] p = {pos[0],pos[l-1]};
		 double pr = ((double)this.count(p, v))/this.amostraLength;
		 return pr;
	 }
	
	 protected double PrYC (int[] comb, int[] pos, int l)
	 	//Retorna Pt(YC)
	 {
		 int[] v = new int[l-1];
		 int[] p = new int[l-1];
		 for (int i=0; i<l-1; i++)
		 {
			 v[i]=comb[i+1];
			 p[i]=pos[i+1];
		 }
		 double pr = ((double)this.count(p, v))/this.amostraLength;
		 return pr;
	 }
	 
	 protected double PrXYC (int[] comb, int[] pos, int l)
	 	//Retorna Pt(XYC)
	 {
		 double pr = ((double)this.count(pos, comb ))/this.amostraLength;
		 return pr;
	 }
	 
	 protected double PrTotal (int[] comb, int[] pos, int l)
	 	//Retorna um elemento do somatório de It(Xi) (ou seja, para uma dada combinação)
	 {
		 double a = this.PrXYC(comb,pos,l);
		 double b = this.PrYC(comb,pos,l);
		 double c = this.PrXC(comb,pos,l);
		 double d = this.PrC(comb,pos,l);
		 
		 if (a*d==0 || b*c==0 )
			 //Guarda, para quando log(x) = NaN (not a number), retornar 0.
		 {
			 return 0;
		 }
		 else
		 {
			 return a*Math.log10((a*d)/(b*c));
		 }
		 
	 }
	 
	 protected double It (int xi, grafoOr g)
	 	//Retorna o parametro It(Xi) de um dado grafo orientado, a usar na função MDL.

	 {
		ArrayList<Integer> pais = g.parents(xi);
		
		if (pais.size()==0)
			//se não existirem pais, a expressão de It fica ...*log(1)=0; logo, It=0.
		{
			return 0;
		}
		
		else
		{
			int l = pais.size()+2;
		
			//construçao vector dos dominios de Xi,Pais e C e respectivo vector das posições.
			int[] doms = new int[l];
			int[] pos = new int[l];
			
			doms[0] = domainsArray.get(xi);
			pos[0]=xi;
			
			for (int j=1; j<l-1; j++)
			{
				doms[j]=domainsArray.get(pais.get(j-1));
				pos[j]=pais.get(j-1);
			}
			
			doms[l-1] = domainsArray.get(this.tLength-1);
			pos[l-1]=this.tLength-1;
			
			//Definição do produtório dos elementos do vector dominios.
			int prod = 1;
			for (int k = 0; k<l; k++)
			{
				prod*=doms[k];
			}
			
			double sum = 0;
			
			//Calculo das combinaçoes possiveis e das respectivas probabilidades. (calculo de cada elemento do somatorio de It(xi))
			int[] comb;
			for (int i=0; i<prod; i++)
			{
				comb = this.fComb(i,doms);
				sum += this.PrTotal(comb,pos,l);
			}
		
			return sum;
		}
	 }
	 
		 
Random r = new Random();

		protected grafoOr randomGraf (int Npais)
		/*
		 * Recebe um numero de pais maximo por parametro (sem contar com o classificador, 
		 *dado que o grafo nao considera o classificador) e retorna um grafo aleatorio.
		 *Numero de iteraçoes (tentativas de colocar 1's na matriz de zeros) = Nº máx de pais * Nº de parametros
		 *As posiçoes onde se vai tentar colocar uma aresta sao geradas aleatoriamente, e podem repetir-se 
		 */
		
		{
			grafoOr g = new grafoOr (this.tLength-1);
			
			int i = 0;
			while (i<Npais*(this.tLength-1))
				//iteraçoes para colocar arestas na matriz de zeros
			{
				int ir = r.nextInt(this.tLength-1);
				int jr = r.nextInt(this.tLength-1);
					//gera posiçoes aleatorias na matrix
				
				if (g.parents(jr).size()<Npais)
					//guarda para nao exceder o numero máx de pais por parametro
				{
					g.add_edge(ir, jr);
					i++;
				}
			}
			return g;
		}
		
		protected grafoOr grafMDLmin (grafoOr g, int Npais) 
		/*Recebe um grafo aleatorio e o Nº máx de pais por parametro e retorna o grafo com MDL score
		 * minimo.
		 * 
		 *  Para todas as entradas da matriz (grafo), vai ser calculado o deltaMDL para cada posição.
		 *  Dado que se pretende minimizar o mdl score do grafo, apenas nos interessa memorizar os valores de deltaMDL negativos
		 *  e a respectivas posiçoes em que isso acontece.
		 *  De seguida, procura-se o valor mais negativo, e tenta-se alterar o grafo nessa posiçao. Se não for possivel fazer essa alteração
		 *  (por o numero maximo de pais desse parametro ja ter sido atingido), elimina-se esse valor do ArrayList e procura-se 
		 *  novamente o valor deltaMDL mais negativo. 
		 *  Se eventualmente se conseguir alterar o grafo, é criado um novo ArrayList de mdlDelta's negativos e das suas respectivas posiçoes
		 *  e tenta-se alterar o grafo novamente.
		 *  Se depois de se percorrer todo o ArrayList nao tiver sido possivel alterar o grafo, o aux passa a "false" e o metodo
		 *  retorna o grafo (grafo de mdl score minimo). 
		 */
		{
			int gDim = g.ma.length;
			int[] pais = new int[gDim];
			//inicializaçao de um vector que vai guardar o numero de pais de cada parametro.
			
			for (int x=0; x<gDim; x++)
			//ciclo para construir o vector com o Nº de  pais de cada parametro inicial.
			{
				pais[x]=g.parents(x).size();
			}
			
			Boolean aux = true;
			//Booleano auxiliar que vai permitir sair de ciclo exterior e retornar o grafo minimizado. 
			
			int pos=0;
			
			while (aux == true)
			//Neste ciclo exterior, constroi-se um ArrayList que guarda 
			{
				ArrayList<double[]> deltaMDL = new ArrayList<double[]>();
				//Inicialização do ArrayList que vai guardar os valores de deltaMDL negativos e as respectivas posições.
				
				for (int i=0; i<gDim; i++)
					//a matriz é percorrida linha a linha..
				{
					for (int j=0; j<gDim; j++)
						//..e em cada posiçao dessa linha..
					{
						double delta = g.MDLdelta(this, i, j);
						//Calcula-se o valor de deltaMDL.
						
						if (delta<0)
							//Se esse valor fôr negativo..
						{
							double[] p = {delta, i, j}; 
							//Cria-se um Array com esse valor de deltaMDL e a respectiva posição no grafo. 
							deltaMDL.add(p);
							//Adiciona-se o array ao ArrayList.
						}
					}
				}
				
				if (deltaMDL.size()==0)
				//Se o ArrayList criado estiver vazio, significa que nenhuma alteração no grafo vai contribuir para minimizar o seu mdl score.
				{
					return g;
					//Deste modo, retorna-se o grafo. Já não é possivel minimizar mais o seu MDL score.
				}
				else
				//caso contrário..
				{
					
					while (deltaMDL.size()>0)
					/*Enquanto o ArrayList não estiver vazio.. 
					*/
					{
					
						pos = 0;
						
						for (int z=1; z<deltaMDL.size(); z++)
						//procura-se o valor mais negativo de deltaMDL no ArrayList e a sua posição no ArrayList.
						{
							double deltaMin = deltaMDL.get(0)[0];
							
							if (deltaMDL.get(z)[0]<deltaMin)
							{
								deltaMin = deltaMDL.get(z)[0];
								pos = z;
							}
						}
						
						int pi = (int) deltaMDL.get(pos)[1];
						int pj = (int) deltaMDL.get(pos)[2];
						
						//..e tenta-se alterar o grafo nessa posiçao associada a esse valor.
						if (g.ma[pi][pj]==1)
							/*Se a entrada for =1, remove-se a aresta, actualiza-se o vector "nº de pais" e sai-se do ciclo interior,
							*para ir construir novo ArrayList de deltaMDL's, visto que o grafo foi alterado.
							*/
						{
							g.rem_edgev(pi, pj);
							pais[pj]-=1;
							break;
						}
						else
							//Caso contrário..
						{
							if (pais[pj]<Npais)
								/*..se o numero de pais ainda não tiver sido excedido, adiciona-se uma aresta, actualiza-se o vector "nº de pais" 
								 * e sai-se do ciclo interior, para ir construir novo ArrayList de deltaMDL's, visto que o grafo foi alterado.
								*/
							{
								g.add_edge(pi, pj);
								pais[pj]+=1;
								break;
							}
						}
						
						deltaMDL.remove(pos);
						/*se nao tiver sido possivel fazer alteraçoes ao grafo na posição associada ao valor minimo de deltaMDL,
						 * remove-se esse Array (valor de mdlDelta + posiçao) do ArrayList e vola-se ao inicio do ciclo,
						 * para procurar o novo valor minimo.
						 */
						
						if (deltaMDL.size()==0)
							/*Se ao remover o Array, o ArrayList ficar fazio, significa que nao foi possivel alterar o grafo em
							 * nenhuma das posições. obteve-se portanto o grafo de MDL minimo possivel. Como o ArrayList está vazio,
							 * sai-se do ciclo interior. 
							 * Neste caso, para garantir que também se sai do ciclo exterior e o grafo e retornado, o booleano auxiliar
							 * passa a false.
							 */
						{
							aux=false; //se for repetir o ciclo inicial, vai analisar o mesmo AL de mdl's
						}					
					}
				}	
			}
			return g;
		}
		
		 
	 
		
		public grafoOr greedy (int Npais, int Ngrafos)
			//Recebe o nº max de pais por parametro e o numero de grafos aleatorios com que se vai realizar a aprendizagem.
			//Retorna o grafo com o minimo MDL score.
		{
			int gDim = this.tLength-1; 
			grafoOr G0 = new grafoOr(gDim);
			//ponto de partida: grafo totalmente desconexo, ou seja, um dos Ngrafos será sempre uma matriz de zeros.
			
			grafoOr Gmin = this.grafMDLmin(G0, Npais);
			//variavel que guarda o grafo de MDL score minimo até ao momento.
			//é inicializada com o grafo totalmente desconexo, depois de feita a sua aprendizagem (grafMDLmin)
			
			double MDLmin = Gmin.MDL(this);
			//variavel que guarda o MDL score do grafo Gmin.
			//é inicializada com o MDL score do grafo totalmente desconexo, depois de feita a sua aprendizagem (grafMDLmin)
			
			double MDLaux = 0;
			
			
			for (int i=1; i<Ngrafos; i++)
				/*sao gerados Ngrafos-1 grafos aleatorios. se apos a sua aprendizagem, o MDL score de algum
				*desses grafos fôr inferior ao MDLmin guardado, essa variavel é actualizada com esse valor,
				*assim como a variavel Gmin é actualizada com esse mesmo grafo.
				*/
			{
				grafoOr Gr = this.randomGraf(Npais);
				grafoOr Gaux = this.grafMDLmin(Gr, Npais);
				MDLaux = Gaux.MDL(this);
				
				if (MDLaux<MDLmin)
				{
					MDLmin=MDLaux;
					Gmin=Gaux;
				}
			}
			return Gmin;
		}
		
		
}

