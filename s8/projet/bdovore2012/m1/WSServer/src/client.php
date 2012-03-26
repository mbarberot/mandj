<?php

// On d�sactive la mise en cache du wsdl (pour le test)
ini_set('soap.wsdl_cache_enabled', 0);

// Inclusion des classes
include('data/Edition.php');
include('data/Volume.php');
include('data/Auteur.php');
include('data/Serie.php');
include('data/Editeur.php');

try	{
	// Instanciation du client SOAP
	$client = new SoapClient(
			'server.wsdl', // Serveur distant : http://172.20.128.37/server.wsdl
			array(
					//'proxy_host'=>'http://monproxy.net', // si vous utilisez un proxy...
					//'proxy_port'=>8080, // si vous utilisez un proxy...
					'trace'=> 1,
					'soap_version'=> SOAP_1_1,
					'classmap' => array('detailsEdition' => 'Edition',
										'detailsVolume' => 'Volume',
										'detailsSerie' => 'Serie',
										'detailsEditeur' => 'Editeur')
			)
	);

	try{
		$res = $client->getDetailsEdition(7);
		var_dump($res);
	}catch (SoapFault $e)
	{
		echo $e;
	}
	// Ancienne version : appel en pr�cisant le nom de m�thode et un tableau associatif de (variable,valeur)
	//$retour_ws =  $client->__call('setBibliotheque',array('bibliotheque',42));

	// Generera des exceptions attrap�es plus bas
	//$retour_ws = $client->setBibliotheque(); // Param�tre manquant
	//$retour_ws = $client->getBibliotheque(1); // Param�tre en trop/non reconnu

	// Affichage des requ�tes et r�ponses SOAP (pour debug)
	echo 'Requete SOAP : '.$client->__getLastRequest();
	echo 'Reponse SOAP : '.$client->__getLastResponse();


}
catch (Exception $e)	{
	//TODO  Traitement en cas d'exception, pour l'instant on l'affiche tel quel...
	echo $e;
}
?>