package com.itheima.bos.dao.base;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.bos.domain.base.Standard;

/**  
 * ClassName:StandardRepository <br/>  
 * Function:  <br/>  
 * Date:     2018年3月12日 下午5:29:05 <br/>       
 */
public interface StandardRepository extends JpaRepository<Standard, Long>{
    //SpringDataJPA提供了一套命名规范,遵循这一套规范定义查询类方法
    // 必须以findBy开头,后面跟属性的名字,首字母必须大写
    // 如果有多个条件,使用对应的SQL关键字
    List<Standard> findByNameLike(String name);
    
    List<Standard> findByNameAndMaxWeight(String name,Integer weight);
    
//    JPQL == HQL
    @Query("from Standard where name=? and maxWeight=?")
    List<Standard> findByNameAndMaxWeight123(String name,Integer weight);
    
//    原生sql
    @Query(value="select * from T_STANDARD where C_NAME=? and C_MAX_WEIGHT=?",nativeQuery=true)
    List<Standard> findByNameAndMaxWeight12345(String name,Integer weight);
    
    @Modifying
    @Transactional
    @Query("update Standard set maxWeight=? where name=?")
    void updateWeightByname(Integer weight,String name);
    
    @Modifying
    @Transactional
    @Query("delete from Standard where name=?")
    void deleteByName(String name);
    
}
  
