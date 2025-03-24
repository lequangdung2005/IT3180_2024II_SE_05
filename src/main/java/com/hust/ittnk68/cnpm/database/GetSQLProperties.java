package com.hust.ittnk68.cnpm.database;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.hust.ittnk68.cnpm.model.*;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value=Account.class, name = "account"),
    @JsonSubTypes.Type(value=Expense.class, name = "expense"),
    @JsonSubTypes.Type(value=Family.class, name = "family"),
    @JsonSubTypes.Type(value=PaymentStatus.class, name = "payment_status"),
    @JsonSubTypes.Type(value=Person.class, name = "person"),
    @JsonSubTypes.Type(value=Vehicle.class, name = "vehicle")
})
public abstract class GetSQLProperties {
    abstract public int getId ();
    abstract public void setId(int id);
    abstract public String getSQLTableName();
    abstract public String getSQLInsertCommand();
    abstract public String getSQLUpdateCommand();

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
    public String getSQLSingleUpdate (String operation) {
        return String.format("update %s set %s where %s_id=%d",
                getSQLTableName(),
                operation,
                getSQLTableName(),
                getId());
    }
}
