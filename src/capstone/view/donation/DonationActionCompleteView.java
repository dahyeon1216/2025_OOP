package capstone.view.donation;

import capstone.controller.DonationPostController;
import capstone.controller.UserController;
import capstone.model.DonationPost;
import capstone.model.Tier;
import capstone.model.User;
import capstone.service.DonationPostService;
import capstone.service.UserService;
import capstone.view.Roundborder.RoundedButton;
import capstone.view.main.DonationPostListView;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;

public class DonationActionCompleteView extends JFrame {

    // 커스텀 폰트 로딩
    private static Font customFont;
    static {
        try {
            customFont = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/font1.ttf")).deriveFont(16f);
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(customFont);
        } catch (Exception e) {
            e.printStackTrace();
            customFont = new Font("SansSerif", Font.PLAIN, 16); // 폴백 폰트
        }
    }

    private final User loginUser;
    private final UserController userController;
    private final DonationPostController donationPostController;
    private final DonationPost uploadedPost; //업로드된 게시물 객체


    public DonationActionCompleteView(User loginUser, UserController userController, DonationPostController donationPostController,int donatedPoint, DonationPost uploadedPost) {
        super("기부글 업로드 완료");

        this.loginUser = loginUser;
        this.userController = userController;
        this.donationPostController = donationPostController;
        this.uploadedPost = uploadedPost;

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
            dispose(); // 현재 완료 화면 닫기
            new DonationPostDetailView(
                    uploadedPost,          // 업로드된 게시물
                    loginUser,             // 로그인 유저
                    donationPostController,// 컨트롤러
                    () -> {},              // refreshAction (이 시점에서는 특별히 할 것 없음, 필요시 구현)
                    null                   // previousView (현재 CompleteView 뒤에는 다른 뷰가 없음)
            ).setVisible(true);

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


    // 테스트용 메인
    /*public static void main(String[] args) {
        // 더미 유저
        User dummyUser = new User("sally1023", "Sally", "프로필 URL 예시", Tier.SILVER); // Tier 추가

        // 서비스 → 컨트롤러 (더미)
        UserService dummyUserService = new UserService();
        UserController dummyUserController = new UserController(dummyUserService);
        DonationPostService dummyPostService = new DonationPostService();
        DonationPostController dummyPostController = new DonationPostController(dummyPostService);

        int testDonatedPoint = 5000; // 테스트를 위한 기부 포인트 (예: 5000원 기부)

        // 완료화면 실행
        SwingUtilities.invokeLater(() ->
                new DonationActionCompleteView(dummyUser, dummyUserController, dummyPostController, testDonatedPoint)
        );
    }*/
}
