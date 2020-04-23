package com.practice.orm.db.utilDao.entiry;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Demo {
    public static void main(String[] args) {
        ResultSet rsObj = null;
        Connection connObj = null;
        PreparedStatement pstmtObj = null;
        PropertyBundle pr = new PropertyBundle(Path.MYSQL_PATH);

        try {
            DBUtil dbUtil = DBUtil.getInstance(pr);
            dbUtil.printMapPoolOfConnection();

            // Performing Database Operation!
            System.out.println("\n=====Making A New Connection Object For Db Transaction=====\n");
            connObj = dbUtil.getConnectionFromPool();
            System.out.println(connObj + " ************");
            pstmtObj = connObj.prepareStatement("SELECT * FROM technical_editors");
            rsObj = pstmtObj.executeQuery();
            while (rsObj.next()) {
                System.out.println("Username: " + rsObj.getString("tech_username"));
            }
            System.out.println("\n=====Releasing Connection Object To Pool=====\n");
            dbUtil.returnConnectionToPool(connObj);
            System.out.println("*****disconnect");
            dbUtil.disconnectAllPoolConnections();
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            try {
                // Closing ResultSet Object
                if (rsObj != null) {
                    rsObj.close();
                }
                // Closing PreparedStatement Object
                if (pstmtObj != null) {
                    pstmtObj.close();
                }
                // Closing Connection Object
                if (connObj != null) {
                    connObj.close();
                }
            } catch (Exception sqlException) {
                sqlException.printStackTrace();
            }
        }
    }

}
