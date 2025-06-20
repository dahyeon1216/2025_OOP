package capstone.view.donation;

import capstone.controller.DonationPostController;
import capstone.controller.ScrapController;
import capstone.controller.UserController;
import capstone.model.DonationPost;
import capstone.model.User;
import capstone.view.BaseView;
import capstone.view.style.RoundedBorder;
import capstone.view.style.RoundedButton;
import capstone.view.user.MyPageMainView;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class DonationPostListView extends BaseView {
    private final User loginUser;
    private final UserController userController;
    private final DonationPostController donationPostController;
    private final ScrapController scrapController;
    private RoundedButton ongoingBtn;
    private RoundedButton completedBtn;
    private boolean showOngoing = true; // true: 진행중, false: 진행완료
    private JPanel postListPanel;


    public DonationPostListView(User loginUser,
                                UserController userController,
                                DonationPostController donationPostController,
                                ScrapController scrapController) {

        super("기부담다");

        this.loginUser = loginUser;
        this.userController = userController;
        this.donationPostController = donationPostController;
        this.scrapController = scrapController;

        JPanel header = createHeader("기부담다");
        addProfileIconToHeader(header);

        // 필터 버튼 패널
        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        filterPanel.setOpaque(false);

        // 진행중 버튼 생성 및 초기 색상 설정
        // 초기에는 '진행중'이 선택된 상태로 시작하도록 설정
        this.ongoingBtn = new RoundedButton("진행중", new Color(60, 60, 60), 30);
        ongoingBtn.setPreferredSize(new Dimension(140, 40));
        ongoingBtn.setFont(customFont.deriveFont(Font.BOLD, 20f));
        ongoingBtn.setForeground(Color.LIGHT_GRAY); // 초기 글씨색 흰색

        // 진행완료 버튼 생성 및 초기 색상 설정
        this.completedBtn = new RoundedButton("진행완료", Color.LIGHT_GRAY, 30);
        completedBtn.setPreferredSize(new Dimension(140, 40));
        completedBtn.setFont(customFont.deriveFont(Font.BOLD, 20f));
        completedBtn.setForeground(Color.DARK_GRAY); // 초기 글씨색 어두운 회색

        // 진행중 버튼 액션 리스너:
        ongoingBtn.addActionListener(e -> {
            setFilterButtonColors(ongoingBtn, completedBtn); // 현재 버튼 활성화, 다른 버튼 비활성화
            showOngoing = true;
            refresh();
        });

        // 진행완료 버튼 액션 리스너:
        completedBtn.addActionListener(e -> {
            setFilterButtonColors(completedBtn, ongoingBtn); // 현재 버튼 활성화, 다른 버튼 비활성화
            showOngoing = false;
            refresh();
        });

        filterPanel.add(ongoingBtn);
        filterPanel.add(completedBtn);

        // 고정 문구
        JLabel messageLbl = new JLabel("“ 당신의 손길이 큰 힘이 됩니다. ”", SwingConstants.CENTER);
        messageLbl.setFont(customFont.deriveFont(Font.BOLD, 20f));
        messageLbl.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        // 기부글 카드 리스트 패널
        postListPanel = new JPanel();
        postListPanel.setLayout(new BoxLayout( postListPanel, BoxLayout.Y_AXIS));
        postListPanel.setOpaque(false);

        // 기부글 쓰기 버튼 생성
        JButton writeBtn = new RoundedButton("기부글 쓰기", new Color(60, 60, 60), 30);
        writeBtn.setForeground(Color.WHITE);
        writeBtn.setPreferredSize(new Dimension(130, 45)); // 너비, 높이
        writeBtn.setFont(customFont.deriveFont(Font.BOLD, 22f));

        //기부글 쓰기 버튼 액션 리스너:
        writeBtn.addActionListener(e ->
        {
            //DonationPostWriteView 호출 시 현재 뷰 숨기기
            new DonationPostWriteView(loginUser, userController, donationPostController, this::refresh).setVisible(true);
        });

        JPanel writeBtnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 20));
        writeBtnPanel.setOpaque(false);
        writeBtnPanel.add(writeBtn);

        // 스크롤 생성
        JScrollPane scrollPane = new JScrollPane(postListPanel);
        scrollPane.setPreferredSize(new Dimension(360, 500));
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        // 전체 조립
        JPanel center = new JPanel();
        center.setLayout(new BorderLayout());
        center.setOpaque(false);
        center.add(filterPanel, BorderLayout.NORTH);
        center.add(messageLbl, BorderLayout.CENTER);

        JPanel body = new JPanel();
        body.setLayout(new BorderLayout());
        body.setOpaque(false);
        body.add(center, BorderLayout.NORTH);
        body.add(scrollPane, BorderLayout.CENTER);
        body.add(writeBtnPanel, BorderLayout.SOUTH);
        body.setComponentZOrder(writeBtnPanel, 0); // writeBtnPanel 자체를 제일 앞에

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(header, BorderLayout.NORTH);
        getContentPane().add(body, BorderLayout.CENTER);

        refresh();

        setVisible(true);
    }

    //개별 기부 게시물(DonationPost 객체)의 정보를 시각적으로 표현하는 메소드
    private JPanel createDonationCard(DonationPost post) {
        JPanel card = new JPanel(null);
        card.setMinimumSize(new Dimension(355, 135));
        card.setPreferredSize(new Dimension(355, 135));
        card.setMaximumSize(new Dimension(355, 120));
        card.setBackground(new Color(245, 245, 245));
        card.setBorder(BorderFactory.createEmptyBorder()); //테두리 제거
        card.setBorder(new RoundedBorder(20));
        card.setOpaque(true);

        // 1. D-Day 계산
        long dDay = ChronoUnit.DAYS.between(LocalDate.now(), post.getEndAt());
        String dDayText = dDay >= 0 ? "D-" + dDay : "D+" + Math.abs(dDay);
        JLabel dDayLabel = new JLabel(dDayText);
        dDayLabel.setBounds(20, 10, 50, 20);
        dDayLabel.setFont(customFont.deriveFont(Font.BOLD, 20f));
        card.add(dDayLabel);

        // 2. 이미지 (donationImg 경로)
        String imgPath = "resources/images/donation/" + post.getDonationImg();
        File imgFile = new File(imgPath);

        ImageIcon icon;
        if (imgFile.exists()) {
            icon = new ImageIcon(imgPath);
        } else {
            icon = new ImageIcon("icons/default_donation.png"); // 기본 이미지
        }
        Image scaledImage = icon.getImage().getScaledInstance(67, 67, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
        imageLabel.setBounds(20, 40, 67, 67);
        imageLabel.setBorder(new RoundedBorder(15));
        card.add(imageLabel);

        // 3. 제목
        String titleHtml = "<html><body style='width: 230px'>" + post.getTitle() + "</body></html>";
        JLabel titleLabel = new JLabel(titleHtml);
        titleLabel.setFont(customFont.deriveFont(Font.BOLD, 22f));
        int maxTitleHeight = 40;
        int titleHeight = Math.min(titleLabel.getPreferredSize().height, maxTitleHeight);
        titleLabel.setBounds(100, 45, 230, titleHeight);
        card.add(titleLabel);

        // 4. 진행률 계산 (raisedPoint / goalPoint)
        int goal = post.getGoalPoint();
        int current = post.getRaisedPoint();
        double rawPercent = goal > 0 ? ((double) current / goal) * 100 : 0;
        double cappedPercent = Math.min(100.0, rawPercent);
        String percentText = String.format("%.1f%%", cappedPercent);

        JLabel progressLabel = new JLabel("진행률 " + percentText + " (" + current + "P)");
        progressLabel.setFont(customFont.deriveFont(Font.PLAIN, 20f));
        int progressY = 45 + titleHeight + 5;
        progressLabel.setBounds(100, progressY, 250, 20);
        card.add(progressLabel);

        // 6. 스크랩 버튼
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


        card.add(scrapBtn);

        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) {
                    new DonationPostDetailView(post, loginUser, donationPostController, scrapController, DonationPostListView.this::refresh);
                }
            }
        });


        //전체 조립
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.setPreferredSize(new Dimension(355, 140));
        wrapper.setMaximumSize(new Dimension(355, 140));
        wrapper.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        wrapper.add(card, BorderLayout.CENTER);

        return wrapper;
    }


    //------추가적인 부가 메소드-------

    // 클릭 여부에 따라 색깔을 설정하는 새로운 메소드
    private void setFilterButtonColors(RoundedButton selectedBtn, RoundedButton unselectedBtn) {
        selectedBtn.setBackground(Color.BLACK);
        selectedBtn.setForeground(Color.WHITE);
        unselectedBtn.setBackground(Color.LIGHT_GRAY);
        unselectedBtn.setForeground(Color.DARK_GRAY);
    }

    //헤더에 사람 모양 아이콘 추가하는 메소드
    private void addProfileIconToHeader(JPanel header) {
        ImageIcon userIcon = new ImageIcon("icons/person.png");
        Image scaled = userIcon.getImage().getScaledInstance(27, 27, Image.SCALE_SMOOTH);
        JButton profileBtn = new JButton(new ImageIcon(scaled));
        profileBtn.setBounds(347, 7, 40, 30);
        profileBtn.setBorderPainted(false);
        profileBtn.setContentAreaFilled(false);
        profileBtn.setFocusPainted(false);

        profileBtn.addActionListener(e -> {
            MyPageMainView myPageFrame = new MyPageMainView(loginUser, userController, donationPostController, scrapController);
            myPageFrame.setVisible(true);
        });

        header.add(profileBtn);
    }


    public void refresh() {
        postListPanel.removeAll();

        Runnable onPostUpdated = this::refresh;

        List<DonationPost> showPosts = donationPostController.getAllPosts().stream()
                .filter(post -> {
                    if (showOngoing) {
                        return !post.getEndAt().isBefore(LocalDate.now()); // 오늘 이후: 진행중
                    } else {
                        return post.getEndAt().isBefore(LocalDate.now()); // 오늘 이전: 완료됨
                    }
                })
                .toList();


        for (DonationPost post : showPosts) {
            JPanel card = createDonationCard(post); // 직접 카드 생성
            postListPanel.add(card);
        }

        //postListPanel.add(listPanel, BorderLayout.CENTER);
        postListPanel.revalidate();
        postListPanel.repaint();


    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // 1. 더미 User 생성
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

            // 2. 더미 Service 및 Controller 생성
            capstone.service.DonationPostService dummyService = new capstone.service.DonationPostService();
            DonationPostController dummyDonationPostController = new DonationPostController(dummyService);

            // 3. 가상의 테스트용 기부글 몇 개 등록
            dummyService.create(dummyUser, "donationImg1.jpg", 1000, LocalDate.now().plusDays(10), "강아지 보호소", "유기견 보호소 후원합니다");
            dummyService.create(dummyUser, "donationImg2.jpg", 2000, LocalDate.now().minusDays(5), "아동 교육 후원", "저소득층 아동을 위한 교육 지원");

            // 4. 더미 UserController 및 ScrapController
            UserController dummyUserController = new UserController(null);
            ScrapController dummyScrapController = new ScrapController(null);

            // 5. View 실행
            new DonationPostListView(dummyUser, dummyUserController, dummyDonationPostController, dummyScrapController).setVisible(true);
        });
    }

}
