<?php
/**
 * Fonctions du Webservice, décrites par server.wsdl
 */

class BDovore {

	/**
	 * Tableau associatif code_erreur => descriptif
	 * Permet de spécifier les erreurs rencontrées lors de l'exécution des fonctions du service web
	 */
	public static $errors = Array(
			"CONNEXION_IMPOSSIBLE" => "La connexion à la base de données a échoué",
			"IDENTIFICATION_KO" => "Les identifiants sont incorrects",
			"ERREUR_REQUETE" => "La requête a échoué");

	/**
	 * Renvoie (sous forme de String), les ouvrages en possession de l'utilisateur passé en paramètre
	 * @param String $userName : le login de l'utilisateur
	 * @param String $userPass : son mot de passe
	 * @throws SoapFault : exception levée en cas d'erreur
	 */
	public function getBibliotheque($userName, $userPass) {

		// On récupère l'identifiant de l'utilisateur
		$sqlGetUser = "SELECT ID_USER FROM user WHERE USERNAME = '{$userName}' AND PASSWORD =  '{$userPass}'";
		$reqGetUser = mysql_query($sqlGetUser);

		// On vérifie que la requete est bien effectuée
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

}


?>
