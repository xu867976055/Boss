package com.itheima.bos.domain.take_delivery;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

/**  
 * ClassName:PageBean <br/>  
 * Function:  <br/>  
 * Date:     2018年3月31日 下午9:24:29 <br/>       
 */

/*JPA框架进行分页查询的结果类型为org.springframework.data.domain.Page,
 * 该类型在使用CXF框架时,无法进行序列化,因为无法添加@XmlRootElement注解,
 * 所以需要自己实现一个PageBean用来封装分页查询的结果.
       该PageBean同时还要使用@XmlSeeAlso注解,指定泛型的类型,
       否则CXF框架序列化时,无法将泛型类型序列化.*/

@XmlRootElement(name="pageBean")
@XmlSeeAlso({Promotion.class})
public class PageBean<T> {

    private List<T> list;
    private Long total;
    public List<T> getList() {
        return list;
    }
    public void setList(List<T> list) {
        this.list = list;
    }
    public Long getTotal() {
        return total;
    }
    public void setTotal(Long total) {
        this.total = total;
    }
    
}
  
