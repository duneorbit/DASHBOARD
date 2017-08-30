package com.jspeedbox.tooling.governance.dashboard;

public class WeightBucket {
	
	private int max = 0;
	private int taken = 0;
	
	private double weight = 0;
	
	public WeightBucket(int max, int available, double weight){
		this.max = max;
		this.taken = available;
		this.weight = weight;
	}
	
	public boolean isAvailable(){
		if(taken<max){
			return true;
		}
		return false;
	}
	
	public double take(){
		this.taken = taken + 1;
		return weight;
	}
}
