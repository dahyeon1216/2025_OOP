package capstone.view.user;

import capstone.controller.DonationPostController;
import capstone.controller.ScrapController;
import capstone.dto.DonatedPostInfo;
import capstone.model.*;
import capstone.service.DonationPostService;
import capstone.view.BaseView;
import capstone.view.donation.DonationPostDetailView;
import capstone.view.donation.DonationPostUpView;
import capstone.view.receipt.ReceiptListView;
import capstone.view.style.RoundedBorder;
import capstone.view.style.RoundedButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;



public class MyDonationPostListView extends BaseView {

    private final User loginUser;
    private final DonationPostController donationPostController;
    private final ScrapController scrapController;
    private JPanel postListPanel;

    public MyDonationPostListView(User loginUser,
                               DonationPostController donationPostController,
                               ScrapController scrapController){
        super("나의 기부글");

        this.loginUser = loginUser;
        this.donationPostController = donationPostController;
        this.scrapController = scrapController;

        JPanel header = createHeader("나의 기부글");

        // 기부글 카드 리스트 패널
        postListPanel = new JPanel();
        postListPanel.setLayout(new BoxLayout(postListPanel, BoxLayout.Y_AXIS));
        postListPanel.setOpaque(false);

        // 스크롤 적용
        JScrollPane scrollPane = new JScrollPane(postListPanel);
        scrollPane.setPreferredSize(new Dimension(360, 500));
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        JPanel center = new JPanel();
        center.setLayout(new BorderLayout());
        center.setOpaque(false);

        JPanel body = new JPanel();
        body.setLayout(new BorderLayout());
        body.setOpaque(false);
        body.add(center, BorderLayout.NORTH);
        body.add(scrollPane, BorderLayout.CENTER);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(header, BorderLayout.NORTH);
        getContentPane().add(body, BorderLayout.CENTER);

        refresh();

        setVisible(true);
    }

    private JPanel createDonationCard(DonationPost post) {

        JPanel card = new JPanel(null);
        card.setMinimumSize(new Dimension(355, 240));
        card.setPreferredSize(new Dimension(355, 240));
        card.setMaximumSize(new Dimension(355, 240));
        card.setBackground(new Color(245, 245, 245));
        card.setBorder(new RoundedBorder(20));
        card.setOpaque(true);

        // 이미지
        String imgPath = "resources/images/donation/" + post.getDonationImg();
        File imgFile = new File(imgPath);
        ImageIcon icon = imgFile.exists() ? new ImageIcon(imgPath) : new ImageIcon("icons/image-fail.png");
        Image scaledImage = icon.getImage().getScaledInstance(65, 65, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
        imageLabel.setBounds(15, 15, 65, 65);
        imageLabel.setBorder(new RoundedBorder(15));
        card.add(imageLabel);

        // 제목
        String titleHtml = "<html><body style='width: 230px'>" + post.getTitle() + "</body></html>";
        JLabel titleLabel = new JLabel(titleHtml);
        titleLabel.setFont(customFont.deriveFont(Font.BOLD, 21f));
        titleLabel.setBounds(97, 18, 230, 25);
        card.add(titleLabel);

        // 내용 일부 표시 (30자 제한)
        String content = post.getContent().length() > 30 ? post.getContent().substring(0, 30) + "..." : post.getContent();
        JLabel contentLabel = new JLabel(content);
        contentLabel.setFont(customFont.deriveFont(Font.PLAIN, 18f));
        contentLabel.setBounds(97, 48, 240, 20);
        card.add(contentLabel);

        // 모임 통장 표시
        JLabel accountLabel = new JLabel("모임통장: " + post.getVirtualAccount().getBankType().getBankName()+ " " + post.getVirtualAccount().getBankAccount());
        accountLabel.setFont(customFont.deriveFont(Font.BOLD, 18f));
        accountLabel.setBounds(20, 93, 250, 25);
        card.add(accountLabel);

        // 기부액 표시
        JLabel donatedLabel = new JLabel("모금액: " + post.getGoalPoint() + "P");
        donatedLabel.setFont(customFont.deriveFont(Font.BOLD, 18f));
        donatedLabel.setBounds(20, 128, 250, 25);
        card.add(donatedLabel);

        // 구분선
        JSeparator separator = new JSeparator();
        separator.setForeground(new Color(179, 179, 179));  // 구분선 색깔 (조금 진한 회색)
        separator.setBounds(20, 160, 310, 10);  // (x, y, width, height)
        card.add(separator);

        // 상세보기 버튼
        JButton detailBtn = new RoundedButton("상세보기", new Color(60, 60, 60), 20);
        detailBtn.setForeground(Color.WHITE);
        detailBtn.setFont(customFont.deriveFont(Font.BOLD, 16f));
        detailBtn.setBounds(20, 180, 90, 30);
        detailBtn.addActionListener(e -> {
            new DonationPostDetailView(post, loginUser, donationPostController, scrapController, this::refresh).setVisible(true);
        });
        card.add(detailBtn);

        // up하기 버튼
        JButton upBtn = new RoundedButton("UP하기", new Color(60, 60, 60), 20);
        upBtn.setForeground(Color.WHITE);
        upBtn.setFont(customFont.deriveFont(Font.BOLD, 16f));
        upBtn.setBounds(117, 180, 80, 30);
        upBtn.addActionListener(e -> {
            new DonationPostUpView(loginUser,post,donationPostController,this::refresh).setVisible(true);
        });
        card.add(upBtn);

        // 기부금 사용내역 버튼
        JButton usageBtn = new RoundedButton("기부금 사용내역", new Color(60, 60, 60), 20);
        usageBtn.setForeground(Color.WHITE);
        usageBtn.setFont(customFont.deriveFont(Font.BOLD, 16f));
        usageBtn.setBounds(205, 180, 130, 30);
        usageBtn.addActionListener(e -> {
            VirtualAccount va = post.getVirtualAccount();
            if (va != null) {
                ReceiptListView receiptView = new ReceiptListView(va);
                receiptView.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "아직 정산되지 않아 사용내역이 없습니다.");
            }
        });
        card.add(usageBtn);

        // 전체 wrapper (패딩용)
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.setPreferredSize(new Dimension(355, 240));
        wrapper.setMaximumSize(new Dimension(355, 240));
        wrapper.setBorder(BorderFactory.createEmptyBorder(15, 1, 0, 0));
        wrapper.add(card, BorderLayout.CENTER);

        return wrapper;
    }

    public void refresh() {
        postListPanel.removeAll();

        List<DonationPost> infos = donationPostController.getAllPosts().stream()
                .filter(post -> post.getWriter() != null && post.getWriter().equals(loginUser)) // 내가 쓴 글만
                .toList();

        for (DonationPost info : infos) {
            JPanel card = createDonationCard(info);
            postListPanel.add(card);
        }

        postListPanel.revalidate();
        postListPanel.repaint();
    }

}



