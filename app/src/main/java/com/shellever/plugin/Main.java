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
    private String FGState = "1";    //发稿状态
    private String FG_List="";  //发稿菜单
    private String FGadmin="1977791013,";     //发稿管理员
    private String fggz="";     //待发稿稿件名字
    private String jiangedate;  //发稿间隔时间
    private String kaiguan;  //发稿间隔时间
    private String randomState;  //随机发稿状态
    private static final Integer ONE = 1;
    Properties properties=null;
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
        this.FGadmin = getString(ROOT+"奶御小助手/jiange","FGadmin");
        this.jiangedate = getString(ROOT+"奶御小助手/jiange","jiange");
        this.kaiguan = getString(ROOT+"奶御小助手/jiange","state");
        this.randomState = getString(ROOT+"奶御小助手/jiange","randomState");

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


        if(msg.matches("奶御小助手")) {
            send.s("欢迎使用奶御发稿小助手\\r" +
                    "1.开始发稿\t2.停止发稿\\r" +
                    "3.间隔设置\t4.稿子菜单\\r" +
                    "5.使用协议\t6.设置管理\\r" +
                    "7.顺序发稿\t8.随机发稿");
        }
        if(msg.matches("间隔设置")) {
            send.s("奶御教学课堂：\\r命令：【间隔 1000】\\r注意：有空格，1秒等于1000");
        }
        if(msg.matches("使用协议")) {
            send.s("奶御小助手用户协议：\\r一丶违法行为\\r"+"1、不得利用本软件做发广告\\r2、不得利用本软件做违反国家法律的事。\\r3、若违反了与本作者无关");
        }
        if(msg.matches("设置管理")) {
            send.s("命令：【设置发稿管理员@凉生】\\r注意：必须是手动AT");
        }
        //设置管理
            if (msg.matches("设置发稿管理员@.+")&& qq==1977791013) {
                set(ROOT + "奶御小助手/jiange", "FGadmin", FGadmin+atqq+",");
                send.s("成功设置:"+atqq+"为发稿管理员");

            }

        if(ifadmin(FGadmin,String.valueOf(qq))) {
            //设置发稿间隔
            if (msg.matches("间隔 .+")) {

                String[] jiange = msg.split(" ");
                send.s("奶御小提示成功设置：" + jiange[1] + "毫秒发稿每条");
                set(ROOT + "奶御小助手/jiange", "jiange", jiange[1]);
                jiangedate = jiange[1];
            }

            //随机发稿状态设置
            if (msg.matches("随机发稿")) {
                send.s("奶御小提示:成功设置稿件随机发送");
                set(ROOT + "奶御小助手/jiange", "randomState", "随机发稿");
            }
            //顺序发稿状态设置
            if (msg.matches("顺序发稿")) {
                send.s("奶御小提示:成功稿件顺序发送");
                set(ROOT + "奶御小助手/jiange", "randomState", "顺序发稿");
            }
            //停止发稿
            if (msg.matches("停止发稿")) {
                send.s("奶御小提示:成功关闭发稿");
                set(ROOT + "奶御小助手/jiange", "state", "关");
            }
            //开始发稿
            if (msg.matches("开始发稿")) {
                send.s("奶御小提示:成功打开发稿开关，现在可以发稿了哦");
                set(ROOT + "奶御小助手/jiange", "state", "开");
            }
            //菜单
            if (msg.matches("稿子菜单")) {
                File file = new File(ROOT + "奶御小助手/发稿菜单");
                if (file.exists()) {
                    File[] file2 = file.listFiles();
                    for (File name : file2) {
                        String namefile = getFileNameNoEx(name.getName());
                        FG_List = FG_List + namefile + "\t";
                    }
                    send.s("稿件菜单：\\r" + FG_List + "发稿命令：【发稿稿件名字】");
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
                } else {
                    send.s("稿子不存在");
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
                if (getString(ROOT + "奶御小助手/jiange","state").matches("开")) {
                    send.s(line);
                    TimeUnit.MILLISECONDS.sleep(Integer.parseInt(getString(ROOT + "奶御小助手/jiange", "jiange")));//MILLISECONDS表示以毫秒为单位延时
                }
            }
            send.s("发送完了");
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
