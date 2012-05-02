/**
 * DetailsEditeur.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package wsdl.server;

public class DetailsEditeur  implements java.io.Serializable {
    private int idEditeur;

    private java.lang.String nomEditeur;

    private java.lang.String url;

    public DetailsEditeur() {
    }

    public DetailsEditeur(
           int idEditeur,
           java.lang.String nomEditeur,
           java.lang.String url) {
           this.idEditeur = idEditeur;
           this.nomEditeur = nomEditeur;
           this.url = url;
    }


    /**
     * Gets the idEditeur value for this DetailsEditeur.
     * 
     * @return idEditeur
     */
    public int getIdEditeur() {
        return idEditeur;
    }


    /**
     * Sets the idEditeur value for this DetailsEditeur.
     * 
     * @param idEditeur
     */
    public void setIdEditeur(int idEditeur) {
        this.idEditeur = idEditeur;
    }


    /**
     * Gets the nomEditeur value for this DetailsEditeur.
     * 
     * @return nomEditeur
     */
    public java.lang.String getNomEditeur() {
        return nomEditeur;
    }


    /**
     * Sets the nomEditeur value for this DetailsEditeur.
     * 
     * @param nomEditeur
     */
    public void setNomEditeur(java.lang.String nomEditeur) {
        this.nomEditeur = nomEditeur;
    }


    /**
     * Gets the url value for this DetailsEditeur.
     * 
     * @return url
     */
    public java.lang.String getUrl() {
        return url;
    }


    /**
     * Sets the url value for this DetailsEditeur.
     * 
     * @param url
     */
    public void setUrl(java.lang.String url) {
        this.url = url;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DetailsEditeur)) return false;
        DetailsEditeur other = (DetailsEditeur) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.idEditeur == other.getIdEditeur() &&
            ((this.nomEditeur==null && other.getNomEditeur()==null) || 
             (this.nomEditeur!=null &&
              this.nomEditeur.equals(other.getNomEditeur()))) &&
            ((this.url==null && other.getUrl()==null) || 
             (this.url!=null &&
              this.url.equals(other.getUrl())));
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
        _hashCode += getIdEditeur();
        if (getNomEditeur() != null) {
            _hashCode += getNomEditeur().hashCode();
        }
        if (getUrl() != null) {
            _hashCode += getUrl().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DetailsEditeur.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("server.wsdl", "detailsEditeur"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idEditeur");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idEditeur"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nomEditeur");
        elemField.setXmlName(new javax.xml.namespace.QName("", "nomEditeur"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("url");
        elemField.setXmlName(new javax.xml.namespace.QName("", "url"));
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
