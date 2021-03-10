import Entity.Click;
import Entity.Family;
import Entity.Record;
import Entity.TmpFamiles;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;

@SuppressWarnings("ALL")
public abstract class Element {

    //这个类保存数据库上抓拍的垃圾分类信息。
    private static String CreateTime;
    private static SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static Boolean Started_To_Auto_Update=false;
    private static Boolean Accessable=true;
    //切勿改变镜像信息数组中内容！！！
    public static ArrayList<Record> Records;
    //保存点击次数
    public static ArrayList<Click> CLICKS;
    //保存衍生物序列
    public static ArrayList<Family> Families;


    public static void Update() throws SQLException {
        DatabaseHelper databaseHelper=new DatabaseHelper();
        databaseHelper.init();
        databaseHelper.ChooseDatabase("ELEMENT");
        ResultSet Data=null;
        Data=databaseHelper.ExecuteQuery("SELECT ID,NAME,TYPECODE,INFORMATION,CLICK,PINYIN FROM ELEMENT ORDER BY ID ASC ;");
        //清空JSON数组里面的数据
        Element.Records.clear();
        Element.CLICKS.clear();
        Element.Families.clear();
        while (Data.next()){
            //保存拉取下来的数据库记录
            Record record=new Record(Data.getString("NAME"),Data.getInt("TYPECODE"),
            Data.getString("INFORMATION"),Data.getInt("ID"),Data.getInt("CLICK"),
                    Data.getString("PINYIN"));
            //保存点击数信息
            Click click=new Click(Data.getInt("ID"),Data.getInt("CLICK"));
            //装载入ArrayList中
            Element.Records.add(record);
            Element.CLICKS.add(click);
        }
        Element.CreateTime=Element.simpleDateFormat.format(System.currentTimeMillis());
        databaseHelper.exit();
        System.gc();
    }

    public static void Check(){
        if(Element.Started_To_Auto_Update){
            return;
        }
        Element.Records=new ArrayList<>();
        Element.CLICKS=new ArrayList<>();
        Element.Families=new ArrayList<>();
        new Thread(()->{
            try {
                while (true) {
                    if(!Element.Accessable){
                        continue;
                    }
                    long StartTime=System.currentTimeMillis();
                    Element.Accessable=false;
                    try {
                        //更新点击次数
                        Click_Update();
                        Update();
                        //建立衍生物序列
                        SearchForFamilies();
                        //建立读音序列
                        SearchForSamePinYin();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    System.out.println("缓存更新完成,耗时："+(System.currentTimeMillis()-StartTime)+"毫秒.");
                    Element.Accessable=true;
                    Thread.sleep(10*60*1000);
                }
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        ).start();
        Element.Started_To_Auto_Update=true;
    }

    private static void SearchForFamilies() {
        //根据字符串匹配将具有相同前缀的若干个记录统合在一起
        //先建立一组临时序列列表
        ArrayList<TmpFamiles> Tmp = new ArrayList<>();
        for (Record record : Element.Records) {
            if (record.getName().length() >= 3) {
                Create_Or_Join_A_Family(record.getName().substring(0, 3), record.getId() + "", Tmp);
                Create_Or_Join_A_Family(record.getName().substring(0, 2), record.getId() + "", Tmp);
                Create_Or_Join_A_Family(record.getName().substring(0, 1), record.getId() + "", Tmp);
            } else {
                switch (record.getName().length()) {
                    case 1: {
                        Create_Or_Join_A_Family(record.getName(), record.getId() + "", Tmp);
                        break;
                    }
                    case 2: {
                        Create_Or_Join_A_Family(record.getName(), record.getId() + "", Tmp);
                        Create_Or_Join_A_Family(record.getName().substring(0, 1), record.getId() + "", Tmp);
                        break;
                    }
                }
            }
        }
            //清理不足二人的临时序列
            Iterator<TmpFamiles> iterator1 = Tmp.iterator();
            while (iterator1.hasNext()) {
                if (((TmpFamiles) iterator1.next()).Population < 2) {
                    iterator1.remove();
                }
            }
        //现在剩下的临时序列可被保存
        for (TmpFamiles tmpFamiles : Tmp) {
            ArrayList<Record> Family_Record = new ArrayList<>();
            for (String ID : tmpFamiles.FamilyMembersID) {
                Family_Record.add(Records.get(Integer.parseInt(ID) - 1000000));
            }
            if (Families == null) {
                Families = new ArrayList<>();
            }
            Families.add(new Family(tmpFamiles.Name, Family_Record));
        }
    }
    //弃用方法
    private static void SearchForSamePinYin(){}

    private static void Create_Or_Join_A_Family(String name,String ID,ArrayList<TmpFamiles> Tmp){
        for(TmpFamiles tmpFamiles:Tmp){
            if(tmpFamiles.Name.equals(name)){
                tmpFamiles.Family_Increase(ID);
                return;
            }
        }
        Tmp.add(new TmpFamiles(name,ID));
    }

    //进行CLICK字段的更新
    private static void Click_Update(){
        if(Element.CLICKS==null||Element.Records==null){
        return;
        }
        DatabaseHelper databaseHelper=new DatabaseHelper();
        databaseHelper.init();
        databaseHelper.ChooseDatabase("ELEMENT");
        //更新点击数据
        for(Click click:CLICKS){
            if(click.getClick()!=Records.get(click.getId()-1000000).getClick()){
                databaseHelper.ExecuteSQL("UPDATE ELEMENT SET CLICK="+click.getClick()+" WHERE ID="+click.getId()+" ;");
            }
        }
        databaseHelper.exit();
        System.gc();
    }

    public static int Click_Query(int ID){
        return CLICKS.get(ID-1000000).getClick();
    }

    public static void Click_ADD(int ID){
        CLICKS.get(ID-1000000).ClickIncrease();
    }

    public static Boolean Can_Read(){
        return Element.Accessable;
    }

    public static Boolean Started(){
        return Element.Started_To_Auto_Update;
    }

    synchronized public static ArrayList<Record> Read(){
        return Element.Records;
    }

}
