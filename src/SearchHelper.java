import Entity.Family;
import Entity.Record;
import Entity.SearchResult;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.sourceforge.pinyin4j.PinyinHelper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

@SuppressWarnings("ALL")
public class SearchHelper {

    //搜索记录缓存区
    private static ArrayList<SearchResult> Results;
    //限定缓存区的大小
    private static final int LENGTH_LIMIT = 300;
    //限定每次清理时清理的数量
    private static final int CLEAN_STEPS = 50;
    //限定结果集中记录的最大数量
    private static final int MAX_NUMBER=20;
    //定义每隔多少分钟，进行搜索缓存的更新任务.
    private static final int UPDATE_TIME=60;
    //定义一个搜索缓存的生存分钟
    private static final int LASTING_TIME=60*24;


    //搜索数据集清理算法
    private static void CleanUp(int num,ArrayList<SearchResult> Object){
        /*
        完成一个算法，实现在输入数据集Object与清理数量num后，可根据某种规则确定应当清理的结果集是哪些,并将其删去，规则的量度：
        1.搜索过程耗时越久，越不应当清理；
        2.搜索命中次数越多，越不应当清理.
         */
        Collections.sort(Object,new Comparator<SearchResult>() {
            public int compare(SearchResult sr1,SearchResult sr2) {
                if(sr1.getShot() > sr2.getShot()) {
                    return -1;
                }
                else if(sr1.getShot() < sr2.getShot()) {
                    return 1;
                }
                else {
                    if(sr1.getLastingTime() >= sr2.getLastingTime()) {
                        return -1;
                    }
                    else {
                        return 1;
                    }
                }
            }
        });
        //清理排在最后的num个元素
        for(int i=1;i<=num;i++) {
            Object.remove(Object.size()-i);
        }
    }

    //总搜索算法
    synchronized public static JSONObject Search(String word,int limit) {
        JSONObject ShotResult = In_The_Buffer(word);
        if (ShotResult != null) {
            //命中了搜索结果
            return ShotResult;
        } else {
            //没有命中缓存
            if (GetSearchResult().size() >= LENGTH_LIMIT) {
                //缓存区结果过多,清理一些
                CleanUp(CLEAN_STEPS, GetSearchResult());
            }
            //记录搜索开始时间
            long START_TIME = System.currentTimeMillis();
            //最终搜索结果容器
            JSONObject AllResult = new JSONObject();
            //保存精确搜索结果
            AllResult.put("jingque", Search_E(word));
            int Type = IsQuanPin(word);
            if (Type == 2) {
                //可能本来就是一个全拼
                AllResult.put("quanpin", Search_M(word.toUpperCase(), 1,limit));
            } else if (Type == 1) {
                //这可能是一个单词，但可能也是一个全拼，尝试建立记录
                AllResult.put("quanpin", Search_M(word.toUpperCase(), 1,limit));
            } else if (Type == 0) {
                //这个结果不是全拼、单词，将其拼音输入作为模糊搜索
                AllResult.put("quanpin", Search_M(word, 0,limit));
            }
            //保存联想搜索结果
            AllResult.put("lianxiang",Search_R(word,limit));
            //最后查找衍生物序列
            if(Type==0) {
                AllResult.put("familyname",FamilyName(word));
                AllResult.put("yanshengwu", Search_C(word,limit));
            }else{
                AllResult.put("familyname","");
                AllResult.put("yanshengwu",new JSONArray());
            }
            //建立搜索结果序列
            SearchHelper.GetSearchResult().add(new SearchResult(word,AllResult,System.currentTimeMillis()-START_TIME,System.currentTimeMillis()));
            return RemoveRepetition(AllResult);
        }
    }

    //去重算法
    public static JSONObject RemoveRepetition(JSONObject AllResult){
        JSONObject result = new JSONObject();
        String familyname = AllResult.getString("familyname");
        JSONArray jingque = AllResult.getJSONArray("jingque");
        JSONArray lianxiang = AllResult.getJSONArray("lianxiang");
        JSONArray yanshengwu = AllResult.getJSONArray("yanshengwu");
        JSONArray quanpin = AllResult.getJSONArray("quanpin");
        JSONArray new_lianxiang = new JSONArray();
        JSONArray new_yanshengwu = new JSONArray();
        JSONArray new_quanpin = new JSONArray();
        ArrayList<String> all = new ArrayList<String>();
        all.add("0");
        String temp;
        for(int i=0;i<jingque.size();i++) {
            all.add(jingque.getJSONObject(i).getString("id"));
        }
        for(int i=0;i<lianxiang.size();i++) {
            temp = lianxiang.getJSONObject(i).getString("id");
            for (int j = 0; j < all.size(); j++) {
                if(temp.equals(all.get(j))){
                    System.out.println(lianxiang.getJSONObject(i).getString("name"));
                    break;
                }
                else if(j == all.size()-1){
                    all.add(temp);
                    new_lianxiang.add(lianxiang.getJSONObject(i));
                }
            }
        }
        for(int i=0;i<yanshengwu.size();i++) {
            temp = yanshengwu.getJSONObject(i).getString("id");
            for (int j = 0; j < all.size(); j++) {
                if(temp.equals(all.get(j))){
                    System.out.println(yanshengwu.getJSONObject(i).getString("name"));
                    break;
                }
                else if(j == all.size()-1){
                    all.add(temp);
                    new_yanshengwu.add(yanshengwu.getJSONObject(i));
                }
            }
        }
        for(int i=0;i<quanpin.size();i++) {
            temp = quanpin.getJSONObject(i).getString("id");
            for (int j = 0; j < all.size(); j++) {
                if(temp.equals(all.get(j))){
                    System.out.println(quanpin.getJSONObject(i).getString("name"));
                    break;
                }
                else if(j == all.size()-1){
                    all.add(temp);
                    new_quanpin.add(quanpin.getJSONObject(i));
                }
            }
        }
        result.put("jingque",jingque);
        System.out.println(jingque.toString());
        result.put("lianxiang",new_lianxiang);
        System.out.println(new_lianxiang.toString());
        result.put("yanshengwu",new_yanshengwu);
        System.out.println(new_yanshengwu.toString());
        result.put("quanpin",new_quanpin);
        System.out.println(new_quanpin.toString());
        result.put("familyname",familyname);
        return result;
    }

    //检查一个输入是否比较可能为全拼或单词
    //0：不是任意一方；1：可能是一个单词；2：可能是一个全拼.
    public static int IsQuanPin(String input) {
        for (int i = 0; i < input.length(); i++) {
            if (!(input.charAt(i) > 'A' && input.charAt(i) < 'Z' || input.charAt(i) > 'a' && input.charAt(i) < 'z')) {
                return 0;
            }
        }
        if(input.length()>5){
            return 1;
        }
        return 2;
    }

    //自动清理缓存区算法
    public static void Start_To_Auto_Clean_Up(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                SearchHelper.Results=new ArrayList<>();
                while (true) {
                    try {
                        Thread.sleep(5 * 60 * 1000);
                        if(GetSearchResult()==null){
                            continue;
                        }
                        if (GetSearchResult().size() >= LENGTH_LIMIT) {
                            //缓存区结果过多,清理一些
                            CleanUp(CLEAN_STEPS, GetSearchResult());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }


    //输入中文词语，获得大写全拼首字母（只要前五位）
    public static String GetQuanPin(String word){
        String result=new String();
        for(int i=0;i<word.length();i++){
            if(i>5){
                break;
            }
            if(!(String.valueOf(word.charAt(i)).matches("[\u4e00-\u9fa5]"))){
                continue;
            }else{
                result+=PinyinHelper.toHanyuPinyinStringArray(word.charAt(i))[0].charAt(0);
            }
        }
        return result.toUpperCase();
    }

    //输入一个中文字符，返回一个字符串，全为其读音的大写首字母
    private static String GetFirstPinYin(char c){
        String string=new String();
        String value=new String()+c;
        if(!value.valueOf(0).matches("[\u4e00-\u9fa5]")){
            return string;
        }
        for(int i=0;i<PinyinHelper.toHanyuPinyinStringArray(c).length;i++){
            string+=PinyinHelper.toHanyuPinyinStringArray(c)[i].toUpperCase();
        }
        return string;
    }

    //精确搜索算法（香蕉皮->香蕉皮）
    public static JSONArray Search_E( String word){
        if(!Element.Can_Read()){
            //缓存正在生成，此时无法进行操作。
            return new JSONArray();
        }
        JSONArray Result=new JSONArray();
        for(Record record: Element.Records){
            if(record.getName().equals(word)){
                Result.add(JSONObject.parseObject(record.toString()));
                Element.CLICKS.get(record.getId()-1000000).ClickIncrease();
                break;
            }
        }
        return Result;
    }

    //全拼搜索算法（香蕉皮->橡胶皮）
    public static JSONArray Search_M(String word,int Mode,int limit) {
        if (!Element.Can_Read()) {
            //缓存正在生成，此时无法进行操作。
            return new JSONArray();
        }
        JSONArray Array=new JSONArray();
        String PinYin=new String();
        if(Mode==0) {
            PinYin = GetQuanPin(word);
        }else if(Mode==1){
            PinYin=word;
        }else{
            PinYin="";
        }
        int length=0;
        for(Record record:Element.Records){
            int count=0;
            int j=0;
            for(int i=0;i<PinYin.length();i++){
                for(;j<record.getPinyin().length();j++){
                    if(PinYin.charAt(i)==record.getPinyin().toUpperCase().charAt(j)){
                        count++;
                        break;
                    }
                }
            }
            j=0;
            if(count!=0&&count>=(PinYin.length())&&length<limit){
                Array.add(JSONObject.parseObject(record.toString()));
                length++;
            }
        }
        return Array;
    }

    //衍生物系列匹配算法
    public static JSONArray Search_C(String word,int limit){
        if(!Element.Can_Read()){
            //缓存正在生成，此时无法进行操作。
            return new JSONArray();
        }
        //检查是否有衍生物序列表符合条件
        Family family=GetFamily(word,1);
        if(family!=null){
            return family.getOrderedResult(limit);
        }
        //如果找不到，就返回一个空序列。
        return new JSONArray();
    }

    public static String FamilyName(String word){
        if(!Element.Can_Read()){
            //缓存正在生成，此时无法进行操作。
            return "";
        }
        //检查是否有衍生物序列表符合条件
        Family family=GetFamily(word,1);
        if(family!=null){
            return family.ClassName;
        }
        //如果找不到，就返回一个空序列。
        return "";
    }

    //输入联想算法
    public static JSONArray Search_R(String word,int limit){
        if(!Element.Can_Read()){
            //缓存正在生成，此时无法进行操作。
            return new JSONArray();
        }
        JSONArray Result=new JSONArray();
        int length=word.length();
        int num=0;
        //进行字符串比对操作，检查哪些条目的字串与输入相同
        for(Record record:Element.Read()){
            if(num>limit){
                break;
            }
            if(!(record.getName().length()>length)){
                continue;
            }else{
                int times=record.getName().length()-length;
                for(int i=0;i<times;i++){
                    if(record.getName().substring(i,i+length).equals(word)){
                        Result.add(JSONObject.parseObject(record.toString()));
                        num++;
                        break;
                    }
                }
            }
        }
        return Result;
    }
    //检查搜索词是否命中缓存结果.如果命中，返回这个结果，否则返回NULL
    private  static JSONObject In_The_Buffer(String KeyWord){
        for(SearchResult searchResult : GetSearchResult()){
            if(searchResult.getKeyWord().equals(KeyWord)){
                searchResult.ShotIncrease();
                return searchResult.getJSONResult();
            }
        }
        return null;
    }

    //开启一个线程，为查找到的所有结果增加被查找次数
    public static void All_Results_Click_Increase(){
        //留存
    }

    //线程同步访问接口
    synchronized public static ArrayList<SearchResult> GetSearchResult(){
        return SearchHelper.Results;
    }

    //不能让一个搜索结果过长时间被缓存。在一个固定节点，应当使得这些结果被清理,并重新建立起这些搜索缓存
    public static void Buffers_Clean_Up(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try{
                        if(GetSearchResult()==null){
                            continue;
                        }
                        Thread.sleep(UPDATE_TIME*60*1000);
                        SearchHelper.GetSearchResult().trimToSize();
                        for(int i=0;i<SearchHelper.GetSearchResult().size();i++){
                            SearchResult searchResult=SearchHelper.GetSearchResult().get(i);
                            if(System.currentTimeMillis()-searchResult.getCreateTime()>(LASTING_TIME*60*1000)){
                                //该搜索结果已经过了生存期，进行清理
                                SearchHelper.GetSearchResult().remove(i);
                            }
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    //根据输入的信息，匹配一个精确符合的序列，没有则返回null
    //弃用
    private static Family GetFamily(String word,int Mode){
        int length=word.length();
        if(Mode==0){
            if(Element.Families==null){
                return null;
            }
            //对获得的序列的全拼列表进行匹配，若精确匹配则成功
            for(Family family:Element.Families){
                if((word.length())>=3&&family.ClassName.length()>=3){
                    for(int i=0;i<3;i++){
                        String PinYins=GetFirstPinYin(family.ClassName.charAt(i));
                        for(int j=0;j<PinYins.length();j++){
                            if(PinYins.charAt(j)==word.charAt(i)){
                                return family;
                            }
                        }
                    }
                }else if(word.length()==2&&family.ClassName.length()>=2){
                    for(int i=0;i<2;i++){
                        String PinYins=GetFirstPinYin(family.ClassName.charAt(i));
                        for(int j=0;j<PinYins.length();j++){
                            if(PinYins.charAt(j)==word.charAt(i)){
                                return family;
                            }
                        }
                    }
                }else if(word.length()==1&&family.ClassName.length()>=1){
                    String PinYins=GetFirstPinYin(family.ClassName.charAt(0));
                    for(int j=0;j<PinYins.length();j++){
                        if(PinYins.charAt(j)==word.charAt(1)){
                            return family;
                        }
                    }
                }
            }
        }else if(Mode==1){
            if(Element.Families==null){
            return null;
            }
            //查找的是衍生物表
            if(length>=3){
                for(Family family:Element.Families){
                    if(family.ClassName.equals(word.substring(0,2))){
                        return family;
                    }
                }
            }else if(length==2){
                for(Family family:Element.Families) {
                    if (family.ClassName.equals(word.substring(0, 1))) {
                        return family;
                    }
                }
            }else if(length==1){
                for(Family family:Element.Families) {
                    if (family.ClassName.equals(word)) {
                        return family;
                    }
                }
            }else {
                return null;
            }
        }else {
            return null;
        }
        return null;
    }


}
//没有这个组别：1.记录少于5（直接加记录）2.记录已经为5组（找出最小的值，若为1进行替换）
//有这个组别：进行组的值加一操作