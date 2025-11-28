package clientes;

    // Aqui criamos a Subclasse que estende a classe mae e adiciona a variavel cpf

public class ClientePessoaFisica extends Cliente {
    private String cpf;

    // Construtor
    public ClientePessoaFisica(int id, String nome, String telefone, Categoria categoria, String cpf) {
        super(id, nome, telefone, categoria);
        this.cpf = cpf;
    }

    // Getters e Setters
    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
}
