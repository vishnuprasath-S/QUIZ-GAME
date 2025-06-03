import javax.swing.*;
import java.awt.*;

public class LevelSelectionFrame extends JFrame {
    int userId;
    String username;
    String difficulty;

    public LevelSelectionFrame(int userId, String username, String difficulty) {
        this.userId = userId;
        this.username = username;
        this.difficulty = difficulty;

        setTitle("Select Level - " + difficulty);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(300, 200);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1));
        for (int i = 1; i <= 3; i++) {
            int level = i;
            JButton levelButton = new JButton("Level " + level);
            levelButton.addActionListener(e -> startQuiz(level));
            panel.add(levelButton);
        }
        add(panel);
    }

    private void startQuiz(int level) {
        new QuizFrame(userId, username, difficulty, level).setVisible(true);
        this.dispose();
    }
}