package org.example.main;

import org.example.login.DangNhap;

import javax.swing.*;

public class _Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new DangNhap().setVisible(true);
        });
    }
}