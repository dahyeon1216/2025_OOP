package capstone.view.user;

import capstone.controller.UserController;
import capstone.model.BankType;
import capstone.view.style.RoundedButton;

import javax.swing.*;
import java.awt.*;
import java.io.File;


public class SignupView extends JFrame {

    //글씨체 적용
    private static Font customFont;
    static {
        try {
            customFont = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/font1.ttf")).deriveFont(15f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
        } catch (Exception e) {
            customFont = new Font("SansSerif", Font.PLAIN, 15); // fallback
            e.printStackTrace();
        }
    }


    /**
     * @param userController
     */
    public SignupView(UserController userController, Runnable onSignupSuccess) {
        setTitle("회원가입");
        setSize(393, 698);  // 9:16 비율 적용
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setBackground(Color.WHITE);
        setLayout(new BorderLayout());


        // Header
        JPanel header = new JPanel(null);
        header.setPreferredSize(new Dimension(393, 45)); // 높이 45px
        header.setBackground(new Color(120, 230, 170));

        //header title
        JLabel title = new JLabel("회원가입");
        title.setFont(customFont.deriveFont(Font.BOLD, 20));
        title.setBounds(150, 10, 200, 30);
        header.add(title);


        // 뒤로가기 버튼
        ImageIcon backIcon = new ImageIcon("icons/arrow-left.png");
        Image scaledImg = backIcon.getImage().getScaledInstance(26, 26, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(scaledImg);

        JButton backBtn = new JButton(resizedIcon);
        backBtn.setBorderPainted(false);
        backBtn.setContentAreaFilled(false);
        backBtn.setFocusPainted(false);
        backBtn.setBounds(5, 6, 40, 30);
        backBtn.addActionListener(e -> dispose());
        header.add(backBtn);


        // 체크 아이콘
        JLabel idCheckIcon = new JLabel(new ImageIcon("icons/checkicon.png"));
        idCheckIcon.setVisible(false);


        JTextField idField = new JTextField();
        idField.setMaximumSize(new Dimension(700, 40));
        idField.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.LIGHT_GRAY));
        idField.setOpaque(false);
        idField.setBackground(Color.WHITE);
        idField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        idField.setForeground(Color.GRAY);
        idField.setText("아이디/이메일");


        idField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { check(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { check(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { check(); }

            public void check() {
                idCheckIcon.setVisible(!idField.getText().trim().isEmpty());
            }
        });


        JPanel idPanel = new JPanel(new BorderLayout());
        idPanel.setMaximumSize(new Dimension(700, 40));
        idPanel.setOpaque(false);
        idPanel.add(idField, BorderLayout.CENTER);
        idPanel.add(idCheckIcon, BorderLayout.EAST);

        // 입력 필드 정의
        JPasswordField pwField = createRoundedPasswordField("비밀번호");
        JPasswordField pwConfirmField = createRoundedPasswordField("비밀번호 재확인");
        JTextField nameField = createRoundedTextField("이름");
        JTextField nicknameField = createRoundedTextField("닉네임");
        JComboBox<BankType> bankCombo = new JComboBox<>(BankType.values());
        JTextField accountField = createRoundedTextField("연동계좌");

        //회원가입 버튼
        RoundedButton joinBtn = new RoundedButton("회원가입", new Color(60, 60, 60), 30);
        joinBtn.setPreferredSize(new Dimension(327, 44));
        joinBtn.setMaximumSize(new Dimension(327, 50));
        joinBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        joinBtn.setFont(customFont.deriveFont(Font.BOLD, 18f));
        joinBtn.setForeground(Color.WHITE);

        //메인 패널 구성
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 30, 30));


        formPanel.add(idPanel); // 아이디 입력창 + 체크아이콘
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        formPanel.add(pwField);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        formPanel.add(pwConfirmField);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        formPanel.add(nameField);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        formPanel.add(nicknameField);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        formPanel.add(bankCombo);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        formPanel.add(accountField);
        formPanel.add(Box.createVerticalGlue());

        formPanel.add(joinBtn);

        //조립
        add(header, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        setVisible(true);


        //버튼 동작
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
                onSignupSuccess.run();
            } else {
                JOptionPane.showMessageDialog(this, "이미 존재하는 아이디입니다.");
            }
        });
    }


    private JTextField createRoundedTextField(String placeholder) {
        JTextField field = new JTextField();
        field.setMaximumSize(new Dimension(700, 40));
        field.setPreferredSize(new Dimension(700, 40));

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
        field.setMaximumSize(new Dimension(700, 40));
        field.setPreferredSize(new Dimension(700, 40));
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

}