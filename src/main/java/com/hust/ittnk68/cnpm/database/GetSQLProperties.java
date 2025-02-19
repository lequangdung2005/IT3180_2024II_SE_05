package com.hust.ittnk68.cnpm.database;

// import com.hust.ittnk68.cnpm.database.GetSQLProperties;
import java.sql.Statement;
// import java.sql.Connection;
// import java.sql.SQLException;

public interface GetSQLProperties {
    String getSQLTableName();
    String getSQLInsertColumns();
    String getSQLInsertValues();
}
