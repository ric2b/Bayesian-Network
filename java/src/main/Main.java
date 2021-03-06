package main;

import java.io.IOException;
import java.util.Arrays;

import bayessian.BayessianNetwork;
import bayessian.MaximumCriterion;
import bayessian.RandomVariable;
import bayessian.RestartCriterion;
import bayessian.StopCriterion;
import bayessian.TransitionBayessianNetwork;
import dataset.Dataset;
import dataset.TimeSlice;
import dataset.TransitionDataset;
import score.FastLLScore;
import score.FastMDLScore;
import score.LLScore;
import score.MDLScore;
import score.Score;
import userinterface.GUI;
import input.DataFileReader;

public class Main {
	
	public static long elapsedTimeBN;
	public static long elapsedTimeInfere;
		
	public static TransitionBayessianNetwork<RandomVariable> buildDBN(String traindataset, String scoreArg, int randtest, SaveToFile out, boolean toFile) {
	
		long startTime = System.nanoTime();  
		
		//arg[0] - filename do train dataset
		DataFileReader trainFile = null;
		try {
			trainFile = new DataFileReader(traindataset);
		} catch (IOException except) {
			out.println(except.getMessage(), toFile);
			System.exit(0);
		}
		
		//arg[2] - score (MDL ou LL)
		Score score = null;
		if(scoreArg.equals("MDL")) {
			score = new MDLScore();
		}
		else if(scoreArg.equals("LL")){
			score = new LLScore();
		}
		else if(scoreArg.equals("FMDL")){ //com speed-up
			score = new FastMDLScore();
		}
		else if(scoreArg.equals("FLL")){ //com speed-up
			score = new FastLLScore();
		}
		else {
			out.println("Score must be given by: <MDL> ou <LL>.", toFile);
			System.exit(0);
		}
			
		int instantCount = trainFile.timeInstantCount();
		TimeSlice[] timeSlices = new TimeSlice[instantCount];
		for(int i = 0; i < instantCount; i++) {
			timeSlices[i] = trainFile.getTimeSlice(i);
		}
		
		// todas as vars dadas
		RandomVariable[] vars = trainFile.getRVars();
		int varCount = trainFile.randomVarCount() / trainFile.timeInstantCount();
		RandomVariable[] varsOfTandNextT = Arrays.copyOfRange(vars, 0, varCount + varCount);
		
		TransitionDataset transitionDataset = null;
		try {
			transitionDataset = new TransitionDataset(timeSlices);
		} catch (IllegalArgumentException except) {
			out.println(except.getMessage(), toFile);
		}	
		
		StopCriterion transitionCriterion = null;
		
		if(randtest > 0) {
			transitionCriterion = new RestartCriterion(randtest);
		} else {
			transitionCriterion = new MaximumCriterion();
		}
		
		TransitionBayessianNetwork<RandomVariable> transitionBN = new TransitionBayessianNetwork<RandomVariable>(varsOfTandNextT, transitionDataset, score, transitionCriterion); 

		elapsedTimeBN = System.nanoTime() - startTime; //tempo que se demorou a construir a o modelo da DBN (sem inferir o test set) 	
		out.println("Building DBN: " + elapsedTimeBN*Math.pow(10, -9) + " seconds", toFile);
		
		
		StopCriterion B0Criterion = null;
		
		if(randtest > 0) {
			B0Criterion = new RestartCriterion(randtest);
		} else {
			B0Criterion = new MaximumCriterion();
		}
		
		// vars de tempo zero
		RandomVariable[] varsOfTime0 = Arrays.copyOfRange(vars, 0, varCount);
		Dataset datasetOfTime0 = new Dataset(timeSlices[0]);
		BayessianNetwork<RandomVariable> BNOfTime0 = new BayessianNetwork<>(varsOfTime0, datasetOfTime0, score, varsOfTime0.length, B0Criterion);
		
		out.println("Initial network: ", toFile);
		out.println(BNOfTime0.toString(), toFile);
		out.println("=== Scores", toFile);
		out.println("LL Score: " + (new LLScore()).getScore(BNOfTime0, datasetOfTime0), toFile);
		out.println("MDL Score: " + (new MDLScore()).getScore(BNOfTime0, datasetOfTime0), toFile);
			
		out.println("Transition network: ", toFile);
		out.println(transitionBN.toString(), toFile); 
		out.println("=== Scores", toFile);
		out.println("LL Score: " + (new LLScore()).getScore(transitionBN, transitionDataset), toFile);
		out.println("MDL Score: " + (new MDLScore()).getScore(transitionBN, transitionDataset), toFile);	
		
		return transitionBN;
	}
	
	public static void infereValue(String testdataset, boolean allVars, int varToInfere, TransitionBayessianNetwork<RandomVariable> transitionBN, SaveToFile out, boolean toFile) {
		
		long startTime = System.nanoTime();   
		
		//arg[1] - filename do test dataset
		DataFileReader testFile = null;
		try {
			testFile = new DataFileReader(testdataset);
		} catch (IOException except) {
			out.println(except.getMessage(), toFile);
			System.exit(0);
		}
		
		out.println("Performing inference:", toFile);
		
		//arg[4] - var		
		if(allVars == true) { //var is not given - all indexes from 1 to n should be considered
			int[][] futureValues = new int[testFile.randomVarCount()][testFile.subjectCount()];
			for(int i = transitionBN.getVarCount() + 1; i < (transitionBN.getVarCount()*2); i++) {
				futureValues[i - transitionBN.getVarCount() - 1] = transitionBN.getFutureValues(i, new Dataset(testFile.getTimeSlice(0)));			
			}
			for(int j = 0; j < testFile.subjectCount(); j++) { //cada coluna da matriz corresponde aos futures values das RVars para um instante de tempo
				out.println("-> instance " + (j+1) + ": ", toFile);
				for(int i = 0; i < testFile.randomVarCount(); i++) { //cada linha da matriz corresponde aos futures values de uma RVar		
					if(i == (testFile.randomVarCount()-1)) {
						out.println(String.valueOf(futureValues[i][j]), toFile); //para a ultima RVar ja nao se imprime a virgula
					}
					else {
						out.print(futureValues[i][j] + ", ", toFile);
					}
				}
			}	
		}
		else {
			int[] futureValues = new int[testFile.subjectCount()];
			futureValues = transitionBN.getFutureValues(varToInfere, new Dataset(testFile.getTimeSlice(0)));
			for(int j = 0; j < testFile.subjectCount(); j++) {
				out.println("-> instance " + (j+1) + ": " + futureValues[j], toFile);
			}
		}	
		
		elapsedTimeInfere = System.nanoTime() - startTime; //tempo que se demorou a inferir o modelo da DBN (sem a fase de aprendizagem do train set) 	
		out.println("Infering with DBN: " + elapsedTimeInfere*Math.pow(10, -9) + " seconds", toFile);
	}
	
	public static void main(String[] args) {
		
		boolean allVars = false;
		boolean toFile = true;
		int varToInfer = 0;
		
		SaveToFile out = null;
		
		if(args.length != 0) {
			String outputFile = "";
		
			for(String arg: args) {
				outputFile += arg + ' ';
			}
			if(toFile)
				out = new SaveToFile(outputFile, toFile);
		} else {
			toFile = false;
			out = new SaveToFile(" ", false);
		}
		
		if(args.length == 5) { //e especificada a RVar sobre a qual se pretende inferir
			out.println("Parameters: " + args[0] + " " + args[1] + " " + args[2] + " " + args[3] + " " + args[4], toFile);		
			varToInfer = Integer.parseInt(args[4]);
		}
		else if(args.length == 4) { //inferir para todas as RVars
			allVars = true;
			out.println("Parameters: " + args[0] + " " + args[1] + " " + args[2] + " " + args[3], toFile);
		} else {
			if(args.length == 1) {
				if(args[0].equals("-gui")) {				
					GUI.main(args);
					while(true);
				}				
			} 
			out.println("The program parameters must be given by: <train> <test> <score> <randtest> <var> to infer the random variable specified by <var>", toFile);
			out.println("				     or: <train> <test> <score> <randtest> to infer all random variables.", toFile);
			System.exit(0);
		}
		
		TransitionBayessianNetwork<RandomVariable> transitionBN = buildDBN(args[0], args[2], Integer.parseInt(args[3]), out, toFile);
		infereValue(args[1], allVars, varToInfer, transitionBN, out, toFile);
		
		out.close();
	}
}
