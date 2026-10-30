package org.toursalesmanager.gui.helper;

import javax.swing.*;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

public final class TableSelectionHelper {

    private TableSelectionHelper() {
    }

    /**
     * Chạy action mỗi khi bảng đổi dòng được chọn (kể cả chọn bằng bàn phím, bỏ chọn, tải lại dữ liệu).
     * Action nhận true nếu đang có dòng được chọn. Chạy ngay một lần để đặt trạng thái ban đầu.
     */
    public static void onSelectionChanged(JTable table, Consumer<Boolean> action) {
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                action.accept(hasSelection(table));
            }
        });
        action.accept(hasSelection(table));
    }

    // Bật các nút khi có dòng được chọn, tắt khi không có
    public static void bindButtons(JTable table, AbstractButton... buttons) {
        bindButtons(table, () -> true, buttons);
    }

    // Bật các nút khi có dòng được chọn VÀ thỏa điều kiện (ví dụ: có quyền quản lý)
    public static void bindButtons(JTable table, BooleanSupplier condition, AbstractButton... buttons) {
        onSelectionChanged(table, selected -> {
            boolean enabled = selected && condition.getAsBoolean();
            for (AbstractButton button : buttons) {
                button.setEnabled(enabled);
            }
        });
    }

    public static boolean hasSelection(JTable table) {
        return table.getSelectedRow() != -1;
    }

    public static int getSelectedModelRow(JTable table) {
        int row = table.getSelectedRow();
        return row == -1 ? -1 : table.convertRowIndexToModel(row);
    }
}
