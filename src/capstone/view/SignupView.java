package capstone.view;

import capstone.controller.UserController;
import capstone.model.BankType;
import capstone.service.UserService;

import javax.swing.*;
import java.awt.*;

public class SignupView extends JFrame {
    public SignupView(UserController userController) {
        setTitle("회원가입");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JTextField idField = new JTextField();
        JPasswordField pwField = new JPasswordField();
        JPasswordField pwConfirmField = new JPasswordField();
        JTextField nameField = new JTextField();
        JTextField nicknameField = new JTextField();
        JComboBox<BankType> bankCombo = new JComboBox<>(BankType.values());
        JTextField accountField = new JTextField();

        JButton joinBtn = new JButton("회원가입");
        JButton cancelBtn = new JButton("취소");

        JPanel panel = new JPanel(new GridLayout(8, 2, 5, 5));
        panel.add(new JLabel("아이디:"));
        panel.add(idField);
        panel.add(new JLabel("비밀번호:"));
        panel.add(pwField);
        panel.add(new JLabel("비밀번호 확인:"));
        panel.add(pwConfirmField);
        panel.add(new JLabel("이름:"));
        panel.add(nameField);
        panel.add(new JLabel("닉네임:"));
        panel.add(nicknameField);
        panel.add(new JLabel("은행명:"));
        panel.add(bankCombo);
        panel.add(new JLabel("계좌번호:"));
        panel.add(accountField);
        panel.add(cancelBtn);
        panel.add(joinBtn);

        add(panel);

        cancelBtn.addActionListener(e -> dispose());

        joinBtn.addActionListener(e -> {
            String userId = idField.getText();
            String pw = new String(pwField.getPassword());
            String pwConfirm = new String(pwConfirmField.getPassword());
            String name = nameField.getText();
            String nickname = nicknameField.getText();
            BankType bank = (BankType) bankCombo.getSelectedItem();
            String account = accountField.getText();

            if (!pw.equals(pwConfirm)) {
                JOptionPane.showMessageDialog(this, "비밀번호가 일치하지 않습니다.");
                return;
            }

            boolean success = userController.signUp(userId, pw, name, nickname, bank, account);
            if (success) {
                JOptionPane.showMessageDialog(this, "회원가입 성공");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "이미 존재하는 아이디입니다.");
            }
        });
    }
}
