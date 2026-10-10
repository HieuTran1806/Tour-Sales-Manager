package org.toursalesmanager.gui.helper;

import org.toursalesmanager.enums.Permission;
import org.toursalesmanager.login.SessionManager;

import javax.swing.*;

public final class PermissionUI {

    private PermissionUI() {
    }

    public static void apply(
            JComponent component,
            Permission permission
    ) {
        boolean allowed =
                SessionManager.hasPermission(permission);

        component.setVisible(allowed);
        component.setEnabled(allowed);
    }

    public static void enable(
            JComponent component,
            Permission permission
    ) {
        component.setEnabled(
                SessionManager.hasPermission(permission)
        );
    }
}