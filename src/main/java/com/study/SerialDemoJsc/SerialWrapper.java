package com.study.SerialDemoJsc;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;

public class SerialWrapper {
    
    private static final int BUFFER_SIZE = 2000;
    private byte[] buffer = new byte[BUFFER_SIZE];
    
    public SerialWrapper() {
        Arrays.fill(this.buffer, (byte)0);
    }

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

        try {
            is = serialPort.getInputStream();
            int buffLength = is.available();
            
            while (buffLength > 0) {
                Arrays.fill(this.buffer, (byte)0);
                is.read(this.buffer);
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
        
        return this.buffer;
    }
}
