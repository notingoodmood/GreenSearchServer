import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;

public class SMSHelper {

    private static final int appid=1400230360;
    private static final String appkey="17c83be92bb770d6b24a3ba120130e38";
    private static final int templateID=373051;
    private static final String smsSign="shelezhihhh网";

    public static boolean Send_Login_SMS(String PhoneNumber,String LoginCode,int time){
        try {
            String[] params = {LoginCode,time+""};
            SmsSingleSender smsSingleSender = new SmsSingleSender(appid, appkey);
            SmsSingleSenderResult smsSingleSenderResult=
                    smsSingleSender.sendWithParam("86",PhoneNumber,templateID,params,smsSign,"","");
            return smsSingleSenderResult.result==0;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }
}