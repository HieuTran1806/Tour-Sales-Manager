package org.toursalesmanager.gui.helper;

import javax.swing.*;

public class AddComponentToForm {
    public static void addFormRow(JPanel formPanel, String labelText, JComponent component) {
        JLabel label = new JLabel(labelText);

        label.setBorder(
                BorderFactory.createEmptyBorder(
                        5, 5, 5, 0
                )
        );

        formPanel.add(label);
        formPanel.add(component);
    }
}
