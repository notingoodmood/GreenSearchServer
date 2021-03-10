/*
功能：一局游戏结束后向后台传递时间和得分
创建时间：2019/8/22
作者：杨榆丰
 */
import com.alibaba.fastjson.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;

@WebServlet("/G100003")
public class G100003 extends HttpServlet {
    synchronized protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    synchronized protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       try {
           JSONObject inputJSON = JSONObject.parseObject(request.getParameter("data"));
           String id = inputJSON.getString("id");
           String token = inputJSON.getString("token");
           int score = inputJSON.getIntValue("score");
           int time = inputJSON.getIntValue("time");

           PrintWriter printWriter = response.getWriter();
           response.setContentType("Application/JSON;charset=UTF-8");
           JSONObject result = new JSONObject();


           String checkSQL = "SELECT * FROM PLAYER WHERE ID=\'" + id + "\' AND TOKEN=\'" + token + "\';";
           String updateSQL = "UPDATE PLAYER SET SCORE=" + score + ",TOTALTIME=" + time + " WHERE ID=\'" + id + "\' AND TOKEN=\'" + token + "\';";

           DatabaseHelper databaseHelper = new DatabaseHelper();
           databaseHelper.init();
           databaseHelper.ChooseDatabase("GAME");
           ResultSet CheckResult = databaseHelper.ExecuteQuery(checkSQL);
           if (CheckResult.next()) {//身份验证通过才进行更新数据操作
               databaseHelper.ExecuteSQL(updateSQL);//数据更新
               //建立数据库连接对象并查询数据是否更新成功
               DatabaseHelper databaseHelper1=new DatabaseHelper();
               databaseHelper1.init();
               databaseHelper.ChooseDatabase("GAME");
               databaseHelper1.ChooseDatabase("GAME");
               ResultSet rs=databaseHelper1.ExecuteQuery("SELECT * FROM PLAYER WHERE ID=\'"+id+"\' AND SCORE="+score+" AND TOTALTIME="+time+";");
               if(rs.next())
                   result.put("result",true);
               else
                   result.put("result",false);
           } else//验证不通过，直接抛出异常
               throw new ServletException();
           printWriter.print(result);
           System.gc();
       }catch (Exception e){
           e.printStackTrace();
       }


    }
}
