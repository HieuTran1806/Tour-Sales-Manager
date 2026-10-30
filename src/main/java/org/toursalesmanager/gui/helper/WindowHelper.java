package org.toursalesmanager.gui.helper;

import javax.swing.*;
import java.awt.*;

public final class WindowHelper {

    private WindowHelper() {
    }

    /**
     * Hiển thị cửa sổ ở giữa cửa sổ cha. Gọi sau khi đã pack()/setSize().
     * Nếu dialog được tạo không có owner (Swing gán một frame ẩn dùng chung),
     * dùng cửa sổ đang active (cửa sổ người dùng vừa bấm) làm cửa sổ cha.
     */
    public static void centerOnParent(Window window) {
        Window parent = window.getOwner();
        if (parent == null || !parent.isShowing()) {
            parent = KeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow();
        }
        if (parent == window) {
            parent = null;
        }
        window.setLocationRelativeTo(parent);
    }

    // Lấy Frame chứa component, dùng làm owner cho các dialog nhận tham số Frame
    public static Frame getParentFrame(Component component) {
        Window window = component instanceof Window w ? w : SwingUtilities.getWindowAncestor(component);
        while (window != null && !(window instanceof Frame)) {
            window = window.getOwner();
        }
        return (Frame) window;
    }
}
