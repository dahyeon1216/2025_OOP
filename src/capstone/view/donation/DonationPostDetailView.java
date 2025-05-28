package capstone.view.donation;

import capstone.controller.DonationPostController;
import capstone.model.DonationPost;
import capstone.model.Tier;
import capstone.model.User;
import capstone.service.DonationPostService;
import capstone.view.BaseView;

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

    public DonationPostDetailView(DonationPost post,
                                  User loginUser,
                                  DonationPostController controller,
                                  Runnable refreshAction,
                                  JFrame previousView) {
        super(post.getTitle(), previousView); // ← 여기도 바꾸고

        setLayout(null);

        // 헤더
        JPanel header = createHeader(post.getTitle()); // ← 여기도 바꿈
        header.setBounds(0, 0, 393, 45);
        add(header);

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

        add(imageLabel);

        // 프로필 영역
        JPanel profilePanel = new JPanel(null);
        profilePanel.setBounds(0, 440, 393, 100); // 사진 영역(393) + 헤더(45) 아래
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


        add(profilePanel);


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
    }

     */


}

