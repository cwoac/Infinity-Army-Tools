package net.codersoffortune.infinity.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Database {
    private static int currentVersion = 1;

    // helper functions

    // version table
    private String versionsTable = "versions";

    // use embedded mode
    private String protocol = "jdbc:derby:";
    private String dbName = "IAT";

    // Tables
    private String dbVersionKey = "dbVersion";
    private String getVersionQueryFmt = "SELECT version FROM versions WHERE thing='%s'";
    private String versionsSchema = "CREATE table versions(thing varchar(64) NOT NULL, version varchar(64), PRIMARY KEY(thing))";

    private Database() throws SQLException {
        // block reflection
        if (dbSingleton != null) {
            throw new RuntimeException("Don't try and create Database directly");
        }
        connection = DriverManager.getConnection(protocol + dbName + ";create=true");

        // Have we been run before?
        try {
            getDBVersion();
        } catch (SQLException e) {
            // TODO update!
            createSchemas();
        }
        System.out.println(getDBVersion());

    }

    private Connection connection;

    private static volatile Database dbSingleton;

    /**
     * build the SQL to insert or update a k/v pairing as required.
     */
    private static String getUpsert(String table, String keyfield, String valueField, String key, String value) {
        return String.format(
                "MERGE INTO %s USING SYSIBM.SYSDUMMY1 on %s.%s = '%s' WHEN MATCHED THEN UPDATE SET %s='%s' WHEN NOT MATCHED THEN INSERT (%s,%s) VALUES ('%s','%s')",
                table,
                table,
                keyfield,
                key,
                valueField,
                value,
                keyfield,
                valueField,
                key,
                value);
    }

    /**
     * getVersion - look up the current version of _something_ in the versions table.
     *
     * @param thing key to look up
     * @return the current version of the thing, if present, NULL otherwise
     * @throws SQLException on internal issue.
     */
    private String getVersion(String thing) throws SQLException {
        ResultSet resultSet = connection.createStatement().executeQuery(String.format(getVersionQueryFmt, thing));
        // TODO:: Check result count?
        while (resultSet.next())
            return resultSet.getString("version");
        return null;
    }

    private int getDBVersion() throws SQLException {
        String dbv = getVersion(dbVersionKey);
        if (dbv == null) {
            return -1;
        }
        return Integer.parseInt(dbv);
    }

    private void createSchemas() throws SQLException {
        /**
         * Create the database versions
         */
        // first the versions
        connection.createStatement().execute(versionsSchema);
        setVersion();

    }

    private void setVersion() throws SQLException {
        String vQuery = getUpsert(versionsTable, "thing", "version", dbVersionKey, String.valueOf(currentVersion));
        connection.createStatement().execute(vQuery);
    }


    public static Database getInstance() throws SQLException {
        if (dbSingleton == null) {
            // thread safe
            synchronized (Database.class) {
                if (dbSingleton == null) {
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
