package db.data;

import gui.FrameMain;
import java.net.Proxy;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import wsdl.server.BDovoreLocator;
import wsdl.server.BDovore_PortType;

/**
 * Informations sur l'utilisateur
 *
 * @author Thorisoka
 */
public class User {

    // User's ID
    private int id;
    // User's username
    private String username;
    // User's password
    // Attention : il n'est pas en MD5
    private String password;

    /**
     * Constructeur
     *
     * @param username Username
     * @param password Password
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.id = getIdFromWebService(null);
    }

    /**
     * Constructeur
     *
     * @param username Username
     * @param password Password
     * @param proxy Proxy
     */
    public User(String username, String password, Proxy proxy) {
        this.username = username;
        this.password = password;
        this.id = getIdFromWebService(proxy);
    }

    /*
     * Getters
     */
    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean isValid() {
        return (id >= 0);
    }

    /**
     * Utilise le WebService pour trouver l'ID d'un utilisateur
     *
     * @param proxy Proxy
     * @return L'id de l'utilisateur ou -1 si l'utilisateur n'existe pas
     */
    private int getIdFromWebService(Proxy proxy)
    {
        int id = -1;
        
        
        try
        {
            BDovore_PortType webservice =  new BDovoreLocator().getBDovore_Port();
            id = webservice.getIdUser(this.username, this.password);
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return id;
    }
}
