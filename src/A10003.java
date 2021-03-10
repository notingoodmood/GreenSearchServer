import com.alibaba.fastjson.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

/*
    程序功能：进行登录操作，下发Token。
    编写时间：2019年7月14日
    作者：吕志伟
    */
@WebServlet("/A10003")
public class A10003 extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    @SuppressWarnings("ALL")
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            //日常处理
            JSONObject inputJSON = JSONObject.parseObject(request.getParameter("data"));
            JSONObject object = new JSONObject();
            DatabaseHelper databaseHelper = new DatabaseHelper();
            databaseHelper.init();
            databaseHelper.ChooseDatabase("USER");
            String Token=TokenUtils.getToken();
            int errorcode;
            //验证是否是正确格式的手机号、验证码，若不是则直接拒绝服务
            if(inputJSON.getString("phonenumber").length()!=11||inputJSON.getString("code").length()!=6){
                throw new Exception();
            }
            if(Right_Loginkey(databaseHelper,inputJSON.getString("phonenumber")).equals(inputJSON.getString("code"))){
                //验证码比对成功
                errorcode=0;
                object.put("token",Token);
                //检查是否存在Token记录。若不存在，则创建。
                if(Have_Record(databaseHelper,inputJSON.getString("phonenumber"))){
                    //存在条目
                    databaseHelper.ExecuteSQL("UPDATE TOKEN SET TOKEN =\'"+Token+"\',CREATETIME=CURRENT_TIMESTAMP "+
                            "WHERE PHONENUMBER=\'"+inputJSON.getString("phonenumber")+"\';");
                }else{
                    //不存在条目
                    databaseHelper.ExecuteSQL("INSERT INTO TOKEN VALUES(\'"+inputJSON.getString("phonenumber")+"\',\'"+Token+
                            "\',CURRENT_TIMESTAMP);");
                }
            }else{
                //验证码比对不成功
                errorcode=2;
            }
            if(errorcode==0){
                if(!User_Exists(databaseHelper,inputJSON.getString("phonenumber"))){
                    //用户条目不存在
                    PreparedStatement preparedStatement=databaseHelper.getConnection().prepareStatement("INSERT INTO USER_INFO" +
                            "(PHONENUMBER) VALUES " +
                            "(?);");
                    preparedStatement.setObject(1,inputJSON.getString("phonenumber"));
                    preparedStatement.execute();
                }
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

    @SuppressWarnings("ALL")
    //工具方法，检查是否有Token条目的记录
    public static boolean Have_Record(DatabaseHelper databaseHelper,String PhoneNumber) throws SQLException {
        ResultSet resultSet1=databaseHelper.ExecuteQuery("SELECT COUNT(*) FROM TOKEN WHERE PHONENUMBER=\'"+PhoneNumber+"\';");
        if(resultSet1.next()){
            return resultSet1.getInt(1)!=0;
        }else{
            return false;
        }
    }

    @SuppressWarnings("ALL")
    //工具方法，检查有效的验证码数据.如果不存在，则为"000000".000000这个验证码永远不会碰撞到，是安全的。
    public static String Right_Loginkey(DatabaseHelper databaseHelper,String PhoneNumber) throws SQLException{
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String Time=simpleDateFormat.format(System.currentTimeMillis()-(10*60*1000));
        ResultSet resultSet1=databaseHelper.ExecuteQuery("SELECT COUNT(*) FROM SMS WHERE CREATETIME > \'"+
                Time+"\'");
        while (resultSet1.next()){
            if(resultSet1.getInt(1)==0){
                //找不到有效验证码
                return "000000";
            }else{
                //找到了有效的验证码
                ResultSet resultSet2=databaseHelper.ExecuteQuery("SELECT LOGINKEY FROM SMS WHERE CREATETIME > \'"+
                        Time+"\' ORDER BY CREATETIME DESC LIMIT 1");
                if (resultSet2.next()){
                    return resultSet2.getString(1);
                }
            }
        }
        //不正常的位置
        return "000000";
    }

    @SuppressWarnings("ALL")
    //工具方法，检查用户条目是否存在。
    public static boolean User_Exists(DatabaseHelper databaseHelper,String PhoneNumber){
        try {
            ResultSet resultSet = databaseHelper.ExecuteQuery("SELECT COUNT(*) FROM USER_INFO WHERE PHONENUMBER=\'" +
                    PhoneNumber + "\';");
            if(resultSet.next()){
                return resultSet.getInt(1)==1;
            }
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
        return false;
    }
}
