package clientes;

import java.util.HashMap;
import java.util.Map;

// Criamos um Map(array que guarda dados em formato key:value como se fosse o "banco de dados" dos clientes cadastrados.

public class ClienteService implements IClienteService {
    private final Map<Integer, Cliente> clientes;

    // Construtor
    public ClienteService() {
        this.clientes = new HashMap<>();
    }

    // Tratamento de exceções
    @Override
    public void cadastrarCliente(Cliente cliente) {
        // Testes de validação (Dados incompletos / inválidos)
        if (cliente == null) {
            throw new IllegalArgumentException("Não é possível cadastrar um cliente nulo.");
        }
        if (this.clientes.containsKey(cliente.getId())) {
            throw new RuntimeException("[SERVIÇO CLIENTE] Erro: Cliente com ID " + cliente.getId() + " já existe.");
        }
        this.clientes.put(cliente.getId(), cliente);
    }

    @Override
    public Cliente consultarCliente(int id) {
        Cliente cliente = this.clientes.get(id);
        if (cliente == null) {
            throw new RuntimeException("[SERVIÇO CLIENTE] Erro: Cliente com ID " + id + " não encontrado.");
        }
        return cliente;
    }

    @Override
    public Cliente[] listarClientes() {
        Cliente[] clientes = new Cliente[this.clientes.size()];
        return this.clientes.values().toArray(clientes);
    }
}