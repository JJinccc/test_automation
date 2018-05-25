package com.ffan.qa.utils;

import java.sql.*;
import java.util.*;

public class DBUtil {
    private String url, user, password;
    private Connection connection;

    public DBUtil(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    private void getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection(url, user, password);
    }

    public List<Map<String, Object>> queryList(String sql) throws SQLException, ClassNotFoundException {
        getConnection();
        Statement ps = connection.createStatement();
        ResultSet rs = ps.executeQuery(sql);
        List<Map<String, Object>> result = resultSetToList(rs);
        rs.close();
        ps.close();
        closeConnection();

        return result;
    }

    public <T> List<T> queryExList(String sql, Class<T> clazz) throws Exception {
        List<Map<String, Object>> result = queryList(sql);
        List<T> ret = new ArrayList<>();
        for (Map<String, Object> o: result) {
            ret.add((T)MapUtil.mapToBean(o, clazz));
        }
        return ret;
    }

    /**
     * 用于增删改
     *
     * @param sql sql语句
     * @return 影响行数
     * @throws SQLException
     */
    public int update(String sql) throws SQLException, ClassNotFoundException {

        try {
            getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);

            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException(e);
        } finally {
            closeConnection();
        }
    }

    /**
     * 用于增删改（带参数）
     *
     * @param sql       sql语句
     * @param paramters sql语句
     * @return 影响行数
     * @throws SQLException
     */
    public int update(String sql, Object... paramters)
            throws SQLException, ClassNotFoundException {
        try {
            getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);

            for (int i = 0; i < paramters.length; i++) {
                ps.setObject(i + 1, paramters[i]);
            }
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException(e);
        } finally {
            closeConnection();
        }
    }

    /**
     * 插入值后返回主键值
     *
     * @param sql 插入sql语句
     * @return 返回结果
     * @throws Exception
     */
    public Object insertWithReturnPrimeKey(String sql)
            throws SQLException, ClassNotFoundException {
        ResultSet rs = null;
        Object result = null;
        try {
            getConnection();
            PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.execute();
            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                result = rs.getObject(1);
            }
            return result;
        } catch (SQLException e) {
            throw new SQLException(e);
        } finally {
            closeConnection();
        }
    }

    /**
     * 插入值后返回主键值
     *
     * @param sql       插入sql语句
     * @param paramters 参数列表
     * @return 返回结果
     * @throws SQLException
     */
    public Object insertWithReturnPrimeKey(String sql, Object... paramters) throws SQLException, ClassNotFoundException {
        ResultSet rs = null;
        Object result = null;
        try {
            getConnection();
            PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            for (int i = 0; i < paramters.length; i++) {
                ps.setObject(i + 1, paramters[i]);
            }
            ps.execute();
            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                result = rs.getObject(1);
            }
            return result;
        } catch (SQLException e) {
            throw new SQLException(e);
        } finally {
            closeConnection();
        }

    }

    /**
     * 批量更新数据
     *
     * @param sqlList 一组sql
     * @return
     */
    public int[] batchUpdate(List<String> sqlList) throws SQLException, ClassNotFoundException {

        int[] result = new int[]{};
        Statement statenent = null;
        try {
            getConnection();
            connection.setAutoCommit(false);
            statenent = connection.createStatement();
            for (String sql : sqlList) {
                statenent.addBatch(sql);
            }
            result = statenent.executeBatch();
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException e1) {
                // TODO Auto-generated catch block
                throw new ExceptionInInitializerError(e1);
            }
            throw new ExceptionInInitializerError(e);
        } finally {
            closeConnection();
        }
        return result;
    }

    private void closeConnection() throws SQLException {
        connection.close();
    }

    private List<Map<String, Object>> resultSetToList(ResultSet rs)
            throws java.sql.SQLException {
        if (rs == null)
            return Collections.EMPTY_LIST;

        ResultSetMetaData md = rs.getMetaData(); // 得到结果集(rs)的结构信息，比如字段数、字段名等
        int columnCount = md.getColumnCount(); // 返回此 ResultSet 对象中的列数
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> rowData = new HashMap<String, Object>();
        while (rs.next()) {
            rowData = new HashMap<String, Object>(columnCount);
            for (int i = 1; i <= columnCount; i++) {
                rowData.put(md.getColumnLabel(i), rs.getObject(i));
            }
            list.add(rowData);
        }
        return list;
    }
}
