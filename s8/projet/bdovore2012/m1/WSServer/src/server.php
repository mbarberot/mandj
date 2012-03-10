<?php
// On d�sactive la mise en cache du wsdl (pour le test)
ini_set('soap.wsdl_cache_enabled', 0);

// Inclusion de la classe contenant l'impl�mentation des fonctions du Service Web
include('BDovore.class.php');

// Tentative d'instanciation du serveur SOAP
try {
	$server = new SoapServer('server.wsdl',  array('trace' => 1, 'soap_version' => SOAP_1_1));
	$server->setclass('BDovore');
}
catch (Exception $e) {
	//TODO  Traitement en cas d'exception, pour l'instant on l'affiche tel quel...
	echo $e;
	exit();
}

// Appel du Service Web (requ�te POST uniquement autoris�e)
if ($_SERVER['REQUEST_METHOD'] == 'POST') {
	// Ouverture de la connexion � la base de donn�es MySQL
	$db = getMySQLConnection();
	// Prise en charge de la requ�te
	$server -> handle();
	// Fermeture de la connexion une fois les op�rations termin�es
	closeMySQLConnection($db);
}
// Sinon, on affiche la liste des fonctions du serveur SOAP et un lien vers le WSDL
else {
	echo 'BDovore<br />';
	echo '<a href="server.wsdl">WSDL</a><br />';
	echo 'Fonctions :';
	echo '<ul>';
	foreach($server -> getFunctions() as $func) {
		echo '<li>' , $func , '</li>';
	}
	echo '</ul>';
}

/**
 * Ouverture de la connexion � la BDD locale.
 * Changer les param�tres de mysql_connect et mysql_select_db pour adapter la fonction � votre BDD.
 * @throws SoapFault
 */
function getMySQLConnection()
{
	// Cr�ation de la connexion � la BDD
	$db = mysql_connect('Localhost', 'root', '');
	if(!$db)
	{
		throw new SoapFault("CONNEXION_IMPOSSIBLE", $errors['CONNEXION_IMPOSSIBLE']);
	}

	// On s�lectionne la base
	mysql_select_db('bdovore',$db);

	return $db;
}

function closeMySQLConnection($db)
{
	mysql_close($db);
}

?>
