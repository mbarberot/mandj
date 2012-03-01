package db;

/**
 * Classe représentant un code barre (EAN/ISBN)<br/>
 * Elle contient des fonctions statiques permettant la manipulation et la conversion des codes barres en divers formats.
 */
public class CodeBarre {

    /**
     * Retire les tiret d'un code barre (ISBN)
     * @param code Le code, avec les tirets
     * @return Le même code, sans les tirets
     */
    public static String removeTiret(String code) {
        if(code == null) { return code; }
        
        String r = "";
        for (int i = 0; i < code.length(); i++) {
            if (code.charAt(i) != '-') {
                r += code.charAt(i);
            }
        }
        return r;
    }

    /**
     * Place les tirets dans un code barre (ISBN)
     * @param code Le code, sans les tirets
     * @return Le même code, avec les tirets
     */
    static String addTiret(String code) {
        if (code.length() == 13) {
            return code.substring(0, 3) + "-" + code.charAt(3) + "-" + code.substring(4, 9) + "-"
                    + code.substring(9, 12) + "-" + code.charAt(12);
        }
        if (code.length() == 10) {
            return code.charAt(0) + "-" + code.substring(1, 6) + "-" + code.substring(6, 9) + "-"
                    + code.charAt(9);
        }
        return code;
    }

    
    /**
     * Calcule la clef associée au code barre EAN/ISBN10
     * @param code Le code
     * @return la clef, comprise entre 0 et 10, ou -1 si une erreur s'est produite
     */
    static int computeCodeEAN10(String code) {
        int cle = 0;
        if (code.length() != 10) {
            return -1;
        }
        for (int i = 0; i < 9; i++) {
            cle += (i + 1) * (((int) code.charAt(i)) - (int) '0');
        }
        cle = cle % 11;
        return cle;
    }

    /**
     * Vérifie si un code barre est bien en EAN/ISBN10
     * @param code Le code
     * @return true si le code est sous la form EAN10, false sinon.
     */
    public static boolean isCodeEAN10(String code) {
        int cle = computeCodeEAN10(code);
        boolean bo = false;

        switch (cle) {
            case -1: // Cas de l'erreur
                break;
            case 10: // Cas du X
                bo = (code.charAt(9) == 'X');
                break;
            default: // Cas entre 0 et 9
                if (code.charAt(9) == (char) (((int) '0') + cle)) {
                    bo = true;
                }
        }
        return bo;
    }

    /**
     * Calcule la clef d'un code en EAN/ISBN13
     * @param code Le code
     * @return La clef, comprise entre 0 et 10, ou -1 si une erreur s'est produite
     */
    public static int computeCodeEAN13(String code) {
        int clep = 0;
        int clei = 0;
        if (code.length() != 13) {
            return -1;
        }
        for (int i = 0; i < 12; i++) {
            if ((i % 2) == 0) {
                clep += (((int) code.charAt(i)) - (int) '0');
            } else {
                clei += (((int) code.charAt(i)) - (int) '0');
            }
        }
        clep += clei * 3;
        clep = (10 - clep % 10) % 10;
        return clep;
    }

    /**
     * Vérifie si un code est bien en EAN/ISBN13
     * @param code Le code
     * @return true si le code est en EAN/ISBN13, false sinon.
     */
    public static boolean isCodeEAN13(String code) {
        int cle = computeCodeEAN13(code);
        boolean bo = false;

        switch (cle) {
            case -1: // Cas de l'erreur
                break;
            default: // cas normal entre 0 et 9
                if (code.charAt(12) == (char) (((int) '0') + cle)) {
                    bo = true;
                }
        }
        return bo;
    }

    /**
     * Convertit un code barre en EAN/ISBN10
     * @param code Le code
     * @return Le même code en EAN/ISBN10 ou null si le code est invalide
     */
    static String convertEAN2ISBN10(String code) {
        if (code.length() == 10) {
            return code;
        }
        if (code.length() == 13) {
            String s = code.substring(3, 12);
            int cle = computeCodeEAN10(code.substring(3, 13));
            if (cle == 10) {
                s += "X";
            } else {
                s += (char) (((int) '0') + cle);
            }
            return s;
        }
        return null;
    }

    /**
     * Convertit un code barre en EAN/ISBN13
     * @param code Le code barre
     * @return Le même code, en EAN/ISBN13, ou null si le code est invalide
     */
    static String convertEAN2ISBN13(String code) {
        if (code.length() == 13) {
            return code;
        }
        if (code.length() == 10) {
            String s = "978" + code.substring(0, 9);
            s += (char) (((int) '0') + computeCodeEAN13("978" + code));
            return s;
        }
        return null;
    }

    /**
     * Convertit un code barre en EAN/ISBN13
     * @param code Le code barre
     * @return Le même code barre en ISBN13 ou null si le code initial est invalide
     */
    static String convertISBN2ISBN13(String code) {
        return convertEAN2ISBN13(code);
    }

    /**
     * Convertit un code barre en EAN/ISBN10 sans tirets
     * @param code Le code barre
     * @return Le même code barre en ISBN10 sans tirets, ou null si le code initial est invalide
     */
    static String toBDovoreISBN(String code) {
        return convertEAN2ISBN10(removeTiret(code));
    }

    /**
     * Convertit un code barre en EAN/ISBN13 sans tirets
     * @param code Le code barre
     * @return Le même code barre en ISBN13 sans tirets, ou null si le code initial est invalide.
     */
    public static String toBDovoreEAN(String code) {
        return convertEAN2ISBN13(removeTiret(code));
    }
}
