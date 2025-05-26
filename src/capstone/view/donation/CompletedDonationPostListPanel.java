package capstone.view.donation;

import capstone.controller.DonationPostController;
import capstone.model.DonationPost;
import capstone.model.User;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CompletedDonationPostListPanel extends JPanel {

    public CompletedDonationPostListPanel(User loginUser, DonationPostController controller) {
        setLayout(new BorderLayout());

        List<DonationPost> posts = controller.getAllPosts().stream()
                .filter(DonationPost::isCompleted)
                .toList();

        JPanel listPanel = DonationPostPanelFactory.createPostListPanel(
                "진행 완료된 기부글 목록",
                posts,
                loginUser,
                controller
        );

        add(listPanel, BorderLayout.CENTER);
    }
}
