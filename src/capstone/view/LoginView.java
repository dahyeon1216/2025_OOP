package capstone.view;

import capstone.auth.LoginSession;
import capstone.model.User;
import capstone.service.DonationPostService;
import capstone.service.UserService;

import javax.swing.*;
import java.awt.*;

public class LoginView extends JFrame {
    public LoginView(UserService userService, DonationPostService donationService) {
        setTitle("로그인");
        setSize(300, 150);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTextField idField = new JTextField();
        JPasswordField pwField = new JPasswordField();
        JButton loginBtn = new JButton("로그인");
        JButton joinBtn = new JButton("회원가입");

        JPanel panel = new JPanel(new GridLayout(3, 2));
        panel.add(new JLabel("ID:"));
        panel.add(idField);
        panel.add(new JLabel("PW:"));
        panel.add(pwField);
        panel.add(loginBtn);
        panel.add(joinBtn);

        add(panel);

        loginBtn.addActionListener(e -> {
            String id = idField.getText();
            String pw = new String(pwField.getPassword());
            User loginUser = userService.login(id, pw);

            if (loginUser != null) {
                LoginSession.login(loginUser);
                JOptionPane.showMessageDialog(this, "로그인 성공");
                new DonationPostView(loginUser, donationService, userService).setVisible(true);
                this.dispose(); // 로그인 창 닫기
            } else {
                JOptionPane.showMessageDialog(this, "로그인 실패");
            }
        });

        joinBtn.addActionListener(e -> {
            new SignupView(userService).setVisible(true);
        });
    }
}
