package capstone.view;

import capstone.controller.DonationPostController;
import capstone.model.DonationPost;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class DonationPostListPanel extends JPanel {
    private final DefaultListModel<DonationPost> listModel = new DefaultListModel<>();
    private final JList<DonationPost> postList = new JList<>(listModel);
    private final DonationPostController controller;

    public DonationPostListPanel(DonationPostController controller) {
        this.controller = controller;
        setLayout(new BorderLayout());

        // 리스트 UI
        postList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(postList);
        add(scrollPane, BorderLayout.CENTER);

        // 새로고침 버튼
        JButton refreshBtn = new JButton("새로고침");
        refreshBtn.addActionListener(e -> refreshList());
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(refreshBtn);
        add(bottomPanel, BorderLayout.SOUTH);

        // 초기 로딩
        refreshList();
    }

    public void refreshList() {
        List<DonationPost> posts = controller.getAllPosts();
        listModel.clear();
        for (DonationPost post : posts) {
            listModel.addElement(post);
        }
    }
}