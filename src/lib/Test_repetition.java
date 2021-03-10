package lib;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;

public class Test_repetition {
    public static void main(String[] args){
        String js = "{\"quanpin\":[{\"name\":\"奶酪\",\"id\":1000405,\"type\":1},{\"name\":\"黄泥螺\",\"id\":1000886,\"type\":1},{\"name\":\"奶酪火锅锅底\",\"id\":1001038,\"type\":1},{\"name\":\"奶茶里的爆珠\",\"id\":1001063,\"type\":1},{\"name\":\"奶茶里面的红豆\",\"id\":1001065,\"type\":1},{\"name\":\"甲仙芋头奶茶里的芋头\",\"id\":1001070,\"type\":1},{\"name\":\"黑糖粉圆鲜奶里的粉圆\",\"id\":1001071,\"type\":1},{\"name\":\"奶茶里的布丁\",\"id\":1001075,\"type\":1},{\"name\":\"柠檬养乐多里的柠檬\",\"id\":1001077,\"type\":1},{\"name\":\"珍珠奶茶里的珍珠\",\"id\":1001111,\"type\":1},{\"name\":\"珍珠奶茶里的仙草\",\"id\":1001112,\"type\":1},{\"name\":\"奶酪蛋糕\",\"id\":1001574,\"type\":1},{\"name\":\"奶茶塑料杯\",\"id\":1001749,\"type\":2},{\"name\":\"牛肋骨\",\"id\":1001839,\"type\":2},{\"name\":\"奶茶塑料盖\",\"id\":1001989,\"type\":2},{\"name\":\"尼龙制品\",\"id\":1002006,\"type\":2},{\"name\":\"冰淇淋纸杯\",\"id\":1002592,\"type\":2},{\"name\":\"速冻饺子内塑料盒\",\"id\":1003116,\"type\":2},{\"name\":\"牛肉粒包装袋\",\"id\":1003391,\"type\":2},{\"name\":\"咖啡纸杯(内表面附塑料膜)\",\"id\":1003424,\"type\":2},{\"name\":\"尼龙袋\",\"id\":1003953,\"type\":3},{\"name\":\"酸奶玻璃瓶\",\"id\":1004192,\"type\":3},{\"name\":\"美年达饮料瓶\",\"id\":1004258,\"type\":3},{\"name\":\"Kindle\",\"id\":1004278,\"type\":3},{\"name\":\"闹铃\",\"id\":1004287,\"type\":3},{\"name\":\"奶粉罐铝盖\",\"id\":1004326,\"type\":3},{\"name\":\"牛奶塑料瓶\",\"id\":1004364,\"type\":3},{\"name\":\"牛奶利乐包装盒\",\"id\":1004484,\"type\":3},{\"name\":\"鸟笼\",\"id\":1004586,\"type\":3},{\"name\":\"尼龙绳\",\"id\":1004700,\"type\":3},{\"name\":\"美年达塑料瓶\",\"id\":1004858,\"type\":3},{\"name\":\"酸奶塑料瓶\",\"id\":1004924,\"type\":3},{\"name\":\"牛奶利乐包\",\"id\":1005097,\"type\":3},{\"name\":\"家用电器内的电路板\",\"id\":1005143,\"type\":3},{\"name\":\"牛奶玻璃瓶\",\"id\":1005188,\"type\":3},{\"name\":\"拟除虫菊酯类杀虫剂\",\"id\":1005781,\"type\":4},{\"name\":\"含氰污泥及冷却液\",\"id\":1005831,\"type\":4},{\"name\":\"眩晕宁颗粒\",\"id\":1005939,\"type\":4}],\"lianxiang\":[{\"name\":\"奶酪火锅锅底\",\"id\":1001038,\"type\":1},{\"name\":\"奶酪蛋糕\",\"id\":1001574,\"type\":1}],\"familyname\":\"奶\",\"jingque\":[{\"name\":\"奶酪\",\"id\":1000405,\"type\":1}],\"yanshengwu\":[{\"name\":\"奶粉\",\"id\":1000181,\"type\":1},{\"name\":\"奶油\",\"id\":1000200,\"type\":1},{\"name\":\"奶酪\",\"id\":1000405,\"type\":1},{\"name\":\"奶油\",\"id\":1000698,\"type\":1},{\"name\":\"奶糕\",\"id\":1000838,\"type\":1},{\"name\":\"奶片\",\"id\":1001490,\"type\":1},{\"name\":\"奶嘴\",\"id\":1001935,\"type\":2},{\"name\":\"奶盒\",\"id\":1005068,\"type\":3},{\"name\":\"奶黄包\",\"id\":1000855,\"type\":1},{\"name\":\"奶茶杯\",\"id\":1002080,\"type\":2},{\"name\":\"奶粉盖\",\"id\":1002316,\"type\":2},{\"name\":\"奶茶盖\",\"id\":1002479,\"type\":2},{\"name\":\"奶粉袋\",\"id\":1002790,\"type\":2},{\"name\":\"奶粉罐\",\"id\":1003754,\"type\":3},{\"name\":\"奶粉桶\",\"id\":1004183,\"type\":3},{\"name\":\"奶油蛋糕\",\"id\":1001223,\"type\":1},{\"name\":\"奶酪蛋糕\",\"id\":1001574,\"type\":1},{\"name\":\"奶茶纸杯\",\"id\":1001688,\"type\":2},{\"name\":\"奶粉勺子\",\"id\":1002312,\"type\":2},{\"name\":\"奶粉盒子\",\"id\":1005316,\"type\":3},{\"name\":\"奶茶塑料杯\",\"id\":1001749,\"type\":2},{\"name\":\"奶茶塑料盖\",\"id\":1001989,\"type\":2},{\"name\":\"奶粉罐铝盖\",\"id\":1004326,\"type\":3},{\"name\":\"奶茶中的珍珠\",\"id\":1000165,\"type\":1},{\"name\":\"奶酪火锅锅底\",\"id\":1001038,\"type\":1},{\"name\":\"奶茶里的爆珠\",\"id\":1001063,\"type\":1},{\"name\":\"奶茶里的布丁\",\"id\":1001075,\"type\":1},{\"name\":\"奶茶里面的红豆\",\"id\":1001065,\"type\":1}]}";
        JSONObject res = new JSONObject();
        res = JSONObject.parseObject(js);
        long startTime = System.currentTimeMillis();
        System.out.println(RemoveRepetition(res).toString());
        long endTime = System.currentTimeMillis();    //获取结束时间

        System.out.println("程序运行时间：" + (endTime - startTime) + "ms");    //输出程序运行时间
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
        System.out.println(result.toString());
        return result;
    }
}
