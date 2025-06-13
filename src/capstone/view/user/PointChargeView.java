package capstone.view.user;

import capstone.model.User;
import capstone.view.style.RoundedBorder;
import capstone.view.style.RoundedButton;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class PointChargeView extends JFrame {

    //폰트
    private static Font customFont;
    static {
        try {
            customFont = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/font1.ttf")).deriveFont(15f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
        } catch (Exception e) {
            customFont = new Font("SansSerif", Font.PLAIN, 15);
            e.printStackTrace();
        }
    }

    private User loginUser; // 생성자에서 전달받은 loginUser를 저장
    private int amountToCharge = 0; // 현재 선택되거나 입력된 충전 예정 금액
    private JLabel currentPointLabel; // "보유 포인트"를 표시하는 라벨 (기존 pointLabel)
    private JLabel selectedAmountLabel; // 사용자가 선택한/입력한 금액을 크게 보여주는 라벨 (예: '34040 P')

    public PointChargeView(User loginUser) {
        this.loginUser = loginUser; // 멤버 변수에 로그인 사용자 정보 저장

        setTitle("포인트 충전");
        setSize(393, 698);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE); // 이 창만 닫히도록 함

        if (this.loginUser == null) {
            JOptionPane.showMessageDialog(this, "로그인한 사용자만 접근할 수 있습니다.", "접근 제한", JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 20)); // 좌우 여백 20px 유지


        // 왼쪽 정렬 contentPanel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);


        // 왼쪽 정렬 패널
        JPanel leftAlignedPanel = new JPanel();
        leftAlignedPanel.setLayout(new BoxLayout(leftAlignedPanel, BoxLayout.Y_AXIS));
        leftAlignedPanel.setOpaque(false);
        leftAlignedPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        ImageIcon checkIcon = new ImageIcon("icons/check.png"); // 아이콘 경로에 맞게 수정
        Image scaledIcon = checkIcon.getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH); // 원하는 크기 조절
        checkIcon = new ImageIcon(scaledIcon);

        JLabel bankInfo = new JLabel(this.loginUser.getBankType().getBankName() + " " + this.loginUser.getBankAccount());
        bankInfo.setIcon(checkIcon);
        bankInfo.setIconTextGap(5); // 아이콘과 텍스트 사이 간격
        bankInfo.setFont(customFont.deriveFont(20f));
        bankInfo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel nameLabel = new JLabel(this.loginUser.getName());
        nameLabel.setFont(customFont.deriveFont(Font.BOLD, 18f));
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel chargeLabel = new JLabel("얼마를 충전할까요?");
        chargeLabel.setFont(customFont.deriveFont(Font.BOLD, 30f));
        chargeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // --- currentPointLabel 초기화 (기존 pointLabel을 재활용) ---
        currentPointLabel = new JLabel("보유 포인트 " + this.loginUser.getPoint() + " P");
        currentPointLabel.setFont(customFont.deriveFont(18f));
        currentPointLabel.setForeground(Color.GRAY);
        currentPointLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        currentPointLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5)); // 오른쪽 여백 5px 추가 (필요에 따라 조절)

        leftAlignedPanel.add(bankInfo);
        leftAlignedPanel.add(Box.createVerticalStrut(5));
        leftAlignedPanel.add(nameLabel);
        leftAlignedPanel.add(Box.createVerticalStrut(30));
        leftAlignedPanel.add(chargeLabel);
        leftAlignedPanel.add(currentPointLabel);
        leftAlignedPanel.add(Box.createVerticalStrut(20));

        // --- selectedAmountLabel 초기화 및 추가 ---
        selectedAmountLabel = new JLabel("0 P"); // 초기값은 0
        selectedAmountLabel.setFont(customFont.deriveFont(Font.BOLD, 38f)); // 이미지와 유사하게 매우 큰 폰트
        selectedAmountLabel.setAlignmentX(Component.LEFT_ALIGNMENT);


        // 금액 선택 버튼 (기존 코드 유지, 액션 리스너만 수정)
        JPanel amountPanel = new JPanel(new GridLayout(2, 3, 15, 20)); // 가로 15, 세로 20 간격 (이미지 기준)
        amountPanel.setOpaque(false);
        amountPanel.setMaximumSize(new Dimension(327, 100)); // 기존 코드 유지
        amountPanel.setAlignmentX(Component.LEFT_ALIGNMENT); // 기존 코드 유지

        String[] amounts = {"10000", "20000", "30000", "40000", "50000", "기타"};


        for (String amt : amounts) {
            String label = amt.equals("기타") ? "기타" : "+" + (Integer.parseInt(amt) / 10000) + "만원";

            // --- RoundedButton 초기화 시 기존 스타일 유지 ---
            RoundedButton btn = new RoundedButton(label, Color.WHITE, 20); // 원래대로 Color.WHITE
            btn.setForeground(new Color(120, 120, 120)); // 원래대로 new Color(120, 120, 120)
            btn.setFocusPainted(false);
            btn.setFont(customFont.deriveFont(Font.PLAIN, 14f)); // 원래대로 14f
            btn.setBorder(new RoundedBorder(30, new Color(180, 180, 180))); // 원래대로 new Color(180, 180, 180)
            btn.setPreferredSize(new Dimension(90, 38)); // 원래대로 90, 38
            amountPanel.add(btn);
            // amountButtons.add(btn); // 버튼 스타일 변경 기능 제거로 인해 이 리스트 추가는 이제 필요 없음. 주석 처리


            if (!amt.equals("기타")) {
                btn.addActionListener(e -> {
                    int added = Integer.parseInt(amt);
                    amountToCharge = added; // 선택된 금액을 amountToCharge에 저장
                    selectedAmountLabel.setText(amountToCharge + " P"); // UI 업데이트
                });
            } else {
                btn.addActionListener(e -> {
                    String input = JOptionPane.showInputDialog(this, "충전할 금액을 입력하세요:");
                    try {
                        if (input == null || input.trim().isEmpty()) {
                            return;
                        }
                        int customAmt = Integer.parseInt(input);
                        if (customAmt <= 0) {
                            throw new NumberFormatException();
                        }
                        amountToCharge = customAmt; // 입력된 금액 저장
                        selectedAmountLabel.setText(amountToCharge + " P"); // UI 업데이트
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "올바른 숫자를 입력해주세요.", "입력 오류", JOptionPane.ERROR_MESSAGE);
                    }
                });
            }
        }


        // contentPanel에 요소 추가
        contentPanel.add(leftAlignedPanel);
        contentPanel.add(selectedAmountLabel); // selectedAmountLabel 추가
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(new JSeparator()); //포인트 밑 줄줄
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(amountPanel);


        // 왼쪽 정렬 래퍼로 contentPanel 감싸기
        JPanel leftWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        leftWrapper.setOpaque(false);
        leftWrapper.add(contentPanel);

        mainPanel.add(Box.createVerticalGlue());
        mainPanel.add(leftWrapper);
        mainPanel.add(Box.createVerticalStrut(30));


        // 충전 버튼은 가운데 정렬
        RoundedButton chargeButton = new RoundedButton("충전하기", new Color(60, 60, 60), 30);
        chargeButton.setPreferredSize(new Dimension(327, 44));
        chargeButton.setMaximumSize(new Dimension(700, 44));
        chargeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        chargeButton.setFont(customFont.deriveFont(Font.BOLD, 18f));
        chargeButton.setForeground(Color.WHITE);
        mainPanel.add(chargeButton);
        mainPanel.add(Box.createVerticalGlue());

        // --- 충전하기 버튼 액션 리스너 수정 ---
        chargeButton.addActionListener(e -> {
            if (amountToCharge > 0) { // 충전할 금액이 0보다 큰지 유효성 검사
                // 실제 포인트 충전 로직 (모델 업데이트)
                this.loginUser.setPoint(this.loginUser.getPoint() + amountToCharge);

                // UI 업데이트
                currentPointLabel.setText("보유 포인트 " + this.loginUser.getPoint() + " P"); // 보유 포인트 업데이트
                JOptionPane.showMessageDialog(this, amountToCharge + " P가 충전되었습니다.", "충전 완료", JOptionPane.INFORMATION_MESSAGE);

                // 충전 후 상태 초기화
                amountToCharge = 0; // 충전 예정 금액 초기화
                selectedAmountLabel.setText("0 P"); // 표시 금액 초기화
            } else {
                JOptionPane.showMessageDialog(this, "충전할 금액을 선택하거나 입력해주세요.", "경고", JOptionPane.WARNING_MESSAGE);
            }
        });

        add(mainPanel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            capstone.model.User testUser = new capstone.model.User();
            testUser.setName("김예린");
            testUser.setBankType(capstone.model.BankType.SHINHAN);
            testUser.setBankAccount("110989191893");
            testUser.setPoint(0);
            new PointChargeView(testUser).setVisible(true);
        });
    }


}
