package clientes;

    // Criamos uma interface para ClienteService com os metodos que a classe tera

public interface IClienteService {
    void cadastrarCliente(Cliente cliente);
    Cliente consultarCliente(int id);
    Cliente[] listarClientes();
}
