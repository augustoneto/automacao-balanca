package br.com.andev.automacao.balanca;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.io.InputStream;
import java.io.OutputStream;


public class SerialComm {
	
	private InputStream inputStream;
	private OutputStream outputStream;
	
	private SerialPort serialPort;
	
	public void connect(String portName) throws Exception {
        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
        if (portIdentifier.isCurrentlyOwned()){
            System.out.println("Error: Port is currently in use");
        }
        else {
            CommPort commPort = portIdentifier.open(this.getClass().getName(),2000);
            
            if (commPort instanceof SerialPort) {
                serialPort = (SerialPort) commPort;
                serialPort.setSerialPortParams(57600,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
                
                this.inputStream = serialPort.getInputStream();
                this.outputStream = serialPort.getOutputStream();
               
            }
            else {
                System.out.println("Error: Only serial ports are handled by this example.");
            }
        }     
    }
	
	public void disconnect() {
		serialPort.close();
	}
	
	public InputStream getInputStream() {
		return inputStream;
	}
	
	public OutputStream getOutputStream() {
		return outputStream;
	}

}
