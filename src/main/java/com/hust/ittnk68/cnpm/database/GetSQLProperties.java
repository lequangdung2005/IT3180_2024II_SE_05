package com.hust.ittnk68.cnpm.database;

// import com.hust.ittnk68.cnpm.database.GetSQLProperties;
// import java.sql.Statement;
// import java.sql.Connection;
// import java.sql.SQLException;

public abstract class GetSQLProperties {
    abstract public void setId(int id);
    abstract public String getSQLTableName();
    abstract public String getSQLInsertCommand();

    public String getSQLFindByConditionCommand(String condition) {
        return String.format("select * from %s where (%s);",
                getSQLTableName(),
                condition);
    }
    public String getSQLDeleteByConditionCommand(String condition) {
        return String.format("delete from %s where (%s);",
                getSQLTableName(),
                condition);
    }
}
