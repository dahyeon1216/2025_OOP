package capstone.view;

import capstone.controller.DonationPostController;
import capstone.model.DonationPost;
import capstone.model.User;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MyDonationPostListPanel extends JFrame {
    public MyDonationPostListPanel(DonationPostController controller, User loginUser) {
        setTitle("내가 쓴 기부글");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        DefaultListModel<DonationPost> listModel = new DefaultListModel<>();
        JList<DonationPost> postList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(postList);

        JButton refreshBtn = new JButton("새로고침");
        refreshBtn.addActionListener(e -> {
            List<DonationPost> userPosts = controller.getPostsByUser(loginUser);
            listModel.clear();
            for (DonationPost post : userPosts) {
                listModel.addElement(post);
            }
        });

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(refreshBtn);

        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // 초기 로딩
        refreshBtn.doClick();
    }
}
