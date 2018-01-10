package io.github.makbn;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class MainPathFactory {

    private static HashMap<String, double[]> geos;

    public static ArrayList<DirectedPath> readFromExcel (LocationGraph lg, String filePath,String runningPath) throws IOException

    {
        ArrayList<DirectedPath> directedPaths=new ArrayList<DirectedPath>();
        InputStream ExcelFileToRead = new FileInputStream(filePath);
        HSSFWorkbook wb = new HSSFWorkbook(ExcelFileToRead);

        HSSFSheet sheet=wb.getSheetAt(0);
        HSSFRow row;
        HSSFCell cell;

        Iterator rows = sheet.rowIterator();
        int rowIndex=0;

        while (rows.hasNext())
        {
            if(rowIndex<4) {
                rowIndex++;
                row=(HSSFRow) rows.next();
                continue;

            }else {
                row=(HSSFRow) rows.next();
                Iterator cells = row.cellIterator();
                int colIndex=0;
                String first=null,last=null;
                ArrayList<String> middle=new ArrayList<String>();
                while (cells.hasNext())
                {
                    cell=(HSSFCell) cells.next();
                    if(colIndex==1 || colIndex==5) {
                        if(cell.getStringCellValue()==null || cell.getStringCellValue().isEmpty()){
                            continue;
                        }
                        String[] cities=cell.getStringCellValue().trim().split("-");
                        if(colIndex==1) {
                            first = cities[0].trim();
                            last = cities[1].trim();
                        }else{
                            for(int i=0;i<cities.length;i++){
                                middle.add(cities[i].trim());
                            }
                        }


                    }

                    colIndex++;
                }
                if(first==null)
                    continue;
                ArrayList<LocationVertex> locationVertices=new ArrayList<LocationVertex>();

                ArrayList<String> pNames=new ArrayList<String>();
                pNames.add(first);

                for(String s:middle){
                    pNames.add(s);
                }
                pNames.add(last);

                for(String s:pNames){
                    LocationVertex vertex=lg.findByPName(s);
                    if(vertex==null){
                        double[] geo=getGeo(s,runningPath);
                        if(geo==null)
                            continue;
                        double lat=geo[0];
                        double lon=geo[1];
                        vertex=new LocationVertex(((rowIndex+1)*100)+(colIndex+1),lat,lon,null,null,-1,s,false);
                    }
                    if(vertex!=null)
                        locationVertices.add(vertex);
                }
                directedPaths.add(new DirectedPath(locationVertices));
                rowIndex++;
            }

        }
        return directedPaths;

    }

    private static double[] getGeo(String pCity,String runningPath){
        if(geos==null){
            geos=getGeo(runningPath);
        }
        return geos.get(pCity);
    }
    @SuppressWarnings("Duplicates")
    private static HashMap<String,double[]> getGeo(String runningPth) {

        JSONParser parser = new JSONParser();
        HashMap<String,double[]> geos=new HashMap<String, double[]>();
        try
        {

            Object obj = parser.parse(new FileReader(runningPth+"/data/city.json"));

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
}
