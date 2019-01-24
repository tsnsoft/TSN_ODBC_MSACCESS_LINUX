package tsn.odbc.msaccess.linux;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

public class App1 {

    public static void main(String[] args) {
        // РАБОТА С БАЗОЙ ДАННЫХ MS ACCESS В LINUX
        try {
            // Имя файла базы данных (без расширения)
            String dbName = "ts_db_demo.mdb";

            // Создание свойств соединения с базой данных
            Properties authorization = new Properties();
            authorization.put("user", ""); // Зададим имя пользователя БД
            authorization.put("password", ""); // Зададим пароль доступа в БД
            authorization.put("charSet", "Cp1251"); // Зададим кодировку данных в БД

            // Считываем каталог запуска программы (в режиме отладки это корневая папка проекта)
            String dir = new File(".").getAbsoluteFile().getParentFile().getAbsolutePath()
                    + System.getProperty("file.separator");

            // Создание соединения с базой данных
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            Connection connection = DriverManager.getConnection("jdbc:ucanaccess://" + 
                    dir + dbName + ";openExclusive=true;ignoreCase=true", authorization);

            // Создание оператора доступа к базе данных
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);

            // Выполнение запроса к базе данных, получение набора данных
            ResultSet table = statement.executeQuery("SELECT * FROM contractor");

            table.first(); // Выведем имена полей
            for (int j = 1; j <= table.getMetaData().getColumnCount(); j++) {
                System.out.print(table.getMetaData().getColumnName(j) + "\t\t");
            }
            System.out.println();

            table.beforeFirst(); // Выведем записи таблицы
            while (table.next()) {
                for (int j = 1; j <= table.getMetaData().getColumnCount(); j++) {
                    System.out.print(table.getString(j) + "\t\t");
                }
                System.out.println();
            }

            if (table != null) {
                table.close();
            } // Закрытие набора данных
            if (statement != null) {
                statement.close();
            } // Закрытие базы данных
            if (connection != null) {
                connection.close();
            } // Отключение от базы данных

        } catch (Exception e) {
            System.err.println("Error accessing database!");
            e.printStackTrace();
        }
    }

}
