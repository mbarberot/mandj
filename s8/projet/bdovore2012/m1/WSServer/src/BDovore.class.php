<?php
/**
 * Fonctions du Webservice, d�crites par server.wsdl
 */

class BDovore {

	/**
	 * Tableau associatif code_erreur => descriptif
	 * Permet de sp�cifier les erreurs rencontr�es lors de l'ex�cution des fonctions du service web
	 */
	public static $errors = Array(
			"CONNEXION_IMPOSSIBLE" => "La connexion � la base de donn�es a �chou�",
			"IDENTIFICATION_KO" => "Les identifiants sont incorrects",
			"ERREUR_REQUETE" => "La requ�te a �chou�");

	/**
	 * Renvoie (sous forme de String), les ouvrages en possession de l'utilisateur pass� en param�tre
	 * @param String $userName : le login de l'utilisateur
	 * @param String $userPass : son mot de passe
	 * @throws SoapFault : exception lev�e en cas d'erreur
	 */
	public function getBibliotheque($userName, $userPass) {

		// On r�cup�re l'identifiant de l'utilisateur
		$sqlGetUser = "SELECT ID_USER FROM user WHERE USERNAME = '{$userName}' AND PASSWORD =  '{$userPass}'";
		$reqGetUser = mysql_query($sqlGetUser);

		// On v�rifie que la requete est bien effectu�e
		if(!$reqGetUser) {
			throw  new SoapFault("ERREUR_REQUETE", $errors["ERREUR_REQUETE"]);
		}

		// On v�rifie l'identification et renvoie une erreur si elle est mauvaise
		if(mysql_num_rows($reqGetUser) != 1) {
			throw new SoapFault("IDENTIFICATION_KO", $errors["IDENTIFICATION_KO"]);
		}

		// On r�cup�re l'identifiant
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
