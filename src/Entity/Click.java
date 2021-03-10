package Entity;

public class Click {
    //这个实体类保存点击次数信息.
    private int id;
    private int click;
    private long CreateTime;

    public Click(int id,int click){
        this.click=click;
        this.id=id;
        this.CreateTime=System.currentTimeMillis();
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setClick(int click) {
        this.click = click;
    }

    public int getId() {
        return id;
    }

    public int getClick() {
        return click;
    }

    //增加点击次数。
    synchronized public void ClickIncrease(){
        this.click++;
    }
}
