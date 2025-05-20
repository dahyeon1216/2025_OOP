package capstone.view;

import capstone.auth.LoginSession;
import capstone.controller.UserController;
import capstone.model.DonationPost;
import capstone.model.User;
import capstone.service.DonationPostService;
import capstone.service.UserService;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DonationPostView extends JFrame{
    private final DefaultListModel<DonationPost> listModel = new DefaultListModel<>();
    private final JList<DonationPost> postList = new JList<>(listModel);
    private final User loginUser;
    private final UserService userService;


    public DonationPostView(User loginUser, DonationPostService donationPostService, UserService userService) {
        this.loginUser = loginUser;
        this.userService = userService;

        if (this.loginUser == null) {
            JOptionPane.showMessageDialog(null, "로그인이 필요합니다.", "접근 제한", JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }

        setTitle("기부글 관리(userId=" + loginUser.getUserId() + ")");
        setSize(600, 500);
        setLocationRelativeTo(null);

        // 메뉴바 추가
        JMenuBar menuBar = new JMenuBar();
        JMenu userMenu = new JMenu("내 정보");
        JMenuItem viewProfile = new JMenuItem("회원 정보 조회");
        JMenuItem editProfile = new JMenuItem("회원 정보 수정");
        JMenuItem chargePoint = new JMenuItem("포인트 충전");
        userMenu.add(viewProfile);
        userMenu.add(editProfile);
        userMenu.add(chargePoint);
        menuBar.add(userMenu);
        setJMenuBar(menuBar);

        viewProfile.addActionListener(e -> {
            new UserProfileView(loginUser, new UserController(userService)).setVisible(true);
        });

        editProfile.addActionListener(e -> {
            new UserProfileEditView(loginUser, new UserController(userService)).setVisible(true);
        });

        chargePoint.addActionListener(e -> {
            new PointChargeView(loginUser).setVisible(true);
        });

        JTextField donationImgField = new JTextField();
        JTextField titleField = new JTextField();
        JTextField goalPointField = new JTextField();
        JTextField endAtField = new JTextField(); // yyyy-MM-dd
        JTextArea contentArea = new JTextArea(3, 20);
        JButton writeBtn = new JButton("글쓰기");
        JButton deleteBtn = new JButton("삭제");
        JButton editBtn = new JButton("수정");

        JPanel inputPanel = new JPanel(new GridLayout(6, 2));
        inputPanel.add(new JLabel("이미지 경로:"));
        inputPanel.add(donationImgField);
        inputPanel.add(new JLabel("제목:"));
        inputPanel.add(titleField);
        inputPanel.add(new JLabel("목표 금액:"));
        inputPanel.add(goalPointField);
        inputPanel.add(new JLabel("마감일 (yyyy-MM-dd):"));
        inputPanel.add(endAtField);
        inputPanel.add(new JLabel("내용:"));
        inputPanel.add(new JScrollPane(contentArea));
        inputPanel.add(new JLabel());
        inputPanel.add(writeBtn);

        add(inputPanel, BorderLayout.NORTH);
        add(new JScrollPane(postList), BorderLayout.CENTER);

        JPanel actionPanel = new JPanel();
        actionPanel.add(editBtn);
        actionPanel.add(deleteBtn);
        add(actionPanel, BorderLayout.SOUTH);


        writeBtn.addActionListener(e -> {
            try {
                String donationImg = donationImgField.getText();
                String title = titleField.getText();
                int goalPoint = Integer.parseInt(goalPointField.getText());
                LocalDate endAt = LocalDate.parse(endAtField.getText());
                String content = contentArea.getText();

                donationPostService.create(loginUser, donationImg, goalPoint, endAt, title, content);
                refreshList(donationPostService);
                JOptionPane.showMessageDialog(this, "글 등록 성공");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "입력 오류: " + ex.getMessage());
            }
        });

        deleteBtn.addActionListener(e -> {
            DonationPost selected = postList.getSelectedValue();
            if (selected != null && selected.getWriter().getUserId().equals(loginUser.getUserId())) {
                donationPostService.delete(selected.getId());
                refreshList(donationPostService);
            } else {
                JOptionPane.showMessageDialog(this, "본인만 삭제 가능");
            }
        });


        editBtn.addActionListener(e -> {
            DonationPost selected = postList.getSelectedValue();
            if (selected != null && selected.getWriter().getUserId().equals(loginUser.getUserId())) {
                String newTitle = JOptionPane.showInputDialog("새 제목", selected.getTitle());
                String newContent = JOptionPane.showInputDialog("새 내용", selected.getContent());
                if (newTitle != null && newContent != null) {
                    selected.setTitle(newTitle);
                    selected.setContent(newContent);
                    refreshList(donationPostService);
                }
            } else {
                JOptionPane.showMessageDialog(this, "본인만 수정 가능");
            }
        });

        // 더블 클릭 시 기부글 상세 보기
        postList.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    DonationPost selected = postList.getSelectedValue();
                    if (selected != null) {
                        String message = String.format(
                                "제목: %s\n내용: %s\n작성자: %s\n모금목표: %,d\n현재 모금: %,d\n만료일: %s\n작성일: %s",
                                selected.getTitle(),
                                selected.getContent(),
                                selected.getWriter().getUserId(),
                                selected.getGoalPoint(),
                                selected.getRaisedPoint(),
                                selected.getEndAt(),
                                selected.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
                        );
                        JOptionPane.showMessageDialog(null, message, "기부글 상세정보", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        });

        refreshList(donationPostService);
    }

    private void refreshList(DonationPostService service) {
        listModel.clear();
        for (DonationPost post : service.getAll()) {
            listModel.addElement(post);
        }
    }

}
