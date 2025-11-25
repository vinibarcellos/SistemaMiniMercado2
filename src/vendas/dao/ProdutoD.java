package vendas.dao;

import vendas.db.Conexao;
import vendas.produtos.Produto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutoD {

    public void salvar(Produto p) {
        // SQL atualizado com as novas colunas
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
            throw new RuntimeException("Erro ao salvar: " + e.getMessage());
        }
    }

    public void atualizar(Produto p) {
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
            throw new RuntimeException("Erro ao atualizar: " + e.getMessage());
        }
    }

    public void excluir(int id) {
        String sql = "DELETE FROM produto WHERE id=?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir: " + e.getMessage());
        }
    }

    public List<Produto> listarTodos() {
        String sql = "SELECT * FROM produto";
        List<Produto> produtos = new ArrayList<>();
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Produto p = new Produto();
                p.setId(rs.getInt("id"));
                p.setNome(rs.getString("nome"));
                p.setCodBarras(rs.getString("cod_barras")); // Novo
                p.setPreco(rs.getDouble("preco"));
                p.setCustoMedio(rs.getDouble("custo_medio")); // Novo
                p.setQtdEstoque(rs.getInt("qtd_estoque"));
                produtos.add(p);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar: " + e.getMessage());
        }
        return produtos;
    }
}