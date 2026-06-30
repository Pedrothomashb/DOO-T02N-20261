package com.ImdbTLM;

import com.ImdbTLM.repository.UsuarioRepository;
import com.ImdbTLM.view.AppTheme;
import com.ImdbTLM.view.LoginView;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        configurarLookAndFeel();
        SwingUtilities.invokeLater(() ->
                new LoginView(new UsuarioRepository()).setVisible(true)
        );
    }

    private static void configurarLookAndFeel() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            System.err.println("Nimbus não disponível: " + e.getMessage());
        }

        UIManager.put("TabbedPane.selected",           AppTheme.BG_CARD);
        UIManager.put("TabbedPane.selectedForeground", AppTheme.GOLD);
        UIManager.put("TabbedPane.foreground",         AppTheme.FG_SECONDARY);
        UIManager.put("TabbedPane.background",         AppTheme.BG_HEADER);
        UIManager.put("TabbedPane.tabAreaBackground",  AppTheme.BG_HEADER);
        UIManager.put("TabbedPane.contentAreaColor",   AppTheme.BG_PRIMARY);
        UIManager.put("TabbedPane.highlight",          AppTheme.GOLD);
        UIManager.put("TabbedPane.focus",              AppTheme.GOLD);

        UIManager.put("Panel.background",              AppTheme.BG_PRIMARY);
        UIManager.put("Button.background",             AppTheme.BTN_SECONDARY);
        UIManager.put("Button.foreground",             AppTheme.FG_PRIMARY);
        UIManager.put("TextField.background",          AppTheme.BG_INPUT);
        UIManager.put("TextField.foreground",          AppTheme.FG_PRIMARY);
        UIManager.put("TextField.caretForeground",     AppTheme.GOLD);
        UIManager.put("ComboBox.background",           AppTheme.BG_CARD);
        UIManager.put("ComboBox.foreground",           AppTheme.FG_PRIMARY);
        UIManager.put("ComboBox.selectionBackground",  AppTheme.TEAL);
        UIManager.put("ComboBox.selectionForeground",  AppTheme.OFFWHITE);
        UIManager.put("List.background",               AppTheme.BG_SURFACE);
        UIManager.put("List.foreground",               AppTheme.FG_PRIMARY);
        UIManager.put("List.selectionBackground",      AppTheme.TEAL);
        UIManager.put("Table.background",              AppTheme.BG_SURFACE);
        UIManager.put("Table.foreground",              AppTheme.FG_PRIMARY);
        UIManager.put("Table.selectionBackground",     AppTheme.TEAL);
        UIManager.put("TableHeader.background",        AppTheme.BG_HEADER);
        UIManager.put("TableHeader.foreground",        AppTheme.GOLD);
        UIManager.put("ScrollPane.background",         AppTheme.BG_SURFACE);
        UIManager.put("SplitPaneDivider.background",   AppTheme.GOLD);
        UIManager.put("OptionPane.background",         AppTheme.BG_SURFACE);
        UIManager.put("OptionPane.messageForeground",  AppTheme.FG_PRIMARY);

        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");
    }
}