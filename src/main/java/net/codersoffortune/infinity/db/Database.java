package net.codersoffortune.infinity.db;

import java.sql.*;

public class Database {
    private static int currentVersion = 1;

    // use embedded mode
    private String framework = "embedded";
    private String protocol = "jdbc:derby:";
    private String dbName = "IAT";

    // Tables
    private String versionsTable = "versions";
    private String versionsSchema = "CREATE table versions(thing varchar(64) NOT NULL, version varchar(64), PRIMARY KEY(thing))";
    private String getVersionQuery = "SELECT version FROM versions WHERE thing='dbVersion'";
    private String setVersionQuery = "MERGE INTO versions USING SYSIBM.SYSDUMMY1 on versions.thing = 'dbVersion' WHEN MATCHED THEN UPDATE SET version='"+currentVersion+"' WHEN NOT MATCHED THEN INSERT (thing,version) VALUES ('dbVersion','"+currentVersion+"')";
    private Connection connection;

    private static volatile Database dbSingleton;

    private int getVersion() throws SQLException {
        ResultSet resultSet = connection.createStatement().executeQuery(getVersionQuery);
        // TODO:: Check result count?
        return resultSet.getInt("version");
    }

    private void setVersion() throws SQLException{
        connection.createStatement().execute(setVersionQuery);
    }

    private void createSchemas() throws SQLException {
        /**
         * Create the database versions
         */
        // first the versions
        connection.createStatement().execute(versionsSchema);
        setVersion();

    }

    private Database() throws SQLException {
        // block reflection
        if (dbSingleton != null) {
            throw new RuntimeException("Don't try and create Database directly");
        }
        connection = DriverManager.getConnection(protocol + dbName + ";create=true");

        // Have we been run before?
        try {
            getVersion();
        } catch (SQLException e) {
            // TODO update!
            createSchemas();
        }
        System.out.println(getVersion());
        //Statement s = connection.createStatement();

        //ResultSet rs = s.executeQuery("SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA");
        //System.out.println(rs);
        //connection.close();

    }


    public static Database getInstance() throws SQLException {
        if ( dbSingleton == null ) {
            // thread safe
            synchronized (Database.class) {
                if ( dbSingleton == null ) {
                    dbSingleton = new Database();
                }
            }
        }
        return dbSingleton;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        if (connection!=null) {
            connection.close();
            connection = null;
        }
    }
}
