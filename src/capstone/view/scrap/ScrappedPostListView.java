package capstone.view.scrap;

import capstone.controller.DonationPostController;
import capstone.controller.ScrapController;
import capstone.dto.DonatedPostInfo;
import capstone.model.BankType;
import capstone.model.DonationPost;
import capstone.model.Tier;
import capstone.model.User;
import capstone.service.DonationPostService;
import capstone.service.ScrapService;
import capstone.view.BaseView;
import capstone.view.donation.DonationPostDetailView;
import capstone.view.style.RoundedBorder;
import capstone.view.style.RoundedButton;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.time.LocalDate;
import java.util.List;

public class ScrappedPostListView extends BaseView {

    private final User loginUser;
    private final DonationPostController donationPostController;
    private final ScrapController scrapController;
    private JPanel postListPanel;

    public ScrappedPostListView(User loginUser,
                                DonationPostController donationPostController,
                                ScrapController scrapController) {
        super("스크랩 내역");

        this.loginUser = loginUser;
        this.donationPostController = donationPostController;
        this.scrapController = scrapController;

        JPanel header = createHeader("스크랩 내역");

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

    private JPanel createDonationCard(DonationPost post) {

        JPanel card = new JPanel(null);
        card.setMinimumSize(new Dimension(355, 145));
        card.setPreferredSize(new Dimension(355, 145));
        card.setMaximumSize(new Dimension(355, 145));
        card.setBackground(new Color(245, 245, 245));
        card.setBorder(new RoundedBorder(20));
        card.setOpaque(true);

        // 제목
        String titleHtml = "<html><body style='width: 230px'>" + post.getTitle() + "</body></html>";
        JLabel titleLabel = new JLabel(titleHtml);
        titleLabel.setFont(customFont.deriveFont(Font.BOLD, 20f));
        titleLabel.setBounds(17, 10, 230, 25);
        card.add(titleLabel);

        // 스크랩 버튼
        JButton scrapBtn = new JButton(new ImageIcon("icons/bookmark.png"));
        scrapBtn.setBounds(310, 10, 35, 35);
        scrapBtn.setContentAreaFilled(false);
        scrapBtn.setBorderPainted(false);
        card.add(scrapBtn);

        scrapBtn.addActionListener(e -> {
            scrapController.toggleScrap(loginUser, post);

            boolean isScrapped = scrapController.isScrapped(loginUser, post);
            scrapBtn.setText(isScrapped ? "스크랩 취소" : "스크랩");

            if (isScrapped) {
                JOptionPane.showMessageDialog(
                        null,
                        "스크랩이 추가되었습니다.",
                        "스크랩 완료",
                        JOptionPane.INFORMATION_MESSAGE
                );
            } else {
                JOptionPane.showMessageDialog(
                        null,
                        "스크랩이 해제되었습니다.",
                        "스크랩 해제",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }

            refresh();
        });

        // 이미지
        String imgPath = "resources/images/donation/" + post.getDonationImg();
        File imgFile = new File(imgPath);
        ImageIcon icon = imgFile.exists() ? new ImageIcon(imgPath) : new ImageIcon("icons/image-fail.png");
        Image scaledImage = icon.getImage().getScaledInstance(67, 67, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
        imageLabel.setBounds(15, 45, 67, 67);
        imageLabel.setBorder(new RoundedBorder(15));
        card.add(imageLabel);

        // 내용 일부 표시 (30자 제한)
        String content = post.getContent().length() > 80 ? post.getContent().substring(0, 80) + "..." : post.getContent();
        String contentHtml = "<html><body style='width:185px'>" + content + "</body></html>";
        JLabel contentLabel = new JLabel(contentHtml);
        contentLabel.setFont(customFont.deriveFont(Font.PLAIN, 17f));
        contentLabel.setBounds(97, 45, 240, 60);
        contentLabel.setVerticalAlignment(SwingConstants.TOP);
        card.add(contentLabel);

        // 상세보기 버튼 (스크랩은 기부액 표시 대신 상세보기만 유지)
        JButton detailBtn = new RoundedButton("상세보기", new Color(60, 60, 60), 20);
        detailBtn.setForeground(Color.WHITE);
        detailBtn.setFont(customFont.deriveFont(Font.BOLD, 16f));
        detailBtn.setBounds(245, 120, 90, 30);
        detailBtn.addActionListener(e -> {
            new DonationPostDetailView(post, loginUser, donationPostController, scrapController, this::refresh).setVisible(true);
        });
        card.add(detailBtn);

        // 진행률 및 모금액 표시 추가

        int goal = post.getGoalPoint();
        int current = post.getRaisedPoint();
        double rawPercent = goal > 0 ? ((double) current / goal) * 100 : 0;
        double cappedPercent = Math.min(100.0, rawPercent);
        String percentText = String.format("%.1f%%", cappedPercent);
        JLabel progressLabel = new JLabel("진행률 " + percentText + " (" + current + "P)");
        progressLabel.setFont(customFont.deriveFont(Font.PLAIN, 20f));
        progressLabel.setBounds(20, 121, 150, 20);
        card.add(progressLabel);

        // 전체 wrapper (패딩용)
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.setPreferredSize(new Dimension(355, 180));
        wrapper.setMaximumSize(new Dimension(355, 180));
        wrapper.setBorder(BorderFactory.createEmptyBorder(15, 1, 0, 0));
        wrapper.setAlignmentX(Component.CENTER_ALIGNMENT);
        wrapper.add(card, BorderLayout.CENTER);

        return wrapper;
    }

    public void refresh() {
        postListPanel.removeAll();

        List<DonationPost> posts = scrapController.getScrappedPosts(loginUser);

        for (DonationPost post : posts) {
            JPanel card = createDonationCard(post);
            postListPanel.add(card);
        }

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

            // 더미 DonationPostController (사실 스크랩 화면에서는 없어도 됨)
            DonationPostController dummyDonationPostController = new DonationPostController(new DonationPostService()) {
                @Override
                public List<DonatedPostInfo> getDonatedPostInfos(User user) {
                    return List.of(); // 빈 리스트 반환 (기부내역은 사용 안하므로)
                }
            };

            // 더미 ScrapController : 여기서 스크랩된 게시물 3개 생성
            ScrapController dummyScrapController = new ScrapController(new ScrapService()) {
                @Override
                public List<DonationPost> getScrappedPosts(User user) {
                    DonationPost post1 = new DonationPost(
                            dummyUser,
                            "donationImg.jpg",
                            1000,
                            LocalDate.now().plusDays(5),
                            "유기견 보호소 지원",
                            "유기가가가가가가가가가가가견 보호소에 사료와 치료비를 지원합니다. 유기견 보호소에 사료와 치료비를 지원합니다. 유기견 보호소에 사료와 치료비를 지원합니다."
                    );

                    DonationPost post2 = new DonationPost(
                            dummyUser,
                            "donationImg2.jpg",
                            3000,
                            LocalDate.now().plusDays(12),
                            "아동 교육 프로그램",
                            "저소득층 아이들에게 교육 기회를 제공합니다."
                    );

                    DonationPost post3 = new DonationPost(
                            dummyUser,
                            "donationImg3.jpg",
                            5000,
                            LocalDate.now().plusDays(20),
                            "환경 보호 캠페인",
                            "플라스틱 줄이기 운동을 함께 해주세요."
                    );

                    DonationPost post4 = new DonationPost(
                            dummyUser,
                            "donationImg3.jpg",
                            5000,
                            LocalDate.now().plusDays(20),
                            "환경 보호 캠페인",
                            "플라스틱 줄이기 운동을 함께 해주세요."
                    );

                    return List.of(post1, post2, post3, post4);
                }
            };

            // 테스트 실행
            new ScrappedPostListView(dummyUser, dummyDonationPostController, dummyScrapController);
        });
    }
}
