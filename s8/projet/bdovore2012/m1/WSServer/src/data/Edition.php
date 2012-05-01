<?php

class Edition{
	/**
	 * Tous les détails d'une édition (dont on a besoin pour le client)
	 * + correspondance avec les tables de la base côté serveur
	 */

	/*
	 * Attributs utilisés dans BD_USER
	 */
	private $idEdition; // => attribut de jointure
	private $idTome; // => bd_edition : ID_VOLUME
	private $flag_pret;// => us_edition : FLG_PRET
	private $flag_dedicace;// => us_edition : FLG_DEDICACE
	private $flag_aAcheter;// => us_edition : FLG_ACHAT
	private $date_ajout;// => us_edition : DATE_AJOUT (nullable)
	/*
	 * Attributs utilisés dans DETAILS_EDITION
	 */
	private $img_couv; // => bd_edition : IMG_COUV (nullable)
	/*
	 * Attributs utilisés dans EDITION
	 */
	private $isbn; // => bd_edition : ISBN (nullable)
	private $date_parution;// => bd_edition : DTE_PARUTION (nullable)
	private $idEditeur; // => bd_edition (nullable) (<-(ID_COLLECTION)->) ed_collection : ID_EDITEUR
	private $flag_default;// => bd_collection : FLG_DEFAUT

	/*
	 * Constructeur
	 */
	public function Edition($nId_edition, $nId_volume,$nflagPret, $nflagDed, $nflagAa,
	$ndate, $nimg, $nIsbn, $nDatePar,$nIdEd, $nflagDef){
			
		$this->idEdition = $nId_edition;
		($nId_volume == NULL)? $this->idTome = -1 : $this->idTome = $nId_volume;
		($nflagPret == NULL)? $this->flag_pret = 0 : $this->flag_pret = $nflagPret;
		($nflagDed == NULL)? $this->flag_dedicace = 0 : $this->flag_dedicace = $nflagDed;
		($nflagAa == NULL)? $this->flag_aAcheter = 0 : $this->flag_aAcheter = $nflagAa;
		($ndate == NULL)? $this->date_ajout = "" : $this->date_ajout = $ndate;
		($nimg == NULL)? $this->img_couv = "" : $this->img_couv = $nimg;
		($nIsbn == NULL)? $this->isbn = "" : $this->isbn = $nIsbn;
		($nDatePar == NULL)? $this->date_parution = "" : $this->date_parution = $nDatePar;
		($nIdEd == NULL)? $this->idEditeur = -1 : $this->idEditeur = $nIdEd;
		($nflagDef == NULL)? $this->flag_default = 0 : $this->flag_default = $nflagDef;
			
	}
        
        public function getIdEdition() {
            return $this->idEdition;
        }

        public function getIdTome() {
            return $this->idTome;
        }

        public function getFlag_pret() {
            return $this->flag_pret;
        }

        public function getFlag_dedicace() {
            return $this->flag_dedicace;
        }

        public function getFlag_aAcheter() {
            return $this->flag_aAcheter;
        }

        public function getDate_ajout() {
            return $this->date_ajout;
        }

        public function getImg_couv() {
            return $this->img_couv;
        }

        public function getIsbn() {
            return $this->isbn;
        }

        public function getDate_parution() {
            return $this->date_parution;
        }

        public function getIdEditeur() {
            return $this->idEditeur;
        }

        public function getFlag_default() {
            return $this->flag_default;
        }



}
?>