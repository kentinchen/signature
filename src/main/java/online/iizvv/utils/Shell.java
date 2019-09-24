package online.iizvv.utils;

import java.io.*;

/**
 * @author ：iizvv
 * @date ：Created in 2019-07-05 09:05
 * @description：执行shell脚本
 * @modified By：
 * @version: 1.0
 */
public class Shell {

    /**
     * create by: iizvv
     * description: 执行shell
     * create time: 2019-07-05 09:17

     * @return 是否执行成功
     */
    public static boolean run(String command) throws IOException, InterruptedException {
        Process process = Runtime.getRuntime().exec(command);
        printMessage(process.getInputStream());
        printMessage(process.getErrorStream());
        int exitValue = process.waitFor();
        return exitValue==0;
    }

    /**
     * create by: iizvv
     * description: 异步执行shell
     * create time: 2019-09-24 11:10

     * @return void
     */
    private static void printMessage(final InputStream input) {
        new Thread(new Runnable() {
            public void run() {
                Reader reader = new InputStreamReader(input);
                BufferedReader bf = new BufferedReader(reader);
                String line = null;
                try {
                    while((line=bf.readLine())!=null)
                    {
                        System.out.println(line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
