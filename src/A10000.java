import com.alibaba.fastjson.JSONObject;

import javax.servlet.annotation.WebServlet;
import java.io.IOException;
import java.io.PrintWriter;


/*
    程序功能：收集输入的关键字，寻找相应的结果。找到的结果分为三类：
    1.精确结果;
    2.模糊搜索(根据全拼拼音)；
    3.衍生物搜索;
    4.联想搜索.
    编写时间：2019年7月12日
    更新时间：2019年7月16日
    作者：吕志伟
    */

@WebServlet("/A10000")
public class A10000 extends javax.servlet.http.HttpServlet {
    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        doGet(request,response);
    }

    @SuppressWarnings("ALL")
    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        try {
            JSONObject inputJSON = JSONObject.parseObject(request.getParameter("data"));
            String word=inputJSON.getString("input");
            response.setContentType("Application/JSON;charset=UTF-8");
            PrintWriter printWriter = response.getWriter();
            printWriter.print(SearchHelper.Search(word,10));
            System.gc();
            //等待更新
        }catch (Exception e){
            e.printStackTrace();
            //异常访问
            response.sendError(403);
        }
    }

}
