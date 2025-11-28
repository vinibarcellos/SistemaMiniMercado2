package vendas.view;

import vendas.produtos.IProdutoService;
import vendas.produtos.Produto;
import texto.EscritorTexto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

public class RelatorioEstoque extends JPanel {
    private IProdutoService service;
    private JTable tabela;
    private DefaultTableModel modelo;

    // Recebe um 'aviso' (callback) que aceita o ID
    public RelatorioEstoque(IProdutoService service, Consumer<Integer> aoClicarNaLinha) {
        this.service = service;
        setLayout(new BorderLayout());

        String[] colunas = {"ID", "Nome do Produto", "Qtd. Disponível"};
        modelo = new DefaultTableModel(colunas, 0);
        tabela = new JTable(modelo);

        // --- EVENTO DE CLIQUE PARA SELECIONAR ---
        tabela.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabela.getSelectedRow() != -1) {
                // Pega o ID da linha selecionada
                int id = (int) tabela.getValueAt(tabela.getSelectedRow(), 0);
                aoClicarNaLinha.accept(id);
            }
        });

        // Botões
        JButton btnAtualizar = new JButton("Atualizar Lista");
        JButton btnExportar = new JButton("Gerar estoque.txt");

        btnAtualizar.addActionListener(e -> carregarDados());

        btnExportar.addActionListener(e -> {
            try {
                EscritorTexto.escreverEstoque(service.listarTodos());
                JOptionPane.showMessageDialog(this, "Arquivo 'estoque.txt' gerado!");
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
                modelo.addRow(new Object[]{p.getId(), p.getNome(), p.getQtdEstoque()});
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage());
        }
    }
}