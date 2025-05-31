package capstone.view.user;

import capstone.controller.DonationPostController;
import capstone.controller.ScrapController;
import capstone.dto.DonatedPostInfo;
import capstone.model.DonationPost;
import capstone.model.User;
import capstone.view.donation.DonationPostDetailView;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class DonationHistoryPanel extends JPanel {
    private final User loginUser;
    private final DonationPostController controller;
    private final ScrapController scrapController;

    public DonationHistoryPanel(User loginUser,
                                DonationPostController controller,
                                ScrapController scrapController) {
        this.loginUser = loginUser;
        this.controller = controller;
        this.scrapController = scrapController;

        setLayout(new BorderLayout());

        JPanel postListPanel = new JPanel();
        postListPanel.setLayout(new BoxLayout(postListPanel, BoxLayout.Y_AXIS));
        postListPanel.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(postListPanel);
        add(scrollPane, BorderLayout.CENTER);

        List<DonatedPostInfo> infos = controller.getDonatedPostInfos(loginUser);

        for (DonatedPostInfo info : infos) {
            DonationPost post = info.getPost();

            // 카드 패널
            JPanel cardPanel = new JPanel();
            cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
            cardPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createEmptyBorder(10, 10, 10, 10),
                    BorderFactory.createLineBorder(Color.LIGHT_GRAY)
            ));
            cardPanel.setBackground(Color.WHITE);
            cardPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

            // 제목
            JLabel titleLabel = new JLabel("제목: " + post.getTitle());
            titleLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
            JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
            titlePanel.setOpaque(false);
            titlePanel.add(titleLabel);

            // 내용
            String content = post.getContent().length() > 30
                    ? post.getContent().substring(0, 30) + "..."
                    : post.getContent();
            JLabel contentLabel = new JLabel("내용: " + content);
            contentLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
            JPanel contentPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
            contentPanel.setOpaque(false);
            contentPanel.add(contentLabel);

            // 상단: 텍스트 패널(제목, 내용)
            JPanel textPanel = new JPanel();
            textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
            textPanel.setOpaque(false);
            textPanel.add(titlePanel);
            textPanel.add(Box.createVerticalStrut(5));
            textPanel.add(contentPanel);


            // 하단: 기부포인트 + 상세보기 버튼
            JPanel bottomPanel = new JPanel(new BorderLayout());
            bottomPanel.setOpaque(false);
            JLabel pointLabel = new JLabel(info.getTotalDonatedPoint() + "P");
            pointLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
            JButton detailButton = new JButton("상세보기");
            detailButton.setPreferredSize(new Dimension(90, 30));
            detailButton.addActionListener(e -> {
                new DonationPostDetailView(post, loginUser, controller, scrapController, this::refresh).setVisible(true);
            });

            bottomPanel.add(pointLabel, BorderLayout.WEST);
            bottomPanel.add(detailButton, BorderLayout.EAST);

            // 패널 구성
            cardPanel.add(titlePanel);
            cardPanel.add(Box.createVerticalStrut(5));
            cardPanel.add(contentPanel);
            cardPanel.add(Box.createVerticalStrut(5));
            cardPanel.add(bottomPanel);

            postListPanel.add(cardPanel);
            postListPanel.add(Box.createVerticalStrut(10));
        }
    }

    private void refresh() {
        removeAll();
        revalidate();
        repaint();
    }
}