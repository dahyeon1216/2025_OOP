package capstone.view.donation;

import capstone.controller.DonationPostController;
import capstone.controller.ScrapController;
import capstone.model.DonationPost;
import capstone.model.User;
import capstone.model.VirtualAccount;

import javax.swing.*;
import java.awt.*;

public class DonationPostDetailView extends JFrame {
    public DonationPostDetailView(DonationPost post, User loginUser, DonationPostController donationPostController, ScrapController scrapController, Runnable onPostUpdated) {
        setTitle("기부글 상세 보기");
        setSize(500, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(new JLabel("제목: " + post.getTitle()));
        panel.add(new JLabel("작성자: " + (post.getWriter() != null ? post.getWriter().getUserId() : "익명")));
        panel.add(new JLabel("내용: "));

        JTextArea contentArea = new JTextArea(post.getContent());
        contentArea.setEditable(false);
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(contentArea);
        scrollPane.setPreferredSize(new Dimension(400, 150));
        panel.add(scrollPane);

        panel.add(new JLabel("이미지: " + post.getDonationImg()));
        panel.add(new JLabel("목표 포인트: " + post.getGoalPoint()));
        panel.add(new JLabel("현재 기부된 포인트: " + post.getRaisedPoint()));
        panel.add(new JLabel("모금 종료일: " + (post.getEndAt() != null ? post.getEndAt().toString() : "없음")));

        int goal = post.getGoalPoint();
        int current = post.getRaisedPoint();
        double rawPercent = goal > 0 ? ((double) current / goal) * 100 : 0;
        double cappedPercent = Math.min(100.0, rawPercent);
        String percentText = String.format("%.1f%%", cappedPercent);
        panel.add(new JLabel("진행률: " + percentText));

        // 공통 버튼 패널 (왼쪽 정렬)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        // 진행 중인 기부글일 때만 보이게
        // 기부하기, up하기 버튼
        if (!post.isCompleted()) {
            JButton donateButton = new JButton("기부하기");
            JButton upButton = new JButton("UP 하기");

            donateButton.addActionListener(e -> {
                String input = JOptionPane.showInputDialog(this, "기부할 포인트를 입력하세요:");
                if (input != null) {
                    try {
                        int amount = Integer.parseInt(input);
                        if (amount <= 0) throw new NumberFormatException();
                        boolean success = donationPostController.donate(post, loginUser, amount);
                        if (success) {
                            JOptionPane.showMessageDialog(this, "기부 완료!");
                            onPostUpdated.run(); // 화면 새로고침
                            dispose();

                            new DonationPostDetailView(post, loginUser, donationPostController, scrapController, onPostUpdated).setVisible(true);
                        } else {
                            JOptionPane.showMessageDialog(this, "기부 실패. 포인트 부족합니다.");
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "올바른 숫자를 입력하세요.");
                    }
                }
            });

            upButton.addActionListener(e -> {
                int confirm = JOptionPane.showConfirmDialog(this,
                        "300포인트를 사용하여 기부글을 상단에 노출하시겠습니까?",
                        "UP 하기 확인",
                        JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    boolean success = donationPostController.upPost(post, loginUser);
                    if (success) {
                        JOptionPane.showMessageDialog(this, "기부글이 상단에 노출되었습니다!");
                        onPostUpdated.run();
                    } else {
                        JOptionPane.showMessageDialog(this, "포인트가 부족합니다. 최소 300P가 필요합니다.");
                    }
                }
            });

            buttonPanel.add(upButton);
            buttonPanel.add(donateButton);
        }

        JButton usageBtn = new JButton("사용내역");

        // 로그인한 유저일 때만 보이게
        // 스크랩 버튼, 사용내역 버튼
        if (loginUser != null) {
            usageBtn.setEnabled(post.isSettled()); // 정산 전에는 비활성화
            JButton scrapButton = new JButton(
                    scrapController.isScrapped(loginUser, post) ? "스크랩 취소" : "스크랩"
            );

            scrapButton.addActionListener(e -> {
                scrapController.toggleScrap(loginUser, post);
                scrapButton.setText(
                        scrapController.isScrapped(loginUser, post) ? "스크랩 취소" : "스크랩"
                );
                if (onPostUpdated != null) onPostUpdated.run(); // 스크랩 후 갱신
            });

            usageBtn.addActionListener(e -> {
                new ReceiptView(post.getVirtualAccount(), loginUser);
            });

            buttonPanel.add(usageBtn);
            buttonPanel.add(scrapButton);
        }

        // 기부글의 작성자일 때만 보이게
        // 수정, 삭제 버튼
        if (post.getWriter() != null && post.getWriter().equals(loginUser)) {

            // 가상 계좌 정보
            VirtualAccount virtualAccount = post.getVirtualAccount();
            panel.add(new JLabel("가상계좌: " + (virtualAccount.getBankAccount() != null ? virtualAccount.getBankAccount() : "없음")));

            JButton editBtn = new JButton("수정");
            JButton deleteBtn = new JButton("삭제");

            editBtn.addActionListener(e -> {
                new DonationPostEditView(post, loginUser, donationPostController, () -> {
                    JOptionPane.showMessageDialog(this, "기부글이 수정되었습니다.");
                    if (onPostUpdated != null) onPostUpdated.run();
                    dispose();
                }).setVisible(true);
            });

            deleteBtn.addActionListener(e -> {
                int confirm = JOptionPane.showConfirmDialog(this, "정말 삭제하시겠습니까?");
                if (confirm == JOptionPane.YES_OPTION) {
                    donationPostController.deletePost(post.getId());
                    JOptionPane.showMessageDialog(this, "삭제 완료");
                    if (onPostUpdated != null) onPostUpdated.run();
                    dispose();
                }
            });

            if (post.isCompleted() && !post.isSettled()) {
                JButton settleButton = new JButton("정산하기");

                settleButton.addActionListener(e -> {
                    boolean success = donationPostController.settlePost(post);
                    if (success) {
                        JOptionPane.showMessageDialog(this, "정산이 완료되었습니다. 포인트가 지급되었습니다.");

                        // 정산 후 사용내역 추가 버튼 활성화
                        VirtualAccount va = post.getVirtualAccount();
                        va.setRaisedPoint(post.getRaisedPoint());
                        va.setCurrentPoint(post.getRaisedPoint());
                        usageBtn.setEnabled(true);

                        onPostUpdated.run(); // 리스트 패널 새로고침
                        dispose(); // 상세창 닫기
                        new DonationPostDetailView(post, loginUser, donationPostController, scrapController, onPostUpdated).setVisible(true); // 새로 열기
                    } else {
                        JOptionPane.showMessageDialog(this, "정산에 실패했습니다.");
                    }
                });
                buttonPanel.add(settleButton);
            }

            buttonPanel.add(editBtn);
            buttonPanel.add(deleteBtn);
        }

        // 버튼 패널 전체에 추가
        panel.add(buttonPanel);

        add(panel);
    }
}
