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

@WebServlet("/GetElement")
public class GetElement extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{
            doGet(request,response);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{
            response.setContentType("Application/JSON;charset=UTF-8");
        JSONObject element=new JSONObject();
        DatabaseHelper databaseHelper=new DatabaseHelper();
        databaseHelper.init();
        databaseHelper.ChooseDatabase("ELEMENT");
        String SQL="SELECT * FROM ELEMENT WHERE SHOPGROUP=0";
        ResultSet resultSet=databaseHelper.ExecuteQuery(SQL);

        int i=0;
            JSONArray jsonArray=new JSONArray();
        while(i<100&&resultSet.next()){
            int id=resultSet.getInt("ID");
            String name=resultSet.getString("NAME");
            JSONObject subjson=new JSONObject();
            subjson.put("id",id);
            subjson.put("name",name);
            jsonArray.add(subjson);
            i++;
        }
        element.put("data",jsonArray);
        PrintWriter printWriter=response.getWriter();
        printWriter.print(element);

    }catch (Exception e){
            e.printStackTrace();
        }
        System.gc();
    }
}
