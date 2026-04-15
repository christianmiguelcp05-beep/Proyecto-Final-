package util;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

/**
 * UTILIDAD — Estilos y temas visuales del sistema
 * Centraliza colores, fuentes y helpers de estilo para toda la UI.
 */
public class Estilos {

    // ── Paleta de colores corporativos ─────────────────────────────────────────
    public static final Color AZUL_PRIMARIO = new Color(30, 80, 160);
    public static final Color AZUL_HOVER = new Color(20, 60, 130);
    public static final Color AZUL_CLARO = new Color(220, 232, 255);
    public static final Color ROJO_ELIMINAR = new Color(200, 40, 50);
    public static final Color ROJO_HOVER = new Color(160, 20, 30);
    public static final Color VERDE_OK = new Color(30, 140, 80);
    public static final Color GRIS_FONDO = new Color(245, 247, 252);
    public static final Color GRIS_BORDE = new Color(200, 208, 225);
    public static final Color TEXTO_PRINCIPAL = new Color(20, 30, 60);
    public static final Color TEXTO_SECUNDARIO = new Color(100, 110, 140);
    public static final Color BLANCO = Color.WHITE;

    // ── Fuentes ────────────────────────────────────────────────────────────────
    public static final Font TITULO = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font SUBTITULO = new Font("Segoe UI", Font.BOLD, 15);
    public static final Font NORMAL = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font PEQUEÑO = new Font("Segoe UI", Font.PLAIN, 11);
    public static final Font BOTON = new Font("Segoe UI", Font.BOLD, 13);

    // ── Fábrica de campos de texto estilizados ─────────────────────────────────
    public static JTextField crearCampo(String placeholder) {
        JTextField campo = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getText().isEmpty() && !isFocusOwner()) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setColor(new Color(160, 170, 200));
                    g2.setFont(PEQUEÑO);
                    g2.drawString(placeholder, 10, getHeight() / 2 + 4);
                    g2.dispose();
                }
            }
        };
        estilizarCampo(campo);
        return campo;
    }

    public static JPasswordField crearCampoPassword(String placeholder) {
        JPasswordField campo = new JPasswordField();
        estilizarCampo(campo);
        return campo;
    }

    private static void estilizarCampo(JComponent campo) {
        campo.setFont(NORMAL);
        campo.setForeground(TEXTO_PRINCIPAL);
        campo.setBackground(BLANCO);
        campo.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(GRIS_BORDE, 1, true),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));
        campo.setPreferredSize(new Dimension(0, 40));
    }

    // ── Fábrica de botones primarios ──────────────────────────────────────────
    public static JButton crearBotonPrimario(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(BOTON);
        btn.setForeground(BLANCO);
        btn.setBackground(AZUL_PRIMARIO);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 24, 10, 24));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(AZUL_HOVER);
            }

            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(AZUL_PRIMARIO);
            }
        });
        return btn;
    }

    // ── Fábrica de botones de peligro (eliminar) ──────────────────────────────
    public static JButton crearBotonPeligro(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(BOTON);
        btn.setForeground(BLANCO);
        btn.setBackground(ROJO_ELIMINAR);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 24, 10, 24));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(ROJO_HOVER);
            }

            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(ROJO_ELIMINAR);
            }
        });
        return btn;
    }

    // ── Fábrica de botones secundarios (volver) ────────────────────────────────
    public static JButton crearBotonSecundario(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(BOTON);
        btn.setForeground(AZUL_PRIMARIO);
        btn.setBackground(BLANCO);
        btn.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(AZUL_PRIMARIO, 1, true),
                BorderFactory.createEmptyBorder(8, 20, 8, 20)));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(AZUL_CLARO);
            }

            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(BLANCO);
            }
        });
        return btn;
    }

    // ── Etiqueta de título con separador ──────────────────────────────────────
    public static JLabel crearTitulo(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(TITULO);
        lbl.setForeground(TEXTO_PRINCIPAL);
        return lbl;
    }

    // ── Panel con fondo blanco y borde suave ──────────────────────────────────
    public static JPanel crearPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(BLANCO);
        panel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(GRIS_BORDE, 1, true),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));
        return panel;
    }

    // ── Configurar Look & Feel del sistema ────────────────────────────────────
    public static void configurarLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            UIManager.put("Table.font", NORMAL);
            UIManager.put("Table.rowHeight", 30);
            UIManager.put("TableHeader.font", new Font("Segoe UI", Font.BOLD, 12));
            UIManager.put("TableHeader.background", AZUL_PRIMARIO);
            UIManager.put("TableHeader.foreground", Color.WHITE);
            UIManager.put("Table.selectionBackground", AZUL_CLARO);
            UIManager.put("Table.selectionForeground", TEXTO_PRINCIPAL);
        } catch (Exception e) {
            System.err.println("No se pudo configurar L&F: " + e.getMessage());
        }
    }
}
