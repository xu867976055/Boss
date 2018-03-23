package com.itheima.bos.service.take_delivery.impl;

import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

import javax.ws.rs.core.MediaType;

import org.apache.cxf.jaxrs.client.WebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.bos.dao.base.AreaRepository;
import com.itheima.bos.dao.base.FixedAreaRepository;
import com.itheima.bos.dao.take_delivery.OrderRepository;
import com.itheima.bos.dao.take_delivery.WorkBillRepository;
import com.itheima.bos.domain.base.Area;
import com.itheima.bos.domain.base.Courier;
import com.itheima.bos.domain.base.FixedArea;
import com.itheima.bos.domain.base.SubArea;
import com.itheima.bos.domain.take_delivery.Order;
import com.itheima.bos.domain.take_delivery.WorkBill;
import com.itheima.bos.service.take_delivery.OrderService;

/**  
 * ClassName:OrderServiceImpl <br/>  
 * Function:  <br/>  
 * Date:     2018年3月23日 下午4:34:20 <br/>       
 */
@Transactional
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private AreaRepository areaRepository;
    @Autowired
    private FixedAreaRepository fixedAreaRepository;
    @Autowired
    private WorkBillRepository workBillRepository;
    
    
    @Override
    public void saveOrder(Order order) {
      
        
        //把瞬时态的Area转化为持久态的Area
        Area sendArea = order.getSendArea();
        if(sendArea != null){
            //持久态对象
            Area sendAreaDB = areaRepository.findByProvinceAndCityAndDistrict(
                    sendArea.getProvince(),
                    sendArea.getCity(),
                    sendArea.getDistrict());
            order.setSendArea(sendAreaDB);
        }
        
        
        //把瞬时态的Area转化为持久态的Area
        Area recArea = order.getRecArea();
        if(recArea != null){
            //持久态对象
            Area recAreaDB = areaRepository.findByProvinceAndCityAndDistrict(
                    recArea.getProvince(),
                    recArea.getCity(),
                    recArea.getDistrict());
            order.setRecArea(recAreaDB);
        }
        
     
        //保存订单
        order.setOrderTime(new Date());
        order.setOrderNum(UUID.randomUUID().toString().replaceAll("-", ""));
        
        orderRepository.save(order);
        
        //自动分单
        //1.用户填写的寄单地址完全匹配
        String sendAddress = order.getSendAddress();
        if(sendAddress != null){
            //给crm发请求根据地址查询定区id
            String fixedAreaId = WebClient.create("http://localhost:8180/crm/webService/customerService/findFixedAreaId")
            .query("address", sendAddress)
            .accept(MediaType.APPLICATION_JSON)
            .type(MediaType.APPLICATION_JSON)
            .get(String.class);
            
            if(fixedAreaId != null){
                FixedArea fixedArea = fixedAreaRepository.findOne(Long.parseLong(fixedAreaId));
               //从定区中查询快递员
                if(fixedArea != null){
                    Set<Courier> couriers = fixedArea.getCouriers();
                    if(!couriers.isEmpty()){
                        Iterator<Courier> iterator = couriers.iterator();
                        Courier courier = iterator.next();
                        //指派快递员
                        order.setCourier(courier);
                        //生成工单
                        WorkBill workBill = new WorkBill();
                        //追单次数
                        workBill.setAttachbilltimes(0);
                        //建单时间
                        workBill.setBuildtime(new Date());
                        //快递员
                        workBill.setCourier(courier);
                        //订单
                        workBill.setOrder(order);
                        
                        workBill.setPickstate("新单");
                        workBill.setRemark(order.getRemark());
                        workBill.setSmsNumber("111");
                        workBill.setType("新");
                        
                        workBillRepository.save(workBill);
                        order.setOrderType("自动分单");
                        return;
                        
                    }
                }
            }else{
                //2.地址部分匹配
                //定区id查不到,不完全匹配
                //根据省市区查询分区
                Area sendArea2 = order.getSendArea();
                if(sendArea2 != null){
                    Set<SubArea> subareas = sendArea2.getSubareas();
                    for (SubArea subArea : subareas) {
                        String keyWords = subArea.getKeyWords();
                        String assistKeyWords = subArea.getAssistKeyWords();
                        if(keyWords.contains(sendAddress) || assistKeyWords.contains(assistKeyWords)){
                            FixedArea fixedArea = subArea.getFixedArea();
                            
                            if(fixedArea != null){
                                Set<Courier> couriers = fixedArea.getCouriers();
                                if(!couriers.isEmpty()){
                                    Iterator<Courier> iterator = couriers.iterator();
                                    Courier courier = iterator.next();
                                    //指派快递员
                                    order.setCourier(courier);
                                    //生成工单
                                    WorkBill workBill = new WorkBill();
                                    //追单次数
                                    workBill.setAttachbilltimes(0);
                                    //建单时间
                                    workBill.setBuildtime(new Date());
                                    //快递员
                                    workBill.setCourier(courier);
                                    //订单
                                    workBill.setOrder(order);
                                    
                                    workBill.setPickstate("新单");
                                    workBill.setRemark(order.getRemark());
                                    workBill.setSmsNumber("222");
                                    workBill.setType("新");
                                    
                                    workBillRepository.save(workBill);
                                    order.setOrderType("自动分单");
                                    return;
                                    
                                }
                            }
                        }
                    }
                }
            }
        }
        //乱写地址
        order.setOrderType("人工分单");
    }
}
  
