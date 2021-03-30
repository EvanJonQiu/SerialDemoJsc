package com.study.SerialDemoJsc;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

/**
 * Hello world!
 *
 */
public class App implements Runnable {
    
    DataInputStream in;
    private boolean checkLoop = false;
    
    final SerialWrapper serialWrapper = new SerialWrapper();
    final SerialPort serialPort = serialWrapper.openComPort("COM1", 115200, 8, 1, 0);
    
    public void run() {
        BufferedReader into = new BufferedReader(new InputStreamReader(System.in));
        String read;
        System.out.println("Welcome...");
        
        serialWrapper.setSerialPortListener(serialPort, new SerialPortDataListener() {

            public int getListeningEvents() {
                return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
            }

            public void serialEvent(SerialPortEvent event) {
                if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE)
                    return;
                
                byte[] bytes = serialWrapper.readData(serialPort);
                System.out.println("收到的数据长度："+bytes.length);
                System.out.println("收到的数据："+ new String(bytes));
            }
            
        });
        
        while(!checkLoop) {
            try {
                System.out.println("running1");
                read = into.readLine();
                System.out.println(read);
                if(read.equals(".bye"))
                {
                    checkLoop = true;
                }
                Thread.sleep(500);
            } catch (IOException e) {
                System.out.println(e);System.out.println("running2");
            } catch (InterruptedException ie) {
                System.out.println(ie);System.out.println("running3");
            }
        }
        System.out.println("running4");
        serialWrapper.closeComPort(serialPort);
    }
    
    public static void main( String[] args ) {
        App main = new App();
        Thread t1 = new Thread(main);
        t1.start();
        
        System.out.println("end");
    }
}
