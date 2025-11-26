package vendas.view;

import vendas.produtos.IProdutoService;
import vendas.produtos.Produto;
import vendas.produtos.ProdutoService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class InterfaceGUI extends JFrame {
    // Agora usamos a Interface
    private IProdutoService service;

    private JTable tabelaProdutos;
    private DefaultTableModel modeloTabela;

    // Campos do Formulário
    private JTextField txtId, txtNome, txtCodBarras, txtPreco, txtCusto, txtQtd;

    public InterfaceGUI() {
        // Inicializa a implementação do serviço
        this.service = new ProdutoService();

        setTitle("Sistema Mercado 2 Amigos");
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centraliza

        // Abas
        JTabbedPane abas = new JTabbedPane();
        abas.add("Cadastro de Produtos", criarPainelCadastro());
        abas.add("Relatório de Estoque", criarPainelRelatorio());

        add(abas);
    }

    // FORMULÁRIO
    private JPanel criarPainelCadastro() {
        JPanel painel = new JPanel(new BorderLayout());

        // Grid
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

        // 3. Código de Barras
        form.add(new JLabel("Código de Barras (EAN):"));
        txtCodBarras = new JTextField();
        form.add(txtCodBarras);

        // 4. Preço de Venda
        form.add(new JLabel("Preço de Venda (R$):"));
        txtPreco = new JTextField();
        form.add(txtPreco);

        // 5. Custo Médio
        form.add(new JLabel("Custo Médio (R$):"));
        txtCusto = new JTextField();
        form.add(txtCusto);

        // 6. Quantidade
        form.add(new JLabel("Quantidade em Estoque:"));
        txtQtd = new JTextField();
        form.add(txtQtd);

        // Botoes
        JPanel botoes = new JPanel(new FlowLayout());
        JButton btnSalvar = new JButton("Salvar");
        JButton btnEditar = new JButton("Editar");
        JButton btnExcluir = new JButton("Excluir");
        JButton btnLimpar = new JButton("Limpar");

        // Açoes
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

    // TABELA
    private JPanel criarPainelRelatorio() {
        JPanel painel = new JPanel(new BorderLayout());

        // 1. Painel de Busca
        JPanel painelBusca = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField txtBusca = new JTextField(20);
        JButton btnBuscar = new JButton("Buscar por Nome");
        JButton btnLimparBusca = new JButton("Ver Todos");

        painelBusca.add(new JLabel("Pesquisar:"));
        painelBusca.add(txtBusca);
        painelBusca.add(btnBuscar);
        painelBusca.add(btnLimparBusca);

        // Ações da Busca
        btnBuscar.addActionListener(e -> pesquisarProduto(txtBusca.getText()));
        btnLimparBusca.addActionListener(e -> {
            txtBusca.setText("");
            carregarTabela(); // Recarrega tudo
        });

        // 2. Tabela
        String[] colunas = {"ID", "Nome", "Cód. Barras", "Preço", "Custo", "Qtd"};
        modeloTabela = new DefaultTableModel(colunas, 0);
        tabelaProdutos = new JTable(modeloTabela);

        // Evento de clique na tabela
        tabelaProdutos.getSelectionModel().addListSelectionListener(e -> {
            int linha = tabelaProdutos.getSelectedRow();
            if (linha >= 0) {
                // Preenche os campos da outra aba com os dados da linha clicada
                txtId.setText(modeloTabela.getValueAt(linha, 0).toString());
                txtNome.setText(modeloTabela.getValueAt(linha, 1).toString());
                txtCodBarras.setText(modeloTabela.getValueAt(linha, 2).toString());
                txtPreco.setText(modeloTabela.getValueAt(linha, 3).toString().replace(",", "."));
                txtCusto.setText(modeloTabela.getValueAt(linha, 4).toString().replace(",", "."));
                txtQtd.setText(modeloTabela.getValueAt(linha, 5).toString());
            }
        });

        painel.add(painelBusca, BorderLayout.NORTH); // Adiciona a busca no topo
        painel.add(new JScrollPane(tabelaProdutos), BorderLayout.CENTER);

        return painel;
    }

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
            service.salvar(p);

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
        // Tratamento para aceitar virgula ou ponto no preço
        double preco = Double.parseDouble(txtPreco.getText().replace(",", "."));
        double custo = Double.parseDouble(txtCusto.getText().replace(",", "."));
        int qtd = Integer.parseInt(txtQtd.getText());

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

    private void pesquisarProduto(String termo) {
        if (termo.isEmpty()) {
            carregarTabela(); // Se estiver vazio, carrega tudo
            return;
        }

        modeloTabela.setRowCount(0); // Limpa tabela
        try {
            // Chama o service
            List<Produto> lista = service.buscarPorNome(termo);

            for (Produto p : lista) {
                modeloTabela.addRow(new Object[]{
                        p.getId(), p.getNome(), p.getCodBarras(), p.getPreco(), p.getCustoMedio(), p.getQtdEstoque()
                });
            }

            if (lista.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nenhum produto encontrado com esse nome.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro na busca: " + e.getMessage());
        }
    }
}