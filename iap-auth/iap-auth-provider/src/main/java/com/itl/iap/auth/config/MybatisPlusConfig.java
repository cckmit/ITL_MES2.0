package com.itl.iap.auth.config;

import com.baomidou.mybatisplus.core.parser.ISqlParser;
import com.baomidou.mybatisplus.extension.parsers.BlockAttackSqlParser;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.google.common.collect.Lists;
import net.sf.jsqlparser.statement.delete.Delete;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.List;

/**
 * Mybatis-Plus  配置类
 *
 * @author IAP
 * @date 2020/10/22
 * @since jdk1.8
 */
@EnableTransactionManagement(proxyTargetClass = true)
@Configuration
@MapperScan("com.itl.iap.auth.oauthserver.mapper")
public class MybatisPlusConfig {

    /**
     * Mybatis-Plus 分页插件
     *
     * @return PaginationInterceptor
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        List<ISqlParser> sqlParserList = Lists.newArrayList();
        // 攻击 SQL 阻断解析器、加入解析链（作用！阻止恶意的全表更新删除）
        sqlParserList.add(new BlockAttackSqlParser() {
            @Override
            public void processDelete(Delete delete) {
                super.processDelete(delete);
            }
        });
        paginationInterceptor.setSqlParserList(sqlParserList);
        return paginationInterceptor;
    }

}