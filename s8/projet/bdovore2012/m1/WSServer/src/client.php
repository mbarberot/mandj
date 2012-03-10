<?php
header('Content-Type: text/plain');

// On désactive la mise en cache du wsdl (pour le test)
ini_set('soap.wsdl_cache_enabled', 0);

try	{
	// Instanciation du client SOAP
	$client = new SoapClient(
			'server.wsdl', // Serveur distant : http://172.20.128.37/server.wsdl
			array(
					//'proxy_host'=>'http://monproxy.net', // si vous utilisez un proxy...
					//'proxy_port'=>8080, // si vous utilisez un proxy...
					'trace'=> 1,
					'soap_version'=> SOAP_1_1
			)
	);

	try{
		$retour_ws = $client->getBibliotheque('latruffe','bdovore');
		// Affichage du résultat
		echo "</br>".$retour_ws;
	}catch (SoapFault $e)
	{
		echo $e;
	}
	// Ancienne version : appel en précisant le nom de méthode et un tableau associatif de (variable,valeur)
	//$retour_ws =  $client->__call('setBibliotheque',array('bibliotheque',42));

	// Generera des exceptions attrapées plus bas
	//$retour_ws = $client->setBibliotheque(); // Paramètre manquant
	//$retour_ws = $client->getBibliotheque(1); // Paramètre en trop/non reconnu

	// Affichage des requêtes et réponses SOAP (pour debug)
	//echo 'Requete SOAP : '.$client->__getLastRequest();
	//echo 'Reponse SOAP : '.$client->__getLastResponse();


}
catch (Exception $e)	{
	//TODO  Traitement en cas d'exception, pour l'instant on l'affiche tel quel...
	echo $e;
}
?>