/**
 * DetailsSerie.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package wsdl.server;

public class DetailsSerie  implements java.io.Serializable {
    private int idSerie;

    private java.lang.String nomSerie;

    private int nbTomes;

    private int flgFini;

    private java.lang.String histoire;

    public DetailsSerie() {
    }

    public DetailsSerie(
           int idSerie,
           java.lang.String nomSerie,
           int nbTomes,
           int flgFini,
           java.lang.String histoire) {
           this.idSerie = idSerie;
           this.nomSerie = nomSerie;
           this.nbTomes = nbTomes;
           this.flgFini = flgFini;
           this.histoire = histoire;
    }


    /**
     * Gets the idSerie value for this DetailsSerie.
     * 
     * @return idSerie
     */
    public int getIdSerie() {
        return idSerie;
    }


    /**
     * Sets the idSerie value for this DetailsSerie.
     * 
     * @param idSerie
     */
    public void setIdSerie(int idSerie) {
        this.idSerie = idSerie;
    }


    /**
     * Gets the nomSerie value for this DetailsSerie.
     * 
     * @return nomSerie
     */
    public java.lang.String getNomSerie() {
        return nomSerie;
    }


    /**
     * Sets the nomSerie value for this DetailsSerie.
     * 
     * @param nomSerie
     */
    public void setNomSerie(java.lang.String nomSerie) {
        this.nomSerie = nomSerie;
    }


    /**
     * Gets the nbTomes value for this DetailsSerie.
     * 
     * @return nbTomes
     */
    public int getNbTomes() {
        return nbTomes;
    }


    /**
     * Sets the nbTomes value for this DetailsSerie.
     * 
     * @param nbTomes
     */
    public void setNbTomes(int nbTomes) {
        this.nbTomes = nbTomes;
    }


    /**
     * Gets the flgFini value for this DetailsSerie.
     * 
     * @return flgFini
     */
    public int getFlgFini() {
        return flgFini;
    }


    /**
     * Sets the flgFini value for this DetailsSerie.
     * 
     * @param flgFini
     */
    public void setFlgFini(int flgFini) {
        this.flgFini = flgFini;
    }


    /**
     * Gets the histoire value for this DetailsSerie.
     * 
     * @return histoire
     */
    public java.lang.String getHistoire() {
        return histoire;
    }


    /**
     * Sets the histoire value for this DetailsSerie.
     * 
     * @param histoire
     */
    public void setHistoire(java.lang.String histoire) {
        this.histoire = histoire;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DetailsSerie)) return false;
        DetailsSerie other = (DetailsSerie) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.idSerie == other.getIdSerie() &&
            ((this.nomSerie==null && other.getNomSerie()==null) || 
             (this.nomSerie!=null &&
              this.nomSerie.equals(other.getNomSerie()))) &&
            this.nbTomes == other.getNbTomes() &&
            this.flgFini == other.getFlgFini() &&
            ((this.histoire==null && other.getHistoire()==null) || 
             (this.histoire!=null &&
              this.histoire.equals(other.getHistoire())));
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
        _hashCode += getIdSerie();
        if (getNomSerie() != null) {
            _hashCode += getNomSerie().hashCode();
        }
        _hashCode += getNbTomes();
        _hashCode += getFlgFini();
        if (getHistoire() != null) {
            _hashCode += getHistoire().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DetailsSerie.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("server.wsdl", "detailsSerie"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idSerie");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idSerie"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nomSerie");
        elemField.setXmlName(new javax.xml.namespace.QName("", "nomSerie"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nbTomes");
        elemField.setXmlName(new javax.xml.namespace.QName("", "nbTomes"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("flgFini");
        elemField.setXmlName(new javax.xml.namespace.QName("", "flgFini"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("histoire");
        elemField.setXmlName(new javax.xml.namespace.QName("", "histoire"));
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
