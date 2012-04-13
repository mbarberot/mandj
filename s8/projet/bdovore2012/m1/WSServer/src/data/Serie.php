<?php

	class Serie
	{
		
		/* Attributs */
		private $idSerie; // ID_SERIE : attribut de jointure
		private $nomSerie; // bd_serie : SERIE
		private $nbTomes; // bd_serie : NB_TOME (nullable)
		private $flgFini; // bd_serie : FLG_FINI (nullable)
		private $histoire; // bd_serie : HISTOIRE_SERIE (nullable)
		
		/* Constructeur */
		public function Serie($nIdSerie, $nNom,$nNbTomes, $nFlgFini, $nHistoire){
			$this->idSerie = $nIdSerie;
			$this->nomSerie = $nNom;
			($nNbTomes == NULL)?  $this->nbTomes = 1 : $this->nbTomes = $nNbTomes;
			($nFlgFini == NULL)? $this->flgFini = 0 : $this->flgFini = $nFlgFini;
			($nHistoire == NULL)? $this->histoire = $nHistoire= "" : $this->histoire = $nHistoire;
		}
	}
?>