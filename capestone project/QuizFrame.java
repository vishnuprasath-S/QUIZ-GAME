import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;

public class QuizFrame extends JFrame {
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

    public QuizFrame(int userId, String username, String difficulty, int level) {
        this.userId = userId;
        this.username = username;
        this.difficulty = difficulty;
        this.level = level;

        setTitle("Quiz - " + difficulty + " Level " + level);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 300);
        setLocationRelativeTo(null);

        questions = fetchQuestions();

        setLayout(new BorderLayout());
        questionLabel = new JLabel();
        add(questionLabel, BorderLayout.NORTH);

        JPanel optionsPanel = new JPanel(new GridLayout(4, 1));
        group = new ButtonGroup();
        for (int i = 0; i < 4; i++) {
            optionButtons[i] = new JRadioButton();
            group.add(optionButtons[i]);
            optionsPanel.add(optionButtons[i]);
        }
        add(optionsPanel, BorderLayout.CENTER);

        nextButton = new JButton("Next");
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
            optionButtons[0].setText("A. " + q.a);
            optionButtons[1].setText("B. " + q.b);
            optionButtons[2].setText("C. " + q.c);
            optionButtons[3].setText("D. " + q.d);
            group.clearSelection();
        } else {
            // Save score and show scoreboard
            saveScore();
            new ScoreboardFrame(userId, username, difficulty, level, score).setVisible(true);
            this.dispose();
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

    // Inner class for holding question data
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
    }
}