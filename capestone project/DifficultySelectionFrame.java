import javax.swing.*;
import java.awt.*;

public class DifficultySelectionFrame extends JFrame {
    int userId;
    String username;

    public DifficultySelectionFrame(int userId, String username) {
        this.userId = userId;
        this.username = username;
        setTitle("Select Difficulty");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(300, 200);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1));

        JButton easyButton = new JButton("Easy");
        JButton mediumButton = new JButton("Medium");
        JButton hardButton = new JButton("Hard");

        easyButton.addActionListener(e -> startQuizFlow("easy"));
        mediumButton.addActionListener(e -> startQuizFlow("medium"));
        hardButton.addActionListener(e -> startQuizFlow("hard"));

        panel.add(easyButton);
        panel.add(mediumButton);
        panel.add(hardButton);

        add(panel);
    }

    private void startQuizFlow(String difficulty) {
        new QuizFlowFrame(userId, username, difficulty, 1, 0).setVisible(true);
        this.dispose();
    }
}