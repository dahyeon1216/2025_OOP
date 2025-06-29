package capstone.view.user;

import capstone.controller.DonationPostController;
import capstone.controller.ScrapController;
import capstone.controller.UserController;
import capstone.model.User;
import capstone.view.style.RoundedBorder;
import capstone.view.style.RoundedButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.File;

public class LoginView extends JFrame {

    //글씨체 적용
    private static Font customFont;
    static {
        try {
            customFont = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/font1.ttf")).deriveFont(15f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
        } catch (Exception e) {
            customFont = new Font("SansSerif", Font.PLAIN, 15); // fallback
            e.printStackTrace();
        }
    }

    public interface LoginCallback {
        void onLoginSuccess(User user);
    }

    public LoginView(UserController userController,
                     DonationPostController donationPostController,
                     ScrapController scrapController,
                     LoginCallback callback) {
        setTitle("로그인");
        setSize(393, 698); // 9:16 비율 적용
        setLayout(new BorderLayout());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.WHITE);


        JLabel welcomeLabel = new JLabel("안녕하세요");
        welcomeLabel.setFont(customFont.deriveFont(Font.BOLD, 25));
        welcomeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel welcomeLabel2 = new JLabel("기부담다 입니다 :)");
        welcomeLabel2.setFont(customFont.deriveFont(Font.BOLD, 25));
        welcomeLabel2.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField idField = new JTextField("아이디");
        idField.setForeground(Color.GRAY);
        idField.setMaximumSize(new Dimension(700, 40));
        idField.setAlignmentX(Component.LEFT_ALIGNMENT);
        idField.setBorder(new RoundedBorder(15));
        idField.setFont(customFont.deriveFont(Font.PLAIN, 14));
        idField.setAlignmentX(Component.LEFT_ALIGNMENT);

        idField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (idField.getText().equals("아이디")) {
                    idField.setText("");
                    idField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (idField.getText().isEmpty()) {
                    idField.setText("아이디");
                    idField.setForeground(Color.GRAY);
                }
            }
        });


        // 기본 생성자로 만들기
        JPasswordField pwField = new JPasswordField();

        // placeholder 설정
        pwField.setText("비밀번호");
        pwField.setEchoChar((char) 0); // 가림 문자 제거 (보이게)
        pwField.setForeground(Color.GRAY);

        // 스타일
        pwField.setMaximumSize(new Dimension(700, 40));
        pwField.setAlignmentX(Component.LEFT_ALIGNMENT);
        pwField.setBorder(new RoundedBorder(15));
        pwField.setFont(customFont.deriveFont(Font.PLAIN, 14));

        // placeholder 동작 구현
        pwField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (String.valueOf(pwField.getPassword()).equals("비밀번호")) {
                    pwField.setText("");
                    pwField.setEchoChar('*'); // 입력 시 가림 문자 적용
                    pwField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (String.valueOf(pwField.getPassword()).isEmpty()) {
                    pwField.setText("비밀번호");
                    pwField.setEchoChar((char) 0); // 다시 보이게
                    pwField.setForeground(Color.GRAY);
                }
            }
        });


        JButton loginBtn = new RoundedButton("로그인", new Color(60, 60, 60), 30);
        loginBtn.setFont(customFont.deriveFont(Font.BOLD, 16f));
        loginBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        loginBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        loginBtn.setPreferredSize(new Dimension(700, 50));


        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(90,15,80,15));
        panel.setBackground(Color.WHITE);
        panel.add(loginBtn); // 그대로 추가


        JLabel idLabel = new JLabel("ID:");
        idLabel.setFont(customFont.deriveFont(Font.PLAIN, 14));
        idLabel.setAlignmentX(Component.LEFT_ALIGNMENT);


        JLabel pwLabel = new JLabel("PW:");
        pwLabel.setFont(customFont.deriveFont(Font.PLAIN, 14));
        pwLabel.setAlignmentX(Component.LEFT_ALIGNMENT);


        //간격

        panel.add(Box.createVerticalGlue());

        panel.add(welcomeLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(welcomeLabel2);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));


        panel.add(idLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(idField);
        panel.add(Box.createRigidArea(new Dimension(0, 25)));

        panel.add(pwLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(pwField);
        panel.add(Box.createRigidArea(new Dimension(0, 25)));

        panel.add(loginBtn);

        panel.add(Box.createVerticalGlue());

        add(panel);
        setVisible(true);


        loginBtn.addActionListener(e -> {
            String id = idField.getText();
            String pw = new String(pwField.getPassword());
            if (id.equals("아이디")) id = "";
            if (pw.equals("비밀번호")) pw = "";
            User loginUser = userController.login(id, pw);

            if (loginUser != null) {
                if (callback != null) {
                    this.dispose();
                    callback.onLoginSuccess(loginUser);
                }
            } else {
                JOptionPane.showMessageDialog(this, "로그인 실패");
            }
        });
    }
}