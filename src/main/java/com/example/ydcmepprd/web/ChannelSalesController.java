package com.example.ydcmepprd.web;


import com.example.ydcmepprd.pojo.repVo.Result;
import com.example.ydcmepprd.service.ChannelSalesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.logstash.logback.encoder.org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDate;

@CrossOrigin
@RestController
@Api(value = "渠道销售接口",tags = "渠道销售接口")
@RequestMapping("/api/ChannelSales")
public class ChannelSalesController {

    @Resource
    private ChannelSalesService channelSalesService;

    private String yearDefault = String.valueOf(LocalDate.now().getYear());

    @ApiOperation("根据年查询信息接口")
    @GetMapping("/findAllByYear")
    public Result findAllByYear(String year) {
        return Result.success(channelSalesService.findAllByYear(StringUtils.isBlank(year)? yearDefault :year));
    }

    @ApiOperation("根据年查询信息接口")
    @GetMapping("/getSecondList")
    public Result getSecondList(String year) {
        return Result.success(channelSalesService.getSecondList(StringUtils.isBlank(year)? yearDefault :year));
    }

    @ApiOperation("饼状图")
    @GetMapping("/proportion")
    public Result proportion() {
        return Result.success(channelSalesService.proportion());
    }

    /**
     * @description:TOP20总和占总销售额的比例
     * @author yaolewei
     * @date 2019年9月5日17:32:37
     * param:AnnualReturnedMoneyVo
     */
    @ApiOperation("二级页面完成率数据接口,TOP20所占总数比例")
    @GetMapping("/percentageComplete")
    public  Result percentageComplete(String year) {
        return Result.success(channelSalesService.percentageComplete((StringUtils.isBlank(year)? yearDefault :year)));
    }
}
