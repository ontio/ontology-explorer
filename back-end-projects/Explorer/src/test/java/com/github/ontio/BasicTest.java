package com.github.ontio;

import com.github.ontio.mapper.BlockMapper;
import org.junit.Test;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import static java.math.BigDecimal.ROUND_HALF_DOWN;

/**
 * @author zhouq
 * @version 1.0
 * @date 2019/3/30
 */
public class BasicTest {


    @Test
    public void calculateScore(){


        Class<?> classT = BlockMapper.class;
        Method[] methods = classT.getMethods();
        for (Method method:methods){
            System.out.println(","+method.getName());
        }

/*        String contractAddress = Address.parse(com.github.ontio.common.Helper.reverse("8c15299cc6843e808b42f1ffb9cff7ec36f81ea1")).toBase58();
        System.out.println("addr；"+contractAddress);*/


        int txnCount = 4003;
        int activeAddrCount = 2880;
        int allActiveAddrCount = 2929;
        int allTxnCount = 5835;

        BigDecimal a = new BigDecimal(txnCount).divide(new BigDecimal(activeAddrCount), 2, ROUND_HALF_DOWN);
        BigDecimal b = new BigDecimal(allTxnCount).divide(new BigDecimal(allActiveAddrCount), 2, ROUND_HALF_DOWN);
        BigDecimal x = new BigDecimal("1.5").multiply(new BigDecimal("2")).multiply(a.divide(b, 2, ROUND_HALF_DOWN));
        double exp = -x.divide(new BigDecimal("2")).doubleValue();

        BigDecimal sapp = x.multiply(new BigDecimal(Math.exp(exp)));
        BigDecimal appScore = sapp.multiply(new BigDecimal(txnCount));
        int score = appScore.setScale(4, BigDecimal.ROUND_HALF_DOWN).intValue();
        System.out.println("score:"+score);


        BigDecimal temp = new BigDecimal(score).divide(new BigDecimal(score), 2, ROUND_HALF_DOWN);
        BigDecimal ongreward = new BigDecimal("19841").divide(new BigDecimal("5"), 2, ROUND_HALF_DOWN).multiply(temp);
        System.out.println("ongreward:"+ongreward);

    }

    @Test
    public void test01(){


        String []arr = {"星期日","星期一","星期二","星期三","星期四","星期五","星期六"};
        Calendar calendar2 = Calendar.getInstance();
        //calendar2.setTimeInMillis(1553875800);
        System.out.println("今天是："+arr[calendar2.get(Calendar.DAY_OF_WEEK)-1]); //1.数组下标从0开始；2.老外的第一天是从星期日开始的


        double e =-new BigDecimal("1").divide(new BigDecimal("2")).doubleValue();
        System.out.println("e:"+e);
        double aa = Math.exp(e);
        System.out.println("aa:"+aa);

/*        int txn = 12;
        int activeAddress = 234;
        int allTxn = 34234;
        int allActiveAddress = 234234;
        BigDecimal x = new BigDecimal("0");
        BigDecimal a = new BigDecimal(txn).divide(new BigDecimal(activeAddress));
        BigDecimal b = new BigDecimal(allTxn).divide(new BigDecimal(allActiveAddress));
        x = new BigDecimal("1.5").multiply(new BigDecimal("2")).multiply(a.divide(b));
        BigDecimal sapp = x.multiply(new BigDecimal(Math.exp(x.divide(new BigDecimal("2")).doubleValue())));
        BigDecimal appScore = sapp.multiply(new BigDecimal(txn));
        System.out.println("appScore:"+appScore.intValue());

        BigDecimal ongReward = new BigDecimal("2000").divide(new BigDecimal("5")).multiply(appScore.divide(new BigDecimal("20")));
        System.out.println("ongReward:"+ongReward);*/

        Date now = new Date();

        Calendar calendar = Calendar.getInstance(); //得到日历
        calendar.setTime(now);//把当前时间赋给日历
        calendar.add(Calendar.DAY_OF_MONTH, -7); //设置为前一天
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        Date yesterday = calendar.getTime();
        System.out.println("昨天0点："+yesterday.getTime()/1000L);

    }

}
