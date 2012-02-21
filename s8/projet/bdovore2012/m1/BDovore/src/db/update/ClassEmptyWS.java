package db.update;

import java.sql.Date;

public class ClassEmptyWS implements ConnectionWS {

	public ClassEmptyWS(){}
	
	public void ajouter(String userName, String userPass, int idEdition, int FLG_Pret, int FLG_Dedicace, int FLG_AAcheter, Date date) {
		
	}

	public void modifier(String userName, String userPass, int idEdition,	int FLG_Pret, int FLG_Dedicace, int FLG_AAcheter) {
		
	}

	public void supprimer(String userName, String userPass, int idEdition) {
		
	}
	
	// Les fonctions suivantes récuperent les infos du Web Service sous format string csv
	public String getInfosManquantesAuteurTome(String userName, String userPass, int idTome) {
		return "";
	}
	
	public String getInfosManquantesAuteur(String userName, String userPass, int idAuteur) {
		return "";
	}

	public String getInfosManquantesEdition(String userName, String userPass, int idEdition) {
		return "";
	}

	public String getInfosManquantesSerie(String userName, String userPass, int idSerie) {
		return "";
	}

	public String getListEditionsUser(String userName, String userPass) {
		return "";
	}

	public String getNewEntries(String userName, String userPass, String nomTable, int lastId) {
		return "";
	}

}
