package db.test;

import db.DataBase;
import db.data.User;
import db.update.Updater;


public class UserUpdate {
	
	public static void main(String[] args) {
		
		DataBase db = null;
		
		try {
			db = new DataBase("db/bdovore");
			System.out.println("DB Open.");
			
			User us = new User("Malicia","caf973c16410b87b3a996405f421ec14");
			
			Updater up = new Updater(db);
			up.getUserData(us);
			
			//db.query("SELECT * FROM USERS_ALBUM");
			
			
			db.shutdown();
			System.out.println("DB Closed.");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
