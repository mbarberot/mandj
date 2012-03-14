<?php
/**
 * Fonctions du Webservice, décrites par server.wsdl
 */

class BDovore {

	/**
	 * Tableau associatif code_erreur => descriptif
	 * Permet de spécifier les erreurs rencontr�es lors de l'exécution des fonctions du service web
	 */
	public static $errors = Array(
			"CONNEXION_IMPOSSIBLE" => "La connexion à la base de données a échoué",
			"IDENTIFICATION_KO" => "Les identifiants sont incorrects",
			"ERREUR_REQUETE" => "La requête a échoué");

	/**
	 * Renvoie (sous forme de String), les ouvrages en possession de l'utilisateur pass� en param�tre
	 * @param String $userName : le login de l'utilisateur
	 * @param String $userPass : son mot de passe
	 * @throws SoapFault : exception levée en cas d'erreur
	 */
	public function getBibliotheque($userName, $userPass) {

		// On récupère l'identifiant de l'utilisateur
		$sqlGetUser = "SELECT ID_USER FROM user WHERE USERNAME = '{$userName}' AND PASSWORD =  '{$userPass}'";
		$reqGetUser = mysql_query($sqlGetUser);

		// On vérifie que la requete est bien effectu�e
		if(!$reqGetUser) {
			throw  new SoapFault("ERREUR_REQUETE", $errors["ERREUR_REQUETE"]);
		}

		// On vérifie l'identification et renvoie une erreur si elle est mauvaise
		if(mysql_num_rows($reqGetUser) != 1) {
			throw new SoapFault("IDENTIFICATION_KO", $errors["IDENTIFICATION_KO"]);
		}

		// On récupère l'identifiant
		$dataUser = mysql_fetch_assoc($reqGetUser);
		$idUser = $dataUser['ID_USER'];

		// Creation de la requete
		$sqlBib = "SELECT ID_EDITION
		FROM us_edition
		WHERE us_edition.ID_USER = {$idUser}";
	  
		$reqBib = mysql_query($sqlBib);

		if(!$reqBib) {
			throw  new SoapFault("ERREUR_REQUETE", $errors["ERREUR_REQUETE"]);
		}
	  
		while($dataBib = mysql_fetch_assoc($reqBib)){
			$res = $dataBib['ID_EDITION'].';'.$res ;
		}

		return $res;
	}

	/**
	 * Récupère les détails d'une édition
	 * @param $idEdition : l'identifiant de l'édition
	 * @return les détails de l'édition
	 */
	public function getDetailsEdition($idEdition, $userName, $userPass){
			
		// On récupère l'identifiant de l'utilisateur
		$sqlGetUser = "SELECT ID_USER FROM user WHERE USERNAME = '{$userName}' AND PASSWORD =  '{$userPass}'";
		$reqGetUser = mysql_query($sqlGetUser);

		// On vérifie que la requete est bien effectu�e
		if(!$reqGetUser) {
			throw  new SoapFault("ERREUR_REQUETE", $errors["ERREUR_REQUETE"]);
		}

		// On vérifie l'identification et renvoie une erreur si elle est mauvaise
		if(mysql_num_rows($reqGetUser) != 1) {
			throw new SoapFault("IDENTIFICATION_KO", $errors["IDENTIFICATION_KO"]);
		}

		// On récupère l'identifiant
		$dataUser = mysql_fetch_assoc($reqGetUser);
		$idUser = $dataUser['ID_USER'];
		
		// Préparation de la requête sql
		$sqlGetDetailsEdition = "SELECT ID_VOLUME, FLG_PRET, FLG_DEDICACE, FLG_ACHAT, DATE_AJOUT, IMG_COUV, ISBN, DTE_PARUTION, ID_EDITEUR, FLG_DEFAULT
		FROM us_edition, bd_edition, ed_collection
		WHERE us_edition.ID_USER = {$idUser} AND
		us_edition.ID_EDITION = {$idEdition} AND 
		us_edition.ID_EDITION = bd_edition.ID_EDITION AND 
		bd_edition.ID_COLLECTION = ed_collection.ID_COLLECTION";
		
		$reqGetDetailsEdition = mysql_query($sqlGetDetailsEdition);
		if(!$reqGetDetailsEdition) {
			throw  new SoapFault("ERREUR_REQUETE", $errors["ERREUR_REQUETE"]);
		}
		$dataDetails = mysql_fetch_assoc($reqGetDetailsEdition);
		
		// Création de l'objet contenant toutes les infos
		$res = new Edition($idEdition,$dataDetails["ID_VOLUME"], $dataDetails["FLG_PRET"], $dataDetails["FLG_DEDICACE"], 
		$dataDetails["FLG_ACHAT"], $dataDetails["DATE_AJOUT"], $dataDetails["IMG_COUV"], $dataDetails["ISBN"], $dataDetails["DATE_PARUTION"], 
		$dataDetails["ID_EDITEUR"], $dataDetails["FLG_DEFAULT"]);
		
		return $res;
	}
		
	/**
	 * Récupère les détails d'un tome
	 * @param int $idTome
	 */
	public function getDetailsTome($idTome){

		// Préparation de la requête SQL
		$sqlDetailsTome = 
		"SELECT VOLUME, ID_SERIE, NUM_TOME 
		FROM bd_volume
		WHERE ID_VOLUME = {$idTome}";
		
		$reqDetailsTome = mysql_query($sqlDetailsTome);
		if(!$reqDetailsTome) {
			throw  new SoapFault("ERREUR_REQUETE", $errors["ERREUR_REQUETE"]);
		}
		
		$dataDetails = mysql_fetch_assoc($reqDetailsTome);
		
		// Création de l'objet contenant toutes les infos
		$res = new Volume($idTome, $dataDetails["VOLUME"], $dataDetails["ID_SERIE"], $dataDetails["NUM_TOME"]);

		return $res;
	}
	
	/**
	 * Récupère les scénaristes d'un tome
	 * @param int $idTome
	 * @return une chaine contenant tous les scénaristes (séparés par un point-virgule)
	 */
	public function getScenaristesTome($idTome){
		
		// Préparation de la requête SQL
		$sqlScenaristes = 
		"SELECT ID_AUTEUR 
		FROM bd_volume_auteur
		WHERE ID_VOLUME = {$idTome}
		AND ID_ROLE = 1";
		
		$reqScenaristes = mysql_query($sqlScenaristes);
		
		if(!$reqScenaristes) {
			throw new SoapFault("ERREUR_REQUETE", $errors["ERREUR_REQUETE"]);
		}
		
		while($data = mysql_fetch_assoc($reqScenaristes)){
			$res = $data["ID_AUTEUR"].';'.$res;
		}
		
		return $res;
	}
	
	/**
	 * Récupère les dessinateurs d'un tome
	 * @param int $idTome
	 * @return une chaine contenant tous les dessinateurs (séparés par un point-virgule)
	 */
	public function getDessinateursTome($idTome){
		$sqlDessinateurs = 
		"SELECT ID_AUTEUR 
		FROM bd_volume_auteur
		WHERE ID_VOLUME = {$idTome}
		AND ID_ROLE = 2";
		
		$reqDessinateurs = mysql_query($sqlDessinateurs);
		
		if(!$reqDessinateurs) {
			throw new SoapFault("ERREUR_REQUETE", $errors["ERREUR_REQUETE"]);
		}
		
		while($data = mysql_fetch_assoc($reqScenaristes)){
			$res = $data["ID_AUTEUR"].';'.$res;
		}
		
		return $res;		
		
	}
	
	/**
	 * Récupère les coloristes d'un tome
	 * @param int $idTome
	 * @return une chaine contenant tous les coloristes (séparés par un point-virgule)
	 */
	public function getColoristesTome($idTome){
		$sqlColoristes = 
		"SELECT ID_AUTEUR 
		FROM bd_volume_auteur
		WHERE ID_VOLUME = {$idTome}
		AND ID_ROLE = 3";
		
		$reqColoristes = mysql_query($sqlColoristes);
		
		if(!$reqColoristes) {
			throw new SoapFault("ERREUR_REQUETE", $errors["ERREUR_REQUETE"]);
		}
		
		while($data = mysql_fetch_assoc($reqScenaristes)){
			$res = $data["ID_AUTEUR"].';'.$res;
		}
		
		return $res;
		
	}
	
	/**
	 * Récupère les détails d'un auteur
	 * (cf. Auteur.php pour ces détails)
	 */
	public function getDetailsAuteur($idAuteur){
		$sqlDetailsAuteur = "SELECT PSEUDO, NOM, PRENOM, DATE_NAISS, DATE_DECES
		FROM auteur
		WHERE auteur.ID_AUTEUR = {$idAuteur}";
		
		$reqDetailsAuteur = mysql_query($sqlDetailsAuteur);
		
		if(!$reqDetailsAuteur) {
			throw new SoapFault("ERREUR_REQUETE", $errors["ERREUR_REQUETE"]);
		}
		
		$dataDetails = mysql_fetch_assoc($reqDetailsAuteur);
		
		// Création de l'objet contenant les infos
		$res = new Auteur($idAuteur, $dataDetails["PSEUDO"], $dataDetails["NOM"], $dataDetails["PRENOM"]
		, $dataDetails["DATE_NAISS"], $dataDetails["DATE_DECES"]);
		
		return $res;
	}
	
	/**
	 * Récupère les détails d'une série
	 * (cf Serie.php pour ces détails)
	 */
	public function getDetailsSerie($idSerie){
		$sqlDetailsSerie = "SELECT SERIE, NB_TOME, FLG_FINI, HISTOIRE_SERIE
		FROM bd_serie
		WHERE ID_SERIE = {$idSerie}";
		
		$reqDetailsSerie = mysql_query($sqlDetailsSerie);
		
		if(!$reqDetailsSerie){
			throw new SoapFault("ERREUR_REQUETE", $errors["ERREUR_REQUETE"]);
		}
		
		$dataDetails = mysql_fetch_assoc($reqDetailsSerie);
		
		// Création de l'objet contenant les infos
		$res = new Serie($idSerie, $dataDetails["SERIE"],$dataDetails["NB_TOME"], $dataDetails["FLG_FINI"], $dataDetails["HISTOIRE_SERIE"]);

		return $res;
	}
	
	/**
	 * Récupère les détails d'un éditeur
	 * (cf Editeur.php pour ces détails)
	 */
	public function getDetailsEditeur($idEditeur){
		$sqlDetailsEditeur = "SELECT EDITEUR, URL_SITE FROM ed_editeur WHERE ID_EDITEUR = {$idEditeur}";
		
		$reqDetailsEditeur = mysql_query($sqlDetailsEditeur);
		
		if(!$reqDetailsEditeur){
			throw new SoapFault("ERREUR_REQUETE", $errors["ERREUR_REQUETE"]);
		}
		
		$dataDetails = mysql_fetch_assoc($reqDetailsEditeur);
		
		// Création de l'objet contenant les infos
		$res = new Editeur($idEditeur, $dataDetails["EDITEUR"], $dataDetails["URL_SITE"]);
		
		return $res;
	}
	
	/**
	 * Récupère l'intitulé d'un genre
	 * @param int $idGenre
	 * @return String l'intitulé
	 */
	public function getGenre($idGenre){
		$sqlGenre = "SELECT GENRE FROM vo_genre WHERE ID_GENRE = {$idGenre}";
		$reqGenre = mysql_query($sqlGenre);
		
		if(!$reqGenre){
			throw new SoapFault("ERREUR_REQUETE", $errors["ERREUR_REQUETE"]);
		}
		
		$dataDetails = mysql_fetch_assoc($reqGenre);
		$res = $dataDetails["GENRE"];
		
		return $res;		
	}
}


?>
