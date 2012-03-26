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
  
  public java.lang.String getBibliotheque(java.lang.String userName, java.lang.String userPass) throws java.rmi.RemoteException{
    if (bDovore_PortType == null)
      _initBDovore_PortTypeProxy();
    return bDovore_PortType.getBibliotheque(userName, userPass);
  }
  
  public wsdl.server.DetailsEdition getDetailsEditionUser(int idEdition, java.lang.String userName, java.lang.String userPass) throws java.rmi.RemoteException{
    if (bDovore_PortType == null)
      _initBDovore_PortTypeProxy();
    return bDovore_PortType.getDetailsEditionUser(idEdition, userName, userPass);
  }
  
  public wsdl.server.DetailsVolume getDetailsTome(int idTome) throws java.rmi.RemoteException{
    if (bDovore_PortType == null)
      _initBDovore_PortTypeProxy();
    return bDovore_PortType.getDetailsTome(idTome);
  }
  
  public java.lang.String getScenaristesTome(int idTome) throws java.rmi.RemoteException{
    if (bDovore_PortType == null)
      _initBDovore_PortTypeProxy();
    return bDovore_PortType.getScenaristesTome(idTome);
  }
  
  public java.lang.String getDessinateursTome(java.lang.String idTome) throws java.rmi.RemoteException{
    if (bDovore_PortType == null)
      _initBDovore_PortTypeProxy();
    return bDovore_PortType.getDessinateursTome(idTome);
  }
  
  public wsdl.server.DetailsAuteur getDetailsAuteur(int idAuteur) throws java.rmi.RemoteException{
    if (bDovore_PortType == null)
      _initBDovore_PortTypeProxy();
    return bDovore_PortType.getDetailsAuteur(idAuteur);
  }
  
  public wsdl.server.DetailsSerie getDetailsSerie(int idSerie) throws java.rmi.RemoteException{
    if (bDovore_PortType == null)
      _initBDovore_PortTypeProxy();
    return bDovore_PortType.getDetailsSerie(idSerie);
  }
  
  public wsdl.server.DetailsEditeur getDetailsEditeur(int idEditeur) throws java.rmi.RemoteException{
    if (bDovore_PortType == null)
      _initBDovore_PortTypeProxy();
    return bDovore_PortType.getDetailsEditeur(idEditeur);
  }
  
  public java.lang.String getGenre(int idGenre) throws java.rmi.RemoteException{
    if (bDovore_PortType == null)
      _initBDovore_PortTypeProxy();
    return bDovore_PortType.getGenre(idGenre);
  }
  
  public java.lang.String getColoristesTome(int idTome) throws java.rmi.RemoteException{
    if (bDovore_PortType == null)
      _initBDovore_PortTypeProxy();
    return bDovore_PortType.getColoristesTome(idTome);
  }
  
  public void addUserBibliotheque(java.lang.String userName, java.lang.String userPass, int idEdition) throws java.rmi.RemoteException{
    if (bDovore_PortType == null)
      _initBDovore_PortTypeProxy();
    bDovore_PortType.addUserBibliotheque(userName, userPass, idEdition);
  }
  
  public boolean doProposal(int typeAjout) throws java.rmi.RemoteException{
    if (bDovore_PortType == null)
      _initBDovore_PortTypeProxy();
    return bDovore_PortType.doProposal(typeAjout);
  }
  
  public java.lang.String getEditionsManquantes(int lastId) throws java.rmi.RemoteException{
    if (bDovore_PortType == null)
      _initBDovore_PortTypeProxy();
    return bDovore_PortType.getEditionsManquantes(lastId);
  }
  
  public int getIdUser(java.lang.String userName, java.lang.String userPass) throws java.rmi.RemoteException{
    if (bDovore_PortType == null)
      _initBDovore_PortTypeProxy();
    return bDovore_PortType.getIdUser(userName, userPass);
  }
  
  public wsdl.server.DetailsEdition getDetailsEdition(int idEdition) throws java.rmi.RemoteException{
    if (bDovore_PortType == null)
      _initBDovore_PortTypeProxy();
    return bDovore_PortType.getDetailsEdition(idEdition);
  }
  
  
}