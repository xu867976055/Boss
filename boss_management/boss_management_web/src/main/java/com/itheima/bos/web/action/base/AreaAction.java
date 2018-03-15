package com.itheima.bos.web.action.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import com.itheima.bos.domain.base.Area;
import com.itheima.bos.domain.base.Constant;
import com.itheima.bos.domain.base.Courier;
import com.itheima.bos.service.AreaService;
import com.itheima.utils.PinYin4jUtils;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**  
 * ClassName:AreaAction <br/>  
 * Function:  <br/>  
 * Date:     2018年3月15日 下午4:45:06 <br/>       
 */
@Namespace("/")
@ParentPackage("struts-default")
@Controller
@Scope("prototype")
public class AreaAction extends ActionSupport implements ModelDriven<Area> {

    private Area model = new Area();
    
    @Autowired
    private AreaService areaService;
    
    @Override
    public Area getModel() {
        return model;
    }
    
    private File file;
    public void setFile(File file) {
        this.file = file;
    }
    
    @Action(value="areaAction_importXLS",results={@Result(name="import_success",location="/pages/base/area.html",type="redirect")})
    public String importXLS(){
//        System.out.println("1111111111111111111111111111111111");
        try {
            HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(file));
//            读取工作簿
            HSSFSheet sheet = workbook.getSheetAt(0);
            
//          创建一个集合保存数据
            List<Area> list = new ArrayList<>();
            
//            遍历行
            for (Row row : sheet) {
//                第一行标题数据不要
                if(row.getRowNum() == 0){
                    continue;
                }
                
//                读取表格的数据
                String province = row.getCell(1).getStringCellValue();
                String city = row.getCell(2).getStringCellValue();
                String district = row.getCell(3).getStringCellValue();
                String postcode = row.getCell(4).getStringCellValue();
                
                //广东省，xx市，xx区
//                去掉省市区最后一个字符
                province = province.substring(0, province.length()-1);
                city = city.substring(0, city.length()-1);
                district = district.substring(0, district.length()-1);
                
//                获取城市编码
                String citycode = PinYin4jUtils.hanziToPinyin(city,"").toUpperCase();
                String[] headByString = PinYin4jUtils.getHeadByString(province+city+district);
//                获取简码
                String shortcode = PinYin4jUtils.stringArrayToString(headByString);
                
//                构造一个area
                Area area = new Area();
                area.setProvince(province);
                area.setCity(city);
                area.setDistrict(district);
                area.setPostcode(postcode);
                area.setCitycode(citycode);
                area.setShortcode(shortcode);
                //添加到集合
                list.add(area);
            }
            //一次性保存区域数据
            areaService.save(list);
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();  
        }
        
        return Constant.IMPORT_SUCCESS;
    }
    
    private int page;
    private int rows;
    public void setPage(int page) {
        this.page = page;
    }
    public void setRows(int rows) {
        this.rows = rows;
    }
    
    @Action(value="areaAction_findByPage")
    public String findByPage() throws IOException{
        
        Pageable pageable = new PageRequest(page-1, rows);
        Page<Area> page = areaService.findAll(pageable);
        
        Long total = page.getTotalElements();
        List<Area> list = page.getContent();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("total",total);
        map.put("rows", list);
        
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setExcludes(new String[] {"subareas"});
        
        String json = JSONObject.fromObject(map,jsonConfig).toString();
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(json);
        
        return NONE;
    }

}
  
