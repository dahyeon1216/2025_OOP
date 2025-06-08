package capstone.view.main;

//기부글 진행중/진행완료 리스트 보여주는 view
//피그마 - 기부글_M_진행중/진행완료

import capstone.controller.DonationPostController;
import capstone.controller.ScrapController;
import capstone.model.DonationPost;
import capstone.model.Tier;
import capstone.model.User;
import capstone.service.DonationPostService;
import capstone.service.ScrapService;
import capstone.view.BaseView;
import capstone.view.Roundborder.RoundedBorder;
import capstone.view.Roundborder.RoundedButton;
import capstone.view.donation.DonationPostPanelFactory;
import capstone.view.donation.DonationPostWriteView;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static capstone.model.BankType.KB;

public class DonationPostListPanel extends BaseView {
    private final DefaultListModel<DonationPost> listModel = new DefaultListModel<>();
    private final JList<DonationPost> postList = new JList<>(listModel);
    private final User loginUser;
    private final DonationPostController controller;
    private final ScrapController scrapController;


    private RoundedButton ongoingBtn;
    private RoundedButton completedBtn;
    private JPanel postListPanel;

    public DonationPostListPanel(User loginUser,
                                 DonationPostController controller,
                                 ScrapController scrapController) {

        super("기부담다");

        this.loginUser = loginUser;
        this.controller = controller;
        this.scrapController = scrapController;

        JPanel header = createHeader("기부담다");
        addProfileIconToHeader(header);

        // 필터 버튼 패널
        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        filterPanel.setOpaque(false);

        // 진행중 버튼 생성 및 초기 색상 설정
        // 초기에는 '진행중'이 선택된 상태로 시작하도록 설정
        this.ongoingBtn = new RoundedButton("진행중", Color.BLACK, 30);
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
            //refresh(); // '진행중' 게시글을 가져오도록 refresh 호출
        });

        // 진행완료 버튼 액션 리스너:
        completedBtn.addActionListener(e -> {
            setFilterButtonColors(completedBtn, ongoingBtn); // 현재 버튼 활성화, 다른 버튼 비활성화
            //refresh(); // '진행완료' 게시글을 가져오도록 refresh 호출
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
        JButton writeBtn = new RoundedButton("기부글 쓰기", Color.BLACK, 30);
        writeBtn.setForeground(Color.WHITE);
        writeBtn.setPreferredSize(new Dimension(130, 45)); // 너비, 높이
        writeBtn.setFont(customFont.deriveFont(Font.BOLD, 22f));

        //기부글 쓰기 버튼 액션 리스너:
        writeBtn.addActionListener(e ->
        {
            //DonationPostWriteView 호출 시 현재 뷰 숨기기
            new DonationPostWriteView(loginUser, controller, null).setVisible(true);
            this.setVisible(false); // 현재 목록 뷰 숨기기
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
        int maxTitleHeight = 40;
        int titleHeight = Math.min(titleLabel.getPreferredSize().height, maxTitleHeight);
        titleLabel.setBounds(80, 30, 230, titleHeight);
        card.add(titleLabel);

        // 4. 진행률 계산 (raisedPoint / goalPoint)
        int percent = post.getGoalPoint() == 0 ? 0 :
                (int) ((double) post.getRaisedPoint() / post.getGoalPoint() * 100);
        JLabel progressLabel = new JLabel("진행률 " + percent + "%    " + post.getRaisedPoint() + "P");
        progressLabel.setFont(customFont.deriveFont(Font.PLAIN, 20f));
        int progressY = 30 + titleHeight + 5;
        progressLabel.setBounds(80, progressY, 250, 20);
        card.add(progressLabel);

        //카드 높이 조정
        int totalHeight = progressY + 25;
        card.setPreferredSize(new Dimension(320, totalHeight));
        card.setMaximumSize(new Dimension(320, totalHeight));

        // 6. 스크랩 버튼
        JButton scrapBtn = new JButton(new ImageIcon("icons/bookmark.png"));
        scrapBtn.setBounds(310, 10, 30, 30);
        scrapBtn.setContentAreaFilled(false);
        scrapBtn.setBorderPainted(false);

        /*여기에 스크랩 리스너 추가해주세요!
        scrapBtn.addActionListener(e -> {
            // 스크랩 로직 (DB 저장 or 로컬 저장 등)
            JOptionPane.showMessageDialog(this, "스크랩에 저장되었습니다.");
        });*/
        card.add(scrapBtn);

        /*그리고 이 부분에 DonationPostWriteFactory에 있는 '더블 클릭 이벤트: 상세보기 창 띄우기
          postList.addMouseListener ' 넣으면 될 것 같습니다!*/

        //전체 조립
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.setPreferredSize(new Dimension(355, 145));
        wrapper.setMaximumSize(new Dimension(355, 145));
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
        profileBtn.setBounds(335, 6, 40, 30);
        profileBtn.setBorderPainted(false);
        profileBtn.setContentAreaFilled(false);
        profileBtn.setFocusPainted(false);

        /*이 부분 MY_MAIN으로 넘어갈 수 있도록 추가해주세요!
        profileBtn.addActionListener(e -> {
        });*/

        header.add(profileBtn);
    }

    public void refresh() {
        postListPanel.removeAll();

        /*List<DonationPost> posts = controller.getAllPosts();

        Runnable onPostUpdated = this::refresh;

        JPanel listPanel = DonationPostPanelFactory.createPostListPanel(
                "전체 기부글 목록", posts, loginUser, controller, scrapController, onPostUpdated
        );*/

        for (DonationPost post : controller.getAllPosts()) {
            JPanel card = createDonationCard(post); // 직접 카드 생성
            postListPanel.add(card);
        }

        //postListPanel.add(listPanel, BorderLayout.CENTER);
        postListPanel.revalidate();
        postListPanel.repaint();
    }

    /*public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // 1. 더미 사용자 생성
            User dummyUser = new User(
                    "user01",                   // userId
                    "password123",              // password
                    "홍길동",                    // name
                    "길동이",                    // nickName
                    "icons/profile.png",        // profileImg (이미지 존재해야 함)
                    KB,                         // BankType
                    "123-456-7890",             // bankAccount
                    0,                          // point
                    Tier.BRONZE                 // tier
            );

            // 2. 더미 포스트 생성
            List<DonationPost> dummyPosts = new ArrayList<>();

            DonationPost post1 = new DonationPost(
                    dummyUser,
                    "icons/sample1.png",        // 이미지 경로 존재해야 함
                    1000,
                    LocalDate.now().plusDays(5),
                    "진행중 기부 제목",
                    "진행중 기부 내용입니다."
            );
            post1.setRaisedPoint(300); // 진행률 30%

            DonationPost post2 = new DonationPost(
                    dummyUser,
                    "icons/sample2.png",
                    1000,
                    LocalDate.now().minusDays(3),
                    "완료된 기부 제목",
                    "완료된 기부 내용입니다."
            );
            post2.setRaisedPoint(1000); // 목표 달성

            dummyPosts.add(post1);
            dummyPosts.add(post2);

            // 3. 더미 DonationPostService 직접 구현
            DonationPostService dummyService = new DonationPostService() {
                @Override
                public List<DonationPost> getAll() {
                    return dummyPosts; // 여기서 바로 더미 리스트 반환
                }
            };


            // 실제 controller 생성자에 서비스 주입
            DonationPostController dummyController = new DonationPostController(dummyService);

            ScrapService dummyScrapService = new ScrapService();
            ScrapController dummyScrapController = new ScrapController(dummyScrapService) {
                // 필요 시 구현
            };

            DonationPostListPanel view = new DonationPostListPanel(dummyUser, dummyController, dummyScrapController);

        });
    }*/
    }