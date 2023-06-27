package org.example.hw4;

import org.example.exceptions.storage.StorageException;
import org.example.hw2.goods.Good;
import org.example.hw2.goods.GoodsGroup;
import org.example.hw2.goods.Group;
import org.example.hw2.goods.StandardGood;
import org.example.hw4.criteria.Criterion;

import java.sql.*;
import java.util.*;

public class SQLiteStorage implements DataBase {
    private enum CRUD {
        CREATE_GROUP, GET_GROUP, DELETE_GROUP,
        CREATE_GOOD, GET_GOOD, UPDATE_GOOD, DELETE_GOOD,
        GET_GOODS_OF_GROUP
    }

    private SQLiteStorage() {
        try {
            Class.forName("org.sqlite.JDBC");
            this.dbConnection = DriverManager.getConnection("jdbc:sqlite:" + dbFileName);
            initTables();
            initPreparedStatements();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void initPreparedStatements() throws SQLException {
        this.createGroup = dbConnection.prepareStatement(sqlCodes.get(CRUD.CREATE_GROUP));
        this.getGroup    = dbConnection.prepareStatement(sqlCodes.get(CRUD.GET_GROUP));
        this.deleteGroup = dbConnection.prepareStatement(sqlCodes.get(CRUD.DELETE_GROUP));

        this.createGood  = dbConnection.prepareStatement(sqlCodes.get(CRUD.CREATE_GOOD));
        this.getGood     = dbConnection.prepareStatement(sqlCodes.get(CRUD.GET_GOOD));
        this.updateGood  = dbConnection.prepareStatement(sqlCodes.get(CRUD.UPDATE_GOOD));
        this.deleteGood  = dbConnection.prepareStatement(sqlCodes.get(CRUD.DELETE_GOOD));

        this.getGoodsOfGroup = dbConnection.prepareStatement(sqlCodes.get(CRUD.GET_GOODS_OF_GROUP));
    }

    private void initTables() throws SQLException {
        createGroupTable();
        createGoodTable();
    }

    private void createGoodTable() throws SQLException {
        final var sqlCreation = """
                CREATE TABLE IF NOT EXISTS Good (
                  good_id INTEGER PRIMARY KEY AUTOINCREMENT,
                  good_name TEXT NOT NULL UNIQUE,
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
                    group_name TEXT NOT NULL UNIQUE
                );
                """;
        executeStatement(sqlCreation);
    }

    private static final class InstanceHolder {
        private static final SQLiteStorage instance = new SQLiteStorage();
    }

    public static SQLiteStorage getInstance() {
        return InstanceHolder.instance;
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
        deleteGroup.close();

        createGood.close();
        getGood.close();
        updateGood.close();
        deleteGood.close();

        getGoodsOfGroup.close();

        dbConnection.close();
    }

    @Override
    public synchronized void addGoodToGroup(Good good, String groupName) throws StorageException {
        //synchronized (createGood) {
            try {
                createGood.setString(1, good.getName());
                createGood.setInt(2, good.getQuantity());
                createGood.setDouble(3, good.getPrice());
                createGood.setString(4, groupName);
                var rowsModified = createGood.executeUpdate();
                if(rowsModified != 1)
                    throw new StorageException("Cannot add the already existent good: " + good.getName());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        //}
    }

    @Override
    public Optional<Good> getGood(String goodName) {
        try {
            getGood.setString(1, goodName);
            var res = getGood.executeQuery();
            if(!res.next())
                return Optional.empty();
            var foundGoodName = res.getString("good_name");
            var foundGoodQuantity = res.getInt("quantity");
            var foundGoodPrice = res.getDouble("price");
            return Optional.of(new StandardGood(foundGoodName, foundGoodQuantity, foundGoodPrice));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized void updateGood(Good good) throws StorageException {
        //synchronized (updateGood) {
            try {
                updateGood.setInt(1, good.getQuantity());
                updateGood.setDouble(2, good.getPrice());
                updateGood.setString(3, good.getName());
                var rowsModified = updateGood.executeUpdate();
                if(rowsModified == 0)
                    throw new StorageException("Cannot update a non-existent good: " + good.getName());
                if(rowsModified != 1)
                    throw new SQLException("DB invariant broken: database allows having several goods with one name.");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
       //}
    }

    @Override
    public synchronized void deleteGood(String name) throws StorageException {
        //synchronized (deleteGood) {
            try {
                deleteGood.setString(1, name);
                var rowsModified = deleteGood.executeUpdate();
                if(rowsModified == 0)
                    throw new StorageException("Cannot delete a non-existent good: " + name);
                if(rowsModified != 1)
                    throw new SQLException("DB invariant broken: database allows having several goods with one name.");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        //}
    }

    @Override
    public Iterable<Good> getGoodsListByCriterion(Criterion criterion) throws StorageException {
        var columnNameCheck = hasInvalidColumnName(criterion);
        if(columnNameCheck.isPresent())
            throw new StorageException("Criterion contains an invalid column name: " + columnNameCheck.get());
        final var sql = """
                SELECT *
                FROM 'Good'
                WHERE\s""" + criterion.getSQLRepresentation();
        System.out.println("Filter query: " + sql);
        try (var statement = dbConnection.createStatement()){
            ResultSet resultSet;
            try {
                resultSet = statement.executeQuery(sql);
            } catch (SQLException e) {
                throw new StorageException("Invalid sql code of criterion: " + criterion.getSQLRepresentation());
            }
            assert(resultSet != null);
            var res = new ArrayList<Good>();
            while(resultSet.next()) {
                var goodName = resultSet.getString("good_name");
                var goodQuantity = resultSet.getInt("quantity");
                var goodPrice = resultSet.getDouble("price");
                res.add(new StandardGood(goodName, goodQuantity, goodPrice));
            }
            return res;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Optional<String> hasInvalidColumnName(Criterion criterion) {
        var criterionColumnNames = criterion.getAllUsedColumnNames();
        for (var columnName : criterionColumnNames) {
            if(!columnNames.contains(columnName))
                return Optional.of(columnName);
        }
        return Optional.empty();
    }

    @Override
    public synchronized void createGroup(GoodsGroup newGroup) throws StorageException {
        //synchronized (createGroup) {
            try {
                createGroup.setString(1, newGroup.getName());
                int rowsModified = createGroup.executeUpdate();
                if(rowsModified != 1)
                    throw new SQLException("DB invariant broken: database modifies several rows when adding only one.");

                for (var good : newGroup.getGoods()) {
                    try {
                        addGoodToGroup(good, newGroup.getName());
                    } catch (StorageException e) {
                        deleteGroup(newGroup.getName());
                        throw new StorageException("Cannot add the good " + good.getName() +
                                " with group " + newGroup.getName() + " as it already exists in" +
                                "another group.");
                    }
                }

            } catch (SQLException e) {
                if(e.getMessage().contains("UNIQUE constraint failed"))
                    throw new StorageException("The group with name " + newGroup.getName() + " already exists.");
                else
                    throw new RuntimeException(e);
            }
        //}
    }

    @Override
    public Optional<GoodsGroup> getGroup(String name) {
        try {
            getGroup.setString(1, name);
            var queryRes = getGroup.executeQuery();
            if(!queryRes.next())
                return Optional.empty();
            var groupName = queryRes.getString("group_name");
            var goods = getGoodsOfGroup(groupName);
            var res = new Group(groupName);
            for(var good : goods) {
                res.addGood(good);
            }
            return Optional.of(res);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized void updateGroup(GoodsGroup group) throws StorageException {
        var gotGroup = getGroup(group.getName())
                .orElseThrow(() -> new StorageException("Cannot update a non-existent group " + group.getName()));
        for(var good : gotGroup.getGoods()) {
            deleteGood(good.getName());
        }
        for(var good : group.getGoods()) {
            try {
                addGoodToGroup(good, group.getName());
            } catch (StorageException e) {
                deleteGroup(gotGroup.getName());
                createGroup(gotGroup);
                throw new StorageException("Cannot add an already existent good " +
                        good.getName() + " while updating the group " + gotGroup.getName());
            }
        }
    }

    @Override
    public synchronized void deleteGroup(String name) throws StorageException {
        //synchronized (deleteGroup) {
            try {
                var gotGroup = getGroup(name)
                        .orElseThrow(() -> new StorageException("Cannot update a non-existent group " + name));
                for (var good : gotGroup.getGoods()) {
                    deleteGood(good.getName());
                }
                deleteGroup.setString(1, name);
                var rowsDeleted = deleteGroup.executeUpdate();
                assert(rowsDeleted == 1);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        //}
    }

    @Override
    public Iterable<GoodsGroup> getGroupsListByCriterion(Criterion criterion) throws StorageException {
        var columnNameCheck = hasInvalidColumnName(criterion);
        if(columnNameCheck.isPresent())
            throw new StorageException("Criterion contains an invalid column name: " + columnNameCheck.get());
        final var sql = """
                SELECT *
                FROM 'GoodsGroup'
                WHERE\s""" + criterion.getSQLRepresentation();
        System.out.println("Filter query (group): " + sql);
        try (var statement = dbConnection.createStatement()){
            ResultSet resultSet;
            try {
                resultSet = statement.executeQuery(sql);
            } catch (SQLException e) {
                throw new StorageException("Invalid sql code of criterion: " + criterion.getSQLRepresentation());
            }
            assert(resultSet != null);
            var res = new ArrayList<GoodsGroup>();
            while(resultSet.next()) {
                var groupName = resultSet.getString("group_name");
                var group = new Group(groupName);
                for(var good : getGoodsOfGroup(groupName)) {
                    group.addGood(good);
                }
                res.add(group);
            }
            return res;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Iterable<Good> getGoodsOfGroup(String groupName) {
        try {
            var res = new ArrayList<Good>();
            getGoodsOfGroup.setString(1, groupName);
            var queryRes = getGoodsOfGroup.executeQuery();
            while(queryRes.next()) {
                var foundGoodName = queryRes.getString("good_name");
                var foundGoodQuantity = queryRes.getInt("quantity");
                var foundGoodPrice = queryRes.getDouble("price");
                res.add(new StandardGood(foundGoodName, foundGoodQuantity, foundGoodPrice));
            }
            return res;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getFileName() {
        return dbFileName;
    }

    private Connection dbConnection;
    private PreparedStatement createGroup;
    private PreparedStatement getGroup;
    private PreparedStatement deleteGroup;
    private PreparedStatement createGood;
    private PreparedStatement getGood;
    private PreparedStatement updateGood;
    private PreparedStatement deleteGood;
    private PreparedStatement getGoodsOfGroup;
    private static final String dbFileName = "GroupedGoodStorage.sqlite";
    private static final Map<CRUD, String> sqlCodes = new TreeMap<>();
    private static final Set<String> columnNames = new TreeSet<>();

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
        sqlCodes.put(CRUD.DELETE_GROUP, """
                DELETE
                FROM 'GoodsGroup'
                WHERE group_name = ( ? )
                """);

        sqlCodes.put(CRUD.CREATE_GOOD, """
                INSERT INTO 'Good' (good_name, quantity, price, group_id)
                VALUES (( ? ), ( ? ), ( ? ), ( SELECT group_id
                                               FROM 'GoodsGroup'
                                               WHERE group_name = ( ? )
                                             )
                       );
                """);
        sqlCodes.put(CRUD.GET_GOOD, """
                SELECT *
                FROM 'Good'
                WHERE good_name = ( ? )
                """);
        sqlCodes.put(CRUD.UPDATE_GOOD, """
                UPDATE 'Good'
                SET quantity = ( ? ), price = ( ? )
                WHERE good_name = ( ? )
                """);
        sqlCodes.put(CRUD.DELETE_GOOD, """
                DELETE
                FROM 'Good'
                WHERE good_name = ( ? )
                """);

        sqlCodes.put(CRUD.GET_GOODS_OF_GROUP, """
                SELECT *
                FROM 'Good'
                WHERE group_id = ( SELECT group_id
                                   FROM 'GoodsGroup'
                                   WHERE group_name = ( ? )
                                 );
                """);

        columnNames.add("good_name");
        columnNames.add("good_id");
        columnNames.add("quantity");
        columnNames.add("price");

        columnNames.add("group_id");
        columnNames.add("group_name");
    }
}
