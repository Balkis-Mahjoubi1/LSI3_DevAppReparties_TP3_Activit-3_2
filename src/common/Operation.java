package common;

import java.io.Serializable;

public class Operation implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private double operand1;
    private double operand2;
    private char operator;
    
    public Operation(double op1, double op2, char op) {
        this.operand1 = op1;
        this.operand2 = op2;
        this.operator = op;
    }
    
    // Getters et Setters
    public double getOperand1() { return operand1; }
    public double getOperand2() { return operand2; }
    public char getOperator() { return operator; }
    
    public void setOperand1(double op1) { this.operand1 = op1; }
    public void setOperand2(double op2) { this.operand2 = op2; }
    public void setOperator(char op) { this.operator = op; }
    
    @Override
    public String toString() {
        return operand1 + " " + operator + " " + operand2;
    }
}
