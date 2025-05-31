package capstone.view.main;

import capstone.controller.DonationPostController;
import capstone.controller.ScrapController;
import capstone.model.DonationPost;
import capstone.model.User;
import capstone.view.donation.DonationPostPanelFactory;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MyDonationPostListPanel extends JPanel {
    private final DefaultListModel<DonationPost> listModel = new DefaultListModel<>();
    private final JList<DonationPost> postList = new JList<>(listModel);
    private final User loginUser;
    private final DonationPostController controller;
    private final ScrapController scrapController;

    private JPanel postListPanel;

    public MyDonationPostListPanel(DonationPostController controller,
                                   ScrapController scrapController,
                                   User loginUser) {
        this.loginUser = loginUser;
        this.controller = controller;
        this.scrapController = scrapController;

        setLayout(new BorderLayout());

        postListPanel = new JPanel(new BorderLayout());
        add(postListPanel, BorderLayout.CENTER);

        refresh();

    }

    public void refresh() {
        postListPanel.removeAll();

        List<DonationPost> posts = controller.getAllPosts().stream()
                .filter(post -> post.getWriter() != null && post.getWriter().equals(loginUser)) // ✅ 내가 쓴 글만
                .toList();

        Runnable onPostUpdated = this::refresh;

        JPanel listPanel = DonationPostPanelFactory.createPostListPanel(
                "내가 작성한 기부글 목록", posts, loginUser, controller, scrapController, onPostUpdated
        );

        postListPanel.add(listPanel, BorderLayout.CENTER);
        postListPanel.revalidate();
        postListPanel.repaint();
    }
}
