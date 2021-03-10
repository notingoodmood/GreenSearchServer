/*
功能：游戏初始化时为用户下发10道题目
创建时间：2019/8/22
作者：杨榆丰
 */


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

@WebServlet("/G100002")
public class G100002 extends HttpServlet {
    synchronized protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);

    }

    synchronized protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        try{
            JSONObject inputJSON = JSONObject.parseObject(request.getParameter("data"));
            String token=inputJSON.getString("token");
            String id=inputJSON.getString("id");
            response.setContentType("Application/JSON;charset=UTF-8");

            JSONObject Questions=new JSONObject();
            JSONArray Easy=new JSONArray();
            JSONArray Mid=new JSONArray();
            JSONArray Hard=new JSONArray();

            PrintWriter printWriter=response.getWriter();
            DatabaseHelper databaseHelper=new DatabaseHelper();
                    databaseHelper.init();
                    databaseHelper.ChooseDatabase("GAME");

                    ResultSet resultSet=databaseHelper.ExecuteQuery("SELECT * FROM PLAYER WHERE ID=\'"+id+"\' AND TOKEN=\'"+token+"\';");
                    if(resultSet.next()) {
                        int index=1;
                        //查找5道简单题
                        String EasyNumSelectSQL = "SELECT * FROM QUESTION WHERE LEVEL=1;";
                        ResultSet EasyRs = databaseHelper.ExecuteQuery(EasyNumSelectSQL);
                        EasyRs.last();
                        int EasyNum=EasyRs.getRow();
                        int easy_index_min = Long.valueOf(System.currentTimeMillis() % (EasyNum - 4)).intValue();
                        EasyRs.first();
                        int i=0;
                        while(EasyRs.next()){
                            i++;
                            if(i>=easy_index_min&&i<=easy_index_min+4){
                                String name=EasyRs.getString("NAME");
                                int typecode=EasyRs.getInt("TYPECODE");
                                int level=EasyRs.getInt("LEVEL");
                                JSONObject innerJson=new JSONObject();
                                innerJson.put("name",name);
                                innerJson.put("typecode",typecode);
                                innerJson.put("level",level);
                                Easy.add(innerJson);
                            }
                        }

                        //查找3道中难度题
                        String MidNumSelectSQL = "SELECT * FROM QUESTION WHERE LEVEL=2;";
                        ResultSet MidRs = databaseHelper.ExecuteQuery(MidNumSelectSQL);
                        MidRs.last();
                        int MidNum=MidRs.getRow();
                        int mid_index_min = Long.valueOf(System.currentTimeMillis() % (MidNum - 2)).intValue();
                        MidRs.first();
                        i=0;
                        while(MidRs.next()){
                            i++;
                            if(i>=mid_index_min&&i<=mid_index_min+2){
                                String name=MidRs.getString("NAME");
                                int typecode=MidRs.getInt("TYPECODE");
                                int level=MidRs.getInt("LEVEL");
                                JSONObject innerJson=new JSONObject();
                                innerJson.put("name",name);
                                innerJson.put("typecode",typecode);
                                innerJson.put("level",level);
                                Mid.add(innerJson);
                            }
                        }

                        //查找2道高难度题目
                        String HardNumSelectSQL = "SELECT * FROM QUESTION WHERE LEVEL=3;";
                        ResultSet HardRs = databaseHelper.ExecuteQuery(HardNumSelectSQL);
                        HardRs.last();
                        int HardNum=HardRs.getRow();
                        int hard_index_min = Long.valueOf(System.currentTimeMillis() % (HardNum - 1)).intValue();
                        HardRs.first();
                        i=0;
                        while(HardRs.next()){
                            i++;
                            if(i>=hard_index_min&&i<=hard_index_min+1){
                                String name=HardRs.getString("NAME");
                                int typecode=HardRs.getInt("TYPECODE");
                                int level=HardRs.getInt("LEVEL");
                                JSONObject innerJson=new JSONObject();
                                innerJson.put("name",name);
                                innerJson.put("typecode",typecode);
                                innerJson.put("level",level);
                                Hard.add(innerJson);
                            }
                        }

                        Questions.put("hard",Hard);
                        Questions.put("middle",Mid);
                        Questions.put("easy",Easy);
                        printWriter.print(Questions);
        }else
            throw new ServletException();
        System.gc();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
