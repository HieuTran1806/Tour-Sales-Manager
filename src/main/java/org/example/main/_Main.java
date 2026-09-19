package org.example.main;

import org.example.login.Login;

import javax.swing.*;

public class _Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Login().setVisible(true);
        });
    }
}