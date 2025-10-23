package servermultiThread;

import java.io.*;
import java.net.*;
import java.util.concurrent.atomic.AtomicInteger;

public class MultiThreadServer {
    // Compteur global synchronisé pour les opérations
    private static AtomicInteger globalOperationCounter = new AtomicInteger(0);
    
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(1234);
            System.out.println("Serveur de calculatrice multi-threads démarré sur le port 1234...");
            System.out.println("Compteur global initial: " + globalOperationCounter.get());
            
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Nouveau client connecté: " + clientSocket.getInetAddress());
                
                // Créer un nouveau ClientProcess pour chaque client
                ClientProcess clientProcess = new ClientProcess(clientSocket, globalOperationCounter);
                Thread clientThread = new Thread(clientProcess);
                clientThread.start();
            }
        } catch (IOException e) {
            System.err.println("Erreur du serveur: " + e.getMessage());
        }
    }
}
