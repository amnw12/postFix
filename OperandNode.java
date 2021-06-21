/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package expressionTree;

/**
 *
 * @author amnwaqar
 */
public class OperandNode extends ExpNode{

    public OperandNode(String value) {
        
        super(value);
    }
    
    @Override
    public double evaluate() throws ArithmeticException {
        try {
            double operand = Double.parseDouble(symbol);
            return operand;
        }
        catch (NumberFormatException e)
        {
            throw new ArithmeticException();
        }
    }
    
}

