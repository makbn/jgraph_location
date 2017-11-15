package io.github.makbn;

import java.sql.SQLException;

public class Main {


    public static void main(String[] args){
        try {

            //SqlHelper.init(SqlHelper.DBRM.MS_SQL_SERVER,"0.0.0.0\\SQLEXPRESS:1401","PDA","sa","<YourStrong!Passw0rd>");
            LocationGraph<LocationVertex,PathEdge<LocationVertex>> lg=GraphFactory.createFromSQL("0.0.0.0\\SQLEXPRESS:1401","PDA","sa","<YourStrong!Passw0rd>","1015","1","1396/02/02","LE_DATEDELIVERED, LE_TIMEDELIVERED");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
