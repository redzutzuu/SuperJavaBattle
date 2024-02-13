package dataBase;

import java.sql.*;

public class DB {
    private Connection connection;
    private Statement statement;

    // ceastă metodă verifică dacă tabela GAME_INFO este goală în baza de date. Se execută o interogare SQL pentru a obține
    // numărul de înregistrări din tabela respectivă. Dacă rezultatul este mai mare decât zero, se returnează false, ceea ce
    // înseamnă că tabela nu este goală. Dacă rezultatul este zero, se returnează true, ceea ce înseamnă că tabela este goală.
    // În caz contrar, se returnează false. De asemenea, se realizează un commit al conexiunii la baza de date.
    public boolean isEmpty() {
        try {
            ResultSet rs = statement.executeQuery("select count(*) from GAME_INFO");
            int rez = rs.getInt(1);
            if (rez > 0)
                return false;
            if (rez == 0)
                return true;
            connection.commit();
        } catch (Exception e) {
            System.out.println("Eroare isEmpty()!");
        }
        return false;
    }

    // Această metodă creează tabela SCORE în baza de date și inserează o înregistrare cu valorile implicite (ID = 1, POTIUNI = 0, SCOR = 0).
    // În primul rând, se încarcă driverul JDBC pentru baza de date SQLite. Apoi, se stabilește o conexiune cu baza de date utilizând
    // URL-ul specificat. Se definește un șir SQL care reprezintă instrucțiunea de creare a tabelei SCORE. Se creează o declarație (Statement)
    // și se execută instrucțiunea SQL pentru crearea tabelei. Apoi, se definește un alt șir SQL pentru inserarea valorilor implicite în tabela SCORE.
    // Se executa instrucțiunea SQL de inserare. La final, se închid declarația și conexiunea.
    public void createTable() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        String url = "jdbc:sqlite:SJB.db";
        try (Connection connection1 = DriverManager.getConnection(url)) {
            String sql = "CREATE TABLE IF NOT EXISTS SCORE (\n"
                    + "id integer PRIMARY KEY, \n"
                    + "POTIUNI integer NOT NULL, \n"
                    + "SCOR integer NOT NULL \n"
                    + ");";
            Statement statement1 = connection1.createStatement();
            statement1.execute(sql);
            sql = "INSERT OR IGNORE INTO SCORE(ID, POTIUNI, SCOR)" + "VALUES (1, 0, 0);";
            statement1.execute(sql);
            statement1.close();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    // Această metodă returnează scorul din tabela SCORE. Se stabilește o conexiune cu baza de date și se creează o declarație.
    // Se execută o interogare SQL pentru a obține scorul din înregistrarea cu ID-ul 1. Se obține scorul din rezultatul interogării
    // și se închid rezultatul și declarația. La final, se returnează scorul obținut.
    public int getScore() {
        String url = "jdbc:sqlite:SJB.db";
        try (Connection connection1 = DriverManager.getConnection(url)) {
            Statement statement1 = connection1.createStatement();
            ResultSet rs = statement1.executeQuery("SELECT SCOR FROM SCORE WHERE ID=1;");
            int score = rs.getInt("scor");
            rs.close();
            statement1.close();
            connection1.close();
            return score;
        } catch (SQLException e) {
            System.out.println(e);
        }
        return -1;
    }

    //  Această metodă actualizează scorul din tabela SCORE cu valoarea specificată. Se stabilește o conexiune cu baza de date și
    //  se creează o declarație. Se dezactivează commit-ul automat pentru conexiunea curentă. Se definește un șir SQL care realizează
    //  actualizarea scorului în tabela SCORE. Se executa instrucțiunea SQL de actualizare. Se realizează commit-ul conexiunii și se închid
    //  declarația și conexiunea.
    public void setScore(int score) {
        String url = "jdbc:sqlite:SJB.db";
        try (Connection connection1 = DriverManager.getConnection(url)) {
            Statement statement1 = connection1.createStatement();
            connection1.setAutoCommit(false);
            String sql = "UPDATE SCORE set SCOR =" + score + " where ID=1;";
            statement1.executeUpdate(sql);
            connection1.commit();
            statement1.close();
            connection1.close();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

}
