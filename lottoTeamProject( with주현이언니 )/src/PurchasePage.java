import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;

public class PurchasePage extends JFrame implements ActionListener {
	private Map<String, ArrayList<Integer>> listSelectedNumber;
	private JToggleButton[] numberButtons;
//   private Map<JPanel, JButton[]> panelButtonMap;
	private ArrayList<JPanel> panelList;
	private ArrayList<Integer> selectedNumbers;
	private JPanel currentPanel;
	private JPanel previousPanel;

	public PurchasePage() {
		setTitle("동행복권");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(900, 600);

		getContentPane().setBackground(Color.WHITE);
		getContentPane().setLayout(null);

		selectedNumbers = new ArrayList<>();
		// 맵 초기화
        listSelectedNumber = new TreeMap<>();
        

		JPanel pnlButtons = new JPanel();
		pnlButtons.setBounds(21, 107, 285, 337);
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
		btnReset.setBounds(22, 459, 82, 37);
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
				// 패널 업데이트를 통해 기존에 표시된 숫자들을 제거하고, UI를 초기 상태로 복구
				// updatePanels();
			}

		});
		JButton btnAuto = new JButton("자동");
		btnAuto.setFont(new Font("나눔고딕", Font.BOLD, 15));
		btnAuto.setBounds(126, 459, 82, 37);
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
			private int currentPanelIndex;

			@Override
			public void actionPerformed(ActionEvent e) {
				// 사용자가 선택한 숫자가 6개가 아닌 경우 경고 다이얼로그 표시
				if (selectedNumbers.size() != 6) {
					JOptionPane.showMessageDialog(null, "숫자 6개를 선택해주세요.", "경고", JOptionPane.WARNING_MESSAGE);
					return; // 추가 작업을 진행하지 않고 리턴
				}

				// 이전 패널의 수정 및 삭제 버튼 비활성화
				// enablePanelButtons(currentPanel);

				// 현재 패널에 선택된 번호 표시
				Collections.sort(selectedNumbers); // 오름차순으로 정렬
				updatePanels();
				System.out.println("현재 선택한 숫자들: " + selectedNumbers);

				// 현재 패널에 선택된 번호 저장
                String panelKey = "";
                switch(currentPanelIndex) {
                    case 0:
                        panelKey = "A";
                        break;
                    case 1:
                        panelKey = "B";
                        break;
                    case 2:
                        panelKey = "C";
                        break;
                    case 3:
                        panelKey = "D";
                        break;
                    case 4:
                        panelKey = "E";
                        break;
                }
                listSelectedNumber.put(panelKey, new ArrayList<>(selectedNumbers));

                // 선택된 번호 초기화
                selectedNumbers.clear();

//				System.out.println("A에저장된 값:" + listSelectedNumber.get("A"));
//				System.out.println("B에저장된 값:" + listSelectedNumber.get("B"));
//				System.out.println("A에저장된 값:" + listSelectedNumber.get("A"));
//
//				System.out.println(selectedNumbers);

				// 다음 패널 선택
				if (currentPanelIndex < panelList.size() - 1) {
					// enablePanelButtons(currentPanel);

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

			// 기존에 주석 처리된 메소드들은 필요에 따라 구현

		});

		btnOk.setFont(new Font("나눔고딕", Font.BOLD, 15));
		btnOk.setBounds(225, 459, 82, 37);
		getContentPane().add(btnOk);

		JPanel panelA = new JPanel();
		panelA.setBounds(392, 134, 285, 43);
		getContentPane().add(panelA);

		JPanel panelB = new JPanel();
		panelB.setBounds(392, 197, 285, 43);
		getContentPane().add(panelB);

		JPanel panelC = new JPanel();
		panelC.setBounds(392, 264, 285, 43);
		getContentPane().add(panelC);

		JPanel panelD = new JPanel();
		panelD.setBounds(392, 328, 285, 43);
		getContentPane().add(panelD);

		JPanel panelE = new JPanel();
		panelE.setBounds(394, 390, 285, 43);
		getContentPane().add(panelE);

		JButton btnUpdateA = new JButton("수정");
		btnUpdateA.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
			}
		});
		btnUpdateA.setEnabled(true);
		btnUpdateA.setFont(new Font("나눔고딕", Font.BOLD, 15));
		btnUpdateA.setBounds(688, 134, 72, 43);
		getContentPane().add(btnUpdateA);

		JButton btnUpdateB = new JButton("수정");
		btnUpdateB.setEnabled(true);
		btnUpdateB.setFont(new Font("나눔고딕", Font.BOLD, 15));
		btnUpdateB.setBounds(688, 197, 72, 43);
		getContentPane().add(btnUpdateB);

		JButton btnUpdateC = new JButton("수정");
		btnUpdateC.setEnabled(true);
		btnUpdateC.setFont(new Font("나눔고딕", Font.BOLD, 15));
		btnUpdateC.setBounds(688, 264, 72, 43);
		getContentPane().add(btnUpdateC);

		JButton btnUpdateD = new JButton("수정");
		btnUpdateD.setEnabled(true);
		btnUpdateD.setFont(new Font("나눔고딕", Font.BOLD, 15));
		btnUpdateD.setBounds(688, 328, 72, 43);
		getContentPane().add(btnUpdateD);

		JButton btnUpdateE = new JButton("수정");
		btnUpdateE.setEnabled(true);
		btnUpdateE.setFont(new Font("나눔고딕", Font.BOLD, 15));
		btnUpdateE.setBounds(690, 390, 72, 43);
		getContentPane().add(btnUpdateE);

		JButton btnDeleteA = new JButton("삭제");
		btnDeleteA.setEnabled(true);
		btnDeleteA.setFont(new Font("나눔고딕", Font.BOLD, 15));
		btnDeleteA.setBounds(779, 134, 72, 43);
		getContentPane().add(btnDeleteA);

		JButton btnDeleteB = new JButton("삭제");
		btnDeleteB.setEnabled(true);
		btnDeleteB.setFont(new Font("나눔고딕", Font.BOLD, 15));
		btnDeleteB.setBounds(780, 197, 69, 43);
		getContentPane().add(btnDeleteB);

		JButton btnDeleteC = new JButton("삭제");
		btnDeleteC.setEnabled(true);
		btnDeleteC.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnDeleteC.setFont(new Font("나눔고딕", Font.BOLD, 15));
		btnDeleteC.setBounds(779, 264, 72, 43);
		getContentPane().add(btnDeleteC);

		JButton btnDeleteD = new JButton("삭제");
		btnDeleteD.setEnabled(true);
		btnDeleteD.setFont(new Font("나눔고딕", Font.BOLD, 15));
		btnDeleteD.setBounds(779, 328, 72, 43);
		getContentPane().add(btnDeleteD);

		JButton btnDeleteE = new JButton("삭제");
		btnDeleteE.setEnabled(true);
		btnDeleteE.setFont(new Font("나눔고딕", Font.BOLD, 15));
		btnDeleteE.setBounds(781, 390, 72, 43);
		getContentPane().add(btnDeleteE);
		panelList = new ArrayList<>();
		panelList.add(panelA);
		panelList.add(panelB);
		panelList.add(panelC);
		panelList.add(panelD);
		panelList.add(panelE);
//      panelButtonMap = new HashMap<>();
//      panelButtonMap.put(panelA, new JButton[] { btnUpdateA, btnDeleteA });
//      panelButtonMap.put(panelB, new JButton[] { btnUpdateB, btnDeleteB });
//      panelButtonMap.put(panelB, new JButton[] { btnUpdateC, btnDeleteC });
//      panelButtonMap.put(panelB, new JButton[] { btnUpdateD, btnDeleteD });
//      panelButtonMap.put(panelB, new JButton[] { btnUpdateE, btnDeleteE });

		// Initially set current panel and enable buttons for it
		currentPanel = panelA;
		// enablePanelButtons(currentPanel);

		JLabel lblNewLabel = new JLabel("구매 금액:");
		lblNewLabel.setFont(new Font("나눔고딕", Font.BOLD, 15));
		lblNewLabel.setBounds(395, 456, 65, 29);
		getContentPane().add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("New label");
		lblNewLabel_1.setBounds(470, 456, 99, 29);
		getContentPane().add(lblNewLabel_1);

		JButton btnNewButton_1 = new JButton("구매하기");
		btnNewButton_1.setFont(new Font("나눔고딕", Font.BOLD, 15));
		btnNewButton_1.setBounds(633, 452, 146, 37);
		getContentPane().add(btnNewButton_1);

		JLabel lblNewLabel_2 = new JLabel("");
		// lblNewLabel_2.setIcon(new
		// ImageIcon(PurchasePage.class.getResource("/image/동행복권.png")));
		lblNewLabel_2.setBounds(279, 10, 211, 90);
		getContentPane().add(lblNewLabel_2);

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
		new PurchasePage();
	}
}