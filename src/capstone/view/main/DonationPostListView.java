package capstone.view.main;

import capstone.controller.DonationPostController;
import capstone.controller.UserController;
import capstone.model.BankType;
import capstone.model.DonationPost;
import capstone.model.User;
import capstone.service.DonationPostService;
import capstone.service.UserService;
import capstone.view.BaseView;
import capstone.view.Roundborder.RoundedBorder;
import capstone.view.Roundborder.RoundedButton;
import capstone.view.donation.DonationPostDetailView;
import capstone.view.donation.DonationPostWriteView;
import capstone.view.user.UserProfileView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class DonationPostListView extends BaseView {

    private JPanel cardListPanel;
    private JButton ongoingBtn;
    private JButton completedBtn;
    private boolean isOngoing = true;

    private final User loginUser;
    private final DonationPostController controller;


    public DonationPostListView(User loginUser, DonationPostController controller, JFrame previousView) {
        super("기부담다", previousView);

        this.loginUser = loginUser;
        this.controller = controller;

        JPanel header = createHeader("기부담다");
        addProfileIconToHeader(header);

        // 필터 버튼 패널
        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10)); // 간격 넓힘
        filterPanel.setOpaque(false);

        // 진행중 버튼
        ongoingBtn = new RoundedButton("진행중", isOngoing ? Color.BLACK : Color.LIGHT_GRAY, 30);
        ongoingBtn.setPreferredSize(new Dimension(140, 40));
        ongoingBtn.setFont(customFont.deriveFont(Font.BOLD, 20f));
        ongoingBtn.setForeground(isOngoing ? Color.WHITE : Color.DARK_GRAY);
        ongoingBtn.addActionListener(e -> {
            isOngoing = true;
            updateFilterButtonStyle();
            refreshCardList();
        });

        // 진행완료 버튼
        completedBtn = new RoundedButton("진행완료", !isOngoing ? Color.BLACK : Color.LIGHT_GRAY, 30);
        completedBtn.setPreferredSize(new Dimension(140, 40));
        completedBtn.setFont(customFont.deriveFont(Font.BOLD, 20f));
        completedBtn.setForeground(!isOngoing ? Color.WHITE : Color.DARK_GRAY);
        completedBtn.addActionListener(e -> {
            isOngoing = false;
            updateFilterButtonStyle();
            refreshCardList();
        });

        filterPanel.add(ongoingBtn);
        filterPanel.add(completedBtn);

        // 고정 문구
        JLabel messageLbl = new JLabel("“ 당신의 손길이 큰 힘이 됩니다. ”", SwingConstants.CENTER);
        messageLbl.setFont(customFont.deriveFont(Font.BOLD, 20f));
        messageLbl.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        // 기부글 카드 리스트 패널
        cardListPanel = new JPanel();
        cardListPanel.setLayout(new BoxLayout(cardListPanel, BoxLayout.Y_AXIS));
        cardListPanel.setOpaque(false);

        // 스크롤 생성
        JScrollPane scrollPane = new JScrollPane(cardListPanel);
        scrollPane.setPreferredSize(new Dimension(360, 500));
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        // 기부글 쓰기 버튼
        JButton writeBtn = new RoundedButton("기부글 쓰기", Color.BLACK, 30);
        writeBtn.setForeground(Color.WHITE);
        writeBtn.setPreferredSize(new Dimension(130, 45)); // 너비, 높이
        writeBtn.setFont(customFont.deriveFont(Font.BOLD, 22f));
        writeBtn.addActionListener(e -> new DonationPostWriteView(this, null, null)); // controller는 예시로 null

        JPanel writeBtnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 20)); // 오른쪽 정렬, 여백 약간
        writeBtnPanel.setOpaque(false);
        writeBtnPanel.add(writeBtn);

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

        refreshCardList();

        setVisible(true);
    }

    private JButton createFilterButton(String text, boolean selected) {
        JButton button = new JButton(text);
        button.setFont(customFont.deriveFont(Font.BOLD, 15f));
        button.setPreferredSize(new Dimension(120, 35));
        button.setFocusPainted(false);
        button.setBackground(selected ? Color.BLACK : Color.LIGHT_GRAY);
        button.setForeground(selected ? Color.WHITE : Color.DARK_GRAY);
        button.addActionListener((ActionEvent e) -> {
            isOngoing = text.equals("진행중");
            updateFilterButtonStyle();
            refreshCardList();
        });
        return button;
    }

    //상태에 따라 진행 상태 버튼 색깔 달라지는 메소드
    private void updateFilterButtonStyle() {
        ongoingBtn.setBackground(isOngoing ? Color.BLACK : Color.LIGHT_GRAY);
        ongoingBtn.setForeground(isOngoing ? Color.WHITE : Color.DARK_GRAY);
        completedBtn.setBackground(isOngoing ? Color.LIGHT_GRAY : Color.BLACK);
        completedBtn.setForeground(isOngoing ? Color.DARK_GRAY : Color.WHITE);
    }


    private void addProfileIconToHeader(JPanel header) {
        ImageIcon userIcon = new ImageIcon("icons/person.png");
        Image scaled = userIcon.getImage().getScaledInstance(27, 27, Image.SCALE_SMOOTH);
        JButton profileBtn = new JButton(new ImageIcon(scaled));
        profileBtn.setBounds(335, 6, 40, 30);
        profileBtn.setBorderPainted(false);
        profileBtn.setContentAreaFilled(false);
        profileBtn.setFocusPainted(false);

        // UserService와 UserController 직접 생성
        UserService userService = new UserService();
        UserController profileController = new UserController(userService);


        profileBtn.addActionListener(e -> {
            new UserProfileView(loginUser, profileController).setVisible(true);
        });

        header.add(profileBtn);
    }

    private void refreshCardList() {
        cardListPanel.removeAll();

         List<DonationPost> posts = isOngoing
                ? controller.getOngoingPosts()
                : controller.getCompletedPosts();

        for (DonationPost post : posts) {
            cardListPanel.add(createDonationCard(post));
            cardListPanel.add(Box.createVerticalStrut(10));
        }

        cardListPanel.revalidate();
        cardListPanel.repaint();
    }

    private JPanel createDonationCard(DonationPost post) {
        JPanel card = new JPanel(null);
        card.setMinimumSize(new Dimension(355, 145));
        card.setPreferredSize(new Dimension(355, 145));
        card.setMaximumSize(new Dimension(355, 145));
        card.setBackground(new Color(245, 245, 245));
        card.setBorder(BorderFactory.createEmptyBorder()); //테두리 제거
        card.setBorder(new RoundedBorder(20));

        card.setOpaque(true);

        // 1. D-Day 계산
        long dDay = ChronoUnit.DAYS.between(LocalDate.now(), post.getEndAt());
        JLabel dDayLabel = new JLabel("D-" + dDay);
        dDayLabel.setBounds(10, 10, 50, 20);
        dDayLabel.setFont(customFont.deriveFont(Font.BOLD, 20f));
        card.add(dDayLabel);

        // 2. 이미지 (donationImg 경로)
        ImageIcon rawIcon = new ImageIcon(post.getDonationImg());
        Image scaledImage = rawIcon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
        imageLabel.setBounds(10, 40, 60, 60);
        imageLabel.setBorder(new RoundedBorder(15));
        card.add(imageLabel);

        // 3. 제목
        String titleHtml = "<html><body style='width: 230px'>" + post.getTitle() + "</body></html>";
        JLabel titleLabel = new JLabel(titleHtml);
        titleLabel.setFont(customFont.deriveFont(Font.BOLD, 22f));

// 최대 높이 제한 적용
        int maxTitleHeight = 40;
        int titleHeight = Math.min(titleLabel.getPreferredSize().height, maxTitleHeight);
        titleLabel.setBounds(80, 30, 230, titleHeight);
        card.add(titleLabel);

// 4. 진행률 계산 (raisedPoint / goalPoint)
        int percent = post.getGoalPoint() == 0 ? 0 :
                (int) ((double) post.getRaisedPoint() / post.getGoalPoint() * 100);

        JLabel progressLabel = new JLabel("진행률 " + percent + "%    " + post.getGoalPoint() + "P");
        progressLabel.setFont(customFont.deriveFont(Font.PLAIN, 20f));

// 제목 바로 아래에 진행률 위치 고정
        int progressY = 30 + titleHeight + 5;
        progressLabel.setBounds(80, progressY, 250, 20);
        card.add(progressLabel);

// 카드 높이 조정 (딱 맞게)
        int totalHeight = progressY + 25;
        card.setPreferredSize(new Dimension(320, totalHeight));
        card.setMaximumSize(new Dimension(320, totalHeight));

        // 6. 스크랩 버튼
        JButton scrapBtn = new JButton(new ImageIcon("icons/bookmark.png"));
        scrapBtn.setBounds(310, 10, 30, 30);
        scrapBtn.setContentAreaFilled(false);
        scrapBtn.setBorderPainted(false);
        scrapBtn.addActionListener(e -> {
            // 스크랩 로직 (DB 저장 or 로컬 저장 등)
            JOptionPane.showMessageDialog(this, "스크랩에 저장되었습니다.");
        });
        card.add(scrapBtn);

        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        card.addMouseListener(new MouseAdapter() {
            private long lastClickTime = 0;

            @Override
            public void mouseClicked(MouseEvent e) {
                long clickTime = System.currentTimeMillis();

                // 더블 클릭 감지 (두 번 클릭 간격 400ms 이하)
                if (clickTime - lastClickTime < 400) {
                    JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(card);

                    new DonationPostDetailView(
                            post,
                            loginUser,
                            controller,
                            () -> refreshCardList(),
                            currentFrame
                    ).setVisible(true);
                }

                lastClickTime = clickTime;
            }
        });


        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.setPreferredSize(new Dimension(355, 145));
        wrapper.setMaximumSize(new Dimension(355, 145));
        wrapper.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0)); // 좌우 10px, 아래 여백도 추가
        wrapper.add(card, BorderLayout.CENTER);

        return wrapper;
    }



    //UI 테스트 용
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // 더미 사용자
            User dummyUser = new User(
                    "dummy01", "pass1234", "홍길동", "길동이",
                    BankType.KB, "123-456-7890"
            );

            // 더미 서비스
            DonationPostService dummyService = new DonationPostService() {
                //@Override
                public List<DonationPost> getOngoingPosts() {
                    return List.of(
                            new DonationPost(dummyUser, "images/dog1.jpg", 10000000, LocalDate.now().plusDays(10), "아기 유기견들을 도와주세요", "내용 없음"),
                            new DonationPost(dummyUser, "images/dog2.jpg", 8000000, LocalDate.now().plusDays(31), "기부글 제목을 적어요", "내용 없음"),
                            new DonationPost(dummyUser, "images/dog2.jpg", 8000000, LocalDate.now().plusDays(31), "기부글 제목을 적어요", "내용 없음"),
                            new DonationPost(dummyUser, "images/dog2.jpg", 8000000, LocalDate.now().plusDays(31), "기부글 제목을 적어요", "내용 없음")
                    );
                }

                //@Override
                public List<DonationPost> getCompletedPosts() {
                    return List.of(
                            new DonationPost(dummyUser, "images/dog3.jpg", 5000000, LocalDate.now().minusDays(1), "완료된 기부입니다", "내용 없음")
                    );
                }

                // 필요하면 다른 메서드도 override
            };

            // 컨트롤러에 서비스 주입
            DonationPostController dummyController = new DonationPostController(dummyService);

            // UI 실행
            new DonationPostListView(dummyUser, dummyController, null);
        });
    }

}
