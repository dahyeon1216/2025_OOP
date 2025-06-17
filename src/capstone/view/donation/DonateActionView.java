package capstone.view.donation;

import capstone.controller.DonationPostController;
import capstone.controller.ScrapController;
import capstone.model.BankType;
import capstone.model.DonationPost;
import capstone.model.Tier;
import capstone.model.User;
import capstone.service.DonationPostService;
import capstone.service.ScrapService;
import capstone.view.BaseView;
import capstone.view.style.RoundedBorder;
import capstone.view.style.RoundedButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class DonateActionView extends BaseView {

    private int amountToDonate = 0;
    private JLabel selectedAmountLabel;

    public DonateActionView(DonationPost post, User loginUser,
                            DonationPostController donationPostController, ScrapController scrapController,
                            Runnable onPostUpdated) {
        super("기부하기");

        add(createHeader("기부하기"), BorderLayout.NORTH);
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(-20, 20, 20, 20));

        // 왼쪽 정렬 contentPanel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);

        // 완쪽 정렬 패널
        JPanel leftAlignedPanel = new JPanel();
        leftAlignedPanel.setLayout(new BoxLayout(leftAlignedPanel, BoxLayout.Y_AXIS));
        leftAlignedPanel.setOpaque(false);
        leftAlignedPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // 상단: 이미지 + 텍스트
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
        topPanel.setBackground(Color.WHITE);

        // 이미지
        String imgPath = "resources/images/donation/" + post.getDonationImg();
        File imgFile = new File(imgPath);
        ImageIcon icon = imgFile.exists() ? new ImageIcon(imgPath) : new ImageIcon("icons/image-fail.png");
        Image scaledImage = icon.getImage().getScaledInstance(67, 67, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
        imageLabel.setPreferredSize(new Dimension(67, 67));
        imageLabel.setBorder(new RoundedBorder(20));

        // 텍스트(D-day, 제목)
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(Color.WHITE);
        textPanel.setBorder(new EmptyBorder(0, 15, 0, 5));

        long dDay = ChronoUnit.DAYS.between(LocalDate.now(), post.getEndAt());
        String dDayText = dDay >= 0 ? "D-" + dDay : "D+" + Math.abs(dDay);
        JLabel dDayLabel = new JLabel(dDayText);
//        dDayLabel.setBounds(20, 10, 50, 20);
        dDayLabel.setFont(customFont.deriveFont(Font.BOLD, 18f));

        JLabel titleLabel = new JLabel(post.getTitle());
        titleLabel.setFont(customFont.deriveFont(Font.BOLD, 22f));

        textPanel.add(dDayLabel);
        textPanel.add(Box.createVerticalStrut(5));
        textPanel.add(titleLabel);

        topPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        topPanel.add(imageLabel);
        topPanel.add(textPanel);

        JLabel chargeLabel = new JLabel("얼마를 기부할까요?");
        chargeLabel.setFont(customFont.deriveFont(Font.BOLD, 30f));
        chargeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel currentPointLabel = new JLabel("보유 포인트: " + loginUser.getPoint() + " P");
        currentPointLabel.setFont(customFont.deriveFont(18f));
        currentPointLabel.setForeground(Color.GRAY);
        currentPointLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        currentPointLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5)); // 오른쪽 여백 5px 추가 (필요에 따라 조절)

        leftAlignedPanel.add(topPanel);
        leftAlignedPanel.add(Box.createVerticalStrut(30));
        leftAlignedPanel.add(chargeLabel);
        leftAlignedPanel.add(currentPointLabel);
        leftAlignedPanel.add(Box.createVerticalStrut(20));


        // --- selectedAmountLabel 초기화 및 추가 ---
        selectedAmountLabel = new JLabel("0 P"); // 초기값은 0
        selectedAmountLabel.setFont(customFont.deriveFont(Font.BOLD, 38f));
        selectedAmountLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        selectedAmountLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));


        // 금액 선택 버튼 (기존 코드 유지, 액션 리스너만 수정)
        JPanel amountPanel = new JPanel(new GridLayout(2, 3, 15, 20)); // 가로 15, 세로 20 간격 (이미지 기준)
        amountPanel.setOpaque(false);
        amountPanel.setMaximumSize(new Dimension(327, 100)); // 기존 코드 유지
        amountPanel.setAlignmentX(Component.LEFT_ALIGNMENT); // 기존 코드 유지
        String[] amounts = {"10000", "20000", "30000", "40000", "50000", "기타"};
        for (String amt : amounts) {
            String label = amt.equals("기타") ? "기타" : "+" + (Integer.parseInt(amt) / 10000) + "만원";

            // --- RoundedButton 초기화 시 기존 스타일 유지 ---
            RoundedButton btn = new RoundedButton(label, Color.WHITE, 20);
            btn.setForeground(new Color(120, 120, 120));
            btn.setFocusPainted(false);
            btn.setFont(customFont.deriveFont(Font.PLAIN, 14f)); // 원래대로 14f
            btn.setBorder(new RoundedBorder(30, new Color(180, 180, 180)));
//            btn.setPreferredSize(new Dimension(93, 38));
            btn.setHorizontalAlignment(SwingConstants.CENTER);
            amountPanel.add(btn);

            if (!amt.equals("기타")) {
                btn.addActionListener(e -> {
                    int added = Integer.parseInt(amt);
                    amountToDonate = added; // 선택된 금액을 amountToDonate 저장
                    selectedAmountLabel.setText(amountToDonate + " P"); // UI 업데이트
                });
            } else {
                btn.addActionListener(e -> {
                    String input = JOptionPane.showInputDialog(this, "기부할 금액을 입력하세요:");
                    try {
                        if (input == null || input.trim().isEmpty()) {
                            return;
                        }
                        int customAmt = Integer.parseInt(input);
                        if (customAmt <= 0) {
                            throw new NumberFormatException();
                        }
                        amountToDonate = customAmt; // 입력된 금액 저장
                        selectedAmountLabel.setText(amountToDonate + " P"); // UI 업데이트
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
        RoundedButton donateButton = new RoundedButton("기부하기", new Color(60, 60, 60), 30);
        donateButton.setPreferredSize(new Dimension(327, 44));
        donateButton.setMaximumSize(new Dimension(700, 44));
        donateButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        donateButton.setFont(customFont.deriveFont(Font.BOLD, 18f));
        donateButton.setForeground(Color.WHITE);
        mainPanel.add(donateButton);
        mainPanel.add(Box.createVerticalGlue());

        donateButton.addActionListener(e -> {
            if (amountToDonate > 0) {
                if (amountToDonate > loginUser.getPoint()) {
                    JOptionPane.showMessageDialog(this, "보유 포인트가 부족합니다.", "포인트 부족", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                boolean success = donationPostController.donate(post, loginUser, amountToDonate);
                if (success) {
                    // 새로고침 콜백 호출
                    if (onPostUpdated != null) {
                        onPostUpdated.run();
                    }
                    new DonateActonCompleteView(amountToDonate, onPostUpdated).setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "기부 실패", "오류", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "기부할 금액을 선택하거나 입력해주세요.", "경고", JOptionPane.WARNING_MESSAGE);
            }
        });


        add(mainPanel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // 임시 사용자 생성
            User testUser = new User(
                    "testuser", "password", "테스트 사용자", "테스트닉네임",
                    "profile.jpg", BankType.KB, "123-456-7890",
                    50000, Tier.SILVER
            );

            // 임시 기부글 생성 (유저가 작성자)
            DonationPost testPost = new DonationPost(
                    testUser,
                    "test-image.jpg",  // 실제 테스트 이미지 경로 지정
                    100000,
                    LocalDate.now().plusDays(30),
                    "테스트 기부글",
                    "테스트 기부 내용입니다."
            );

            // 컨트롤러는 일단 null-safe 목업 생성
            DonationPostController dummyDonationPostController = new DonationPostController(new DonationPostService()) {
                @Override
                public boolean donate(DonationPost post, User user, int point) {
                    System.out.println(point + "P 기부 처리됨 (모의)");
                    user.setPoint(user.getPoint() - point);
                    return true;
                }
            };

            ScrapController dummyScrapController = new ScrapController(new ScrapService());

            // 화면 띄우기
            DonateActionView view = new DonateActionView(
                    testPost,
                    testUser,
                    dummyDonationPostController,
                    dummyScrapController,
                    () -> System.out.println("기부 이후 새로고침 호출됨")
            );
            view.setVisible(true);
        });
    }

}
