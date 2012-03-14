<?php
	class Auteur
	{
		private $idAuteur; // Attribut de jointure
		private $pseudo; // auteur : PSEUDO
		private $nom; // auteur : NOM		
		private $prenom; // auteur : PRENOM
		private $date_naissance; // auteur : DATE_NAISS
		private $date_deces; // auteur : DATE_DECES
		
		/*
		private $nationalite;// auteur <- ID_PAYS -> pays : PAYS
		=> TODO : à voir : apparemment, tous les ID_PAYS dans auteur sont à NULL
		*/
		
		/* Constructeur */
		public function Auteur($nAuteur, $nPseudo, $nNom, $nPrenom, $nNaissance, $nDeces){
			$this->idAuteur = $nAuteur;
			$this->pseudo = $nPseudo;
			$this->nom = $nNom;
			$this->prenom = $nPrenom;
			$this->date_naissance = $nNaissance;
			$this->date_deces = $nDeces;
			/*$this->nationalite = $nNationalite;*/			
		}
	}
?>