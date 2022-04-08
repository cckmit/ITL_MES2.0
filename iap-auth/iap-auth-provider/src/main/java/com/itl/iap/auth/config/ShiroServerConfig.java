package com.itl.iap.auth.config;

import com.itl.iap.auth.shiro.credentials.JWTCredentialsMatcher;
import com.itl.iap.auth.shiro.realm.JWTClientRealm;
import com.itl.iap.auth.shiro.realm.JWTModularRealmAuthenticator;
import com.itl.iap.auth.shiro.realm.JWTRealm;
import com.itl.iap.auth.shiro.filter.OAuth2AuthenticationFilter;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.pam.AtLeastOneSuccessfulStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.*;

/**
 * Shiro配置类
 *
 * @author 汤俊
 * @date 2020/10/22
 * @since jdk1.8
 */
@Configuration
public class ShiroServerConfig {

    /**
     * 注意/r/n前不能有空格
     */
    private static final String CRLF = "\r\n";


    @Bean
    public JWTRealm jwtRealm(CredentialsMatcher matcher) {
        JWTRealm jwtRealm = new JWTRealm();
        jwtRealm.setCredentialsMatcher(matcher);
        return jwtRealm;
    }

    @Bean
    public JWTClientRealm jwtClientRealm(CredentialsMatcher matcher) {
        JWTClientRealm jwtClientRealm = new JWTClientRealm();
        jwtClientRealm.setCredentialsMatcher(matcher);
        return jwtClientRealm;
    }

    @Bean
    public CredentialsMatcher credentialsMatcher() {
        return new JWTCredentialsMatcher();
    }

    @Bean(name = "securityMananger")
    public DefaultWebSecurityManager securityManager(CredentialsMatcher matcher) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        //设置realm.
        securityManager.setAuthenticator(modularRealmAuthenticator());
        List<Realm> realms = new ArrayList<>();
        realms.add(jwtRealm(matcher));
        realms.add(jwtClientRealm(matcher));
        securityManager.setRealms(realms);
//        securityManager.setRealm(jwtRealm(matcher));
        /*
         * 关闭shiro自带的session，详情见文档
         * http://shiro.apache.org/session-management.html#SessionManagement-StatelessApplications%28Sessionless%29
         */
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        securityManager.setSubjectDAO(subjectDAO);
        return securityManager;
    }

    /**
     * 系统自带的Realm管理，主要针对多realm
     */
    @Bean
    public ModularRealmAuthenticator modularRealmAuthenticator() {
        //自己重写的ModularRealmAuthenticator
        JWTModularRealmAuthenticator modularRealmAuthenticator = new JWTModularRealmAuthenticator();
        modularRealmAuthenticator.setAuthenticationStrategy(new AtLeastOneSuccessfulStrategy());
        return modularRealmAuthenticator;
    }

    @Bean(name = "rememberCookies")
    public SimpleCookie createSimpleCookies() {
        SimpleCookie sc = new SimpleCookie("rememberMe");
        sc.setHttpOnly(true);
        sc.setMaxAge(5);
        return sc;
    }

    @Bean(name = "cookieRemmberMananger")
    public CookieRememberMeManager createCookieRemmberMananger() {
        CookieRememberMeManager crm = new CookieRememberMeManager();
        crm.setCookie(createSimpleCookies());
        return crm;
    }

    @Bean(name = "cacheManager")
    public EhCacheManager createEhcacheManager() {
        EhCacheManager cacheManager = new EhCacheManager();
        String path = "classpath:ehcache.xml";
        cacheManager.setCacheManagerConfigFile(path);
        return cacheManager;
    }

    /**
     * 读取ini文获得固定权限
     */
    public String loadFilterChainDefinitions() {
        StringBuffer sb = new StringBuffer();
        //	sb.append(getFixedAuthRule());//固定权限，采用读取配置文件
       /* sb.append("/ = anon").append(CRLF);
        sb.append("/login = anon").append(CRLF);
        sb.append("/logout = logout").append(CRLF);
        sb.append("/authorize=anon").append(CRLF);
        sb.append("/accessToken=anon").append(CRLF);
        sb.append("/authClient/** = anon").append(CRLF);*/
        sb.append("/userInfo = oauth2AuthenticationFilter").append(CRLF);
//        sb.append("/swagger/** = anon").append(CRLF);
//        sb.append("/v2/api-docs = anon").append(CRLF);
//        sb.append("/swagger-ui.html = anon").append(CRLF);
//        sb.append("/swagger-resources/** = anon").append(CRLF);
//        sb.append("/** = user").append(CRLF);
        return sb.toString();
    }


    @Bean(name = "shiroFilterFactoryBean")
    public ShiroFilterFactoryBean createShiroSecurityFilterFactory() {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager(credentialsMatcher()));
        shiroFilterFactoryBean.setLoginUrl("/login");
        Map filterChainDefinitionMap = new HashMap<String, String>();
        //注意过滤器配置顺序 不能颠倒
//        filterChainDefinitionMap.put("/userInfo/**", "authc");
  /*     filterChainDefinitionMap.put("/static/**", "anon");
         filterChainDefinitionMap.put("/templates/**", "anon");
         filterChainDefinitionMap.put("/g/unLogin", "anon");
         filterChainDefinitionMap.put("/g/login", "loginFtr");
         filterChainDefinitionMap.put("/g/logout", "authc");
         filterChainDefinitionMap.put("/g/unauthorized", "anon");
         filterChainDefinitionMap.put("/**", "permsFilter");*/
        // 添加自己的过滤器并且取名为jwt
        LinkedHashMap<String, Filter> filterMap = new LinkedHashMap<>();
        filterMap.put("oauth2AuthenticationFilter", oauth2AuthenticationFilter());
        shiroFilterFactoryBean.setFilters(filterMap);
        // 过滤链定义，从上向下顺序执行，一般将放在最为下边
        filterChainDefinitionMap.put("/**", "oauth2AuthenticationFilter");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        shiroFilterFactoryBean.setFilterChainDefinitions(loadFilterChainDefinitions());
        return shiroFilterFactoryBean;
    }

    @Bean
    public OAuth2AuthenticationFilter oauth2AuthenticationFilter() {
        OAuth2AuthenticationFilter authFilter = new OAuth2AuthenticationFilter();
        authFilter.setAuthcCodeParam("code");
        return authFilter;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(CredentialsMatcher matcher) {//@Qualifier("hashedCredentialsMatcher")
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager(matcher));
        return authorizationAttributeSourceAdvisor;
    }

    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        return defaultAdvisorAutoProxyCreator;
    }
}
