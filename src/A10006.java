import com.alibaba.fastjson.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.function.LongToDoubleFunction;

@WebServlet("/A10006")
public class A10006 extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{
            JSONObject inputJSON = JSONObject.parseObject(request.getParameter("data"));
            String word=inputJSON.getString("input");
            response.setContentType("Application/JSON;charset=UTF-8");
            PrintWriter printWriter = response.getWriter();
            printWriter.print(SearchHelper.Search(word,100));
            System.gc();
        }catch (Exception e){
            e.printStackTrace();
            response.sendError(403);
        }
    }
}
