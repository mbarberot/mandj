/**
 * DetailsAuteur.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package wsdl.server;

public class DetailsAuteur  implements java.io.Serializable {
    private int idAuteur;

    private java.lang.String pseudo;

    private java.lang.String nom;

    private java.lang.String prenom;

    private java.lang.String date_naissance;

    private java.lang.String date_deces;

    private java.lang.String nationalite;

    public DetailsAuteur() {
    }

    public DetailsAuteur(
           int idAuteur,
           java.lang.String pseudo,
           java.lang.String nom,
           java.lang.String prenom,
           java.lang.String date_naissance,
           java.lang.String date_deces,
           java.lang.String nationalite) {
           this.idAuteur = idAuteur;
           this.pseudo = pseudo;
           this.nom = nom;
           this.prenom = prenom;
           this.date_naissance = date_naissance;
           this.date_deces = date_deces;
           this.nationalite = nationalite;
    }


    /**
     * Gets the idAuteur value for this DetailsAuteur.
     * 
     * @return idAuteur
     */
    public int getIdAuteur() {
        return idAuteur;
    }


    /**
     * Sets the idAuteur value for this DetailsAuteur.
     * 
     * @param idAuteur
     */
    public void setIdAuteur(int idAuteur) {
        this.idAuteur = idAuteur;
    }


    /**
     * Gets the pseudo value for this DetailsAuteur.
     * 
     * @return pseudo
     */
    public java.lang.String getPseudo() {
        return pseudo;
    }


    /**
     * Sets the pseudo value for this DetailsAuteur.
     * 
     * @param pseudo
     */
    public void setPseudo(java.lang.String pseudo) {
        this.pseudo = pseudo;
    }


    /**
     * Gets the nom value for this DetailsAuteur.
     * 
     * @return nom
     */
    public java.lang.String getNom() {
        return nom;
    }


    /**
     * Sets the nom value for this DetailsAuteur.
     * 
     * @param nom
     */
    public void setNom(java.lang.String nom) {
        this.nom = nom;
    }


    /**
     * Gets the prenom value for this DetailsAuteur.
     * 
     * @return prenom
     */
    public java.lang.String getPrenom() {
        return prenom;
    }


    /**
     * Sets the prenom value for this DetailsAuteur.
     * 
     * @param prenom
     */
    public void setPrenom(java.lang.String prenom) {
        this.prenom = prenom;
    }


    /**
     * Gets the date_naissance value for this DetailsAuteur.
     * 
     * @return date_naissance
     */
    public java.lang.String getDate_naissance() {
        return date_naissance;
    }


    /**
     * Sets the date_naissance value for this DetailsAuteur.
     * 
     * @param date_naissance
     */
    public void setDate_naissance(java.lang.String date_naissance) {
        this.date_naissance = date_naissance;
    }


    /**
     * Gets the date_deces value for this DetailsAuteur.
     * 
     * @return date_deces
     */
    public java.lang.String getDate_deces() {
        return date_deces;
    }


    /**
     * Sets the date_deces value for this DetailsAuteur.
     * 
     * @param date_deces
     */
    public void setDate_deces(java.lang.String date_deces) {
        this.date_deces = date_deces;
    }


    /**
     * Gets the nationalite value for this DetailsAuteur.
     * 
     * @return nationalite
     */
    public java.lang.String getNationalite() {
        return nationalite;
    }


    /**
     * Sets the nationalite value for this DetailsAuteur.
     * 
     * @param nationalite
     */
    public void setNationalite(java.lang.String nationalite) {
        this.nationalite = nationalite;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DetailsAuteur)) return false;
        DetailsAuteur other = (DetailsAuteur) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.idAuteur == other.getIdAuteur() &&
            ((this.pseudo==null && other.getPseudo()==null) || 
             (this.pseudo!=null &&
              this.pseudo.equals(other.getPseudo()))) &&
            ((this.nom==null && other.getNom()==null) || 
             (this.nom!=null &&
              this.nom.equals(other.getNom()))) &&
            ((this.prenom==null && other.getPrenom()==null) || 
             (this.prenom!=null &&
              this.prenom.equals(other.getPrenom()))) &&
            ((this.date_naissance==null && other.getDate_naissance()==null) || 
             (this.date_naissance!=null &&
              this.date_naissance.equals(other.getDate_naissance()))) &&
            ((this.date_deces==null && other.getDate_deces()==null) || 
             (this.date_deces!=null &&
              this.date_deces.equals(other.getDate_deces()))) &&
            ((this.nationalite==null && other.getNationalite()==null) || 
             (this.nationalite!=null &&
              this.nationalite.equals(other.getNationalite())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        _hashCode += getIdAuteur();
        if (getPseudo() != null) {
            _hashCode += getPseudo().hashCode();
        }
        if (getNom() != null) {
            _hashCode += getNom().hashCode();
        }
        if (getPrenom() != null) {
            _hashCode += getPrenom().hashCode();
        }
        if (getDate_naissance() != null) {
            _hashCode += getDate_naissance().hashCode();
        }
        if (getDate_deces() != null) {
            _hashCode += getDate_deces().hashCode();
        }
        if (getNationalite() != null) {
            _hashCode += getNationalite().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DetailsAuteur.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("server.wsdl", "detailsAuteur"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idAuteur");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idAuteur"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("pseudo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "pseudo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nom");
        elemField.setXmlName(new javax.xml.namespace.QName("", "nom"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("prenom");
        elemField.setXmlName(new javax.xml.namespace.QName("", "prenom"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("date_naissance");
        elemField.setXmlName(new javax.xml.namespace.QName("", "date_naissance"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("date_deces");
        elemField.setXmlName(new javax.xml.namespace.QName("", "date_deces"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nationalite");
        elemField.setXmlName(new javax.xml.namespace.QName("", "nationalite"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
