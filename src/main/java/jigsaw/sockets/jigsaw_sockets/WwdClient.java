package jigsaw.sockets.jigsaw_sockets;

/*     WwdClientExample.java
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

import java.sql.*;
import java.time.Instant;
import java.util.Scanner;

/*
 * todo: This sample program is described in the Getting Started With Derby Manual (look at getstartderby.pdf)
 * TODO: set working directory to DERBY folder before starting this application in IDEA
 *      to have the database and derby.log located there...
 */

public class WwdClient {
    public static void main(String[] args) throws SQLException {
        // TODO: ## DEFINE VARIABLES SECTION ##
        // define the driver to use
        String driver = "org.apache.derby.jdbc.ClientDriver";
        // the database name
        String dbName = "TOP_TABLE";
//        dbName = ".." + File.separator + ".." + File.separator +  "DERBY" + File.separator + dbName;
        // define the Derby connection URL to use
        String connectionURL = "jdbc:derby://localhost:1527/" + dbName + ";create=true";
        System.out.println("connetionURL = " + connectionURL);

        Connection conn = null;
        Statement s;
        PreparedStatement psInsert;
        ResultSet myWishes;
        String printLine = "  __________________________________________________";
        String createString = "CREATE TABLE TOP_TABLE  "
                + "(ID INT PRIMARY KEY," +
                "NAME VARCHAR(255), " +
                "GAME_END INT, " +
                "STEPS INT, " +
                "GAME_TIME INT)";
        String name;

        //   JDBC code sections
        //  Beginning of Primary DB access section
        // TODO: ## BOOT DATABASE SECTION ##
        try {
            // Create (if needed) and connect to the database.
            // The driver is loaded automatically.
            conn = DriverManager.getConnection(connectionURL);
            System.out.println("Connected to database " + dbName);

            //   Create a statement to issue simple commands.
            s = conn.createStatement();
            // Call utility method to check if table exists.
            //      Create the table if needed
//            s.executeQuery("")
            s.execute("DROP TABLE TOP_TABLE");
            if (!WwdUtils.wwdChk4Table(conn)) {
                System.out.println(" . . . . creating table TOP_TABLE");
                s.execute(createString);
            }
            //  Prepare the insert statement to use
            psInsert = conn.prepareStatement("insert into TOP_TABLE values (?, ?, ?, ?, ?)");

            // TODO: ## ADD / DISPLAY RECORD SECTION ##
            // The Add-Record Loop - continues until 'exit' is entered
            Scanner in = new Scanner(System.in);
            do {
                System.out.println("Type an information!");
                // Call utility method to ask user for input
                name = in.nextLine();
                int steps = in.nextInt();
                int time = in.nextInt();
                // Check if it is time to EXIT, if not insert the data
                if (!name.equals("exit")) {
                    //Insert the text entered into the TOP_TABLE table
                    psInsert.setString(2, name);
                    psInsert.setLong(3, System.currentTimeMillis());
                    psInsert.setInt(4, steps);
                    psInsert.setInt(5, time);
                    psInsert.executeUpdate();
                }
                    //   Select all records in the TOP_TABLE table
                    myWishes = s.executeQuery("select * from TOP_TABLE order by GAME_END");

                    //  Loop through the ResultSet and print the data
                    System.out.println(printLine);
                    while (myWishes.next()) {
                        System.out.println(myWishes.getString(1));
                        System.out.println(myWishes.getTimestamp(2));
                        System.out.println(myWishes.getInt(3));
                        System.out.println(myWishes.getInt(4));
                        System.out.println(printLine);
                        //  Close the resultSet
                        myWishes.close();
                    }       //  END of IF block
                    // Check if it is time to EXIT, if so end the loop
                }
                while (!name.equals("exit")) ;  // End of do-while loop

                // Release the resources (clean up )
                psInsert.close();
                s.close();
                conn.close();
                System.out.println("Closed connection");

                // TODO: ## DATABASE SHUTDOWN SECTION ##
                /*** In embedded mode, an application should shut down Derby.
                 Shutdown throws the XJ015 exception to confirm success. ***/
                if (driver.equals("org.apache.derby.jdbc.EmbeddedDriver")) {
                    boolean gotSQLExc = false;
                    try {
                        DriverManager.getConnection("jdbc:derby:;shutdown=true");
                    } catch (SQLException se) {
                        if (se.getSQLState().equals("XJ015")) {
                            gotSQLExc = true;
                        }
                    }
                    if (!gotSQLExc) {
                        System.out.println("Database did not shut down normally");
                    } else {
                        System.out.println("Database shut down normally");
                    }
                }
                //  Beginning of the primary catch block: prints stack trace
            } catch(Throwable e){
                /*       Catch all exceptions and pass them to
                 *       the Throwable.printStackTrace method  */
                System.out.println(" . . . exception thrown:");
                e.printStackTrace(System.out);
            }
            System.out.println("Getting Started With Derby JDBC program ending.");
        }
    }
