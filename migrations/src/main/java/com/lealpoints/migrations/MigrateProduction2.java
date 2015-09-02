package com.lealpoints.migrations;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import com.lealpoints.migrations.db.DatabaseManager;
import com.lealpoints.migrations.db.ProductionDatabaseManager;
import com.lealpoints.migrations.db.UATDatabaseManager;
import com.lealpoints.migrations.util.DBUtil;
import com.lealpoints.migrations.util.DateUtil;
import org.apache.commons.collections15.CollectionUtils;
import org.apache.commons.collections15.Predicate;

public class MigrateProduction2 {
    public static void main(String[] args) throws Exception {
        System.out.println("Running migrations for production...");
        new MigrateProduction2().run(new ProductionDatabaseManager());
        System.out.println("Running migrations for UAT...");
        new MigrateProduction2().run(new UATDatabaseManager());
        System.out.println("Process finished successfully.");
    }

    private void run(DatabaseManager abstractDatabaseManager) throws Exception {
        File[] files = loadMigrationScripts(abstractDatabaseManager);
        Connection connection = abstractDatabaseManager.getConnection();
        String lastFileExecuted = "";
        for (File file : files) {
            DBUtil.executeScript(file, connection);
            lastFileExecuted = file.getName();
        }
        if (!lastFileExecuted.equals("")) {
            final String lastMigrationString = lastFileExecuted.substring(0, lastFileExecuted.indexOf("_"));
            DBUtil.executeSql("UPDATE migration SET last_run_migration = " + lastMigrationString, connection);
        }
    }

    private File[] loadMigrationScripts(DatabaseManager databaseManager) throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        final URL resource = classLoader.getResource("scripts/migrations/");
        if (resource == null) {
            return new File[0];
        }
        File dir = new File(resource.getFile());
        File[] filesArray = dir.listFiles();
        List<File> filesFromMigration = new ArrayList<>();
        if (filesArray != null) {
            filesFromMigration = filterMigrationsByDate(Arrays.asList(filesArray), databaseManager);
        }

        List<File> totalListFiles = new ArrayList<>();
        totalListFiles.addAll(filesFromMigration);

        final File[] totalArrayFiles = new File[totalListFiles.size()];
        totalListFiles.toArray(totalArrayFiles);
        return totalArrayFiles;
    }

    private List<File> filterMigrationsByDate(List<File> filesFromSetup, DatabaseManager abstractDatabaseManager) throws Exception {
        final Connection connection = abstractDatabaseManager.getConnection();
        Statement st = null;
        Collection<File> filteredFiles = filesFromSetup;
        try {
            String lastRunMigrationString = "19700101000000";
            st = connection.createStatement();
            final ResultSet resultSet = st.executeQuery("SELECT last_run_migration FROM migration;");
            if (resultSet.next()) {
                lastRunMigrationString = resultSet.getString("last_run_migration");
            }
            final Date lastRunMigrationDate = DateUtil.parseDate(lastRunMigrationString, "yyyyMMddHHmmss");
            filteredFiles = CollectionUtils.select(filesFromSetup, new Predicate<File>() {
                @Override
                public boolean evaluate(File file) {
                    final String fileName = file.getName();
                    String dateString = fileName.substring(0, fileName.indexOf("_"));
                    Date date = DateUtil.parseDate(dateString, "yyyyMMddHHmmss");
                    return date.after(lastRunMigrationDate);
                }
            });
        } finally {
            if (st != null) {
                st.close();
            }
        }
        for (File filteredFile : filteredFiles) {
            System.out.println(filteredFile.getName());
        }
        return (List<File>) filteredFiles;
    }
}