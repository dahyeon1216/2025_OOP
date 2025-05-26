package capstone.view.donation;

import capstone.controller.DonationPostController;
import capstone.model.DonationPost;
import capstone.model.User;

import javax.swing.*;
import java.awt.*;

public class DonationPostDetailView extends JFrame {
    public DonationPostDetailView(DonationPost post, User loginUser, DonationPostController donationPostController, Runnable onPostUpdated) {
        setTitle("기부글 상세 보기");
        setSize(500, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(new JLabel("제목: " + post.getTitle()));
        panel.add(new JLabel("작성자: " + (post.getWriter() != null ? post.getWriter().getUserId() : "익명")));
        panel.add(new JLabel("내용: "));

        JTextArea contentArea = new JTextArea(post.getContent());
        contentArea.setEditable(false);
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(contentArea);
        scrollPane.setPreferredSize(new Dimension(400, 150));
        panel.add(scrollPane);

        panel.add(new JLabel("이미지: " + post.getDonationImg()));
        panel.add(new JLabel("목표 금액: " + post.getGoalPoint() + "P"));
        panel.add(new JLabel("마감일: " + (post.getEndAt() != null ? post.getEndAt().toString() : "없음")));

        if (post.getWriter() != null && post.getWriter().equals(loginUser)) {
//            panel.add(Box.createVerticalStrut(10)); // 여백
            panel.add(new JLabel("가상계좌: " + (post.getVirtualAccount() != null ? post.getVirtualAccount() : "없음")));

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

            JButton editBtn = new JButton("수정");
            JButton deleteBtn = new JButton("삭제");

            buttonPanel.add(editBtn);
            buttonPanel.add(deleteBtn);
            panel.add(buttonPanel);

            editBtn.addActionListener(e -> {
                new DonationPostEditView(post, loginUser, donationPostController, () -> {
                    JOptionPane.showMessageDialog(this, "기부글이 수정되었습니다.");
                    if (onPostUpdated != null) onPostUpdated.run();
                    dispose();
                }).setVisible(true);
            });

            deleteBtn.addActionListener(e -> {
                int confirm = JOptionPane.showConfirmDialog(this, "정말 삭제하시겠습니까?");
                if (confirm == JOptionPane.YES_OPTION) {
                    donationPostController.deletePost(post.getId());
                    JOptionPane.showMessageDialog(this, "삭제 완료");
                    if (onPostUpdated != null) onPostUpdated.run();
                    dispose();
                }
            });


        }

        add(panel);
    }
}
