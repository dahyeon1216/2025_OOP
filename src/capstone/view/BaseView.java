package capstone.view;

//기본 뷰


import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;

public abstract class BaseView extends JFrame {

    protected JFrame previousView;
    protected static Font customFont;
    protected JButton backButton;

    //폰트
    static {
        try {
            customFont = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/font1.ttf")).deriveFont(15f);
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(customFont);
        } catch (Exception e) {
            customFont = new Font("SansSerif", Font.PLAIN, 15);
            e.printStackTrace();
        }
    }

    public BaseView(String title) {
        super(title);

        // 공통 프레임 설정
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(393, 698); // 9:16 비율
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.WHITE);
    }

    //공통 헤더 패널 생성 메소드
    public JPanel createHeader(String title) {
        JPanel header = new JPanel(null);
        header.setPreferredSize(new Dimension(393, 45));
        header.setBackground(new Color(120, 230, 170));

        // 뒤로가기 버튼
        JButton backBtn = createBackButton();
        header.add(backBtn);

        // 타이틀 라벨
        JLabel titleLbl = new JLabel(title, SwingConstants.CENTER);
        titleLbl.setFont(customFont.deriveFont(Font.BOLD, 23f));
        titleLbl.setBounds(13, 7, 360, 30);
        header.add(titleLbl);

        return header;
    }

    // 삭제,수정할 수 있는 버튼 만드는 메소드
    protected JButton createMenuBarButton() {
        ImageIcon editIcon = new ImageIcon("icons/menuVertical.png");
        Image scaledImg = editIcon.getImage().getScaledInstance(26, 26, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(scaledImg);

        JButton editBtn = new JButton(resizedIcon);
        editBtn.setBorderPainted(false);
        editBtn.setContentAreaFilled(false);
        editBtn.setFocusPainted(false);
        editBtn.setBounds(335, 6, 40, 30);

        return editBtn;
    }

    //뒤로 가기 버튼 만드는 메소드
    protected JButton createBackButton() {
        ImageIcon backIcon = new ImageIcon("icons/arrow-leftb.png");
        Image scaledImg = backIcon.getImage().getScaledInstance(26, 26, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(scaledImg);

        JButton backBtn = new JButton(resizedIcon);
        backBtn.setBorderPainted(false);
        backBtn.setContentAreaFilled(false);
        backBtn.setFocusPainted(false);
        backBtn.setBounds(5, 6, 40, 30);

        return backBtn;
    }


}

