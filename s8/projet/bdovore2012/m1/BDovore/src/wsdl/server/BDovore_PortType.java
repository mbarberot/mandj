/**
 * BDovore_PortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package wsdl.server;

public interface BDovore_PortType extends java.rmi.Remote {
    public java.lang.String getBibliotheque(java.lang.String userName, java.lang.String userPass) throws java.rmi.RemoteException;
    public wsdl.server.DetailsEdition getDetailsEdition(int idEdition, java.lang.String userName, java.lang.String userPass) throws java.rmi.RemoteException;
    public wsdl.server.DetailsVolume getDetailsTome(int idTome) throws java.rmi.RemoteException;
    public java.lang.String getScenaristesTome(int idTome) throws java.rmi.RemoteException;
    public java.lang.String getDessinateursTome(java.lang.String idTome) throws java.rmi.RemoteException;
    public wsdl.server.DetailsAuteur getDetailsAuteur(int idAuteur) throws java.rmi.RemoteException;
    public wsdl.server.DetailsSerie getDetailsSerie(int idSerie) throws java.rmi.RemoteException;
    public wsdl.server.DetailsEditeur getDetailsEditeur(int idEditeur) throws java.rmi.RemoteException;
    public java.lang.String getGenre(int idGenre) throws java.rmi.RemoteException;
    public java.lang.String getColoristesTome(int idTome) throws java.rmi.RemoteException;
    public void addUserBibliotheque(java.lang.String userName, java.lang.String userPass, int idEdition) throws java.rmi.RemoteException;
    public boolean doProposal(int typeAjout) throws java.rmi.RemoteException;
    public java.lang.String getEditionsManquantes(int lastId) throws java.rmi.RemoteException;
}
