package db;


public class CodeBarre {
	
	/* On considere que l'ISBN n'a pas de tiret */
	/* Sinon on utilise removeTiret ou addTiret */

	static String removeTiret(String code) {
		String r = "";
		for (int i = 0; i < code.length(); i++) {
			if (code.charAt(i) != '-') r += code.charAt(i);
		}
		return r;
	}
	
	static String addTiret(String code) {
		if (code.length() == 13)
			return code.substring(0, 3) + "-" + code.charAt(3) + "-" + code.substring(4, 9) + "-"
					+ code.substring(9, 12) + "-" + code.charAt(12);
		if (code.length() == 10)
			return code.charAt(0) + "-" + code.substring(1, 6) + "-" + code.substring(6, 9) + "-"
					+ code.charAt(9);
		return code;
	}
	
	/* Donne la cle associee a un code barre EAN10 */
	/* Retourne : */
	/* -1 si il y a un probleme */
	/* Sinon de 0 à 10 (10 normalement doit être égale à X */
	static int computeCodeEAN10(String code) {
		int cle = 0;
		if (code.length() != 10) return -1;
		for (int i = 0; i < 9; i++)
			cle += (i + 1) * (((int) code.charAt(i)) - (int) '0');
		cle = cle % 11;
		return cle;
	}
	
	/* Retourne True si code est un code EAN10 Valide */
	/* False sinon */
	static boolean isCodeEAN10(String code) {
		int cle = computeCodeEAN10(code);
		boolean bo = false;
		
		switch (cle) {
			case -1: // erreur sur le code
				// bo = false
				break;
			case 10: // cas du X
				if (code.charAt(9) == 'X') bo = true;
				// else bo = false;
				break;
			default: // cas nomianl entre 0 et 9
				if (code.charAt(9) == (char) (((int) '0') + cle)) bo = true;
				// else bo = false;
		}
		return bo;
	}
	
	/* Donne la cle associee a un code barre EAN13 */
	/* Retourne : */
	/* -1 si il y a un probleme */
	/* Sinon de 0 à 0 */

	static int computeCodeEAN13(String code) {
		int clep = 0;
		int clei = 0;
		if (code.length() != 13) return -1;
		for (int i = 0; i < 12; i++) {
			if ((i % 2) == 0)
				clep += (((int) code.charAt(i)) - (int) '0');
			else clei += (((int) code.charAt(i)) - (int) '0');
		}
		clep += clei * 3;
		clep = 10 - clep % 10;
		return clep;
	}
	
	/* Retourne True si code est un code EAN13 Valide */
	/* False sinon */
	static boolean isCodeEAN13(String code) {
		int cle = computeCodeEAN13(code);
		boolean bo = false;
		
		switch (cle) {
			case -1: // erreur sur le code
				// bo = false;
				break;
			default: // cas nomianl entre 0 et 9
				if (code.charAt(12) == (char) (((int) '0') + cle)) bo = true;
				// else bo = false;
		}
		return bo;
	}
	
	
	/* Convertie un code barre en ISBN10 */
	/* Renvoie : le numero ISBN10 ou null */
	static String convertEAN2ISBN10(String code) {
		if (code.length() == 10) return code;
		if (code.length() == 13) {
			String s = code.substring(3, 12);
			int cle = computeCodeEAN10(code.substring(3, 13));
			if (cle == 10)
				s += "X";
			else s += (char) (((int) '0') + cle);
			return s;
		}
		return code;
	}
	
	/* Convertie un code barre en ISBN13 */
	/* Renvoie : le numero ISBN10 ou null */
	static String convertEAN2ISBN13(String code) {
		if (code.length() == 13) return code;
		if (code.length() == 10) {
			String s = "978" + code.substring(0, 9);
			s += (char) (((int) '0') + computeCodeEAN13("978" + code));
			return s;
		}
		return code;
	}
	
	
	/* Convertie un ISBN10 en ISBN13 */
	/* Renvoie : le numero ISBN13 ou null */
	static String convertISBN2ISBN13(String code) {
		return convertEAN2ISBN13(code);
	}
	
	
	
	
	/**
	 * Version BDovore : convertit vers la version ISBN (EAN/ISBN10, sans tirets)
	 */
	static String toBDovoreISBN(String code) {
		return convertEAN2ISBN10(removeTiret(code));
	}
	
	
	
	/**
	 * Version BDovore : convertit vers la version EAN (EAN13, sans tirets)
	 */
	static String toBDovoreEAN(String code) {
		return convertEAN2ISBN13(removeTiret(code));
	}
}
