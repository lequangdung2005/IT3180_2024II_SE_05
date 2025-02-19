package com.hust.ittnk68.cnpm.database;

import java.sql.Connection;

public class MySQLDatabase {
    private Connection con;

    public MySQLDatabase(String driver, String url, String username, String password) {
        try {
            con = MySQLDatabaseUtils.createConnection(driver, url, username, password);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void finalize() {
        MySQLDatabaseUtils.close(con);
    }
}
