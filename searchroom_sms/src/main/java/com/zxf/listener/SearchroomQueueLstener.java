package com.zxf.listener;

import com.zxf.util.SmsUtil;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 消费者
 * 监听rabbit    searchroom_sms
 * @author zxf
 */
@Component
@RabbitListener(queues = "searchroom_sms")
public class SearchroomQueueLstener {

    @Autowired
    SmsUtil smsUtil;

    @Value("${aliyun.sms.template_code}")
    private String template_code;

    @Value("${aliyun.sms.sign_name}")
    private String sign_name;

    @RabbitHandler
    public void sendPhoneMsg(Map<String, String> map)throws Exception{
        String phoneNumber = map.get("phoneNumber");
        String checkCode = map.get("checkCode");
        System.out.println(phoneNumber+"    "+checkCode);
        smsUtil.sendSms(phoneNumber, template_code, sign_name, "{\"code\":\""+checkCode+"\"}");
    }

}
