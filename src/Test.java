import Entity.Family;
import Entity.Record;
import Entity.SearchResult;
import Entity.TmpFamiles;
import com.alibaba.fastjson.JSONObject;
import net.sourceforge.pinyin4j.PinyinHelper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

@WebServlet("/Test")
@SuppressWarnings("ALL")
public class Test extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("Application/JSON;charset=UTF-8");
        PrintWriter printWriter = response.getWriter();


        for(SearchResult searchResult:SearchHelper.GetSearchResult()){
            printWriter.println(searchResult.toString());
            printWriter.print("\n\n\n\n");
        }
        System.gc();
    }

    public static void main(String[] args){
        int a=50,b=92;
        int i=0;
        for(int x=a,y=b;(x!=y)&&(y-x!=1);a--,b++,x++,y--){
            for(;i<a;i++){
                System.out.print(" ");
            }
            i=0;
            System.out.print("*");
            for(;i<(x-a);i++){
                System.out.print(" ");
            }
            i=0;
            System.out.print("*");
            for(;i<(y-x);i++){
                System.out.print(" ");
            }
            i=0;
            System.out.print("*");
            for(;i<(b-y);i++){
                System.out.print(" ");
            }
            i=0;
            System.out.print("*");
            System.out.print("\n");
        }
        a+=1;
        for(;(a!=b)&&(b-a!=1);a++,b--){
            for(;i<a;i++){
                System.out.print(" ");
            }
            i=0;
            System.out.print("*");
            for(;i<(b-a);i++){
                System.out.print(" ");
            }
            i=0;
            System.out.print("*");
            System.out.print("\n");
        }
    }
}
