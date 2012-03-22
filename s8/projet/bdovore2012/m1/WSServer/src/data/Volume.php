<?php
	class Volume {		
	/**
	 * Tous les détails d'un volume (dont on a besoin pour le client)
	 * + correspondance avec les tables de la base côté serveur
	 */
		private $idTome; // => attribut de jointure
		private $titre; // => bd_volume : VOLUME (nullable)
		private $idSerie; // => bd_volume : ID_SERIE
		private $numTome; // => bd_volume : NUM_TOME (nullable)
		private $idGenre; // => bd_volume_genre : ID_GENRE (nullable)
		/*
		 * Constructeur
		 */
		public function Volume($nIdTome, $nTitre, $nIdSerie, $nNumTome, $nIdGenre){			
			$this->idTome = $nIdTome;
			($nTitre == NULL)? $this->titre = "" : $this->titre = $nTitre;
			$this->idSerie = $nIdSerie;
			($nNumTome == NULL)? $this->numTome = -1 : $this->numTome = $nNumTome;
			($nIdGenre == NULL)? $this->idGenre = -1 : $this->idGenre = $nIdGenre;
		}
	}
?>