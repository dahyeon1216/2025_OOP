package capstone.view.Roundborder;

import javax.swing.*;
import java.awt.*;

public class RoundedButton extends JButton {

    private Color backgroundColor;
    private final int radius;

    public RoundedButton(String text, Color backgroundColor, int radius) {
        super(text);
        this.backgroundColor = backgroundColor;
        this.radius = radius;
        setContentAreaFilled(false); // 기본 배경 제거
        setFocusPainted(false);
        setBorderPainted(false);
        setOpaque(false); // 배경 투명 처리
        setForeground(Color.WHITE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(backgroundColor);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

        super.paintComponent(g);
        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        // 테두리 없음
    }

    // setButtonColor 메소드 추가
    public void setButtonColor(Color color) {
        this.backgroundColor = color;
        repaint(); // 색상 변경 후 다시 그리기
    }

}
