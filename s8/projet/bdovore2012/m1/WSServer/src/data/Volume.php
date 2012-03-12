<?php
	class Volume {		
	/**
	 * Tous les détails d'un volume (dont on a besoin pour le client)
	 * + correspondance avec les tables de la base côté serveur
	 */
		private $idTome; // => attribut de jointure
		private $titre; // => bd_volume : VOLUME
		private $idSerie; // => bd_volume : ID_SERIE
		private $numTome; // => bd_volume : NUM_TOME
		
		/*
		 * Constructeur
		 */
		public function Volume($nIdTome, $nTitre, $nIdSerie, $nNumTome){			
			$this->idTome = $nIdTome;
			$this->titre = $nTitre;
			$this->idSerie = $nIdSerie;
			$this->numTome = $nNumTome;
		}
	}
?>