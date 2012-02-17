package wsdl.server;

import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

public class TestClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		// Création du service depuis le endpoint
		// BDovore correspond au nom du service dans le fichier "wsdl"
		// c'est la balise : wsdl:service name="bdovore"

		BDovore service = new BDovoreLocator();
		
		try {
			// Utilisation du service pour obtenir un stub qui implemente le SDI
			// (Service Definition Type ; i.e. PortType).
			// Pour le typage, c'est la balise : wsdl:portType name="sommer"
			// Pour le getsommer(), le sommer correspond à la balise :
			// wsdl:port binding="impl:sommerSoapBinding" name="sommer"
			BDovore_PortType port = service.getBDovore_Port();
			
			String s;
			
			try {
				// Mise en oeuvre du service par application directe des méthodes
				s = port.getDessinateur("toto");
				System.out.println(s);
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}

}
