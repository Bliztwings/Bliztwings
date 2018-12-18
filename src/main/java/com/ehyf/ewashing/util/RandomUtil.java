package com.ehyf.ewashing.util;

import java.text.SimpleDateFormat;
import java.util.Random;
/**
 * 
 * TODO 通过随机数组成相关的ID.<br>
 * @author User <br>
 * @version 1.0.0 2014年2月24日<br>
 * @see 
 * @since
 */
public class RandomUtil {
	/**
	 * 生产随机的ID：生成规则：年月日时分秒毫秒
	 * @return
	 */
	public String getRandomId(){
		SimpleDateFormat sdformat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss sss");  
		java.util.Date date=new java.util.Date();  
        String newId = sdformat.format(date).substring(0, 4)+sdformat.format(date).substring(5, 7)+sdformat.format(date).substring(8, 10)+sdformat.format(date).substring(11, 13)+sdformat.format(date).substring(14, 16)+sdformat.format(date).substring(17, 19)+sdformat.format(date).substring(20, 23);
        String val = "";     
        
        Random random = new Random();     
        for(int i = 0; i < 7; i++)     
        {     
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num"; // 输出字母还是数字      
                     
            if("char".equalsIgnoreCase(charOrNum)) // 字符串      
            {     
                int choice =   97; //取得大写字母还是小写字母      
                val += (char) (choice + random.nextInt(26));     
            }     
            else if("num".equalsIgnoreCase(charOrNum)) // 数字      
            {     
                val += String.valueOf(random.nextInt(10));     
            }     
        }     
        return newId+val;
	}
	
	/**
	 * 获取随机验证码
	 * @param min
	 * @param max
	 * @return
	 */
	public static int getRandNum(int min, int max) {
	    int randNum = min + (int)(Math.random() * ((max - min) + 1));
	    return randNum;
	}
}
