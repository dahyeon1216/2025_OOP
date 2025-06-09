package capstone.view.donation;

//기부글 진행중/ 진행완료 세부 조회
//피그마-기부글_세부내역P

import capstone.controller.DonationPostController;
import capstone.controller.ScrapController;
import capstone.model.DonationPost;
import capstone.model.Tier;
import capstone.model.User;
import capstone.model.VirtualAccount;
import capstone.service.DonationPostService;
import capstone.service.ScrapService;
import capstone.view.BaseView;
import capstone.view.style.RoundedBorder;
import capstone.view.style.RoundedButton;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

import static capstone.model.BankType.KB;
import static capstone.model.BankType.SHINHAN;

public class DonationPostDetailView extends BaseView {
    public DonationPostDetailView(DonationPost post, User loginUser, DonationPostController donationPostController, ScrapController scrapController, Runnable onPostUpdated) {
        super(post.getTitle());

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
            JMenuItem settleMenuItem = new JMenuItem("정산하기");

            popupMenu.add(editMenuItem);
            popupMenu.add(deleteMenuItem);

            //수정 기능 Item 액션 리스너
            editMenuItem.addActionListener(e -> {
                new DonationPostEditView(post, loginUser, donationPostController, () -> {
                    JOptionPane.showMessageDialog(this, "기부글이 수정되었습니다.");
                    if (onPostUpdated != null) onPostUpdated.run();
                    dispose();
                }).setVisible(true);
            });

            //삭제 기능 Item 액션 리스너
            deleteMenuItem.addActionListener(e -> {
                int confirm = JOptionPane.showConfirmDialog(this, "정말 삭제하시겠습니까?");
                if (confirm == JOptionPane.YES_OPTION) {
                    donationPostController.deletePost(post.getId());
                    JOptionPane.showMessageDialog(this, "삭제 완료");
                    if (onPostUpdated != null) onPostUpdated.run();
                    dispose();
                }
            });

            //정산버튼 액션리스너
            //진행완료인 기부글인 경우에만 나오게
            if (post.isCompleted() && !post.isSettled()) {

                settleMenuItem.addActionListener(e -> {
                    boolean success = donationPostController.settlePost(post);
                    if (success) {
                        JOptionPane.showMessageDialog(this, "정산이 완료되었습니다. 포인트가 지급되었습니다.");

                        // 정산 후 사용내역 추가 버튼 활성화
                        VirtualAccount va = post.getVirtualAccount();
                        va.setRaisedPoint(post.getRaisedPoint());
                        va.setCurrentPoint(post.getRaisedPoint());
                        //usageButton.setEnabled(true);
                        //-> 이 부분은 아래 사용내역 코드에 존재해서 주석처리했습니다

                        onPostUpdated.run(); // 리스트 패널 새로고침
                        dispose(); // 상세창 닫기
                        new DonationPostDetailView(post, loginUser, donationPostController, scrapController, onPostUpdated).setVisible(true); // 새로 열기
                    } else {
                        JOptionPane.showMessageDialog(this, "정산에 실패했습니다.");
                    }
                });
                popupMenu.add(settleMenuItem);
            }
            header.add(optionButton);

            // optionButton 버튼 리스너: 옵션 버튼 클릭 시 팝업 메뉴 표시
            optionButton.addActionListener(e -> {
                popupMenu.show(optionButton, 0, optionButton.getHeight());
            });
        }

        mainPanel.add(header);

        // 2. 이미지 영역
        JLabel imageLabel = new JLabel();
        imageLabel.setBounds(0, 45, 393, 393);
        imageLabel.setIcon(loadImageOrDefault("resources/images/" + post.getDonationImg(), 393, 393));
        mainPanel.add(imageLabel);

        // 3. 프로필 영역
        JPanel profilePanel = new JPanel(null);
        profilePanel.setBounds(0, 445, 393, 100);
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
        JLabel tierLabel = new JLabel(loginUser.getTier() + "티어");
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

        //4. 본문 영역

        // 본문 전체를 감쌀 둥근 패널
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(null);
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBounds(15, 545, 348, 400);

        // 내용 텍스트
        JTextArea contentArea = new JTextArea(post.getContent());
        contentArea.setBounds(5, 0, 340, 250); // ← 좌우 여백 20px
        contentArea.setBorder(new RoundedBorder(15));
        contentArea.setBackground(new Color(240, 240, 240));
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        contentArea.setEditable(false); //읽기 전용
        contentArea.setFont(new Font("SansSerif", Font.PLAIN, 15));

        contentPanel.add(contentArea);


        //여기서 up 버튼은 '나의 기부글 패널'에서 구현해야할 것 같습니다!
        // 기부하기 버튼
        // 진행 중인 기부글일 때 & 로그인한 유저일 때만 보이게
        if (!post.isCompleted() && loginUser != null) {
            //JButton upButton = new JButton("UP 하기");

            // 기부하기 버튼 생성 및 설정
            RoundedButton donateButton = new RoundedButton("기부하기", new Color(60, 60, 60), 30);
            donateButton.setPreferredSize(new Dimension(0, 44));
            donateButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
            donateButton.setFont(customFont.deriveFont(Font.BOLD, 20f));
            donateButton.setForeground(Color.WHITE);
            donateButton.setBounds(245, 270, 100, 44);

            donateButton.addActionListener(e -> {
                String input = JOptionPane.showInputDialog(this, "기부할 포인트를 입력하세요:");
                if (input != null) {
                    try {
                        int amount = Integer.parseInt(input);
                        if (amount <= 0) throw new NumberFormatException();
                        boolean success = donationPostController.donate(post, loginUser, amount);
                        if (success) {
                            JOptionPane.showMessageDialog(this, "기부 완료!");
                            onPostUpdated.run(); // 화면 새로고침
                            dispose();

                            new DonationPostDetailView(post, loginUser, donationPostController, scrapController, onPostUpdated).setVisible(true);
                        } else {
                            JOptionPane.showMessageDialog(this, "기부 실패. 포인트 부족합니다.");
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "올바른 숫자를 입력하세요.");
                    }
                }
            });

           /* upButton.addActionListener(e -> {
                int confirm = JOptionPane.showConfirmDialog(this,
                        "300포인트를 사용하여 기부글을 상단에 노출하시겠습니까?",
                        "UP 하기 확인",
                        JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    boolean success = donationPostController.upPost(post, loginUser);
                    if (success) {
                        JOptionPane.showMessageDialog(this, "기부글이 상단에 노출되었습니다!");
                        onPostUpdated.run();
                    } else {
                        JOptionPane.showMessageDialog(this, "포인트가 부족합니다. 최소 300P가 필요합니다.");
                    }
                }
            })
            */

            contentPanel.add(donateButton);
        }

        // 사용내역 버튼
        // 진행완료된 포스트 && 로그인한 유저일 때만 보이게
        if (post.isCompleted() && loginUser != null) {

            // 사용내역 버튼 생성 및 설정
            RoundedButton usageButton = new RoundedButton("사용내역", new Color(60, 60, 60), 30);
            usageButton.setPreferredSize(new Dimension(0, 44));
            usageButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
            usageButton.setFont(customFont.deriveFont(Font.BOLD, 20f));
            usageButton.setForeground(Color.WHITE);
            usageButton.setBounds(245, 270, 100, 44);

            usageButton.setEnabled(post.isSettled()); // 정산 전에는 비활성화
            contentPanel.add(usageButton);
        }

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



}

