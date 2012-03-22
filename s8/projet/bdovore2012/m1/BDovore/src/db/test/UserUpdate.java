package db.test;

import db.DataBase;


public class UserUpdate {
	
	public static void main(String[] args) {
		
		DataBase db = null;
		
		try {
			db = new DataBase("db/bdovore");
			System.out.println("DB Open.");
			
			
			//Updater up = new Updater(db);
			
			//db.query("SELECT * FROM BD_USER");
			
			
			db.shutdown();
			System.out.println("DB Closed.");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
