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
import java.sql.SQLException;

/*
    程序功能：找到搜索到次数最多的若干条记录。最多十条。
    编写时间：2019年7月13日
    作者：吕志伟
    */
@WebServlet("/A10001")
public class A10001 extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            JSONObject object=new JSONObject();
            JSONArray array=new JSONArray();
            DatabaseHelper databaseHelper=new DatabaseHelper();
            databaseHelper.init();
            databaseHelper.ChooseDatabase("ELEMENT");
            //按照搜索次数多少倒序排列，至多10条
            ResultSet resultSet=databaseHelper.ExecuteQuery("SELECT NAME,TYPECODE,INFORMATION FROM ELEMENT ORDER BY CLICK DESC LIMIT 10");
            while(resultSet.next()){
                //将结果装入JSON数组中，并且可根据数据块的相对位置得知搜索次数
                JSONObject jsonObject=new JSONObject();
                jsonObject.put("name",resultSet.getString("NAME"));
                jsonObject.put("type",resultSet.getInt("TYPECODE"));
                if(resultSet.getString("INFORMATION")!=null){
                    jsonObject.put("extra",resultSet.getString("INFORMATION"));
                }
                array.add(jsonObject);
            }
            object.put("results", array);
            response.setContentType("Application/JSON;charset=UTF-8");
            PrintWriter printWriter = response.getWriter();
            printWriter.print(object.toJSONString());
            databaseHelper.exit();
            System.gc();
        }catch(Exception e){
            //异常访问
            e.printStackTrace();
            response.sendError(403);
        }
    }
}
