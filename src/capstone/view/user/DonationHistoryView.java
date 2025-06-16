package capstone.view.user;

import capstone.controller.UserController;
import capstone.dto.DonatedPostInfo;
import capstone.model.*;
import capstone.controller.DonationPostController;
import capstone.controller.ScrapController;
import capstone.service.DonationPostService;
import capstone.service.ScrapService;
import capstone.service.UserService;
import capstone.view.BaseView;
import capstone.view.donation.DonationPostDetailView;
import capstone.view.style.RoundedBorder;
import capstone.view.style.RoundedButton;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class DonationHistoryView extends BaseView {

    private final User loginUser;
    private final DonationPostController donationPostController;
    private final ScrapController scrapController;
    private JPanel postListPanel;

    public DonationHistoryView(User loginUser,
                               DonationPostController donationPostController,
                               ScrapController scrapController){
        super("기부 내역");

        this.loginUser = loginUser;
        this.donationPostController = donationPostController;
        this.scrapController = scrapController;

        JPanel header = createHeader("기부 내역");

        // 기부글 카드 리스트 패널
        postListPanel = new JPanel();
        postListPanel.setLayout(new BoxLayout(postListPanel, BoxLayout.Y_AXIS));
        postListPanel.setOpaque(false);

        // 스크롤 적용
        JScrollPane scrollPane = new JScrollPane(postListPanel);
        scrollPane.setPreferredSize(new Dimension(360, 500));
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        JPanel center = new JPanel();
        center.setLayout(new BorderLayout());
        center.setOpaque(false);

        JPanel body = new JPanel();
        body.setLayout(new BorderLayout());
        body.setOpaque(false);
        body.add(center, BorderLayout.NORTH);
        body.add(scrollPane, BorderLayout.CENTER);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(header, BorderLayout.NORTH);
        getContentPane().add(body, BorderLayout.CENTER);

        refresh();

        setVisible(true);
    }

    private JPanel createDonationCard(DonatedPostInfo info) {
        DonationPost post = info.getPost();

        JPanel card = new JPanel(null);
        card.setMinimumSize(new Dimension(355, 145));
        card.setPreferredSize(new Dimension(355, 145));
        card.setMaximumSize(new Dimension(355, 145));
        card.setBackground(new Color(245, 245, 245));
        card.setBorder(new RoundedBorder(20));
        card.setOpaque(true);

        // 이미지
        String imgPath = "resources/images/donation/" + post.getDonationImg();
        File imgFile = new File(imgPath);
        ImageIcon icon = imgFile.exists() ? new ImageIcon(imgPath) : new ImageIcon("icons/image-fail.png");
        Image scaledImage = icon.getImage().getScaledInstance(67, 67, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
        imageLabel.setBounds(15, 15, 67, 67);
        imageLabel.setBorder(new RoundedBorder(15));
        card.add(imageLabel);

        // 제목
        String titleHtml = "<html><body style='width: 230px'>" + post.getTitle() + "</body></html>";
        JLabel titleLabel = new JLabel(titleHtml);
        titleLabel.setFont(customFont.deriveFont(Font.BOLD, 21f));
        titleLabel.setBounds(97, 18, 230, 25);
        card.add(titleLabel);

        // 내용 일부 표시 (30자 제한)
        String content = post.getContent().length() > 30 ? post.getContent().substring(0, 30) + "..." : post.getContent();
        JLabel contentLabel = new JLabel(content);
        contentLabel.setFont(customFont.deriveFont(Font.PLAIN, 18f));
        contentLabel.setBounds(97, 48, 240, 20);
        card.add(contentLabel);

        // 기부액 표시
        JLabel donatedLabel = new JLabel("나의 기부액: " + info.getTotalDonatedPoint() + "P");
        donatedLabel.setFont(customFont.deriveFont(Font.BOLD, 18f));
        donatedLabel.setBounds(20, 93, 250, 25);
        card.add(donatedLabel);

        // 상세보기 버튼
        JButton detailBtn = new RoundedButton("상세보기", new Color(60, 60, 60), 20);
        detailBtn.setForeground(Color.WHITE);
        detailBtn.setFont(customFont.deriveFont(Font.BOLD, 16f));
        detailBtn.setBounds(245, 90, 90, 30);
        detailBtn.addActionListener(e -> {
            new DonationPostDetailView(post, loginUser, donationPostController, scrapController, this::refresh).setVisible(true);
        });
        card.add(detailBtn);

        // 전체 wrapper (패딩용)
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.setPreferredSize(new Dimension(355, 150));
        wrapper.setMaximumSize(new Dimension(355, 150));
        wrapper.setBorder(BorderFactory.createEmptyBorder(15, 1, 0, 0));
        wrapper.add(card, BorderLayout.CENTER);

        return wrapper;
    }

    public void refresh() {
        postListPanel.removeAll();

        Runnable onPostUpdated = this::refresh;

        List<DonatedPostInfo> infos = donationPostController.getDonatedPostInfos(loginUser);

        for (DonatedPostInfo info : infos) {
            JPanel card = createDonationCard(info);
            postListPanel.add(card);
        }

        // 하단 여백 추가 (스크롤 맨 아래 공간 확보)
        postListPanel.add(Box.createVerticalStrut(15));

        postListPanel.revalidate();
        postListPanel.repaint();
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // 더미 User 생성
            BankType bankType = BankType.KB;
            Tier tier = Tier.SILVER;

            User dummyUser = new User(
                    "testuser",
                    "password123",
                    "테스트 사용자",
                    "테스트닉네임",
                    "profile.jpg",
                    bankType,
                    "123-4567-8901",
                    10000,
                    tier
            );

            // 더미 DonationPostController 생성
            DonationPostController dummyDonationPostController = new DonationPostController(new DonationPostService()) {
                @Override
                public List<DonatedPostInfo> getDonatedPostInfos(User user) {
                    // DonationPost 생성자 순서: (User writer, String donationImg, int goalPoint, LocalDate endAt, String title, String content)
                    DonationPost post1 = new DonationPost(
                            dummyUser,
                            "donationImg.jpg",
                            1000,
                            LocalDate.now().plusDays(10),
                            "강아지 구조 프로젝트",
                            "버려진 강아지들을 보호합니다 보호보호보호보호 버려진 강아지들을 보호합니다 보호보호보호보호 버려진 강아지들을 보호합니다 보호보호보호보호 버려진 강아지들을 보호합니다 보호보호보호보호"
                    );

                    DonationPost post2 = new DonationPost(
                            dummyUser,
                            "donationImg2.jpg",
                            2000,
                            LocalDate.now().plusDays(20),
                            "아동 교육 지원",
                            "어려운 아동에게 장학금을 제공합니다"
                    );

                    // DonatedPostInfo 생성자 반영: (DonationPost post, int totalDonatedPoint, LocalDateTime donatedAt)
                    DonatedPostInfo info1 = new DonatedPostInfo(
                            post1,
                            300,
                            LocalDateTime.now().minusDays(2)  // 2일 전에 기부했다고 가정
                    );

                    DonatedPostInfo info2 = new DonatedPostInfo(
                            post2,
                            150,
                            LocalDateTime.now().minusDays(5)  // 5일 전에 기부했다고 가정
                    );

                    DonatedPostInfo info3 = new DonatedPostInfo(
                            post2,
                            150,
                            LocalDateTime.now().minusDays(5)  // 5일 전에 기부했다고 가정
                    );

                    DonatedPostInfo info4 = new DonatedPostInfo(
                            post2,
                            150,
                            LocalDateTime.now().minusDays(5)  // 5일 전에 기부했다고 가정
                    );

                    DonatedPostInfo info5 = new DonatedPostInfo(
                            post2,
                            150,
                            LocalDateTime.now().minusDays(5)  // 5일 전에 기부했다고 가정
                    );

                    return List.of(info1, info2,info3,info4,info5);
                }
            };

            ScrapController dummyScrapController = null;

            DonationHistoryView view = new DonationHistoryView(
                    dummyUser,
                    dummyDonationPostController,
                    dummyScrapController
            );

            view.setVisible(true);
        });
    }

}