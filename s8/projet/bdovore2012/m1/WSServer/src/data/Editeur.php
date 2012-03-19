<?php
	class Editeur{
		/* Attributs */
		private $idEditeur; // ed_editeur : ID_EDITEUR
		private $nomEditeur; // ed_editeur : EDITEUR
		private $url;// ed_editeur : URL_SITE (nullable)
		
		public function Editeur($nIdEditeur, $nNomEditeur, $nUrl){
			$this->idEditeur = $nIdEditeur;
			$this->nomEditeur = $nNomEditeur;
			($nUrl == NULL)? $this->url ="" : $this->url = $nUrl;
		}
	}
?>