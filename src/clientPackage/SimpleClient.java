package clientPackage;

import java.io.*;
import java.net.*;
import java.util.Scanner;
import common.Operation;
import common.Result;

public class SimpleClient {
    public static void main(String[] args) {
        String serverAddress = "localhost";
        if (args.length > 0) {
            serverAddress = args[0];
        }
        
        try (
            Socket socket = new Socket(serverAddress, 1234);
            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
            Scanner scanner = new Scanner(System.in);
        ) {
            System.out.println(" Connecté au serveur de calculatrice: " + serverAddress);
            System.out.println(" Entrez vos opérations (format: nombre opérateur nombre)");
            System.out.println(" Opérateurs supportés: +, -, *, /");
            System.out.println(" Tapez 'quit' pour quitter");
            
            while (true) {
                System.out.print("\n  Votre opération: ");
                String inputLine = scanner.nextLine().trim();
                
                if (inputLine.equalsIgnoreCase("quit")) {
                    break;
                }
                
                try {
                    Operation operation = parseOperation(inputLine);
                    if (operation != null) {
                        // Envoyer l'opération au serveur
                        output.writeObject(operation);
                        output.flush();
                        
                        // Recevoir le résultat
                        Result result = (Result) input.readObject();
                        System.out.println( result);
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println(" Format invalide: " + e.getMessage());
                }
            }
        } catch (UnknownHostException e) {
            System.err.println("Hôte inconnu: " + serverAddress);
        } catch (IOException e) {
            System.err.println(" Erreur de connexion: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println(" Erreur de format de données");
        }
    }
    
    private static Operation parseOperation(String input) {
        String[] parts = input.split(" ");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Format: nombre opérateur nombre");
        }
        
        try {
            double op1 = Double.parseDouble(parts[0]);
            char operator = parts[1].charAt(0);
            double op2 = Double.parseDouble(parts[2]);
            
            return new Operation(op1, op2, operator);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Nombres invalides");
        }
    }
}
