package capstone.view;

import capstone.controller.DonationPostController;
import capstone.model.DonationPost;
import capstone.model.User;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class DonationPostListView extends JFrame {
    private final DefaultListModel<DonationPost> listModel = new DefaultListModel<>();
    private final JList<DonationPost> postList = new JList<>(listModel);

    public DonationPostListView(User loginUser, DonationPostController controller) {
        this(controller, loginUser);
    }

    public DonationPostListView(DonationPostController controller, User loginUser) {
        setTitle("기부글 목록");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        postList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(postList);

        JButton refreshBtn = new JButton("새로고침");
        JButton editBtn = new JButton("수정");
        JButton deleteBtn = new JButton("삭제");

        // 🔽 수정 & 삭제 버튼 패널 구성
        JPanel actionPanel = new JPanel();
        actionPanel.add(refreshBtn);
        actionPanel.add(editBtn);
        actionPanel.add(deleteBtn);

        add(scrollPane, BorderLayout.CENTER);
        add(actionPanel, BorderLayout.SOUTH);

        refreshBtn.addActionListener(e -> refreshList(controller));

        // 🔽 수정 버튼 동작
        editBtn.addActionListener(e -> {
            DonationPost selected = postList.getSelectedValue();
            if (selected == null || loginUser == null) return;

            if (!selected.getWriter().getUserId().equals(loginUser.getUserId())) {
                JOptionPane.showMessageDialog(this, "본인만 수정할 수 있습니다.");
                return;
            }

            String newTitle = JOptionPane.showInputDialog(this, "새 제목", selected.getTitle());
            String newContent = JOptionPane.showInputDialog(this, "새 내용", selected.getContent());

            if (newTitle != null && newContent != null) {
                boolean updated = controller.updatePost(selected.getId(), newTitle, newContent, loginUser);
                if (updated) {
                    JOptionPane.showMessageDialog(this, "수정 완료");
                    refreshList(controller);
                } else {
                    JOptionPane.showMessageDialog(this, "수정 실패");
                }
            }
        });

        // 🔽 삭제 버튼 동작
        deleteBtn.addActionListener(e -> {
            DonationPost selected = postList.getSelectedValue();
            if (selected == null || loginUser == null) return;

            if (!selected.getWriter().getUserId().equals(loginUser.getUserId())) {
                JOptionPane.showMessageDialog(this, "본인만 삭제할 수 있습니다.");
                return;
            }

            int result = JOptionPane.showConfirmDialog(this, "정말 삭제하시겠습니까?", "삭제 확인", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                boolean deleted = controller.deletePost(selected.getId(), loginUser);
                if (deleted) {
                    JOptionPane.showMessageDialog(this, "삭제 완료");
                    refreshList(controller);
                } else {
                    JOptionPane.showMessageDialog(this, "삭제 실패");
                }
            }
        });

        refreshList(controller);
    }

    private void refreshList(DonationPostController controller) {
        List<DonationPost> posts = controller.getAllPosts();
        listModel.clear();
        for (DonationPost post : posts) {
            listModel.addElement(post);
        }
    }
}
