import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import com.alibaba.fastjson.*;

@WebServlet("/UploadGroup")
public class UploadGroup extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{
            JSONObject inputJson=JSONObject.parseObject(request.getParameter("data"));
            int id=inputJson.getIntValue("id");
            int shopgroup=inputJson.getIntValue("shopgroup");
            String SQL="UPDATE ELEMENT SET SHOPGROUP="+shopgroup+" WHERE ID="+id+";";
            DatabaseHelper databaseHelper=new DatabaseHelper();
            databaseHelper.init();
            databaseHelper.ChooseDatabase("ELEMENT");
            databaseHelper.ExecuteSQL(SQL);

        }catch (Exception e){
            e.printStackTrace();
        }
        System.gc();
    }
}
