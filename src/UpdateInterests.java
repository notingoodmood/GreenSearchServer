import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

public class UpdateInterests {
    //传入用户phonenumber和垃圾id，更新用户购物兴趣值,使其变化store_value
    synchronized public static void UpdateShopInterest(int id,String phone_num,int store_value,String column)throws SQLException {
        //与用户信息表的数据库连接
        DatabaseHelper databaseHelper=new DatabaseHelper();
        databaseHelper.init();
        databaseHelper.ChooseDatabase("USER");

        //与垃圾表的数据库连接
        DatabaseHelper databaseHelper1=new DatabaseHelper();
        databaseHelper1.init();
        databaseHelper1.ChooseDatabase("ELEMENT");

        String SQL1="SELECT SHOPGROUP FROM ELEMENT WHERE ID="+id;
        String SQL2="SELECT "+column+" FROM USER_INFO WHERE PHONENUMBER=\'"+phone_num+"\';";
        ResultSet resultSet1=databaseHelper1.ExecuteQuery(SQL1);

        resultSet1.next();
        int shopgroup=resultSet1.getInt("SHOPGROUP");
        //查询的垃圾暂无分组，直接退出
        if(shopgroup==999){
            //System.out.println("too complex!");
            return;
        }

        ResultSet resultSet=databaseHelper.ExecuteQuery(SQL2);
        resultSet.next();
        JSONArray jsonArray=JSONArray.parseArray(resultSet.getString(column));

        boolean is_store=false;
        int flag=100;
        if(jsonArray==null)
            jsonArray=new JSONArray();
        else
        for(int i=0;i<jsonArray.size();i++){
            JSONObject object= (JSONObject) jsonArray.get(i);
            if(object.getIntValue("group")==shopgroup) {
                is_store = true;
                flag=i;
                break;
            }
        }

        //没有这个组别的记录且记录中少于20组记录
        if(jsonArray.size()<20&&!is_store){
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("group",shopgroup);
            jsonObject.put("value",store_value);
            jsonArray.add(jsonObject);
            databaseHelper.ExecuteSQL("UPDATE USER_INFO SET "+column+" =\'"+jsonArray.toJSONString()+"\' WHERE PHONENUMBER=\'"+phone_num+"\';");
            //没有这个组的记录且记录已经为20组
        }else if(jsonArray.size()==20&&!is_store){
            int flag1=100,value1=10000;
            for(int j=0;j<jsonArray.size();j++){
                JSONObject jsonObject=jsonArray.getJSONObject(j);
                if(jsonObject.getIntValue("value")<value1){
                    value1=jsonObject.getIntValue("value");
                    flag1=j;
                }
            }
            //System.out.println(""+value1+flag1);
            if(value1==1){
                jsonArray.remove(flag1);
                JSONObject jsonObject=new JSONObject();
                jsonObject.put("group",shopgroup);
                jsonObject.put("value",store_value);
                jsonArray.add(jsonObject);
                databaseHelper.ExecuteSQL("UPDATE USER_INFO SET  "+column+" =\'"+jsonArray.toJSONString()+"\' WHERE PHONENUMBER=\'"+phone_num+"\';");
            }
         //有这个组的记录，这个组上的记录value加一
        }else if(is_store){
            JSONObject jsonObject=jsonArray.getJSONObject(flag);

            int newshopvalue=jsonObject.getIntValue("value");
            newshopvalue+=store_value;
            jsonObject.remove("value");
            jsonObject.put("value",newshopvalue);
            jsonArray.remove(flag);
            jsonArray.add(jsonObject);
            databaseHelper.ExecuteSQL("UPDATE USER_INFO SET  "+column+" =\'"+jsonArray.toJSONString()+"\' WHERE PHONENUMBER=\'"+phone_num+"\';");
        }
    }
    //如果更新时间在一周之前则将用户兴趣值减一，为零的就直接清除掉
     synchronized static public void TimeoutDelete(){
        try{
        DatabaseHelper select_databaseHelper=new DatabaseHelper();//用于搜索的数据库连接
        select_databaseHelper.init();
        select_databaseHelper.ChooseDatabase("USER");
        String SQL1="SELECT * FROM USER_INFO WHERE SHOPPINGINTERESTS!=''";
        DatabaseHelper update_databaseHelper=new DatabaseHelper();//用于更新数据的数据库连接
        update_databaseHelper.init();
        update_databaseHelper.ChooseDatabase("USER");

        ResultSet resultSet=select_databaseHelper.ExecuteQuery(SQL1);
        while(resultSet.next()){
            Long lasttime=Long.valueOf(resultSet.getString("UPDATETIME"));
            Long nowtime=System.currentTimeMillis();
            if(nowtime-lasttime>=604800000L){//一周的毫秒数
                String phone_num=resultSet.getString("PHONENUMBER");
                JSONArray jsonArray=JSONArray.parseArray(resultSet.getString("SHOPPINGINTERESTS"));
                for(int i=0;i<jsonArray.size();i++){
                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                    int value=jsonObject.getIntValue("value")-1;
                    if(value==0) {//减少后value值为零，直接删除
                        jsonArray.remove(i);
                        continue;
                    }
                    jsonObject.replace("value",value);
                    jsonArray.set(i,jsonObject);
                }
               update_databaseHelper.ExecuteSQL("UPDATE USER_INFO SET SHOPPINGINTERESTS='"+jsonArray.toJSONString()+"' ,UPDATETIME='"+nowtime+"' WHERE PHONENUMBER='"+phone_num+"';");
            }
        }
     }catch (Exception e){
            e.printStackTrace();
        }
    }

    synchronized static public void Mondayclear(){
        Calendar cal = Calendar.getInstance();
        if(cal.get(Calendar.DAY_OF_WEEK)==Calendar.MONDAY){
            DatabaseHelper databaseHelper=new DatabaseHelper();
            databaseHelper.init();
            databaseHelper.ChooseDatabase("USER");
            databaseHelper.ExecuteSQL("UPDATE USER_INFO SET FOODRECORD=null WHERE 1");
        }
    }


}
