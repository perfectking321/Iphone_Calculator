import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.EmptyBorder;

public class CalculatorApp extends JFrame implements ActionListener {
    JTextField display;
    String operator;
    double num1, num2, result;
    String expression = ""; // Store the complete expression
    boolean startNewNumber = false;
    
    // Modern color scheme
    private final Color DARK_BG = new Color(30, 30, 30);
    private final Color DISPLAY_BG = new Color(40, 40, 40);
    private final Color BUTTON_BG = new Color(60, 60, 60);
    private final Color OPERATOR_COLOR = new Color(255, 149, 0);
    private final Color TEXT_COLOR = Color.WHITE;

    public CalculatorApp() {
        setTitle("Calculator");
        setSize(350, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(0, 15));
        getContentPane().setBackground(DARK_BG);

        // Modern display with padding
        display = new JTextField("0");
        display.setEditable(false);
        display.setFont(new Font("Segoe UI", Font.PLAIN, 48));
        display.setBackground(DISPLAY_BG);
        display.setForeground(TEXT_COLOR);
        display.setBorder(new EmptyBorder(30, 20, 30, 20));
        display.setHorizontalAlignment(JTextField.RIGHT);
        add(display, BorderLayout.NORTH);

        // Add keyboard listener
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleKeyPress(e);
            }
        });
        setFocusable(true);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(15, 0));
        mainPanel.setBackground(DARK_BG);
        mainPanel.setBorder(new EmptyBorder(0, 15, 15, 15));

        // Left panel for operators
        JPanel operatorPanel = new JPanel();
        operatorPanel.setLayout(new GridLayout(5, 1, 10, 10));
        operatorPanel.setBackground(DARK_BG);

        String[] operators = {"/", "*", "-", "+", "C"};
        for (String text : operators) {
            JButton button = createRoundButton(text, OPERATOR_COLOR, TEXT_COLOR);
            operatorPanel.add(button);
        }

        // Right panel for numbers and equals
        JPanel numberPanel = new JPanel();
        numberPanel.setLayout(new GridLayout(4, 3, 10, 10));
        numberPanel.setBackground(DARK_BG);

        String[] numbers = {
            "7", "8", "9",
            "4", "5", "6",
            "1", "2", "3",
            "0", ".", "="
        };

        for (String text : numbers) {
            Color bgColor = text.equals("=") ? OPERATOR_COLOR : BUTTON_BG;
            JButton button = createRoundButton(text, bgColor, TEXT_COLOR);
            numberPanel.add(button);
        }

        mainPanel.add(numberPanel, BorderLayout.CENTER);
        mainPanel.add(operatorPanel, BorderLayout.EAST);
        add(mainPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    private void handleKeyPress(KeyEvent e) {
        char keyChar = e.getKeyChar();
        int keyCode = e.getKeyCode();
        
        // Handle number keys (0-9)
        if (Character.isDigit(keyChar)) {
            processInput(String.valueOf(keyChar));
        }
        // Handle operators
        else if (keyChar == '+' || keyChar == '-' || keyChar == '*' || keyChar == '/') {
            processInput(String.valueOf(keyChar));
        }
        // Handle decimal point
        else if (keyChar == '.') {
            processInput(".");
        }
        // Handle Enter/Return for equals
        else if (keyCode == KeyEvent.VK_ENTER) {
            processInput("=");
        }
        // Handle Escape or Delete for clear
        else if (keyCode == KeyEvent.VK_ESCAPE || keyCode == KeyEvent.VK_DELETE) {
            processInput("C");
        }
        // Handle Backspace to delete last character
        else if (keyCode == KeyEvent.VK_BACK_SPACE) {
            if (!expression.isEmpty()) {
                expression = expression.substring(0, expression.length() - 1);
                display.setText(expression.isEmpty() ? "0" : expression);
            }
        }
    }

    private void processInput(String input) {
        if (input.matches("[0-9]")) {
            if (startNewNumber || display.getText().equals("0")) {
                expression = expression + input;
                display.setText(expression);
                startNewNumber = false;
            } else {
                expression = expression + input;
                display.setText(expression);
            }
        } else if (input.equals(".")) {
            // Get the current number being entered
            String currentNum = getCurrentNumber();
            if (!currentNum.contains(".")) {
                expression = expression + ".";
                display.setText(expression);
            }
        } else if (input.matches("[+\\-*/]")) {
            if (!expression.isEmpty() && !isLastCharOperator()) {
                expression = expression + input;
                display.setText(expression);
                startNewNumber = true;
            }
        } else if (input.equals("=")) {
            if (!expression.isEmpty() && !isLastCharOperator()) {
                try {
                    result = evaluateExpression(expression);
                    // Format result to remove unnecessary decimals
                    if (result == (long) result) {
                        display.setText(String.format("%d", (long) result));
                        expression = String.format("%d", (long) result);
                    } else {
                        display.setText(String.format("%.2f", result));
                        expression = String.format("%.2f", result);
                    }
                } catch (Exception ex) {
                    display.setText("Error");
                    expression = "";
                }
            }
        } else if (input.equals("C")) {
            display.setText("0");
            expression = "";
            num1 = num2 = result = 0;
            operator = null;
            startNewNumber = false;
        }
    }

    private JButton createRoundButton(String text, Color bgColor, Color fgColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (getModel().isPressed()) {
                    g2.setColor(bgColor.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(bgColor.brighter());
                } else {
                    g2.setColor(bgColor);
                }
                
                g2.fillOval(0, 0, getWidth() - 1, getHeight() - 1);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        
        button.setFont(new Font("Segoe UI", Font.BOLD, 24));
        button.setForeground(fgColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.addActionListener(this);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        return button;
    }

    public void actionPerformed(ActionEvent e) {
        String input = e.getActionCommand();
        processInput(input);
    }
    
    private boolean isLastCharOperator() {
        if (expression.isEmpty()) return false;
        char lastChar = expression.charAt(expression.length() - 1);
        return lastChar == '+' || lastChar == '-' || lastChar == '*' || lastChar == '/';
    }
    
    private String getCurrentNumber() {
        StringBuilder num = new StringBuilder();
        for (int i = expression.length() - 1; i >= 0; i--) {
            char c = expression.charAt(i);
            if (c == '+' || c == '-' || c == '*' || c == '/') {
                break;
            }
            num.insert(0, c);
        }
        return num.toString();
    }
    
    private double evaluateExpression(String expr) {
        // Simple expression evaluator with operator precedence
        java.util.List<Double> numbers = new java.util.ArrayList<>();
        java.util.List<Character> operators = new java.util.ArrayList<>();
        
        StringBuilder numBuilder = new StringBuilder();
        for (int i = 0; i < expr.length(); i++) {
            char c = expr.charAt(i);
            if (Character.isDigit(c) || c == '.') {
                numBuilder.append(c);
            } else if (c == '+' || c == '-' || c == '*' || c == '/') {
                numbers.add(Double.parseDouble(numBuilder.toString()));
                numBuilder = new StringBuilder();
                operators.add(c);
            }
        }
        numbers.add(Double.parseDouble(numBuilder.toString()));
        
        // First pass: handle * and /
        for (int i = 0; i < operators.size(); i++) {
            if (operators.get(i) == '*' || operators.get(i) == '/') {
                double result;
                if (operators.get(i) == '*') {
                    result = numbers.get(i) * numbers.get(i + 1);
                } else {
                    result = numbers.get(i + 1) != 0 ? numbers.get(i) / numbers.get(i + 1) : 0;
                }
                numbers.set(i, result);
                numbers.remove(i + 1);
                operators.remove(i);
                i--;
            }
        }
        
        // Second pass: handle + and -
        for (int i = 0; i < operators.size(); i++) {
            double result;
            if (operators.get(i) == '+') {
                result = numbers.get(i) + numbers.get(i + 1);
            } else {
                result = numbers.get(i) - numbers.get(i + 1);
            }
            numbers.set(i, result);
            numbers.remove(i + 1);
            operators.remove(i);
            i--;
        }
        
        return numbers.get(0);
    }

    public static void main(String[] args) {
        new CalculatorApp();
    }
}