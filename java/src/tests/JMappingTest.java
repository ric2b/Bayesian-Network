package tests;

import java.util.Arrays;

import bayessian.InstanceCounting;

public class JMappingTest {
	public static void main(String[] args) {
		int i;
		
		int[][] ranges =	{	{3,4,3}, 
								{2,2,2,2},
								{5},
								{1,3,2,3,2},
								{}
							};

		int[][] j =	{	{2,2,2},
						{0,0,1,1},
						{3},
						{0,1,1,2,1},
						{}
					};

		int[] J = {32, 12, 3, 34, 0};
		
		// j -> J
		int works = 1;
						
		for(i = 0; i < ranges.length; i++){
			if(InstanceCounting.mapjToJ(ranges[i], j[i]) != J[i]){
				works = 0;
				break;
			}
		}
		
		System.out.print("j to J: ");
		if(works == 1) 	System.out.println("YAY!");
		else			System.out.println("Nay  :( " + i);
		
		// J -> j
		works = 1;
		
		for(i = 0; i < j.length; i++){
			int [] tmp = InstanceCounting.mapJToj(ranges[i], J[i]);
			
			if(!Arrays.equals(tmp, j[i])){
				works = 0;
				break;
			}
		}
		
		System.out.print("J to j: ");
		if(works == 1) 	System.out.println("YAY!");
		else			System.out.println("Nay  :( " + i);
	}	
}
