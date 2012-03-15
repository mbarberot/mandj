package wsdl.server;

import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

public class ClientTest {

	public static void main(String[] args){
		BDovore service = new BDovoreLocator();
		
		try {	
			BDovore_PortType port = service.getBDovore_Port();
			
			try {
				// Récupération des éditions d'un utilisateur
				String ed = port.getBibliotheque("latruffe", "bdovore");
				System.out.println(ed + "\n");
				
				// Détails d'une édition
				DetailsEdition dEd = port.getDetailsEdition(75267, "latruffe", "bdovore");
				System.out.println(dEd.getIdEdition() + " ajouté le : " + dEd.getDate_ajout() + " id Tome : " + dEd.getIdTome() +"\n");
				
				// On récupère son titre
				DetailsVolume tome = port.getDetailsTome(74303);
				System.out.println("Titre du volume : " + tome.getTitre() + "\n");

			} catch (RemoteException e1) {
				e1.printStackTrace();
			}
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}
}

