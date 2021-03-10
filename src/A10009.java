/*
功能：实现收藏夹的收藏、删除和浏览功能
创建日期：2019/8/13
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
import java.text.SimpleDateFormat;
import java.util.Collections;

@WebServlet("/A10009")
public class A10009 extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{
            JSONObject inputJson=JSONObject.parseObject(request.getParameter("data"));
            String Phonenumber=inputJson.getString("tel");
            int Option=inputJson.getIntValue("opt");
            String name=inputJson.getString("name");
            int typecode=inputJson.getIntValue("typecode");
            response.setContentType("Application/JSON;charset=UTF-8");
            PrintWriter printWriter=response.getWriter();
            if(Option==1){
                printWriter.print(CollectionHelper.AddCollection(Phonenumber,name,typecode));
            }
            else if(Option==2){
                printWriter.print(CollectionHelper.DeleteCollection(Phonenumber,name));
            }
            else{
                printWriter.print(CollectionHelper.BrowseCollection(Phonenumber));
            }
        }catch (Exception e){
            e.printStackTrace();
            response.sendError(403);
        }
        System.gc();

    }

}
