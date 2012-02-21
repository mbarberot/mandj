package db.update;

import java.sql.Date;

public interface ConnectionWS {
	/** Ajoute une édition dans la base de l'utilisateur du site
	 * 
	 * @param userName
	 * @param userPass
	 * @param idEdition
	 * @param FLG_Pret
	 * @param FLG_Dedicace
	 * @param FLG_AAcheter
	 * @param date
	 */
	public void ajouter(String userName, String userPass, int idEdition, int FLG_Pret, int FLG_Dedicace, int FLG_AAcheter, Date date);
	
	/** Modifie une édition de la base de l'utilisateur du site
	 * 
	 * @param userName
	 * @param userPass
	 * @param idEdition
	 * @param FLG_Pret
	 * @param FLG_Dedicace
	 * @param FLG_AAcheter
	 */
	public void modifier(String userName, String userPass, int idEdition, int FLG_Pret, int FLG_Dedicace, int FLG_AAcheter);
	
	/** Supprime une édition de la base de l'utilisateur du site
	 * 
	 * @param userName
	 * @param userPass
	 * @param idEdition
	 */
	public void supprimer(String userName, String userPass, int idEdition);
	
	/** Renvoi l'url de l'image de l'édition pour details_edition
	 * 
	 * @param userName
	 * @param userPass
	 * @param idEdition
	 * @return
	 */
	public String getInfosManquantesEdition(String userName, String userPass, int idEdition);
	
	/** Renvoi les auteurs du tome 
	 * 
	 * @param userName
	 * @param userPass
	 * @param idTome
	 * @return
	 */
	public String getInfosManquantesAuteurTome(String userName, String userPass, int idTome);
	
	/** Renvoi les infos supplémentaires d'un auteur (date naissance, date deces, nationalite)
	 * 
	 * @param userName
	 * @param userPass
	 * @param idAuteur
	 * @return
	 */
	public String getInfosManquantesAuteur(String userName, String userPass, int idAuteur);
	
	/** Renvoi les infos supplémentaires d'une série (nbTomes, avancement,histoire)
	 * 
	 * @param userName
	 * @param userPass
	 * @param idSerie
	 * @return
	 */
	public String getInfosManquantesSerie(String userName, String userPass, int idSerie);
	
	/** Renvoi la liste des éditions possédés par l'user sur le site bdovore.com
	 * 
	 * @param userName
	 * @param userPass
	 * @return
	 */
	public String getListEditionsUser(String userName, String userPass);
	
	/** Renvoi la liste des nouveaux tuples présent dans la table de la base du site 
	 * en fonction du dernier id que l'on avait dans la table locale
	 * 
	 * @param userName
	 * @param userPass
	 * @param nomTable
	 * @param lastId
	 * @return
	 */
	public String getNewEntries(String userName, String userPass, String nomTable, int lastId);
}
