package capstone.view;

import capstone.controller.UserController;
import capstone.model.BankType;
import capstone.model.User;

import javax.swing.*;
import java.awt.*;

public class UserProfileEditView extends JFrame {
    public UserProfileEditView(User loginUser, UserController editController) {
        setTitle("회원 정보 수정");
        setSize(400, 300);
        setLocationRelativeTo(null);

        if (loginUser == null) {
            JOptionPane.showMessageDialog(this, "로그인한 사용자만 접근할 수 있습니다.", "접근 제한", JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }

        JTextField nameField = new JTextField(loginUser.getName());
        JTextField nickNameField = new JTextField(loginUser.getNickName());
        JComboBox<BankType> bankTypeCombo = new JComboBox<>(BankType.values());
        bankTypeCombo.setSelectedItem(loginUser.getBankType());
        JTextField bankAccountField = new JTextField(loginUser.getBankAccount());
        JPasswordField currentPasswordField = new JPasswordField();
        JPasswordField newPasswordField = new JPasswordField();
        JPasswordField confirmPasswordField = new JPasswordField();

        JButton saveButton = new JButton("저장");
        JButton cancelButton = new JButton("취소");

        JPanel panel = new JPanel(new GridLayout(8, 2, 5, 5));
        panel.add(new JLabel("이름:"));
        panel.add(nameField);
        panel.add(new JLabel("닉네임:"));
        panel.add(nickNameField);
        panel.add(new JLabel("은행명:"));
        panel.add(bankTypeCombo);
        panel.add(new JLabel("계좌번호:"));
        panel.add(bankAccountField);
        panel.add(new JLabel("현재 비밀번호:"));
        panel.add(currentPasswordField);
        panel.add(new JLabel("새 비밀번호:"));
        panel.add(newPasswordField);
        panel.add(new JLabel("비밀번호 확인:"));
        panel.add(confirmPasswordField);
        panel.add(cancelButton);
        panel.add(saveButton);

        add(panel);

        cancelButton.addActionListener(e -> dispose());

        saveButton.addActionListener(e -> {
            String name = nameField.getText();
            String nickName = nickNameField.getText();
            BankType bankType = (BankType) bankTypeCombo.getSelectedItem();
            String bankAccount = bankAccountField.getText();
            String currentPassword = new String(currentPasswordField.getPassword());
            String newPassword = new String(newPasswordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());

            if (!newPassword.isEmpty()) {
                if (!loginUser.getPassword().equals(currentPassword)) {
                    JOptionPane.showMessageDialog(this, "현재 비밀번호가 일치하지 않습니다.");
                    return;
                }
                if (!newPassword.equals(confirmPassword)) {
                    JOptionPane.showMessageDialog(this, "새 비밀번호가 일치하지 않습니다.");
                    return;
                }
            }

            boolean success = editController.updateUserProfile(loginUser, name, nickName, bankType, bankAccount);
            if (success) {
                if (!newPassword.isEmpty()) {
                    loginUser.setPassword(newPassword);
                }

                JOptionPane.showMessageDialog(this, "회원 정보가 수정되었습니다.");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "수정 실패. 다시 시도해주세요.");
            }
        });
    }
}
