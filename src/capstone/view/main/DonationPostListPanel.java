package capstone.view.main;

import capstone.controller.DonationPostController;
import capstone.model.DonationPost;
import capstone.model.User;
import capstone.view.donation.DonationPostDetailView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DonationPostListPanel extends JPanel {
    private final DefaultListModel<DonationPost> listModel = new DefaultListModel<>();
    private final JList<DonationPost> postList = new JList<>(listModel);
    private final DonationPostController controller;

    public DonationPostListPanel(User loginUser, DonationPostController controller) {
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

        // 더블 클릭 시 상세보기
        postList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && postList.getSelectedValue() != null) {
                    DonationPost selected = postList.getSelectedValue();
                    new DonationPostDetailView(selected, loginUser, controller, () -> refreshList()).setVisible(true);
                }
            }
        });

        // 초기 로딩
        refreshList();
    }

    public void refreshList() {
        listModel.clear();
        for (DonationPost post : controller.getAllPosts()) {
            listModel.addElement(post);
        }
    }

}