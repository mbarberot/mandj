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
	private $date_ajout;// => us_edition : DATE_AJOUT
	/*
	 * Attributs utilisés dans DETAILS_EDITION
	 */
	private $img_couv; // => bd_edition : IMG_COUV
	/*
	 * Attributs utilisés dans EDITION
	 */
	private $isbn; // => bd_edition : ISBN
	private $date_parution;// => bd_edition : DTE_PARUTION
	private $idEditeur; // => bd_edition (<-(ID_COLLECTION)->) ed_collection : ID_EDITEUR
	private $flag_default;// => bd_collection : FLG_DEFAUT

	/*
	 * Constructeur
	 */
	public function Edition($nId_edition, $nId_volume,$nflagPret, $nflagDed, $nflagAa,
	$ndate, $nimg, $nIsbn, $nDatePar,$nIdEd, $nflagDef){
			
		$this->idEdition = $nId_edition;
		$this->idTome = $nId_volume;
		$this->flag_pret = $nflagPret;
		$this->flag_dedicace = $nflagDed;
		$this->flag_aAcheter = $nflagAa;
		$this->date_ajout = $ndate;
		$this->img_couv = $nimg;
		$this->isbn = $nIsbn;
		$this->date_parution = $nDatePar;
		$this->idEditeur = $nIdEd;
		$this->flag_default = $nflagDef;
			
	}

}
?>