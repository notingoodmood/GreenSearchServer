/*
功能：对玩家查询排行榜的请求进行响应，返回目前数据库中得分和时间综合排序最高的10位玩家
创建时间：2018/9/23
作者：杨榆丰
 */
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

@WebServlet("/G100004")
public class G100004 extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{
            response.setContentType("Application/JSON;charset=UTF-8");
            PrintWriter printWriter=response.getWriter();
            JSONArray ranking=new JSONArray();

            String SQL="SELECT * FROM PLAYER ORDER BY SCORE DESC,TOTALTIME ASC;";

            DatabaseHelper databaseHelper=new DatabaseHelper();
            databaseHelper.init();
            databaseHelper.ChooseDatabase("GAME");
            ResultSet rs=databaseHelper.ExecuteQuery(SQL);
            int index=1;
            while (rs.next()&&index<=10){
                String name=rs.getString("NAME");
                int time=rs.getInt("TOTALTIME");
                int score=rs.getInt("SCORE");
                JSONObject innerJson=new JSONObject();
                innerJson.put("ranking",index);
                innerJson.put("name",name);
                innerJson.put("score",score);
                innerJson.put("time",time);
                ranking.add(innerJson);
                index++;
            }
            printWriter.print(ranking);
            System.gc();

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
