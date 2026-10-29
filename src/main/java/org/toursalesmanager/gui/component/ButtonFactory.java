package org.toursalesmanager.gui.component;

import javax.swing.JButton;
import org.toursalesmanager.gui.helper.UIColors;

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

        button.setBackground(UIColors.BUTTON);
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
        button.setRolloverEnabled(true);
        button.getModel().addChangeListener(event -> {
            if (!button.isEnabled()) {
                button.setBackground(UIColors.BUTTON_DISABLED);
            } else if (button.getModel().isRollover() || button.getModel().isPressed()) {
                button.setBackground(UIColors.BUTTON_HOVER);
            } else {
                button.setBackground(UIColors.BUTTON);
            }
        });

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