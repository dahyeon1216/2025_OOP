package capstone.view.donation;

import capstone.controller.DonationPostController;
import capstone.model.DonationPost;
import capstone.model.User;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class OngoingDonationPostListPanel extends JPanel {

    public OngoingDonationPostListPanel(User loginUser, DonationPostController controller) {
        setLayout(new BorderLayout());

        List<DonationPost> posts = controller.getAllPosts().stream()
                .filter(DonationPost::isInProgress)
                .toList();

        JPanel listPanel = DonationPostPanelFactory.createPostListPanel(
                "진행 중인 기부글 목록",
                posts,
                loginUser,
                controller
        );

        add(listPanel, BorderLayout.CENTER);
    }
}
