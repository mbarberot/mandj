/**
 * BDovoreLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package wsdl.server;

public class BDovoreLocator extends org.apache.axis.client.Service implements wsdl.server.BDovore {

/**
 * Webservice BDovore
 */

    public BDovoreLocator() {
    }


    public BDovoreLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public BDovoreLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for BDovore_Port
    private java.lang.String BDovore_Port_address = "http://localhost/bdovore/webservice/server.php";

    public java.lang.String getBDovore_PortAddress() {
        return BDovore_Port_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String BDovore_PortWSDDServiceName = "BDovore_Port";

    public java.lang.String getBDovore_PortWSDDServiceName() {
        return BDovore_PortWSDDServiceName;
    }

    public void setBDovore_PortWSDDServiceName(java.lang.String name) {
        BDovore_PortWSDDServiceName = name;
    }

    public wsdl.server.BDovore_PortType getBDovore_Port() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(BDovore_Port_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getBDovore_Port(endpoint);
    }

    public wsdl.server.BDovore_PortType getBDovore_Port(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            wsdl.server.BDovore_BindingStub _stub = new wsdl.server.BDovore_BindingStub(portAddress, this);
            _stub.setPortName(getBDovore_PortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setBDovore_PortEndpointAddress(java.lang.String address) {
        BDovore_Port_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (wsdl.server.BDovore_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                wsdl.server.BDovore_BindingStub _stub = new wsdl.server.BDovore_BindingStub(new java.net.URL(BDovore_Port_address), this);
                _stub.setPortName(getBDovore_PortWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("BDovore_Port".equals(inputPortName)) {
            return getBDovore_Port();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("server.wsdl", "BDovore");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("server.wsdl", "BDovore_Port"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("BDovore_Port".equals(portName)) {
            setBDovore_PortEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
