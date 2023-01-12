package jigsaw.sockets.jigsaw_sockets;

/*
     Derby - WwdUtils.java - utilitity methods used by WwdEmbedded.java

        Licensed to the Apache Software Foundation (ASF) under one
           or more contributor license agreements.  See the NOTICE file
           distributed with this work for additional information
           regarding copyright ownership.  The ASF licenses this file
           to you under the Apache License, Version 2.0 (the
           "License"); you may not use this file except in compliance
           with the License.  You may obtain a copy of the License at

             http://www.apache.org/licenses/LICENSE-2.0

           Unless required by applicable law or agreed to in writing,
           software distributed under the License is distributed on an
           "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
           KIND, either express or implied.  See the License for the
           specific language governing permissions and limitations
           under the License.

*/

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.*;

public class WwdUtils {

    /*****************
     **  Asks user to enter a wish list item or 'exit' to exit the loop - returns
     **       the string entered - loop should exit when the string 'exit' is returned
     ******************/
    public static String getWishItem() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String ans = "";
        try {
            while (ans.length() == 0) {
                System.out.println("Enter wish-list item (enter exit to end): ");
                ans = br.readLine();
                if (ans.length() == 0)
                    System.out.print("Nothing entered: ");
            }
        } catch (java.io.IOException e) {
            System.out.println("Could not read response from stdin");
        }
        return ans;
    }  /**  END  getWishItem  ***/

    /***      Check for  WISH_LIST table    ****/
    public static boolean wwdChk4Table(Connection conTst) throws SQLException {
        boolean result = check4Table(conTst);
        System.out.println("Table \"TOP_TABLE\" exists: " + result );

        try {
            Statement s = conTst.createStatement();
            s.execute("update TOP_TABLE set NAME = 'TEST', GAME_END = CURRENT_TIMESTAMP, " +
                    "STEPS = 5, GAME_TIME = 60 where 1=3");
        } catch (SQLException sqle) {
            String theError = (sqle).getSQLState();
            //   System.out.println("  Utils GOT:  " + theError);
            /** If table exists will get -  WARNING 02000: No row was found **/
            if (theError.equals("42X05"))   // Table does not exist
            {
                return false;
            } else if (theError.equals("42X14") || theError.equals("42821")) {
                System.out.println("WwdChk4Table: Incorrect table definition. Drop table TOP_TABLE and rerun this program");
                throw sqle;
            } else {
                System.out.println("WwdChk4Table: Unhandled SQLException");
                throw sqle;
            }
        }
        //  System.out.println("Just got the warning - table exists OK ");
        return true;
    }

    // just another implementation for wwdChk4Table()- method:
    public static boolean  check4Table(Connection connection) throws SQLException {
        System.out.println("printAllTables --------------------------------------------------------");
//        printAllTables(connection, "WISH_LIST");
//        printAllTables(connection, null);
        printAllTables(connection);
        System.out.println("-----------------------------------------------------------------------");

//        return tableExists0(connection, "WISH_LIST");
        return tableExists1(connection, "TOP_TABLE");
    }

    /*** END wwdInitTable  **/


    public static void main(String[] args) {
        // This method allows stand-alone testing of the getWishItem method
        String answer;
        do {
            answer = getWishItem();
            if (!answer.equals("exit")) {
                System.out.println("You said: " + answer);
            }
        } while (!answer.equals("exit"));
    }

    static void printAllTables(Connection connection) throws SQLException {
        DatabaseMetaData databaseMetaData = connection.getMetaData();
        ResultSet resultSet = databaseMetaData.getTables(null, null, null, new String[] {"TABLE"});

        while (resultSet.next()) {
            String name = resultSet.getString("TABLE_NAME");
            String schema = resultSet.getString("TABLE_SCHEM");
            System.out.println(name + " on schema " + schema);
        }
    }

    static boolean tableExists0(Connection connection, String tableName) throws SQLException {
        DatabaseMetaData databaseMetaData = connection.getMetaData();
        ResultSet resultSet = databaseMetaData.getTables(null, null,
                "TOP_TABLE", new String[]{"TABLE"});
        return resultSet.next() && resultSet.getString(3).equals(tableName);
    }
    static boolean tableExists1(Connection connection, String tableName) throws SQLException {
        DatabaseMetaData meta = connection.getMetaData();
        ResultSet resultSet = meta.getTables(null, null, tableName, new String[] {"TABLE"});

        return resultSet.next();
    }

//TODO: Derby does not support some extra operations that depend on SQL specification details...

//    static boolean tableExists2(Connection connection, String tableName) throws SQLException {
//        PreparedStatement preparedStatement = connection.prepareStatement("SELECT count(*) " +
//                "FROM information_schema.tables " + // TODO: Derby does not support information_schema ...
//                "WHERE table_name = ?" +
//                "  LIMIT 1;"); // TODO: Derby does NOT support LIMIT...
//        preparedStatement.setString(1, tableName);
//
//        ResultSet resultSet = preparedStatement.executeQuery();
//        resultSet.next();
//        return resultSet.getInt(1) != 0;
//    }
}