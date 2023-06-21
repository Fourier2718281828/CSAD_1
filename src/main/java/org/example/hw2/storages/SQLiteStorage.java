package org.example.hw2.storages;

import org.example.exceptions.StorageException;
import org.example.hw2.goods.Good;
import org.example.hw2.goods.GoodsGroup;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;

public class SQLiteStorage implements GroupedGoodStorage {
    private SQLiteStorage() {
        try {
            Class.forName("org.sqlite.JDBC");
            this.dbConnection = DriverManager.getConnection("jdbc:sqlite:" + dbName);
            createGoodTable();
            createGroupTable();
        } catch (ClassNotFoundException | SQLException e) {

        }
    }

    private void createGoodTable() throws SQLException {
        final var sqlCreation = """
                CREATE TABLE Good (
                  good_id INT PRIMARY KEY AUTO_INCREMENT,
                  good_name VARCHAR(255),
                  quantity INT,
                  price DECIMAL(12,2),
                  FOREIGN KEY (group_id) REFERENCES Group(group_id)
                );
                """;
        executeStatement(sqlCreation);
    }

    private void createGroupTable() throws SQLException {
        final var sqlCreation = """
                CREATE TABLE Group (
                    group_id INT PRIMARY KEY AUTO_INCREMENT,
                    group_name VARCHAR(255),
                );
                """;
        executeStatement(sqlCreation);
    }

    public static SQLiteStorage getInstance() {
        if(instance == null) {
            instance = new SQLiteStorage();
        }
        return instance;
    }

    private void executeStatement(String sql) throws SQLException {
        final var statement = dbConnection.createStatement();
        statement.executeQuery(sql);
    }

    @Override
    public void addGoodToGroup(Good good, String groupName) throws StorageException {

    }

    @Override
    public Optional<Good> getGood(String goodName) {
        return Optional.empty();
    }

    @Override
    public void updateGood(Good good) throws StorageException {

    }

    @Override
    public void deleteGood(String name) throws StorageException {

    }

    @Override
    public void createGroup(GoodsGroup newGroup) throws StorageException {

    }

    @Override
    public Optional<GoodsGroup> getGroup(String name) {
        return Optional.empty();
    }

    @Override
    public void updateGroup(GoodsGroup group) throws StorageException {

    }

    @Override
    public void deleteGroup(String name) throws StorageException {

    }

    private Connection dbConnection;
    private static SQLiteStorage instance;
    private static final String dbName = "GroupedGoodStorage";
}
