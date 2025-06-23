package capstone.view.receipt;

import capstone.model.ReceiptRecord;
import capstone.model.User;
import capstone.model.VirtualAccount;
import capstone.view.BaseView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Map;

public class ReceiptWriteView extends BaseView {
    private final VirtualAccount virtualAccount;
    private final User loginUser;
    private final Runnable onPostUpdated;
    private final DefaultListModel<String> listModel = new DefaultListModel<>();
    private final JLabel currentPointLabel = new JLabel(); // 잔여 포인트 표시 라벨

    public ReceiptWriteView(VirtualAccount virtualAccount, User loginUser, Runnable onPostUpdated) {
        super("사용내역 작성");
        this.virtualAccount = virtualAccount;
        this.loginUser = loginUser;
        this.onPostUpdated = onPostUpdated;

        setTitle("사용내역 작성");

        // 현재 포인트 표시
        updateCurrentPointLabel();
        add(currentPointLabel, BorderLayout.NORTH);

        // 사용 내역 목록
        JList<String> receiptList = new JList<>(listModel);
        refreshReceiptList(); // 목록 초기 로딩
        add(new JScrollPane(receiptList), BorderLayout.CENTER);

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

                // ReceiptListView 새로고침
                if (onPostUpdated != null) {
                    onPostUpdated.run();
                }

                dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "숫자를 입력하세요.");
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });

        add(inputPanel, BorderLayout.SOUTH);
        setVisible(true);
    }

    // 포인트 표시 라벨 갱신
    private void updateCurrentPointLabel() {
        currentPointLabel.setText("전체 모금 포인트: " + virtualAccount.getRaisedPoint() + "P");
    }

    // 리스트 갱신 메소드
    private void refreshReceiptList() {
        listModel.clear();
        Map<Integer, ReceiptRecord> receipt = virtualAccount.getReceipt();
        if (receipt != null) {
            for (Map.Entry<Integer, ReceiptRecord> entry : receipt.entrySet()) {
                ReceiptRecord record = entry.getValue();
                String display = String.format(
                        "%d. [%s] %s - %sP (잔액: %sP)",
                        entry.getKey(),
                        record.getTimestamp().toLocalDate(),
                        record.getUsageDetail(),
                        record.getUsedAmount(),
                        record.getRemainingBalance()
                );
                listModel.addElement(display);
            }
        }
    }
}