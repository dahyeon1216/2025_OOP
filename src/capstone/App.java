package capstone;

import capstone.controller.DonationPostController;
import capstone.controller.ScrapController;
import capstone.controller.UserController;
import capstone.model.BankType;
import capstone.service.DonationPostService;
import capstone.service.ScrapService;
import capstone.service.UserService;
import capstone.view.donation.DonationPostListView;
import capstone.view.style.RoundedBorder;
import capstone.view.style.RoundedButton;
import capstone.view.user.LoginView;
import capstone.view.user.SignupView;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class App extends JFrame {

    //커스텀 폰트
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

    public App(UserController userController,
               DonationPostController donationPostController,
               ScrapController scrapController) {

        setTitle("기부담다");
        setSize(393, 698);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(Color.WHITE);
        setLayout(new BorderLayout());

        // 상단 기부담다 텍스트
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(Color.WHITE);
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBorder(BorderFactory.createEmptyBorder(180, 0, 50, 0));

        JLabel subtitle = new JLabel("기부를 마켓처럼 !", SwingConstants.CENTER);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitle.setForeground(new Color(120,230,170));
        subtitle.setFont(customFont.deriveFont(Font.BOLD, 22f));

        JLabel mainTitle = new JLabel("기부담다", SwingConstants.CENTER);
        mainTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainTitle.setFont(customFont.deriveFont(Font.BOLD, 50f));
        mainTitle.setForeground(new Color(60,60,60));

        titlePanel.add(subtitle);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 10))); // 간격
        titlePanel.add(mainTitle);


        // 버튼
        JButton loginBtn = new RoundedButton("로그인", new Color(60, 60, 60), 30);
        loginBtn.setFont(customFont.deriveFont(Font.BOLD, 20f));
        loginBtn.setPreferredSize(new Dimension(230, 50));

        JButton signupBtn = new JButton("회원가입");
        signupBtn.setBackground(new Color(235,235,235));
        signupBtn.setForeground(Color.BLACK);
        signupBtn.setFont(customFont.deriveFont(Font.BOLD, 20f));
        signupBtn.setPreferredSize(new Dimension(230, 50));
        signupBtn.setBorder(new RoundedBorder(30));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setLayout(new GridLayout(2, 1, 15, 15));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 100, 30));
        buttonPanel.add(loginBtn);
        buttonPanel.add(signupBtn);

        add(titlePanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        setVisible(true);

        LoginView.LoginCallback loginCallback = user -> {
            new DonationPostListView(user, userController, donationPostController, scrapController);
        };

        loginBtn.addActionListener(e -> {
            new LoginView(userController, donationPostController, scrapController, loginUser -> {
                new DonationPostListView(loginUser, userController, donationPostController, scrapController);
            }).setVisible(true);
        });


        signupBtn.addActionListener(e -> {
            new SignupView(userController, () -> {
                new LoginView(userController, donationPostController, scrapController, loginUser -> {
                    new DonationPostListView(loginUser, userController, donationPostController, scrapController);
                }).setVisible(true);
            }).setVisible(true);
        });
    }


    public static void main(String[] args) {
        // 서비스 및 컨트롤러 생성
        UserService userService = new UserService();
        UserController userController = new UserController(userService);

        DonationPostService donationService = new DonationPostService();
        DonationPostController donationPostController = new DonationPostController(donationService);

        ScrapService scrapService = new ScrapService();
        ScrapController scrapController = new ScrapController(scrapService);

        // 테스트용 사용자 등록
        userService.signUp("default-profile.png","1", "1", "1", "123456789012345678901234567890", BankType.KAKAO, "1");
        userService.signUp("default-profile.png","2", "2", "2", "2", BankType.SHINHAN, "2");

        // App 실행 (시작 메뉴 열기)
        SwingUtilities.invokeLater(() ->
                new App(userController, donationPostController, scrapController));
    }
}
