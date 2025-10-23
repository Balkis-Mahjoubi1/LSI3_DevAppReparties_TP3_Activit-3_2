package servermultiThread;

import java.io.*;
import java.net.*;
import java.util.concurrent.atomic.AtomicInteger;
import common.Operation;
import common.Result;

public class ClientProcess implements Runnable {
    private Socket clientSocket;
    private AtomicInteger operationCounter;
    
    public ClientProcess(Socket socket, AtomicInteger counter) {
        this.clientSocket = socket;
        this.operationCounter = counter;
    }
    
    @Override
    public void run() {
        try (
            ObjectInputStream input = new ObjectInputStream(clientSocket.getInputStream());
            ObjectOutputStream output = new ObjectOutputStream(clientSocket.getOutputStream());
        ) {
            System.out.println("ClientProcess démarré pour: " + clientSocket.getInetAddress());
            
            while (true) {
                // Lire l'opération envoyée par le client
                Operation operation = (Operation) input.readObject();
                System.out.println("Opération reçue: " + operation);
                
                // Traiter l'opération
                Result result = processOperation(operation);
                
                // Envoyer le résultat au client
                output.writeObject(result);
                output.flush();
                System.out.println("Résultat envoyé: " + result);
            }
        } catch (EOFException e) {
            System.out.println("Client déconnecté: " + clientSocket.getInetAddress());
        } catch (IOException | ClassNotFoundException e) {
            System.err.println(" Erreur avec client " + clientSocket.getInetAddress() + ": " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    private Result processOperation(Operation operation) {
        // Incrémenter le compteur global (opération atomique)
        int operationNumber = operationCounter.incrementAndGet();
        
        double result;
        try {
            switch (operation.getOperator()) {
                case '+':
                    result = operation.getOperand1() + operation.getOperand2();
                    break;
                case '-':
                    result = operation.getOperand1() - operation.getOperand2();
                    break;
                case '*':
                    result = operation.getOperand1() * operation.getOperand2();
                    break;
                case '/':
                    if (operation.getOperand2() == 0) {
                        return new Result("Division par zéro impossible", operationNumber);
                    }
                    result = operation.getOperand1() / operation.getOperand2();
                    break;
                default:
                    return new Result("Opérateur non supporté: " + operation.getOperator(), operationNumber);
            }
            
            // Afficher le compteur global mis à jour
            System.out.println(" Compteur global: " + operationCounter.get() + " (Opération n°" + operationNumber + ")");
            
            return new Result(result, operationNumber);
        } catch (Exception e) {
            return new Result("Erreur de calcul: " + e.getMessage(), operationNumber);
        }
    }
}
