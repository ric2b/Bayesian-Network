package tests;

import bayessian.InstanceCounting;

public class JMappingTest {
	public static void main(String[] args) {
		int[] ranges = {3,4,3};
		int[] j = {2,2,2};		
		int works = 1;
		
		if(InstanceCounting.mapjToJ(ranges, j) != 32)
			works = 0;
		
		int [] ranges2 = {2,2,2,2};
		int [] j2 = {0,0,1,1};		
		if(InstanceCounting.mapjToJ(ranges2, j2) != 12)
			works = 0;
		
		int [] ranges3 = {};
		int [] j3 = {};		
		if(InstanceCounting.mapjToJ(ranges3, j3) != 0)
			works = 0;
		
		if(works == 1) 	System.out.println("YAY!");
		else			System.out.println("Nay  :(");
		
	}	
}
