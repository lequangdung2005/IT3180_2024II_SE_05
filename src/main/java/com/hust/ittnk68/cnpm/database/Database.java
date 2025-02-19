package com.hust.ittnk68.cnpm.database;

import java.sql.SQLException;

public interface Database {
    int create(GetSQLProperties g) throws SQLException;
    // int search(GetSQLProperties g);
    // void delete(GetSQLProperties g);
}
