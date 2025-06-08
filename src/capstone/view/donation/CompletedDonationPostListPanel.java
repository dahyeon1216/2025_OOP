package capstone.view.donation;

import capstone.controller.DonationPostController;
import capstone.controller.ScrapController;
import capstone.model.DonationPost;
import capstone.model.User;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CompletedDonationPostListPanel extends JPanel {
    private final User loginUser;
    private final DonationPostController controller;
    private final ScrapController scrapController;

    private JPanel postListPanel;

    public CompletedDonationPostListPanel(User loginUser,
                                          DonationPostController controller,
                                          ScrapController scrapController) {
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
                .filter(DonationPost::isCompleted) // 완료된 기부글만
                .toList();

        Runnable onPostUpdated = this::refresh;

        JPanel listPanel = DonationPostPanelFactory.createPostListPanel(
                "진행 완료된 기부글 목록", posts, loginUser, controller, scrapController, onPostUpdated
        );

        postListPanel.add(listPanel, BorderLayout.CENTER);
        postListPanel.revalidate();
        postListPanel.repaint();
    }
}
