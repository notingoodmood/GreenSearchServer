/*
功能：实现根据用户所丢垃圾的信息，向用户推送具体的广告
日期：2019/9/30
作者：杨榆丰
 */
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;

@WebServlet("/A10012")
public class A10012 extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    synchronized  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            JSONObject jsonObject = JSON.parseObject(request.getParameter("data"));
            String phone_num = jsonObject.getString("phonenumber");
            response.setContentType("Application/JSON;charset=UTF-8");
            PrintWriter printWriter = response.getWriter();
            JSONObject result = new JSONObject();
            JSONArray interest_result=new JSONArray();
            JSONArray random_result=new JSONArray();

            DatabaseHelper interest_select = new DatabaseHelper();
            interest_select.init();
            interest_select.ChooseDatabase("USER");
            DatabaseHelper link_select = new DatabaseHelper();
            link_select.init();
            link_select.ChooseDatabase("SHOP");

            ResultSet interest = interest_select.ExecuteQuery("SELECT SHOPPINGINTERESTS FROM USER_INFO WHERE PHONENUMBER='" + phone_num + "';");
            int index=1;
            interest.next();
            if (interest.getString("SHOPPINGINTERESTS")!=null){//有感兴趣的商品分组
                int mostinterest=0,flag=0;//mostinterest记录用户最感兴趣的商品分组
                JSONArray jsonArray=JSONArray.parseArray(interest.getString("SHOPPINGINTERESTS"));
                for(int i=0;i<jsonArray.size();i++){
                    JSONObject jsonObject1=jsonArray.getJSONObject(i);
                    if(jsonObject1.getIntValue("value")>flag){
                        mostinterest=jsonObject1.getIntValue("group");
                        flag=jsonObject1.getIntValue("value");
                    }
                }
                ResultSet resultSet=link_select.ExecuteQuery("SELECT * FROM GOODS WHERE SHOPGROUP="+mostinterest);
                while(resultSet.next()&&index<=4){
                    String name=resultSet.getString("NAME");
                    String link=resultSet.getString("LINK");
                    int pic_id=resultSet.getInt("ID");
                    JSONObject innerjson=new JSONObject();
                    innerjson.put("name",name);
                    innerjson.put("link",link);
                    innerjson.put("pic_id",pic_id);
                    interest_result.add(innerjson);
                    index++;
                }
                ResultSet other_result=link_select.ExecuteQuery("select * from GOODS order by rand() limit 50 ");
                while(other_result.next()&&index<=8){
                    if(other_result.getInt("SHOPGROUP")!=mostinterest){
                        String link=other_result.getString("LINK");
                        String name=other_result.getString("NAME");
                        int pic_id=other_result.getInt("ID");
                        JSONObject innerjson=new JSONObject();
                        innerjson.put("name",name);
                        innerjson.put("link",link);
                        innerjson.put("pic_id",pic_id);
                        random_result.add(innerjson);
                        index++;
                    }
                }
            }else{//用户的商品兴趣记录为空，则随机推送8条广告信息
                ResultSet other_result=link_select.ExecuteQuery("select * from GOODS order by rand() limit 50 ");
                while(other_result.next()&&index<=8){
                    String name=other_result.getString("NAME");
                    int pic_id=other_result.getInt("ID");
                    String link=other_result.getString("LINK");
                    JSONObject inner_json=new JSONObject();
                    inner_json.put("name",name);
                    inner_json.put("link",link);
                    inner_json.put("pic_id",pic_id);
                    random_result.add(inner_json);
                    index++;
                    }
            }
            result.put("interest",interest_result);
            result.put("random",random_result);
                printWriter.print(result);
        }catch (Exception e){
            e.printStackTrace();
        }
        System.gc();
    }
}
