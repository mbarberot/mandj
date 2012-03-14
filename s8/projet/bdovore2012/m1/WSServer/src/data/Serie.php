<?php

	class Serie
	{
		
		/* Attributs */
		private $idSerie; // ID_SERIE : attribut de jointure
		private $nomSerie; // bd_serie : SERIE
		// private $idGenre; -> TODO : a retrouver dans la BDD du site 
		private $nbTomes; // bd_serie : NB_TOME
		private $flgFini; // bd_serie : FLG_FINI
		private $histoire; // bd_serie : HISTOIRE_SERIE
		
		/* Constructeur */
		public function Serie($nIdSerie, $nNom,$nNbTomes, $nFlgFini, $nHistoire){
			$this->idSerie = $nIdSerie;
			$this->nomSerie = $nNom;
			//$this->idGenre = $nIdGenre;
			$this->nbTomes = $nNbTomes;
			$this->flgFini = $nFlgFini;
			$this->histoire = $nHistoire;
		}
	}
?>