import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.*;

public class EncryptionApp extends JFrame {
    private JTextField plainTextField, keyField, cipherTextField;
    private JLabel resultLabel;

    public EncryptionApp() {
        setTitle("Secure Communication - Java Version");
        setSize(700, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        add(new JLabel("Enter Plain Text:"));
        plainTextField = new JTextField(40);
        add(plainTextField);

        add(new JLabel("Secret Key (integer):"));
        keyField = new JTextField(10);
        add(keyField);

        JButton encryptBtn = new JButton("Encrypt");
        JButton decryptBtn = new JButton("Decrypt");
        JButton copyBtn = new JButton("Copy Result");

        add(encryptBtn);
        add(decryptBtn);
        add(copyBtn);

        add(new JLabel("Enter Cipher Text:"));
        cipherTextField = new JTextField(40);
        add(cipherTextField);

        resultLabel = new JLabel("Result will appear here");
        add(resultLabel);

        encryptBtn.addActionListener(e -> encryptAction());
        decryptBtn.addActionListener(e -> decryptAction());
        copyBtn.addActionListener(e -> copyToClipboard());

        getContentPane().setBackground(new Color(44, 62, 80));
        resultLabel.setForeground(Color.WHITE);
    }

    private void encryptAction() {
        try {
            String plain = plainTextField.getText();
            int userKey = Integer.parseInt(keyField.getText());
            String cipher = encrypt(plain, userKey);
            resultLabel.setText("Cipher Text: " + cipher);
        } catch (Exception ex) {
            resultLabel.setText("Error: Enter valid key!");
        }
    }

    private void decryptAction() {
        try {
            String cipher = cipherTextField.getText();
            int userKey = Integer.parseInt(JOptionPane.showInputDialog("Enter Secret Key:"));
            String plain = decrypt(cipher, userKey);
            resultLabel.setText("Plain Text: " + plain);
        } catch (Exception ex) {
            resultLabel.setText("Error: Invalid key!");
        }
    }

    private void copyToClipboard() {
        String text = resultLabel.getText().replace("Cipher Text:", "").trim();
        StringSelection selection = new StringSelection(text);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, selection);
        JOptionPane.showMessageDialog(this, "Copied to clipboard!");
    }

    private String encrypt(String text, int key) {
        StringBuilder temp = new StringBuilder();
        for (char c : text.toCharArray()) {
            temp.append((char) (c + key));
        }
        int val = generateProgramKey(text.length());
        StringBuilder finalCipher = new StringBuilder();
        for (char c : temp.toString().toCharArray()) {
            finalCipher.append((char) (c + val));
        }
        return finalCipher.toString();
    }

    private String decrypt(String text, int userKey) {
        int val = generateProgramKey(text.length());
        StringBuilder layer1 = new StringBuilder();
        for (char c : text.toCharArray()) {
            layer1.append((char) (c - val));
        }
        StringBuilder original = new StringBuilder();
        for (char c : layer1.toString().toCharArray()) {
            original.append((char) (c - userKey));
        }
        return original.toString();
    }

    private int generateProgramKey(int n) {
        n = n * 2;
        n += 2;
        n *= 5;
        n += 5;
        return nearestPrime(n);
    }

    private boolean isPrime(int n) {
        if (n < 2) return false;
        for (int i = 2; i <= Math.sqrt(n); i++) {
            if (n % i == 0) return false;
        }
        return true;
    }

    private int nearestPrime(int n) {
        int lower = n - 1, upper = n + 1;
        while (!isPrime(lower)) lower--;
        while (!isPrime(upper)) upper++;
        if (Math.abs(n - lower) <= Math.abs(n - upper)) return lower;
        else return upper;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EncryptionApp().setVisible(true));
    }
}
