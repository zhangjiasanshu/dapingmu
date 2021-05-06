package com.example.ydcmepprd.web;

import com.example.ydcmepprd.pojo.repVo.Result;
import com.example.ydcmepprd.service.InventoryDetailService;
import com.example.ydcmepprd.utils.MyStringUtil;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDate;

@CrossOrigin
@RestController
@Api(value = "库存明细查询接口",tags = "库存明细查询接口")
@RequestMapping("/api/inventoryDetail")
public class InventoryDetailController {

    @Resource
    private InventoryDetailService inventoryDetailService;
    /**
     * @description:全量查询
     * @author: yaolewei
     * @date:date{time}
     * param:
     */
    @GetMapping("/getAll")
    public Result getAll(String year) {
        if (MyStringUtil.isEmpty(year)){
            year = LocalDate.now().getYear()+"";
        }
        System.err.println("year = " + year);
       return Result.success(inventoryDetailService.getAll(year)) ;
    }
}
