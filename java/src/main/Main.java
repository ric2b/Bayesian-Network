package main;

import java.io.IOException;

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
		
		System.out.println("Parameters: " + args[0] + args[1] + args[2] + Integer.parseInt(args[4]));
		
		long start = System.nanoTime();  
		
		//arg[0] - filename do train dataset
		DataFileReader trainFile = null;
		try {
			trainFile = new DataFileReader(args[0]);
		} catch (IOException except) {
			System.out.println(except.getMessage());
		}
		
		//arg[1] - filename do test dataset
		DataFileReader testFile = null;
		try {
			testFile = new DataFileReader(args[1]);
		} catch (IOException except) {
			System.out.println(except.getMessage());
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
			System.out.println("o score deve ser dado por <MDL> ou <LL>");
		}
			
		int instantCount = trainFile.timeInstantCount();
		TimeSlice[] timeSlices = new TimeSlice[instantCount];
		for(int i = 0; i < instantCount; i++) {
			timeSlices[i] = trainFile.getTimeSlice(i);
		}
		
		RandomVariable[] vars = trainFile.getRVars();	
		
		TransitionDataset dataset = null;
		try {
			dataset = new TransitionDataset(timeSlices);
		} catch (Exception except) {
			System.out.println(except.getMessage());
		}	
		TransitionBayessianNetwork<RandomVariable> transitionBN = new TransitionBayessianNetwork<RandomVariable>(vars, dataset, score); 
		
		long elapsedTime = System.nanoTime() - start; //tempo que se demorou a construir a o modelo da DBN (sem inferir o test set) 	
		System.out.println("Building DBN: " + elapsedTime);
		
		//System.out.println("Transition network: ");
		
		//arg[3] - maximum number of random restarts
		//bonus
		
		start = System.nanoTime();    
		System.out.println("Performing inference:");
		
		//arg[4] - var		
		if(args[4] == null) { //var is not given - all indexes from 1 to n should be considered
			int[][] futureValues = new int[testFile.randomVarCount()][testFile.subjectCount()];
			for(int i = 0; i < testFile.randomVarCount(); i++) {
				futureValues[i] = transitionBN.getFutureValues(i, new Dataset(testFile.getTimeSlice(0)));
			}
		}
		else {
			int[] futureValues = new int[testFile.subjectCount()];
			futureValues = transitionBN.getFutureValues(Integer.parseInt(args[4]), new Dataset(testFile.getTimeSlice(0)));
			for(int i = 0; i < testFile.subjectCount(); i++) {
				System.out.println("-> instance " + (i+1) + ": " + futureValues[i]);
			}
		}	
		
		elapsedTime = System.nanoTime() - start; //tempo que se demorou a inferir o modelo da DBN (sem a fase de aprendizagem do train set) 	
		System.out.println("Infering with DBN: " + elapsedTime);
	}
}
