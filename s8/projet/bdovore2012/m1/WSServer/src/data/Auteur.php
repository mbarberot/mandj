<?php
	class Auteur
	{
		private $idAuteur; // Attribut de jointure
		private $pseudo; // auteur : PSEUDO 
		private $nom; // auteur : NOM (nullable)		
		private $prenom; // auteur : PRENOM (nullable)
		private $date_naissance; // auteur : DATE_NAISS (nullable)
		private $date_deces; // auteur : DATE_DECES (nullable)
		private $nationalite;// auteur <- ID_PAYS -> pays : PAYS (nullable)
		
		/* Constructeur */
		public function Auteur($nAuteur, $nPseudo, $nNom, $nPrenom, $nNaissance, $nDeces, $nNationalite){
			$this->idAuteur = $nAuteur;
			$this->pseudo = $nPseudo;
			($nNom == NULL)? $this->nom = "" : $this->nom = $nNom;
			($nPrenom == NULL)? $this->prenom = "" : $this->prenom = $nPrenom;
			($nNaissance == NULL)? $this->date_naissance = "" : $this->date_naissance = $nNaissance;
			($nDeces == NULL)? $this->date_deces = "" : $this->date_deces = $nDeces;
			($nNationalite == NULL)? $this->nationalite = "" : $this->nationalite = $nNationalite;			
		}
	}
?>