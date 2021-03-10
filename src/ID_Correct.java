import java.sql.ResultSet;
import java.sql.SQLException;

public class ID_Correct {
public static void main(String[] args) throws SQLException {
    DatabaseHelper databaseHelper=new DatabaseHelper();
    databaseHelper.init();
    databaseHelper.ChooseDatabase("ELEMENT");
    DatabaseHelper databaseHelper1=new DatabaseHelper();
    databaseHelper1.init();
    databaseHelper1.ChooseDatabase("ELEMENT");
    ResultSet wrongele=databaseHelper.ExecuteQuery("SELECT ID FROM ELEMENT WHERE ID>1006588;");
    while(wrongele.next()){
        int id=wrongele.getInt("ID");
        int id1=id-313;
        String SQL="UPDATE ELEMENT SET ID="+id1+" WHERE ID="+id;
        databaseHelper1.ExecuteSQL(SQL);
    }
}
}
