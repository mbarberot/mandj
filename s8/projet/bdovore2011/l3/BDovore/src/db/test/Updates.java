package db.test;

import db.DataBase;
import db.update.Updater;


public class Updates {
	
	public static void main(String[] args) {
		
		try {
			DataBase db = new DataBase("db/bdovore");
			
			System.out.println("DB Ouverte.");
			
			Updater up = new Updater(db);
			up.updateGlobal();
			
			db.shutdown();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
