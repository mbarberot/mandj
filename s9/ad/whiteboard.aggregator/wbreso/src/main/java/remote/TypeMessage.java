package remote;

/**
 * Les différents types de message.
 * @author Mathieu Barberot et Joan Racenet
 */
public enum TypeMessage
{
    ENVOI_NOUVELLE_FORME,
    CONNEXION_NOUVEAU_PROC,
    DECONNEXION_PROC,
    DEMANDE_SC,
    AUTORISER_ACCES_SC,
    REFUSER_ACCES_SC,
    DEMANDE_ETAT_WB,
    ELECTION
}
