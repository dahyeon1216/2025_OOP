package capstone.view.donation;

//기부글 진행중/ 진행완료 세부 조회
//피그마-기부글_세부내역P

import capstone.controller.DonationPostController;
import capstone.controller.ScrapController;
import capstone.model.DonationPost;
import capstone.model.User;
import capstone.model.VirtualAccount;
import capstone.view.BaseView;
import capstone.view.receipt.ReceiptListView;
import capstone.view.style.RoundedBorder;
import capstone.view.style.RoundedButton;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class DonationPostDetailView extends BaseView {
    private final DonationPostController donationPostController;
    private DonationPost donationPost;
    private JLabel imageLabel;
    private JLabel goalLabel;
    private JLabel raisedLabel;
    private JTextArea contentArea;
    private RoundedButton donateButton;
    private RoundedButton usageButton;
    private JPanel contentPanel;
    private JMenuItem settleMenuItem;
    private JLabel tierLabel;


    public DonationPostDetailView(DonationPost post, User loginUser, DonationPostController donationPostController, ScrapController scrapController, Runnable onPostUpdated) {
        super(post.getTitle());
        this.donationPostController = donationPostController;
        this.donationPost = post;

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setLayout(null);
        mainPanel.setPreferredSize(new Dimension(393, 900));

        // 1. 헤더
        JPanel header = createHeader(post.getTitle());
        header.setBounds(0, 0, 393, 45);

        //삭제, 수정, 정산 버튼
        //기부글 작성자의 경우에만 나오게 설정
        if (post.getWriter() != null && post.getWriter().equals(loginUser)) {

            JButton optionButton = createMenuBarButton();
            optionButton.setBounds(335, 6, 40, 30);

            // 팝업 메뉴 생성
            JPopupMenu popupMenu = new JPopupMenu();
            JMenuItem editMenuItem = new JMenuItem("수정하기");
            JMenuItem deleteMenuItem = new JMenuItem("삭제하기");
            settleMenuItem = new JMenuItem("정산하기");

            //수정 기능
            editMenuItem.addActionListener(e -> {
                new DonationPostEditView(post, loginUser, donationPostController, () -> {
                    JOptionPane.showMessageDialog(this, "기부글이 수정되었습니다.");
                    if (onPostUpdated != null) onPostUpdated.run();
                    this.refresh();
                }).setVisible(true);
            });

            //삭제 기능
            deleteMenuItem.addActionListener(e -> {
                int confirm = JOptionPane.showConfirmDialog(
                        this,
                        "정말 삭제하시겠습니까?",
                        "삭제 확인",
                        JOptionPane.YES_NO_OPTION
                );
                if (confirm == JOptionPane.YES_OPTION) {
                    donationPostController.deletePost(post.getId());
                    JOptionPane.showMessageDialog(this, "삭제 완료");
                    if (onPostUpdated != null) onPostUpdated.run();
                    dispose();
                }
            });

            // 정산하기 기능
            settleMenuItem.addActionListener(e -> {
                boolean success = donationPostController.settlePost(post);
                if (success) {
                    JOptionPane.showMessageDialog(this, "정산이 완료되었습니다. 포인트가 지급되었습니다.");
                    onPostUpdated.run(); // 리스트 패널 새로고침
                    dispose(); // 상세창 닫기
                    new DonationPostDetailView(post, loginUser, donationPostController, scrapController, onPostUpdated).setVisible(true); // 새로 열기
                } else {
                    JOptionPane.showMessageDialog(this, "정산에 실패했습니다.");
                }
            });

            popupMenu.add(editMenuItem);
            popupMenu.add(deleteMenuItem);
            popupMenu.add(settleMenuItem);

            settleMenuItem.setVisible(post.isCompleted());
            settleMenuItem.setEnabled(!post.isSettled());

            header.add(optionButton);

            // optionButton 버튼 리스너: 옵션 버튼 클릭 시 팝업 메뉴 표시
            optionButton.addActionListener(e -> {
                popupMenu.show(optionButton, 0, optionButton.getHeight());
            });
        }

        mainPanel.add(header);

        // 2. 이미지 영역
        imageLabel = new JLabel();
        imageLabel.setBounds(0, 45, 393, 393);
        imageLabel.setIcon(loadImageOrDefault("resources/images/donation/" + post.getDonationImg(), 393, 393));
        mainPanel.add(imageLabel);

        // 3. 프로필 영역
        JPanel profilePanel = new JPanel(null);
        profilePanel.setBounds(0, 445, 393, 100);
        profilePanel.setBackground(Color.WHITE);

        // 프로필 이미지
        JLabel profileImg = new JLabel();
        profileImg.setBounds(15, 15, 70, 70);

        String profilePath = "resources/images/profile/" + post.getWriter().getProfileImg();

        ImageIcon roundedIcon = getRoundedImageIcon(profilePath, 70);
        if (roundedIcon != null) {
            profileImg.setIcon(roundedIcon);
        } else {
            // 이미지 파일을 못 읽었을 경우 fallback 색상 적용
            profileImg.setOpaque(true);
            profileImg.setBackground(Color.LIGHT_GRAY);
        }

        profilePanel.add(profileImg);

        // 닉네임
        JLabel nicknameLabel = new JLabel(post.getWriter().getNickName());
        nicknameLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        nicknameLabel.setBounds(100, 10, 170, 25);
        profilePanel.add(nicknameLabel);

        // 티어의 이모티콘 깨짐 방지
        String os = System.getProperty("os.name").toLowerCase();
        Font emojiFont;

        if (os.contains("mac")) {
            emojiFont = new Font("Apple Color Emoji", Font.PLAIN, 13);
        } else if (os.contains("win")) {
            emojiFont = new Font("Segoe UI Emoji", Font.PLAIN, 13);
        } else {
            emojiFont = new Font("Noto Color Emoji", Font.PLAIN, 13); // 리눅스 등
        }

        // 티어
        tierLabel = new JLabel(post.getWriter().getTier());
        tierLabel.setFont(emojiFont);
        tierLabel.setBounds(290, 10, 100, 25);
        profilePanel.add(tierLabel);

        // 목표 금액
        goalLabel = new JLabel("목표금액 " + post.getGoalPoint() + "P");
        goalLabel.setFont(new Font("SansSerif", Font.PLAIN, 15));
        goalLabel.setBounds(100, 37, 200, 20);
        profilePanel.add(goalLabel);

        // 현재 금액
        raisedLabel = new JLabel("현재금액 " + post.getRaisedPoint() + "P");
        raisedLabel.setFont(new Font("SansSerif", Font.PLAIN, 15));
        raisedLabel.setBounds(100, 57, 200, 20);
        profilePanel.add(raisedLabel);

        mainPanel.add(profilePanel);

        //4. 본문 영역

        // 본문 전체를 감쌀 둥근 패널
        contentPanel = new JPanel();
        contentPanel.setLayout(null);
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBounds(15, 545, 348, 400);

        // 내용 텍스트
        contentArea = new JTextArea(post.getContent());
        contentArea.setBounds(5, 0, 340, 250); // ← 좌우 여백 20px
        contentArea.setBorder(new RoundedBorder(15));
        contentArea.setBackground(new Color(240, 240, 240));
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        contentArea.setEditable(false); //읽기 전용
        contentArea.setFont(new Font("SansSerif", Font.PLAIN, 15));
        contentPanel.add(contentArea);


        // 기부하기 버튼
        donateButton = new RoundedButton("기부하기", new Color(60, 60, 60), 30);
        donateButton.setPreferredSize(new Dimension(0, 44));
        donateButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        donateButton.setFont(customFont.deriveFont(Font.BOLD, 20f));
        donateButton.setForeground(Color.WHITE);
        donateButton.setBounds(245, 270, 100, 44);

        donateButton.addActionListener(e -> {
            new DonateActionView(donationPost, loginUser, donationPostController, scrapController, () -> {
                if (onPostUpdated != null) onPostUpdated.run();
                refresh();
            }).setVisible(true);
        });

        contentPanel.add(donateButton);


        // 사용내역 버튼
        usageButton = new RoundedButton("사용내역", new Color(60, 60, 60), 30);
        usageButton.setPreferredSize(new Dimension(0, 44));
        usageButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        usageButton.setFont(customFont.deriveFont(Font.BOLD, 20f));
        usageButton.setForeground(Color.WHITE);
        usageButton.setBounds(245, 270, 100, 44);
        usageButton.setEnabled(post.isSettled());

        usageButton.addActionListener(e -> {
            VirtualAccount va = donationPost.getVirtualAccount();
            if (va != null) {
                ReceiptListView receiptView = new ReceiptListView(va, loginUser);
                receiptView.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "아직 정산되지 않아 사용내역이 없습니다.");
            }
        });

        contentPanel.add(usageButton);

        donateButton.setVisible(!post.isCompleted());
        usageButton.setVisible(post.isCompleted());
        usageButton.setEnabled(post.isSettled());

        mainPanel.add(contentPanel);

        //스크롤 기능 추가
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBounds(0, 0, 393, 698); // 프레임 크기와 동일
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // 가로 스크롤 제거
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);    // 세로 스크롤 항상 표시

        add(scrollPane);

        setVisible(true);
    }

    private ImageIcon loadImageOrDefault(String path, int width, int height) {
        try {
            File file = new File(path);
            if (!file.exists()) throw new IOException("파일 없음");
            ImageIcon icon = new ImageIcon(path);
            Image scaled = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaled);
        } catch (Exception e) {
            ImageIcon fallback = new ImageIcon("icons/image-fail.png");
            Image scaled = fallback.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaled);
        }
    }

    //----view 부가적인 코드----
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

    public void refresh() {
        DonationPost updatedPost = donationPostController.getPost(donationPost.getId());
        this.donationPost = updatedPost;

        // 2. 헤더 타이틀 변경
        setTitle(updatedPost.getTitle());

        // 3. 이미지 다시 로딩
        ImageIcon updatedImageIcon = loadImageOrDefault("resources/images/donation/" + updatedPost.getDonationImg(), 393, 393);
        imageLabel.setIcon(updatedImageIcon);

        // 4. 목표금액 & 현재금액 갱신
        goalLabel.setText("목표금액 " + updatedPost.getGoalPoint() + "P");
        raisedLabel.setText("현재금액 " + updatedPost.getRaisedPoint() + "P");

        // 5. 본문 내용 갱신
        contentArea.setText(updatedPost.getContent());

        donateButton.setVisible(!updatedPost.isCompleted());
        usageButton.setVisible(updatedPost.isCompleted());
        usageButton.setEnabled(updatedPost.isSettled());

        if (settleMenuItem != null) {
            settleMenuItem.setVisible(updatedPost.isCompleted());
            settleMenuItem.setEnabled(!updatedPost.isSettled());
        }

        // 기부하기 기능ㅇ로 인한 티어 갱신
        if (tierLabel != null) {
            String os = System.getProperty("os.name").toLowerCase();
            Font emojiFont = os.contains("mac") ? new Font("Apple Color Emoji", Font.PLAIN, 13)
                    : os.contains("win") ? new Font("Segoe UI Emoji", Font.PLAIN, 13)
                    : new Font("Noto Color Emoji", Font.PLAIN, 13);

            tierLabel.setFont(emojiFont);
            tierLabel.setText(updatedPost.getWriter().getTier());
        }
    }
}

