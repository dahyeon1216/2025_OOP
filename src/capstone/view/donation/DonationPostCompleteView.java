package capstone.view.donation;

//기부글 업로드시 보이는 화면
//피그마 - 기부글 업로드 완료

import capstone.controller.DonationPostController;
import capstone.controller.UserController;
import capstone.model.DonationPost;
import capstone.model.User;
import capstone.service.DonationPostService;
import capstone.view.style.RoundedButton;


import java.awt.*;import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.io.File;

import static capstone.model.BankType.KB;
import static capstone.model.Tier.BRONZE;

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



    private DonationPostService donationPostService;

    public DonationPostCompleteView(DonationPost createdPost) {
        super("기부글 업로드 완료");

        this.donationPostService = new DonationPostService();


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

        String virtualAccountNum = createdPost.getVirtualAccount().getBankAccount();
        System.out.println(virtualAccountNum);
        JLabel accountLabel = new JLabel("모금통장: " + virtualAccountNum);
        accountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        accountLabel.setFont(customFont.deriveFont(Font.BOLD, 21f));
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
        mainBtn.addActionListener(e -> dispose());

        JPanel footer = new JPanel(new BorderLayout());
        footer.setBorder(new EmptyBorder(20, 20, 20, 20));
        footer.setOpaque(false); // 바깥 회색 박스 없애기
        footer.add(mainBtn, BorderLayout.CENTER);

        //  전체 조립
        getContentPane().add(centerPanel, BorderLayout.CENTER);
        getContentPane().add(footer, BorderLayout.SOUTH);

        setVisible(true);
    }

    /*
    public static void main(String[] args) {
        // Create dummy User object
        // Based on the User constructor you provided:
        // public User(String userId, String password, String name, String nickName,
        //            String profileImg, BankType bankType, String bankAccount,
        //            int point, Tier tier)
        User dummyUser = new User(
                "testuser",              // userId
                "password123",           // password
                "테스트 사용자",           // name
                "테스트닉네임",            // nickName
                "profile.jpg",           // profileImg
                KB,        // bankType
                "123-4567-8901",         // bankAccount
                5000,                    // point
                BRONZE              // tier
        );

        // Create dummy controller objects (can be null or minimal mocks for UI testing)
        UserController dummyUserController = new UserController(null); // Pass null or a mock DAO if needed
        DonationPostController dummyDonationPostController = new DonationPostController(null); // Pass null or mock DAOs

        // Run the UI on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            new DonationPostCompleteView();
        });
    }
    */
}



