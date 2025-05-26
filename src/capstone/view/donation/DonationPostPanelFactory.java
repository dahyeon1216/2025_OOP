package capstone.view.donation;

import capstone.controller.DonationPostController;
import capstone.controller.ScrapController;
import capstone.model.DonationPost;
import capstone.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class DonationPostPanelFactory {

    public static JPanel createPostListPanel(String title,
                                             List<DonationPost> posts,
                                             User loginUser,
                                             DonationPostController controller,
                                             ScrapController scrapController,
                                             Runnable onPostUpdated) {

        JPanel panel = new JPanel(new BorderLayout());

        // 목록 구성
        JList<DonationPost> postList = new JList<>(posts.toArray(new DonationPost[0]));
        postList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // 더블 클릭 이벤트: 상세보기 창 띄우기
        postList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    DonationPost selected = postList.getSelectedValue();
                    if (selected != null) {
                        new DonationPostDetailView(selected, loginUser, controller, scrapController, onPostUpdated).setVisible(true);
                    }
                }
            }
        });

        panel.add(new JLabel(title, SwingConstants.CENTER), BorderLayout.NORTH);
        panel.add(new JScrollPane(postList), BorderLayout.CENTER);

        return panel;
    }
}
