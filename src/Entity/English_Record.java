package Entity;

import com.alibaba.fastjson.JSONObject;

public class English_Record {
    private int ID;
    private String NAME;
    private int TYPECODE;
    private int CLICK;
    public English_Record(int ID,String NAME,int TYPECODE, int CLICK){
        this.ID = ID;
        this.NAME=NAME;
        this.TYPECODE=TYPECODE;
        this.CLICK=CLICK;
    }

    public int getID(){return this.ID;}
    public String getNAME(){return this.NAME;}
    public int getTYPECODE(){return this.TYPECODE;}
    public int getCLICK(){return this.CLICK;}

    public void setId(int id) {
        this.ID = id;
    }
    public void setClick(int click) {
        this.CLICK = click;
    }
    public void setTypecode(int typecode) {
        this.TYPECODE = typecode;
    }
    public void setName(String name) {
        this.NAME = name;
    }

    @Override
    public String toString(){
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("name",NAME);
        jsonObject.put("type",TYPECODE);
        jsonObject.put("id",ID);
        return jsonObject.toJSONString();
    }

}
