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

import com.hust.ittnk68.cnpm.config.ConfigFileLoader;
import com.hust.ittnk68.cnpm.config.MySQLDefaultConfig;
import com.hust.ittnk68.cnpm.exception.ConfigFileException;

import org.springframework.stereotype.Repository;

import jakarta.annotation.*;

@Repository
public class MySQLDatabase {
    private Connection con;
    private String driver;
    private String url;
    private String username;
    private String password;

    @PostConstruct
    public void start() throws FileNotFoundException, IOException, ConfigFileException {

        ConfigFileLoader loader = new ConfigFileLoader("/MySQL.conf", new MySQLDefaultConfig());

        driver = loader.getProperty("driver"); 
        url = loader.getProperty("url");
        username = loader.getProperty("username");
        password = loader.getProperty("password");

        System.out.println ("debug " + driver);
        System.out.println ("debug " + url);
        System.out.println ("debug " + username);
        System.out.println ("debug " + password);

        try {
            con = MySQLDatabaseUtils.createConnection(driver, url, username, password);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @PreDestroy
    public void close() {
        MySQLDatabaseUtils.close(con);
    }
    
    public void create(GetSQLProperties g) throws SQLException {
        try (
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
        }
    }

    public int deleteByCondition(String condition, GetSQLProperties g) throws SQLException {
        try (
            PreparedStatement statement = con.prepareStatement(g.getSQLDeleteByConditionCommand(condition));
        )
        {
            System.out.println(statement);
            System.out.flush();

            int affectedRows = statement.executeUpdate();
            return affectedRows;
        }
    }

    public List< Map<String, Object> > findByCondition(String condition, GetSQLProperties g) throws SQLException {
        try (
            PreparedStatement statement = con.prepareStatement(g.getSQLFindByConditionCommand(condition));
        )
        {
            System.out.println(statement);
            System.out.flush();

            ResultSet rs = statement.executeQuery();
            return MySQLDatabaseUtils.map(rs);
        }
    }

    public int singleUpdate (GetSQLProperties g) throws SQLException {
        try (
            PreparedStatement statement = con.prepareStatement(g.getSQLUpdateCommand());
        )
        {
            System.out.println (statement);
            System.out.flush ();

            int affectedRows = statement.executeUpdate ();
            return affectedRows;
        }
    }

    public List< Map<String, Object> > findById (GetSQLProperties g) throws SQLException
    {
	    return findByCondition (String.format("%s_id='%d'",
                                            g.getSQLTableName(),
                                            g.getId()), g);
    }

}
