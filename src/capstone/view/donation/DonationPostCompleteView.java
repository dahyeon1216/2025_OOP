package capstone.view.donation;

import capstone.view.Roundborder.RoundedBorder;
import capstone.view.Roundborder.RoundedButton;

import java.awt.*;import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.io.File;

public class DonationPostCompleteView extends JFrame {

    // 커스텀 폰트 로딩
    private static Font customFont;
    static {
        try {
            customFont = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/font1.ttf")).deriveFont(16f);
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(customFont);
        } catch (Exception e) {
            e.printStackTrace();
            customFont = new Font("SansSerif", Font.PLAIN, 16); // 폴백 폰트
        }
    }

    public DonationPostCompleteView() {
        super("기부글 업로드 완료");
        setSize(393, 698);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(Color.WHITE);
        getContentPane().setLayout(new BorderLayout());

        // 중앙 패널
        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        // 꽃 이미지
        JLabel flowerLabel = new JLabel();
        flowerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        ImageIcon icon = new ImageIcon("icons/flower.png"); // 아이콘 경로
        Image scaled = icon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
        flowerLabel.setIcon(new ImageIcon(scaled));
        flowerLabel.setBorder(BorderFactory.createEmptyBorder(100, 0, 30, 0));

        // 텍스트
        JLabel successLabel = new JLabel("기부글 업로드 완료 !");
        successLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        successLabel.setFont(customFont.deriveFont(Font.BOLD, 28f));
        successLabel.setForeground(Color.BLACK);
        successLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));

        JLabel accountLabel = new JLabel("모금통장: 12310294234");
        accountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        accountLabel.setFont(customFont.deriveFont(Font.BOLD, 24f));
        accountLabel.setForeground(Color.GRAY);
        accountLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

        //  중앙 컴포넌트 조립
        centerPanel.add(flowerLabel);
        centerPanel.add(successLabel);
        centerPanel.add(accountLabel);
        centerPanel.add(Box.createVerticalGlue()); // 아래 공간 확보

        // 하단 버튼 (footer)
        JButton mainBtn = new RoundedButton("메인으로", new Color(60, 60, 60), 30);
        mainBtn.setFont(customFont.deriveFont(Font.BOLD, 20f));
        mainBtn.setPreferredSize(new Dimension(0, 44));
        mainBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));


        mainBtn.addActionListener(e -> {
            // 메인화면 이동 로직
            dispose();
        });

        JPanel footer = new JPanel(new BorderLayout());
        footer.setBorder(new EmptyBorder(20, 20, 20, 20));
        footer.setOpaque(false); // 바깥 회색 박스 없애기
        footer.add(mainBtn, BorderLayout.CENTER);


        //  전체 조립
        getContentPane().add(centerPanel, BorderLayout.CENTER);
        getContentPane().add(footer, BorderLayout.SOUTH);

        setVisible(true);
    }


    // 테스트용 메인
    public static void main(String[] args) {
        SwingUtilities.invokeLater(DonationPostCompleteView::new);
    }
}

