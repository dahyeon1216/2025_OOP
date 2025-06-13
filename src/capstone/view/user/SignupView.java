package capstone.view.user;

import capstone.controller.UserController;
import capstone.model.BankType;
import capstone.service.UserService;
import capstone.view.BaseView;
import capstone.view.style.RoundedBorder;
import capstone.view.style.RoundedButton;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class SignupView extends BaseView {

    private final UserController userController;

    public SignupView(UserController userController, Runnable onSignupSuccess) {
        super("회원가입");

        this.userController = userController;

        JPanel header = createHeader("회원가입");

        // 입력 필드 정의
        JTextField idField = createRoundedTextField("아이디/이메일");
        JPasswordField pwField = createRoundedPasswordField("비밀번호");
        JPasswordField pwConfirmField = createRoundedPasswordField("비밀번호 재확인");
        JTextField nameField = createRoundedTextField("이름");
        JTextField nicknameField = createRoundedTextField("닉네임");
        JComboBox<BankType> bankCombo = new JComboBox<>(BankType.values());
        JTextField accountField = createRoundedTextField("연동계좌");

        // 회원가입 버튼
        RoundedButton joinBtn = new RoundedButton("회원가입", new Color(60, 60, 60), 30);
        joinBtn.setPreferredSize(new Dimension(327, 44));
        joinBtn.setMaximumSize(new Dimension(327, 50));
        joinBtn.setFont(customFont.deriveFont(Font.BOLD, 18f));
        joinBtn.setForeground(Color.WHITE);

        // 메인 form 패널 구성 (BoxLayout)
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 28, 20, 30));

        // 프로필 이미지 영역
        JPanel profilePanel = new JPanel(null);
        profilePanel.setPreferredSize(new Dimension(130, 100));
        profilePanel.setMaximumSize(new Dimension(130, 100));
        profilePanel.setOpaque(false);

        JLabel profileCircle = new JLabel();
        profileCircle.setBounds(25, 10, 80, 80);
        profileCircle.setOpaque(true);
        profileCircle.setBackground(Color.LIGHT_GRAY);
        profileCircle.setBorder(new RoundedBorder(25));
        profileCircle.setHorizontalAlignment(SwingConstants.CENTER);

        ImageIcon plusIcon = new ImageIcon("icons/plus-icon.png");
        Image plusImg = plusIcon.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
        JButton plusBtn = new JButton(new ImageIcon(plusImg));
        plusBtn.setBounds(87, 68, 30, 30);
        plusBtn.setFocusPainted(false);
        plusBtn.setContentAreaFilled(false);
        plusBtn.setBorderPainted(false);

        final File[] selectedImageFile = {null};

        plusBtn.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("이미지 선택");
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                selectedImageFile[0] = fileChooser.getSelectedFile();

                ImageIcon selectedIcon = new ImageIcon(selectedImageFile[0].getAbsolutePath());
                Image scaled = selectedIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                profileCircle.setIcon(new ImageIcon(scaled));
                profileCircle.setText(null);
            }
        });

        profilePanel.add(plusBtn);
        profilePanel.add(profileCircle);
        profilePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(profilePanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // 공통 필드 추가 메소드
        addField(formPanel, idField);
        addField(formPanel, pwField);
        addField(formPanel, pwConfirmField);
        addField(formPanel, nameField);
        addField(formPanel, nicknameField);

        // 은행 + 계좌번호 입력
        JPanel accountPanel = new JPanel();
        accountPanel.setLayout(new BoxLayout(accountPanel, BoxLayout.X_AXIS));
        accountPanel.setOpaque(false);
        bankCombo.setPreferredSize(new Dimension(120, 40));
        accountField.setPreferredSize(new Dimension(180, 40));
        accountPanel.add(bankCombo);
        accountPanel.add(Box.createHorizontalStrut(20));
        accountPanel.add(accountField);
        accountPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(accountPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        joinBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(joinBtn);

        // 가운데 정렬을 위한 외부 패널 적용 (FlowLayout 사용)
        JPanel outerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        outerPanel.setBackground(Color.WHITE);
        outerPanel.add(formPanel);

        add(header, BorderLayout.NORTH);
        add(outerPanel, BorderLayout.CENTER);

        // 회원가입 버튼 동작
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

            String savedProfileFileName = null;
            if (selectedImageFile[0] != null) {
                try {
                    String uploadDir = "resources/profile/images";
                    File targetDir = new File(uploadDir);
                    if (!targetDir.exists()) targetDir.mkdirs();

                    String fileName = System.currentTimeMillis() + "_" + selectedImageFile[0].getName();
                    File destFile = new File(targetDir, fileName);
                    Files.copy(selectedImageFile[0].toPath(), destFile.toPath());
                    savedProfileFileName = fileName;
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "프로필 이미지 저장 중 오류 발생: " + ex.getMessage());
                }
            }

            boolean success = userController.signUp(savedProfileFileName,userId, pw, name, nickname, bank, account);
            if (success) {
                JOptionPane.showMessageDialog(this, "회원가입 성공");
                dispose();
                if (onSignupSuccess != null) {
                    onSignupSuccess.run();  // 회원가입 성공 콜백 호출
                }
            } else {
                JOptionPane.showMessageDialog(this, "이미 존재하는 아이디입니다.");
            }
        });
    }

    // 공통 필드 추가 메소드 (중복 줄이기용)
    private void addField(JPanel formPanel, JComponent field) {
        field.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(field);
        formPanel.add(Box.createRigidArea(new Dimension(0, 30)));
    }

    private JTextField createRoundedTextField(String placeholder) {
        JTextField field = new JTextField();
        field.setMaximumSize(new Dimension(330, 40));
        field.setPreferredSize(new Dimension(330, 40));
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setText(placeholder);
        field.setForeground(Color.GRAY);
        field.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.LIGHT_GRAY));
        field.setOpaque(false);
        field.setBackground(Color.WHITE);

        field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                }
            }
            public void focusLost(java.awt.event.FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setForeground(Color.GRAY);
                    field.setText(placeholder);
                }
            }
        });
        return field;
    }

    private JPasswordField createRoundedPasswordField(String placeholder) {
        JPasswordField field = new JPasswordField();
        field.setMaximumSize(new Dimension(330, 40));
        field.setPreferredSize(new Dimension(330, 40));
        field.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.LIGHT_GRAY));
        field.setOpaque(false);
        field.setBackground(Color.WHITE);
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setText(placeholder);
        field.setForeground(Color.GRAY);

        field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                String current = new String(field.getPassword());
                if (current.equals(placeholder)) {
                    field.setText("");
                    field.setEchoChar('●');
                    field.setForeground(Color.BLACK);
                }
            }
            public void focusLost(java.awt.event.FocusEvent e) {
                String current = new String(field.getPassword());
                if (current.isEmpty()) {
                    field.setForeground(Color.GRAY);
                    field.setText(placeholder);
                    field.setEchoChar((char) 0);
                }
            }
        });

        return field;
    }

    // UI 테스트용 main
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UserController dummyController = new UserController(new UserService());
            SignupView view = new SignupView(dummyController, null);
            view.setVisible(true);
        });
    }
}

