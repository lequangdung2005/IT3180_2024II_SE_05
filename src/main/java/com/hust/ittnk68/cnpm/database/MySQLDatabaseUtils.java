package com.hust.ittnk68.cnpm.database;

import com.hust.ittnk68.cnpm.database.MySQLDatabaseUtils;
import com.hust.ittnk68.cnpm.model.Account;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import java.sql.*;

public class MySQLDatabaseUtils {
    public static Connection createConnection(String driver, String url, String username, String password) throws SQLException {

        System.out.println("JDBC driver: " + driver);
        System.out.flush();

        try {
            Class.forName(driver);
        }
        catch(ClassNotFoundException e) {
            e.printStackTrace();
        }

        if((username == null) || (password == null) || (username.trim().length() == 0) || (password.trim().length() == 0)) {
            return DriverManager.getConnection(url);
        }
        else {
            return DriverManager.getConnection(url, username, password);
        }

    }

    public static void close(Connection connection) {
        try {
            if(connection != null) {
                connection.close();
            }
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public static void close(Statement st) {
        try {
            if(st != null) {
                st.close();
            }
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public static void close(ResultSet rs) {
        try {
            if(rs != null) {
                rs.close();
            }
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public static void rollback(Connection connection) {
        try {
            if(connection != null) {
                connection.rollback();
            }
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public static List< Map<String, Object> > map(ResultSet rs) throws SQLException {
        List< Map<String, Object> > results = new ArrayList< Map<String, Object> > ();
        try {
            if(rs != null) {
                ResultSetMetaData meta = rs.getMetaData();
                int numColumns = meta.getColumnCount();
                while(rs.next()) {
                    Map<String, Object> row = new HashMap<String, Object> ();
                    for(int i = 1; i <= numColumns; ++i) {
                        String name = meta.getColumnName(i);
                        Object value = rs.getObject(i);
                        row.put(name, value);
                    }
                    results.add(row);
                }
            }
        }
        finally {
            close(rs);
        }
        return results;
    }

    public static List< Map<String, Object> > findAccountByUsername(MySQLDatabase sql, Account acc) throws SQLException
    {
        return sql.findByCondition(
                    String.format("username='%s'", acc.getUsername()),
                    acc
                );
    }
}
