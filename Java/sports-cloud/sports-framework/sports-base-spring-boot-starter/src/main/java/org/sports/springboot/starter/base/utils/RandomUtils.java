package org.sports.springboot.starter.base.utils;

import java.util.Random;

public class RandomUtils {

    /**
     * 随机生成2位字母+6位数字昵称
     * @return
     */
    public static String buildNickName(){
        return buildNickName(2, 6);
    }

    /**
     * 随机生成指定位数字母+指定位数数字昵称
     * @return
     */
    public static String buildNickName(int letterlength, int digitlength){
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for(int i = 0; i< letterlength; i++){
            int index = random.nextInt(52);
            if(index < 26){
                sb.append((char) (index + 'A'));
            }else{
                sb.append((char) (index - 26 + 'a'));
            }
        }
        for(int i = 0; i< digitlength; i++){
            sb.append(random.nextInt(9));
        }
        if(sb.toString().toUpperCase().startsWith("SB")){
            return buildNickName(letterlength, digitlength);
        }
        return sb.toString();
    }

    private static String[] codeStrs = new String[]{"0","1","2","3","4","5","6","7","8","9","a","b","c","d","e","f","g",
            "h","j","k","l","m","n","p","q","r","s","t","u","v","w","x","y","z","A","B","C","D","E","F","G",
            "H","J","K","L","M","N","P","Q","R","S","T","U","V","W","X","Y","Z"}; //58 = 10 + 24 +24

    /**
     * 随机密码生成规则：随机密码生成为16位，6位字母+10位数字组合，第一位字母大写
     * @return
     */
    public static String buildPasswd(){
         StringBuilder back= new StringBuilder();
         for(int i = 1; i< 17; i++){
             if(i == 1){
                 back.append(codeStrs[randomCount(35, 58) -1]);
             }else if(i <= 6){
                 back.append(codeStrs[randomCount(11, 58) -1]);
             }else {
                 back.append(codeStrs[randomCount(1, 10) -1]);
             }
         }
        return back.toString();
    }

    /**
     * 根据长度生成随机数字
     * @param start 起始数字
     * @param end 结束数字
     * @return 生成的随机码
     */
    public static Integer randomCount(Integer start, Integer end){
        return (int)(Math.random()*(end - start +1) + start);
    }

    public static void main(String[] args) {
        for(int i = 1; i< 100000; i++){
            System.out.println(buildPasswd());
        }

//        for (int i = 0;i<50;i++){
//            System.out.println(buildNickName());
//        }

    }


}
