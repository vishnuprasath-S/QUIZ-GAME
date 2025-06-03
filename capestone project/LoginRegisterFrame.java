import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class LoginRegisterFrame extends JFrame {
    JTextField usernameField;
    JPasswordField passwordField;
    JButton loginButton, registerButton;
    JLabel messageLabel;

    public LoginRegisterFrame() {
        setTitle("Quiz Game - Login/Register");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400, 250);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(5, 1));

        usernameField = new JTextField();
        passwordField = new JPasswordField();
        loginButton = new JButton("Login");
        registerButton = new JButton("Register");
        messageLabel = new JLabel("", SwingConstants.CENTER);

        add(new JLabel("Username:", SwingConstants.CENTER));
        add(usernameField);
        add(new JLabel("Password:", SwingConstants.CENTER));
        add(passwordField);
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        add(buttonPanel);
        add(messageLabel);

        loginButton.addActionListener(e -> login());
        registerButton.addActionListener(e -> register());
    }

    private void login() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE username=? AND password=?");
            stmt.setString(1, username);
            stmt.setString(2, password); // Use hashed password in production!
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                messageLabel.setText("Login successful!");
                new DifficultySelectionFrame(rs.getInt("user_id"), username).setVisible(true);
                this.dispose();
            } else {
                messageLabel.setText("Invalid credentials!");
            }
        } catch (SQLException ex) {
            messageLabel.setText("Database error!");
            ex.printStackTrace();
        }
    }

    private void register() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO users (username, password) VALUES (?, ?)");
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.executeUpdate();
            messageLabel.setText("Registration successful! You can login now.");
        } catch (SQLException ex) {
            if (ex.getErrorCode() == 1062) { // Duplicate entry
                messageLabel.setText("Username already exists!");
            } else {
                messageLabel.setText("Database error!");
                ex.printStackTrace();
            }
        }
    }
}
