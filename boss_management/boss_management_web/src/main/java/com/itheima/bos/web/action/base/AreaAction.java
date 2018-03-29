package com.itheima.bos.web.action.base;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
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
import com.itheima.bos.service.base.AreaService;
import com.itheima.bos.web.action.CommonAction;
import com.itheima.utils.FileDownloadUtils;
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
public class AreaAction extends CommonAction<Area>{

    public AreaAction() {
        super(Area.class);  
    }

    @Autowired
    private AreaService areaService;
    
    private File file;
    public void setFile(File file) {
        this.file = file;
    }
    
    @Action(value="areaAction_importXLS",results={@Result(name="import_success",location="/pages/base/area.html",type="redirect")})
    public String importXLS(){
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
        
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setExcludes(new String[] {"subareas"});
        
        page2json(page, jsonConfig);
        return NONE;
    }
    
    private String q;
    public void setQ(String q) {
        this.q = q;
    }
    
    @Action(value="areaAction_findAll")
    public String findAll() throws IOException{
        List<Area> list;
        if(StringUtils.isNotEmpty(q)){
            list = areaService.findByQ(q);
        }else{
            Page<Area> page = areaService.findAll(null);
            list = page.getContent();
        }
        
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setExcludes(new String[] {"subareas"});
        
        list2json(list, jsonConfig);
        return NONE;
    }
    
    @Action(value="areaAction_addArea",results={@Result(name="save_success",location="/pages/base/area.html",type="redirect")})
    public String addArea(){
        areaService.addArea(getModel());
        return Constant.SAVE_SUCCESS;
    }
    
    
    private String ids;
    public void setIds(String ids) {
        this.ids = ids;
    }
    @Action(value="areaAction_delete",results={@Result(name="delete_by_id_success",location="/pages/base/area.html",type="redirect")})
    public String delete(){
        areaService.deleteById(ids);
        return Constant.DELETE_BY_ID_SUCCESS;
    }
    
    
    //导出Excel
    @Action(value="areaAction_exportExcel")
    public String exportExcel() throws IOException{
        
        //先从数据库中查询所有的区域数据
        Page<Area> page = areaService.findAll(null);
        List<Area> list = page.getContent();
        
        //创建一个excel文件
        HSSFWorkbook workbook = new HSSFWorkbook();
        //创建sheet
        HSSFSheet sheet = workbook.createSheet();
        //创建行标题
        HSSFRow titleRow = sheet.createRow(0);
        
        titleRow.createCell(0).setCellValue("省");
        titleRow.createCell(1).setCellValue("市");
        titleRow.createCell(2).setCellValue("区");
        titleRow.createCell(3).setCellValue("邮编");
        titleRow.createCell(4).setCellValue("简码");
        titleRow.createCell(5).setCellValue("城市编码");
        
        //遍历数据,创建数据行
        for (Area area : list) {
            //每次都从最后一行添加,获取最后一行行号
            int lastRowNum = sheet.getLastRowNum();
            //创建新行
            HSSFRow newRow = sheet.createRow(lastRowNum+1);

            //添加数据
            newRow.createCell(0).setCellValue(area.getProvince());
            newRow.createCell(1).setCellValue(area.getCity());
            newRow.createCell(2).setCellValue(area.getDistrict());
            newRow.createCell(3).setCellValue(area.getPostcode());
            newRow.createCell(4).setCellValue(area.getShortcode());
            newRow.createCell(5).setCellValue(area.getCitycode());
           
        }
        //文件名
        String fileName = "区域统计.xls";
        
        //两头一流
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ServletContext servletContext = ServletActionContext.getServletContext();
        
        ServletOutputStream outputStream = response.getOutputStream();
      
        // 获取mimeType
        // 先获取mimeType再重新编码,避免编码后后缀名丢失,导致获取失败
        String mimeType = servletContext.getMimeType(fileName);
        //获取浏览器类型
        String userAgent = request.getHeader("User-Agent");
        //对文件名重新编码
        fileName = FileDownloadUtils.encodeDownloadFilename(fileName, userAgent);
        
        //设置信息头
        response.setContentType(mimeType);
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        
        //写出文件
        workbook.write(outputStream);
        workbook.close();
        
        return NONE;
    }
    

}
  
