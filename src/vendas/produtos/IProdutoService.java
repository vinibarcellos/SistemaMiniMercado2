package vendas.produtos;

import java.util.List;

public interface IProdutoService {
    void salvar(Produto produto);
    void atualizar(Produto produto);
    void excluir(int id);
    List<Produto> listarTodos();
}