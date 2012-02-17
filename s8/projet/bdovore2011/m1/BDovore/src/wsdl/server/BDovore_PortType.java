/**
 * BDovore_PortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package wsdl.server;

public interface BDovore_PortType extends java.rmi.Remote {
    public java.lang.String getDetails() throws java.rmi.RemoteException;
    public java.lang.String getBibliotheque() throws java.rmi.RemoteException;
    public java.lang.String setBibliotheque(java.lang.String bibliotheque) throws java.rmi.RemoteException;
    public java.lang.String getDate() throws java.rmi.RemoteException;
    public java.lang.String getAlbums(java.lang.String date) throws java.rmi.RemoteException;
    public java.lang.String setSubmission(java.lang.String album) throws java.rmi.RemoteException;
    public java.lang.String getValidationState(java.lang.String id) throws java.rmi.RemoteException;
    public java.lang.String getScenariste(java.lang.String id) throws java.rmi.RemoteException;
    public java.lang.String getDessinateur(java.lang.String id) throws java.rmi.RemoteException;
    public java.lang.String getColoriste(java.lang.String id) throws java.rmi.RemoteException;
    public java.lang.String getSerie(java.lang.String id) throws java.rmi.RemoteException;
    public java.lang.String getGenre(java.lang.String id) throws java.rmi.RemoteException;
    public java.lang.String getEditeur(java.lang.String id) throws java.rmi.RemoteException;
    public java.lang.String getCollection(java.lang.String id) throws java.rmi.RemoteException;
}
