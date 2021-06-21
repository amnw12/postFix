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
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel; 
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;

/**
 * Almost Complete GUI - just need to finish the code when pressing the buttons and updating
 * the number of nodes in the tree.. WIll only build once ExpNode subclasses are made
 * @author sehall
 */
public class ExpressionTreeGUI extends JPanel implements ActionListener {

    private final JButton evaluateButton, buildTreeButton;

    private DrawPanel drawPanel;
    private ExpNode root;
    private int numberNodes = 0;
    private JTextField postFixField;
    public static int PANEL_H = 500;
    public static int PANEL_W = 900;
    private JLabel nodeCounterLabel;
    private final int BOX_SIZE = 40;

    public ExpressionTreeGUI() {
        super(new BorderLayout());
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {}
        root = null;
        super.setPreferredSize(new Dimension(PANEL_W, PANEL_H + 30));
        JPanel buttonPanel = new JPanel();
        buttonPanel.setPreferredSize(new Dimension(PANEL_W, 30));
        drawPanel = new DrawPanel();

        evaluateButton = new JButton("Evaluate to infix");
        buildTreeButton = new JButton("Build Expression Tree");

        evaluateButton.addActionListener((ActionListener) this);
        buildTreeButton.addActionListener((ActionListener) this);

        postFixField = new JTextField(40);

        buttonPanel.add(postFixField);
        buttonPanel.add(buildTreeButton);
        buttonPanel.add(evaluateButton);

        super.add(drawPanel, BorderLayout.CENTER);
        super.add(buttonPanel, BorderLayout.SOUTH);

        nodeCounterLabel = new JLabel("Number of Nodes: " + numberNodes);
        super.add(nodeCounterLabel, BorderLayout.NORTH);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        Object source = event.getSource();
        double result;
        ExpressionTreeBuilder builder = new ExpressionTreeBuilder();

        if (source == evaluateButton) {   
            if (root == null) {
                JOptionPane.showMessageDialog(this, "Tree is null, not built", "INFO", JOptionPane.ERROR_MESSAGE);
            } else {
                
                result = root.evaluate();
                
                JOptionPane.showMessageDialog(this, builder.toInfixString(root) + " = " + result, "RESULT", JOptionPane.OK_OPTION);
            }

        } else if (source == buildTreeButton && postFixField.getText() != null) { 
           
            String postfix = postFixField.getText();
            String[] postfixStrings = postfix.split(" ");
            
            try
            {
                root = builder.buildExpressionTree(postfixStrings);
                numberNodes = builder.countNodes(root);
            }
            catch (NumberFormatException e)
            {
                JOptionPane.showMessageDialog(this, "Unrecognisable symbol", "ERROR",
                        JOptionPane.ERROR_MESSAGE);
            }
            catch (ArithmeticException e)
            {
                JOptionPane.showMessageDialog(this, "Not in postfix format", "ERROR",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
        
        nodeCounterLabel.setText("Number of Nodes: " + numberNodes);
        
        drawPanel.repaint();
    }

    private class DrawPanel extends JPanel {

        public DrawPanel() {
            super();
            super.setBackground(Color.WHITE);
            super.setPreferredSize(new Dimension(PANEL_W, PANEL_H));
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            if (root != null) {
                drawTree(g, getWidth());
            }
        }

        public void drawTree(Graphics g, int width) {
            drawNode(g, root, BOX_SIZE, 0, 0, new HashMap<>());
        }

        private int drawNode(Graphics g, ExpNode current,
                int x, int level, int nodeCount, Map<ExpNode, Point> map) {
            
            
            if (current.leftChild != null) {
                nodeCount = drawNode(g, current.leftChild, x, level + 1, nodeCount, map);
            }

            int currentX = x + nodeCount * BOX_SIZE;
            int currentY = level * 2 * BOX_SIZE + BOX_SIZE;
            nodeCount++;
            map.put(current, new Point(currentX, currentY));

            if (current.rightChild != null) {
                nodeCount = drawNode(g, current.rightChild, x, level + 1, nodeCount, map);
            }

            g.setColor(Color.red);
            if (current.leftChild != null) {
                Point leftPoint = map.get(current.leftChild);
                g.drawLine(currentX, currentY, leftPoint.x, leftPoint.y - BOX_SIZE / 2);
            }
            if (current.rightChild != null) {
                Point rightPoint = map.get(current.rightChild);
                g.drawLine(currentX, currentY, rightPoint.x, rightPoint.y - BOX_SIZE / 2);

            }
            if (current instanceof OperandNode) {
                g.setColor(Color.WHITE);
            } else {
                g.setColor(Color.YELLOW);
            }

            Point currentPoint = map.get(current);
            g.fillRect(currentPoint.x - BOX_SIZE / 2, currentPoint.y - BOX_SIZE / 2, BOX_SIZE, BOX_SIZE);
            g.setColor(Color.BLACK);
            g.drawRect(currentPoint.x - BOX_SIZE / 2, currentPoint.y - BOX_SIZE / 2, BOX_SIZE, BOX_SIZE);
            Font f = new Font("courier new", Font.BOLD, 16);
            g.setFont(f);
            if (current instanceof OperandNode) 
                g.drawString(current.toString(), currentPoint.x-current.toString().length()*4, currentPoint.y);
            else
                g.drawString(current.toString(), currentPoint.x-3, currentPoint.y);
            return nodeCount;

        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Expression Tree GUI builder");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new ExpressionTreeGUI());
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimension = toolkit.getScreenSize();
        int screenHeight = dimension.height;
        int screenWidth = dimension.width;
        frame.pack();             //resize frame apropriately for its content
        //positions frame in center of screen
        frame.setLocation(new Point((screenWidth / 2) - (frame.getWidth() / 2),
                (screenHeight / 2) - (frame.getHeight() / 2)));
        frame.setVisible(true);
    }
}