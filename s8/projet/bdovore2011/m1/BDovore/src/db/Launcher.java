package db;

import db.data.Statistics;



public class Launcher {
	
	public static void main(String[] args) {
		
		DataBase db = null;
		
		try {
			db = new DataBase("db/bdovore");
			System.out.println("DB Open.");
			
			Statistics s = db.statistics();
			
			System.out.println("Total : " + s.getTotal());
			System.out.println("Editeurs : " + s.getEditeurs().toString());
			
			db.shutdown();
			System.out.println("DB Closed.");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
