import java.sql.*;


public class DatabaseHelper {

    private static final String address="jdbc:mysql://cdb-9z0nzkve.cd.tencentcdb.com:10010?characterEncoding=utf8&useSSL=true";
    private static final String client="Test";
    private static final String code="Test2019!@";

    private Statement statement;
    private Connection connection;


    public void init(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            this.connection=(Connection) DriverManager.getConnection(address,client,code);
            this.statement=this.connection.createStatement();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
            System.out.println("没有找到驱动！！！\n");
        }catch (SQLException e){
            e.printStackTrace();
            System.out.println("没有找到数据库！！！\n");
        }
    }

    public Connection getConnection(){
        return this.connection;
    }

    synchronized public ResultSet ExecuteQuery(String SQL){
        try{
            return this.statement.executeQuery(SQL);
        }catch (SQLException e){
            e.printStackTrace(System.err);
            return null;
        }
    }

    synchronized public  boolean ExecuteSQL(String SQL){
        try{
            return this.statement.execute(SQL);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean ChooseDatabase(String name){
        try {
            this.statement.executeQuery("use " + name+";");
            return true;
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }
    public void exit(){
        try {
            this.statement.close();
            this.connection.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

}
