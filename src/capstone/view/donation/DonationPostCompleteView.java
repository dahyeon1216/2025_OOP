package capstone.view.donation;

import capstone.controller.DonationPostController;
import capstone.controller.UserController;
import capstone.model.User;
import capstone.service.DonationPostService;
import capstone.service.UserService;
import capstone.view.Roundborder.RoundedButton;
import capstone.view.main.DonationPostListView;
import capstone.view.main.MainView;

import java.awt.*;import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.io.File;

public class DonationPostCompleteView extends JFrame {

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
    private final DonationPostListView donationPostListView;

    public DonationPostCompleteView(User loginUser, UserController userController,
                                    DonationPostController donationPostController, DonationPostListView donationPostListView) {
        super("기부글 업로드 완료");

        this.loginUser = loginUser;
        this.userController = userController;
        this.donationPostController = donationPostController;
        this.donationPostListView = donationPostListView;

        setSize(393, 698);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(Color.WHITE);
        getContentPane().setLayout(new BorderLayout());

        // 중앙 패널
        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        // 꽃 이미지
        JLabel flowerLabel = new JLabel();
        flowerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        ImageIcon icon = new ImageIcon("icons/flower.png"); // 아이콘 경로
        Image scaled = icon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
        flowerLabel.setIcon(new ImageIcon(scaled));
        flowerLabel.setBorder(BorderFactory.createEmptyBorder(100, 0, 30, 0));

        // 텍스트
        JLabel successLabel = new JLabel("기부글 업로드 완료 !");
        successLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        successLabel.setFont(customFont.deriveFont(Font.BOLD, 33f));
        successLabel.setForeground(Color.BLACK);
        successLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));

        JLabel accountLabel = new JLabel("모금통장: 12310294234");
        accountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        accountLabel.setFont(customFont.deriveFont(Font.BOLD, 24f));
        accountLabel.setForeground(Color.GRAY);
        accountLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

        //  중앙 컴포넌트 조립
        centerPanel.add(flowerLabel);
        centerPanel.add(successLabel);
        centerPanel.add(accountLabel);
        centerPanel.add(Box.createVerticalGlue()); // 아래 공간 확보

        // 하단 버튼 (footer)
        JButton mainBtn = new RoundedButton("메인으로", new Color(60, 60, 60), 30);
        mainBtn.setFont(customFont.deriveFont(Font.BOLD, 20f));
        mainBtn.setPreferredSize(new Dimension(0, 44));
        mainBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));


        mainBtn.addActionListener(e -> {
            dispose(); // 현재 완료 화면 닫기

            // 이전에 저장했던 DonationPostListView 인스턴스를 사용
            if (this.donationPostListView != null) {
                this.donationPostListView.refreshCardList(); // <-- 목록 새로 고침 요청
                this.donationPostListView.setVisible(true); // <-- 목록 뷰 다시 보이게 함
            } else {
                // 만약 참조가 없다면, 새로운 뷰를 생성하는 백업 로직 (하지만 가급적이면 참조를 전달하는 것이 좋음)
                new DonationPostListView(loginUser, donationPostController, null).setVisible(true);
            }
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
        User dummyUser = new User("sally1023", "Sally", "프로필 URL 예시");

        // 서비스 → 컨트롤러
        UserService dummyUserService = new UserService(); // 만약 인자가 필요하면 수정
        UserController dummyUserController = new UserController(dummyUserService);

        DonationPostService dummyPostService = new DonationPostService(); // 마찬가지로 인자가 필요하면 수정
        DonationPostController dummyPostController = new DonationPostController(dummyPostService);

        // 완료화면 실행
        SwingUtilities.invokeLater(() ->
                new DonationPostCompleteView(dummyUser, dummyUserController, dummyPostController)
        );
    }*/
}



