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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

/*
    程序功能：根据Token找到对应用户的信息记录，并返回结果。
    编写时间：2019年7月15日
    作者：吕志伟
    */
@WebServlet("/A10004")
public class A10004 extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            //日常处理
            JSONObject inputJSON = JSONObject.parseObject(request.getParameter("data"));
            JSONObject object = new JSONObject();
            JSONObject container=new JSONObject();
            DatabaseHelper databaseHelper = new DatabaseHelper();
            databaseHelper.init();
            databaseHelper.ChooseDatabase("USER");
            //检查Token有效性
            int errorcode = Token_Check(databaseHelper, inputJSON.getString("token"));
            if(errorcode==0){
                String PhoneNumber=Get_PhoneNumber(databaseHelper,inputJSON.getString("token"));
                ResultSet resultSet2=databaseHelper.ExecuteQuery("SELECT NAME,ADDRESS,EMAIL FROM USER_INFO WHERE "
                +"PHONENUMBER=\'"+PhoneNumber+"\'");
                while (resultSet2.next()){
                    container.put("name",resultSet2.getString(1));
                    container.put("address",resultSet2.getString(2));
                    container.put("email",resultSet2.getString(3));
                    object.put("info",container);
                }
            }
            object.put("errorcode",errorcode);
            response.setContentType("Application/JSON;charset=UTF-8");
            PrintWriter printWriter = response.getWriter();
            printWriter.print(object.toJSONString());
            databaseHelper.exit();
            System.gc();
        }catch (Exception e){
            //异常访问
            e.printStackTrace();
            response.sendError(403);
        }
    }

    //检查Token有效性的方法，检查Token是否对应、有效。
    public static int Token_Check(DatabaseHelper databaseHelper,String Token){
        try {
            if(Token.length()!=30){
                //Token的长度不正确
                return 1;
            }
            ResultSet resultSet1=databaseHelper.ExecuteQuery("SELECT COUNT(*) FROM TOKEN WHERE TOKEN=\'"+
                    Token+"\';");
            if(resultSet1.next()){
                if(resultSet1.getInt(1)==0){
                    //不存在Token
                    return 1;
                }else{
                    //存在Token
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String Time = simpleDateFormat.format(System.currentTimeMillis() - (24 * 60 * 60 * 1000));
                    ResultSet resultSet2=databaseHelper.ExecuteQuery("SELECT COUNT(*) FROM TOKEN WHERE TOKEN = \'"+
                            Token+"\' AND CREATETIME > \'"+Time+"\'");
                    if (resultSet2.next()){
                        if(resultSet2.getInt(1)==0){
                            //Token过期，现已删除。
                            PreparedStatement preparedStatement=databaseHelper.getConnection().
                                    prepareStatement("UPDATE TOKEN SET TOKEN=NULL, CREATETIME=NULL WHERE TOKEN=?");
                            preparedStatement.setObject(1,Token);
                            preparedStatement.execute();
                            return 2;
                        }else{
                            //Token有效.
                            return 0;
                        }
                    }
                }
            }

        }catch (SQLException e){
            e.printStackTrace();
            return 3;
        }
        //不正常的位置
        return 3;
    }

    //检查Token对应的手机号的方法，请勿在未验证Token有效性的情况下调用。
    public static String Get_PhoneNumber(DatabaseHelper databaseHelper,String Token){
        try {
            ResultSet resultSet1 = databaseHelper.ExecuteQuery("SELECT PHONENUMBER FROM TOKEN WHERE "
                    + "TOKEN=\'" + Token + "\'");
            if (resultSet1.next()) {
                return resultSet1.getString(1);
            }
        }catch (SQLException e){
            e.printStackTrace();
            return "00000000000";
        }
        return "00000000000";
    }
}
