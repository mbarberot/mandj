package db.test;

import db.SearchQuery;


public class SearchQueries {
	
	public static void main(String[] args) {
		
		//System.out.println(SearchQuery.searchTitre("ast'erix"));
		System.out.println(SearchQuery.searchTitre(SearchQuery.SEARCH_IN_ALL,"Asterix",SearchQuery.GET_FIELDS,"t.TITRE",SearchQuery.ORDER_ASC));
		//System.out.println(SearchQuery.searchSerie("Slayers"));
		//System.out.println(SearchQuery.searchAuteur("KANZAKA"));
		
		// Note : le titre du bouquin m'intéresse.
		//System.out.println(SearchQuery.searchISBN("2869676115"));
		
		// Youpi ça marche avec les ALPHA en clé !
		//System.out.println(SearchQuery.searchISBN("287687122X"));
	}
	
}
