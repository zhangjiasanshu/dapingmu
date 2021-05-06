package com.example.ydcmepprd.web;

import com.example.ydcmepprd.pojo.repVo.Result;
import com.example.ydcmepprd.pojo.reqVo.BDPageQueryVo;
import com.example.ydcmepprd.service.BookDetailService;
import com.example.ydcmepprd.utils.PageUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@CrossOrigin
@RestController
@Api(value = "图书详情",tags = "图书详情")
@RequestMapping("/api/bookDetail")
public class BookDetailController {

    @Resource
    private BookDetailService bookDetailService;
    /**
     * @description:条件查询
     * @author: yaolewei
     * @date:2019年11月15日17:57:11
     * param:
     */
    @PostMapping("/getPageList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "querytime",value = "查询时间",paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "name",value = "图书名称",paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "executiveEditor",value = "责任编辑",paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "bookCentre",value = "图书中心",paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "currentPage",value = "当前页",paramType = "query",dataType = "Integer"),
            @ApiImplicitParam(name = "pageSize",value = "每页显示的总条数",paramType = "query",dataType = "Integer"),
            @ApiImplicitParam(name = "totalPage",value = "总页数",paramType = "query",dataType = "Integer"),
            @ApiImplicitParam(name = "label",value = "排序字段",paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "direction",value = "排序方式",paramType = "query",dataType = "String")
    })
    public Result getPageList(@RequestBody BDPageQueryVo bdPageQueryVo){
        PageUtil.pageChack(bdPageQueryVo);
        return Result.success(bookDetailService.getPageList(bdPageQueryVo));
    }



    /**
     * @description:条件查询
     * @author: yaolewei
     * @date:2019年11月15日17:57:11
     * param:
     */
    @PostMapping("/getEEPageList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "querytime",value = "查询时间",paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "name",value = "图书名称",paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "executiveEditor",value = "责任编辑",paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "bookCentre",value = "图书中心",paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "currentPage",value = "当前页",paramType = "query",dataType = "Integer"),
            @ApiImplicitParam(name = "pageSize",value = "每页显示的总条数",paramType = "query",dataType = "Integer"),
            @ApiImplicitParam(name = "totalPage",value = "总页数",paramType = "query",dataType = "Integer"),
            @ApiImplicitParam(name = "label",value = "排序字段",paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "direction",value = "排序方式",paramType = "query",dataType = "String")
    })
    public Result getEEPageList(@RequestBody BDPageQueryVo bdPageQueryVo) {
        PageUtil.pageChack(bdPageQueryVo);
        return Result.success(bookDetailService.getEEPageList(bdPageQueryVo));
    }
}
