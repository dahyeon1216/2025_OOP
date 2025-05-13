package capstone.view;

import capstone.service.UserService;

import javax.swing.*;
import java.awt.*;

public class SignupView extends JFrame {
    public SignupView(UserService userService) {
        setTitle("회원가입");
        setSize(300, 150);
        setLocationRelativeTo(null);

        JTextField idField = new JTextField();
        JPasswordField pwField = new JPasswordField();
        JButton registerBtn = new JButton("가입");

        JPanel panel = new JPanel(new GridLayout(3, 2));
        panel.add(new JLabel("ID:"));
        panel.add(idField);
        panel.add(new JLabel("PW:"));
        panel.add(pwField);
        panel.add(new JLabel());
        panel.add(registerBtn);

        add(panel);

        registerBtn.addActionListener(e -> {
            boolean success = userService.SignUp(idField.getText(), new String(pwField.getPassword()));
            if (success) {
                JOptionPane.showMessageDialog(this, "회원가입 성공");
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "이미 존재하는 ID");
            }
        });
    }
}
