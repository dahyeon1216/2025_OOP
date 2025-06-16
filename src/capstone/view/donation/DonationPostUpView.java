package capstone.view.donation;

import capstone.controller.DonationPostController;
import capstone.model.DonationPost;
import capstone.model.User;
import capstone.view.BaseView;
import capstone.view.style.RoundedButton;

import javax.swing.*;
import java.awt.*;

public class DonationPostUpView extends BaseView {

    private static final int WIDTH = 320;
    private static final int HEIGHT = 320;
    private final User loginUser;
    private final DonationPostController donationPostController;
    private final DonationPost post;

    public DonationPostUpView(User loginUser, DonationPost post, DonationPostController donationPostController, Runnable onPostUpdated) {
        super("기부글 UP");
        this.loginUser=loginUser;
        this.post = post;
        this.donationPostController = donationPostController;

//        System.out.println(post.getUpFuncAt());

        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);

        // 메인 패널 생성 (모든 UI 요소를 담을 패널)
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));

        // 텍스트 레이블
        JLabel upQuestionLabel = new JLabel("UP 하시겠습니까?");
        upQuestionLabel.setFont(customFont.deriveFont(Font.BOLD, 20));
        upQuestionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 포인트 차감 문구 레이블
        JLabel pointDeductionLabel = new JLabel("포인트 300P가 차감됩니다.");
        pointDeductionLabel.setFont(customFont.deriveFont(Font.BOLD, 14));
        pointDeductionLabel.setForeground(Color.GRAY);
        pointDeductionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 이미지 아이콘 레이블
        JLabel imageLabel = new JLabel();
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // 가운데 정렬
        ImageIcon originalIcon = new ImageIcon("icons/TalkFile.png");
        Image originalImage = originalIcon.getImage();
        Image scaledImage = originalImage.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        ImageIcon imageIcon = new ImageIcon(scaledImage);
        imageLabel.setIcon(imageIcon);


        // 각 요소 사이에 공간 추가
        mainPanel.add(upQuestionLabel);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(pointDeductionLabel);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(imageLabel);
        mainPanel.add(Box.createVerticalStrut(0));

        // 버튼 패널 생성
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 30));
        buttonPanel.setBackground(Color.WHITE); // 배경색 설정

        // "취소하기" 버튼 생성
        RoundedButton cancelButton = new RoundedButton("취소하기", new Color(230, 230, 230), 20);
        cancelButton.setPreferredSize(new Dimension(100, 40));
        cancelButton.setFont(customFont.deriveFont(Font.BOLD, 16f));
        cancelButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        //취소하기 버튼 액션리스너
        cancelButton.addActionListener(e -> {
            dispose();
        });

        // "UP 하기" 버튼 생성
        RoundedButton upButton = new RoundedButton("UP 하기", Color.DARK_GRAY, 20);
        upButton.setFont(customFont.deriveFont(Font.BOLD, 16f));
        upButton.setPreferredSize(new Dimension(100, 40));

        // "UP 하기" 버튼 액션리스너
        upButton.addActionListener(e -> {
            boolean success = donationPostController.upPost(post, loginUser);
            if (success) {
                JOptionPane.showMessageDialog(this, "해당 글이 상단에 노출되었습니다!");
                onPostUpdated.run();
//                System.out.println(post.getUpFuncAt());
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "포인트가 부족합니다. 최소 300P가 필요합니다.");
            }
        });

        buttonPanel.add(cancelButton);
        buttonPanel.add(upButton);

        // 메인 패널에 버튼 패널 추가
        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // 더미 User 생성
            User dummyUser = new User(
                    "testuser",
                    "password123",
                    "테스트 사용자",
                    "테스트닉네임",
                    "profile.jpg",
                    capstone.model.BankType.KB,
                    "123-4567-8901",
                    10000,
                    capstone.model.Tier.SILVER
            );

            // 더미 DonationPost 생성
            DonationPost dummyPost = new DonationPost(
                    dummyUser,
                    "donation.jpg",
                    50000,
                    java.time.LocalDate.now().plusDays(30),
                    "기부 테스트",
                    "내용"
            );

            // 더미 DonationPostController 생성
            capstone.service.DonationPostService dummyService = new capstone.service.DonationPostService();
            DonationPostController dummyController = new DonationPostController(dummyService);

            // 테스트용 onPostUpdated 콜백 (단순히 콘솔 출력)
            Runnable dummyOnPostUpdated = () -> System.out.println("업데이트 콜백 호출됨");

            // View 실행
            new DonationPostUpView(dummyUser, dummyPost, dummyController, dummyOnPostUpdated);
        });
    }


}
