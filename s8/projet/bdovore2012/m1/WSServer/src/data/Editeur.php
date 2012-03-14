<?php
	class Editeur{
		/* Attributs */
		private $idEditeur; // ed_editeur : ID_EDITEUR
		private $nomEditeur; // ed_editeur : EDITEUR
		private $url;// ed_editeur : URL_SITE
		
		public function Editeur($nIdEditeur, $nNomEditeur, $nUrl){
			$this->idEditeur = $nIdEditeur;
			$this->nomEditeur = $nNomEditeur;
			$this->url = $nUrl;
		}
	}
?>