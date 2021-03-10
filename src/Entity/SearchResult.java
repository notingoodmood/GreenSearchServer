package Entity;

import com.alibaba.fastjson.JSONObject;


public class SearchResult {
    //这个实体类缓存有效的搜索记录。
    private String KeyWord;
    private JSONObject JSONResult;
    //从搜索任务建立起，至搜索结果建立完成的时间开销
    private long LastingTime;
    //从建立搜索结果开始，成功命中搜索结果次数
    private int Shot;
    private long CreateTime;


    public SearchResult(String KeyWord,JSONObject JSONResult,long LastingTime,long CreateTime){
        this.JSONResult=JSONResult;
        this.KeyWord=KeyWord;
        this.LastingTime=LastingTime;
        this.Shot=0;
        this.CreateTime=CreateTime;
    }

    public JSONObject getJSONResult() {
        return JSONResult;
    }

    public String getKeyWord() {
        return KeyWord;
    }

    public void setJSONResult(JSONObject JSONResult) {
        this.JSONResult = JSONResult;
    }

    public void setKeyWord(String keyWord) {
        KeyWord = keyWord;
    }

    public long getLastingTime() {
        return LastingTime;
    }

    public void setLastingTime(long lastingTime) {
        LastingTime = lastingTime;
    }

    public int getShot() {
        return Shot;
    }

    public void setShot(int shot) {
        Shot = shot;
    }
    synchronized public void ShotIncrease(){
        this.Shot++;
    }

    public long getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(long createTime) {
        CreateTime = createTime;
    }

    @Override
    public String toString(){
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("shot",this.Shot);
        jsonObject.put("result",this.JSONResult);
        jsonObject.put("lastingtime",this.LastingTime);
        return jsonObject.toJSONString();
    }
}
