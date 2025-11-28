package vendas.produtos;

import vendas.db.Conexao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutoService implements IProdutoService {

    private void validar(Produto p) {
        // 1. Validação de Nome
        if (p.getNome() == null || p.getNome().trim().isEmpty()) {
            throw new RuntimeException("Erro: O Nome do produto é obrigatório.");
        }

        // 2. Validação de Código de Barras (Obrigatório e NÃO negativo)
        if (p.getCodBarras() == null || p.getCodBarras().trim().isEmpty()) {
            throw new RuntimeException("Erro: O Código de Barras é obrigatório.");
        }

        // --- AQUI ESTÁ A CORREÇÃO QUE VOCÊ PEDIU ---
        if (p.getCodBarras().trim().startsWith("-")) {
            throw new RuntimeException("Erro: O Código de Barras não pode ser negativo.");
        }

        // Se quiser ser mais rigoroso e garantir que só tenha números (sem letras), descomente abaixo:
        // if (!p.getCodBarras().matches("[0-9]+")) throw new RuntimeException("Erro: O Código de Barras deve conter apenas números.");

        // 3. Validações de Valores (Numéricos)
        if (p.getPreco() < 0) {
            throw new RuntimeException("Erro: O Preço de Venda não pode ser negativo.");
        }
        if (p.getCustoMedio() < 0) {
            throw new RuntimeException("Erro: O Custo Médio não pode ser negativo.");
        }
        if (p.getQtdEstoque() < 0) {
            throw new RuntimeException("Erro: A Quantidade em Estoque não pode ser negativa.");
        }
    }

    @Override
    public void salvar(Produto p) {
        validar(p); // Valida antes de salvar

        String sql = "INSERT INTO produto (nome, cod_barras, preco, custo_medio, qtd_estoque) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, p.getNome());
            stmt.setString(2, p.getCodBarras());
            stmt.setDouble(3, p.getPreco());
            stmt.setDouble(4, p.getCustoMedio());
            stmt.setInt(5, p.getQtdEstoque());
            stmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Erro de Banco ao salvar: " + e.getMessage());
        }
    }

    @Override
    public void atualizar(Produto p) {
        validar(p); // Valida antes de editar

        String sql = "UPDATE produto SET nome=?, cod_barras=?, preco=?, custo_medio=?, qtd_estoque=? WHERE id=?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, p.getNome());
            stmt.setString(2, p.getCodBarras());
            stmt.setDouble(3, p.getPreco());
            stmt.setDouble(4, p.getCustoMedio());
            stmt.setInt(5, p.getQtdEstoque());
            stmt.setInt(6, p.getId());
            stmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Erro de Banco ao atualizar: " + e.getMessage());
        }
    }

    @Override
    public void excluir(int id) {
        String sql = "DELETE FROM produto WHERE id=?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir (Pode haver vendas vinculadas): " + e.getMessage());
        }
    }

    @Override
    public List<Produto> listarTodos() {
        String sql = "SELECT * FROM produto";
        List<Produto> lista = new ArrayList<>();
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(montarProdutoDoBanco(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public List<Produto> buscarPorNome(String nome) {
        String sql = "SELECT * FROM produto WHERE nome LIKE ?";
        List<Produto> lista = new ArrayList<>();
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + nome + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(montarProdutoDoBanco(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar: " + e.getMessage());
        }
        return lista;
    }

    private Produto montarProdutoDoBanco(ResultSet rs) throws SQLException {
        Produto p = new Produto();
        p.setId(rs.getInt("id"));
        p.setNome(rs.getString("nome"));
        p.setCodBarras(rs.getString("cod_barras"));
        p.setPreco(rs.getDouble("preco"));
        p.setCustoMedio(rs.getDouble("custo_medio"));
        p.setQtdEstoque(rs.getInt("qtd_estoque"));
        return p;
    }
}