package capstone.view.receipt;

import capstone.controller.DonationPostController;
import capstone.controller.ScrapController;
import capstone.model.*;
import capstone.view.BaseView;
import capstone.view.style.RoundedBorder;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ReceiptListView extends BaseView {
    private JPanel contentPanel; // 사용 내역 리스트를 담을 패널
    private VirtualAccount virtualAccount;

    public ReceiptListView(VirtualAccount virtualAccount) {
        super("사용내역");
        this.virtualAccount = virtualAccount;

        setLayout(new BorderLayout());

        // 헤더 패널 생성
        JPanel headerPanel = createHeader("사용내역");

        // i 버튼 생성
        ImageIcon infoIcon = new ImageIcon("icons/info.png");

        Image scaledInfoImg = infoIcon.getImage().getScaledInstance(26, 26, Image.SCALE_SMOOTH);
        ImageIcon resizedInfoIcon = new ImageIcon(scaledInfoImg);
        JButton infoButton = new JButton(resizedInfoIcon);
        infoButton.setBorderPainted(false);
        infoButton.setContentAreaFilled(false);
        infoButton.setFocusPainted(false);
        infoButton.setBounds(350, 7, 40, 30);
        headerPanel.add(infoButton);

        // i 버튼 액션 리스너
        infoButton.addActionListener(e -> {
        });

        add(headerPanel, BorderLayout.NORTH);

        // 사용 내역을 담을 스크롤 가능한 패널
        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(new Color(240, 240, 240));
        contentPanel.setBorder(new EmptyBorder(15, 10, 0, 10));

        //스크롤 추가
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);

        refresh();
    }

    // 사용 내역 하나를 나타내는 패널을 추가하는 메소드
    public void addUsageRecord(ReceiptRecord record) {
        String date = record.getTimestamp().toLocalDate().toString();
        String time = record.getTimestamp().toLocalTime().toString().substring(0, 5);
        String place = record.getUsageDetail();
        String amount = "-"+record.getUsedAmount()+"P";
        String remainPoint = record.getRemainingBalance()+"P";

        JPanel recordPanel = new JPanel();
        recordPanel.setLayout(new BoxLayout(recordPanel, BoxLayout.Y_AXIS));
        recordPanel.setBackground(Color.WHITE);
        recordPanel.setBorder(new RoundedBorder(20));
        recordPanel.setMaximumSize(new Dimension(360, 150));
        recordPanel.setPreferredSize(new Dimension(360, 150));
        recordPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        recordPanel.add(Box.createVerticalStrut(10));

        // 날짜 라벨을 담을 패널 (왼쪽 정렬)
        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        datePanel.setOpaque(false);

        // 날짜
        JLabel dateLabel = new JLabel(date);
        dateLabel.setFont(customFont.deriveFont(Font.BOLD, 22f));
        dateLabel.setBorder(new EmptyBorder(0, 10, 0, 0));
        datePanel.add(dateLabel);

        // 시간
        JLabel timeLabel = new JLabel(time);
        timeLabel.setFont(customFont.deriveFont(Font.BOLD, 16f));
        timeLabel.setForeground(Color.GRAY);
        timeLabel.setBorder(new EmptyBorder(7, 10, 0, 25));
        datePanel.add(timeLabel);

        recordPanel.add(datePanel);
        datePanel.setAlignmentX(Component.LEFT_ALIGNMENT);


        // 사용처와 가격을 담을 패널 (사용처 왼쪽, 가격 오른쪽)
        JPanel placeAmountPanel = new JPanel(new BorderLayout());
        placeAmountPanel.setOpaque(false);
        placeAmountPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // 사용처 라벨
        JLabel placeLabel = new JLabel(place);
        placeLabel.setFont(customFont.deriveFont(Font.BOLD, 18f));
        placeLabel.setBorder(new EmptyBorder(10, 10, 7, 0));
        placeAmountPanel.add(placeLabel, BorderLayout.WEST);

        // 포인트 라벨
        JLabel amountLabel;
        if (record.getUsedAmount() == 0) {
            amountLabel = new JLabel(remainPoint);
        } else {
            amountLabel = new JLabel(amount);
        }
        amountLabel.setFont(customFont.deriveFont(Font.BOLD, 18f));
        amountLabel.setBorder(new EmptyBorder(10, 0, 7, 10));
        placeAmountPanel.add(amountLabel, BorderLayout.EAST);

        recordPanel.add(placeAmountPanel);

        // 구분선
        JPanel separatorPanel = new JPanel(new BorderLayout());
        separatorPanel.setOpaque(false);
        separatorPanel.setBorder(new EmptyBorder(0, 5, 0, 5));
        JSeparator separator = new JSeparator();
        separator.setForeground(new Color(179, 179, 179));
        separatorPanel.add(separator, BorderLayout.CENTER);
        recordPanel.add(separatorPanel);
        separatorPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // 잔액 패널(잔액, 잔액금)
        JPanel remainPanel = new JPanel(new BorderLayout());
        remainPanel.setOpaque(false);
        remainPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // 사용처 라벨
        JLabel remainLabel = new JLabel("잔액");
        remainLabel.setFont(customFont.deriveFont(Font.BOLD, 18f));
        remainLabel.setBorder(new EmptyBorder(5, 10, 10, 0));
        remainPanel.add(remainLabel, BorderLayout.WEST);

        // 포인트 라벨
        JLabel remainPointLabel = new JLabel(remainPoint);
        remainPointLabel.setFont(customFont.deriveFont(Font.BOLD, 18f));
        remainPointLabel.setBorder(new EmptyBorder(5, 0, 10, 10));
        remainPanel.add(remainPointLabel, BorderLayout.EAST);
        recordPanel.add(remainPanel);

        contentPanel.add(recordPanel);
        contentPanel.add(Box.createVerticalStrut(15)); // 각 포스트 간의 간격
    }

    public void refresh() {
        contentPanel.removeAll();

        List<ReceiptRecord> records = new ArrayList<>(virtualAccount.getReceipt().values());
        Collections.reverse(records); // 선입후출
        for (ReceiptRecord record : records) {
            addUsageRecord(record);
        }

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // 테스트용 사용자 생성
            User dummyUser = new User(
                    "testuser", "password123", "테스트 사용자", "테스트닉네임",
                    "profile.jpg", BankType.KB, "123-4567-8901",
                    10000, Tier.SILVER
            );

            // 테스트용 DonationPost 생성
            DonationPost dummyPost = new DonationPost(
                    dummyUser, "donation.jpg", 50000,
                    LocalDate.now().plusDays(30), "기부 테스트", "내용"
            );

            // 테스트용 VirtualAccount 생성
            VirtualAccount dummyAccount = new VirtualAccount(BankType.KB, "123-456-7890");

            // 수동으로 사용내역 추가
            dummyAccount.settleReceipt(50000);
            dummyAccount.usePoint(20000, "식사비");
            dummyAccount.usePoint(15000, "의료비");
            dummyAccount.usePoint(5000, "교육비");


            // View 실행
            ReceiptListView view = new ReceiptListView(dummyAccount);
            view.setVisible(true);
        });
    }


}
