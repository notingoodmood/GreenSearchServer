package Entity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

@SuppressWarnings("ALL")
public class Family {

    //这个实体类保存整理的同特征系列。

    public  String ClassName;
    public  ArrayList<Record> FamilyMembers;

    public Family(String ClassName,ArrayList<Record> FamilyMembers){
        this.ClassName=ClassName;
        this.FamilyMembers=FamilyMembers;
    }

    //按顺序选取一些有效信息打印。
    private JSONArray getFamilyMembers(int limit) {
        JSONArray result=new JSONArray();
        for(Record record :this.FamilyMembers){
            limit--;
            result.add(JSONObject.parseObject(record.toString()));
            if(limit==0){
                break;
            }
        }
        return result;
    }

    //对字符串进行排列,使得其降序排列输出结果
    synchronized public JSONArray getOrderedResult(int limit){
        Collections.sort(this.FamilyMembers, new Comparator<Record>() {
                    @Override
                    public int compare(Record o1, Record o2) {
                         if(o1.getName().length()>o2.getName().length()){
                             return 1;
                         }else if(o1.getName().length()==o2.getName().length()){
                             return 0;
                         }else{
                             return -1;
                         }
                    }
                });
        return getFamilyMembers(limit);
    }




    public String getClassName() {
        return this.ClassName;
    }

    public void setClassName(String className) {
        this.ClassName = className;
    }

    public void setFamilyMembers(ArrayList<Record> familyMembers) {
        FamilyMembers = familyMembers;
    }
}
