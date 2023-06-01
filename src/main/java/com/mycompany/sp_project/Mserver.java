/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sp_project;

import static com.mycompany.sp_project.Server.*;
import java.io.*;
import java.net.*;

/**
 *
 * @author ASUS
 */

public class Mserver extends Thread {
        private Socket clientSocket;
        private BufferedReader reader;
        private PrintWriter writer;
        private boolean authenticated;

        public Mserver(Socket socket) {
            this.clientSocket = socket;
            this.authenticated = false;
        }

        public void run() {
            try {
                reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                writer = new PrintWriter(clientSocket.getOutputStream(), true);

                // Perform authentication
                if (!authenticate()) {
                    writer.println("Authentication failed. Disconnecting...");
                    disconnect();
                    return;
                }
                
                writer.println("Authentication successful. You can now access other commands.");
                
                // Book a trip to damascus
                
                String message;
                while ((message = reader.readLine()) != null) {
                    System.out.println("Received message from client: " + message);
                    processMessage(message);
                }

                System.out.println("Client disconnected: " + clientSocket);
               
                disconnect();
                
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        
         private boolean authenticate() {
            try (BufferedReader fileReader = new BufferedReader(new FileReader(PASSENGERS_FILE))) {
                
                //clean the BufferedReader
                reader.readLine();
                
                writer.println("Please provide your name:");
                String name = reader.readLine();
                writer.println("Please provide your password:");
                String password = reader.readLine();

                String line;

                while ((line = fileReader.readLine()) != null) {
                    String[] parts = line.split(",");
                    
                    if (parts.length >= 3 && parts[0].equals(name.trim()) && parts[1].equals(password.trim())) {
                        this.authenticated = true;
                        return true;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            
            return false;
        }


        private void processMessage(String message) {
            
            if (!authenticated) {
                writer.println("Access denied. You need to authenticate first.");
                return;
            }

            String[] tokens = message.split(" ");
            if (tokens.length < 2) {
                writer.println("Invalid command.");
                return;
            }
            
            // "ADD_PASSENGER Ahmed,234532,23
            // command = 'ADD_PASSENGER'
            // data = 'Ahmed,234532,23'
            String command = tokens[0];
            String data = message.substring(command.length()).trim();

            switch (command) {
                case "ADD_PASSENGER":
                    addPassenger(PASSENGERS_FILE, data);
                    writer.println("Passenger added successfully.");
                    break;
                case "BOOK_DAMASCUS":
                    String damascusTrips = bookDamascusTrip(DAMASCUS_TRIPS_FILE);
                    writer.println(damascusTrips);
                    writer.println("Trip to Damascus booked successfully.");
                    break;
//                case "ADD_DRIVER":
//                    appendToFile(DRIVERS_FILE, data);
//                    writer.println("Driver added successfully.");
//                    break;
//                case "SET_TRIP":
//                    writeToFile(TRIP_FILE, data);
//                    writer.println("Trip set successfully.");
//                    break;
//                case "GET_PASSENGERS":
//                    String passengers = readFile(PASSENGERS_FILE);
//                    writer.println(passengers);
//                    break;
//                case "GET_DRIVERS":
//                    String drivers = readFile(DRIVERS_FILE);
//                    writer.println(drivers);
//                    break;
//                case "GET_TRIP":
//                    String trip = readFile(TRIP_FILE);
//                    writer.println(trip);
//                    break;
                default:
                    writer.println("Invalid command.");
                    break;
            }
        }

        private String readFile(String filename) {
            StringBuilder content = new StringBuilder();
            try (BufferedReader fileReader = new BufferedReader(new FileReader(filename))) {
                String line;
                while ((line = fileReader.readLine()) != null) {
                    content.append(line).append(System.lineSeparator());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return content.toString();
        }

        private void writeToFile(String filename, String data) {
            try (PrintWriter fileWriter = new PrintWriter(new FileWriter(filename))) {
                fileWriter.println(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void addPassenger(String filename, String data) {
            try (PrintWriter fileWriter = new PrintWriter(new FileWriter(filename, true))) {
                fileWriter.println(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        private String bookDamascusTrip(String filename) {
            StringBuilder content = new StringBuilder();
            try (BufferedReader fileReader = new BufferedReader(new FileReader(filename))) {
                String line;
                while ((line = fileReader.readLine()) != null) {
                    content.append(line).append(System.lineSeparator());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return content.toString();
        }
        
        private void disconnect() {
            try {
                reader.close();
                writer.close();
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
