package Entity;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;

public class TmpFamiles {
    //这个临时实体类记录满足特定前缀的记录数量
    public String Name;
    public ArrayList<String> FamilyMembersID=new ArrayList<>();
    public int Population;
    public TmpFamiles(String Name,String ID){
        this.Name=Name;
        FamilyMembersID.add(ID);
        this.Population=1;
    }
    public void Family_Increase(String ID){
        this.FamilyMembersID.add(ID);
        Population++;
    }

    @Override
    public String toString(){
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("name",this.Name);
        jsonObject.put("population",this.Population);
        jsonObject.put("IDs",this.FamilyMembersID);
        return jsonObject.toJSONString();
    }
}
