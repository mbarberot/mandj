/**
 * DetailsVolume.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package wsdl.server;

public class DetailsVolume  implements java.io.Serializable {
    private int idTome;

    private java.lang.String titre;

    private int idSerie;

    private int numTome;

    public DetailsVolume() {
    }

    public DetailsVolume(
           int idTome,
           java.lang.String titre,
           int idSerie,
           int numTome) {
           this.idTome = idTome;
           this.titre = titre;
           this.idSerie = idSerie;
           this.numTome = numTome;
    }


    /**
     * Gets the idTome value for this DetailsVolume.
     * 
     * @return idTome
     */
    public int getIdTome() {
        return idTome;
    }


    /**
     * Sets the idTome value for this DetailsVolume.
     * 
     * @param idTome
     */
    public void setIdTome(int idTome) {
        this.idTome = idTome;
    }


    /**
     * Gets the titre value for this DetailsVolume.
     * 
     * @return titre
     */
    public java.lang.String getTitre() {
        return titre;
    }


    /**
     * Sets the titre value for this DetailsVolume.
     * 
     * @param titre
     */
    public void setTitre(java.lang.String titre) {
        this.titre = titre;
    }


    /**
     * Gets the idSerie value for this DetailsVolume.
     * 
     * @return idSerie
     */
    public int getIdSerie() {
        return idSerie;
    }


    /**
     * Sets the idSerie value for this DetailsVolume.
     * 
     * @param idSerie
     */
    public void setIdSerie(int idSerie) {
        this.idSerie = idSerie;
    }


    /**
     * Gets the numTome value for this DetailsVolume.
     * 
     * @return numTome
     */
    public int getNumTome() {
        return numTome;
    }


    /**
     * Sets the numTome value for this DetailsVolume.
     * 
     * @param numTome
     */
    public void setNumTome(int numTome) {
        this.numTome = numTome;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DetailsVolume)) return false;
        DetailsVolume other = (DetailsVolume) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.idTome == other.getIdTome() &&
            ((this.titre==null && other.getTitre()==null) || 
             (this.titre!=null &&
              this.titre.equals(other.getTitre()))) &&
            this.idSerie == other.getIdSerie() &&
            this.numTome == other.getNumTome();
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
        _hashCode += getIdTome();
        if (getTitre() != null) {
            _hashCode += getTitre().hashCode();
        }
        _hashCode += getIdSerie();
        _hashCode += getNumTome();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DetailsVolume.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("server.wsdl", "detailsVolume"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idTome");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idTome"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("titre");
        elemField.setXmlName(new javax.xml.namespace.QName("", "titre"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idSerie");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idSerie"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numTome");
        elemField.setXmlName(new javax.xml.namespace.QName("", "numTome"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
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
