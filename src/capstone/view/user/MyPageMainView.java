package capstone.view.user;

import capstone.controller.DonationPostController;
import capstone.controller.ScrapController;
import capstone.controller.UserController;
import capstone.model.BankType;
import capstone.model.Tier;
import capstone.model.User;
import capstone.service.DonationPostService;
import capstone.view.BaseView;
import capstone.view.scrap.ScrappedPostListView;
import capstone.view.style.RoundedBorder;
import capstone.view.style.RoundedButton;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MyPageMainView extends BaseView {

    private final User loginUser;
    private final UserController userController;
    private final DonationPostController donationPostController;
    private final ScrapController scrapController;
    private JLabel amountLabel;

    public MyPageMainView(User loginUser,
                          UserController userController,
                          DonationPostController donationPostController,
                          ScrapController scrapController) {

        super("마이페이지");

        this.loginUser = loginUser;
        this.userController = userController;
        this.donationPostController = donationPostController;
        this.scrapController = scrapController;

        JPanel header = createHeader("마이페이지");

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.white);
        mainPanel.setBorder(new EmptyBorder(10, 20, 20, 20));

        add(header,BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);


        ImageIcon backIcon = new ImageIcon("icons/arrow-leftb.png");
        Image scaledImg = backIcon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(scaledImg);

        JPanel topProfilePanel = new JPanel(null);
        topProfilePanel.setPreferredSize(new Dimension(393, 100));
        topProfilePanel.setMaximumSize(new Dimension(393, 100));
        topProfilePanel.setBackground(Color.WHITE);
        topProfilePanel.setOpaque(false);

        //이미지
        JLabel imageLabel = new JLabel();
        imageLabel.setBounds(10, 15, 70, 70);
        ImageIcon roundedIcon = getRoundedImageIcon("resources/images/profile/"+loginUser.getProfileImg(),70);
        if (roundedIcon != null) {
            imageLabel.setIcon(roundedIcon);
        } else {
            // 이미지 파일을 못 읽었을 경우 fallback 색상 적용
            imageLabel.setOpaque(true);
            imageLabel.setBackground(Color.LIGHT_GRAY);
        }

        // + 버튼
        ImageIcon plusIcon = new ImageIcon("icons/plus-icon.png");
        Image scaledPlus = plusIcon.getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH);
        JButton addImgBtn = new JButton(new ImageIcon(scaledPlus));
        addImgBtn.setBounds(63, 63, 20, 20);
        addImgBtn.setFocusPainted(false);
        addImgBtn.setContentAreaFilled(false);
        addImgBtn.setBorderPainted(false);

        addImgBtn.addActionListener(e -> {
            // UserProfileEditView
        });


        // 닉네임
        JLabel nicknameLabel = new JLabel(loginUser.getNickName());
        nicknameLabel.setFont(customFont.deriveFont(Font.BOLD, 22f));
        nicknameLabel.setSize(new Dimension(170, nicknameLabel.getPreferredSize().height));
        nicknameLabel.setLocation(90, 32);

        // 티어
        JLabel tierLabel = new JLabel(loginUser.getTier());
        tierLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));
        tierLabel.setForeground(new Color(255, 170, 0));
        tierLabel.setBounds(267, 38, 200, 20);

        topProfilePanel.add(addImgBtn);
        topProfilePanel.add(imageLabel);
        topProfilePanel.add(nicknameLabel);
        topProfilePanel.add(tierLabel);


        // 보유 포인트 패널
        JPanel pointPanel = new JPanel(null);
        pointPanel.setBackground(new Color(235, 235, 235));
        pointPanel.setMinimumSize(new Dimension(360, 160));
        pointPanel.setMaximumSize(new Dimension(360, 160));
        pointPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        pointPanel.setBorder(new RoundedBorder(30));

        // 문구
        JLabel pointLabel = new JLabel("보유 포인트");
        pointLabel.setFont(customFont.deriveFont(Font.BOLD, 22f));
        pointLabel.setBounds(30, 25, 120, 30); // x, y, w, h

        // 구분선
        JSeparator separator = new JSeparator();
        separator.setBounds(30, 75, 295, 10);
        separator.setForeground(new Color(179, 179, 179));

        amountLabel = new JLabel(String.format("%,d P", loginUser.getPoint()));
        amountLabel.setFont(customFont.deriveFont(Font.BOLD, 23f));

        int panelWidth = 360;  // pointPanel의 폭
        int rightMargin = 50;
        int labelWidth = amountLabel.getPreferredSize().width;
        amountLabel.setBounds(panelWidth - rightMargin - labelWidth, 30, 120, 30);

        // 충전버튼
        JButton chargeButton = new RoundedButton("충전", new Color(60, 60, 60), 30);
        chargeButton.setFont(customFont.deriveFont(Font.BOLD, 18f));
        chargeButton.setBackground(Color.BLACK);
        chargeButton.setForeground(Color.WHITE);
        chargeButton.setBounds(230, 100, 85, 40);
        chargeButton.addActionListener(e -> {
            PointChargeView pointChargeView = new PointChargeView(loginUser, ()-> {refreshPoint();});
            pointChargeView.setVisible(true);
        });

        pointPanel.add(pointLabel);
        pointPanel.add(separator);
        pointPanel.add(amountLabel);
        pointPanel.add(chargeButton);


        // 나의 거래 패널
        JPanel transactionPanel = new JPanel(null);
        transactionPanel.setBackground(new Color(235, 235, 235));
        transactionPanel.setMinimumSize(new Dimension(360, 300));
        transactionPanel.setMaximumSize(new Dimension(360, 300));
        transactionPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        transactionPanel.setBorder(new RoundedBorder(30));

        // 나의 거래 라벨
        JLabel dealLabel = new JLabel("나의 거래");
        dealLabel.setFont(customFont.deriveFont(Font.BOLD, 22f));
        dealLabel.setBounds(30, 25, 120, 30); // x, y, w, h
        transactionPanel.add(dealLabel);

        // 구분선
        JSeparator separator0 = new JSeparator();
        separator0.setBounds(30, 75, 295, 10);
        separator0.setForeground(new Color(179, 179, 179));
        transactionPanel.add(separator0);

        // 아이콘 준비
        ImageIcon arrowIcon = new ImageIcon("icons/gogo.png");
        Image scaledArrow = arrowIcon.getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH);
        ImageIcon finalArrowIcon = new ImageIcon(scaledArrow);


        // 나의 기부글
        JPanel labelPanel = new JPanel(new BorderLayout());
        labelPanel.setOpaque(false);  // 배경 투명
        labelPanel.setBounds(35, 95, 280, 30); // 크기 조정 (조금 넉넉히)

        ImageIcon leftIcon = new ImageIcon("icons/Vector.png");
        JLabel donationLabel = new JLabel(" 나의 기부글", leftIcon, JLabel.LEFT);
        donationLabel.setFont(customFont.deriveFont(Font.BOLD, 20f));
        transactionPanel.add(donationLabel);

        // 왼쪽 아이콘 + 텍스트 JLabel은 CENTER에
        labelPanel.add(donationLabel, BorderLayout.CENTER);

        // 오른쪽 아이콘은 별도 JLabel로 추가
        JLabel rightIconLabel = new JLabel(finalArrowIcon);
        rightIconLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // 새 화면 띄우기
                MyDonationPostListView myDonationPostListView = new MyDonationPostListView(
                        loginUser, donationPostController, scrapController
                );
                myDonationPostListView.setVisible(true);
            }
        });
        labelPanel.add(rightIconLabel, BorderLayout.EAST);

        // 패널을 transactionPanel에 추가
        transactionPanel.add(labelPanel);


        // 구분선
        JSeparator separator1 = new JSeparator();
        separator1.setBounds(30, 140, 295, 10);
        separator1.setForeground(new Color(179, 179, 179));
        transactionPanel.add(separator1);


        // 스크랩 목록
        JPanel labelPanel2 = new JPanel(new BorderLayout());
        labelPanel2.setOpaque(false);  // 배경 투명
        labelPanel2.setBounds(30, 160, 285, 30); // 크기 조정 (조금 넉넉히)

        ImageIcon leftIcon2 = new ImageIcon("icons/bookmark.png");
        JLabel scrapLabel = new JLabel(" 스크랩 목록", leftIcon2, JLabel.LEFT);
        scrapLabel.setFont(customFont.deriveFont(Font.BOLD, 20f));
        transactionPanel.add(scrapLabel);

        // 왼쪽 아이콘 + 텍스트 JLabel은 CENTER에
        labelPanel2.add(scrapLabel, BorderLayout.CENTER);

        // 오른쪽 아이콘은 별도 JLabel로 추가
        JLabel rightIconLabel2 = new JLabel(finalArrowIcon);
        rightIconLabel2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // 새 화면 띄우기
                ScrappedPostListView scrappedPostListView = new ScrappedPostListView(
                        loginUser, donationPostController, scrapController
                );
                scrappedPostListView.setVisible(true);
            }
        });
        labelPanel2.add(rightIconLabel2, BorderLayout.EAST);

        // 패널을 transactionPanel에 추가
        transactionPanel.add(labelPanel2);


        // 구분선
        JSeparator separator3 = new JSeparator();
        separator3.setBounds(30, 205, 295, 10);
        separator3.setForeground(new Color(179, 179, 179));
        transactionPanel.add(separator3);


        // 기부 내역
        JPanel labelPanel3 = new JPanel(new BorderLayout());
        labelPanel3.setOpaque(false);  // 배경 투명
        labelPanel3.setBounds(37, 225, 278, 30); // 크기 조정 (조금 넉넉히)

        ImageIcon leftIcon3 = new ImageIcon("icons/donationhistory.png");
        JLabel historyLabel = new JLabel("  기부 내역", leftIcon3, JLabel.LEFT);
        historyLabel.setFont(customFont.deriveFont(Font.BOLD, 20f));
        transactionPanel.add(historyLabel);

        // 왼쪽 아이콘 + 텍스트 JLabel은 CENTER에
        labelPanel3.add(historyLabel, BorderLayout.CENTER);

        // 오른쪽 아이콘은 별도 JLabel로 추가
        JLabel rightIconLabel3 = new JLabel(finalArrowIcon);
        rightIconLabel3.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // 새 화면 띄우기
                DonationHistoryView donationHistoryView = new DonationHistoryView(
                        loginUser, donationPostController, scrapController
                );
                donationHistoryView.setVisible(true);
            }
        });
        labelPanel3.add(rightIconLabel3, BorderLayout.EAST);

        // 패널을 transactionPanel에 추가
        transactionPanel.add(labelPanel3);


        // 구분선
        JSeparator separator4 = new JSeparator();
        separator4.setBounds(30, 270, 295, 10);
        separator4.setForeground(new Color(179, 179, 179));
        transactionPanel.add(separator4);


        // 조립
        mainPanel.add(Box.createVerticalStrut(0));
        mainPanel.add(topProfilePanel);
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(pointPanel);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(transactionPanel);
        add(mainPanel);

        this.addWindowFocusListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowGainedFocus(java.awt.event.WindowEvent e) {
                refreshPoint();
            }
        });
    }

    //이미지 둥글게 하는 코드
    private ImageIcon getRoundedImageIcon(String imagePath, int diameter) {
        try {
            BufferedImage originalImage = ImageIO.read(new File(imagePath));
            int imgWidth = originalImage.getWidth();
            int imgHeight = originalImage.getHeight();

            // Center Crop: 이미지 비율 무시하고 꽉 차게 확대
            float scale = Math.max((float) diameter / imgWidth, (float) diameter / imgHeight);
            int scaledWidth = Math.round(imgWidth * scale);
            int scaledHeight = Math.round(imgHeight * scale);

            Image scaledImage = originalImage.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);

            // 원형 마스킹용 버퍼 생성
            BufferedImage rounded = new BufferedImage(diameter, diameter, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = rounded.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // 자른 이미지의 중심을 원에 맞춤
            int x = (scaledWidth - diameter) / 2;
            int y = (scaledHeight - diameter) / 2;

            // 원형 클리핑
            g2.setClip(new Ellipse2D.Float(0, 0, diameter, diameter));
            g2.drawImage(scaledImage, -x, -y, null);
            g2.dispose();

            return new ImageIcon(rounded);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void refreshPoint() {
        amountLabel.setText(String.format("%,d P", loginUser.getPoint()));

        int panelWidth = 360;
        int rightMargin = 50;
        int labelWidth = amountLabel.getPreferredSize().width;
        amountLabel.setBounds(panelWidth - rightMargin - labelWidth, 30, labelWidth, 30);
    }

    //테스트 main
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            User testUser = new User();
            testUser.setProfileImg("1749917905234_IMG_A236BCEC2250-1.jpeg");
            testUser.setUserId("test01");
            testUser.setName("홍길동");
            testUser.setNickName("기부천사");
            testUser.setBankType(BankType.KB);
            testUser.setBankAccount("123-456-789");
            testUser.setTier(Tier.BRONZE);
            testUser.setPoint(10000);

            class DummyUserController extends UserController {
                public DummyUserController() {
                    super(null);
                }

                @Override
                public User getUserProfile(User user) {
                    return user;
                }
            }

            MyPageMainView view = new MyPageMainView(testUser, new DummyUserController(), new DonationPostController(null), new ScrapController(null));
            view.setVisible(true);
        });
    }

}

