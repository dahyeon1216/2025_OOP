package capstone.view;

import capstone.model.DonationPost;
import capstone.view.donation.DonationPostDetailView;
import capstone.view.donation.DonationPostEditView;

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

        setVisible(true);
    }

    /**
     * 공통 헤더 패널 생성
     * @param title 텍스트
     * @return JPanel (헤더)
     */
    public JPanel createHeader(String title) {
        JPanel header = new JPanel(null);
        header.setPreferredSize(new Dimension(393, 45));
        header.setBackground(new Color(120, 230, 170));

        // 뒤로가기 버튼 - 공통 메서드 사용
        JButton backBtn = createBackButton();
        header.add(backBtn);

        // 타이틀 라벨
        JLabel titleLbl = new JLabel(title, SwingConstants.CENTER);
        titleLbl.setFont(customFont.deriveFont(Font.BOLD, 23f));
        titleLbl.setBounds(13, 7, 360, 30);
        header.add(titleLbl);

        return header;
    }

    // 메뉴바 버튼 만드는 메소드
    protected JButton createMenuBarButton() {
        ImageIcon editIcon = new ImageIcon("icons/menuVertical.png");
        Image scaledImg = editIcon.getImage().getScaledInstance(26, 26, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(scaledImg);

        JButton editBtn = new JButton(resizedIcon);
        editBtn.setBorderPainted(false);
        editBtn.setContentAreaFilled(false);
        editBtn.setFocusPainted(false);
        editBtn.setBounds(335, 6, 40, 30);

        //리스너는 하위클래스에서 붙일거임
        return editBtn;
    }

    //뒤로 가기 검정버튼 만드는 메소드
    protected JButton createBackButton() {
        ImageIcon backIcon = new ImageIcon("icons/arrow-leftb.png");
        Image scaledImg = backIcon.getImage().getScaledInstance(26, 26, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(scaledImg);

        JButton backBtn = new JButton(resizedIcon);
        backBtn.setBorderPainted(false);
        backBtn.setContentAreaFilled(false);
        backBtn.setFocusPainted(false);
        backBtn.setBounds(5, 6, 40, 30);

        backBtn.addActionListener(e -> {
            if (previousView != null) previousView.setVisible(true);
            dispose();
        });

        return backBtn;
    }

    //뒤로 가기 흰색 버튼 만드는 메소드
    protected JButton createBackWhiteButton() {
        ImageIcon backIcon = new ImageIcon("icons/arrow-leftw.png");
        Image scaledImg = backIcon.getImage().getScaledInstance(26, 26, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(scaledImg);

        JButton backBtn = new JButton(resizedIcon);
        backBtn.setBorderPainted(false);
        backBtn.setContentAreaFilled(false);
        backBtn.setFocusPainted(false);
        backBtn.setOpaque(false);
        backBtn.setBounds(5, 6, 40, 30);

        backBtn.addActionListener(e -> {
            if (previousView != null) previousView.setVisible(true);
            dispose();
        });

        return backBtn;
    }

}
