package capstone.view.donation;

import capstone.controller.DonationPostController;
import capstone.controller.ScrapController;
import capstone.model.DonationPost;
import capstone.model.User;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class OngoingDonationPostListPanel extends JPanel {
    public OngoingDonationPostListPanel(User loginUser,
                                        DonationPostController controller,
                                        ScrapController scrapController) {
        setLayout(new BorderLayout());

        Runnable refresh = () -> {
            removeAll();
            List<DonationPost> posts = controller.getAllPosts().stream()
                    .filter(post -> !post.isCompleted())
                    .toList();
            add(DonationPostPanelFactory.createPostListPanel(
                    "진행 중인 기부글 목록",
                    posts,
                    loginUser,
                    controller,
                    scrapController,
                    this::revalidateAndRepaint // 목록 갱신 콜백
            ), BorderLayout.CENTER);
            revalidate();
            repaint();
        };

        refresh.run();
    }

    private void revalidateAndRepaint() {
        revalidate();
        repaint();
    }
}
