/*
功能：在用户点击搜索列表某个对象时对用户购物兴趣值进行更新
创建时间：2019/9/25
作者：杨榆丰
 */

import com.alibaba.fastjson.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/A10010")
public class A10010 extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            JSONObject jsonObject = JSONObject.parseObject(request.getParameter("data"));
            int id = jsonObject.getIntValue("id");
            String phone_num = jsonObject.getString("phonenumber");
            UpdateInterests.UpdateShopInterest(id, phone_num,1);
            UpdateInterests.TimeoutDelete();
        }catch (Exception e){
            e.printStackTrace();
        }
        System.gc();
    }
}
