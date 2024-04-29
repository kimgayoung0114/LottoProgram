import javax.swing.*;

import dbutil.MySqlConnectionProvider;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.event.ActionEvent;
import java.text.DecimalFormat;

public class Records extends JFrame {
	private JPanel panel;
	private JPanel panel_1;
	private JPanel panel_A;
	private int round;
	private String addLbl_round;
	private JLabel lblNewLabel_1;
	private JPanel panel_D;
	private JPanel panel_E;
	private JPanel panel_B;
	private JPanel panel_C;
	private int totalMoney = 0;
	private JLabel totalwinnings;
	private boolean go;
	private boolean go2;
	private JLabel lbl_1;
	private JLabel lbl_2;
	private JLabel lbl_3;
	private JLabel lbl_4;
	private JLabel lbl_5;
	private Font font = new Font("맑은고딕", Font.BOLD, 15);;

	public Records(int round) {
		this.round = round;
		this.addLbl_round = Integer.toString(round); // round 값을 문자열로 변환하여 초기화
		extracted();
		showGUI();

		String sql = "SELECT * FROM purchase WHERE round = ?";
		try (Connection conn = MySqlConnectionProvider.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, round);

			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) { // 현재 회차를 구매한 경우 현재 회차 당첨 번호 나옴
					System.out.println(round+ "회차 구매 기록이 있습니다. 당첨 번호를 확인합니다");
					printWinningNumber();
					printPurchaseInfo();
					
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private ImageIcon createNumberIcon(int number) {
		int diameter = 30;
		BufferedImage image = new BufferedImage(diameter, diameter, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = image.createGraphics();

		if (number >= 1 && number <= 10) {
			g2d.setColor(new Color(250, 196, 0));
		} else if (number >= 11 && number <= 20) {
			g2d.setColor(new Color(104, 200, 242));
		} else if (number >= 21 && number <= 30) {
			g2d.setColor(new Color(255, 114, 113));
		} else if (number >= 31 && number <= 40) {
			g2d.setColor(new Color(170, 170, 170));
		} else if (number >= 41 && number <= 45) {
			g2d.setColor(new Color(176, 216, 65));
		} else {
			g2d.setColor(Color.BLACK);
		}

		g2d.fillOval(0, 0, diameter, diameter);

		Font font = new Font("맑은고딕", Font.BOLD, 15);
		g2d.setFont(font);
		g2d.setColor(Color.WHITE);

		FontMetrics fm = g2d.getFontMetrics();
		String numberText = String.valueOf(number);
		int x = (diameter - fm.stringWidth(numberText)) / 2;
		int y = (diameter - fm.getHeight()) / 2 + fm.getAscent();
		g2d.drawString(numberText, x, y);

		g2d.dispose();

		return new ImageIcon(image);
	}

	private void printWinningNumber() {
		String sql = "SELECT * FROM numberofwinners WHERE round = ?";
		try (Connection conn = MySqlConnectionProvider.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, round);
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					List<Integer> numbers = new ArrayList<>();
					numbers.add(rs.getInt("num1"));
					numbers.add(rs.getInt("num2"));
					numbers.add(rs.getInt("num3"));
					numbers.add(rs.getInt("num4"));
					numbers.add(rs.getInt("num5"));
					numbers.add(rs.getInt("num6"));

					addNumberIconsToPanel(numbers, panel);

					List<Integer> bonusList = new ArrayList<>();
					bonusList.add(rs.getInt("bonus"));
					addNumberIconsToPanel(bonusList, panel_1);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void addNumberIconsToPanel(List<Integer> numbers, JPanel panel) {
		panel.removeAll();
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

		for (Integer number : numbers) {
			JLabel label = new JLabel(createNumberIcon(number));
			panel.add(label);
		}

		panel.revalidate();
		panel.repaint();
	}

	private void printPurchaseInfo() {
		try (Connection conn = MySqlConnectionProvider.getConnection()) {
			// 패널에 대한 정보를 담고 있는 배열
			JPanel[] panels = { panel_A, panel_B, panel_C, panel_D, panel_E };
			String[] lines = { "A", "B", "C", "D", "E" };

			// 패널 수만큼 반복하면서 데이터를 가져와서 출력
			for (int i = 0; i < panels.length; i++) {
				String line = lines[i];
				JPanel panel = panels[i];

				// 해당 라인에 대한 데이터를 가져오는 SQL 쿼리
				String sql = "SELECT * FROM purchase WHERE round = ? AND line = ?";
				try (PreparedStatement stmt = conn.prepareStatement(sql)) {
					stmt.setInt(1, round);
					stmt.setString(2, line);

					try (ResultSet rs = stmt.executeQuery()) {
						while (rs.next()) {
							String method = rs.getString("method");
							String rank_result = rs.getString("result");
							int num1 = rs.getInt("num1");
							int num2 = rs.getInt("num2");
							int num3 = rs.getInt("num3");
							int num4 = rs.getInt("num4");
							int num5 = rs.getInt("num5");
							int num6 = rs.getInt("num6");

							List<Integer> numbers = new ArrayList<>();
							numbers.add(num1);
							numbers.add(num2);
							numbers.add(num3);
							numbers.add(num4);
							numbers.add(num5);
							numbers.add(num6);

							// 해당 패널에 숫자 아이콘 추가
							addNumberIconsToPanel(numbers, panel, rank_result);

							// 해당 라인에 method와 result를 출력하는 라벨에 데이터 설정
							setLabelData(line, method, rank_result);

							// 해당 라인의 총 당첨금을 계산하여 출력
							calMoney(rank_result);
						}
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// 해당 라인에 method와 result를 출력하는 라벨에 데이터 설정
	private void setLabelData(String line, String method, String result) {
		switch (line) {
		case "A":
			lbl_1.setText(method + " (" + result + ")");
			break;
		case "B":
			lbl_2.setText(method + " (" + result + ")");
			break;
		case "C":
			lbl_3.setText(method + " (" + result + ")");
			break;
		case "D":
			lbl_4.setText(method + " (" + result + ")");
			break;
		case "E":
			lbl_5.setText(method + " (" + result + ")");
			break;
		default:
			break;
		}
	}

	private void addNumberIconsToPanel(List<Integer> numbers, JPanel panel, String rank_result) {
		panel.removeAll();
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

		// 당첨 결과와 일치하는 숫자를 저장할 Set 생성
		Set<Integer> matchingNumbers = new HashSet<>();

		String sql = "SELECT num1, num2, num3, num4, num5, num6 FROM numberofwinners WHERE round = ?";
		try (Connection conn = MySqlConnectionProvider.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, round);
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					matchingNumbers.add(rs.getInt("num1"));
					matchingNumbers.add(rs.getInt("num2"));
					matchingNumbers.add(rs.getInt("num3"));
					matchingNumbers.add(rs.getInt("num4"));
					matchingNumbers.add(rs.getInt("num5"));
					matchingNumbers.add(rs.getInt("num6"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// 주어진 숫자 리스트를 순회하면서 아이콘 추가
		for (Integer number : numbers) {
			// 당첨 번호와 일치하는지 여부 확인
			if (matchingNumbers.contains(number)) {

				// 당첨 번호와 일치하는 경우, 컬러 아이콘 추가
				JLabel label = new JLabel(createNumberIcon(number));
				panel.add(label);
			} else {
				JLabel label;
				if (rank_result.equals("2등")) {
					label = new JLabel(createNumberIcon(number));
				} else {
					// 일치하지 않는 경우
				   label = new JLabel(createWhiteNumberIcon(number));
					
				}
				panel.add(label);
			}
		}

		panel.revalidate();
		panel.repaint();
	}

	private ImageIcon createWhiteNumberIcon(int number) {
		int diameter = 30;
		BufferedImage image = new BufferedImage(diameter, diameter, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = image.createGraphics();

		g2d.setColor(Color.WHITE);
		g2d.fillOval(0, 0, diameter, diameter);

		g2d.setFont(font);
		g2d.setColor(Color.BLACK);

		FontMetrics fm = g2d.getFontMetrics();
		String numberText = String.valueOf(number);
		int x = (diameter - fm.stringWidth(numberText)) / 2;
		int y = (diameter - fm.getHeight()) / 2 + fm.getAscent();
		g2d.drawString(numberText, x, y);

		g2d.dispose();

		return new ImageIcon(image);
	}

	private void calMoney(String result) {
		String sql = "SELECT money FROM winnings WHERE `rank` = ?";
		try (Connection conn = MySqlConnectionProvider.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql);) {
			stmt.setString(1, result);

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					int m = rs.getInt("money");
					totalMoney += m;

				}

				// totalMoney를 세 자리마다 쉼표로 구분하여 문자열로 변환
				DecimalFormat decimalFormat = new DecimalFormat("#,###");
				String formattedTotalMoney = decimalFormat.format(totalMoney);

				// totalwinnings 라벨에 쉼표로 구분된 문자열 설정
				totalwinnings.setText(formattedTotalMoney + "원");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void extracted() {
		getContentPane().setLayout(null);

		lbl_5 = new JLabel(" ");
		lbl_5.setFont(font);
		lbl_5.setBounds(111, 506, 99, 38);
		getContentPane().add(lbl_5);

		lbl_4 = new JLabel(" ");
		lbl_4.setFont(font);
		lbl_4.setBounds(111, 462, 99, 38);
		getContentPane().add(lbl_4);

		lbl_3 = new JLabel(" ");
		lbl_3.setFont(font);
		lbl_3.setBounds(111, 418, 99, 38);
		getContentPane().add(lbl_3);

		lbl_2 = new JLabel(" ");
		lbl_2.setFont(font);
		lbl_2.setBounds(111, 373, 99, 38);
		getContentPane().add(lbl_2);

		lbl_1 = new JLabel(" ");
		lbl_1.setFont(font);
		lbl_1.setBounds(111, 329, 99, 38);
		getContentPane().add(lbl_1);

		totalwinnings = new JLabel();
		totalwinnings.setFont(new Font("맑은고딕", Font.BOLD, 17));
		totalwinnings.setText(String.valueOf(totalMoney));
		totalwinnings.setBounds(378, 571, 150, 40);
		getContentPane().add(totalwinnings);

		panel_D = new JPanel();
		panel_D.setBackground(Color.WHITE);
		panel_D.setBounds(199, 458, 274, 40);
		getContentPane().add(panel_D);

		panel_E = new JPanel();
		panel_E.setBackground(Color.WHITE);
		panel_E.setBounds(199, 502, 274, 40);
		getContentPane().add(panel_E);

		panel_B = new JPanel();
		panel_B.setBackground(Color.WHITE);
		panel_B.setBounds(199, 368, 274, 40);
		getContentPane().add(panel_B);

		panel_C = new JPanel();
		panel_C.setBackground(Color.WHITE);
		panel_C.setBounds(199, 413, 274, 40);
		getContentPane().add(panel_C);

		panel_A = new JPanel(); // panel_2를 초기화
		panel_A.setBackground(Color.WHITE);
		panel_A.setBounds(199, 325, 274, 40);
		getContentPane().add(panel_A);

		JButton btnNewButton_2 = new JButton(">");
		isNextRound();
		if (!go) {
			btnNewButton_2.setEnabled(false);
		}
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				round++;
				dispose();

				// 새로운 round 값을 가지고 Result 객체를 생성합니다.
				Records newRecords = new Records(round);
				newRecords.setSize(523, 739);
				newRecords.setVisible(true);
				newRecords.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			}

		});

		btnNewButton_2.setFont(new Font("맑은 고딕", Font.BOLD, 43));
		btnNewButton_2.setContentAreaFilled(false);
		btnNewButton_2.setBorderPainted(false);
		btnNewButton_2.setFocusPainted(false);
		btnNewButton_2.setBounds(440, 95, 84, 40);
		getContentPane().add(btnNewButton_2);

		JButton btnNewButton_1 = new JButton("<");
		isPreviousRound();
		if (!go2) {
			btnNewButton_1.setEnabled(false);
		}
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				round--;

				dispose();

				// 새로운 round 값을 가지고 Result 객체를 생성합니다.
				Records newRecords = new Records(round);
				newRecords.setSize(523, 739);
				newRecords.setVisible(true);
				newRecords.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				
			}
		});

		btnNewButton_1.setToolTipText("이전 회차 결과 보기");
		btnNewButton_1.setFont(new Font("맑은 고딕", Font.BOLD, 43));
		btnNewButton_1.setContentAreaFilled(false);
		btnNewButton_1.setBorderPainted(false);
		btnNewButton_1.setFocusPainted(false);
		btnNewButton_1.setBounds(195, 95, 93, 40);
		getContentPane().add(btnNewButton_1);

		panel_1 = new JPanel();
		panel_1.setBackground(Color.WHITE);
		panel_1.setBounds(374, 233, 70, 40);
		getContentPane().add(panel_1);

		JLabel lblNewLabel_3 = new JLabel("+");
		lblNewLabel_3.setFont(new Font("맑은 고딕", Font.BOLD, 37));
		lblNewLabel_3.setBounds(347, 234, 46, 40);
		getContentPane().add(lblNewLabel_3);

		panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setBounds(71, 233, 274, 40);
		getContentPane().add(panel);

		JLabel lblNewLabel_2 = new JLabel("당첨번호");
		lblNewLabel_2.setFont(new Font("맑은 고딕", Font.BOLD, 22));
		lblNewLabel_2.setBounds(213, 185, 113, 30);
		getContentPane().add(lblNewLabel_2);

		JButton btnNewButton = new JButton("");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Main main = new Main();
				main.setLocationRelativeTo(Records.this); // Main 클래스의 위치에 맞추어 창이 생성됨
				main.setVisible(true);
				dispose();
			}
		});
		btnNewButton.setContentAreaFilled(false);
		btnNewButton.setBorderPainted(false);
		btnNewButton.setFocusPainted(false);
		btnNewButton.setBounds(210, 635, 105, 53);
		getContentPane().add(btnNewButton);

		lblNewLabel_1 = new JLabel("제 " + addLbl_round + "회");
		lblNewLabel_1.setFont(new Font("맑은 고딕", Font.BOLD, 30));
		lblNewLabel_1.setBounds(293, 100, 164, 40);
		getContentPane().add(lblNewLabel_1);

		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(Result.class.getResource("/image/로또 결과창(진짜진짜).jpg")));
		lblNewLabel.setBounds(-80, 0, 635, 716);
		getContentPane().add(lblNewLabel);
	}

	private void isNextRound() {
		round++;
		String sql = "select * from purchase where round = ?";
		try (Connection conn = MySqlConnectionProvider.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, round);

			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					go = true;
				} else {
					go = false;
				}
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		round--;
	}

	private void isPreviousRound() {
		round--;
		String sql = "select * from purchase where round = ?";
		try (Connection conn = MySqlConnectionProvider.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, round);

			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					go2 = true;
				} else {
					go2 = false;
				}
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		round++;
	}

	
	private void showGUI() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false); // 창 크기 고정
		setSize(523, 739);
		setLocationRelativeTo(null); // 화면 중앙에 위치
		setVisible(true);
	}

}