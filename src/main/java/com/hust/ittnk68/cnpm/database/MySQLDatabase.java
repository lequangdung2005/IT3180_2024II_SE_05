package com.hust.ittnk68.cnpm.database;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.hust.ittnk68.cnpm.config.MySQLConfig;
import com.hust.ittnk68.cnpm.exception.ConfigFileException;

public class MySQLDatabase implements Database {
    private String driver;
    private String url;
    private String username;
    private String password;

    public MySQLDatabase() throws FileNotFoundException, IOException, ConfigFileException {

		MySQLConfig mySQLConf = new MySQLConfig();
        this.driver = mySQLConf.getDriver();
        this.url = mySQLConf.getUrl();
        this.username = mySQLConf.getUsername();
        this.password = mySQLConf.getPassword();

    }

    private Connection connectToMySQL() throws SQLException {
        return MySQLDatabaseUtils.createConnection(driver, url, username, password);
    }

    // @Override
    // protected void finalize() {
    // }
    
    public void create(GetSQLProperties g) throws SQLException {
        try (
            Connection con = this.connectToMySQL();
            PreparedStatement statement = con.prepareStatement(
                g.getSQLInsertCommand(),
                Statement.RETURN_GENERATED_KEYS
            );
        )
        {
            System.out.println(statement);
            System.out.flush();

            int affectedRows = statement.executeUpdate();

            if(affectedRows == 0) {
                throw new SQLException("Create object of table " + g.getSQLTableName() + " failed, no rows affected");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if(generatedKeys.next()) {
                    g.setId(generatedKeys.getInt(1));
                }
                else {
                    throw new SQLException("Creating object of table " + g.getSQLTableName() + ", no ID obtained.");
                }
            }

            MySQLDatabaseUtils.close(con);
        }
    }

    public void deleteById(int id, GetSQLProperties g) throws SQLException {
        try (
            Connection con = this.connectToMySQL();
            PreparedStatement statement = con.prepareStatement(g.getSQLDeleteByIdCommand(id));
        )
        {
            System.out.println(statement);
            System.out.flush();

            statement.executeUpdate();
        }
    }

    public List< Map<String, Object> > findById(int id, GetSQLProperties g) throws SQLException {
        try (
            Connection con = this.connectToMySQL();
            PreparedStatement statement = con.prepareStatement(g.getSQLFindByIdCommand(id));
        )
        {
            System.out.println(statement);
            System.out.flush();

            ResultSet rs = statement.executeQuery();
            return MySQLDatabaseUtils.map(rs);
        }
    }
}
