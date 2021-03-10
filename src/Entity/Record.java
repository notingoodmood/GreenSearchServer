package Entity;

import com.alibaba.fastjson.JSONObject;

public class Record{

    //这个实体类保存Element表上的信息.
    private String name;
    private int typecode;
    private String information;
    private int id;
    private int click;
    private String Pinyin;

    public Record(String name,int typecode,String information,int id,int click,String Pinyin){
        this.name=name;
        this.typecode=typecode;
        this.information=information;
        this.id=id;
        this.click=click;
        this.Pinyin=Pinyin;
    }

    public int getClick() {
        return click;
    }

    public int getId() {
        return id;
    }

    public int getTypecode() {
        return typecode;
    }

    public String getInformation() {
        return information;
    }

    public String getName() {
        return name;
    }

    public void setClick(int click) {
        this.click = click;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTypecode(int typecode) {
        this.typecode = typecode;
    }

    public String getPinyin() {
        return Pinyin;
    }

    public void setPinyin(String pinyin) {
        Pinyin = pinyin;
    }

    @Override
    public String toString() {
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("name",name);
        jsonObject.put("type",typecode);
        jsonObject.put("extra",information);
        jsonObject.put("id",id);
        return jsonObject.toJSONString();
    }

}
