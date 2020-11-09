package net.codersoffortune.infinity.db;

import net.codersoffortune.infinity.metadata.Faction;
import net.codersoffortune.infinity.metadata.Metadata;
import net.codersoffortune.infinity.metadata.SectoralList;
import net.codersoffortune.infinity.metadata.unit.Unit;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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


    // in memory structures
    Metadata metadata;
    Map<Integer, SectoralList> factions;

    private Database() throws SQLException, IOException {
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
        metadata = Metadata.loadMetadata();
        factions = new HashMap<>();
        for (Faction f : metadata.getFactions()) {
            int id = f.getID();
            if (id == 901) continue; // NA2 doesn't have a vanilla option
            factions.put(id, SectoralList.loadFaction(String.valueOf(id)));
        }
    }

    public static Database getInstance() throws SQLException, IOException {
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

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public Map<Integer, SectoralList> getFactions() {
        return factions;
    }

    public void setFactions(Map<Integer, SectoralList> factions) {
        this.factions = factions;
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
        // first the versions
        connection.createStatement().execute(versionsSchema);
        setVersion();

    }

    private void setVersion() throws SQLException {
        String vQuery = getUpsert(versionsTable, "thing", "version", dbVersionKey, String.valueOf(currentVersion));
        connection.createStatement().execute(vQuery);
    }

    public Optional<Unit> getUnitName(int unit_id, int faction_id) {
        SectoralList f = factions.get(faction_id);
        if (f == null) {
            // TODO:: Throw exception?
            return Optional.empty();
        }

        return f.getUnit(unit_id);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        if (connection != null) {
            connection.close();
            connection = null;
        }
    }
}
