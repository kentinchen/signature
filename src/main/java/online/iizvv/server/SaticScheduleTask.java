package online.iizvv.server;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;

/**
 * @Author FXS
 * @Date 2019/7/23
 **/
@Configuration      //1.主要用于标记配置类，兼备Component的效果。 // 2.开启定时任务
public class SaticScheduleTask {

    @Resource
    private WebSocketServer webSocketServer;


    //3.添加定时任务
    //@Scheduled(cron = "0/5 * * * * ?")
    //或直接指定时间间隔，例如：5秒
    @Scheduled(cron = "0/20 * * * * ?")
    private void configureTasks() throws Exception{
        System.out.println("------");
        webSocketServer.sendInfo("连接成功",null);
    }
}
