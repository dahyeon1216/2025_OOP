package capstone.view.scrap;

import capstone.controller.DonationPostController;
import capstone.controller.ScrapController;
import capstone.model.DonationPost;
import capstone.model.User;
import capstone.view.donation.DonationPostDetailView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class ScrappedPostListPanel extends JPanel {
    private final DefaultListModel<DonationPost> listModel = new DefaultListModel<>();
    private final JList<DonationPost> postList = new JList<>(listModel);

    public ScrappedPostListPanel(User loginUser,
                                 DonationPostController donationPostController,
                                 ScrapController scrapController) {
        setLayout(new BorderLayout());

        // 스크롤 가능한 리스트
        JScrollPane scrollPane = new JScrollPane(postList);
        add(scrollPane, BorderLayout.CENTER);

        // 새로고침 버튼
        JButton refreshBtn = new JButton("새로고침");
        refreshBtn.addActionListener(e -> refresh(loginUser, scrapController));

        // 하단 패널 구성
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(refreshBtn);
        add(bottomPanel, BorderLayout.SOUTH);

        // 더블 클릭 시 상세보기
        postList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && postList.getSelectedValue() != null) {
                    DonationPost selected = postList.getSelectedValue();
                    new DonationPostDetailView(
                            selected,
                            loginUser,
                            donationPostController,
                            scrapController,
                            () -> refresh(loginUser, scrapController)
                    ).setVisible(true);
                }
            }
        });

        // 초기 로딩
        refresh(loginUser, scrapController);
    }

    // ✅ 새로고침 메서드
    private void refresh(User loginUser, ScrapController scrapController) {
        List<DonationPost> scrappedPosts = scrapController.getScrappedPosts(loginUser);
        listModel.clear();
        for (DonationPost post : scrappedPosts) {
            listModel.addElement(post);
        }
    }
}
