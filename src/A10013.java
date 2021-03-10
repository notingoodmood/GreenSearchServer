/*
获取健康监测数据
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

@WebServlet("/A10013")
public class A10013 extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            JSONObject jsonObject = JSON.parseObject(request.getParameter("data"));
            String phone_num = jsonObject.getString("phone_num");
            response.setContentType("Application/JSON;charset=UTF-8");
            PrintWriter printWriter = response.getWriter();
            DatabaseHelper interest_select = new DatabaseHelper();
            interest_select.init();
            interest_select.ChooseDatabase("USER");
            DatabaseHelper link_select = new DatabaseHelper();
            link_select.init();
            link_select.ChooseDatabase("SHOP");
            JSONObject result = new JSONObject();
            JSONArray queshao = new JSONArray();
            JSONArray guoduo = new JSONArray();
            JSONArray shaoliang = new JSONArray();

            ResultSet interest = interest_select.ExecuteQuery("SELECT FOODRECORD FROM USER_INFO WHERE PHONENUMBER='" + phone_num + "';");
            interest.next();
            if(interest.getString("FOODRECORD")!=null){
                JSONArray jsonArray=JSONArray.parseArray(interest.getString("FOODRECORD"));

                //设置缺少标志位（0：肉类）（1：蔬菜）（2：水果）（3：奶制品）（4：坚果）
                boolean lack_flag[]=new boolean[5];

                for(int i=0;i<jsonArray.size();i++){
                    //与饮食无关的值直接跳过
                    int group_id = jsonArray.getJSONObject(i).getIntValue("group");
                    if(group_id>200||group_id==100||group_id==110||group_id==160||group_id==170)
                        continue;
                    //逐个饮食类进行处理
                    int group_value = jsonArray.getJSONObject(i).getIntValue("value");
                    if(group_id==120&&group_value>50){ //零食过量
                        JSONObject innerjson = new JSONObject();
                        innerjson.put("food","零食");
                        innerjson.put("status","过量");
                        innerjson.put("times",group_value);
                        guoduo.add(innerjson);
                        continue;
                    }
                    if(group_id==131&&group_value>50){ //肉类过量
                        JSONObject innerjson = new JSONObject();
                        innerjson.put("food","肉类");
                        innerjson.put("status","过量");
                        innerjson.put("times",group_value);
                        guoduo.add(innerjson);
                        lack_flag[0] = true;//缺少标志位置1
                        continue;
                    }
                    if(group_id==134&&group_value>50){ //奶制品过量
                        JSONObject innerjson = new JSONObject();
                        innerjson.put("food","奶制品");
                        innerjson.put("status","过量");
                        innerjson.put("times",group_value);
                        guoduo.add(innerjson);
                        lack_flag[3] = true;//缺少标志位置1
                        continue;
                    }
                    if(group_id==180&&group_value>50){ //坚果过量
                        JSONObject innerjson = new JSONObject();
                        innerjson.put("food","坚果");
                        innerjson.put("status","过量");
                        innerjson.put("times",group_value);
                        guoduo.add(innerjson);
                        lack_flag[4] = true;//缺少标志位置1
                        continue;
                    }
                    if(group_id==131&&group_value<5){ //肉类少量
                        JSONObject innerjson = new JSONObject();
                        innerjson.put("food","肉类");
                        innerjson.put("status","少量");
                        innerjson.put("times",group_value);
                        shaoliang.add(innerjson);
                        lack_flag[0] = true;//缺少标志位置1
                        continue;
                    }
                    if(group_id==134&&group_value<5){ //奶制品少量
                        JSONObject innerjson = new JSONObject();
                        innerjson.put("food","奶制品");
                        innerjson.put("status","少量");
                        innerjson.put("times",group_value);
                        shaoliang.add(innerjson);
                        lack_flag[3] = true;//缺少标志位置1
                        continue;
                    }
                    if(group_id==132&&group_value<5){ //蔬菜少量
                        JSONObject innerjson = new JSONObject();
                        innerjson.put("food","蔬菜");
                        innerjson.put("status","少量");
                        innerjson.put("times",group_value);
                        shaoliang.add(innerjson);
                        lack_flag[1] = true;//缺少标志位置1
                        continue;
                    }
                    if(group_id==133&&group_value<5){ //水果少量
                        JSONObject innerjson = new JSONObject();
                        innerjson.put("food","水果");
                        innerjson.put("status","少量");
                        innerjson.put("times",group_value);
                        shaoliang.add(innerjson);
                        lack_flag[2] = true;//缺少标志位置1
                        continue;
                    }

                    if (group_id==180&&group_value<3){
                        JSONObject innerjson = new JSONObject();
                        innerjson.put("food","坚果");
                        innerjson.put("status","少量");
                        innerjson.put("times",group_value);
                        shaoliang.add(innerjson);
                        lack_flag[4]=true;
                    }
                }
                //处理缺少项
                if(!lack_flag[0]){
                    JSONObject innerjson = new JSONObject();
                    innerjson.put("food","肉类");
                    innerjson.put("status","缺少");
                    innerjson.put("times",0);
                    queshao.add(innerjson);
                }
                if(!lack_flag[1]){
                    JSONObject innerjson = new JSONObject();
                    innerjson.put("food","蔬菜");
                    innerjson.put("status","缺少");
                    innerjson.put("times",0);
                    queshao.add(innerjson);
                }
                if(!lack_flag[2]){
                    JSONObject innerjson = new JSONObject();
                    innerjson.put("food","水果");
                    innerjson.put("status","缺少");
                    innerjson.put("times",0);
                    queshao.add(innerjson);
                }
                if(!lack_flag[3]){
                    JSONObject innerjson = new JSONObject();
                    innerjson.put("food","奶制品");
                    innerjson.put("status","缺少");
                    innerjson.put("times",0);
                    queshao.add(innerjson);
                }
                if(!lack_flag[4]){
                    JSONObject innerjson = new JSONObject();
                    innerjson.put("food","坚果");
                    innerjson.put("status","缺少");
                    innerjson.put("times",0);
                    queshao.add(innerjson);
                }
                result.put("result",1);
                result.put("guoliang",guoduo);
                result.put("shaoliang",shaoliang);
                result.put("queshao",queshao);


            }else{
                result.put("result",0);
            }
            printWriter.print(result);

        }catch (Exception e){
            e.printStackTrace();
        }
        System.gc();
    }
}
