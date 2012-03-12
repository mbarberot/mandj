<?php
	class Auteur
	{
		private $idAuteur;
		private $pseudo;
		private $nom;
		private $prenom;
		private $date_naissance;
		private $date_deces;
		private $nationalite;
		
		public function Auteur($nAuteur, $nPseudo, $nNom, $nPrenom, $nNaissance, $nDeces, $nNationalite){
			$this->idAuteur = $nAuteur;
			$this->pseudo = $nPseudo;
			$this->nom = $nNom;
			$this->prenom = $nPrenom;
			$this->date_naissance = $nNaissance;
			$this->date_naissance = $nDeces;
			$this->nationalite = $nNationalite;			
		}
	}
?>