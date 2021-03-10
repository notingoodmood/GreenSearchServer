import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import Entity.Record;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CollectionHelper {
    //添加收藏条目
    static JSONObject AddCollection(String phone_number,String name,int typecode)throws SQLException {
        JSONObject add_result=new JSONObject();
        DatabaseHelper databaseHelper=new DatabaseHelper();
        databaseHelper.init();
        databaseHelper.ChooseDatabase("USER");
        if(databaseHelper.ExecuteQuery("SELECT * FROM COLLECTION WHERE PHONENUMBER='"+phone_number+"' AND NAME='"+name+"';").next()){
            add_result.put("add_result","AlreadyCollection");
            return add_result;

        }

        String Add_Sql ="INSERT INTO COLLECTION (PHONENUMBER,NAME,TYPECODE,TIME)" +
                "VALUES('"+phone_number+"','"+name+"',"+typecode+",NOW())";
        String Check_Sql="SELECT * FROM COLLECTION WHERE PHONENUMBER='"+phone_number+"' AND NAME='"+name+"';";
        databaseHelper.ExecuteSQL(Add_Sql);
        if(!databaseHelper.ExecuteQuery(Check_Sql).next())
            add_result.put("add_result",false);
        else
            add_result.put("add_result",true);
        return add_result;
    }
    //删除收藏条目
    static JSONObject DeleteCollection(String phone_number,String name)throws SQLException{
        JSONObject delete_result=new JSONObject();
        DatabaseHelper databaseHelper=new DatabaseHelper();
        databaseHelper.init();
        databaseHelper.ChooseDatabase("USER");

        String SQL= "DELETE FROM COLLECTION WHERE PHONENUMBER='"+phone_number+"' AND NAME='"+name+"';";
        String SQL1="SELECT * FROM COLLECTION WHERE PHONENUMBER='"+phone_number+"' AND NAME='"+name+"';";
        databaseHelper.ExecuteSQL(SQL);
        if(databaseHelper.ExecuteQuery(SQL1).next())
            delete_result.put("delete_result",false);
        else
            delete_result.put("delete_result",true);
        return delete_result;
    }
    //浏览收藏内容
    static JSONObject BrowseCollection(String phone_number)throws SQLException{
        JSONObject FianlResult=new JSONObject();
        DatabaseHelper databaseHelper=new DatabaseHelper();
        databaseHelper.init();
        databaseHelper.ChooseDatabase("USER");
        String SQL="SELECT * FROM COLLECTION WHERE PHONENUMBER='"+phone_number+"';";
        ResultSet data=databaseHelper.ExecuteQuery(SQL);
        JSONArray ArrayResult =new JSONArray();
        while(data.next()){
            String name=data.getString("NAME");
            String time=data.getString("TIME");
            int typecode=data.getInt("TYPECODE");
            JSONObject ObjectResult=new JSONObject();
            ObjectResult.put("name",name);
            ObjectResult.put("typecode",typecode);
            ObjectResult.put("time",time);
            ArrayResult.add(ObjectResult);
        }
        FianlResult.put("information",ArrayResult);
        return FianlResult;
    }
}
