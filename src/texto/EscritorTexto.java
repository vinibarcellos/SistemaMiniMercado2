package texto;

import vendas.produtos.Produto;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class EscritorTexto {

    // Gerar o estoque.txt
    public static void escreverEstoque(List<Produto> produtos) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("estoque.txt"))) {
            writer.println(String.format("%-5s | %-30s | %s", "ID", "PRODUTO", "QUANTIDADE"));
            writer.println("-------------------------------------------------");

            for (Produto p : produtos) {
                // Formatação: %-5s
                writer.println(String.format("%-5d | %-30s | %d",
                        p.getId(),
                        p.getNome(),
                        p.getQtdEstoque()));
            }
            writer.println("-------------------------------------------------");
            writer.println("Total de itens listados: " + produtos.size());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Gerar catálogo.txt
    public static void escreverCatalogo(List<Produto> produtos) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("catalogo.txt"))) {
            writer.println(String.format("%-30s | %-15s | %s", "PRODUTO", "CÓD. BARRAS", "PREÇO (R$)"));
            writer.println("-----------------------------------------------------------");

            for (Produto p : produtos) {
                writer.println(String.format("%-30s | %-15s | R$ %.2f",
                        p.getNome(),
                        p.getCodBarras(),
                        p.getPreco()));
            }
            writer.println("-----------------------------------------------------------");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
