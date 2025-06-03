package capstone.view.donation;

import capstone.controller.DonationPostController;
import capstone.controller.UserController;
import capstone.model.DonationPost;
import capstone.model.Tier;
import capstone.model.User;
import capstone.service.DonationPostService;
import capstone.service.UserService;
import capstone.view.BaseView;
import capstone.view.Roundborder.RoundedButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;



public class DonationActionView extends BaseView {

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

    private final DonationPost donationPost;
    private final User loginUser;
    private final DonationPostController donationPostController;
    private int selectedPoint = 0; // 선택된 기부 포인트
    private JLabel currentDonationPointLabel; // 기부 포인트 표시 라벨
    private JCheckBox anonymousCheckBox; // 익명 기부 체크박스

    public DonationActionView(DonationPost donationPost, User loginUser, DonationPostController donationPostController, JFrame previousView) {
        super("기부하기", previousView); // BaseView의 생성자 호출

        this.donationPost = donationPost;
        this.loginUser = loginUser;
        this.donationPostController = donationPostController;

        setupUI();
    }

    private void setupUI() {
        getContentPane().setLayout(new BorderLayout());

        // 헤더 추가
        add(createHeader("기부하기"), BorderLayout.NORTH);

        // 중앙 컨텐츠와 하단 버튼을 담을 메인 컨테이너
        JPanel mainContentWrapper = new JPanel(new BorderLayout()); // 새로운 메인 래퍼 패널 (BorderLayout)
        mainContentWrapper.setBackground(Color.WHITE);

        // --- 중앙 컨텐츠 (PostPanel, separator, mainPointOptionsPanel) ---
        JPanel centerContentPanel = new JPanel();
        centerContentPanel.setLayout(new BoxLayout(centerContentPanel, BoxLayout.Y_AXIS));
        centerContentPanel.setBackground(Color.WHITE);
        centerContentPanel.setBorder(new EmptyBorder(10, 20, 10, 20)); // 좌우 여백 추가

        // PostPanel
        centerContentPanel.add(createPostPanel());
        centerContentPanel.add(Box.createVerticalStrut(3));

        // 구분선
        JPanel separator = new JPanel();
        separator.setPreferredSize(new Dimension(Integer.MAX_VALUE, 1));
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        separator.setBackground(new Color(220, 220, 220));
        centerContentPanel.add(separator);

        // 구분선과 mainPointOptionsPanel 사이의 간격
        centerContentPanel.add(Box.createVerticalStrut(20));

        JPanel pointOptionsPanel = createPointPanel();
        centerContentPanel.add(pointOptionsPanel);

        centerContentPanel.add(Box.createVerticalGlue());

        // mainContentWrapper의 CENTER에 중앙 컨텐츠 추가
        mainContentWrapper.add(centerContentPanel, BorderLayout.CENTER);

        // --- 하단 버튼 (ButtonPanel) ---
        // createButtonPanel()은 이미 적절한 여백과 버튼 자체를 반환합니다.
        JPanel buttonPanel = createButtonPanel();
        mainContentWrapper.add(buttonPanel, BorderLayout.SOUTH);

        // 최종적으로 JFrame의 CENTER에 메인 컨텐츠 래퍼를 추가
        add(mainContentWrapper, BorderLayout.CENTER);

    }

    private JPanel createPostPanel() {
        JPanel postPanel = new JPanel(new BorderLayout());
        postPanel.setBackground(Color.WHITE);
        postPanel.setBorder(new EmptyBorder(0, 0, 0, 0));

        // 1. 이미지 영역 (왼쪽)
        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);

        int imageHeight = 100;
        int imageWidth = 100;

        if (donationPost.getDonationImg() != null && !donationPost.getDonationImg().isEmpty()) {
            ImageIcon imageIcon = new ImageIcon(donationPost.getDonationImg());
            Image scaled = imageIcon.getImage().getScaledInstance(imageWidth, imageHeight, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(scaled));
        } else {
            imageLabel.setPreferredSize(new Dimension(imageWidth, imageHeight));
            imageLabel.setOpaque(true);
            imageLabel.setBackground(Color.LIGHT_GRAY);
            imageLabel.setText("사진 없음");
            imageLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        }

        JPanel imageWrapperPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        imageWrapperPanel.setBackground(Color.WHITE);
        imageWrapperPanel.add(imageLabel);
        imageWrapperPanel.setPreferredSize(new Dimension(imageWidth, imageHeight));
        imageWrapperPanel.setMaximumSize(new Dimension(imageWidth, imageHeight));
        imageWrapperPanel.setMinimumSize(new Dimension(imageWidth, imageHeight));


        postPanel.add(imageWrapperPanel, BorderLayout.WEST);

        // 2. 타이틀 및 사용자 닉네임 영역 (오른쪽)
        JPanel textAndUserPanel = new JPanel();
        textAndUserPanel.setLayout(new BoxLayout(textAndUserPanel, BoxLayout.Y_AXIS)); // 세로 정렬 유지
        textAndUserPanel.setBackground(Color.WHITE);
        textAndUserPanel.setBorder(new EmptyBorder(0, 10, 0, 0)); // 이미지와의 간격

        // 이 패널 자체를 왼쪽으로 정렬
        textAndUserPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // 타이틀 라벨
        JLabel titleLabel = new JLabel(donationPost.getTitle());
        titleLabel.setFont(customFont.deriveFont(Font.BOLD, 25f));
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        textAndUserPanel.add(titleLabel);

        textAndUserPanel.add(Box.createVerticalStrut(15));

        // 사용자의 닉네임 라벨 추가
        JLabel userNicknameLabel = new JLabel(loginUser.getNickName()); // loginUser에서 닉네임 가져오기
        userNicknameLabel.setFont(customFont.deriveFont(Font.BOLD, 22f));
        userNicknameLabel.setForeground(Color.GRAY);
        userNicknameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        textAndUserPanel.add(userNicknameLabel);

        postPanel.add(textAndUserPanel, BorderLayout.CENTER); // 텍스트 및 사용자 닉네임 영역을 중앙에 배치

        int postPanelHeight = imageHeight + 20; // 넉넉하게 이미지 높이 + 여백
        postPanel.setPreferredSize(new Dimension(getWidth(), postPanelHeight));
        postPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, postPanelHeight));
        postPanel.setMinimumSize(new Dimension(0, postPanelHeight));

        return postPanel;
    }

    private JPanel createPointPanel() {
        JPanel mainPointOptionsPanel = new JPanel();
        mainPointOptionsPanel.setLayout(new BoxLayout(mainPointOptionsPanel, BoxLayout.Y_AXIS));
        mainPointOptionsPanel.setBackground(Color.WHITE);
        mainPointOptionsPanel.setBorder(new EmptyBorder(0, 0, 0, 0)); // 패널 자체 여백 제거

        // 기부 포인트 라벨 (현재 포인트 표시)
        JPanel currentPointWrapper = new JPanel(new BorderLayout()); // BorderLayout 사용
        currentPointWrapper.setBackground(Color.WHITE);
        currentPointWrapper.setBorder(new EmptyBorder(0, 20, 0, 20)); // currentPointWrapper 자체에 좌우 여백 추가 (선택 사항)

        JLabel pointTextLabel = new JLabel("현재 기부 포인트");
        pointTextLabel.setFont(customFont.deriveFont(Font.BOLD, 26));
        pointTextLabel.setForeground(Color.GRAY);
        currentPointWrapper.add(pointTextLabel, BorderLayout.WEST); // "현재 기부 포인트"를 WEST에 배치

        currentDonationPointLabel = new JLabel("0 P"); // 초기값을 "0 P"로 변경
        currentDonationPointLabel.setFont(customFont.deriveFont(Font.BOLD, 26f));
        currentDonationPointLabel.setForeground(new Color(60, 60, 60));
        currentDonationPointLabel.setBorder(new EmptyBorder(0, 0, 0, 5));
        currentPointWrapper.add(currentDonationPointLabel, BorderLayout.EAST); // "0 P"를 EAST에 배치

        mainPointOptionsPanel.add(currentPointWrapper); // mainPointOptionsPanel의 맨 위에 추가
        mainPointOptionsPanel.add(Box.createVerticalStrut(10)); // 다른 컴포넌트와의 간격

        // 숫자 옵션 버튼
        JPanel buttonGridPanel = new JPanel(new GridLayout(2, 3, 10, 10)); // 2x3 그리드, 간격 10
        buttonGridPanel.setBackground(Color.WHITE);
        int[] points = {1000, 3000, 5000, 10000, 30000, 50000};
        for (int p : points) {
            JButton pointBtn = new RoundedButton(p + " P", new Color(240, 240, 240), 20);
            pointBtn.setFont(customFont.deriveFont(Font.BOLD, 23f));
            pointBtn.setPreferredSize(new Dimension(100, 50));
            pointBtn.setForeground(Color.BLACK);

            pointBtn.addActionListener(e -> {
                selectedPoint = p;
                currentDonationPointLabel.setText(selectedPoint + " P"); // PointPanel의 라벨 업데이트
                resetButtonColors(buttonGridPanel);
                ((RoundedButton) e.getSource()).setButtonColor(new Color(120, 230, 170));
            });
            buttonGridPanel.add(pointBtn);
        }
        mainPointOptionsPanel.add(buttonGridPanel);
        mainPointOptionsPanel.add(Box.createVerticalStrut(15));

        // 직접 입력 필드
        JPanel directInputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        directInputPanel.setBackground(Color.WHITE);
        JTextField directInputTextField = new JTextField(10);
        directInputTextField.setFont(customFont.deriveFont(Font.PLAIN, 20f));
        directInputTextField.setHorizontalAlignment(JTextField.RIGHT);
        directInputPanel.add(directInputTextField);

        JButton directInputButton = new RoundedButton("직접 입력", new Color(60, 60, 60), 20);
        directInputButton.setFont(customFont.deriveFont(Font.BOLD, 23f));
        directInputButton.setPreferredSize(new Dimension(130, 40));
        directInputButton.addActionListener(e -> {
            try {
                int inputPoint = Integer.parseInt(directInputTextField.getText());
                if (inputPoint < 0) {
                    JOptionPane.showMessageDialog(this, "0 이상의 포인트를 입력해주세요.", "입력 오류", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                selectedPoint = inputPoint;
                currentDonationPointLabel.setText(selectedPoint + " P"); // PointPanel의 라벨 업데이트
                directInputTextField.setText("");
                resetButtonColors(buttonGridPanel);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "유효한 숫자를 입력해주세요.", "입력 오류", JOptionPane.WARNING_MESSAGE);
            }
        });
        directInputPanel.add(directInputButton);

        JButton clearInputButton = new RoundedButton("초기화", new Color(180, 180, 180), 20); // 연한 회색 버튼
        clearInputButton.setFont(customFont.deriveFont(Font.BOLD, 20f));
        clearInputButton.setPreferredSize(new Dimension(90, 40));
        clearInputButton.setForeground(Color.WHITE);
        clearInputButton.addActionListener(e -> {
            selectedPoint = 0; // 선택 포인트 0으로 초기화
            currentDonationPointLabel.setText("0 P"); // 라벨 업데이트
            directInputTextField.setText(""); // 입력 필드 비우기
            resetButtonColors(buttonGridPanel); // 모든 숫자 버튼 색상 초기화
        });
        directInputPanel.add(clearInputButton);

        mainPointOptionsPanel.add(directInputPanel);
        mainPointOptionsPanel.add(Box.createVerticalStrut(15));

        // 익명으로 기부하기 체크박스
        anonymousCheckBox = new JCheckBox("익명으로 기부하기");
        anonymousCheckBox.setBackground(Color.WHITE);
        anonymousCheckBox.setFont(customFont.deriveFont(Font.BOLD, 22f));
        anonymousCheckBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPointOptionsPanel.add(anonymousCheckBox);

        return mainPointOptionsPanel;
    }

    // 버튼 색상을 초기화하는 헬퍼 메서드 추가
    private void resetButtonColors(JPanel buttonPanel) {
        for (Component comp : buttonPanel.getComponents()) {
            if (comp instanceof RoundedButton) {
                ((RoundedButton) comp).setButtonColor(new Color(240, 240, 240));
            }
        }
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setBorder(new EmptyBorder(20, 0, 20, 0)); // 상하 여백
        buttonPanel.setOpaque(false); // 배경 투명하게 설정

        // 버튼 자체의 높이를 고정하기 위해 새로운 JPanel을 사용
        JPanel buttonWrapper = new JPanel();
        buttonWrapper.setBackground(Color.WHITE); // 배경색 설정 (디버깅 용이)
        buttonWrapper.setLayout(new BoxLayout(buttonWrapper, BoxLayout.Y_AXIS)); // 수직 정렬

        // 기부하기 버튼
        JButton donateButton = new RoundedButton("기부하기", new Color(60, 60, 60), 30);
        donateButton.setFont(customFont.deriveFont(Font.BOLD, 20f));

        // 버튼의 선호/최대/최소 크기를 동일하게 설정하여 높이 고정
        int desiredButtonWidth = 353; // 원하는 버튼의 가로 길이 설정 (예: 300px)
        donateButton.setPreferredSize(new Dimension(desiredButtonWidth, 44));
        donateButton.setMaximumSize(new Dimension(desiredButtonWidth, 44));
        donateButton.setMinimumSize(new Dimension(desiredButtonWidth, 44));
        donateButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        donateButton.addActionListener(e -> {
            if (selectedPoint <= 0) {
                JOptionPane.showMessageDialog(this, "기부 포인트를 선택해주세요.", "기부 오류", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (loginUser.getPoint() < selectedPoint) {
                JOptionPane.showMessageDialog(this, "보유 포인트가 부족합니다.", "기부 오류", JOptionPane.WARNING_MESSAGE);
                return;
            }

            loginUser.setPoint(loginUser.getPoint() - selectedPoint);
            donationPost.setRaisedPoint(donationPost.getRaisedPoint() + selectedPoint);

            String donorName = anonymousCheckBox.isSelected() ? "익명" : loginUser.getNickName();

            JOptionPane.showMessageDialog(this,
                    donorName + "님, " + selectedPoint + " P 기부가 완료되었습니다!\n" +
                            "현재 " + donationPost.getTitle() + "의 모금액: " + donationPost.getRaisedPoint() + " P",
                    "기부 완료",
                    JOptionPane.INFORMATION_MESSAGE);

            dispose();
            new DonationPostCompleteView(loginUser, new UserController(new UserService()), donationPostController);
        });

        buttonWrapper.add(donateButton); // donateButton을 buttonWrapper에 추가
        buttonPanel.add(buttonWrapper, BorderLayout.CENTER); // buttonWrapper를 buttonPanel의 CENTER에 추가

        return buttonPanel;
    }

    // --- 테스트용 메인 함수 ---
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // 1. 더미 사용자
            User dummyUser = new User("sally1023", "기부자1", "images/profile.jpg", Tier.SILVER);
            dummyUser.setPoint(50000); // 테스트를 위해 충분한 포인트 부여

            // 2. 더미 기부글
            DonationPost dummyPost = new DonationPost();
            dummyPost.setTitle("유기견 아이들을 도와주세요");
            dummyPost.setContent("이 아이들은 추운 겨울을 이겨내야 합니다. 따뜻한 손길이 필요해요.");
            dummyPost.setDonationImg("images/sample.jpg"); // 실제 이미지 경로로 변경 필요
            dummyPost.setGoalPoint(100000);
            dummyPost.setRaisedPoint(3500);
            dummyPost.setWriter(dummyUser);

            DonationPostService service = new DonationPostService();
            // 3. 더미 컨트롤러
            DonationPostController dummyController = new DonationPostController(service); // 필요 시 수정

            // 4. 이전 화면 없음 (null) 또는 DonationPostDetailView 더미 인스턴스
            JFrame dummyPreviousView = new JFrame("이전 화면 더미");
            dummyPreviousView.setSize(300, 300);
            dummyPreviousView.setLocationRelativeTo(null);
            dummyPreviousView.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            dummyPreviousView.setVisible(false); // 처음에는 숨김

            new DonationActionView(dummyPost, dummyUser, dummyController, dummyPreviousView);
        });
    }
}
