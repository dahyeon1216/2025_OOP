package capstone.view.donation;

import capstone.controller.DonationPostController;
import capstone.controller.ScrapController;
import capstone.model.DonationPost;
import capstone.model.User;
import capstone.view.BaseView;
import capstone.view.style.RoundedButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import static capstone.model.BankType.KB;

public class DonateActonCompleteView extends BaseView {

    public DonateActonCompleteView(int donatedPoint, Runnable afterCloseAction) {
        super("기부하기 완료");


        setSize(393, 698);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(Color.WHITE);
        getContentPane().setLayout(new BorderLayout());

        // 중앙 패널
        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        // 나무 이미지
        JLabel flowerLabel = new JLabel();
        flowerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        ImageIcon icon = new ImageIcon("icons/tree.png"); // 아이콘 경로
        Image scaled = icon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
        flowerLabel.setIcon(new ImageIcon(scaled));
        flowerLabel.setBorder(BorderFactory.createEmptyBorder(100, 0, 30, 0));

        // 텍스트
        JLabel successLabel = new JLabel("기부 완료 !");
        successLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        successLabel.setFont(customFont.deriveFont(Font.BOLD, 33f));
        successLabel.setForeground(Color.BLACK);
        successLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));

        int accumulatedPoint = (int) (donatedPoint * 0.01); // 기부 포인트의 1% 계산
        JLabel pointAccumulationLabel = new JLabel(accumulatedPoint + "포인트가 적립되었습니다");
        pointAccumulationLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        pointAccumulationLabel.setFont(customFont.deriveFont(Font.BOLD, 24f)); // 폰트 스타일 변경 (UI 이미지 참조)
        pointAccumulationLabel.setForeground(Color.GRAY); // 색상 유지
        pointAccumulationLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

        //  중앙 컴포넌트 조립
        centerPanel.add(flowerLabel);
        centerPanel.add(successLabel);
        centerPanel.add(pointAccumulationLabel);
        centerPanel.add(Box.createVerticalGlue()); // 아래 공간 확보

        // 하단 버튼 (footer)
        JButton mainBtn = new RoundedButton("상세화면으로", new Color(60, 60, 60), 30);
        mainBtn.setFont(customFont.deriveFont(Font.BOLD, 20f));
        mainBtn.setPreferredSize(new Dimension(0, 44));
        mainBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        mainBtn.addActionListener(e -> {
            dispose();
            afterCloseAction.run();
        });

        JPanel footer = new JPanel(new BorderLayout());
        footer.setBorder(new EmptyBorder(20, 20, 20, 20));
        footer.setOpaque(false); // 바깥 회색 박스 없애기
        footer.add(mainBtn, BorderLayout.CENTER);

        //  전체 조립
        getContentPane().add(centerPanel, BorderLayout.CENTER);
        getContentPane().add(footer, BorderLayout.SOUTH);

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
                    KB,
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

            // 가상 계좌 (더미)
            capstone.model.VirtualAccount va = new capstone.model.VirtualAccount(KB, "110-1234-5678");
            dummyPost.setVirtualAccount(va);

            // 컨트롤러 (더미 생성)
            capstone.service.DonationPostService dummyService = new capstone.service.DonationPostService();
            DonationPostController dummyDonationPostController = new DonationPostController(dummyService);

            capstone.service.ScrapService dummyScrapService = new capstone.service.ScrapService();
            ScrapController dummyScrapController = new ScrapController(dummyScrapService);

            // 테스트용 onPostUpdated 콜백
            Runnable dummyOnPostUpdated = () -> System.out.println("업데이트 콜백 호출됨");

            // View 실행 (기부한 금액: 예: 10000P 기부했다고 가정)
            new DonateActonCompleteView(10000,null);
        });
    }

}
