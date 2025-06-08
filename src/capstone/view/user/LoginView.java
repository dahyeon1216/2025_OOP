package capstone.view.user;

import capstone.controller.UserController;
import capstone.model.User;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class LoginView extends JFrame {
    public interface LoginCallback {
        void onLoginSuccess(User user);
    }

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


    public LoginView(UserController userController, LoginCallback callback) {
        setTitle("로그인");
        setSize(300, 150);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JTextField idField = new JTextField();
        JPasswordField pwField = new JPasswordField();
        JButton loginBtn = new JButton("로그인");

        JPanel panel = new JPanel(new GridLayout(3, 2));
        panel.add(new JLabel("ID:"));
        panel.add(idField);
        panel.add(new JLabel("PW:"));
        panel.add(pwField);
        panel.add(loginBtn);

        add(panel);

        loginBtn.addActionListener(e -> {
            String id = idField.getText();
            String pw = new String(pwField.getPassword());
            User loginUser = userController.login(id, pw);  // ✅ 컨트롤러 사용

            if (loginUser != null) {
                if (callback != null) {
                    callback.onLoginSuccess(loginUser);
                }
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "로그인 실패");
            }
        });

    }
}