package capstone.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;

public abstract class BaseView extends JFrame {

    protected JFrame previousView;
    protected static Font customFont;

    static {
        try {
            customFont = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/font1.ttf")).deriveFont(15f);
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(customFont);
        } catch (Exception e) {
            customFont = new Font("SansSerif", Font.PLAIN, 15);
            e.printStackTrace();
        }
    }

    public BaseView(String title, JFrame previousView) {
        super(title);
        this.previousView = previousView;

        // 공통 프레임 설정
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(393, 698); // 9:16 비율
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.WHITE);
    }

    /**
     * 공통 헤더 패널 생성
     * @param title 텍스트
     * @return JPanel (헤더)
     */
    protected JPanel createHeader(String title) {
        JPanel header = new JPanel(null);
        header.setPreferredSize(new Dimension(393, 45));
        header.setBackground(new Color(120, 230, 170));

        // 뒤로가기 버튼
        ImageIcon backIcon = new ImageIcon("icons/arrow-left.png");
        Image scaledImg = backIcon.getImage().getScaledInstance(26, 26, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(scaledImg);

        JButton backBtn = new JButton(resizedIcon);
        backBtn.setBorderPainted(false);
        backBtn.setContentAreaFilled(false);
        backBtn.setFocusPainted(false);
        backBtn.setBounds(5, 6, 40, 30);

        // 공통 리스너: 이전 화면 보이기 + 현재 닫기
        backBtn.addActionListener(e -> {
            if (previousView != null) previousView.setVisible(true);
            dispose();
        });

        header.add(backBtn);

        // 타이틀 라벨
        JLabel titleLbl = new JLabel(title, SwingConstants.CENTER);
        titleLbl.setFont(customFont.deriveFont(Font.BOLD, 23f));
        titleLbl.setBounds(13, 7, 360, 30);
        header.add(titleLbl);

        return header;
    }
}
