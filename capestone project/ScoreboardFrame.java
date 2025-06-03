import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class ScoreboardFrame extends JFrame {
    public ScoreboardFrame(int userId, String username, String difficulty, int level, int totalScore) {
        setTitle("Scoreboard");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());
        JLabel resultLabel = new JLabel("Your Total Score: " + totalScore, SwingConstants.CENTER);
        resultLabel.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(resultLabel, BorderLayout.NORTH);

        JTextArea scoreboard = new JTextArea();
        scoreboard.setFont(new Font("Arial", Font.PLAIN, 18));
        scoreboard.setEditable(false);
        panel.add(new JScrollPane(scoreboard), BorderLayout.CENTER);

        // Fetch top scores
        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                "SELECT u.username, SUM(s.score) as total_score FROM scores s " +
                "JOIN users u ON s.user_id = u.user_id " +
                "WHERE s.difficulty=? GROUP BY s.user_id ORDER BY total_score DESC LIMIT 10"
            );
            stmt.setString(1, difficulty);
            ResultSet rs = stmt.executeQuery();
            scoreboard.append("Top Scores (" + difficulty + "):\n");
            int rank = 1;
            while (rs.next()) {
                scoreboard.append(rank++ + ". " + rs.getString("username") + ": " + rs.getInt("total_score") + "\n");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        add(panel);
    }
}