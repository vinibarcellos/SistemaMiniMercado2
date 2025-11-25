package vendas.view;

import vendas.produtos.IProdutoService;
import vendas.produtos.Produto;
import vendas.produtos.ProdutoService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class InterfaceGUI extends JFrame {
    // Agora usamos a Interface, não direto o DAO (Padrão Enterprise)
    private IProdutoService service;

    private JTable tabelaProdutos;
    private DefaultTableModel modeloTabela;

    // Campos do Formulário (Atualizados com os novos dados do SQL)
    private JTextField txtId, txtNome, txtCodBarras, txtPreco, txtCusto, txtQtd;

    public InterfaceGUI() {
        // Inicializa a implementação do serviço
        this.service = new ProdutoService();

        setTitle("Sistema Mercado 2 Amigos - v3.0 Enterprise");
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centraliza

        // Abas
        JTabbedPane abas = new JTabbedPane();
        abas.add("Cadastro de Produtos", criarPainelCadastro());
        abas.add("Relatório de Estoque", criarPainelRelatorio());

        add(abas);
    }

    // --- ABA 1: FORMULÁRIO ---
    private JPanel criarPainelCadastro() {
        JPanel painel = new JPanel(new BorderLayout());

        // Grid com 6 linhas para acomodar os novos campos
        JPanel form = new JPanel(new GridLayout(6, 2, 10, 10));
        form.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 1. ID
        form.add(new JLabel("ID (Automático):"));
        txtId = new JTextField();
        txtId.setEditable(false);
        txtId.setBackground(Color.LIGHT_GRAY);
        form.add(txtId);

        // 2. Nome
        form.add(new JLabel("Nome do Produto:"));
        txtNome = new JTextField();
        form.add(txtNome);

        // 3. Código de Barras (Novo)
        form.add(new JLabel("Código de Barras (EAN):"));
        txtCodBarras = new JTextField();
        form.add(txtCodBarras);

        // 4. Preço de Venda
        form.add(new JLabel("Preço de Venda (R$):"));
        txtPreco = new JTextField();
        form.add(txtPreco);

        // 5. Custo Médio (Novo)
        form.add(new JLabel("Custo Médio (R$):"));
        txtCusto = new JTextField();
        form.add(txtCusto);

        // 6. Quantidade
        form.add(new JLabel("Quantidade em Estoque:"));
        txtQtd = new JTextField();
        form.add(txtQtd);

        // Botões
        JPanel botoes = new JPanel(new FlowLayout());
        JButton btnSalvar = new JButton("Salvar");
        JButton btnEditar = new JButton("Editar");
        JButton btnExcluir = new JButton("Excluir");
        JButton btnLimpar = new JButton("Limpar");

        // Ações
        btnSalvar.addActionListener(e -> acaoSalvar());
        btnEditar.addActionListener(e -> acaoEditar());
        btnExcluir.addActionListener(e -> acaoExcluir());
        btnLimpar.addActionListener(e -> limparCampos());

        botoes.add(btnSalvar);
        botoes.add(btnEditar);
        botoes.add(btnExcluir);
        botoes.add(btnLimpar);

        painel.add(form, BorderLayout.NORTH);
        painel.add(botoes, BorderLayout.CENTER);

        return painel;
    }

    // --- ABA 2: TABELA ---
    private JPanel criarPainelRelatorio() {
        JPanel painel = new JPanel(new BorderLayout());

        // Colunas da tabela
        String[] colunas = {"ID", "Nome", "Cód. Barras", "Preço", "Custo", "Qtd"};
        modeloTabela = new DefaultTableModel(colunas, 0);
        tabelaProdutos = new JTable(modeloTabela);

        // Evento: Clicar na linha preenche o formulário da outra aba
        tabelaProdutos.getSelectionModel().addListSelectionListener(e -> {
            int linha = tabelaProdutos.getSelectedRow();
            if (linha >= 0) {
                txtId.setText(modeloTabela.getValueAt(linha, 0).toString());
                txtNome.setText(modeloTabela.getValueAt(linha, 1).toString());
                txtCodBarras.setText(modeloTabela.getValueAt(linha, 2).toString());
                txtPreco.setText(modeloTabela.getValueAt(linha, 3).toString().replace(",", "."));
                txtCusto.setText(modeloTabela.getValueAt(linha, 4).toString().replace(",", "."));
                txtQtd.setText(modeloTabela.getValueAt(linha, 5).toString());
            }
        });

        JButton btnAtualizar = new JButton("Atualizar Lista");
        btnAtualizar.addActionListener(e -> carregarTabela());

        painel.add(new JScrollPane(tabelaProdutos), BorderLayout.CENTER);
        painel.add(btnAtualizar, BorderLayout.SOUTH);

        return painel;
    }

    // --- LÓGICA (Conversa com o Service) ---

    private void carregarTabela() {
        modeloTabela.setRowCount(0);
        try {
            List<Produto> lista = service.listarTodos();
            for (Produto p : lista) {
                modeloTabela.addRow(new Object[]{
                        p.getId(),
                        p.getNome(),
                        p.getCodBarras(),
                        p.getPreco(),
                        p.getCustoMedio(),
                        p.getQtdEstoque()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao listar: " + e.getMessage());
        }
    }

    private void acaoSalvar() {
        try {
            Produto p = montarProdutoDoFormulario();
            service.salvar(p); // Chama o Service, não o DAO direto

            JOptionPane.showMessageDialog(this, "Produto salvo com sucesso!");
            limparCampos();
            carregarTabela();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar: " + e.getMessage());
        }
    }

    private void acaoEditar() {
        try {
            if (txtId.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Selecione um produto para editar.");
                return;
            }
            Produto p = montarProdutoDoFormulario();
            p.setId(Integer.parseInt(txtId.getText()));

            service.atualizar(p);

            JOptionPane.showMessageDialog(this, "Produto atualizado!");
            limparCampos();
            carregarTabela();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao editar: " + e.getMessage());
        }
    }

    private void acaoExcluir() {
        try {
            if (txtId.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Selecione um produto para excluir.");
                return;
            }
            int id = Integer.parseInt(txtId.getText());
            int confirm = JOptionPane.showConfirmDialog(this, "Deseja realmente excluir?");

            if (confirm == JOptionPane.YES_OPTION) {
                service.excluir(id);
                JOptionPane.showMessageDialog(this, "Produto excluído.");
                limparCampos();
                carregarTabela();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao excluir: " + e.getMessage());
        }
    }

    // Método auxiliar para não repetir código
    private Produto montarProdutoDoFormulario() {
        String nome = txtNome.getText();
        String barras = txtCodBarras.getText();
        // Tratamento para aceitar vírgula ou ponto no preço
        double preco = Double.parseDouble(txtPreco.getText().replace(",", "."));
        double custo = Double.parseDouble(txtCusto.getText().replace(",", "."));
        int qtd = Integer.parseInt(txtQtd.getText());

        // Construtor correspondente à classe Produto atualizada
        return new Produto(0, nome, barras, preco, custo, qtd);
    }

    private void limparCampos() {
        txtId.setText("");
        txtNome.setText("");
        txtCodBarras.setText("");
        txtPreco.setText("");
        txtCusto.setText("");
        txtQtd.setText("");
    }
}