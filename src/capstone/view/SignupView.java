package capstone.view;

import capstone.model.BankType;
import capstone.service.UserService;

import javax.swing.*;
import java.awt.*;

public class SignupView extends JFrame {
    public SignupView(UserService userService) {
        setTitle("회원가입");
        setSize(400, 400);
        setLocationRelativeTo(null);

        // ✅ 입력 필드 선언
        JTextField userIdField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JPasswordField passwordConfirmField = new JPasswordField();
        JTextField nameField = new JTextField();
        JTextField nickNameField = new JTextField();
        JComboBox<BankType> bankTypeCombo = new JComboBox<>(BankType.values());
        JTextField bankAccountField = new JTextField();
        JButton registerBtn = new JButton("가입하기");

        JPanel panel = new JPanel(new GridLayout(8, 2, 5, 5));
        panel.add(new JLabel("아이디:"));
        panel.add(userIdField);
        panel.add(new JLabel("비밀번호:"));
        panel.add(passwordField);
        panel.add(new JLabel("비밀번호 확인:"));
        panel.add(passwordConfirmField);
        panel.add(new JLabel("이름:"));
        panel.add(nameField);
        panel.add(new JLabel("닉네임:"));
        panel.add(nickNameField);
        panel.add(new JLabel("은행명:"));
        panel.add(bankTypeCombo);
        panel.add(new JLabel("계좌번호:"));
        panel.add(bankAccountField);
        panel.add(new JLabel());
        panel.add(registerBtn);

        add(panel);

        registerBtn.addActionListener(e -> {
            String userId = userIdField.getText();
            String password = new String(passwordField.getPassword());
            String passwordConfirm = new String(passwordConfirmField.getPassword());
            String name = nameField.getText();
            String nickName = nickNameField.getText();
            BankType bankType = (BankType) bankTypeCombo.getSelectedItem();
            String bankAccount = bankAccountField.getText();

            if (!password.equals(passwordConfirm)) {
                JOptionPane.showMessageDialog(this, "비밀번호가 일치하지 않습니다.");
                return;
            }

            boolean success = userService.SignUp(userId, password, name, nickName, bankType, bankAccount);
            if (success) {
                JOptionPane.showMessageDialog(this, "회원가입 성공!");
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "이미 존재하는 아이디입니다.");
            }
        });
    }
}
