package vendas.view;

import vendas.produtos.IProdutoService;
import vendas.produtos.Produto;
import vendas.produtos.ProdutoService;

import javax.swing.*;
import java.awt.*;

public class InterfaceGUI extends JFrame {
    private IProdutoService service;
    private JTabbedPane abas; // Transformei em variável da classe para poder focar

    // Campos
    private JTextField txtId, txtNome, txtCodBarras, txtPreco, txtCusto, txtQtd;

    public InterfaceGUI() {
        this.service = new ProdutoService();

        setTitle("Sistema Mercado 2 Amigos - Modular");
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        abas = new JTabbedPane();

        // Aba 1: Cadastro
        abas.add("Cadastro", criarPainelCadastro());
        abas.add("Relatório de Estoque", new RelatorioEstoque(service, this::carregarParaEdicao));
        abas.add("Catálogo de Vendas", new CatalogoProduto(service, this::carregarParaEdicao));
        add(abas);
    }

    private void carregarParaEdicao(int id) {
        try {
            // Busca o produto completo no banco (ou na lista)
            Produto produtoEncontrado = null;
            for(Produto p : service.listarTodos()) {
                if(p.getId() == id) {
                    produtoEncontrado = p;
                    break;
                }
            }

            if (produtoEncontrado != null) {
                // Preenche os campos
                txtId.setText(String.valueOf(produtoEncontrado.getId()));
                txtNome.setText(produtoEncontrado.getNome());
                txtCodBarras.setText(produtoEncontrado.getCodBarras());
                txtPreco.setText(String.valueOf(produtoEncontrado.getPreco()).replace(".", ","));
                txtCusto.setText(String.valueOf(produtoEncontrado.getCustoMedio()).replace(".", ","));
                txtQtd.setText(String.valueOf(produtoEncontrado.getQtdEstoque()));

                // Volta o foco para a aba de Cadastro automaticamente
                abas.setSelectedIndex(0);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar produto: " + e.getMessage());
        }
    }

    private JPanel criarPainelCadastro() {
        JPanel painel = new JPanel(new BorderLayout());
        JPanel form = new JPanel(new GridLayout(6, 2, 10, 10));
        form.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        form.add(new JLabel("ID:")); txtId = new JTextField(); txtId.setEditable(false); txtId.setBackground(Color.LIGHT_GRAY); form.add(txtId);
        form.add(new JLabel("Nome:")); txtNome = new JTextField(); form.add(txtNome);
        form.add(new JLabel("Cód. Barras:")); txtCodBarras = new JTextField(); form.add(txtCodBarras);
        form.add(new JLabel("Preço (R$):")); txtPreco = new JTextField(); form.add(txtPreco);
        form.add(new JLabel("Custo (R$):")); txtCusto = new JTextField(); form.add(txtCusto);
        form.add(new JLabel("Estoque:")); txtQtd = new JTextField(); form.add(txtQtd);

        JPanel botoes = new JPanel(new FlowLayout());
        JButton btnSalvar = new JButton("Salvar");
        JButton btnEditar = new JButton("Editar");
        JButton btnExcluir = new JButton("Excluir");
        JButton btnLimpar = new JButton("Limpar");

        btnSalvar.addActionListener(e -> acaoSalvar());
        btnEditar.addActionListener(e -> acaoEditar());
        btnExcluir.addActionListener(e -> acaoExcluir());
        btnLimpar.addActionListener(e -> limparCampos());

        botoes.add(btnSalvar); botoes.add(btnEditar); botoes.add(btnExcluir); botoes.add(btnLimpar);
        painel.add(form, BorderLayout.NORTH);
        painel.add(botoes, BorderLayout.CENTER);
        return painel;
    }

    private void acaoSalvar() {
        try {
            Produto p = montarProduto();
            service.salvar(p);
            JOptionPane.showMessageDialog(this, "Produto salvo com sucesso!");
            limparCampos();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar: " + e.getMessage());
        }
    }

    private void acaoEditar() {
        try {
            if(txtId.getText().isEmpty()) throw new RuntimeException("Selecione um ID para editar.");
            Produto p = montarProduto();
            p.setId(Integer.parseInt(txtId.getText()));
            service.atualizar(p);
            JOptionPane.showMessageDialog(this, "Atualizado com sucesso!");
            limparCampos();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao editar: " + e.getMessage());
        }
    }

    private void acaoExcluir() {
        try {
            if(txtId.getText().isEmpty()) throw new RuntimeException("Selecione um ID para excluir.");
            service.excluir(Integer.parseInt(txtId.getText()));
            JOptionPane.showMessageDialog(this, "Excluído com sucesso!");
            limparCampos();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao excluir: " + e.getMessage());
        }
    }

    private Produto montarProduto() {
        String nome = txtNome.getText();
        String barras = txtCodBarras.getText();
        double preco = Double.parseDouble(txtPreco.getText().replace(",", "."));
        double custo = Double.parseDouble(txtCusto.getText().replace(",", "."));
        int qtd = Integer.parseInt(txtQtd.getText());
        return new Produto(0, nome, barras, preco, custo, qtd);
    }

    private void limparCampos() {
        txtId.setText(""); txtNome.setText(""); txtCodBarras.setText("");
        txtPreco.setText(""); txtCusto.setText(""); txtQtd.setText("");
    }
}