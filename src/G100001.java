/*
 功能：在游戏启动时自动为玩家生成ID和姓名并发给玩家
 创建时间：2019/8/22
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
import java.sql.ResultSet;

@WebServlet("/G100001")
public class G100001 extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    synchronized protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{
            response.setContentType("Application/JSON;charset=UTF-8");
            //63个性格形容词
            String[] FirstWord={"开朗","大方","主动","外向","俏皮","敏捷","乐观","调皮","爽脆","爽朗","豪爽","正直","直率","直爽","干脆","爽直","刚直","憨直","率直","耿直","公正","公道","公平","公允","正派","爽快","简捷","开阔","豁达","明朗","率真","真诚","热诚","至诚","赤诚","诚挚","恳切","纯真","率直","坦率","笃实","热忱","热诚","热心","好客","客气","殷勤","和气","和蔼","和善","和婉","和悦","和易","亲切","过谦","谦卑","谦恭","谦和","谦让","谦虚","谦逊","虚心","自谦"};
            response.setContentType("Application/JSON;charset=UTF-8");
            PrintWriter printWriter = response.getWriter();

            DatabaseHelper QuestionDBHelper=new DatabaseHelper();
            DatabaseHelper PlayerBDHelper=new DatabaseHelper();
            QuestionDBHelper.init();
            PlayerBDHelper.init();
            QuestionDBHelper.ChooseDatabase("GAME");
            PlayerBDHelper.ChooseDatabase("GAME");

            String NumSelectSQL="SELECT COUNT(*) FROM QUESTION;";
            ResultSet num=QuestionDBHelper.ExecuteQuery(NumSelectSQL);
            num.next();
            String NameSelectSQL="SELECT NAME FROM QUESTION WHERE ID="+System.currentTimeMillis()%(num.getInt(1))+";";
            ResultSet Secondword=QuestionDBHelper.ExecuteQuery(NameSelectSQL);
            Secondword.next();
            String PlayerName=FirstWord[(int) (System.currentTimeMillis()%63)]+"的"+Secondword.getString("NAME");
            String PlayerId=String.valueOf(System.currentTimeMillis()-1566451996730L);

            String Token=TokenUtils.getToken();

            String PlayerSQL="INSERT INTO PLAYER VALUES(\'"+PlayerId+"\',\'"+PlayerName+"\',0,NULL,\'"+Token+"\');";
            PlayerBDHelper.ExecuteSQL(PlayerSQL);

            JSONObject player=new JSONObject();
            player.put("id",PlayerId);
            player.put("name",PlayerName);
            player.put("token",Token);
            printWriter.print(player);
            System.gc();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
