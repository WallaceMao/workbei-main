package com.workbei;

import com.workbei.constant.TestConstant;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Wallace Mao
 * Date: 2018-11-30 10:35
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-context.xml"})
public class BaseUnitTest {
    @Test
    public void test(){}

    protected void checkDateCloseToNow(Date... checkArray){
        Date now = new Date();
        for(int i = 0; i < checkArray.length; i++){
            assertThat(checkArray[i]).as("检查checkDateCloseToNow")
                    .isCloseTo(now, TestConstant.DEFAULT_DATE_DELTA);
        }
    }
}
