package vendas.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {
    private static final String URL = "jdbc:mysql://localhost:3306/mercado2amigos";
    private static final String USER = "root";
    private static final String PASSWORD = "20112004"; // <-- SUA SENHA AQUI

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}