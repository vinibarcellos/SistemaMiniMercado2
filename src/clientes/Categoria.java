package clientes;

    // Criamos o enum Categoria para classificar os clientes e seus respectivos descontos
    // Dividimos em 3 categorias
public enum Categoria {
    CLIENTECOMUM(0.0),
    CLIENTEOURO(0.10),
    CLIENTEPLATINA(0.20);

    private final double descontoPercentual;

    Categoria(double descontoPercentual) {
        this.descontoPercentual = descontoPercentual;
    }

    // Retorna o desconto
    public double getDescontoPercentual() {
        return descontoPercentual;
    }
}
