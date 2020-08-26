package com.shellever.plugin;

import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import static com.shellever.plugin.Utils.*;


class Main {

    private final Send send;
    private final long groupID;
    private final String msg;
    private final String nick;
    private final String KEY;
    private final long qq;
    private final long atqq;
    private String zr;
    private String FGState;    //发稿状态
    private String FG_List="";  //发稿菜单
    private String FGadmin;     //发稿管理员
    private String fggz="";     //待发稿稿件名字
    private String jiangedate;  //发稿间隔时间
    private String kaiguan;  //发稿间隔时间
    private String randomState;  //随机发稿状态
    private String tsstate;  //随机发稿状态
   //积分用到的变量
    private String qdtime;  //签到时间
    private int jf;      //积分
    private String jfname;   //积分别名
    private int zzk;     //转账卡
    private int zzkjg;     //转账卡价格
    private String yyt;     //语音条计数
    private String yyttime;     //语音条计数
    private String fy;      //发言计数  用于计算今天的龙王
    private String fytime;  //发言时间
    private int qdjl;    //签到奖励
    private int dgjl;    //读稿奖励
    private String da;     //语音条计数
    private String cjl;     //语音条计数

    private static final Integer ONE = 1;
    private static Random rand = new Random();
    /**
     *  @param send          发言用的一个函数
     * @param groupID       消息来源群号
     * @param msg           消息
     * @param nick          昵称
     * @param qq            QQ
     * @param atqq          被at人的QQ
     */
    Main(Send send, long groupID, String msg, String nick, long qq, long atqq,String KEY) {
        this.send = send;
        this.groupID = groupID;
        this.msg = msg;
        this.nick = nick;
        this.qq = qq;
        this.atqq = atqq;
        this.KEY = KEY;
        this.FGadmin = getString(ROOT+"奶御小助手/配置/"+groupID,"FGadmin");
        this.jiangedate = getString(ROOT+"奶御小助手/配置/"+groupID,"jiange");
        this.kaiguan = getString(ROOT+"奶御小助手/配置/"+groupID,"state");
        this.randomState = getString(ROOT+"奶御小助手/配置/"+groupID,"randomState");
        this.zr = getString(sdCardPath() + "/Clousx6/sd1/cookie/QQ","QQ");
        this.tsstate = getString(ROOT+"奶御小助手/配置/"+groupID,"tsstate");
        this.qdtime = getString(ROOT + "奶御小助手/配置/奶御积分助手/签到日期"+qq,"day");
        this.jf = getInt(ROOT + "奶御小助手/配置/奶御积分助手/积分/"+qq,"jf");
        this.jfname = getString(ROOT + "奶御小助手/配置/奶御积分助手/config","jfname");
        this.zzk = getInt(ROOT + "奶御小助手/配置/奶御积分助手/背包/"+qq,"zzk");
        this.yyt = getString(ROOT + "奶御小助手/配置/奶御积分助手/语音条记录/"+qq,"yyt");
        this.yyttime = getString(ROOT + "奶御小助手/配置/奶御积分助手/签到日期"+qq,"yyttime");
        this.fytime = getString(ROOT + "奶御小助手/配置/奶御积分助手/签到日期","fytime");
        this.fy = getString(ROOT + "奶御小助手/配置/奶御积分助手/发言/"+qq,"fy");
        this.qdjl = getInt(ROOT + "奶御小助手/配置/奶御积分助手/奖励","qdjl");
        this.dgjl = getInt(ROOT + "奶御小助手/配置/奶御积分助手/奖励","dgjl");
        this.zzkjg = getInt(ROOT + "奶御小助手/配置/奶御积分助手/奖励","zzkjg");
        this.da= getString(ROOT + "奶御小助手/配置/奶御积分助手/奖励","da");
        this.cjl= getString(ROOT + "奶御小助手/配置/奶御积分助手/奖励","cjl");
    }

    /**
     * 这个函数是判断 发送消息的人在不在 发稿管理列表内
     * @param fgadmin   发稿管理员列表  1977791013,1549425893，1811494105
     * @param qq        发送消息的人
     * @return
     */
    private boolean iffgadmin(String fgadmin,Long qq){

        String[] fgadminList = fgadmin.split(",");
        for(String admin:fgadminList) {
            if (admin.matches(Long.toString(qq))) {
                return true;
            }
        }
        return false;
    }

    boolean ifadmin(String name,String qq){
        String[] admin=name.split(",");
        for(String i:admin){
            if(i.matches(qq)){return true;}
        }
        return false;
    }

/**
 *               processMsg    消息接收器 框架里面勾选的群都能处理
 *              .表示除\n以外的任何字符
 *               *表示0个到无穷个
 *               +表示1个到无穷个
 *               |表示左侧或右侧任意一个（可以与括号配合使用）
 *              [0-9]表示任意一个数字
 *              [0-9A-Za-z]表示任意一个数字或字母
 */
    void processMsg() {
        //判断猜谜答案
        if(msg.equals(da)){
            jf = getInt(ROOT + "奶御小助手/配置/奶御积分助手/积分/"+qq, "jf");
            int jl = jf+getInt(ROOT + "奶御小助手/配置/奶御积分助手/奖励","cjl");
            set(ROOT + "奶御小助手/配置/奶御积分助手/积分/"+qq, "jf", jl );
            send.s("奶御恭喜："+nick+"第一个猜中\\r奖励"+jfname+":"+getInt(ROOT + "奶御小助手/配置/奶御积分助手/奖励","cjl")+"\\r您的"+jfname+"余额："+getInt(ROOT + "奶御小助手/配置/奶御积分助手/积分/"+qq, "jf"));
            set(ROOT + "奶御小助手/配置/奶御积分助手/奖励","da",getRandomInt(456,789));
        }
        //设置猜谜奖励
        if(msg.matches("猜谜奖励 .+") && qq==1977791013){
            set(ROOT + "奶御小助手/配置/奶御积分助手/奖励","cjl",msg.split(" ")[1]);
            send.s("奶御小提示：猜谜奖励"+msg.split(" ")[1]);
        }
        //设置答案
        if(msg.matches("答案 .+") && qq==1977791013){
            set(ROOT + "奶御小助手/配置/奶御积分助手/奖励","da",msg.split(" ")[1]);
            send.s("设置答案为："+msg.split(" ")[1]);
        }
        //设置积分名
        if (msg.matches("积分名 .+")  ) {
            if(iffgadmin(zr,qq)) {

                set(ROOT + "奶御小助手/配置/奶御积分助手/config","jfname",msg.split(" ")[1]);
                send.s("奶御小提示：积分名设置为:"+msg.split(" ")[1]);
            }else{
                send.s("奶御小提示：你不是我主人哟");
            }
        }
        //设置签到奖励
        if (msg.matches("查询")  ) {
            send.s(jfname+":"+jf+"\\r转账卡："+zzk);
        }

        if(msg.matches("抽奖 .+")){
            Integer.parseInt(msg);
            int cjjf = Integer.parseInt(msg.split(" ")[1]);
            jf = getInt(ROOT + "奶御小助手/配置/奶御积分助手/积分/"+qq, "jf");

            if(!(cjjf <=0) && !(cjjf >20)){
                if(jf>=cjjf){
                    set(ROOT + "奶御小助手/配置/奶御积分助手/积分/"+qq, "jf",  jf-cjjf);
                    int zjjf = getRandomInt(-1,cjjf*2);
                    int zzjf = getInt(ROOT + "奶御小助手/配置/奶御积分助手/积分/"+qq, "jf")+zjjf;
                    set(ROOT + "奶御小助手/配置/奶御积分助手/积分/"+qq, "jf",  zzjf);
                    send.s("您用："+cjjf+jfname+"\\r抽中了:"+zjjf+jfname+"\\r您的"+jfname+"余额："+zzjf);
                }else send.s("您"+jfname+"不足"+cjjf);
            }else send.s("抽奖积分不能是负数或者0或者大于20积分");



        }

        //设置签到奖励
        if (msg.matches("签到奖励 .+")  ) {
            if(iffgadmin(zr,qq)) {

                set(ROOT + "奶御小助手/配置/奶御积分助手/奖励","qdjl",msg.split(" ")[1]);
                send.s("奶御小提示：签到奖励"+msg.split(" ")[1]+jfname);
            }else{
                send.s("奶御小提示：你不是我主人哟");
            }
        }

        //设置转账卡价格
        if (msg.matches("转账卡价格 .+")  ) {
            if(iffgadmin(zr,qq)) {
                set(ROOT + "奶御小助手/配置/奶御积分助手/奖励","zzkjg",msg.split(" ")[1]);
                send.s("奶御小提示：转账卡售卖"+msg.split(" ")[1]+jfname+"一张");
            }else{
                send.s("奶御小提示：你不是我主人哟");
            }
        }


        if(msg.matches("加@.+") && qq==1977791013){
            int czjf = Integer.parseInt(msg.split(" ")[1]);
            jf=jf+czjf;
            set(ROOT + "奶御小助手/配置/奶御积分助手/积分/"+atqq, "jf",  jf);
            send.s("奶御小提示：充值成功\\r您的"+jfname+"余额:"+jf);
        }

        if (msg.matches("购买转账卡 .+")){
            int xyjf = Integer.parseInt(msg.split(" ")[1])*zzkjg;
            jf = getInt(ROOT + "奶御小助手/配置/奶御积分助手/积分/"+qq, "jf");
            if(jf>xyjf){
                jf=jf-xyjf;
                zzk=zzk+Integer.parseInt(msg.split(" ")[1]);
                set(ROOT + "奶御小助手/配置/奶御积分助手/背包/"+qq,"zzk",zzk);
                set(ROOT + "奶御小助手/配置/奶御积分助手/积分/"+qq, "jf",jf);
                send.s("花费"+jfname+":"+xyjf+"\\r您的余额："+getInt(ROOT + "奶御小助手/配置/奶御积分助手/积分/"+qq, "jf")+"\\r成功购买转账卡："+msg.split(" ")[1]+"张");
            }else {send.s("购买失败\\r需要"+jfname+":"+xyjf+"\\r您的"+jfname+":"+jf);}
        }

        if(msg.matches("转账@.+")){
            jf = getInt(ROOT + "奶御小助手/配置/奶御积分助手/积分/"+qq, "jf");
            int atjf = getInt(ROOT + "奶御小助手/配置/奶御积分助手/积分/"+atqq, "jf");
            int zzjf = Integer.parseInt(msg.split(" ")[1]);
            if (zzjf>0){
                if(zzk>0){
                    if(jf>zzjf){
                        set(ROOT + "奶御小助手/配置/奶御积分助手/积分/"+atqq, "jf", atjf + zzjf);
                        set(ROOT + "奶御小助手/配置/奶御积分助手/积分/"+qq, "jf",  jf-zzjf);
                        zzk=zzk-1;
                        set(ROOT + "奶御小助手/配置/奶御积分助手/背包/"+qq,"zzk",zzk);
                        send.s("转账成功："+zzjf+jfname+"\\r"+jfname+"余额:"+getInt(ROOT + "奶御小助手/配置/奶御积分助手/积分/"+qq, "jf"));
                    }else send.s("您"+jfname+"不足"+zzjf);
                }else send.s("您还没有转账卡，请购买转账卡【购买转账卡 1】");
            }else send.s("您输入有误，不能是负数");
        }

        //签到
        if(msg.matches("签到")){
            Calendar calendar = Calendar.getInstance();
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            if (!qdtime.equals(String.valueOf(day))){

                if(jf==0){
                    set(ROOT + "奶御小助手/配置/奶御积分助手/签到日期"+qq, "day",day);
                    set(ROOT + "奶御小助手/配置/奶御积分助手/积分/"+qq, "jf",  qdjl);
                    send.s("奶御小提示：签到成功\\r"+jfname+":"+getString(ROOT + "奶御小助手/配置/奶御积分助手/积分/"+qq,"jf"));
                }else {
                    set(ROOT + "奶御小助手/配置/奶御积分助手/签到日期"+qq, "day", day);
                    set(ROOT + "奶御小助手/配置/奶御积分助手/积分/"+qq, "jf",  jf+qdjl);
                    send.s("奶御小提示：签到成功\\r"+jfname+":"+getString(ROOT + "奶御小助手/配置/奶御积分助手/积分/"+qq,"jf"));
                }
            }else {
                send.s("奶御小提示：您今天已经签到了哟！");
            }
        }

        //调试开关
        if(tsstate.matches("真")){
            send.s("你发送了："+msg);
        }

        if(msg.matches("开启调试")){
            set(ROOT + "奶御小助手/配置/"+groupID, "tsstate",  "真");
            send.s("成功设置:开启调试成功");
        }
        if(msg.matches("关闭调试")){
            set(ROOT + "奶御小助手/配置/"+groupID, "tsstate",  "假");
            send.s("成功设置:关闭调试成功");

        }

        //菜单


        if(msg.matches("奶御小助手")) {

            send.s("欢迎使用奶御发稿小助手\\r" +
                    "1.开始发稿\t2.停止发稿\\r" +
                    "3.间隔设置\t4.稿子菜单\\r" +
                    "5.使用协议\t6.设置管理\\r" +
                    "7.顺序发稿\t8.随机发稿\\r" +
                    "9.积分系统\t10.背包系统\\r" +
                    "奶御小提示：\\r【非管理员】只能使用【3\t5\t9\t10】");
        }
        if(msg.matches("间隔设置")) {
            send.s("奶御教学课堂：\\r命令：【间隔 1000】\\r注意：有空格，1秒等于1000");
        }
        if(msg.matches("使用协议")) {
            send.s("奶御小助手用户协议：\\r一丶违法行为\\r"+"1、不得利用本软件做发广告\\r2、不得利用本软件做违反国家法律的事。\\r3、若违反了与本作者无关");
        }
        if(msg.matches("设置管理")) {
            send.s("奶御教学课堂：\\r命令：【设置发稿管理员@凉生】\\r注意：必须是手动AT");
        }
        //设置管理
        if (msg.matches("设置发稿管理员@.+")  ) {
            if(iffgadmin(zr,qq)) {
                if (FGadmin=="null"){
                       set(ROOT + "奶御小助手/配置/"+groupID, "FGadmin",  atqq +",");
                       send.s("成功设置:" + atqq + "为第一个发稿管理员!");
                }
                if (!(FGadmin=="null")) {
                       set(ROOT + "奶御小助手/配置/"+groupID, "FGadmin", FGadmin + atqq + ",");
                       send.s("成功设置:" + atqq + "为发稿管理员");
                }
            }
        }
        //判断是是否时发稿管理员
        if(ifadmin(FGadmin,String.valueOf(qq))) {
            //设置发稿间隔
            if (msg.matches("间隔 .+")) {


                String[] jiange = msg.split(" ");
                if(jiange[1].matches("[0-9][0-9][0-9][0-9]")){
                    send.s("奶御小提示成功设置：" + jiange[1] + "毫秒发稿每条");
                    set(ROOT + "奶御小助手/配置/"+groupID, "jiange", jiange[1]);
                    jiangedate = jiange[1];
                }

            }

            //随机发稿状态设置
            if (msg.matches("随机发稿")) {
                send.s("奶御小提示:成功设置稿件随机发送");
                set(ROOT + "奶御小助手/配置/"+groupID, "randomState", "随机发稿");
            }
            //顺序发稿状态设置
            if (msg.matches("顺序发稿")) {
                send.s("奶御小提示:成功稿件顺序发送");
                set(ROOT + "奶御小助手/配置/"+groupID, "randomState", "顺序发稿");
            }
            //停止发稿
            if (msg.matches("停止发稿")) {
                send.s("奶御小提示:成功关闭发稿");
                set(ROOT + "奶御小助手/配置/"+groupID, "state", "关");
            }
            //开始发稿
            if (msg.matches("开始发稿")) {
                send.s("奶御小提示:成功打开发稿开关，现在可以发稿了哦");
                set(ROOT + "奶御小助手/配置/"+groupID, "state", "开");
            }
            //稿子菜单
            if (msg.matches("稿子菜单")) {
                File file = new File(ROOT + "奶御小助手/发稿菜单");
                if (file.exists()) {
                    int a = 1;
                    File[] file2 = file.listFiles();
                    for (File name : file2) {
                        if(a%2==0){
                            String namefile = getFileNameNoEx(name.getName());
                            FG_List = FG_List + namefile + "\t\\r";
                            a++;
                        }else {
                            String namefile = getFileNameNoEx(name.getName());
                            FG_List = FG_List + namefile + "\t\t\t\t\t\t\t\t\t\t";
                            a++;
                        }

                    }

                    send.s("稿件菜单：\\r" + FG_List + "\\r发稿命令：【发稿稿件名字】");
                } else {
                    send.s("奶御小提示:\\r您似乎还没有稿子,请手动配置或者创建稿子\\r手动配置稿子路径：\\r"+ROOT + "奶御小助手/发稿菜单");
                    file.mkdir();
                }
            }
            //发稿功能
            if (msg.matches("发稿.+")) {
                File file1 = new File(ROOT + "奶御小助手/发稿菜单");
                if (file1.exists()) {

                    File[] file2 = file1.listFiles();
                    for (File name : file2) {
                        String namefile = getFileNameNoEx(name.getName());
                        fggz = fggz + namefile + ",";
                    }
                } else {
                    send.s("您似乎还没有稿子,请手动配置或者创建稿子");
                    file1.mkdir();
                }
                if (booleangz(fggz, msg.substring(2))) {
                    send.s("即将发稿:"
                            + msg.substring(2) + "\\r发稿间隔："
                            + jiangedate + "毫秒" + "\\r发稿开关：" + kaiguan + "\\r发稿顺序："+randomState);
                    new Thread(new Runnable() {// 发送端口
                        @Override
                        public void run() {
                            fg(msg.substring(2));
                        }
                    }).start();
                }
            }
        }
    }

    /**
     * 发稿函数
     * @param gzname //稿件名
     */
    public void fg(String gzname){

            // TimeUnit.MILLISECONDS.sleep(Integer.parseInt(getString(ROOT+"奶御小助手/jiange","jiange")));//MILLISECONDS表示以毫秒为单位延时
            //if (getString(ROOT + "奶御小助手/jiange","state").matches("开"))




        try {
            StringBuffer gz = new StringBuffer();
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(ROOT+"奶御小助手/发稿菜单/"+gzname+".txt")),
                    "UTF-8"));
                String lineTxt = null;

                while ((lineTxt = br.readLine()) != null) {
                    gz.append(lineTxt+",");
                }

            String[] gzarr = gz.toString().split(",");

            if (randomState.matches("随机发稿")){
                gzarr = m3(gzarr);
            }

            for(String line:gzarr){
                if (getString(ROOT + "奶御小助手/配置/"+groupID,"state").matches("开")) {
                    send.s(line);
                    TimeUnit.MILLISECONDS.sleep(Integer.parseInt(getString(ROOT + "奶御小助手/配置/"+groupID, "jiange")));//MILLISECONDS表示以毫秒为单位延时
                }
            }

            br.close();
        } catch (Exception e) {
            System.err.println("read errors :" + e);
        }
    }



    /**
     * 判断稿子列表里是否有稿子
     * @param fggz 稿子列表
     * @param msg  稿子名字
     * @return
     */
    public boolean booleangz(String fggz,String msg){

        String[] gz = fggz.split(",");
        for (String gzname:gz){
            if(gzname.matches(msg)) {
                return true;
            }
        }
        return false;
        }

    /**
     * 打乱数组函数
     *
     * @param arr
     * @return
     */
    public static String []  m3(String [] arr) {
        String [] arr2 =new String[arr.length];
        int count = arr.length;
        int cbRandCount = 0;// 索引
        int cbPosition = 0;// 位置
        int k =0;
        do {
            Random rand = new Random();
            int r = count - cbRandCount;
            cbPosition = rand.nextInt(r);
            arr2[k++] = arr[cbPosition];
            cbRandCount++;
            arr[cbPosition] = arr[r - 1];// 将最后一位数值赋值给已经被使用的cbPosition
        } while (cbRandCount < count);
        return arr2;
    }




}
