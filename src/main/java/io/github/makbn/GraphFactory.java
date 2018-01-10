package io.github.makbn;

import com.jsoniter.annotation.JsonObject;
import com.jsoniter.any.Any;
import com.jsoniter.output.JsonStream;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class GraphFactory {

    private static HashMap<String,double[]> geos;


    public static LocationGraph createEmpty(){
        return new LocationGraph(PathEdge.class);
    }
    public static LocationGraph createFromSQL(String host,String dbName,String username,String password,String cityId,String postmanId,String date,String orderTableName,String runningPath) throws SQLException, ClassNotFoundException {

        SqlHelper.init(SqlHelper.DBRM.MS_SQL_SERVER,host,dbName,username,password);


        // String query="SELECT LE_DATEDELIVERED, LE_TIMEDELIVERED, LE_LAT, LE_LON, LE_LETTER_ID, PCity FROM TBL_Letter WHERE LE_POSTMAN_ID={POSTMAN_ID} AND LE_DATEDELIVERED ='{DATA_DELIVERED}' AND CityID={CITY_ID} ORDER BY LE_DATEDELIVERED , LE_TIMEDELIVERED";
        String query="select * from Full_Exchange order by ParcelCode , EventDate ";

        //query=query.replace("{POSTMAN_ID}",postmanId).replace("{CITY_ID}",cityId).replace("{ORDER_TABLE}",orderTableName).replace("{DATA_DELIVERED}",date);

        if(SqlHelper.isConnected()){
            System.out.println("connected to db...");
            Statement statement=SqlHelper.getStatement();
            ResultSet resultSet=statement.executeQuery(query);
            System.out.println("query executed...");
            return createModel(resultSet,runningPath);
        }
        return null;
    }




    private static LocationGraph createModel(ResultSet resultSet,String runningPath) throws SQLException {
        String csv="";
        ArrayList<LocationVertex> vertices=new ArrayList<LocationVertex>();
        ArrayList<PathEdge> pathEdges=new ArrayList<PathEdge>();
        LocationGraph<LocationVertex,PathEdge<LocationVertex>> lg=new LocationGraph(PathEdge.class);
        LocationVertex lastVertex=null;
        String lastParcelCode=null;
        int lastPostNodeId=-1;
        int lastCityId=-1;
        int id=1;
        while (resultSet.next()){
            String parcelCode=resultSet.getString("ParcelCode");
            int postNodeId=resultSet.getInt("PostnodeID");
            String pCity=resultSet.getString("PCity");
            int cityId=resultSet.getInt("CityID");
            int event=resultSet.getInt("EventID");
            double[] geo=getGeo(pCity,runningPath);
            if(geo==null)
                continue;
            double lat=geo[0];
            double lon=geo[1];
            String date=resultSet.getString("EventDate");

            LocationVertex currentVertex=new LocationVertex(id++,lat,lon,date,null,-1,pCity,false);
            currentVertex.setCityId(cityId);
            currentVertex.setEventId(event);
            currentVertex.setPostNodeId(postNodeId);
            lg.addVertex(currentVertex);
            if(lastVertex!=null){
                if(parcelCode.equals(lastParcelCode)) {
                    if(postNodeId!=lastPostNodeId
                        & lastCityId!=cityId){
                        PathEdge pe=lg.createEdge(lastVertex,currentVertex);
                        pe.print();
                    }
                }

            }
            lastVertex=currentVertex;
            lastParcelCode=parcelCode;
            lastPostNodeId=postNodeId;
            lastCityId=cityId;
        }

        System.out.println("Done!");
        resultSet.close();
        return lg;
    }

    private static double[] getGeo(String pCity,String runningPth){
        if(geos==null){
            geos=getGeo(runningPth);
        }


        return geos.get(pCity);
    }
    @SuppressWarnings("Duplicates")
    private static HashMap<String,double[]> getGeo(String runningPath) {

        JSONParser parser = new JSONParser();
        HashMap<String,double[]> geos=new HashMap<String, double[]>();
        try
        {

            Object obj = parser.parse(new FileReader(runningPath+"/data/city.json"));

            JSONObject jsonObject = (JSONObject) obj;

            JSONArray records= (JSONArray) jsonObject.get("RECORDS");

            for(int i=0;i<records.size();i++){
                JSONObject city= (JSONObject) records.get(i);
                String name= (String) city.get("name");
                name=name.substring(1,name.length());
                double[] latlon=new double[2];
                latlon[0]= (Double) city.get("latitude");
                latlon[1]= (Double) city.get("longitude");
                geos.put(name,latlon);
            }


        }catch (Exception e){
            e.printStackTrace();
        }
        return geos;
    }


    public static void main(String[] a){
        String name="ايلام";
        String orom="ارومیه";
        double[] res=getGeo(name,"F:\\Mehdi\\Projects\\jmapviewer2\\bin");
        System.out.println(res);
    }
}
