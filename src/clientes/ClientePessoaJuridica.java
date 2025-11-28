package clientes;

    // O mesmo que fizemos para ClientePessoaFisica e aplicamos na PessoaJuridica

public class ClientePessoaJuridica extends Cliente {
    private String cnpj;

    // Construtor
    public ClientePessoaJuridica(int id, String nome, String telefone, Categoria categoria, String cnpj) {
        super(id, nome, telefone, categoria);
        this.cnpj = cnpj;
    }

    // Getters e Setters
    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }
}
