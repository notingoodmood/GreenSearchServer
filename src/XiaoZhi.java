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

@WebServlet("/XiaoZhi")

public class XiaoZhi extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{
            JSONObject inputJSON = JSONObject.parseObject(request.getParameter("data"));
            String word=inputJSON.getString("input");
            String phone_num = inputJSON.getString("phone_num");
            response.setContentType("Application/JSON;charset=UTF-8");
            PrintWriter printWriter = response.getWriter();
            JSONObject searchresult = SearchHelper.Search(word,100);
            JSONObject result = new JSONObject();
            result.put("name",word);
            int id = 0;//用于存储命中的条目编号
            if(searchresult.getJSONArray("jingque").size()!=0){//首先看精确搜索有没有，有就直接返回
                JSONArray array=searchresult.getJSONArray("jingque");
                result.put("type",array.getJSONObject(0).getIntValue("type"));
                id = array.getJSONObject(0).getIntValue("id");//记录条目编号
            }else if(searchresult.getJSONArray("quanpin").size()!=0){//没有精确就看全拼搜索，有就直接返回
                JSONArray array=searchresult.getJSONArray("quanpin");
                result.put("type",array.getJSONObject(0).getIntValue("type"));
                id = array.getJSONObject(0).getIntValue("id");//记录条目编号
            }else if(searchresult.getJSONArray("lianxiang").size()!=0){//没有全拼就看联想搜索，有就直接返回
                JSONArray array=searchresult.getJSONArray("lianxiang");
                result.put("type",array.getJSONObject(0).getIntValue("type"));
                id = array.getJSONObject(0).getIntValue("id");//记录条目编号
            }else if(searchresult.getJSONArray("lianxiang").size()!=0){
                JSONArray array=searchresult.getJSONArray("lianxiang");
                result.put("type",array.getJSONObject(0).getIntValue("type"));
                id = array.getJSONObject(0).getIntValue("id");//记录条目编号
            }else{
                result.put("wrong_reason","no find");
            }
            printWriter.print(result);

            //下面改变用户兴趣值
            if(id!=0) {
                UpdateInterests.Mondayclear();
                UpdateInterests.UpdateShopInterest(id, phone_num, 1, "FOODRECORD");
            }

            System.gc();
        }catch (Exception e){
            e.printStackTrace();
            response.sendError(403);
        }
    }
}
