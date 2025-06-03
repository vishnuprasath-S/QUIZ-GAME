import javax.swing.UIManager;
import java.awt.Font;

public class QuizGameMain {
    public static void main(String[] args) {
        // Set default font for all Swing components
        Font font = new Font("Arial", Font.PLAIN, 18); // or any size you prefer
        UIManager.put("Label.font", font);
        UIManager.put("Button.font", font);
        UIManager.put("TextField.font", font);
        UIManager.put("PasswordField.font", font);
        UIManager.put("TextArea.font", font);
        UIManager.put("RadioButton.font", font);
        UIManager.put("ComboBox.font", font);

        javax.swing.SwingUtilities.invokeLater(() -> {
            new LoginRegisterFrame().setVisible(true);
        });
    }
}