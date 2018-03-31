package com.itheima.bos.web.action.system;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import javax.servlet.ServletContext;

import org.apache.commons.io.FileUtils;
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

import com.itheima.bos.domain.base.Constant;
import com.itheima.bos.domain.take_delivery.Promotion;
import com.itheima.bos.service.take_delivery.PromotionService;
import com.itheima.bos.web.action.CommonAction;

/**
 * ClassName:PromotionAction <br/>
 * Function: <br/>
 * Date: 2018年3月31日 下午4:25:41 <br/>
 */

@Namespace("/")
@Controller
@Scope("prototype")
@ParentPackage(value = "struts-default")
public class PromotionAction extends CommonAction<Promotion> {

    public PromotionAction() {
        super(Promotion.class);
    }

    @Autowired
    private PromotionService promotionService;

    // 属性驱动获取图片和图片文件名
    private File titleImgFile;
    private String titleImgFileFileName;

    public void setTitleImgFile(File titleImgFile) {
        this.titleImgFile = titleImgFile;
    }

    public void setTitleImgFileFileName(String titleImgFileFileName) {
        this.titleImgFileFileName = titleImgFileFileName;
    }

    @Action(value = "promotionAction_save", results = {@Result(name = "save_success",
            location = "/pages/take_delivery/promotion.html", type = "redirect")})
    public String save() {

        Promotion promotion = getModel();

        try {
            // 指定保存图片的文件夹
            String dirPath = "/upload";
            // 获取保存图片的文件夹的绝对磁盘路径
            ServletContext servletContext = ServletActionContext.getServletContext();
            String dirRealPath = servletContext.getRealPath(dirPath);

            // 获取文件名后缀
            String suffix = titleImgFileFileName.substring(titleImgFileFileName.lastIndexOf("."));
            // 使用uuid生成新文件名
            String fileName = UUID.randomUUID().toString().replaceAll("-", "") + suffix;
            File destFile = new File(dirRealPath + "/" + fileName);

            // 保存文件
            FileUtils.copyFile(titleImgFile, destFile);
            
            promotion.setTitleImg("upload/"+fileName);

            System.out.println(promotion.getTitleImg()+"------1111111111111111111");
        } catch (IOException e) {

            e.printStackTrace();
            promotion.setTitleImg(null);
        }
        
        promotion.setStatus("1");

        promotionService.save(getModel());
        return Constant.SAVE_SUCCESS;
    }
    
    @Action(value = "promotionAction_findByPage")
    public String findByPage() throws IOException {
        
        Pageable pageable = new PageRequest(page - 1, rows);

        Page<Promotion> page = promotionService.findAll(pageable);

        page2json(page, null);
        return NONE;

    }


}
