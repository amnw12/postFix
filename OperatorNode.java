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
public class OperatorNode extends ExpNode{

    public OperatorNode(String value) {
        super(value);
    }

    @Override
    public double evaluate() throws ArithmeticException {
        switch (symbol)
        {
            case "+":
                return (leftChild.evaluate() + rightChild.evaluate());
            case "-":
                return (leftChild.evaluate() - rightChild.evaluate());
            case "*":
                return (leftChild.evaluate() * rightChild.evaluate());
            case "/":
                return  (leftChild.evaluate() / rightChild.evaluate());
            case "~":
                return  (Math.pow(rightChild.evaluate(), -1));
            default:
                throw new ArithmeticException();
        }
    }
    
}
