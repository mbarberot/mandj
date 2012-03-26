/**
 * DetailsEdition.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package wsdl.server;

public class DetailsEdition  implements java.io.Serializable {
    private int idEdition;

    private int idTome;

    private int flag_pret;

    private int flag_dedicace;

    private int flag_aAcheter;

    private java.lang.String date_ajout;

    private java.lang.String img_couv;

    private java.lang.String isbn;

    private java.lang.String date_parution;

    private int idEditeur;

    private int flag_default;

    public DetailsEdition() {
    }

    public DetailsEdition(
           int idEdition,
           int idTome,
           int flag_pret,
           int flag_dedicace,
           int flag_aAcheter,
           java.lang.String date_ajout,
           java.lang.String img_couv,
           java.lang.String isbn,
           java.lang.String date_parution,
           int idEditeur,
           int flag_default) {
           this.idEdition = idEdition;
           this.idTome = idTome;
           this.flag_pret = flag_pret;
           this.flag_dedicace = flag_dedicace;
           this.flag_aAcheter = flag_aAcheter;
           this.date_ajout = date_ajout;
           this.img_couv = img_couv;
           this.isbn = isbn;
           this.date_parution = date_parution;
           this.idEditeur = idEditeur;
           this.flag_default = flag_default;
    }


    /**
     * Gets the idEdition value for this DetailsEdition.
     * 
     * @return idEdition
     */
    public int getIdEdition() {
        return idEdition;
    }


    /**
     * Sets the idEdition value for this DetailsEdition.
     * 
     * @param idEdition
     */
    public void setIdEdition(int idEdition) {
        this.idEdition = idEdition;
    }


    /**
     * Gets the idTome value for this DetailsEdition.
     * 
     * @return idTome
     */
    public int getIdTome() {
        return idTome;
    }


    /**
     * Sets the idTome value for this DetailsEdition.
     * 
     * @param idTome
     */
    public void setIdTome(int idTome) {
        this.idTome = idTome;
    }


    /**
     * Gets the flag_pret value for this DetailsEdition.
     * 
     * @return flag_pret
     */
    public int getFlag_pret() {
        return flag_pret;
    }


    /**
     * Sets the flag_pret value for this DetailsEdition.
     * 
     * @param flag_pret
     */
    public void setFlag_pret(int flag_pret) {
        this.flag_pret = flag_pret;
    }


    /**
     * Gets the flag_dedicace value for this DetailsEdition.
     * 
     * @return flag_dedicace
     */
    public int getFlag_dedicace() {
        return flag_dedicace;
    }


    /**
     * Sets the flag_dedicace value for this DetailsEdition.
     * 
     * @param flag_dedicace
     */
    public void setFlag_dedicace(int flag_dedicace) {
        this.flag_dedicace = flag_dedicace;
    }


    /**
     * Gets the flag_aAcheter value for this DetailsEdition.
     * 
     * @return flag_aAcheter
     */
    public int getFlag_aAcheter() {
        return flag_aAcheter;
    }


    /**
     * Sets the flag_aAcheter value for this DetailsEdition.
     * 
     * @param flag_aAcheter
     */
    public void setFlag_aAcheter(int flag_aAcheter) {
        this.flag_aAcheter = flag_aAcheter;
    }


    /**
     * Gets the date_ajout value for this DetailsEdition.
     * 
     * @return date_ajout
     */
    public java.lang.String getDate_ajout() {
        return date_ajout;
    }


    /**
     * Sets the date_ajout value for this DetailsEdition.
     * 
     * @param date_ajout
     */
    public void setDate_ajout(java.lang.String date_ajout) {
        this.date_ajout = date_ajout;
    }


    /**
     * Gets the img_couv value for this DetailsEdition.
     * 
     * @return img_couv
     */
    public java.lang.String getImg_couv() {
        return img_couv;
    }


    /**
     * Sets the img_couv value for this DetailsEdition.
     * 
     * @param img_couv
     */
    public void setImg_couv(java.lang.String img_couv) {
        this.img_couv = img_couv;
    }


    /**
     * Gets the isbn value for this DetailsEdition.
     * 
     * @return isbn
     */
    public java.lang.String getIsbn() {
        return isbn;
    }


    /**
     * Sets the isbn value for this DetailsEdition.
     * 
     * @param isbn
     */
    public void setIsbn(java.lang.String isbn) {
        this.isbn = isbn;
    }


    /**
     * Gets the date_parution value for this DetailsEdition.
     * 
     * @return date_parution
     */
    public java.lang.String getDate_parution() {
        return date_parution;
    }


    /**
     * Sets the date_parution value for this DetailsEdition.
     * 
     * @param date_parution
     */
    public void setDate_parution(java.lang.String date_parution) {
        this.date_parution = date_parution;
    }


    /**
     * Gets the idEditeur value for this DetailsEdition.
     * 
     * @return idEditeur
     */
    public int getIdEditeur() {
        return idEditeur;
    }


    /**
     * Sets the idEditeur value for this DetailsEdition.
     * 
     * @param idEditeur
     */
    public void setIdEditeur(int idEditeur) {
        this.idEditeur = idEditeur;
    }


    /**
     * Gets the flag_default value for this DetailsEdition.
     * 
     * @return flag_default
     */
    public int getFlag_default() {
        return flag_default;
    }


    /**
     * Sets the flag_default value for this DetailsEdition.
     * 
     * @param flag_default
     */
    public void setFlag_default(int flag_default) {
        this.flag_default = flag_default;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DetailsEdition)) return false;
        DetailsEdition other = (DetailsEdition) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.idEdition == other.getIdEdition() &&
            this.idTome == other.getIdTome() &&
            this.flag_pret == other.getFlag_pret() &&
            this.flag_dedicace == other.getFlag_dedicace() &&
            this.flag_aAcheter == other.getFlag_aAcheter() &&
            ((this.date_ajout==null && other.getDate_ajout()==null) || 
             (this.date_ajout!=null &&
              this.date_ajout.equals(other.getDate_ajout()))) &&
            ((this.img_couv==null && other.getImg_couv()==null) || 
             (this.img_couv!=null &&
              this.img_couv.equals(other.getImg_couv()))) &&
            ((this.isbn==null && other.getIsbn()==null) || 
             (this.isbn!=null &&
              this.isbn.equals(other.getIsbn()))) &&
            ((this.date_parution==null && other.getDate_parution()==null) || 
             (this.date_parution!=null &&
              this.date_parution.equals(other.getDate_parution()))) &&
            this.idEditeur == other.getIdEditeur() &&
            this.flag_default == other.getFlag_default();
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
        _hashCode += getIdEdition();
        _hashCode += getIdTome();
        _hashCode += getFlag_pret();
        _hashCode += getFlag_dedicace();
        _hashCode += getFlag_aAcheter();
        if (getDate_ajout() != null) {
            _hashCode += getDate_ajout().hashCode();
        }
        if (getImg_couv() != null) {
            _hashCode += getImg_couv().hashCode();
        }
        if (getIsbn() != null) {
            _hashCode += getIsbn().hashCode();
        }
        if (getDate_parution() != null) {
            _hashCode += getDate_parution().hashCode();
        }
        _hashCode += getIdEditeur();
        _hashCode += getFlag_default();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DetailsEdition.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("server.wsdl", "detailsEdition"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idEdition");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idEdition"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idTome");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idTome"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("flag_pret");
        elemField.setXmlName(new javax.xml.namespace.QName("", "flag_pret"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("flag_dedicace");
        elemField.setXmlName(new javax.xml.namespace.QName("", "flag_dedicace"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("flag_aAcheter");
        elemField.setXmlName(new javax.xml.namespace.QName("", "flag_aAcheter"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("date_ajout");
        elemField.setXmlName(new javax.xml.namespace.QName("", "date_ajout"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("img_couv");
        elemField.setXmlName(new javax.xml.namespace.QName("", "img_couv"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("isbn");
        elemField.setXmlName(new javax.xml.namespace.QName("", "isbn"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("date_parution");
        elemField.setXmlName(new javax.xml.namespace.QName("", "date_parution"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idEditeur");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idEditeur"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("flag_default");
        elemField.setXmlName(new javax.xml.namespace.QName("", "flag_default"));
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
