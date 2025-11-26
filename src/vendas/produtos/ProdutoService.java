package vendas.produtos;

import vendas.db.Conexao; // Vamos criar esse arquivo na raiz vendas
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutoService implements IProdutoService {

    @Override
    public void salvar(Produto p) {
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
            throw new RuntimeException("Erro ao salvar produto: " + e.getMessage());
        }
    }

    @Override
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
            throw new RuntimeException("Erro ao atualizar produto: " + e.getMessage());
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
            throw new RuntimeException("Erro ao excluir produto: " + e.getMessage());
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
                Produto p = new Produto();
                p.setId(rs.getInt("id"));
                p.setNome(rs.getString("nome"));
                p.setCodBarras(rs.getString("cod_barras"));
                p.setPreco(rs.getDouble("preco"));
                p.setCustoMedio(rs.getDouble("custo_medio"));
                p.setQtdEstoque(rs.getInt("qtd_estoque"));
                lista.add(p);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar produtos: " + e.getMessage());
        }
        return lista;


    }

    @Override
    public List<Produto> buscarPorNome(String nome) {
        // O operador LIKE com % permite buscar partes do nome (ex: "Arr" acha "Arroz")
        String sql = "SELECT * FROM produto WHERE nome LIKE ?";
        List<Produto> lista = new ArrayList<>();

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + nome + "%"); // Adiciona % antes e depois

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Produto p = new Produto();
                    p.setId(rs.getInt("id"));
                    p.setNome(rs.getString("nome"));
                    p.setCodBarras(rs.getString("cod_barras"));
                    p.setPreco(rs.getDouble("preco"));
                    p.setCustoMedio(rs.getDouble("custo_medio"));
                    p.setQtdEstoque(rs.getInt("qtd_estoque"));
                    lista.add(p);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar produto por nome: " + e.getMessage());
        }
        return lista;
    }
}
