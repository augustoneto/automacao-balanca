package br.com.andev.automacao.balanca;

import java.util.ArrayList;
import java.util.List;

import gnu.io.CommPortIdentifier;


public class ReadPort {

	public static void main(String[] args) {
		new ReadPort().listPorts();

	}
	
	public List<String> listPorts() {
        java.util.Enumeration<CommPortIdentifier> portEnum = CommPortIdentifier.getPortIdentifiers();
        
        List<String> ports = new ArrayList<String>();
        
        String port = null;
        
        while (portEnum.hasMoreElements()) {
            CommPortIdentifier portIdentifier = portEnum.nextElement();
            
            port = new String(portIdentifier.getName()  +  " - " +  getPortTypeName(portIdentifier.getPortType()));
            
            ports.add(port);
            
            System.out.println(port);
        }        
        
        return ports;
    }
    
    public String getPortTypeName(int portType) {
        switch(portType) {
            case CommPortIdentifier.PORT_I2C:
                return "I2C";
            case CommPortIdentifier.PORT_PARALLEL:
                return "Parallel";
            case CommPortIdentifier.PORT_RAW:
                return "Raw";
            case CommPortIdentifier.PORT_RS485:
                return "RS485";
            case CommPortIdentifier.PORT_SERIAL:
                return "Serial";
            default:
                return "unknown type";
        }
    }

}
