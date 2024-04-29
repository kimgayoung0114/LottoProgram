import javax.swing.JFrame;
import javax.swing.JLabel;

import dbutil.MySqlConnectionProvider;

import javax.swing.ImageIcon;
import java.awt.Color;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Main extends JFrame {
	private JLabel lblNowRound;
	private int round;
	private JButton btnResult;

	public Main() {
		extracted();
		callRound();
		disableBtn();
		showGUI();

	}

	private void callRound() {
		String sql = "SELECT * FROM lottorounds order by round DESC LIMIT 1;";
		try (Connection conn = MySqlConnectionProvider.getConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {
			if (rs.next()) {
				round = rs.getInt("round");
				lblNowRound.setText("제 " + round + "회");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void showGUI() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false); // 창 크기 고정
		setSize(733, 423);
		setLocationRelativeTo(null); // 화면 중앙에 위치
		setVisible(true);
	}

	private void extracted() {

		getContentPane().setBackground(Color.WHITE);
		getContentPane().setLayout(null);
		
		JButton btnRecord = new JButton("이전 기록 보기");
		btnRecord.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Records records = new Records(round-1);
				records.setLocationRelativeTo(Main.this); // Main 클래스의 위치에 맞추어 창이 생성됨
				records.setVisible(true);
				dispose();
			}
		});
		btnRecord.setBounds(392, 288, 136, 50);
		getContentPane().add(btnRecord);

		JButton btnPurchase = new JButton("구매하기");
		btnPurchase.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
//				PurchasePage p = new PurchasePage(round);
//				p.setLocationRelativeTo(Main.this);
//				p.setVisible(true);
//				dispose();
			}
		});
		btnPurchase.setBounds(63, 288, 136, 50);
		getContentPane().add(btnPurchase);

		btnResult = new JButton("결과보기");
		btnResult.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Result result = new Result(round);
				result.setLocationRelativeTo(Main.this); // Main 클래스의 위치에 맞추어 창이 생성됨
				result.setVisible(true);
				dispose();
			}
		});
		btnResult.setBounds(232, 288, 136, 50);
		getContentPane().add(btnResult);

		lblNowRound = new JLabel("제 회");
		lblNowRound.setFont(new Font("맑은 고딕", Font.BOLD, 30));
		lblNowRound.setBounds(63, 71, 191, 50);
		getContentPane().add(lblNowRound);

		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(Main.class.getResource("/image/ㄷㅎㅂㄱ.png")));
		lblNewLabel.setBounds(0, -33, 767, 455);
		getContentPane().add(lblNewLabel);
	}
	// 구매 전이라면 비활성화하는 메소드
	private void disableBtn() {
		String sql = "SELECT * FROM purchase WHERE round = ?";
		try (Connection conn = MySqlConnectionProvider.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, round);
			
			try (ResultSet rs = stmt.executeQuery()){
				if (!rs.next()) { // 존재하지 않는다면 버튼 비활성화
					btnResult.setEnabled(false);
				} 
			}
			
		} catch (SQLException e) {
				e.printStackTrace();
			}
	}

	public static void main(String[] args) {
		new Main();

	}
}
