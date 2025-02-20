package com.hust.ittnk68.cnpm.database;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface Database {
    void create(GetSQLProperties g) throws SQLException;
    void deleteById(int id, GetSQLProperties g) throws SQLException;
    List< Map<String, Object> > findById(int id, GetSQLProperties g) throws SQLException;
    // void delete(GetSQLProperties g);
    // void update(GetSQLProperties g);
}
