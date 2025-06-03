import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;

public class QuizFlowFrame extends JFrame {
    int userId;
    String username;
    String difficulty;
    int currentLevel;
    int totalScore;

    JLabel levelLabel;

    public QuizFlowFrame(int userId, String username, String difficulty, int startLevel, int accumulatedScore) {
        this.userId = userId;
        this.username = username;
        this.difficulty = difficulty;
        this.currentLevel = startLevel;
        this.totalScore = accumulatedScore;

        setTitle("Quiz - " + difficulty + " Level " + currentLevel);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 350);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());
        levelLabel = new JLabel("Level: " + currentLevel, SwingConstants.CENTER);
        levelLabel.setFont(new Font("Arial", Font.BOLD, 22));
        add(levelLabel, BorderLayout.NORTH);

        startLevelQuiz();
    }

    private void startLevelQuiz() {
        QuizPanel quizPanel = new QuizPanel(userId, username, difficulty, currentLevel);
        quizPanel.setListener(levelScore -> {
            totalScore += levelScore;
            if (currentLevel < 3) {
                // Next level
                new QuizFlowFrame(userId, username, difficulty, currentLevel + 1, totalScore).setVisible(true);
            } else {
                // All levels done, show scoreboard
                new ScoreboardFrame(userId, username, difficulty, 0, totalScore).setVisible(true);
            }
            this.dispose();
        });
        add(quizPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    // Inner class for quiz panel at each level
    static class QuizPanel extends JPanel {
        int userId;
        String username;
        String difficulty;
        int level;
        ArrayList<Question> questions;
        int currentIndex = 0;
        int score = 0;
        JLabel questionLabel;
        JRadioButton[] optionButtons = new JRadioButton[4];
        ButtonGroup group;
        JButton nextButton;
        QuizCompleteListener listener;

        public QuizPanel(int userId, String username, String difficulty, int level) {
            this.userId = userId;
            this.username = username;
            this.difficulty = difficulty;
            this.level = level;
            setLayout(new BorderLayout());
            setFont(new Font("Arial", Font.PLAIN, 18));

            questions = fetchQuestions();

            questionLabel = new JLabel();
            questionLabel.setFont(new Font("Arial", Font.BOLD, 18));
            add(questionLabel, BorderLayout.NORTH);

            JPanel optionsPanel = new JPanel(new GridLayout(4, 1));
            group = new ButtonGroup();
            for (int i = 0; i < 4; i++) {
                optionButtons[i] = new JRadioButton();
                optionButtons[i].setFont(new Font("Arial", Font.PLAIN, 18));
                group.add(optionButtons[i]);
                optionsPanel.add(optionButtons[i]);
            }
            add(optionsPanel, BorderLayout.CENTER);

            nextButton = new JButton("Next");
            nextButton.setFont(new Font("Arial", Font.BOLD, 18));
            add(nextButton, BorderLayout.SOUTH);

            nextButton.addActionListener(e -> submitAnswer());

            updateQuestion();
        }

        private ArrayList<Question> fetchQuestions() {
            ArrayList<Question> qList = new ArrayList<>();
            try (Connection conn = DBConnection.getConnection()) {
                PreparedStatement stmt = conn.prepareStatement(
                    "SELECT * FROM questions WHERE difficulty=? AND level=? LIMIT 5"
                );
                stmt.setString(1, difficulty);
                stmt.setInt(2, level);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    qList.add(new Question(
                        rs.getString("question_text"),
                        rs.getString("option_a"),
                        rs.getString("option_b"),
                        rs.getString("option_c"),
                        rs.getString("option_d"),
                        rs.getString("correct_option").charAt(0)
                    ));
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return qList;
        }

        private void updateQuestion() {
            if (currentIndex < questions.size()) {
                Question q = questions.get(currentIndex);
                questionLabel.setText((currentIndex + 1) + ". " + q.text);
                for (int i = 0; i < 4; i++) {
                    char opt = (char)('A' + i);
                    optionButtons[i].setText(opt + ". " + q.getOption(opt));
                }
                group.clearSelection();
            } else {
                saveScore();
                if (listener != null) listener.onComplete(score);
            }
        }

        private void submitAnswer() {
            if (group.getSelection() == null) {
                JOptionPane.showMessageDialog(this, "Please select an option.");
                return;
            }
            char selected = ' ';
            for (int i = 0; i < 4; i++) {
                if (optionButtons[i].isSelected()) {
                    selected = (char) ('A' + i);
                }
            }
            if (questions.get(currentIndex).correct == selected) {
                score++;
            }
            currentIndex++;
            updateQuestion();
        }

        private void saveScore() {
            try (Connection conn = DBConnection.getConnection()) {
                PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO scores (user_id, score, difficulty, level) VALUES (?, ?, ?, ?)"
                );
                stmt.setInt(1, userId);
                stmt.setInt(2, score);
                stmt.setString(3, difficulty);
                stmt.setInt(4, level);
                stmt.executeUpdate();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        public void setListener(QuizCompleteListener listener) {
            this.listener = listener;
        }

        static class Question {
            String text, a, b, c, d;
            char correct;
            public Question(String text, String a, String b, String c, String d, char correct) {
                this.text = text;
                this.a = a;
                this.b = b;
                this.c = c;
                this.d = d;
                this.correct = correct;
            }
            public String getOption(char opt) {
                switch (opt) {
                    case 'A': return a;
                    case 'B': return b;
                    case 'C': return c;
                    case 'D': return d;
                }
                return "";
            }
        }
        interface QuizCompleteListener {
            void onComplete(int levelScore);
        }
    }
}