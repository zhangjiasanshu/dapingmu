package com.example.ydcmepprd.web;

import com.example.ydcmepprd.exeption.BusinessExeption;
import com.example.ydcmepprd.exeption.BusinessExeptionCodeEnum;
import com.example.ydcmepprd.pojo.bo.FileUpVo;
import com.example.ydcmepprd.service.FileUpService;
import com.example.ydcmepprd.utils.MyStringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @description:
 * @author: yaolewei
 * @date: 2019-09-18 10:13
 */
@Api(value = "poi",tags = "poi")
@RestController
@RequestMapping("/api")
public class FileUpController {

    @Resource
    private FileUpService fileUpService;

    @PostMapping("/annualReturnedMoneyFileUp")
    @ApiOperation("年度回款导入")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type",value = "type",paramType = "query",dataType = "Integer"),
            @ApiImplicitParam(name = "minLineNum",value = "minLineNum",paramType = "query",dataType = "Integer"),
            @ApiImplicitParam(name = "maxLineNum",value = "maxLineNum",paramType = "query",dataType = "Integer"),
            @ApiImplicitParam(name = "headRow",value = "headRow",paramType = "query",dataType = "Integer"),
            @ApiImplicitParam(name = "unlessRow",value = "unlessRow",paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "unlessLine",value = "unlessLine",paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "headMap",value = "headMap",paramType = "query",dataType = "Map<Integer,String>")
    })
    public void annualReturnedMoneyFileUp(@RequestParam("imgFile") MultipartFile file, FileUpVo fileUpVo) throws IOException {
        fileCheck(file);
        fileUpService.annualReturnedMoneyFileUp(file);
    }

    @PostMapping("/deptAnalysisFileUp")
    @ApiOperation("部门信息导入")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type",value = "type",paramType = "query",dataType = "Integer"),
            @ApiImplicitParam(name = "minLineNum",value = "minLineNum",paramType = "query",dataType = "Integer"),
            @ApiImplicitParam(name = "maxLineNum",value = "maxLineNum",paramType = "query",dataType = "Integer"),
            @ApiImplicitParam(name = "headRow",value = "headRow",paramType = "query",dataType = "Integer"),
            @ApiImplicitParam(name = "unlessRow",value = "unlessRow",paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "unlessLine",value = "unlessLine",paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "headMap",value = "headMap",paramType = "query",dataType = "Map<Integer,String>")
    })
    public void deptAnalysisFileUp(@RequestParam("imgFile") MultipartFile file, FileUpVo fileUpVo) throws IOException {
        fileCheck(file);
        fileUpService.deptAnalysisFileUp(file);
    }

    @PostMapping("/provinceSalesFileUp")
    @ApiOperation("省份导入")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type",value = "type",paramType = "query",dataType = "Integer"),
            @ApiImplicitParam(name = "minLineNum",value = "minLineNum",paramType = "query",dataType = "Integer"),
            @ApiImplicitParam(name = "maxLineNum",value = "maxLineNum",paramType = "query",dataType = "Integer"),
            @ApiImplicitParam(name = "headRow",value = "headRow",paramType = "query",dataType = "Integer"),
            @ApiImplicitParam(name = "unlessRow",value = "unlessRow",paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "unlessLine",value = "unlessLine",paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "headMap",value = "headMap",paramType = "query",dataType = "Map<Integer,String>")
    })
    public void provinceSalesFileUp(@RequestParam("imgFile") MultipartFile file, FileUpVo fileUpVo) throws IOException {
        fileCheck(file);
        fileUpService.provinceSalesFileUp(file);
    }


    @PostMapping("/SalesDisPriceFileUp")
    @ApiOperation("销售导入")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type",value = "type",paramType = "query",dataType = "Integer"),
            @ApiImplicitParam(name = "minLineNum",value = "minLineNum",paramType = "query",dataType = "Integer"),
            @ApiImplicitParam(name = "maxLineNum",value = "maxLineNum",paramType = "query",dataType = "Integer"),
            @ApiImplicitParam(name = "headRow",value = "headRow",paramType = "query",dataType = "Integer"),
            @ApiImplicitParam(name = "unlessRow",value = "unlessRow",paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "unlessLine",value = "unlessLine",paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "headMap",value = "headMap",paramType = "query",dataType = "Map<Integer,String>")
    })
    public void salesDisPriceFileUp(@RequestParam("imgFile") MultipartFile file, FileUpVo fileUpVo) throws IOException {
        fileCheck(file);
        fileUpService.salesDisPriceFileUp(file);
    }
    @PostMapping("/bookTOP20FileUp")
    @ApiOperation("图书TOP20导入")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "year",value = "year",paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "type",value = "type",paramType = "query",dataType = "Integer"),
            @ApiImplicitParam(name = "minLineNum",value = "minLineNum",paramType = "query",dataType = "Integer"),
            @ApiImplicitParam(name = "maxLineNum",value = "maxLineNum",paramType = "query",dataType = "Integer"),
            @ApiImplicitParam(name = "headRow",value = "headRow",paramType = "query",dataType = "Integer"),
            @ApiImplicitParam(name = "unlessRow",value = "unlessRow",paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "unlessLine",value = "unlessLine",paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "headMap",value = "headMap",paramType = "query",dataType = "Map<Integer,String>")
    })
    public void bookTOP20FileUp(@RequestParam("imgFile") MultipartFile file,String year, FileUpVo fileUpVo) throws IOException {
        fileCheck(file);
        if (MyStringUtil.isEmpty(year)){
            throw new BusinessExeption(BusinessExeptionCodeEnum.REQUEST_DATE_EXCEPTION,"导入数据所属年份不可为空");
        }
        fileUpService.bookTOP20FileUp(file,year);
    }
    @PostMapping("/executiveEditorFileUp")
    @ApiOperation("责编TOP20导入")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "year",value = "year",paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "type",value = "type",paramType = "query",dataType = "Integer"),
            @ApiImplicitParam(name = "minLineNum",value = "minLineNum",paramType = "query",dataType = "Integer"),
            @ApiImplicitParam(name = "maxLineNum",value = "maxLineNum",paramType = "query",dataType = "Integer"),
            @ApiImplicitParam(name = "headRow",value = "headRow",paramType = "query",dataType = "Integer"),
            @ApiImplicitParam(name = "unlessRow",value = "unlessRow",paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "unlessLine",value = "unlessLine",paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "headMap",value = "headMap",paramType = "query",dataType = "Map<Integer,String>")
    })
    public void executiveEditorFileUp(@RequestParam("imgFile") MultipartFile file,String year, FileUpVo fileUpVo) throws IOException {
        fileCheck(file);
        if (MyStringUtil.isEmpty(year)){
            throw new BusinessExeption(BusinessExeptionCodeEnum.REQUEST_DATE_EXCEPTION,"导入数据所属年份不可为空");
        }
        fileUpService.executiveEditorFileUp(file,year);
    }

    public void fileCheck(MultipartFile file) {
        if (file == null || file.isEmpty()){
           throw new BusinessExeption(BusinessExeptionCodeEnum.REQUEST_DATE_EXCEPTION,"文件不可为空");
        }
    }
    /**
     * @description:畅销书
     * @author: yaolewei
     * @date:2019年12月5日14:58:10
     * param:
     */
    @ApiOperation("畅销书")
    @PostMapping("/readSaleFileUp")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type",value = "type",paramType = "query",dataType = "Integer"),
            @ApiImplicitParam(name = "minLineNum",value = "minLineNum",paramType = "query",dataType = "Integer"),
            @ApiImplicitParam(name = "maxLineNum",value = "maxLineNum",paramType = "query",dataType = "Integer"),
            @ApiImplicitParam(name = "headRow",value = "headRow",paramType = "query",dataType = "Integer"),
            @ApiImplicitParam(name = "unlessRow",value = "unlessRow",paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "unlessLine",value = "unlessLine",paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "headMap",value = "headMap",paramType = "query",dataType = "Map<Integer,String>")
    })
    public void readSaleFileUp(@RequestParam("imFile")MultipartFile file,String year, FileUpVo fileUpVo) throws IOException {
        fileCheck(file);
        if (MyStringUtil.isEmpty(year)){
            throw new BusinessExeption(BusinessExeptionCodeEnum.REQUEST_DATE_EXCEPTION,"Excel文件录入所需年份不可为空");
        }
        fileUpService.readySalesFileUp(file,year);
    }

    /**
     * @description:图书明细
     * @author: yaolewei
     * @date:2019年11月18日09:59:23
     * param:
     */
    @ApiOperation("图书明细")
    @PostMapping("/bookDetailFileUP")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type",value = "type",paramType = "query",dataType = "Integer"),
            @ApiImplicitParam(name = "minLineNum",value = "minLineNum",paramType = "query",dataType = "Integer"),
            @ApiImplicitParam(name = "maxLineNum",value = "maxLineNum",paramType = "query",dataType = "Integer"),
            @ApiImplicitParam(name = "headRow",value = "headRow",paramType = "query",dataType = "Integer"),
            @ApiImplicitParam(name = "unlessRow",value = "unlessRow",paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "unlessLine",value = "unlessLine",paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "headMap",value = "headMap",paramType = "query",dataType = "Map<Integer,String>")
    })
    public void bookDetailFileUP(@RequestParam("imFile")MultipartFile file, FileUpVo fileUpVo) throws IOException {
        fileCheck(file);
        fileUpService.bookDetailFileUP(file);
    }

    /**
     * @description:库存
     * @author: yaolewei
     * @date:2019年11月18日09:59:23
     * param:
     */
    @ApiOperation("库存")
    @PostMapping("/inventoryDetailFileUP")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "year",value = "year",paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "type",value = "type",paramType = "query",dataType = "Integer"),
            @ApiImplicitParam(name = "minLineNum",value = "minLineNum",paramType = "query",dataType = "Integer"),
            @ApiImplicitParam(name = "maxLineNum",value = "maxLineNum",paramType = "query",dataType = "Integer"),
            @ApiImplicitParam(name = "headRow",value = "headRow",paramType = "query",dataType = "Integer"),
            @ApiImplicitParam(name = "unlessRow",value = "unlessRow",paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "unlessLine",value = "unlessLine",paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "headMap",value = "headMap",paramType = "query",dataType = "Map<Integer,String>")
    })
    public void inventoryDetailFileUP(@RequestParam("imFile")MultipartFile file, String year, FileUpVo fileUpVo) throws IOException {
        fileCheck(file);
        if (MyStringUtil.isEmpty(year)){
            throw new BusinessExeption(BusinessExeptionCodeEnum.REQUEST_DATE_EXCEPTION,"Excel文件录入所需年份不可为空");
        }
        fileUpService.inventoryDetailFileUP(file,year);
    }
}
