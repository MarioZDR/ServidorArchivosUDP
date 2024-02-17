
package org.itson.clientearchivos;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ClienteArchivos {

    private static final int TIMEOUT = 3000;
    private static final int MAXTRIES = 5;
    
    public static void main(String[] args) throws IOException{
        
        InetAddress serverAddress = InetAddress.getByName("localhost");
        
        String msg = "Hola";
        byte[] bytesToSend = msg.getBytes();
        
        int servPort = 7;
        
        DatagramSocket socket = new DatagramSocket();
        
        socket.setSoTimeout(TIMEOUT);
        
        DatagramPacket sendPacket = new DatagramPacket(bytesToSend, bytesToSend.length, serverAddress, servPort);
        
        DatagramPacket receivePacket = new DatagramPacket(new byte[bytesToSend.length], bytesToSend.length);
        
        int tries = 0;
        
        boolean receivedResponse = false;
        
        do{
            socket.send(sendPacket);
            try{
                socket.receive(receivePacket);
                
                if(!receivePacket.getAddress().equals(serverAddress)){
                    throw new IOException("Received packet from an unkown source");
                }
                receivedResponse = true;
            }catch(InterruptedIOException e){
                tries+=1;
                System.out.println("Timed out, "+(MAXTRIES - tries) +" more tries...");
            }
        }while((!receivedResponse)&&(tries<MAXTRIES));
        
        if(receivedResponse){
            System.out.println("Received: "+new String(receivePacket.getData()));
        }else{
            System.out.println("No response -- giving up");
        }
        socket.close();
    }
}
