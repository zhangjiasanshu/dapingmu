package com.example.ydcmepprd.api;

import com.example.ydcmepprd.api.thread.DeptSAPThread;
import com.example.ydcmepprd.contant.otherEnum.CodeTableEnum;
import com.example.ydcmepprd.contant.otherEnum.DeptSAPMdxEnum;
import com.example.ydcmepprd.pojo.entity.*;
import com.example.ydcmepprd.service.*;
import com.example.ydcmepprd.utils.JsonToDaoUtils;
import com.example.ydcmepprd.utils.JsonUtils;
import com.example.ydcmepprd.utils.MyStringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.example.ydcmepprd.utils.XmlUtil.xml2JSON;

/**
 * @description:每日数据查询定时器
 * @author yaolewei
 * @date 2019年9月5日17:32:37
 * param:
 */
@Slf4j
@Service
public class CxfScheduleApi {

    /**更新去年数据的最大日期*/
    @Value("${ydcmepprd.updateMaxDay}")
    private Integer updateMaxDay;

    @Resource
    private CodeTableService codeTableService;

    /**年度回款SAP请求MDX语句*/
    private static final String ARMMDX = "SELECT NON EMPTY{[Measures].[00B9BRYDHQFBPRIN4HJ7T24N4],[Measures].[00B9BRYDHQFBPRIN4HJ7T2AYO],[Measures].[00B9BRYDHQFBPRIN4HJ7T2HA8],[Measures]." +
            "" +
            "[00B9BRYDHQFBPRIN4HJ7T2NLS]}ON COLUMNS, NON EMPTY [0CALMONTH].Members ON ROWS FROM [FMYDXS01/ZYDCM_XSHZB_01_HK]";
    /**销售渠道*/
    private static final String CSMDX = "SELECT NON EMPTY{[Measures].[00B9BRYDHQFBPT0V1PZ9JR0FY],[Measures]. [00B9BRYDHQFBPT0V1PZ9JSLBY]," +
            "[Measures].[00B9BRYDHQFBPT0V1PZ9JS8OU],[Measures].[00B9BRYDHQFBPT0V1PZ9JSF0E]}ON COLUMNS,NON EMPTY [0CUSTOMER__0NIELSEN_ID].Members ON ROWS FROM [FMYDXS01/ZYDCM_XSHZB_01_KHSX_DP]";
    /**年度在途*/
    private static final String ATMDX = "SELECT NON EMPTY { [Measures].[00B9BRYDHQFBPRINT8823N0GL], [Measures].[00B9BRYDHQFBPRINT8823ND3P], " +
            "[Measures].[00B9BRYDHQF9ZIIPEP0P5H8SD], [Measures].[00B9BRYDHQFBPRINT8823NPQT] } ON COLUMNS, NON EMPTY [0FISCYEAR].Members ON ROWS FROM [FMYDXS01/ZYDCM_XSHZB_01_DP]";
    /**图示top20*/
    private static final String BOOKTOP20MDX = "SELECT NON EMPTY {[Measures].[00B9BRYDHQF9ZC4KPN9KH5HRG]} ON COLUMNS, NON EMPTY TopCount" +
            "( { [0MATERIAL].Members },21,[Measures].[00B9BRYDHQF9ZC4KPN9KH5HRG]) ON ROWS FROM [FMYDXS01/ZYDCM_XSHZB_01_1_02_BO]";
    /**年度销售实洋*/
    private static final String SDPMDX = "SELECT NON EMPTY{[Measures].[00B9BRYDHQFBPRICE6RP063LV],[Measures].[00B9BRYDHQFBPRICE6RP069XF]," +
            "[Measures].[00B9BRYDHQFBPRICE6RP06G8Z],[Measures].[00B9BRYDHQFBPRICE6RP075J7],[Measures].[00B9BRYDHQFBPRICE6RP07BUR]," +
            "[Measures].[00B9BRYDHQFBPRICE6RP07UTF],[Measures].[00B9BRYDHQFBPRICE6RP08DS3],[Measures].[00B9BRYDHQFBPRICE6RP07I6B],[Measures]." +
            "[00B9BRYDHQFBPRICE6RP0814Z]}ON COLUMNS,NON EMPTY [0CALMONTH].Members ON ROWS FROM [FMYDXS01/ZYDCM_XSHZB_01_NY]";
    /**责编*/
    private static final String EEMDX = "SELECT NON EMPTY {[Measures].[00B9BRYDHQF9Z9Y192Y7DN8J8]} ON COLUMNS,NON EMPTY TopCount" +
            "({[0MATERIAL__Z_AR_ZB].Members },21,[Measures].[00B9BRYDHQF9Z9Y192Y7DN8J8]) ON ROWS FROM [FMYDHZ01/ZBO_YDCM_ZBPM]";
    /**省份销售*/
    private static final String PSMDX = "SELECT NON EMPTY {[Measures].[00B9BRYDHQF9ZIJ7RLHMEKP6Q], [Measures]." +
            "[00B9BRYDHQF9ZIJ7RLHMELR42] } ON COLUMNS, NON EMPTY [0CUSTOMER__0REGION].Members ON ROWS FROM [FMYDXS01/ZYDCM_XSHZB_01_KH_DP] WHERE {[0FISCYEAR]";
    /**社存码洋*/
    private static final String SCMYMDX = "SELECT NON EMPTY {[Measures].[00B9BRYDHQFBTTKUQE00OD7HW]}ON ROWS FROM [FAYDKC01/ZYDCM_SCMY_DP]";
    /**回款TOP20*/
    private static final String RETURN_TOP20 = "SELECT NON EMPTY{[Measures].[00B9BRYDHQF9ZMH70KJPRVPS5],[Measures]." +
            "[00B9BRYDHQF9ZMH70KJPRVW3P],[Measures].[00B9BRYDHQF9ZMH70KJPRW2F9],[Measures].[00B9BRYDHQF9ZMH70KJPRW8QT]}ON" +
            " COLUMNS,NON EMPTY TopCount ({[0CUSTOMER].Members},21,[Measures].[00B9BRYDHQF9ZMH70KJPRVPS5] ) ON ROWS FROM [FMYDXS01/ZYDCM_XSHZB_01_LLS_DP]";
    /**库存数据*/
    private static final String INVENTORY_DETAIL = "";
    /***/
    private static final String DEPT_YEAR_MONTH = "所有 日历年/月";
    /**报文返回基础路径*/
    private static final String BASE_PATH ="Envelope/Body/ExecuteResponse/return/root";


    @Autowired
    private AnnualReturnedMoneyService annualReturnedMoneyService;
    @Autowired
    private ChannelSalesService channelSalesService;
    @Autowired
    private AnnualTransitService annualTransitService ;
    @Autowired
    private BookTop20Service bookTop20Service;
    @Autowired
    private DeptAnalysisService deptAnalysisService;
    @Autowired
    private SalesDisPriceService salesDisPriceService;
    @Autowired
    private ExecutiveEditorService executiveEditorService;
    @Autowired
    private WapRequestClient wapRequestClient;
    @Autowired
    private DeptSAPThread deptSAPThread;
    @Autowired
    private ProvinceSalesService provinceSalesService;
    @Autowired
    private InventoryService inventoryService;
    @Resource
    private AnnualReturnedMoneyTop20Service annualReturnedMoneyTop20Service;
    @Resource
    private InventoryDetailService inventoryDetailService;


    /**
     * @description:年度回款定时任务
     * @author yaolewei
     * @date 2019年9月5日16:51:17
     * param:
     */
    @Scheduled(cron = "0 0 5 * * ?")
    public void updateAnnualReturnedMoney(){
        try{
            LocalDateTime startTime = LocalDateTime.now();
            log.info("年度回款定时获取SAP报文,执行开始时间为[{}]",startTime);
            String clientXml = wapRequestClient.client(ARMMDX);
            List<AnnReturnMoneyEntity> annReturnMoneyList = JsonToDaoUtils.arDateFormat(JsonUtils.findData(BASE_PATH, xml2JSON(clientXml.getBytes())));
            //年度回款
            LocalDateTime endTime = LocalDateTime.now();
            log.info("年度回款定时获取SAP报文执行成功,结束时间为[{}],耗时[{}]",endTime,Duration.between(startTime,endTime).toMillis());
            annualReturnedMoneyService.updateMonthModel(annReturnMoneyList);
        }catch (Exception e){
            log.error("年度回款定时任务执行失败,执行时间[{}],失败原因[{}]", LocalDateTime.now(),e);
        }
    }
    /**
     * @description:渠道销售汇总定时任务
     * @author yaolewei
     * @date 2019年9月5日16:51:17
     * param:
     */
    @Scheduled(cron = "0 5 5 * * ?")
    public void updateChannelSales() {
        try{
            LocalDateTime startTime = LocalDateTime.now();
            log.info("渠道销售定时获取SAP报文,执行开始时间为[{}]",startTime);
            String clientXml = wapRequestClient.client(CSMDX);
            List<ChannelSalesEntity> channelSalesEntityList = JsonToDaoUtils.csDateFormat(JsonUtils.findData(BASE_PATH, xml2JSON(clientXml.getBytes())));
            LocalDateTime endTime = LocalDateTime.now();
            log.info("渠道销售定时获取SAP报文,执行成功,结束时间为[{}],耗时:[{}]",endTime,Duration.between(startTime,endTime).toMillis());
            //解析返回报文转换次集合
            channelSalesService.updateMonthModel(channelSalesEntityList);
        }catch (Exception e){
            log.error("渠道销售定时任务执行失败,执行时间[{}],失败原因[{}]",LocalDateTime.now(),e);
        }
    }
    /**
     * @description:年度在途定时任务
     * @author yaolewei
     * @date 2019年9月5日16:51:17
     * param:
     */
    @Scheduled(cron = "0 10 5 * * ?")
    public void updateAnnualTransit(){
        try{
            LocalDateTime startTime = LocalDateTime.now();
            log.info("年度在途定时获取SAP报文,执行开始时间为[{}]",startTime);
            String clientXml = wapRequestClient.client(ATMDX);
            List<AnnualTransitEntity> annualTransitDaoList = JsonToDaoUtils.atDateFormat(JsonUtils.findData(BASE_PATH, xml2JSON(clientXml.getBytes())));
            LocalDateTime endTime = LocalDateTime.now();
            log.info("年度在途定时获取SAP报文,执行完成,结束时间为[{}],耗时:[{}]",endTime,Duration.between(startTime,endTime).toMillis());
            //解析返回报文转换次集合
            annualTransitService.updateMonthModel(annualTransitDaoList);
        }catch (Exception e){
            log.error("年度在途定时任务执行失败,执行时间[{}],失败原因[{}]",LocalDateTime.now(),e);
        }
    }
    /**
     * @description:图书top20定时任务
     * @author yaolewei
     * @date 2019年9月5日16:51:17
     * param:
     */
    @Scheduled(cron = "0 15 5 * * ?")
    public void updateBootTop20() {
        try{
            LocalDateTime startTime = LocalDateTime.now();
            log.info("图书TOP20定时获取SAP报文,执行开始时间为[{}]",startTime);
            String clientXml = wapRequestClient.client(BOOKTOP20MDX);
            List<BookTop20Entity> bootTop20List = JsonToDaoUtils.b20DateFormat(JsonUtils.findData(BASE_PATH, xml2JSON(clientXml.getBytes())));
            //解析返回报文转换次集合
            LocalDateTime endTime = LocalDateTime.now();
            bookTop20Service.updateMonthModel(bootTop20List);
            log.info("图书TOP20定时获取SAP报文,执行结束时间为[{}],耗时[{}]毫秒",endTime, Duration.between(startTime,endTime).toMillis());
        }catch (Exception e){
            log.error("图书TOP20定时任务执行失败,执行时间[{}],失败原因[{}]",LocalDateTime.now(),e);
        }
    }
    /**
     * @description:部门分析定时任务
     * @author yaolewei
     * @date 2019年9月5日16:51:17
     * param:
     */
    @Scheduled(cron = "0 20 5 * * ?")
    public void updateDeptAnalysis() {
        try{
            List<DeptAnalysisEntity> deptAnalysisEntityList = new ArrayList<>();
            LocalDateTime startTime = LocalDateTime.now();
            log.info("部门分析定时获取SAP报文,执行开始时间为[{}]",startTime);
            CountDownLatch countDownLatch = new CountDownLatch(DeptSAPMdxEnum.values().length);
            ArrayList<Future<List<DeptAnalysisEntity>>> futures = new ArrayList<>();
            for (DeptSAPMdxEnum deptSAPMdxEnum:DeptSAPMdxEnum.values()){
                futures.add(deptSAPThread.deptMdxRequest(deptSAPMdxEnum)) ;
                countDownLatch.countDown();
            }
            countDownLatch.await(10, TimeUnit.MINUTES);
            for (Future<List<DeptAnalysisEntity>> future:futures){
                deptAnalysisEntityList.addAll(future.get());
            }
            LocalDateTime endTime = LocalDateTime.now();
            log.info("部门分析定时获取SAP报文,对接SAP执行成功,结束时间为[{}],耗时[{}]毫秒",endTime,Duration.between(startTime,endTime).toMillis());
            //解析返回报文转换次集合
            //剔除无用数据(关键字-所有)
            List<DeptAnalysisEntity> collect = deptAnalysisEntityList.stream().filter(a -> !a.getYearMonth().equals(DEPT_YEAR_MONTH)).collect(Collectors.toList());
            deptAnalysisService.updateMonthModel(collect);
        }catch (Exception e){
            log.error("图书TOP20定时任务执行失败,执行时间[{}],失败原因[{}]",LocalDateTime.now(),e);
        }
    }

    /**
     * @description:年度销售实洋定时任务
     * @author yaolewei
     * @date 2019年9月5日16:51:17
     * param:
     */
    @Scheduled(cron = "0 25 5 * * ?")
    public void updateSalesDisPrice() {
        try {
            LocalDateTime startTime = LocalDateTime.now();
            log.info("年度销售实洋定时获取SAP报文,开始时间为[{}]",startTime);
            String clientXml = wapRequestClient.client(SDPMDX);
            List<SalesDisPriceEntity> salesDisPriceEntityList = JsonToDaoUtils.sdDateFormat(JsonUtils.findData(BASE_PATH, xml2JSON(clientXml.getBytes())));
            LocalDateTime sendTime = LocalDateTime.now();
            log.info("年度销售实洋定时获取SAP报文,结束时间为[{}],耗时:[{}]",sendTime,Duration.between(startTime,sendTime).toMillis());
            //解析返回报文转换次集合
            salesDisPriceService.updateMonthModel(salesDisPriceEntityList);
        } catch (Exception e) {
            log.error("年度销售实洋定时任务执行失败,失败原因为[{}]",e);
        }
    }
    /**
     * @description:责编定时任务
     * @author yaolewei
     * @date 2019年9月5日16:51:17
     * param:
     */
    @Scheduled(cron = "0 40 5 * * ?")
    public void updateExecutiveEditor() {
        try {
            LocalDateTime startTime = LocalDateTime.now();
            log.info("责编定时获取SAP信息执行开始时间为[{}]",startTime);
            StringBuilder newEEMDX = new StringBuilder(EEMDX);
            newEEMDX.append("WHERE { [0FISCYEAR].[K4").append(LocalDate.now().getYear()).append("] }");
            String clientXml = wapRequestClient.client(newEEMDX.toString());
            List<ExecutiveEditorEntity> executiveEditorEntityList = JsonToDaoUtils.eeDateFormat(JsonUtils.findData(BASE_PATH, xml2JSON(clientXml.getBytes())));
            LocalDateTime endTime = LocalDateTime.now();
            log.info("责编定时获取SAP信息,完成时间为[{}],耗时:[{}]",endTime,Duration.between(startTime,endTime).toMillis());
            //解析返回报文转换次集合
            executiveEditorService.updateMonthModel(executiveEditorEntityList);
        } catch (Exception e) {
            log.error("责编定时任务执行失败,失败原因为[{}]",e);
        }
    }
    /**
     * @description:全国省份销售信息定时任务
     * @author yaolewei
     * @date 2019年9月5日16:51:17
     * param:
     */
    @Scheduled(cron = "0 0/10 * * * ?")
    public void updateProvinceSales() {
        try{
            LocalDateTime startTime = LocalDateTime.now();
            log.info("全国省份销售信息定时获取SAP报文,任务执行开始时间为[{}]",startTime);
            Integer thisYear = LocalDate.now().getYear();
            StringBuilder thisPSMDXSB = new StringBuilder().append(PSMDX).append(".[K4").append(thisYear).append("]}");
            String clientXml = wapRequestClient.client(thisPSMDXSB.toString());
            List<ProvinceSalesEntity> annReturnMoneyList = JsonToDaoUtils.psDateFormat(JsonUtils.findData(BASE_PATH, xml2JSON(clientXml.getBytes())));
            //每年一月1日至一月5日更新去年数据
            if (startTime.getMonthValue()==1 && startTime.getDayOfMonth()<updateMaxDay){
                //查询当年信息
                StringBuilder lastPSMDXSB = new StringBuilder().append(PSMDX).append(".[K4").append(thisYear-1).append("]}");
                String lastClientXml = wapRequestClient.client(lastPSMDXSB.toString());
                List<ProvinceSalesEntity> lastAnnReturnMoneyList = JsonToDaoUtils.psDateFormat(JsonUtils.findData(BASE_PATH, xml2JSON(lastClientXml.getBytes())));
                lastAnnReturnMoneyList.forEach(a->a.setYearMonth(String.valueOf(thisYear-1)));
                provinceSalesService.updateMonthModel(lastAnnReturnMoneyList,thisYear-1);
            }
            LocalDateTime endTime = LocalDateTime.now();
            log.info("全国省份销售信息定时获取SAP报文,任务执行结束时间为[{}],耗时:[{}]",endTime,Duration.between(startTime,endTime).toMillis());
            //年度回款
            provinceSalesService.updateMonthModel(annReturnMoneyList,thisYear);
        }catch (Exception e){
            log.error("年度回款定时任务执行失败,执行时间[{}],失败原因[{}]", LocalDateTime.now(),e);
        }
    }


    /**
     * @description:社存码洋信息定时任务
     * @author yaolewei
     * @date 2019年9月5日16:51:17
     * param:
     */
    @Scheduled(cron = "0 50 5 * * ?")
    public void updateInventory() {
        try{
            LocalDateTime startTime = LocalDateTime.now();
            log.info("社存码洋(库存)定时获取SAP信息,任务执行开始时间为[{}]",startTime);
            String clientXml = wapRequestClient.client(SCMYMDX);
            List<InventoryEntity> inventoryEntityList = JsonToDaoUtils.inDateFormat(JsonUtils.findData(BASE_PATH, xml2JSON(clientXml.getBytes())));
            LocalDateTime endTime = LocalDateTime.now();
            log.info("社存码洋(库存)定时获取SAP信息,任务执行结束时间为[{}],耗时:[{}]",endTime,Duration.between(startTime,endTime).toMillis());
            inventoryEntityList.stream().forEach(a->a.setCreatTime(LocalDateTime.now()));
            //年度回款
            inventoryService.updateMonthModel(inventoryEntityList);
        }catch (Exception e){
            log.error("社存码洋(库存定时任务执行失败,执行时间[{}],失败原因[{}]", LocalDateTime.now(),e);
        }
    }


    /**
     * @description:回款TOP20
     * @author yaolewei
     * @date 2019年9月5日16:51:17
     * param:
     */
    @Scheduled(cron = "0 55 5 * * ?")
    public void returnTop20() {
        try{
            LocalDateTime startTime = LocalDateTime.now();
            log.info("汇款Top20定时获取SAP信息,任务执行开始时间为[{}]",startTime);
            StringBuilder responseXml = new StringBuilder(RETURN_TOP20);
            responseXml.append(" WHERE { [0FISCYEAR].[K4").append(LocalDate.now().getYear()).append("] }");
            String clientXml = wapRequestClient.client(responseXml.toString());
            List<AnnReturnMoneyTop20Entity> returnTopList = JsonToDaoUtils.rTOP20DateFormat(JsonUtils.findData(BASE_PATH, xml2JSON(clientXml.getBytes())));
            LocalDateTime endTime = LocalDateTime.now();
            log.info("社存码洋(库存)定时获取SAP信息,任务执行结束时间为[{}],耗时:[{}]",endTime,Duration.between(startTime,endTime).toMillis());
            returnTopList.stream().forEach(a->a.setCreatTime(LocalDateTime.now()));
            //年度回款
            annualReturnedMoneyTop20Service.updateMonthModel(returnTopList);
        }catch (Exception e){
            log.error("社存码洋(库存定时任务执行失败,执行时间[{}],失败原因[{}]", LocalDateTime.now(),e);
        }
    }


    /**
     * @description:库存数据
     * @author yaolewei
     * @date 2019年9月5日16:51:17
     * param:
     */
//    @Scheduled(cron = "0 45 5 * * ?")
    public void inventoryDetail() {
        try{
            LocalDateTime startTime = LocalDateTime.now();
            log.info("库存数据定时获取SAP信息,任务执行开始时间为[{}]",startTime);
            StringBuilder responseXml = new StringBuilder(INVENTORY_DETAIL);
            String clientXml = wapRequestClient.client(responseXml.toString());
            List<InventoryDetailEntity> inventoryDetailEntityList = JsonToDaoUtils.idTOP20DateFormat(JsonUtils.findData(BASE_PATH, xml2JSON(clientXml.getBytes())));
            LocalDateTime endTime = LocalDateTime.now();
            log.info("社存码洋(库存)定时获取SAP信息,任务执行结束时间为[{}],耗时:[{}]",endTime,Duration.between(startTime,endTime).toMillis());
            inventoryDetailEntityList.stream().forEach(a->a.setCreatTime(LocalDateTime.now()));
            //年度回款
            inventoryDetailService.updateMonthModel(inventoryDetailEntityList);
        }catch (Exception e){
            log.error("社存码洋(库存定时任务执行失败,执行时间[{}],失败原因[{}]", LocalDateTime.now(),e);
        }
    }

    /**
     * @description:判断是否开启
     * @author: yaolewei
     * @date:2019年12月24日14:21:06
     * param:
     */
    public Boolean timingSwitch() {
        String valueByKey = codeTableService.findValueByKey(CodeTableEnum.TIMING_SWITCH.getCode());
        return !MyStringUtil.isEmpty(valueByKey) && valueByKey.equals("1");
    }


}