package capstone.view.donation;

import capstone.controller.DonationPostController;
import capstone.controller.ScrapController;
import capstone.model.DonationPost;
import capstone.model.User;

import javax.swing.*;
import java.awt.*;

public class DonationPostDetailView extends JFrame {
    public DonationPostDetailView(DonationPost post, User loginUser, DonationPostController donationPostController, ScrapController scrapController, Runnable onPostUpdated) {
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

        // 공통 버튼 패널 (왼쪽 정렬)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        // 스크랩 버튼
        if (loginUser != null) {
            JButton scrapButton = new JButton(
                    scrapController.isScrapped(loginUser, post) ? "스크랩 취소" : "스크랩"
            );
            scrapButton.addActionListener(e -> {
                scrapController.toggleScrap(loginUser, post);
                scrapButton.setText(
                        scrapController.isScrapped(loginUser, post) ? "스크랩 취소" : "스크랩"
                );
                if (onPostUpdated != null) onPostUpdated.run(); // 스크랩 후 갱신
            });
            buttonPanel.add(scrapButton);
        }

        // 수정/삭제 버튼 (작성자일 때만)
        if (post.getWriter() != null && post.getWriter().equals(loginUser)) {
            panel.add(new JLabel("가상계좌: " + (post.getVirtualAccount() != null ? post.getVirtualAccount() : "없음")));

            JButton editBtn = new JButton("수정");
            JButton deleteBtn = new JButton("삭제");

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

            buttonPanel.add(editBtn);
            buttonPanel.add(deleteBtn);
        }

        // 버튼 패널 전체에 추가
        panel.add(buttonPanel);

        add(panel);
    }
}
