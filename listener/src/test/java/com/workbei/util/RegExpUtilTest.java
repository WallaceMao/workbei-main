package com.workbei.util;

import com.workbei.BaseUnitTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-context.xml"})
public class RegExpUtilTest {

    @Test
    public void testReplacePathVariable() {
        Date now = new Date();
        final String domain = "http://www.workbei.com/";
        final String key1 = "abc_" + now.getTime();
        final String val1 = "abc_val_" + now.getTime();
        final String key2 = "xyz_" + now.getTime();
        final String val2 = "xyz_val_" + now.getTime();
        final String orgPath = domain + "{" + key1 + "}/{" + key2 + "}";
        final String targetPath = domain+ val1 + "/" + val2;
        Map<String, Object> params = new HashMap<>();
        params.put(key1, val1);
        params.put(key2, val2);
        String result = RegExpUtil.replacePathVariable(orgPath, params);
        assertThat(result).isEqualTo(targetPath);

        // 未发生改变时，还是返回原样
        String noChangePath = RegExpUtil.replacePathVariable(targetPath, params);
        assertThat(noChangePath).isEqualTo(targetPath);
    }
}