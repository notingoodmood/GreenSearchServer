import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class SerletInitializeListener implements ServletContextListener {

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void contextInitialized(ServletContextEvent arg0) {
        //开启自动同步数据库缓存
        Element.Check();
        //开启自动清理搜索记录缓存区
        SearchHelper.Start_To_Auto_Clean_Up();
        //开启自动清理过时搜索缓存
        SearchHelper.Buffers_Clean_Up();
        System.out.println("开始初始化本地缓存\n");
    }








}
