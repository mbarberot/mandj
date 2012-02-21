package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.h2.jaqu.Table;

import db.data.*;
import db.update.Updater;



public class Launcher {
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		
		DataBase db = null;
		
		try {
			db = new DataBase("db/bdovore");
			//System.out.println("DB Open.");
			
			/*System.out.println(CodeBarre.isCodeEAN10("2864971895"));
			System.out.println(CodeBarre.isCodeEAN13("2864971895"));
			*/
			/*String sql = SearchQuery.searchNothing(SearchQuery.SEARCH_IN_OWNED, SearchQuery.GET_FIELDS, "t.TITRE", SearchQuery.ORDER_ASC);
			System.out.println("requete: \n\n"+sql+"\n\n");
			
			db.query(sql);/**/
			
			/*db.query(StatsQuery.total());
			db.query(StatsQuery.totalOwned());
			db.query(StatsQuery.totalWanted());

			db.query(StatsQuery.genres());
			db.query(StatsQuery.editeurs());
			db.query(StatsQuery.dessinateurs());
			db.query(StatsQuery.scenaristes());
			db.query(StatsQuery.coloristes());/**/
			
			/*db.query(InfoQuery.getAuteurs(1, "coloriste"));
			db.query(InfoQuery.getEditions(1));
			db.query(InfoQuery.getSerie(1));
			db.query(InfoQuery.getEditionsPossedees(100));/**/
			
			/*Statistics s = db.statistics();
			
			System.out.println("Total : " + s.getTotal());
			System.out.println("Editeurs : " + s.getEditeurs().toString());/**/

			
			db.shutdown();
			//System.out.println("DB Closed.");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
