/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sp_project;

/**
 *
 * @author ASUS
 */
import java.io.*;
import java.net.*;

public class Client {
    
    private static final String MESSAGE_TERMINATOR = "#";
    
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 8888);

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            BufferedReader serverReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            String message;
            while ((message = reader.readLine()) != null) {
                writer.println(message);
                if (message.equals("Bye."))
                    break;
                String response = serverReader.readLine();
                System.out.println("Received response from server: " + response);
                System.out.print("input: ");
            }

           
            reader.close();
            serverReader.close();
            writer.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private static String readFullMessage(BufferedReader reader) throws IOException {
       StringBuilder message = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.equals(MESSAGE_TERMINATOR)) {
                break;
            }
            message.append(line).append(System.lineSeparator());
        }
        return message.toString();
    }
}
