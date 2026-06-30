package com.ImdbTLM.view;

import java.awt.*;

// Paleta de Cores do APP
public final class AppTheme {

    // Cores primárias
    public static final Color GOLD       = new Color(0xC9, 0xA2, 0x27); // #C9A227 — dourado IMDB
    public static final Color BLACK      = new Color(0x0D, 0x0D, 0x0D); // #0D0D0D — fundo principal
    public static final Color OFFWHITE   = new Color(218, 230, 230); // #F5F2EC — surface/cards
    public static final Color TEAL       = new Color(0, 0, 0); // #1D8FAB — azul petróleo

    // Cores funcionais
    public static final Color BG_PRIMARY      = BLACK;                          // fundo de janelas
    public static final Color BG_SURFACE      = new Color(0x18, 0x18, 0x18);   // painéis internos
    public static final Color BG_CARD         = new Color(0x22, 0x20, 0x1A);   // cards/abas
    public static final Color BG_INPUT        = new Color(0x1A, 0x1A, 0x14);   // campos de texto
    public static final Color BG_HEADER       = new Color(0x12, 0x10, 0x08);   // header / topo
    public static final Color BG_ROW_EVEN     = new Color(0x1C, 0x1A, 0x13);   // linhas pares da tabela
    public static final Color BG_ROW_ODD      = new Color(0x22, 0x20, 0x18);   // linhas ímpares

    public static final Color FG_PRIMARY      = OFFWHITE;                       // texto principal
    public static final Color FG_SECONDARY    = new Color(0xB0, 0xA8, 0x90);   // texto secundário
    public static final Color FG_MUTED        = new Color(0x70, 0x68, 0x50);   // texto apagado

    public static final Color BORDER          = new Color(0x40, 0x38, 0x20);   // bordas gerais

    // Botões
    public static final Color BTN_PRIMARY     = GOLD;
    public static final Color BTN_PRIMARY_FG  = BLACK;
    public static final Color BTN_DANGER      = new Color(0x8B, 0x20, 0x20);
    public static final Color BTN_SECONDARY   = new Color(0x2A, 0x28, 0x20);
    public static final Color BTN_SECONDARY_FG= OFFWHITE;

    // Fontes
    public static final Font FONT_TITLE   = new Font("SansSerif", Font.BOLD, 20);
    public static final Font FONT_HEADING = new Font("SansSerif", Font.BOLD, 14);
    public static final Font FONT_BODY    = new Font("SansSerif", Font.PLAIN, 13);
    public static final Font FONT_SMALL   = new Font("SansSerif", Font.PLAIN, 11);
    public static final Font FONT_MONO    = new Font("Monospaced", Font.PLAIN, 12);

    private AppTheme() {}
}
