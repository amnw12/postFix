/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package expressionTree;

import java.util.EmptyStackException;
import java.util.Stack;

/**
 *
 * @author amnwaqar
 */
public class ExpressionTreeBuilder {
    
    public static ExpNode buildExpressionTree(String[] postfixStrings)
    {
        Stack<ExpNode> stack = new Stack<>();
        ExpNode parent = null;
        String symbol;
        
        for (int k = 0; k < postfixStrings.length; k++)
        {
            symbol = postfixStrings[k];
            
            if (symbol.equals("+")||symbol.equals("-")||symbol.equals("*")||symbol.equals("/")||symbol.equals("~"))
            {
                parent = new OperatorNode(symbol);
                
                try 
                {
                    parent.rightChild = stack.pop();
                
                    if (!symbol.equals("~"))
                    {
                        parent.leftChild = stack.pop();
                    }
                
                    stack.push(parent);
                }
                catch (EmptyStackException e)
                {
                    throw new ArithmeticException();
                }
            }
            else
            {
                try 
                {
                    double operand = Double.parseDouble(symbol);
                    stack.push(new OperandNode(symbol));
                }
                catch (NumberFormatException e)
                {
                    throw new NumberFormatException();
                }
            }
        }
        return parent;
    }
    
    public static int countNodes(ExpNode node)
    {
        int count = 0;
        
        if (node.leftChild == null && node.rightChild == null)
        {
            count += 1;
        }
        else
        {
            count += 1;
            
            if (node.leftChild != null)
            {
                count += countNodes(node.leftChild);
            }
            
            if (node.rightChild != null)
            {
                count += countNodes(node.rightChild);
            }
        }
        
        return count;
    }
    
    public static String toInfixString(ExpNode node)
    {
        String infix = "";
        
        if (node.rightChild == null && node.leftChild == null)
        {
            infix += node.symbol;
        }
        else
        {
            infix += "(";
            
            if (node.leftChild != null)
            {
                infix += toInfixString(node.leftChild);
            }
            infix += " " + node + " ";
            
            if (node.rightChild != null)
            {
                infix += toInfixString(node.rightChild);
            }
            infix += ")";
        }
        
        return infix;
    }
}
