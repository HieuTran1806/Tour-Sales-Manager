package org.toursalesmanager.gui.helper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public final class PasswordFieldWithReveal {

    private PasswordFieldWithReveal() {
    }

    public static JPanel create(JPasswordField passwordField) {
        char echoChar = passwordField.getEchoChar();
        JButton revealButton = new JButton(new EyeIcon());

        revealButton.setFocusable(false);
        revealButton.setToolTipText("Giữ để hiện mật khẩu");
        revealButton.getAccessibleContext().setAccessibleName(
                "Giữ để hiện mật khẩu"
        );
        revealButton.setBorder(BorderFactory.createEmptyBorder(2, 6, 2, 6));
        revealButton.setContentAreaFilled(false);
        revealButton.setBorderPainted(false);
        revealButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        revealButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent event) {
                if (SwingUtilities.isLeftMouseButton(event)) {
                    passwordField.setEchoChar((char) 0);
                }
            }

            @Override
            public void mouseReleased(MouseEvent event) {
                passwordField.setEchoChar(echoChar);
            }
        });

        passwordField.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusLost(java.awt.event.FocusEvent event) {
                passwordField.setEchoChar(echoChar);
            }
        });

        JPanel fieldPanel = new JPanel(new BorderLayout());
        fieldPanel.setBorder(passwordField.getBorder());
        passwordField.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 2));
        fieldPanel.add(passwordField, BorderLayout.CENTER);
        fieldPanel.add(revealButton, BorderLayout.EAST);
        return fieldPanel;
    }

    private static final class EyeIcon implements Icon {
        private static final int WIDTH = 18;
        private static final int HEIGHT = 14;

        @Override
        public void paintIcon(Component component, Graphics graphics, int x, int y) {
            Graphics2D g2 = (Graphics2D) graphics.create();
            g2.translate(x, y);
            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );
            Color color = UIManager.getColor("Button.foreground");
            g2.setColor(color != null ? color : Color.DARK_GRAY);
            g2.setStroke(new BasicStroke(1.4f));
            g2.drawOval(1, 3, WIDTH - 3, HEIGHT - 7);
            g2.fillOval(WIDTH / 2 - 2, HEIGHT / 2 - 2, 5, 5);
            g2.dispose();
        }

        @Override
        public int getIconWidth() {
            return WIDTH;
        }

        @Override
        public int getIconHeight() {
            return HEIGHT;
        }
    }
}
