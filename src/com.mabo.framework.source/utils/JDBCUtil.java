package com.mabo.framework.source.utils;

import java.sql.*;
import java.util.*;

/**
 * 工具类：负责数据库操作
 * Created by Ethel_oo on 2018/3/19.
 */
public class JDBCUtil {
    private static String driverClass=PropertyUtil.get("config/database.properties","driverClass");
    private static String url=PropertyUtil.get("config/database.properties","url");
    private static String username=PropertyUtil.get("config/database.properties","username");
    private static String password=PropertyUtil.get("config/database.properties","password");
    private static Connection connection;
    private static PreparedStatement preparedStatement;
    private static ResultSet resultSet;


    /**
     * 封装jdbc连接
     *
     */
    public static Connection getConnection() {
        try {
            Class.forName(driverClass);
            connection = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * 封装jdbc增加，删除，修改
     *
     */
    public static int update(String sql, Object... params) {
        boolean flag = false;
        int result = -1;
        connection = getConnection();
        try {
            preparedStatement = connection.prepareStatement(sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        int index = 1;
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                try {
                    preparedStatement.setObject(index++, params[i]);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        try {
            result = preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        closeAll();
        return result;
    }

    /**
     * 封装jdbc查询方法
     *
     */
    public static List<Map<String, Object>> queryForList(String sql, Object... params) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        int index = 1;
        connection = getConnection();
        try {
            preparedStatement = connection.prepareStatement(sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                try {
                    preparedStatement.setObject(index++, params[i]);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        try {
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        ResultSetMetaData setMetaData = null;
        try {
            setMetaData = resultSet.getMetaData();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        // 获取列的数量
        int col_len = 0;
        try {
            col_len = setMetaData.getColumnCount();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        while (true) {
            try {
                if (!resultSet.next()) break;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            Map<String, Object> map = new HashMap<String, Object>();
            for (int i = 0; i < col_len; i++) {
                String col_name = null;
                try {
                    col_name = setMetaData.getColumnName(i + 1);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                Object col_value = null;
                try {
                    col_value = resultSet.getObject(col_name);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                if (col_value == null) {
                    col_value = "";
                }
                map.put(col_name, col_value);
            }
            list.add(map);
        }
        closeAll();
        return list;
    }

    /**
     * close所有的jdbc操作
     */
    public static void closeAll() {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
