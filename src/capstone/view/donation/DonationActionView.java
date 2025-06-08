package capstone.view.donation;

import capstone.controller.DonationPostController;
import capstone.controller.ScrapController;
import capstone.model.DonationPost;
import capstone.model.User;
import capstone.view.BaseView;
import capstone.view.Roundborder.RoundedButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class DonationActionView extends BaseView {

    private int selectedPoint = 0;

    public DonationActionView(DonationPost post, User loginUser,
                              DonationPostController donationPostController, ScrapController scrapController,
                              Runnable onPostUpdated) {
        super("기부하기");

        getContentPane().setLayout(new BorderLayout());

        // 헤더 추가
        add(createHeader("기부하기"), BorderLayout.NORTH);

        JPanel mainPanel  = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // 중앙 컨텐츠 (PostPanel, 구분선 , mainPointOptionsPanel)
        JPanel centerContentPanel = new JPanel();
        centerContentPanel.setLayout(new BoxLayout(centerContentPanel, BoxLayout.Y_AXIS));
        centerContentPanel.setBackground(Color.WHITE);
        centerContentPanel.setBorder(new EmptyBorder(10, 20, 10, 20));

        //1. PostPanel 생성 및 구현
        JPanel postPanel = new JPanel(new BorderLayout());
        postPanel.setBackground(Color.WHITE);
        postPanel.setBorder(new EmptyBorder(0, 0, 0, 0));

        // 이미지 영역
        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);

        int imageHeight = 100;
        int imageWidth = 100;

        if (post.getDonationImg() != null && !post.getDonationImg().isEmpty()) {
            ImageIcon imageIcon = new ImageIcon(post.getDonationImg());
            Image scaled = imageIcon.getImage().getScaledInstance(imageWidth, imageHeight, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(scaled));
        } else {
            imageLabel.setPreferredSize(new Dimension(imageWidth, imageHeight));
            imageLabel.setOpaque(true);
            imageLabel.setBackground(Color.LIGHT_GRAY);
            imageLabel.setText("사진 없음");
            imageLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        }

        JPanel imagePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        imagePanel.setBackground(Color.WHITE);
        imagePanel.add(imageLabel);
        imagePanel.setPreferredSize(new Dimension(imageWidth, imageHeight));
        imagePanel.setMaximumSize(new Dimension(imageWidth, imageHeight));
        imagePanel.setMinimumSize(new Dimension(imageWidth, imageHeight));

        postPanel.add(imagePanel, BorderLayout.WEST);

        // 타이틀 및 사용자 닉네임 영역
        JPanel textAndUserPanel = new JPanel();
        textAndUserPanel.setLayout(new BoxLayout(textAndUserPanel, BoxLayout.Y_AXIS));
        textAndUserPanel.setBackground(Color.WHITE);
        textAndUserPanel.setBorder(new EmptyBorder(0, 10, 0, 0));
        textAndUserPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // 타이틀 라벨
        JLabel titleLabel = new JLabel(post.getTitle());
        titleLabel.setFont(customFont.deriveFont(Font.BOLD, 25f));
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        textAndUserPanel.add(titleLabel);

        textAndUserPanel.add(Box.createVerticalStrut(15));

        // 사용자의 닉네임 라벨 추가
        JLabel userNicknameLabel = new JLabel(loginUser.getNickName());
        userNicknameLabel.setFont(customFont.deriveFont(Font.BOLD, 22f));
        userNicknameLabel.setForeground(Color.GRAY);
        userNicknameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        textAndUserPanel.add(userNicknameLabel);

        postPanel.add(textAndUserPanel, BorderLayout.CENTER); // 텍스트 및 사용자 닉네임 영역을 중앙에 배치

        int postPanelHeight = imageHeight + 20;
        postPanel.setPreferredSize(new Dimension(getWidth(), postPanelHeight));
        postPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, postPanelHeight));
        postPanel.setMinimumSize(new Dimension(0, postPanelHeight));

        centerContentPanel.add(postPanel);
        centerContentPanel.add(Box.createVerticalStrut(3));

        // 구분선
        JPanel separator = new JPanel();
        separator.setPreferredSize(new Dimension(Integer.MAX_VALUE, 1));
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        separator.setBackground(new Color(220, 220, 220));
        centerContentPanel.add(separator);

        // 구분선과 mainPointOptionsPanel 사이의 간격
        centerContentPanel.add(Box.createVerticalStrut(10));

        //2. mainPointOptionPanel
        JPanel mainPointOptionsPanel = new JPanel();
        mainPointOptionsPanel.setLayout(new BoxLayout(mainPointOptionsPanel, BoxLayout.Y_AXIS));
        mainPointOptionsPanel.setBackground(Color.WHITE);
        mainPointOptionsPanel.setBorder(new EmptyBorder(0, 0, 0, 0));

        // 기부 포인트 라벨 (현재 포인트 표시)
        JPanel currentPointWrapper = new JPanel(new BorderLayout());
        currentPointWrapper.setBackground(Color.WHITE);
        currentPointWrapper.setBorder(new EmptyBorder(0, 20, 0, 20));

        currentPointWrapper.setPreferredSize(new Dimension(Integer.MAX_VALUE, 60)); // 적절한 높이 설정 (예: 40px)
        currentPointWrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        JLabel pointTextLabel = new JLabel("현재 기부 포인트");
        pointTextLabel.setFont(customFont.deriveFont(Font.BOLD, 26));
        pointTextLabel.setForeground(Color.GRAY);
        currentPointWrapper.add(pointTextLabel, BorderLayout.WEST);

        JLabel currentDonationPointLabel = new JLabel("0 P"); // 초기값을 "0 P"로 변경
        currentDonationPointLabel.setFont(customFont.deriveFont(Font.BOLD, 26f));
        currentDonationPointLabel.setForeground(new Color(60, 60, 60));
        currentDonationPointLabel.setBorder(new EmptyBorder(0, 0, 10 , 5));
        currentPointWrapper.add(currentDonationPointLabel, BorderLayout.EAST);

        mainPointOptionsPanel.add(currentPointWrapper);
        mainPointOptionsPanel.add(Box.createVerticalStrut(10));

        // 숫자 옵션 버튼
        JPanel buttonGridPanel = new JPanel(new GridLayout(2, 3, 10, 10));
        buttonGridPanel.setBackground(Color.WHITE);
        int[] points = {1000, 3000, 5000, 10000, 30000, 50000};
        for (int p : points) {
            JButton pointBtn = new RoundedButton(p + " P", new Color(240, 240, 240), 20);
            pointBtn.setFont(customFont.deriveFont(Font.BOLD, 24f));
            pointBtn.setPreferredSize(new Dimension(100, 35));
            pointBtn.setForeground(Color.BLACK);

            /*//숫자 옵션 버튼 액션 리스너
            pointBtn.addActionListener(e -> {
                selectedPoint = p;
                currentDonationPointLabel.setText(selectedPoint + " P");
                // 버튼 색상을 초기화하는 헬퍼 메서드 사용
                resetButtonColors(buttonGridPanel); // buttonGridPanel이 final 또는 effectively final 이어야 함
                ((RoundedButton) e.getSource()).setButtonColor(new Color(120, 230, 170));
            });*/
            buttonGridPanel.add(pointBtn);
        }

        mainPointOptionsPanel.add(buttonGridPanel);
        mainPointOptionsPanel.add(Box.createVerticalStrut(30));

        // 직접 입력 패널
        JPanel directInputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        directInputPanel.setBackground(Color.WHITE);
        JTextField directInputTextField = new JTextField(10);
        directInputTextField.setFont(customFont.deriveFont(Font.PLAIN, 23f));
        directInputTextField.setHorizontalAlignment(JTextField.RIGHT);
        directInputPanel.add(directInputTextField);

        //직접 입력 버튼 생성
        JButton directInputButton = new RoundedButton("직접 입력", new Color(60, 60, 60), 20);
        directInputButton.setFont(customFont.deriveFont(Font.BOLD, 23f));
        directInputButton.setPreferredSize(new Dimension(130, 40));

        /*//직접입력버튼 액션 리스너
        directInputButton.addActionListener(e -> {
            try {
                int inputPoint = Integer.parseInt(directInputTextField.getText());
                if (inputPoint < 0) {
                    JOptionPane.showMessageDialog(this, "0 이상의 포인트를 입력해주세요.", "입력 오류", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                selectedPoint = inputPoint;
                currentDonationPointLabel.setText(selectedPoint + " P");
                directInputTextField.setText("");
                resetButtonColors(buttonGridPanel);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "유효한 숫자를 입력해주세요.", "입력 오류", JOptionPane.WARNING_MESSAGE);
            }
        });*/
        directInputPanel.add(directInputButton);

        //초기화 버튼 생성
        JButton clearInputButton = new RoundedButton("초기화", new Color(180, 180, 180), 20);
        clearInputButton.setFont(customFont.deriveFont(Font.BOLD, 20f));
        clearInputButton.setPreferredSize(new Dimension(90, 40));
        clearInputButton.setForeground(Color.WHITE);

        /*//초기화 버튼 액션 리스너
        clearInputButton.addActionListener(e -> {
            int selectedPoint = 0;
            currentDonationPointLabel.setText("0 P");
            directInputTextField.setText("");
            resetButtonColors(buttonGridPanel); // buttonGridPanel이 final 또는 effectively final 이어야 함
        });*/
        directInputPanel.add(clearInputButton);

        mainPointOptionsPanel.add(directInputPanel);
        mainPointOptionsPanel.add(Box.createVerticalStrut(5));

        //익명 기부하기 체크 박스
        JCheckBox anonymousCheckBox = new JCheckBox("익명으로 기부하기");
        anonymousCheckBox.setBackground(Color.WHITE);
        anonymousCheckBox.setFont(customFont.deriveFont(Font.BOLD, 22f));
        anonymousCheckBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPointOptionsPanel.add(anonymousCheckBox);

        centerContentPanel.add(mainPointOptionsPanel);
        centerContentPanel.add(Box.createVerticalGlue());
        mainPanel.add(centerContentPanel, BorderLayout.CENTER);

        //기부하기 버튼 패널
        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setBorder(new EmptyBorder(5, 0, 20, 0));
        buttonPanel.setOpaque(false);

        JPanel buttonWrapper = new JPanel();
        buttonWrapper.setBackground(Color.WHITE);
        buttonWrapper.setBorder(new EmptyBorder(30, 0, 0, 0));
        buttonWrapper.setLayout(new BoxLayout(buttonWrapper, BoxLayout.Y_AXIS));
        buttonWrapper.setBorder(new EmptyBorder(0, 0, 20, 0));

        //기부하기 버튼 생성
        RoundedButton donateButton = new RoundedButton("기부하기", new Color(60, 60, 60), 30);
        donateButton.setFont(customFont.deriveFont(Font.BOLD, 20f));

        int desiredButtonWidth = 353;
        donateButton.setPreferredSize(new Dimension(desiredButtonWidth, 44));
        donateButton.setMaximumSize(new Dimension(desiredButtonWidth, 44));
        donateButton.setMinimumSize(new Dimension(desiredButtonWidth, 44));
        donateButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        //기부하기 버튼 액션 리스너
        donateButton.addActionListener(e -> {
            if (selectedPoint <= 0) {
                JOptionPane.showMessageDialog(this, "기부할 포인트를 입력하거나 선택해주세요.", "기부 오류", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (loginUser != null && selectedPoint > loginUser.getPoint()) {
                JOptionPane.showMessageDialog(this, "포인트가 부족합니다. 현재 포인트: " + loginUser.getPoint() + "P", "기부 오류", JOptionPane.WARNING_MESSAGE);
                return;
            }

            boolean isAnonymous = anonymousCheckBox.isSelected();

            //여기서 DonationActionCompleteview화면으로 넘어가고 기부한 포인트도 넘어갈 수 있게 해주세요
            boolean success = donationPostController.donate(post, loginUser, selectedPoint);
            if (success) {
                JOptionPane.showMessageDialog(this, selectedPoint + "P 기부 완료!", "기부 성공", JOptionPane.INFORMATION_MESSAGE);
                if (onPostUpdated != null) {
                    onPostUpdated.run();
                }
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "기부 처리 중 오류가 발생했습니다.", "기부 실패", JOptionPane.ERROR_MESSAGE);
            }
        });

        buttonWrapper.add(Box.createVerticalGlue());
        buttonWrapper.add(donateButton);
        mainPanel.add(buttonWrapper, BorderLayout.SOUTH);
        buttonWrapper.add(Box.createVerticalGlue());

        add(mainPanel, BorderLayout.CENTER);
    }

    // 버튼 색상을 초기화하는 헬퍼 메서드 (createPointPanel 내부에서 사용)
    private void resetButtonColors(JPanel buttonPanel) {
        for (Component comp : buttonPanel.getComponents()) {
            if (comp instanceof RoundedButton) {
                ((RoundedButton) comp).setButtonColor(new Color(240, 240, 240));
                ((RoundedButton) comp).setForeground(Color.BLACK); // 텍스트 색상도 초기화
            }
        }
    }
}
