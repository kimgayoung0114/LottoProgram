import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class WinnerOrNot { // 당첨 유무 확인 클래스
    private static final String DB_URL = "jdbc:mysql://localhost:3306/lotto";
    private static final String USER = "root";
    private static final String PASSWORD = "root";
    private int round;

    public WinnerOrNot(int round) {
        this.round = round;
    }

    public void determineWinners() {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            char line = 'A';
            while (line <= 'E') {
                determineWinnersForLine(conn, round, line);
                line++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void determineWinnersForLine(Connection conn, int round, char line) throws SQLException {
        int[] purchasedNumbers = getPurchasedNumbers(conn, round, line);
        int[] winningNumbers = getWinningNumbers(conn, round);
        int bonusNumber = getBonusNumber(conn, round);

        int matchingNumbers = countMatchingNumbers(purchasedNumbers, winningNumbers);
        String result = determineResult(matchingNumbers, bonusNumber);

        updateResult(conn, round, line, result);
    }

    private int[] getPurchasedNumbers(Connection conn, int round, char line) throws SQLException {
        int[] numbers = new int[6];
        String sql = "SELECT num1, num2, num3, num4, num5, num6 FROM purchase WHERE round = ? AND line = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, round);
            pstmt.setString(2, String.valueOf(line));
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    for (int i = 0; i < 6; i++) {
                        numbers[i] = rs.getInt(i + 1);
                    }
                }
            }
        }
        return numbers;
    }

    private int[] getWinningNumbers(Connection conn, int round) throws SQLException {
        int[] numbers = new int[6];
        String sql = "SELECT num1, num2, num3, num4, num5, num6 FROM numberofwinners WHERE round = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, round);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    for (int i = 0; i < 6; i++) {
                        numbers[i] = rs.getInt(i + 1);
                    }
                }
            }
        }
        return numbers;
    }

    private int getBonusNumber(Connection conn, int round) throws SQLException {
        int bonusNumber = 0;
        String sql = "SELECT bonus FROM numberofwinners WHERE round = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, round);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    bonusNumber = rs.getInt("bonus");
                }
            }
        }
        return bonusNumber;
    }

    private int countMatchingNumbers(int[] purchasedNumbers, int[] winningNumbers) {
        int count = 0;
        for (int num : purchasedNumbers) {
            for (int winningNum : winningNumbers) {
                if (num == winningNum) {
                    count++;
                    break;
                }
            }
        }
        return count;
    }

    private String determineResult(int matchingNumbers, int bonusNumber) {
        if (matchingNumbers == 6) {
            return "1등";
        } else if (matchingNumbers == 5 && hasMatchingBonus(matchingNumbers, bonusNumber)) {
            return "2등";
        } else if (matchingNumbers == 5) {
            return "3등";
        } else if (matchingNumbers == 4) {
            return "4등";
        } else if (matchingNumbers == 3) {
            return "5등";
        } else {
            return "낙첨";
        }
    }

    private boolean hasMatchingBonus(int matchingNumbers, int bonusNumber) {
        return matchingNumbers == 5 && bonusNumber > 0;
    }

    private void updateResult(Connection conn, int round, char line, String result) throws SQLException {
        String sql = "UPDATE purchase SET result = ? WHERE round = ? AND line = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, result);
            pstmt.setInt(2, round);
            pstmt.setString(3, String.valueOf(line));
            pstmt.executeUpdate();
        }
    }

    public static void main(String[] args) {
        int round = 1000; // 검사할 회차 입력
        WinnerOrNot winnerOrNot = new WinnerOrNot(round);
        winnerOrNot.determineWinners();
    }
}
