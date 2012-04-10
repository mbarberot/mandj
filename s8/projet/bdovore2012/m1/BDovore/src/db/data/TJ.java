package db.data;

/**
 * Modélise un triplet de clés primaires de la table TJ_TOME_AUTEUR
 * @author Barberot Mathieu et Racenet Joan
 */
public class TJ implements Comparable<TJ>
{
    public static int SCEN = 0;
    public static int DESS = 1;
    public static int COLO = 2;
    
    private int idTome;
    private int idAuteur;
    private int role;
    
    public TJ (int idTome, int idAuteur, String role)
    {
        this.idTome = idTome ;
        this.idAuteur = idAuteur ;
        
        if(role.equalsIgnoreCase("scenariste")) { this.role = SCEN; }
        else if(role.equalsIgnoreCase("dessinateur")) { this.role = DESS; }
        else { this.role = COLO; }
    }

    public int compareTo(TJ o)
    {
        int res ;
        TJ otj = (TJ)o;
        
        res = this.idTome - otj.getIdTome() ;
        if(res == 0) { res += this.getIdAuteur() - otj.getIdAuteur(); }
        if(res == 0) { res += this.role - otj.getRole() ; }
        
        return res;
    }

    public int getIdAuteur()
    {
        return idAuteur;
    }

    public void setIdAuteur(int idAuteur)
    {
        this.idAuteur = idAuteur;
    }

    public int getIdTome()
    {
        return idTome;
    }

    public void setIdTome(int idTome)
    {
        this.idTome = idTome;
    }

    public int getRole()
    {
        return role;
    }

    public void setRole(int role)
    {
        this.role = role;
    }
    
    public String getStringRole ()
    {
        return (role == SCEN) ? "Scenariste" : (role == DESS) ? "Dessinateur" : "Coloriste" ;
    }
    
    @Override
    public String toString() 
    {
        return idTome + "|" + idAuteur + "|" + getStringRole() ;
    }
}
