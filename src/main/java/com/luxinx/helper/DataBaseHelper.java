package com.luxinx.helper;

import com.luxinx.util.CollectionUtil;
import com.luxinx.util.PropsUtil;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * DatabaseHelper is used to operate Database with insert update delete and Entity
 * Operate insertEntity updateEntity deleteEntity
 */
public class DataBaseHelper {


    private static final Logger LOG = Logger.getLogger(DataBaseHelper.class);

    private static final ThreadLocal<Connection> CONNECTION_HOLDER = new ThreadLocal<>();

    private static final String DRIVER;
    private static final String URL;
    private static final String USERNAME;
    private static final String PASSWORD;

    static {
        Properties props = PropsUtil.loadProperties("config.properties");
        DRIVER = props.getProperty("jdbc.driver");
        URL = props.getProperty("jdbc.url");
        USERNAME = props.getProperty("jdbc.username");
        PASSWORD = props.getProperty("jdbc.password");

        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            LOG.error("can't load jdbc driver", e);
        }
    }

    private static final QueryRunner QUERY_RUNNER = new QueryRunner();

    /**
     * use sql to query EntityList
     * @param entityClass Class<T> entityClass
     * @param sql use prepared sql grammar with this param
     * @param params sql's param is use to query sql
     * @param <T> entityClass type
     * @return return entityClass type
     */
    public static <T> List<T> queryEntityList(Class<T> entityClass, String sql, Object... params) {
        Connection conn = getConnection();
        List<T> entityList;
        try {
            entityList = QUERY_RUNNER.query(conn, sql, new BeanListHandler<T>(entityClass), params);
        } catch (SQLException e) {
            LOG.error("query entity list failed", e);
            throw new RuntimeException(e);
        } finally {
            closeConnection();
        }
        return entityList;
    }

    /**
     * this method use to get Connection Object from database
     * @return
     */
    public static Connection getConnection() {
        Connection connection = CONNECTION_HOLDER.get();
        try {
            if (connection == null) {
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            }
        } catch (SQLException e) {
            LOG.error("get connection failed!", e);
        } finally {
            CONNECTION_HOLDER.set(connection);
        }
        return connection;
    }

    /**
     * query sql to query single Entity
     * @param entityClass EntityClass type
     * @param sql use prepared grammar with this param
     * @param params the param of the sql prepared
     * @param <T>
     * @return EntityClass type
     */
    public static <T> T queryEntity(Class<T> entityClass, String sql, Object... params) {
        T entity = null;
        try {
            Connection connection = getConnection();
            entity = QUERY_RUNNER.query(connection, sql, new BeanHandler<T>(entityClass), params);
        } catch (SQLException e) {
            LOG.error("query bean failed !", e);
            throw new RuntimeException(e);
        } finally {
            closeConnection();
        }
        return entity;
    }

    /**
     * insert into a row data into database
     * @param entityClass entityClass.class
     * @param filedMap the value of the entity
     * @param <T>
     * @return true success;false insert failed
     */
    public static <T> boolean insertEntity(Class<T> entityClass, Map<String, Object> filedMap) {
        if (CollectionUtil.isEmpty(filedMap)) {
            LOG.error("can 't insert Entity because Map is empty!");
            return false;
        }

        String sql = "insert into " + getTableName(entityClass);
        StringBuilder colums = new StringBuilder("(");
        StringBuilder values = new StringBuilder("(");
        filedMap.forEach((k, v) -> {
            colums.append(k).append(", ");
            values.append("?").append(", ");
        });
        colums.replace(colums.lastIndexOf(", "), colums.length(), ")");
        values.replace(values.lastIndexOf(", "), values.length(), ")");
        sql += colums + " values " + values;
        Object[] params = filedMap.values().toArray();
        return executeUpdate(sql, params) == 1;
    }

    /**
     * the method is used to update Entity
     * @param entityClass user entityClass
     * @param id the id of entityClass
     * @param fieldMap the value will to update
     * @param <T> entityClass
     * @return true update success;false update failed
     */
    public static <T> boolean updateEntity(Class<T> entityClass, long id, Map<String, Object> fieldMap) {
        if (CollectionUtil.isEmpty(fieldMap)) {
            LOG.error("can 't insert Entity because Map is empty!");
            return false;
        }
        String sql = "update " + getTableName(entityClass) + " set ";
        StringBuilder colval = new StringBuilder();
        fieldMap.forEach((k, v) -> {
            colval.append(k).append("=?,");
        });
        sql += colval.substring(0, colval.lastIndexOf(",")) + " where id=?";
        List<Object> paramsList = new ArrayList<>();
        paramsList.addAll(fieldMap.values());
        paramsList.add(id);
        return executeUpdate(sql) == 1;
    }

    /**
     * delete an entity use id
     * @param entityClass entityClass
     * @param id union symble with
     * @param <T>
     * @return true delete success;false delete faild
     */
    public static <T> boolean deleteEntity(Class<T> entityClass,long id){
        String sql = "delete from "+getTableName(entityClass)+" where id=?";
        return executeUpdate(sql,id)==1;
    }

    /**
     * get Table from entityClass
     * @param entityClass the entity of dataTable
     * @return tablename
     */
    private static String getTableName(Class<?> entityClass) {
        return entityClass.getSimpleName();
    }

    /**
     * use this method to executeQuery Method get a List<Map<String,Object>></String,Object>
     * @param sql the sql is used to select entity
     * @param params the params of sql
     * @return the data in List<Map<String, Object>> type
     */
    public static List<Map<String, Object>> executeQuery(String sql, Object... params) {
        Connection connection = getConnection();
        List<Map<String, Object>> result = null;
        try {
            result = QUERY_RUNNER.query(connection, sql, new MapListHandler(), params);

        } catch (SQLException e) {
            LOG.error("execute Query Failed !", e);
            throw new RuntimeException(e);
        } finally {
            closeConnection();
        }
        return result;
    }

    /**
     * use this method to update database data
     * @param sql use prepared grammar
     * @param params the param of sql
     * @return affect rows num
     */
    public static int executeUpdate(String sql, Object... params) {
        Connection connection = getConnection();
        int result = 0;
        try {
            result = QUERY_RUNNER.update(connection, sql, params);
        } catch (SQLException e) {
            LOG.error("update Failed !", e);
            throw new RuntimeException(e);
        } finally {
            closeConnection();
        }
        return result;
    }

    /**
     * release Connection Object
     */
    public static void closeConnection() {
        Connection connection = CONNECTION_HOLDER.get();
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                LOG.error("close connection failed", e);
            } finally {
                CONNECTION_HOLDER.remove();
            }
        }
    }


}
