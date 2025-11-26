package vendas;

import vendas.view.InterfaceGUI;
import javax.swing.SwingUtilities;

// Chamamos a Interface
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            InterfaceGUI tela = new InterfaceGUI();
            tela.setVisible(true);
        });
    }
}