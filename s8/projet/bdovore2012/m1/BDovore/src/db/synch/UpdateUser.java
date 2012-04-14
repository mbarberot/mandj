package db.synch;

import db.DataBase;
import db.data.User;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import wsdl.server.BDovore_PortType;

/**
 * 
 * @author kawa
 */
public class UpdateUser extends Thread
{
    private LinkedList<Integer> allIdLocal ;
    private LinkedList<Integer> allIdServer ;
    
    private DataBase db;
    private User user;
    private Update update;
    private BDovore_PortType webservice;
    
    private boolean canceled ;
    
    
    public UpdateUser (DataBase db, User user, Update update, BDovore_PortType webservice )
    {
        this.allIdLocal = null;
        this.allIdServer = null;
        
        this.db = db;
        this.user = user;
        this.update = update;
        this.webservice = webservice;
        
        this.canceled = false;
        
    }
    
    
    @Override
    public void run()
    {
        Integer id;
        
        try
        {
            if(canceled) { return ; }
            
            allIdLocal = db.getBDtheque();
            allIdServer = convert(webservice.getBibliotheque(user.getUsername(), user.getPassword()));
            
            while(allIdLocal.size() > 0 && !canceled)
            {
                id = allIdLocal.poll();
                
                if(allIdServer.contains(id))
                {
                    transaction(id,true,true);
                    allIdServer.remove(id);
                }
                else
                {
                    transaction(id,true,false);
                }
            }
            
            while(allIdServer.size() > 0 && !canceled)
            {
                id = allIdServer.poll();
                
                transaction(id,false,true);
            }
            
            


        } catch (SQLException ex)
        {
            Logger.getLogger(UpdateUser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException ex)
        {
            Logger.getLogger(UpdateUser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void transaction(Integer id, boolean inLocal, boolean inServer)
    {
        
    }
    
    /**
     * Transforme une chaine de caractère représentant une liste d'entier au format CSV
     * en un arbre trié avec ces entiers.
     * Utilité : récupération d'un liste d'id_édition via le webservice
     * 
     * @param fromWS Liste d'entier au format CSV
     * @return Arbre trié contenant les entiers de la chaine
     */
    private LinkedList<Integer> convert(String fromWS)
    {
        LinkedList<Integer> liste = new LinkedList<Integer>();
        String tabstr[] = fromWS.split(";");
        int len = tabstr.length;
        
        for(int i = 0; i < len; i++)
        {
            liste.offer(
                    new Integer(tabstr[i])
                    );
        }
        
        return liste;
    }

    public void cancel()
    {
        this.canceled = true;
    }
    
    
}
