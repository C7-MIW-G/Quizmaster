package database.mysql;

import view.Main;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

/**
 * @author Vincent Velthuizen <v.r.velthuizen@pl.hanze.nl>
 *
 * provide functionality to run SQL files
 */
public class SQLFileRunner {

    private DBAccess dbAccess;

    public SQLFileRunner(DBAccess dbAccess) {
        this.dbAccess = dbAccess;
    }

    public void executeSQL(String fileName) {
        try (Scanner sqlFile = new Scanner(new File(fileName))) {
            Statement statement = dbAccess.getConnection().createStatement();

            statement.addBatch("USE " + dbAccess.getDatabaseName() + ";");

            StringBuilder sql = new StringBuilder();
            while (sqlFile.hasNextLine()) {
                String line = sqlFile.nextLine();
                sql.append(line).append("\n");
                if (line.contains(";")) {
                    statement.addBatch(sql.toString());
                    sql = new StringBuilder();
                }
            }

            statement.executeBatch();
        } catch (SQLException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
