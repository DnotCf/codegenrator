/*
package com.cmsiw;

import com.cmsiw.common.utils.security.config.AuthorizeConfigManager;
import com.cmsiw.common.utils.social.mobile.authentication.SmsCodeAuthenticationSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.servlet.http.HttpServletResponse;

*/
/**
 * @author tangs
 * @date 2019/9/9 15:53
 *//*

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthorizeConfigManager authorizeConfigManager;
    @Autowired
    private SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().exceptionHandling().authenticationEntryPoint((request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "请登陆"));
        authorizeConfigManager.config(http.authorizeRequests());
        //http.logout().logoutUrl("/auth/logout");
        http.apply(smsCodeAuthenticationSecurityConfig);
    }
}
*/
