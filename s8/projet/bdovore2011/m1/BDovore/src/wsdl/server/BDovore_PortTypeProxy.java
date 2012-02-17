package wsdl.server;

public class BDovore_PortTypeProxy implements wsdl.server.BDovore_PortType {
  private String _endpoint = null;
  private wsdl.server.BDovore_PortType bDovore_PortType = null;
  
  public BDovore_PortTypeProxy() {
    _initBDovore_PortTypeProxy();
  }
  
  public BDovore_PortTypeProxy(String endpoint) {
    _endpoint = endpoint;
    _initBDovore_PortTypeProxy();
  }
  
  private void _initBDovore_PortTypeProxy() {
    try {
      bDovore_PortType = (new wsdl.server.BDovoreLocator()).getBDovore_Port();
      if (bDovore_PortType != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)bDovore_PortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)bDovore_PortType)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (bDovore_PortType != null)
      ((javax.xml.rpc.Stub)bDovore_PortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public wsdl.server.BDovore_PortType getBDovore_PortType() {
    if (bDovore_PortType == null)
      _initBDovore_PortTypeProxy();
    return bDovore_PortType;
  }
  
  public java.lang.String getDetails() throws java.rmi.RemoteException{
    if (bDovore_PortType == null)
      _initBDovore_PortTypeProxy();
    return bDovore_PortType.getDetails();
  }
  
  public java.lang.String getBibliotheque() throws java.rmi.RemoteException{
    if (bDovore_PortType == null)
      _initBDovore_PortTypeProxy();
    return bDovore_PortType.getBibliotheque();
  }
  
  public java.lang.String setBibliotheque(java.lang.String bibliotheque) throws java.rmi.RemoteException{
    if (bDovore_PortType == null)
      _initBDovore_PortTypeProxy();
    return bDovore_PortType.setBibliotheque(bibliotheque);
  }
  
  public java.lang.String getDate() throws java.rmi.RemoteException{
    if (bDovore_PortType == null)
      _initBDovore_PortTypeProxy();
    return bDovore_PortType.getDate();
  }
  
  public java.lang.String getAlbums(java.lang.String date) throws java.rmi.RemoteException{
    if (bDovore_PortType == null)
      _initBDovore_PortTypeProxy();
    return bDovore_PortType.getAlbums(date);
  }
  
  public java.lang.String setSubmission(java.lang.String album) throws java.rmi.RemoteException{
    if (bDovore_PortType == null)
      _initBDovore_PortTypeProxy();
    return bDovore_PortType.setSubmission(album);
  }
  
  public java.lang.String getValidationState(java.lang.String id) throws java.rmi.RemoteException{
    if (bDovore_PortType == null)
      _initBDovore_PortTypeProxy();
    return bDovore_PortType.getValidationState(id);
  }
  
  public java.lang.String getScenariste(java.lang.String id) throws java.rmi.RemoteException{
    if (bDovore_PortType == null)
      _initBDovore_PortTypeProxy();
    return bDovore_PortType.getScenariste(id);
  }
  
  public java.lang.String getDessinateur(java.lang.String id) throws java.rmi.RemoteException{
    if (bDovore_PortType == null)
      _initBDovore_PortTypeProxy();
    return bDovore_PortType.getDessinateur(id);
  }
  
  public java.lang.String getColoriste(java.lang.String id) throws java.rmi.RemoteException{
    if (bDovore_PortType == null)
      _initBDovore_PortTypeProxy();
    return bDovore_PortType.getColoriste(id);
  }
  
  public java.lang.String getSerie(java.lang.String id) throws java.rmi.RemoteException{
    if (bDovore_PortType == null)
      _initBDovore_PortTypeProxy();
    return bDovore_PortType.getSerie(id);
  }
  
  public java.lang.String getGenre(java.lang.String id) throws java.rmi.RemoteException{
    if (bDovore_PortType == null)
      _initBDovore_PortTypeProxy();
    return bDovore_PortType.getGenre(id);
  }
  
  public java.lang.String getEditeur(java.lang.String id) throws java.rmi.RemoteException{
    if (bDovore_PortType == null)
      _initBDovore_PortTypeProxy();
    return bDovore_PortType.getEditeur(id);
  }
  
  public java.lang.String getCollection(java.lang.String id) throws java.rmi.RemoteException{
    if (bDovore_PortType == null)
      _initBDovore_PortTypeProxy();
    return bDovore_PortType.getCollection(id);
  }
  
  
}