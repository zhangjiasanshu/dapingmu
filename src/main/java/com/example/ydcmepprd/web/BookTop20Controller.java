package com.example.ydcmepprd.web;

import com.example.ydcmepprd.pojo.repVo.Result;
import com.example.ydcmepprd.service.BookTop20Service;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.logstash.logback.encoder.org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@CrossOrigin
@Api(value = "图书TOP20接口",tags = "图书TOP20接口")
@RestController
@RequestMapping("/api/book")
public class BookTop20Controller {

    private String defaultYear = String.valueOf(LocalDate.now().getYear());
    @Autowired
    private BookTop20Service bookTop20Service;
    /**
     * @description:根据年信息获取当年所有销售信息
     * @author yaolewei
     * @date 2019年9月5日17:32:37
     * param:AnnualReturnedMoneyVo
     */
    @ApiOperation("根据年查询信息接口")
    @GetMapping("/findBaseModelByYear")
    public Result findBaseModelByYear(String year) {
        return Result.success(bookTop20Service.findBaseModelByYear(StringUtils.isBlank(year)? defaultYear :year));
    }

    /**
     * @description:二级页面表格
     * @author yaolewei
     * @date 2019年9月5日17:32:37
     * param:AnnualReturnedMoneyVo
     */
    @ApiOperation("图书TOP20二级页面")
    @GetMapping("/getSecSecondList")
    public Result getSecSecondList(String year) {
        return  Result.success(bookTop20Service.getSecSecondList(StringUtils.isBlank(year)? defaultYear :year));
    }
    /**
     * @description:TOP20总和占总销售额的比例
     * @author yaolewei
     * @date 2019年9月5日17:32:37
     * param:AnnualReturnedMoneyVo
     */
    @ApiOperation("二级页面饼状图数据接口,TOP20所占总数比例")
    @GetMapping("/proportion")
    public  Result proportion(String year) {
        return Result.success(bookTop20Service.proportion((StringUtils.isBlank(year)? defaultYear :year)));
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
        return Result.success(bookTop20Service.percentageComplete((StringUtils.isBlank(year)? defaultYear :year)));
    }
}
