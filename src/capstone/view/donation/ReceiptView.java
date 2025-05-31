package capstone.view.donation;

import capstone.model.User;
import capstone.model.VirtualAccount;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Map;

public class ReceiptView extends JFrame {
    private final VirtualAccount virtualAccount;
    private final User loginUser;
    private final DefaultListModel<String> listModel = new DefaultListModel<>();
    private final JLabel currentPointLabel = new JLabel(); // 💡 잔여 포인트 표시 라벨

    public ReceiptView(VirtualAccount virtualAccount, User loginUser) {
        this.virtualAccount = virtualAccount;
        this.loginUser = loginUser;

        setTitle("포인트 사용 내역");
        setSize(400, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 💡 현재 포인트 표시
        updateCurrentPointLabel();
        add(currentPointLabel, BorderLayout.NORTH);

        // 사용 내역 목록
        JList<String> receiptList = new JList<>(listModel);
        refreshReceiptList(); // 목록 초기 로딩
        add(new JScrollPane(receiptList), BorderLayout.CENTER);

        // 작성자일 때만 입력창 추가
        if (loginUser.equals(virtualAccount.getOwnerUser())) {
            JPanel inputPanel = new JPanel(new GridLayout(5, 1));
            JTextField pointField = new JTextField();
            JTextField usageField = new JTextField();
            JButton submitBtn = new JButton("작성");

            inputPanel.add(new JLabel("포인트:"));
            inputPanel.add(pointField);
            inputPanel.add(new JLabel("내역 설명:"));
            inputPanel.add(usageField);
            inputPanel.add(submitBtn);

            submitBtn.addActionListener((ActionEvent e) -> {
                try {
                    int usedPoint = Integer.parseInt(pointField.getText());
                    String usageText = usageField.getText();

                    if (!virtualAccount.usePoint(usedPoint, usageText)) {
                        JOptionPane.showMessageDialog(this, "잔여 포인트가 부족합니다.");
                        return;
                    }

                    refreshReceiptList();
                    updateCurrentPointLabel();

                    // 입력 초기화
                    pointField.setText("");
                    usageField.setText("");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "숫자를 입력하세요.");
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage());
                }
            });

            add(inputPanel, BorderLayout.SOUTH);
        }

        setVisible(true);
    }

    // 💡 포인트 표시 라벨 갱신
    private void updateCurrentPointLabel() {
        currentPointLabel.setText("전체 모금 포인트: " + virtualAccount.getRaisedPoint() + "P");
    }

    // 💡 리스트 갱신 메서드
    private void refreshReceiptList() {
        listModel.clear();
        Map<Integer, String> receipt = virtualAccount.getReceipt();
        if (receipt != null) {
            for (Map.Entry<Integer, String> entry : receipt.entrySet()) {
                listModel.addElement(entry.getKey() + ". " + entry.getValue());
            }
        }
    }
}