package capstone.view;

import capstone.model.User;

import javax.swing.*;
import java.awt.*;

public class PointChargeView extends JFrame {
    public PointChargeView(User loginUser) {
        setTitle("포인트 충전");
        setSize(400, 300);
        setLocationRelativeTo(null);

        if (loginUser == null) {
            JOptionPane.showMessageDialog(this, "로그인한 사용자만 접근할 수 있습니다.", "접근 제한", JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }

        JPanel panel = new JPanel(new GridLayout(7, 2, 5, 5));

        panel.add(new JLabel("은행명:"));
        panel.add(new JLabel(loginUser.getBankType().getBankName()));

        panel.add(new JLabel("계좌번호:"));
        panel.add(new JLabel(loginUser.getBankAccount()));

        panel.add(new JLabel("이름:"));
        panel.add(new JLabel(loginUser.getName()));

        panel.add(new JLabel("얼마를 충전할까요?"));
        panel.add(new JLabel());

        panel.add(new JLabel("보유 포인트:"));
        JLabel pointLabel = new JLabel(loginUser.getPoint() + "P");
        panel.add(pointLabel);

        panel.add(new JLabel("충전할 포인트:"));
        JTextField chargeField = new JTextField();
        panel.add(chargeField);

        JButton chargeButton = new JButton("충전하기");
        panel.add(new JLabel());
        panel.add(chargeButton);

        add(panel);

        chargeButton.addActionListener(e -> {
            try {
                int amount = Integer.parseInt(chargeField.getText());
                if (amount <= 0) throw new NumberFormatException();
                loginUser.setPoint(loginUser.getPoint() + amount);
                pointLabel.setText(loginUser.getPoint() + "P");
                JOptionPane.showMessageDialog(this, amount + "P 충전되었습니다.");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "올바른 숫자를 입력해주세요.");
            }
        });
    }
}