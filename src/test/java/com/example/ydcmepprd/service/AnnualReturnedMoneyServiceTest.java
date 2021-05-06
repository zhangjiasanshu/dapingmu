package com.example.ydcmepprd.service;

import com.example.ydcmepprd.pojo.entity.AnnReturnMoneyEntity;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AnnualReturnedMoneyServiceTest {
    @Resource
    private AnnualReturnedMoneyService annualReturnedMoneyService;

    @Test
    public void findOneyearModelTest() {
        List<AnnReturnMoneyEntity> oneyearModel = annualReturnedMoneyService.findOneyearModel();
        Assert.assertNull("返回的空的",oneyearModel);
    }
}
