package vendas.produtos;

public class Produto {
    private int id;
    private String nome;
    private String codBarras;
    private double preco;
    private double custoMedio;
    private int qtdEstoque;

    public Produto() {}

    public Produto(int id, String nome, String codBarras, double preco, double custoMedio, int qtdEstoque) {
        this.id = id;
        this.nome = nome;
        this.codBarras = codBarras;
        this.preco = preco;
        this.custoMedio = custoMedio;
        this.qtdEstoque = qtdEstoque;
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getCodBarras() { return codBarras; }
    public void setCodBarras(String codBarras) { this.codBarras = codBarras; }
    public double getPreco() { return preco; }
    public void setPreco(double preco) { this.preco = preco; }
    public double getCustoMedio() { return custoMedio; }
    public void setCustoMedio(double custoMedio) { this.custoMedio = custoMedio; }
    public int getQtdEstoque() { return qtdEstoque; }
    public void setQtdEstoque(int qtdEstoque) { this.qtdEstoque = qtdEstoque; }
}