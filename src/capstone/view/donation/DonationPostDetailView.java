package capstone.view.donation;

import capstone.controller.DonationPostController;
import capstone.model.DonationPost;
import capstone.model.Tier;
import capstone.model.User;
import capstone.service.DonationPostService;
import capstone.view.BaseView;
import capstone.view.Roundborder.RoundedBorder;
import capstone.view.Roundborder.RoundedButton;
import capstone.view.main.DonationPostListView;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class DonationPostDetailView extends BaseView {

    private final DonationPost post;
    private final User loginUser;
    private final DonationPostController controller;
    private final Runnable refreshAction;
    private DonationPostListView postListView;

    public DonationPostDetailView(DonationPost post,
                                  User loginUser,
                                  DonationPostController controller,
                                  Runnable refreshAction,
                                  DonationPostListView listView) {
        super(post.getTitle(), listView);

        this.post = post;
        this.loginUser = loginUser;
        this.controller = controller;
        this.refreshAction = refreshAction;
        this.postListView = listView;

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setLayout(null);  // 자유 배치
        mainPanel.setPreferredSize(new Dimension(393, 900));

        // 헤더
        JPanel header = createHeader(post.getTitle());
        header.setBounds(0, 0, 393, 45);
        JButton optionButton = createMenuBarButton();
        optionButton.setBounds(335, 6, 40, 30);

        // 팝업 메뉴 생성
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem editMenuItem = new JMenuItem("수정하기");
        JMenuItem deleteMenuItem = new JMenuItem("삭제하기");

        // 로그인한 유저의 ID와 게시글 작성자의 ID를 비교
        if (loginUser != null && post.getWriter() != null && loginUser.getUserId().equals(post.getWriter().getUserId())) {
            popupMenu.add(editMenuItem);
            popupMenu.add(deleteMenuItem);
            header.add(optionButton);
        } else {
            // 본인 글이 아닐 경우, 메뉴 버튼을을 팝업 메뉴에 추가하지 않음
        }

        // "수정하기" 메뉴 아이템 액션 리스너
        editMenuItem.addActionListener(e -> {
            dispose(); // 현재 상세 뷰 닫기

            // 콜백 정의: 수정 완료 후 새 상세 뷰 열기
            DonationPostEditView.EditCallback callback = (int updatedPostId) -> {
                // 수정된 게시글 정보를 서비스에서 다시 불러옴
                DonationPost updatedPost = controller.getPostById(updatedPostId);

                if (updatedPost == null) {
                    JOptionPane.showMessageDialog(this, "수정된 글 정보를 불러올 수 없습니다."); // this 사용
                    if (postListView != null) {
                        postListView.refreshCardList();
                        postListView.setVisible(true);
                    }
                    return;
                }

                // 새 상세 뷰를 보이도록 설정
                new DonationPostDetailView(
                        updatedPost,
                        loginUser,
                        controller,
                        refreshAction,
                        postListView // ListView 참조 전달
                ).setVisible(true);

                if (postListView != null) {
                    postListView.refreshCardList();
                    postListView.setVisible(false); // 이미 숨겨져 있거나 DetailView가 활성화되면서 숨겨짐
                }

            };


            // 수정 화면 열기
            new DonationPostEditView(post, loginUser, controller, callback).setVisible(true);
        });


        // "삭제하기" 메뉴 아이템 액션 리스너
        deleteMenuItem.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    this, // 부모 컴포넌트: null 대신 this(JFrame)를 사용
                    "정말로 이 게시글을 삭제하시겠습니까?",
                    "삭제 확인",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                controller.deletePost(post.getId()); // controller를 통해 deletePost 호출
                JOptionPane.showMessageDialog(this, "게시글이 삭제되었습니다."); // 메시지 먼저 표시 (this 사용)

                dispose(); // 현재 상세 뷰 닫기

                // 삭제 후 목록으로 돌아가야 합니다.
                if (postListView != null) {
                    postListView.refreshCardList(); // 목록 새로고침
                    postListView.setVisible(true); // 목록 뷰를 다시 보이게
                } else if (refreshAction != null) { // 백업으로 refreshAction 실행
                    refreshAction.run();
                    if (previousView != null) previousView.setVisible(true); // 이전 뷰로 돌아감
                } else {
                    // 돌아갈 뷰가 없는 경우 (오류 처리 또는 앱 종료)
                    JOptionPane.showMessageDialog(this, "돌아갈 화면을 찾을 수 없습니다.");
                    // System.exit(0);
                }
            }
        });

        // 버튼 리스너: 옵션 버튼 클릭 시 팝업 메뉴 표시
        optionButton.addActionListener(e -> {
            // 버튼 아래에 팝업 메뉴가 나타나도록 위치를 조정
            popupMenu.show(optionButton, 0, optionButton.getHeight());
        });

        mainPanel.add(header);

        //이미지 영역
        JLabel imageLabel = new JLabel();
        imageLabel.setBounds(0, 45, 393, 393);

        String imagePath = post.getDonationImg();
        if (imagePath != null && !imagePath.isEmpty()) {
            ImageIcon imageIcon = new ImageIcon(imagePath);
            Image scaled = imageIcon.getImage().getScaledInstance(393, 393, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(scaled));
        } else {
            imageLabel.setOpaque(true);
            imageLabel.setBackground(Color.LIGHT_GRAY);
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            imageLabel.setVerticalAlignment(SwingConstants.CENTER);
            imageLabel.setText("사진");
            imageLabel.setFont(new Font("SansSerif", Font.PLAIN, 20));
        }

        mainPanel.add(imageLabel);

        // 프로필 영역
        JPanel profilePanel = new JPanel(null);
        profilePanel.setBounds(0, 445, 393, 100); // 사진 영역(393) + 헤더(45) 아래
        profilePanel.setBackground(Color.WHITE);

        // 프로필 이미지
        JLabel profileImg = new JLabel();
        profileImg.setBounds(15, 15, 70, 70);

        String profilePath = loginUser.getProfileImg();


        if (profilePath != null && !profilePath.isEmpty()) {

            ImageIcon roundedIcon = getRoundedImageIcon(profilePath, 70);
            profileImg.setIcon(roundedIcon);
        } else {
            profileImg.setOpaque(true);
            profileImg.setBackground(Color.LIGHT_GRAY);
        }

        profilePanel.add(profileImg);

        // 닉네임
        JLabel nicknameLabel = new JLabel(loginUser.getNickName());
        nicknameLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        nicknameLabel.setBounds(100, 10, 200, 25);
        profilePanel.add(nicknameLabel);

       // 티어
        JLabel tierLabel = new JLabel("⭐ " + loginUser.getTier() + "티어");
//나중에 티어마다 임티 가져오는거 설정하기

// getTier()는 String or int
        tierLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tierLabel.setBounds(290, 10, 100, 25);
        profilePanel.add(tierLabel);

// 목표 금액
        JLabel goalLabel = new JLabel("목표금액 " + post.getGoalPoint() + "P");
        goalLabel.setFont(new Font("SansSerif", Font.PLAIN, 15));
        goalLabel.setBounds(100, 37, 200, 20);
        profilePanel.add(goalLabel);

// 현재 금액
        JLabel raisedLabel = new JLabel("현재금액 " + post.getRaisedPoint() + "P");
        raisedLabel.setFont(new Font("SansSerif", Font.PLAIN, 15));
        raisedLabel.setBounds(100, 57, 200, 20);
        profilePanel.add(raisedLabel);

        mainPanel.add(profilePanel);

        //본문 영역
        // 본문 전체를 감쌀 둥근 패널
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(null);
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBounds(15, 545, 348, 400); // 좌우 여백 15씩 정확히 맞춤


// 내용 텍스트
        JTextArea contentArea = new JTextArea(post.getContent());
        contentArea.setBounds(5, 0,340 , 250); // ← 좌우 여백 20px
        contentArea.setBorder(new RoundedBorder(15));
        contentArea.setBackground(new Color(240, 240, 240));
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        contentArea.setEditable(false); //읽기 전용
        contentArea.setFont(new Font("SansSerif", Font.PLAIN, 15));

        contentPanel.add(contentArea);

        // 기부하기 버튼 생성 및 설정
        RoundedButton donationBtn = new RoundedButton("기부하기", new Color(60, 60, 60), 30);
        donationBtn.setPreferredSize(new Dimension(0, 44));
        donationBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        donationBtn.setFont(customFont.deriveFont(Font.BOLD, 20f));
        donationBtn.setForeground(Color.WHITE);
        donationBtn.setBounds(245, 270, 100, 44);

        donationBtn.addActionListener(e -> {
            // DonationActionView로 이동
            // 현재 DonationPostDetailView를 previousView로 전달
            new DonationActionView(post, loginUser, controller, this).setVisible(true);
            this.setVisible(false); // DonationActionView가 닫히면 다시 이 화면이 보이게 하려면
        });

        contentPanel.add(donationBtn);

        mainPanel.add(contentPanel);

        //스크롤
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBounds(0, 0, 393, 698); // 프레임 크기와 동일
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // 가로 스크롤 제거
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);    // 세로 스크롤 항상 표시

        add(scrollPane);

        setVisible(true);
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

    //테스트용 UI
    /*public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            //1. 더미 사용자
            User dummyUser = new User("sally1023", "기부자1","images/profile.jpg", Tier.SILVER);

            // 2. 더미 기부글
            DonationPost dummyPost = new DonationPost();
            dummyPost.setTitle("유기견 아이들을 도와주세요");
            dummyPost.setContent("이 아이들은 추운 겨울을 이겨내야 합니다. 따뜻한 손길이 필요해요.");
            dummyPost.setDonationImg("images/sample.jpg");
            dummyPost.setGoalPoint(10000);
            dummyPost.setRaisedPoint(3500);
            dummyPost.setWriter(dummyUser);

            DonationPostService service = new DonationPostService();
            // 3. 더미 컨트롤러
            DonationPostController dummyController = new DonationPostController(service); // 필요 시 수정

            // 4. 새로고침 액션
            Runnable refreshAction = () -> System.out.println("새로고침됨");

            // 5. 이전 화면 없음 (null)
            new DonationPostDetailView(dummyPost, dummyUser, dummyController, refreshAction, null);

        });
    }*/

}

