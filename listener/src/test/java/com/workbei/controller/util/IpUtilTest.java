package com.workbei.controller.util;

import com.workbei.BaseUnitTest;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional(transactionManager = "transactionManager")
@Rollback
public class IpUtilTest extends BaseUnitTest {

    @Test
    public void getRequestIp() {
        HttpServletRequest mockRequest = Mockito.mock(HttpServletRequest.class);
        final String address1 = "127.0.0.1";
        final String address2 = "192.168.0.1";
        Mockito.when(mockRequest.getHeader("X-FORWARDED-FOR")).thenReturn(address1);
        assertThat(IpUtil.getRequestIp(mockRequest)).isEqualTo(address1);

        Mockito.reset(mockRequest);
        Mockito.when(mockRequest.getHeader("X-FORWARDED-FOR")).thenReturn(null);
        Mockito.when(mockRequest.getRemoteAddr()).thenReturn(address2);
        assertThat(IpUtil.getRequestIp(mockRequest)).isEqualTo(address2);

    }

    @Test
    public void checkIpInSubNetwork() {
        final String EMPTY_STRING = "";
        final String ALL_NETWORK = "0.0.0.0";
        final String SINGLE_IP = "172.16.5.6";
        final String SINGLE_IP_2 = "172.18.0.1";
        final String SINGLE_IP_3 = "192.18.0.1";
        final String SINGLE_NETWORK = "172.16.5.6";
        final String SUB_NETWORK = "172.16.0.0/16";
        final String MULTI_NETWORK = "172.16.0.0/16,,172.18.0.1";
        assertThat(IpUtil.checkIpInSubNetwork(null, null)).isFalse();
        assertThat(IpUtil.checkIpInSubNetwork(null, EMPTY_STRING)).isFalse();
        assertThat(IpUtil.checkIpInSubNetwork(EMPTY_STRING, null)).isFalse();
        assertThat(IpUtil.checkIpInSubNetwork(EMPTY_STRING, EMPTY_STRING)).isFalse();
        assertThatThrownBy(() -> IpUtil.checkIpInSubNetwork("RANDOM SUBNEt", "RANDOM IP"))
                .isInstanceOf(IllegalArgumentException.class);
        assertThat(IpUtil.checkIpInSubNetwork(ALL_NETWORK, "any string")).isTrue();
        assertThat(IpUtil.checkIpInSubNetwork(SUB_NETWORK, SINGLE_IP)).isTrue();
        assertThat(IpUtil.checkIpInSubNetwork(SUB_NETWORK, SINGLE_IP_2)).isFalse();
        assertThat(IpUtil.checkIpInSubNetwork(SINGLE_NETWORK, SINGLE_IP)).isTrue();
        assertThat(IpUtil.checkIpInSubNetwork(SINGLE_NETWORK, SINGLE_IP_2)).isFalse();
        assertThat(IpUtil.checkIpInSubNetwork(MULTI_NETWORK, SINGLE_IP_2)).isTrue();
        assertThat(IpUtil.checkIpInSubNetwork(MULTI_NETWORK, SINGLE_IP_3)).isFalse();
    }
}