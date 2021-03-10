import com.alibaba.fastjson.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;

/*
    程序功能：根据传入的信息更新用户的个人信息。
    编写时间：2019年7月15日
    作者：吕志伟
    */
@WebServlet("/A10005")
public class A10005 extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            //日常处理
            JSONObject inputJSON = JSONObject.parseObject(request.getParameter("data"));
            JSONObject infoJSON=(JSONObject)inputJSON.getJSONObject("info");
            JSONObject object = new JSONObject();
            DatabaseHelper databaseHelper = new DatabaseHelper();
            databaseHelper.init();
            databaseHelper.ChooseDatabase("USER");
            //Token检查
            int errorcode=A10004.Token_Check(databaseHelper,inputJSON.getString("token"));
            String PhoneNumber=A10004.Get_PhoneNumber(databaseHelper,inputJSON.getString("token"));
            //输入信息检查
            if(infoJSON.getString("address").length()>25||infoJSON.getString("name").length()>15||
                    infoJSON.getString("email").length()>40){
                errorcode=3;
            }
            if(errorcode==0){
                Connection connection=databaseHelper.getConnection();
                PreparedStatement preparedStatement=connection.prepareStatement("UPDATE USER_INFO SET ADDRESS=?,NAME=?,EMAIL=? WHERE PHONENUMBER=?");
                preparedStatement.setObject(1,infoJSON.getString("address"));
                preparedStatement.setObject(2,infoJSON.getString("name"));
                preparedStatement.setObject(3,infoJSON.getString("email"));
                preparedStatement.setObject(4,PhoneNumber);
                preparedStatement.execute();
            }
            object.put("errorcode",errorcode);
            response.setContentType("Application/JSON;charset=UTF-8");
            PrintWriter printWriter = response.getWriter();
            printWriter.print(object.toJSONString());
            databaseHelper.exit();
            System.gc();
        }catch (Exception e){
            e.printStackTrace();
            response.sendError(403);
        }
    }
}
