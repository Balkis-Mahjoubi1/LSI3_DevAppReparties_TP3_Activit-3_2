package common;

import java.io.Serializable;

public class Result implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private double value;
    private String errorMessage;
    private boolean success;
    private int operationNumber;
    
    public Result(double value, int opNumber) {
        this.value = value;
        this.success = true;
        this.operationNumber = opNumber;
    }
    
    public Result(String error, int opNumber) {
        this.errorMessage = error;
        this.success = false;
        this.operationNumber = opNumber;
    }
    
    // Getters
    public double getValue() { return value; }
    public String getErrorMessage() { return errorMessage; }
    public boolean isSuccess() { return success; }
    public int getOperationNumber() { return operationNumber; }
    
    @Override
    public String toString() {
        if (success) {
            return "Succès - Résultat: " + value + " (Opération n°" + operationNumber + ")";
        } else {
            return "Erreur: " + errorMessage + " (Opération n°" + operationNumber + ")";
        }
    }
}
