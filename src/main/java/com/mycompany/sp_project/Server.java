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

public class Server {
    public static final String PASSENGERS_FILE = "Passengers.txt";
    public static final String DAMASCUS_TRIPS_FILE = "DamascusTrips.txt";
    public static final String DRIVERS_FILE = "Drivers.txt";
    public static final String TRIP_FILE = "Trip.txt";

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(8888);
            System.out.println("Server started. Waiting for clients...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket);

                // Start a new thread for each client
                Mserver clientHandler = new Mserver(clientSocket);
                clientHandler.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

