/**
 * BDovore_PortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package wsdl.server;

public interface BDovore_PortType extends java.rmi.Remote {
    public java.lang.String getBibliotheque(java.lang.String userName, java.lang.String userPass) throws java.rmi.RemoteException;
    public wsdl.server.DetailsEdition getDetailsEditionUser(int idEdition, java.lang.String userName, java.lang.String userPass) throws java.rmi.RemoteException;
    public wsdl.server.DetailsVolume getDetailsTome(int idTome) throws java.rmi.RemoteException;
    public java.lang.String getScenaristesTome(int idTome) throws java.rmi.RemoteException;
    public java.lang.String getDessinateursTome(int idTome) throws java.rmi.RemoteException;
    public wsdl.server.DetailsAuteur getDetailsAuteur(int idAuteur) throws java.rmi.RemoteException;
    public wsdl.server.DetailsSerie getDetailsSerie(int idSerie) throws java.rmi.RemoteException;
    public wsdl.server.DetailsEditeur getDetailsEditeur(int idEditeur) throws java.rmi.RemoteException;
    public java.lang.String getGenre(int idGenre) throws java.rmi.RemoteException;
    public java.lang.String getColoristesTome(int idTome) throws java.rmi.RemoteException;
    public boolean addUserBibliotheque(java.lang.String userName, java.lang.String userPass, int idEdition) throws java.rmi.RemoteException;
    public boolean setUserBibliotheque(java.lang.String userName, java.lang.String userPass, int idEdition, int flagPret, int flagDedicace, int flagAacheter) throws java.rmi.RemoteException;
    public boolean delUserBibliotheque(java.lang.String userName, java.lang.String userPass, int idEdition) throws java.rmi.RemoteException;
    public boolean doProposal(int typeAjout) throws java.rmi.RemoteException;
    public java.lang.String getEditionsManquantes(long lastId) throws java.rmi.RemoteException;
    public long getNbEditionsManquantes(long lastId) throws java.rmi.RemoteException;
    public int getIdUser(java.lang.String userName, java.lang.String userPass) throws java.rmi.RemoteException;
    public wsdl.server.DetailsEdition getDetailsEdition(int idEdition) throws java.rmi.RemoteException;
}
