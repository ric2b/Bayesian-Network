import java.util.ArrayList;
import java.util.Arrays;

public class BN {
	
	amostra am;
	grafoOr graf;
	
	/*Initialização de um ArrayList que vai ser a rede de bayes;
	 *Initialização de um ArrayList paralelo que vai guardar as combinaçoes que originam as probabilidades, 
	 *pela mesma ordem, para posteriormente facilitar a sua identificação, no método "probs";
	 */
	ArrayList<double[]> rede = new ArrayList<double[]>();
	ArrayList<ArrayList<int[]>> listaComb = new ArrayList<ArrayList<int[]>>();
	
	
	public BN() 
		//Método constructor que cria uma rede de bayes vazia.
	{
		
	}
	
	public BN(amostra a, grafoOr g, double s) {
		//Método constructor que cria uma nova rede de bayes
		
		am = a;
		graf = g;
		//Variaveis globais que guardam a amostra e grafo utilizados para a construçao da rede de bayes.
		
		ArrayList<Integer> pais;
		
		for(int i=0; i<a.tLength-1; i++)
		{
			pais = g.parents(i);
			int l = pais.size()+2;
			
			/*construçao do vector dos dominios de Xi,Pais e C e respectivo vector das posições,
			 * e construçao do vector dos dominios dos Pais e C e respectivo vector das posições
			 */
			int[] doms = new int[l];
			int[] pos = new int[l];
			int[] pos2 = new int[l-1];
			
			doms[0] = a.domainsArray.get(i);
			pos[0]=i;
			
			for (int j=0; j<l-2; j++)
			{
				doms[j+1]=a.domainsArray.get(pais.get(j));
				pos[j+1]=pais.get(j);
			}
			
			for (int k=0; k<l-1; k++)///l-1
			{
				pos2[k]=pos[k+1];//pais.get(k-1);
			}
			
			doms[l-1] = a.domainsArray.get(a.tLength-1);
			pos[l-1]=a.tLength-1;
			pos2[l-2]=a.tLength-1;
			
			//Definição do produtório dos elementos do vector dominios.
			int prod = 1;
			for (int f = 0; f<l; f++)
			{
				prod*=doms[f];
			}
			
			/*Criaçao do vector dfo's para Xi.
			 * Criaçao do array list combsXi para Xi.
			 */
			double[] dfo = new double[prod];
			ArrayList<int[]> combsXi = new ArrayList<int[]>();
			int[] comb = new int[l];
			int[] comb2 = new int[l-1];
			double Pdfo = 0;
			
			for (int d=0; d<prod; d++)
			{
				comb = a.fComb(d,doms);
				combsXi.add(comb);
				
				for (int q=1; q<l; q++)
				{
					comb2[q-1]=comb[q];
				}
				
				Pdfo = (double)(a.count(pos, comb)+s)/(a.count(pos2, comb2)+(s*a.domainsArray.get(i)));
				dfo[d] = Pdfo;
			}
			                
			rede.add(dfo);
			listaComb.add(combsXi);
		}
	}
		
	public Double prob (int[] t)
	//Retorna a probabilidade do vector que recebe, recorrendo à rede de bayes.
	{
		for (int x=0; x<am.tLength; x++)
			//Se algum dos valores das variaveis de T exceder o dominio da amostra que gerou a rede de bayes, retorna null.
		{
			if (t[x]+1>am.domainsArray.get(x))
			{
				return null;
			}
		}
		
			ArrayList<Integer> pais;
			double[] probs = new double[am.tLength];
			
			for (int i=0; i<am.tLength-1; i++)
			{
				pais = graf.parents(i);
				int l = pais.size()+2;
				
				//construçao vector com valor de xi, dos pais de xi e do classificador
				int[] comb = new int[l];
				comb[0] = t[i];
				comb[l-1] = t[am.tLength-1];
				
				for (int j=1; j<l-1; j++)
				{
					comb[j] = t[pais.get(j-1)];
				}
				
				//Pesquisa, por comparaçao de vectores, da posiçao da combinaçao e respectiva probabilidasde na BN
				int position = -1;
				for (int k=0; k<this.listaComb.get(i).size(); k++)
				{
					if (Arrays.toString(comb).equals(Arrays.toString(this.listaComb.get(i).get(k))))
					{
						position = k;
						break;
						/*Quando encontrar a combinaçao correspondente, sai do ciclo "for" e grava a posiçao 
						 *da combinaçao (igual à da probabilidade na BN).
						 */
					}
				}
				probs[i] = this.rede.get(i)[position];	
				//Construçao de um vector probabilidades.
			}
			
			int[] v = {t[am.tLength-1]};
			int[] p = {am.tLength-1};
			probs[am.tLength-1]= ((double)am.count(p,v))/am.amostraLength;
			//calculo da probabilidade de C.
			
			double prod = 1;
			for (int f = 0; f<am.tLength; f++)
				//produtorio do vector probabilidades.
			{
				prod*=probs[f];
			}
			
			return prod;
	}
}
