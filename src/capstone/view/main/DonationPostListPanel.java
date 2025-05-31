package capstone.view.main;

import capstone.controller.DonationPostController;
import capstone.controller.ScrapController;
import capstone.model.DonationPost;
import capstone.model.User;
import capstone.view.donation.DonationPostDetailView;
import capstone.view.donation.DonationPostPanelFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class DonationPostListPanel extends JPanel {
    private final DefaultListModel<DonationPost> listModel = new DefaultListModel<>();
    private final JList<DonationPost> postList = new JList<>(listModel);
    private final User loginUser;
    private final DonationPostController controller;
    private final ScrapController scrapController;

    private JPanel postListPanel;

    public DonationPostListPanel(User loginUser,
                                 DonationPostController controller,
                                 ScrapController scrapController) {
        this.loginUser = loginUser;
        this.controller = controller;
        this.scrapController = scrapController;

        setLayout(new BorderLayout());

        // 리스트 UI
        postList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(postList);
        add(scrollPane, BorderLayout.CENTER);

        // 포스트 목록 패널
        postListPanel = new JPanel(new BorderLayout());
        add(postListPanel, BorderLayout.CENTER);

        refresh(); // 초기 로딩
    }

    public void refresh() {
        postListPanel.removeAll();

        List<DonationPost> posts = controller.getAllPosts();

        Runnable onPostUpdated = this::refresh;

        JPanel listPanel = DonationPostPanelFactory.createPostListPanel(
                "전체 기부글 목록", posts, loginUser, controller, scrapController, onPostUpdated
        );

        postListPanel.add(listPanel, BorderLayout.CENTER);
        postListPanel.revalidate();
        postListPanel.repaint();
    }
}
