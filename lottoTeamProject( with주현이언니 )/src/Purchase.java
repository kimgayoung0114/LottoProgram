

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;

import javax.swing.JButton;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.ImageIcon;

public class Purchase extends JFrame implements ActionListener {
	private JToggleButton[] numberButtons;
	private Map<JPanel, JButton[]> panelButtonMap;
	private ArrayList<JPanel> panelList;
	private ArrayList<Integer> selectedNumbers;
	private JPanel currentPanel;
	private JPanel previousPanel;

	public Purchase() {
		setTitle("동행복권");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(900, 560);

		getContentPane().setBackground(Color.WHITE);
		getContentPane().setLayout(null);

		selectedNumbers = new ArrayList<>();

		JPanel pnlButtons = new JPanel();
		pnlButtons.setBounds(21, 105, 285, 296);
		getContentPane().add(pnlButtons);
		numberButtons = new JToggleButton[45];
		for (int i = 0; i < 45; i++) {
			numberButtons[i] = new JToggleButton(String.valueOf(i + 1));
			numberButtons[i].addActionListener(this);
			pnlButtons.add(numberButtons[i]);
		}
		getContentPane().add(pnlButtons);
		pnlButtons.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		JButton btnReset = new JButton("초기화");
		btnReset.setFont(new Font("나눔고딕", Font.BOLD, 15));
		btnReset.setBounds(22, 421, 82, 37);
		getContentPane().add(btnReset);
		btnReset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (Component component : pnlButtons.getComponents()) {
					if (component instanceof JToggleButton) {
						JToggleButton button = (JToggleButton) component;
						button.setSelected(false);
					}
				}
				// 선택된 숫자들을 저장하는 리스트 초기화
				selectedNumbers.clear();

			}

		});

		JButton btnAuto = new JButton("자동");
		btnAuto.setFont(new Font("나눔고딕", Font.BOLD, 15));
		btnAuto.setBounds(123, 421, 82, 37);
		getContentPane().add(btnAuto);
		btnAuto.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int remaining = 6 - selectedNumbers.size();
				if (remaining > 0) {
					ArrayList<Integer> remainingNumbers = new ArrayList<>();
					// 선택되지 않은 번호를 찾아서 리스트에 추가
					for (int i = 0; i < numberButtons.length; i++) {
						if (!selectedNumbers.contains(i + 1)) {
							remainingNumbers.add(i + 1);
						}
					}
					// 랜덤하게 번호 선택하여 리스트에 추가
					while (remaining > 0 && !remainingNumbers.isEmpty()) {
						int randomIndex = (int) (Math.random() * remainingNumbers.size());
						int selectedNumber = remainingNumbers.remove(randomIndex);
						selectedNumbers.add(selectedNumber);
						numberButtons[selectedNumber - 1].setSelected(true); // 해당 번호에 해당하는 토글 버튼 누름
						remaining--;
					}
//               updatePanels(); // 패널 업데이트
				}
			}
		});

		JButton btnOk = new JButton("확인");
		btnOk.addActionListener(new ActionListener() {
			private int currentPanelIndex = 0;

			@Override
			public void actionPerformed(ActionEvent e) {
				// 현재 패널에 선택된 번호 표시
				updatePanels();

				enablePanelButtons(currentPanel);

				// 선택된 번호 초기화
				selectedNumbers.clear();

				// 다음 패널 선택
				if (currentPanelIndex < panelList.size() - 1) {

					currentPanelIndex++;
					currentPanel = panelList.get(currentPanelIndex);

					// 현재 패널에 선택된 번호 표시
					updatePanels();
					System.out.println(currentPanelIndex);
					System.out.println(panelList.size());
				} else {
					currentPanelIndex++;
				}

				// 확인 버튼 비활성화 조건 검사
				if (currentPanelIndex == panelList.size()) { // 패널 리스트의 크기와 같을 때
					btnOk.setEnabled(false);
				}
				resetButtons();
			}

			private void resetButtons() {
				for (Component component : pnlButtons.getComponents()) {
					if (component instanceof JToggleButton) {
						JToggleButton button = (JToggleButton) component;
						button.setSelected(false);
					}
				}
			}

		});

		btnOk.setFont(new Font("나눔고딕", Font.BOLD, 15));
		btnOk.setBounds(225, 420, 82, 37);
		getContentPane().add(btnOk);

		JPanel panelA = new JPanel();
		panelA.setBounds(389, 105, 285, 43);
		getContentPane().add(panelA);

		JPanel panelB = new JPanel();
		panelB.setBounds(389, 165, 285, 43);
		getContentPane().add(panelB);

		JPanel panelC = new JPanel();
		panelC.setBounds(389, 232, 285, 43);
		getContentPane().add(panelC);

		JPanel panelD = new JPanel();
		panelD.setBounds(389, 296, 285, 43);
		getContentPane().add(panelD);

		JPanel panelE = new JPanel();
		panelE.setBounds(391, 358, 285, 43);
		getContentPane().add(panelE);

		JButton btnUpdateA = new JButton("수정");
		btnUpdateA.setEnabled(false);
		btnUpdateA.setFont(new Font("나눔고딕", Font.BOLD, 15));
		btnUpdateA.setBounds(685, 105, 72, 43);
		getContentPane().add(btnUpdateA);

		JButton btnUpdateB = new JButton("수정");
		btnUpdateB.setEnabled(false);
		btnUpdateB.setFont(new Font("나눔고딕", Font.BOLD, 15));
		btnUpdateB.setBounds(685, 165, 72, 43);
		getContentPane().add(btnUpdateB);

		JButton btnUpdateC = new JButton("수정");
		btnUpdateC.setEnabled(false);
		btnUpdateC.setFont(new Font("나눔고딕", Font.BOLD, 15));
		btnUpdateC.setBounds(685, 232, 72, 43);
		getContentPane().add(btnUpdateC);

		JButton btnUpdateD = new JButton("수정");
		btnUpdateD.setEnabled(false);
		btnUpdateD.setFont(new Font("나눔고딕", Font.BOLD, 15));
		btnUpdateD.setBounds(685, 296, 72, 43);
		getContentPane().add(btnUpdateD);

		JButton btnUpdateE = new JButton("수정");
		btnUpdateE.setEnabled(false);
		btnUpdateE.setFont(new Font("나눔고딕", Font.BOLD, 15));
		btnUpdateE.setBounds(687, 358, 72, 43);
		getContentPane().add(btnUpdateE);

		JButton btnDeleteA = new JButton("삭제");
		btnDeleteA.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// 해당 패널의 원들을 모두 제거
		        panelA.removeAll();
		        // 패널을 다시 그리도록 강제 호출
		        panelA.revalidate();
		        panelA.repaint();
		        
		        // 사용자가 선택한 숫자들을 초기화
		        selectedNumbers.clear();
			}
			
		});
		btnDeleteA.setEnabled(false);
		btnDeleteA.setFont(new Font("나눔고딕", Font.BOLD, 15));
		btnDeleteA.setBounds(776, 105, 72, 43);
		getContentPane().add(btnDeleteA);

		JButton btnDeleteB = new JButton("삭제");
		btnDeleteB.setEnabled(false);
		btnDeleteB.setFont(new Font("나눔고딕", Font.BOLD, 15));
		btnDeleteB.setBounds(777, 165, 69, 43);
		getContentPane().add(btnDeleteB);

		JButton btnDeleteC = new JButton("삭제");
		btnDeleteC.setEnabled(false);
		btnDeleteC.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnDeleteC.setFont(new Font("나눔고딕", Font.BOLD, 15));
		btnDeleteC.setBounds(776, 232, 72, 43);
		getContentPane().add(btnDeleteC);

		JButton btnDeleteD = new JButton("삭제");
		btnDeleteD.setEnabled(false);
		btnDeleteD.setFont(new Font("나눔고딕", Font.BOLD, 15));
		btnDeleteD.setBounds(776, 296, 72, 43);
		getContentPane().add(btnDeleteD);

		JButton btnDeleteE = new JButton("삭제");
		btnDeleteE.setEnabled(false);
		btnDeleteE.setFont(new Font("나눔고딕", Font.BOLD, 15));
		btnDeleteE.setBounds(778, 358, 72, 43);
		getContentPane().add(btnDeleteE);
		panelList = new ArrayList<>();
		panelList.add(panelA);
		panelList.add(panelB);
		panelList.add(panelC);
		panelList.add(panelD);
		panelList.add(panelE);
		panelButtonMap = new HashMap<>();
		panelButtonMap.put(panelA, new JButton[] { btnUpdateA, btnDeleteA });
		panelButtonMap.put(panelB, new JButton[] { btnUpdateB, btnDeleteB });
		panelButtonMap.put(panelC, new JButton[] { btnUpdateC, btnDeleteC });
		panelButtonMap.put(panelD, new JButton[] { btnUpdateD, btnDeleteD });
		panelButtonMap.put(panelE, new JButton[] { btnUpdateE, btnDeleteE });

		// Initially set current panel and enable buttons for it
		currentPanel = panelA;
		// enablePanelButtons(currentPanel);

		JLabel lblNewLabel = new JLabel("구매 금액:");
		lblNewLabel.setFont(new Font("나눔고딕", Font.BOLD, 15));
		lblNewLabel.setBounds(431, 430, 65, 29);
		getContentPane().add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("New label");
		lblNewLabel_1.setBounds(506, 430, 99, 29);
		getContentPane().add(lblNewLabel_1);

		JButton btnNewButton_1 = new JButton("구매하기");
		btnNewButton_1.setFont(new Font("나눔고딕", Font.BOLD, 15));
		btnNewButton_1.setBounds(669, 426, 146, 37);
		getContentPane().add(btnNewButton_1);

		JLabel lblNewLabel_2 = new JLabel("");
		lblNewLabel_2.setIcon(new ImageIcon(Purchase.class.getResource("/image/동행복권.png")));
		lblNewLabel_2.setBounds(279, 10, 211, 90);
		getContentPane().add(lblNewLabel_2);
		
		JLabel lblNewLabel_3 = new JLabel("A");
		lblNewLabel_3.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 17));
		lblNewLabel_3.setBounds(360, 111, 26, 29);
		getContentPane().add(lblNewLabel_3);
		
		JLabel lblB = new JLabel("B");
		lblB.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 17));
		lblB.setBounds(360, 168, 26, 29);
		getContentPane().add(lblB);
		
		JLabel lblC = new JLabel("C");
		lblC.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 17));
		lblC.setBounds(360, 235, 26, 29);
		getContentPane().add(lblC);
		
		JLabel lblD = new JLabel("D");
		lblD.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 17));
		lblD.setBounds(360, 299, 26, 29);
		getContentPane().add(lblD);
		
		JLabel lblE = new JLabel("E");
		lblE.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 17));
		lblE.setBounds(360, 361, 26, 29);
		getContentPane().add(lblE);

		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		for (int i = 0; i < numberButtons.length; i++) {
			if (e.getSource() == numberButtons[i]) {
				int selectedNumber = i + 1;
				if (selectedNumbers.contains(selectedNumber)) {
					selectedNumbers.remove((Integer) selectedNumber);
				} else {
					if (selectedNumbers.size() < 6) { // 최대 6개까지 선택
						selectedNumbers.add(selectedNumber);
					}
				}
				// updatePanels();
				// 만약 선택된 번호가 6개면 모든 토글 버튼 비활성화
				if (selectedNumbers.size() == 6) {
					disableAdditionalToggleButtons();
				}
				break;
			}
		}
	}

	// 추가적인 토글 버튼 비활성화
	private void disableAdditionalToggleButtons() {
		for (JToggleButton button : numberButtons) {
			if (!selectedNumbers.contains(Integer.parseInt(button.getText()))) {
				button.setSelected(false);
			}
		}
	}

// 수정 삭제 버튼 활성화
	private void enablePanelButtons(JPanel currentPanel) {
		JButton[] buttons = panelButtonMap.get(currentPanel);
		if (buttons != null) {
			for (JButton button : buttons) {
				button.setEnabled(true);
			}
		}

	}

	private void updatePanels() {
		// 각 패널에 표시되는 선택된 번호의 인덱스
		int selectedIndex = 0;

		// 각 패널에 대해
		for (int i = 0; i < panelList.size(); i++) {
			JPanel panel = panelList.get(i);

			// 해당 패널에 선택된 번호 표시
			for (int j = 0; j < 6; j++) {
				// 선택된 번호가 더 이상 없을 경우 종료
				if (selectedIndex >= selectedNumbers.size()) {
					break;
				}

				// 현재 선택된 번호를 원으로 그리기
				String numberText = String.valueOf(selectedNumbers.get(selectedIndex));
				ImageIcon numberIcon = createNumberIcon(numberText);
				JLabel label = new JLabel(numberIcon);
				currentPanel.add(label);

				// 다음 선택된 번호를 위해 인덱스 증가
				selectedIndex++;
			}

			panel.revalidate(); // 패널 다시 그리기
			panel.repaint();

			// 선택된 번호가 더 이상 없을 경우 종료
			if (selectedIndex >= selectedNumbers.size()) {
				break;
			}
		}
	}

	private ImageIcon createNumberIcon(String numberText) {
		int diameter = 30;
		BufferedImage image = new BufferedImage(diameter, diameter, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = image.createGraphics();
		int number = Integer.parseInt(numberText);
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
		g2d.fillOval(0, 0, diameter, diameter); // 원 그리기

		// 텍스트 그리기
		Font font = new Font("맑은고딕", Font.BOLD, 15);
		g2d.setFont(font);
		g2d.setColor(Color.WHITE); // 텍스트 색상
		FontMetrics fm = g2d.getFontMetrics();
		int x = (diameter - fm.stringWidth(numberText)) / 2;
		int y = (diameter - fm.getHeight()) / 2 + fm.getAscent();
		g2d.drawString(numberText, x, y);

		g2d.dispose(); // 그래픽스 객체 해제

		return new ImageIcon(image);
	}

	public static void main(String[] args) {
		// Event Dispatch Thread (EDT)에서 GUI를 생성하고 보여주기
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new Purchase(); // 애플리케이션의 메인 윈도우 생성
			}
		});
	}
}
