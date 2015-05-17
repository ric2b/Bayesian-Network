package main;

import java.io.IOException;
import java.util.Arrays;

import bayessian.BayessianNetwork;
import bayessian.RandomVariable;
import bayessian.TransitionBayessianNetwork;
import dataset.Dataset;
import dataset.TimeSlice;
import dataset.TransitionDataset;
import score.LLScore;
import score.MDLScore;
import score.Score;
import input.DataFileReader;

public class Main {

	public static void main(String[] args) {
		
		boolean allVars = false;
		
		if(args.length == 5) { //e especificada a RVar sobre a qual se pretende inferir
			System.out.println("Parameters: " + args[0] + " " + args[1] + " " + args[2] + " " + Integer.parseInt(args[4]));		
		}
		else if(args.length == 4) { //inferir para todas as RVars
			System.out.println("Parameters: " + args[0] + " " + args[1] + " " + args[2]);
			allVars = true;
		}
		else{
			System.out.println("The program parameters must be given by: <train> <test> <score> <randtest> <var> to infer the random variable specified by <var>");
			System.out.println("				     or: <train> <test> <score> <randtest> to infer all random variables.");
			System.exit(0);
		}
			
		long startTime = System.nanoTime();  
		
		//arg[0] - filename do train dataset
		DataFileReader trainFile = null;
		try {
			trainFile = new DataFileReader(args[0]);
		} catch (IOException except) {
			System.out.println(except.getMessage());
			System.exit(0);
		}
		
		//arg[1] - filename do test dataset
		DataFileReader testFile = null;
		try {
			testFile = new DataFileReader(args[1]);
		} catch (IOException except) {
			System.out.println(except.getMessage());
			System.exit(0);
		}
		
		//arg[2] - score (MDL ou LL)
		Score score = null;
		if(args[2].equals("MDL")) {
			score = new MDLScore();
		}
		else if(args[2].equals("LL")){
			score = new LLScore();
		}
		else {
			System.out.println("Score must be given by: <MDL> ou <LL>.");
			System.exit(0);
		}
			
		int instantCount = trainFile.timeInstantCount();
		TimeSlice[] timeSlices = new TimeSlice[instantCount];
		for(int i = 0; i < instantCount; i++) {
			timeSlices[i] = trainFile.getTimeSlice(i);
		}
		
		// todas as vars dadas
		RandomVariable[] vars = trainFile.getRVars();
		
		// vars de tempo zero
		int varCount = trainFile.randomVarCount() / trainFile.timeInstantCount();
		RandomVariable[] varsOfTime0 = Arrays.copyOfRange(vars, 0, varCount);

		Dataset datasetOfTime0 = new Dataset(timeSlices[0]);
		BayessianNetwork<RandomVariable> BNOfTime0 = new BayessianNetwork<>(varsOfTime0, datasetOfTime0, score, varsOfTime0.length);
		
		System.out.println(BNOfTime0);
		
		RandomVariable[] varsOfTandNextT = Arrays.copyOfRange(vars, 0, varCount + varCount);
		
		TransitionDataset transitionDataset = null;
		try {
			transitionDataset = new TransitionDataset(timeSlices);
		} catch (Exception except) {
			System.out.println(except.getMessage());
		}	
		
		TransitionBayessianNetwork<RandomVariable> transitionBN = new TransitionBayessianNetwork<RandomVariable>(varsOfTandNextT, transitionDataset, score); 
		//TransitionBayessianNetwork<RandomVariable> transitionBN = new TransitionBayessianNetwork<RandomVariable>(varsOfTandNextT, transitionDataset);
		//o de baixo for�a o grafo do quadro
		
		long elapsedTime = System.nanoTime() - startTime; //tempo que se demorou a construir a o modelo da DBN (sem inferir o test set) 	
		System.out.println("Building DBN: " + elapsedTime*Math.pow(10, -9) + " seconds");
		
		System.out.println("Transition network: ");
		System.out.println(transitionBN); 
		
		System.out.println("=== Scores");
		System.out.println("LL Score: " + (new LLScore()).getScore(transitionBN, transitionDataset));
		System.out.println("MDL Score: " + (new MDLScore()).getScore(transitionBN, transitionDataset));
				
		//arg[3] - maximum number of random restarts
		//bonus
		
		startTime = System.nanoTime();    
		System.out.println("Performing inference:");
		
		//arg[4] - var		
		if(allVars == true) { //var is not given - all indexes from 1 to n should be considered
			int[][] futureValues = new int[testFile.randomVarCount()][testFile.subjectCount()];
			for(int i = varCount + 1; i < (varCount * 2); i++) {
				futureValues[i - varCount - 1] = transitionBN.getFutureValues(i, new Dataset(testFile.getTimeSlice(0)));			
			}
			for(int j = 0; j < testFile.subjectCount(); j++) { //cada coluna da matriz corresponde aos futures values das RVars para um instante de tempo
				System.out.println("-> instance " + (j+1) + ": ");
				for(int i = 0; i < testFile.randomVarCount(); i++) { //cada linha da matriz corresponde aos futures values de uma RVar		
					if(i == (testFile.randomVarCount()-1)) {
						System.out.println(futureValues[i][j]); //para a ultima RVar ja nao se imprime a virgula
					}
					else {
						System.out.print(futureValues[i][j] + ", ");
					}
				}
			}	
		}
		else {
			int[] futureValues = new int[testFile.subjectCount()];
			futureValues = transitionBN.getFutureValues(Integer.parseInt(args[4]), new Dataset(testFile.getTimeSlice(0)));
			for(int j = 0; j < testFile.subjectCount(); j++) {
				System.out.println("-> instance " + (j+1) + ": " + futureValues[j]);
			}
		}	
		
		elapsedTime = System.nanoTime() - startTime; //tempo que se demorou a inferir o modelo da DBN (sem a fase de aprendizagem do train set) 	
		System.out.println("Infering with DBN: " + elapsedTime*Math.pow(10, -9) + " seconds");
	}
}
