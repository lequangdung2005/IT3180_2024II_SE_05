package com.hust.ittnk68.cnpm.database;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface Database {
    void create(GetSQLProperties g) throws SQLException;
    void deleteByCondition(String condtion, GetSQLProperties g) throws SQLException;
    List< Map<String, Object> > findByCondition(String condition, GetSQLProperties g) throws SQLException;
}
