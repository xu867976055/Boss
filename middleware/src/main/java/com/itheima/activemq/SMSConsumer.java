package com.itheima.activemq;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.springframework.stereotype.Component;

import com.aliyuncs.exceptions.ClientException;
import com.itheima.utils.SmsUtils;

/**  
 * ClassName:SMSConsumer <br/>  
 * Function:  <br/>  
 * Date:     2018年3月25日 下午4:13:03 <br/>       
 */

@Component
public class SMSConsumer implements MessageListener{

    @Override
    public void onMessage(Message msg) {
          MapMessage message = (MapMessage) msg;
          try {
            String tel = message.getString("tel");
            String code = message.getString("code");
            System.out.println(tel+"-------------------------"+code);
//            SmsUtils.sendSms(tel, code);
        } catch (JMSException e) {
            e.printStackTrace();  
            
        } 
        
    }

}
  
