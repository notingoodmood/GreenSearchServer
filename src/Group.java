import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Group {
    public static void main(String[] args)throws SQLException {
        DatabaseHelper databaseHelper=new DatabaseHelper();
        databaseHelper.init();
        databaseHelper.ChooseDatabase("ELEMENT");

        String SQL1="SELECT * FROM ELEMENT WHERE SHOPGROUP=0 AND ID<1003294";

        ResultSet resultSet=databaseHelper.ExecuteQuery(SQL1);
        DatabaseHelper databaseHelper2=new DatabaseHelper();
        databaseHelper2.init();
        databaseHelper2.ChooseDatabase("ELEMENT");

        while(resultSet.next()){
            int id=resultSet.getInt("ID");
            String name=resultSet.getString("name");
            System.out.print(name+"  :  ");
            Scanner input=new Scanner(System.in);
            int GroupId=input.nextInt();
            String SQL2="UPDATE ELEMENT SET SHOPGROUP="+GroupId+" WHERE ID="+id+";";
            databaseHelper2.ExecuteSQL(SQL2);
        }

    }
}
