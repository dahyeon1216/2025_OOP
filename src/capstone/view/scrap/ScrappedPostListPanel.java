package capstone.view.scrap;

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

public class ScrappedPostListPanel extends JPanel {
    private final DefaultListModel<DonationPost> listModel = new DefaultListModel<>();
    private final JList<DonationPost> postList = new JList<>(listModel);
    private final User loginUser;
    private final DonationPostController donationPostController;
    private final ScrapController scrapController;

    private JPanel postListPanel;

    public ScrappedPostListPanel(User loginUser,
                                 DonationPostController donationPostController,
                                 ScrapController scrapController) {
        this.loginUser = loginUser;
        this.donationPostController = donationPostController;
        this.scrapController = scrapController;

        setLayout(new BorderLayout());

        // 기부글 목록 패널
        postListPanel = new JPanel(new BorderLayout());
        add(postListPanel, BorderLayout.CENTER);

        refresh(); // 초기 로딩
    }
    public void refresh() {
        postListPanel.removeAll();

        List<DonationPost> posts = scrapController.getScrappedPosts(loginUser);

        Runnable onPostUpdated = this::refresh; // 상세 보기에서 스크랩 취소 시 자동 새로고침

        JPanel listPanel = DonationPostPanelFactory.createPostListPanel(
                "스크랩한 기부글 목록", posts, loginUser, donationPostController, scrapController, onPostUpdated
        );

        postListPanel.add(listPanel, BorderLayout.CENTER);
        postListPanel.revalidate();
        postListPanel.repaint();
    }
}
