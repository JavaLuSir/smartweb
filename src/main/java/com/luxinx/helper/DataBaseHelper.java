package com.luxinx.helper;

import com.luxinx.util.PropsUtil;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

public class DataBaseHelper {
    private static final Logger LOG = Logger.getLogger(DataBaseHelper.class);

    private static final String DRIVER;
    private static final String URL;
    private static final String USERNAME;
    private static final String PASSWORD;
    static{
        Properties props = PropsUtil.loadProperties("config.properties");
        DRIVER=props.getProperty("jdbc.driver");
        URL=props.getProperty("jdbc.url");
        USERNAME=props.getProperty("jdbc.username");
        PASSWORD=props.getProperty("jdbc.password");

        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e){
            LOG.error("can't load jdbc driver",e);
        }
    }

    private static final QueryRunner QUERY_RUNNER = new QueryRunner();

    public static <T> List<T> queryEntityList(Connection conn,Class<T> entityClass,String sql, Object ...params){
        List<T> entityList;
        try {
            entityList = QUERY_RUNNER.query(conn,sql,new BeanListHandler<T>(entityClass),params);
        } catch (SQLException e) {
            LOG.error("query entity list failed",e);
            throw new RuntimeException(e);
        } finally {
            closeConnection(conn);
        }
        return entityList;
    }

    public static Connection getConnection(){
        Connection connection = null;
        try {
            connection =  DriverManager.getConnection(URL,USERNAME,PASSWORD);
        } catch (SQLException e) {
            LOG.error("get connection failed!",e);
        }
        return connection;
    }

    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                LOG.error("close connection failed", e);
            }
        }
    }


}
