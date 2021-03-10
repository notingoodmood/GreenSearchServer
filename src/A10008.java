/*
功能：实现英文搜索
创建时间：2019/8/9
作者：杨榆丰
 */

import com.alibaba.fastjson.JSONObject;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;
import java.io.PrintWriter;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/A10008")
public class A10008 extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{
            JSONObject inputJSON = JSONObject.parseObject(request.getParameter("data"));
            String word=inputJSON.getString("input");
            response.setContentType("Application/JSON;charset=UTF-8");
            PrintWriter printWriter = response.getWriter();
            printWriter.print(EnglishSearchHelper.Search(word,10));
            System.gc();
        }catch (Exception e){
            e.printStackTrace();
            response.sendError(403);
        }
    }
}
