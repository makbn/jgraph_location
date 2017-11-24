package io.github.makbn;

import com.jsoniter.any.Any;
import com.jsoniter.output.JsonStream;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class GraphFactory {


    public static LocationGraph createFromSQL(String host,String dbName,String username,String password,String cityId,String postmanId,String date,String orderTableName) throws SQLException, ClassNotFoundException {

        SqlHelper.init(SqlHelper.DBRM.MS_SQL_SERVER,host,dbName,username,password);


        String query="SELECT LE_DATEDELIVERED, LE_TIMEDELIVERED, LE_LAT, LE_LON, LE_LETTER_ID, PCity FROM TBL_Letter WHERE LE_POSTMAN_ID={POSTMAN_ID} AND LE_DATEDELIVERED ='{DATA_DELIVERED}' AND CityID={CITY_ID} ORDER BY LE_DATEDELIVERED , LE_TIMEDELIVERED";

        query=query.replace("{POSTMAN_ID}",postmanId).replace("{CITY_ID}",cityId).replace("{ORDER_TABLE}",orderTableName).replace("{DATA_DELIVERED}",date);

        if(SqlHelper.isConnected()){
            Statement statement=SqlHelper.getStatement();
            ResultSet resultSet=statement.executeQuery(query);
            return createModel(resultSet);
        }
        return null;
    }




    private static LocationGraph createModel(ResultSet resultSet) throws SQLException {

        ArrayList<LocationVertex> vertices=new ArrayList<LocationVertex>();
        LocationGraph<LocationVertex,PathEdge<LocationVertex>> lg=new LocationGraph(PathEdge.class);
        int id=1;
        while (resultSet.next()){
            double lat=resultSet.getDouble("LE_LAT");
            double lon=resultSet.getDouble("LE_LON");
            String pCity=resultSet.getString("PCity");
            String time=resultSet.getString("LE_TIMEDELIVERED");
            String date=resultSet.getString("LE_DATEDELIVERED");
            String letter_id=resultSet.getString("LE_LETTER_ID");
            LocationVertex lv=new LocationVertex(id++, lat,lon, date, time,1,pCity,false);
            vertices.add(lv);
            lg.addVertex(lv);
            //System.out.println("lat: "+lat+" \t\t\t\t lon: "+lon+ " \t\t\t\t date: "+date+" \t\t time: "+time);
        }

        ArrayList<PathEdge> pathEdges=new ArrayList<PathEdge>();

        for (int i=0;i<vertices.size()-1;i++){
            LocationVertex src=vertices.get(i);
            LocationVertex dst=vertices.get(i+1);
            PathEdge pe=new PathEdge(src,dst);
            pathEdges.add(pe);
            lg.createEdge(src,dst);
        }

        lg.print();

        return lg;
    }


}
