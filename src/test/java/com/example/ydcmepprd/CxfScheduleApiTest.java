package com.example.ydcmepprd;

import com.example.ydcmepprd.api.CxfScheduleApi;
import com.example.ydcmepprd.api.WapRequestClient;
import com.example.ydcmepprd.utils.JsonUtils;
import com.example.ydcmepprd.utils.TestUtils;
import org.jdom2.JDOMException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;

import static com.example.ydcmepprd.utils.XmlUtil.xml2JSON;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CxfScheduleApiTest {

    @Autowired
    private CxfScheduleApi cxfScheduleApi;
    @Resource
    private TestUtils testUtils;
    @Test
    public void testUpdateAnnualReturnedMoney(){
        cxfScheduleApi.updateAnnualReturnedMoney();
    }

    @Test
    public void testYpdateSalesDisPrice(){
        cxfScheduleApi.updateSalesDisPrice();
    }

    @Test
    public void testupdateProvinceSales(){
        cxfScheduleApi.updateProvinceSales();
    }

    @Test
    public void testUpdateAnnualTransit(){
        cxfScheduleApi.updateAnnualTransit();
    }

    @Test
    public void testUpdateExecutiveEditor(){
        cxfScheduleApi.updateExecutiveEditor();
    }

    @Test
    public void testUpdateBootTop20(){
        cxfScheduleApi.updateBootTop20();
    }
    @Test
    public void testUpdateDeptAnalysis(){
        cxfScheduleApi.updateDeptAnalysis();
    }
    @Test
    public void testinventoryDetail(){
        cxfScheduleApi.inventoryDetail();
    }
    @Test
    public void testUpdateProvinceSales(){
        cxfScheduleApi.updateProvinceSales();
    }
    @Test
    public void testUpdateChannelSales(){
        cxfScheduleApi.updateChannelSales();
    }
    /**
     * @description:库存
     * @author: yaolewei
     * @date:2019年12月3日14:57:40
     * param:
     */
    @Test
    public void testUpdateInventory(){
        cxfScheduleApi.updateInventory();
    }


    @Test
    public void testReturnTop20(){
        cxfScheduleApi.returnTop20();
    }

    @Autowired
    private WapRequestClient wapRequestClient;
    private static final String BASE_PATH ="Envelope/Body/ExecuteResponse/return/root";
    private static final String ARMMDX = "SELECT NON EMPTY{[Measures].[00B9BRYDHQFBPRIN4HJ7T24N4],[Measures].[00B9BRYDHQFBPRIN4HJ7T2AYO],[Measures].[00B9BRYDHQFBPRIN4HJ7T2HA8],[Measures].[00B9BRYDHQFBPRIN4HJ7T2NLS]}ON COLUMNS, NON EMPTY [0CALMONTH].Members ON ROWS FROM [FMYDXS01/ZYDCM_XSHZB_01_HK]";

    @Test
    public void testTwo() throws JDOMException, IOException {
        String clientXml = wapRequestClient.client(ARMMDX);
        System.out.println(JsonUtils.findData(BASE_PATH, xml2JSON(clientXml.getBytes())));;
    }


    @Test
    public  void testThriead() {
        CountDownLatch countDownLatch = new CountDownLatch(10);
        StringBuilder stringBuilder = new StringBuilder();
        ArrayList<Future<String>> futures = new ArrayList<>();
        for (int i =0;i<10;i++){
            futures.add(testUtils.thread(i));
            countDownLatch.countDown();
        }
        System.out.println("最后总个数"+futures.size());
        Assert.assertNull("kongde",futures);

    }



}
