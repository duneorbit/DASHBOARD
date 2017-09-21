package com.jspeedbox.test;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class TestCloudConnection {
	
	public static void main(String[] args){
		
//		MongoClientURI uri = new MongoClientURI("mongodb://duneorbit:Thelongcut@1@duneorbit-shard-00-00-dwjjr.mongodb.net:27017,"
//				+ "duneorbit-shard-00-01-dwjjr.mongodb.net:27017,"
//				+ "duneorbit-shard-00-02-dwjjr.mongodb.net:27017/test?ssl=true&replicaSet=Duneorbit-shard-0&authSource=admin");
//		
//		MongoClient client = new MongoClient(uri);
//		MongoDatabase database = client.getDatabase("test");
		
		MongoClientURI uri = new MongoClientURI("mongodb://duneorbit:Thelongcut!1@duneorbit-shard-00-00-dwjjr.mongodb.net:27017,"
				+ "duneorbit-shard-00-01-dwjjr.mongodb.net:27017,"
				+ "duneorbit-shard-00-02-dwjjr.mongodb.net:27017/test?ssl=true&replicaSet=Duneorbit-shard-0&authSource=admin");

		MongoClient mongoClient = new MongoClient(uri);
		MongoDatabase database = mongoClient.getDatabase("test");
		System.out.println(database.getName());
		database.createCollection("UserProfiles");
//		MongoCollection<Document> userProfilesCollection = database.getCollection("UserProfiles");
//		System.out.println(userProfilesCollection.count());
				
		
	}

}
