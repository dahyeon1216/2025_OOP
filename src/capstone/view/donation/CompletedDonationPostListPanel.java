package capstone.view.donation;

import capstone.controller.DonationPostController;
import capstone.controller.ScrapController;
import capstone.model.DonationPost;
import capstone.model.User;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CompletedDonationPostListPanel extends JPanel {
    public CompletedDonationPostListPanel(User loginUser,
                                          DonationPostController controller,
                                          ScrapController scrapController) {
        setLayout(new BorderLayout());

        Runnable refresh = () -> {
            removeAll();
            List<DonationPost> posts = controller.getAllPosts().stream()
                    .filter(DonationPost::isCompleted)
                    .toList();
            add(DonationPostPanelFactory.createPostListPanel(
                    "진행 완료된 기부글 목록",
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
