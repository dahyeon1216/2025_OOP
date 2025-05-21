package capstone.view.main;

import capstone.controller.DonationPostController;
import capstone.model.DonationPost;
import capstone.model.User;
import capstone.view.donation.DonationPostDetailView;
import capstone.view.donation.DonationPostWriteView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class MyDonationPostListPanel extends JPanel { // ✅ JFrame → JPanel로 변경
    private final DefaultListModel<DonationPost> listModel = new DefaultListModel<>();
    private final JList<DonationPost> postList = new JList<>(listModel);

    public MyDonationPostListPanel(DonationPostController controller, User loginUser) {
        setLayout(new BorderLayout());

        JScrollPane scrollPane = new JScrollPane(postList);

        JButton refreshBtn = new JButton("새로고침");
        refreshBtn.addActionListener(e -> {
            List<DonationPost> userPosts = controller.getPostsByUser(loginUser);
            listModel.clear();
            for (DonationPost post : userPosts) {
                listModel.addElement(post);
            }
        });

        // 더블 클릭 시 상세보기
        postList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && postList.getSelectedValue() != null) {
                    DonationPost selected = postList.getSelectedValue();
                    new DonationPostDetailView(selected, loginUser, controller, refreshBtn::doClick).setVisible(true);
                }
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

