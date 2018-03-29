package com.itheima.bos.service.jobs;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.itheima.bos.dao.take_delivery.WorkBillRepository;
import com.itheima.bos.domain.take_delivery.WorkBill;
import com.itheima.utils.MailUtils;

/**  
 * ClassName:WorkBillJob <br/>  
 * Function:  <br/>  
 * Date:     2018年3月29日 下午5:25:05 <br/>       
 */

@Component
public class WorkBillJob {

    @Autowired
    private WorkBillRepository workBillRepository;
    
    public void sendEmail(){
        //查询所有工单
        List<WorkBill> list = workBillRepository.findAll();
        String content = "编号\t快递员\t取件状态\t时间<br/>";
        for (WorkBill workBill : list) {
            content +=  workBill.getId()+"\t"
                    + workBill.getCourier().getName()+"\t"
                    + workBill.getPickstate()+"\t"
                    + workBill.getBuildtime().toLocaleString()+"<br/>";
        }
        
        
        MailUtils.sendMail("工单统计", content, "a6409662@163.com");
        System.out.println("邮件已发送"+new Date().toLocaleString());
    }
    
}
  
