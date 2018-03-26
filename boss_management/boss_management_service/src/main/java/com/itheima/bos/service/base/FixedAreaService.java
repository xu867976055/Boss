package com.itheima.bos.service.base;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.itheima.bos.domain.base.FixedArea;

/**  
 * ClassName:FixedAreaService <br/>  
 * Function:  <br/>  
 * Date:     2018年3月18日 下午2:58:20 <br/>       
 */
public interface FixedAreaService {

    void save(FixedArea model);

    Page<FixedArea> findAll(Pageable pageable);

    void associationFixedAreaToCourier(Long id, Long courierId, Long takeTimeId);

    void assignSubArea2FixedArea(Long[] subAreaIds, Long fixedAreaId);

    void noAssignSubArea2FixedArea(Long fixedAreaId);

}
  
