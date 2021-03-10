import com.alibaba.fastjson.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;

/*
    程序功能：下发验证码到用户手机号，用于登录。
    编写时间：2019年7月14日
    作者：吕志伟
    */
@WebServlet("/A10002")
public class A10002 extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    @SuppressWarnings("ALL")
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{
            //产生六位随机数字
            long LOGINKEY=System.currentTimeMillis()%100;
            LOGINKEY*=8694;
            LOGINKEY+=100000;
            int errorcode;
            JSONObject inputJSON=JSONObject.parseObject(request.getParameter("data"));
            JSONObject object=new JSONObject();
            DatabaseHelper databaseHelper=new DatabaseHelper();
            databaseHelper.init();
            databaseHelper.ChooseDatabase("USER");
            Connection connection=databaseHelper.getConnection();
            //先去检查是否在SMS表上存在60秒内发送过的短信，或者今天是否已经发送超过3条短信
            errorcode=Can_Send_SMS(databaseHelper,inputJSON.getString("phonenumber"));
            if(errorcode==0) {
                //验证正常
                //发送验证码短信
                Boolean Success=SMSHelper.Send_Login_SMS(inputJSON.getString("phonenumber"),LOGINKEY+"",10);
                if(!Success){
                    //验证码下发失败，更新错误码
                    object.put("errorcode",3);
                }else{
                    //在数据库上记载发送记录
                    object.put("errorcode",0);
                    PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO SMS(PHONENUMBER,LOGINKEY)" +
                            " VALUES(?,?)");
                    preparedStatement.setObject(1, inputJSON.getString("phonenumber"));
                    preparedStatement.setObject(2, LOGINKEY + "");
                    preparedStatement.execute();
                }
            }else {
                //出现异常
                object.put("errorcode",errorcode);
            }
            response.setContentType("Application/JSON;charset=UTF-8");
            PrintWriter printWriter = response.getWriter();
            printWriter.print(object.toJSONString());
            databaseHelper.exit();
            System.gc();
        }catch (Exception e){
            //错误访问
            e.printStackTrace();
            response.sendError(403);
        }
    }
    //工具方法，检查是否可以发送短信
    private static int Can_Send_SMS(DatabaseHelper databaseHelper,String Phonenumber)throws SQLException {
        long Current_Time=System.currentTimeMillis()-(60*1000);
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String Date=simpleDateFormat.format(Current_Time);
        ResultSet resultSet1=databaseHelper.ExecuteQuery("SELECT COUNT(*) FROM SMS WHERE PHONENUMBER=\'"+Phonenumber+
                "\' AND CREATETIME > \'"+Date+"\' ;");
        if(resultSet1.next()){
            if(resultSet1.getInt(1)!=0){
                return 1;
            }else{
                ResultSet resultSet2=databaseHelper.ExecuteQuery("SELECT COUNT(*) FROM SMS WHERE PHONENUMBER=\'"+Phonenumber+
                        "\' AND DATE(CREATETIME)=CURRENT_DATE()");
                if(resultSet2.next()){
                    if(resultSet2.getInt(1)>3){
                        return 2;
                    }else{
                        return 0;
                    }
                }else{
                   return 3;
                }
            }
        }else{
            return 3;
        }
    }
}
