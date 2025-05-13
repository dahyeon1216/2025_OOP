package capstone.view;

import capstone.auth.LoginSession;
import capstone.model.DonationPost;
import capstone.model.User;
import capstone.service.DonationPostService;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DonationPostView extends JFrame{
    private final DefaultListModel<DonationPost> listModel = new DefaultListModel<>();
    private final JList<DonationPost> postList = new JList<>(listModel);

    public DonationPostView(DonationPostService donationPostService) {
        // ✅ 수정된 부분: 사용자 입력 필드 추가
        JTextField donationImgField = new JTextField();
        JTextField titleField = new JTextField();
        JTextField goalPointField = new JTextField();
        JTextField endAtField = new JTextField(); // yyyy-MM-dd
        JTextArea contentArea = new JTextArea(3, 20);
        JButton writeBtn = new JButton("글쓰기");
        JButton deleteBtn = new JButton("삭제");
        JButton editBtn = new JButton("수정");

        // ✅ 수정된 부분: 레이아웃 구성 변경 (6행 → 이미지, 제목, 금액, 마감일, 내용, 버튼)
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
            if (!LoginSession.isLoggedIn()) {
                JOptionPane.showMessageDialog(this, "로그인이 필요합니다.");
                return;
            }

            try {
                String donationImg = donationImgField.getText();
                String title = titleField.getText();
                int goalPoint = Integer.parseInt(goalPointField.getText());
                LocalDate endAt = LocalDate.parse(endAtField.getText());
                String content = contentArea.getText();

                User currentUser = LoginSession.getCurrentUser();
                donationPostService.create(currentUser, donationImg, goalPoint, endAt, title, content);
                refreshList(donationPostService);

                JOptionPane.showMessageDialog(this, "기부글 등록 완료");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "입력 오류: " + ex.getMessage());
            }
        });

        deleteBtn.addActionListener(e -> {
            DonationPost selected = postList.getSelectedValue();
            if (selected != null && selected.getWriter().equals(LoginSession.getCurrentUser())) {
                donationPostService.delete(selected.getId());
                refreshList(donationPostService);
            } else {
                JOptionPane.showMessageDialog(this, "본인만 삭제 가능");
            }
        });

        editBtn.addActionListener(e -> {
            DonationPost selected = postList.getSelectedValue();
            if (selected != null && selected.getWriter().equals(LoginSession.getCurrentUser())) {
                String newTitle = JOptionPane.showInputDialog("새 제목", selected.getTitle());
                String newContent = JOptionPane.showInputDialog("새 내용", selected.getContent());
                selected.setTitle(newTitle);
                selected.setContent(newContent);
                refreshList(donationPostService);
            } else {
                JOptionPane.showMessageDialog(this, "본인만 수정 가능");
            }
        });

        // 🔍 더블 클릭 시 상세 보기
        postList.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    DonationPost selected = postList.getSelectedValue();
                    if (selected != null) {
                        String message = String.format(
                                "제목: %s\n내용: %s\n작성자: %s\n목표금액: %,d원\n모금액: %,d원\n이미지: %s\n작성일시: %s",
                                selected.getTitle(),
                                selected.getContent(),
                                selected.getWriter().getUserId(),
                                selected.getGoalPoint(),
                                selected.getRaisedPoint(),
                                selected.getDonationImg(),
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
