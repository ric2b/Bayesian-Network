package tests;

import input.DataFileReader;

import java.util.Arrays;

import dataset.TimeSlice;
import dataset.TransitionDataset;
import bayessian.RandomVariable;

public class datasetTest {
	public static void main(String[] args) throws Exception {
		DataFileReader reader = new DataFileReader("short-train-data.csv");
		
		System.out.println("#Subjects: " + reader.subjectCount());
		System.out.println("#RVars: " + reader.randomVarCount());
		System.out.println("#TimeInstants: " + reader.timeInstantCount());
		
		int instantCount = reader.timeInstantCount();
		TimeSlice[] timeSlices = new TimeSlice[instantCount];
		
		for(int i = 0; i < instantCount; i++) {
			timeSlices[i] = reader.getTimeSlice(i);
			
			System.out.println("TimeSlice " + i);
			System.out.println(timeSlices[i]);
		}
		
		RandomVariable[] vars = reader.getRVars();
		System.out.println(Arrays.toString(vars));
		
		TransitionDataset dataset = new TransitionDataset(timeSlices);
		
		System.out.println(dataset);
	}
}

