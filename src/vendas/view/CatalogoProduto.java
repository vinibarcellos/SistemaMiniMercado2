package vendas.view;

import vendas.produtos.IProdutoService;
import vendas.produtos.Produto;
import texto.EscritorTexto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.function.Consumer; // IMPORTANTE

public class CatalogoProduto extends JPanel {
    private IProdutoService service;
    private JTable tabela;
    private DefaultTableModel modelo;

    public CatalogoProduto(IProdutoService service, Consumer<Integer> aoClicarNaLinha) {
        this.service = service;
        setLayout(new BorderLayout());

        // ADICIONEI A COLUNA ID
        String[] colunas = {"ID", "Nome", "Cód. Barras", "Preço (R$)"};
        modelo = new DefaultTableModel(colunas, 0);
        tabela = new JTable(modelo);

        // Evento de Clique
        tabela.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabela.getSelectedRow() != -1) {
                int id = (int) tabela.getValueAt(tabela.getSelectedRow(), 0);
                aoClicarNaLinha.accept(id); // Manda o ID lá pra cima
            }
        });

        JButton btnAtualizar = new JButton("Atualizar Catálogo");
        JButton btnExportar = new JButton("Gerar catalogo.txt");

        btnAtualizar.addActionListener(e -> carregarDados());

        btnExportar.addActionListener(e -> {
            try {
                EscritorTexto.escreverCatalogo(service.listarTodos());
                JOptionPane.showMessageDialog(this, "Arquivo 'catalogo.txt' gerado!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
            }
        });

        JPanel painelBotoes = new JPanel();
        painelBotoes.add(btnAtualizar);
        painelBotoes.add(btnExportar);

        add(new JScrollPane(tabela), BorderLayout.CENTER);
        add(painelBotoes, BorderLayout.SOUTH);

        carregarDados();
    }

    public void carregarDados() {
        modelo.setRowCount(0);
        try {
            List<Produto> lista = service.listarTodos();
            for (Produto p : lista) {
                modelo.addRow(new Object[]{
                        p.getId(),
                        p.getNome(),
                        p.getCodBarras(),
                        String.format("%.2f", p.getPreco())
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage());
        }
    }
}