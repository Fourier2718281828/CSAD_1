package org.example.hw2.storages;

import org.example.exceptions.StorageException;
import org.example.hw2.goods.Good;
import org.example.hw2.goods.GoodsGroup;
import org.example.hw2.goods.Group;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

public class SQLiteStorage implements AutoCloseableStorage {
    private enum CRUD {
        CREATE_GROUP, GET_GROUP, UPDATE_GROUP, DELETE_GROUP,
        CREATE_GOOD, GET_GOOD, UPDATE_GOOD, DELETE_GOOD,
    }

    private SQLiteStorage() {
        try {
            Class.forName("org.sqlite.JDBC");
            this.dbConnection = DriverManager.getConnection("jdbc:sqlite:" + dbName);
            initTables();
            initPreparedStatements();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void initPreparedStatements() throws SQLException {
        this.createGroup = dbConnection.prepareStatement(sqlCodes.get(CRUD.CREATE_GROUP));
        this.getGroup    = dbConnection.prepareStatement(sqlCodes.get(CRUD.GET_GROUP));
        //this.updateGroup = dbConnection.prepareStatement(sqlCodes.get(CRUD.UPDATE_GROUP));
        this.deleteGroup = dbConnection.prepareStatement(sqlCodes.get(CRUD.DELETE_GROUP));

        //this.createGood  = dbConnection.prepareStatement(sqlCodes.get(CRUD.CREATE_GOOD));
        //this.getGood     = dbConnection.prepareStatement(sqlCodes.get(CRUD.GET_GOOD));
        //this.updateGood  = dbConnection.prepareStatement(sqlCodes.get(CRUD.UPDATE_GOOD));
        //this.deleteGood  = dbConnection.prepareStatement(sqlCodes.get(CRUD.DELETE_GOOD));
    }

    private void initTables() throws SQLException {
        createGroupTable();
        createGoodTable();
    }

    private void createGoodTable() throws SQLException {
        final var sqlCreation = """
                CREATE TABLE IF NOT EXISTS Good (
                  good_id INTEGER PRIMARY KEY AUTOINCREMENT,
                  good_name TEXT NOT NULL,
                  quantity INTEGER NOT NULL,
                  price REAL NOT NULL,
                  group_id INTEGER,
                  FOREIGN KEY (group_id) REFERENCES GoodsGroup(group_id)
                );
                """;
        executeStatement(sqlCreation);
    }

    private void createGroupTable() throws SQLException {
        final var sqlCreation = """
                CREATE TABLE IF NOT EXISTS GoodsGroup (
                    group_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    group_name TEXT NOT NULL
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
        try (var statement = dbConnection.createStatement()){
            statement.executeUpdate(sql);
        }
    }

    @Override
    public void close() throws Exception {
        createGroup.close();
        getGroup.close();
        //updateGroup.close();
        deleteGroup.close();
        //TODO uncomment when initialized
        //createGood.close();
        //getGood.close();
        //updateGood.close();
        //deleteGood.close();

        dbConnection.close();
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
        try {
            createGroup.setString(1, newGroup.getName());
            int rowsModified = createGroup.executeUpdate();
            if(rowsModified <= 0)
                throw new StorageException("The group with name " + newGroup.getName() + " already exists.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<GoodsGroup> getGroup(String name) {
        try {
            getGroup.setString(1, name);
            var res = getGroup.executeQuery();
            if(!res.next())
                return Optional.empty();
            var groupName = res.getString("group_name");
            // TODO make to get goods and not only the group's name
            // var goods = null;
            return Optional.of(new Group(groupName));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateGroup(GoodsGroup group) throws StorageException {

    }

    @Override
    public void deleteGroup(String name) throws StorageException {
        try {
            deleteGroup.setString(1, name);
            var rowsDeleted = deleteGroup.executeUpdate();
            if(rowsDeleted == 0)
                throw new StorageException("Cannot delete a non-existent group: " + name);
            assert(rowsDeleted == 1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Connection dbConnection;
    private PreparedStatement createGroup;
    private PreparedStatement getGroup;
    private PreparedStatement updateGroup;
    private PreparedStatement deleteGroup;
    private PreparedStatement createGood;
    private PreparedStatement getGood;
    private PreparedStatement updateGood;
    private PreparedStatement deleteGood;
    private static SQLiteStorage instance;
    private static final String dbName = "GroupedGoodStorage";
    private static Map<CRUD, String> sqlCodes = new TreeMap<>();

    static {
        sqlCodes.put(CRUD.CREATE_GROUP, """
                INSERT INTO 'GoodsGroup' (group_name)
                VALUES ( ? );
                """);
        sqlCodes.put(CRUD.GET_GROUP, """
                SELECT *
                FROM 'GoodsGroup'
                WHERE group_name = ( ? );
                """);
        sqlCodes.put(CRUD.UPDATE_GROUP, """
                """);
        sqlCodes.put(CRUD.DELETE_GROUP, """
                DELETE
                FROM 'GoodsGroup'
                WHERE group_name = ( ? )
                """);

        sqlCodes.put(CRUD.CREATE_GOOD, """
                """);
        sqlCodes.put(CRUD.GET_GOOD, """
                """);
        sqlCodes.put(CRUD.UPDATE_GOOD, """
                """);
        sqlCodes.put(CRUD.DELETE_GOOD, """
                """);
    }
}
