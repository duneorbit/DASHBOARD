package com.jspeedbox.tooling.governance.dashboard;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class FontWeightBucket {
	
	private static Map<String, WeightBucket> bucket = new HashMap<String, WeightBucket>();
	
	private static void buildCleanBucket(){
		bucket.put("0", new WeightBucket(2,0,13));
		bucket.put("1", new WeightBucket(2,0,10.5));
		bucket.put("2", new WeightBucket(2,0,9.4));
		bucket.put("3", new WeightBucket(2,0,8));
		bucket.put("4", new WeightBucket(2,0,6.2));
		bucket.put("5", new WeightBucket(4,0,5));
		bucket.put("6", new WeightBucket(5,0,4));
		bucket.put("7", new WeightBucket(7,0,3));
		bucket.put("8", new WeightBucket(10,0,2));
		bucket.put("9", new WeightBucket(100,0,1));
	}
	
	public static void reset(){
		bucket.clear();
		buildCleanBucket();
	}
	
	public static void buildSizeIndex(List<String> words, WordWrapper wordWrapper){
		for(String word : words){
			String cleanWord = word.replace("~", " ");
			wordWrapper.addCloudWord(new CloudWord(cleanWord, randomBucketChoice()));
		}
	}
	
	private static String randomBucketChoice(){
		return getAvailableBucket();
	}
	
	private static String getRandomKey(){
		Random ran = new Random();
		return String.valueOf(ran.nextInt(0 + 10));
	}
	
	private static String getAvailableBucket(){
		String key = getRandomKey();
		if(bucket.get(key).isAvailable()){
			double weight = bucket.get(key).take();
			return String.valueOf(weight);
		}else{
			return getAvailableBucket();
		}
	}

}
