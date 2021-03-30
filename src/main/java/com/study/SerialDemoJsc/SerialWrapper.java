package com.study.SerialDemoJsc;

import java.io.IOException;
import java.io.InputStream;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;

public class SerialWrapper {

    public SerialPort openComPort(String portName, int baudRate, int dataBits, int stopBits, int parity) {
        SerialPort comPort = null;
        
        if (portName == null || "".equals(portName)) {
            SerialPort [] serialPorts = SerialPort.getCommPorts();
            if (serialPorts != null && serialPorts.length > 0) {
                portName = serialPorts[0].getSystemPortName();
            }
        }
        
        comPort = SerialPort.getCommPort(portName);
        comPort.openPort();
        comPort.setComPortParameters(baudRate, dataBits, stopBits, parity);
        comPort.setFlowControl(SerialPort.FLOW_CONTROL_DISABLED);
        return comPort;
    }
    
    public void closeComPort(SerialPort serialPort) {
        if (serialPort != null && serialPort.isOpen()) {
            serialPort.closePort();
        }
    }
    
    public void setSerialPortListener(SerialPort serialPort, SerialPortDataListener listener) {
        if (serialPort != null && serialPort.isOpen()) {
            serialPort.addDataListener(listener);
        }
    }
    
    public final byte[] readData(SerialPort serialPort) {
        InputStream is = null;
        
        byte[] bytes = null;
        try {
            is = serialPort.getInputStream();
            int buffLength = is.available();
            
            while (buffLength > 0) {
                bytes = new byte[buffLength];
                is.read(bytes);
                buffLength = is.available();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        
        return bytes;
    }
}
