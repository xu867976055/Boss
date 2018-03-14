package com.itheima.bos.dao.test;

import java.util.List;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.itheima.bos.dao.base.StandardRepository2;
import com.itheima.bos.domain.base.Standard;

/**  
 * ClassName:StandardRepositoryTest <br/>  
 * Function:  <br/>  
 * Date:     2018年3月12日 下午5:34:58 <br/>       
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class StandardRepositoryTest {
    @Autowired
    private StandardRepository2 standardRepository;
//    添加
    @Test
    public void save(){
        Standard standard = new Standard();
        standard.setMaxWeight(120);
        standard.setName("孙尚香");
        standardRepository.save(standard);
    }
    
//    查询所有
    @Test
    public void test1(){
        List<Standard> list = standardRepository.findAll();
        for (Standard standard : list) {
            System.out.println(standard);
        }
    }
    
//    修改
    @Test
    public void test2(){
        Standard standard = new Standard();
        standard.setId(1L);
        standard.setName("空间环境");
        standardRepository.save(standard);
        
    }
    
//    查询指定
    @Test
    public void test03(){
        Standard standard = standardRepository.findOne(1L);
        System.out.println(standard);
    }
    
//    删除指定
    @Test
    public void test04(){
        standardRepository.delete(1L);
    }
    
//   查找姓孙的
    @Test
    public void test05(){
        List<Standard> list = standardRepository.findByNameLike("孙%");
        for (Standard standard : list) {
            System.out.println(standard.getName());
        }
    }
    
//    多个条件共同查询
    @Test
    public void test06(){
        List<Standard> list = standardRepository.findByNameAndMaxWeight("孙权", 120);
        for (Standard standard : list) {
            System.out.println(standard);
        }
    }
    
    @Test
    public void test07(){
        List<Standard> list = standardRepository.findByNameAndMaxWeight("孙权", 120);
        for (Standard standard : list) {
            System.out.println(standard);
        }
    }
    
    @Test
    public void test08(){
        List<Standard> list = standardRepository.findByNameAndMaxWeight123("孙权", 120);
        for (Standard standard : list) {
            System.out.println(standard);
        }
    }
    
    @Test
    public void test09(){
        List<Standard> list = standardRepository.findByNameAndMaxWeight12345("孙权", 120);
        for (Standard standard : list) {
            System.out.println(standard);
        }
    }
    
    @Test
    public void test10(){
      standardRepository.updateWeightByname(110,"孙权");
    }
    
    @Test
    public void test11(){
      standardRepository.deleteByName("孙权");
    }
    
}
  
