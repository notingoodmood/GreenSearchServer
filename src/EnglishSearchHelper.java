
import Entity.*;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


import java.sql.ResultSet;
import java.sql.SQLException;


public class EnglishSearchHelper{
    synchronized static JSONObject Search(String word,int limit){
        JSONObject SearchResult=new JSONObject();

        JSONArray jingque=new JSONArray();
        JSONArray yanshengwu=new JSONArray();
        try{
            jingque=JingQue(word);
        }catch (SQLException e){
            e.printStackTrace();
        }
        try{
            yanshengwu=YangShengWu(word,limit);
        }catch (SQLException e){
            e.printStackTrace();
        }
        SearchResult.put("yanshengwu",yanshengwu);
        SearchResult.put("jingque",jingque);

        return SearchResult;
    }
    //精确搜索
    private static JSONArray JingQue(String word) throws SQLException {
        String UpWord=word.toUpperCase();
        String SQL="SELECT ID,NAME,TYPECODE,CLICK FROM ELEMENT_ENGLISH WHERE upper(NAME)='"+UpWord+"';";
        return DatabaseTravel(SQL,1);
    }

    //子串匹配模糊搜索衍生物
    private static JSONArray YangShengWu(String word,int limit) throws SQLException{
        String UpWord=word.toUpperCase();
        String SQL="SELECT ID,NAME,TYPECODE,CLICK FROM ELEMENT_ENGLISH WHERE upper(NAME) LIKE '%"+UpWord+"%'ORDER BY ID ASC;";
        return DatabaseTravel(SQL,limit);
    }

    //访问数据库查询数据并更新Click
    private static JSONArray DatabaseTravel(String SQL,int limit) throws SQLException{
        int count=0;
        JSONArray result=new JSONArray();
        DatabaseHelper databaseHelper=new DatabaseHelper();
        databaseHelper.init();
        databaseHelper.ChooseDatabase("ELEMENT");

        DatabaseHelper databaseHelper1=new DatabaseHelper();
        databaseHelper1.init();
        databaseHelper1.ChooseDatabase("ELEMENT");

        ResultSet Data=databaseHelper.ExecuteQuery(SQL);
        while (Data.next()){
            count++;
            English_Record record=new English_Record(Data.getInt("ID"),Data.getString("NAME")
                    , Data.getInt("TYPECODE"),Data.getInt("CLICK"));
            JSONObject demo = new JSONObject();
            demo.put("id",record.getID());
            demo.put("name",record.getNAME());
            demo.put("typecode",record.getTYPECODE());
            result.add(demo);
            int click=record.getCLICK();
            click++;
            String Update_Click_SQL="LOCK TABLES ELEMENT_ENGLISH WRITE;" +
                    "LOCK TABLES ELEMENT_ENGLISH READ;" +
                    "UPDATE ELEMENT_ENGLISH SET Click="+click+" WHERE ID="+record.getID()+";" +
                    "UNLOCK TABLES";
            databaseHelper1.ExecuteSQL(Update_Click_SQL);
            if(count>=limit){
                databaseHelper.exit();
                databaseHelper1.exit();
                return result;
            }
        }
        databaseHelper.exit();
        databaseHelper1.exit();
        return result;
    }


}
