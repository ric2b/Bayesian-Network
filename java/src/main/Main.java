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
		else {
			score = new LLScore();
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
		
		//arg[3] - maximum number of random restarts
		//bonus
		
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
		}	
	}
}
