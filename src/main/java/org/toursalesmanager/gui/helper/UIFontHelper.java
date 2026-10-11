package org.toursalesmanager.gui.helper;

import java.awt.Component;
import java.awt.Font;

public final class UIFontHelper {
    public static final Font PAGE_TITLE = new Font("Arial", Font.BOLD, 18);
    public static final Font SECTION_TITLE = new Font("Arial", Font.BOLD, 14);
    public static final Font BODY = new Font("Arial", Font.PLAIN, 14);
    public static final Font TABLE = new Font("Arial", Font.PLAIN, 13);

    private UIFontHelper() {
    }

    public static void apply(Component component, Font font) {
        component.setFont(font);
    }
}
