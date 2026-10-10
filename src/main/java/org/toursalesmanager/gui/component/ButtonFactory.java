package org.toursalesmanager.gui.component;

import javax.swing.JButton;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionListener;

public final class ButtonFactory {

    private ButtonFactory() {
        // Không cho tạo object ButtonFactory
    }

    public static JButton create(
            String text,
            Color backgroundColor
    ) {
        JButton button = new JButton(text);

        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);

        button.setFocusPainted(false);

        button.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        13
                )
        );

        button.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );

        button.setContentAreaFilled(true);
        button.setOpaque(true);
        button.setBorderPainted(false);

        return button;
    }

    public static JButton create(
            String text,
            Color backgroundColor,
            ActionListener listener
    ) {
        JButton button = create(
                text,
                backgroundColor
        );

        if (listener != null) {
            button.addActionListener(listener);
        }

        return button;
    }
}