package com.itl.iap.common.base.utils;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * 国际化工具类
 *
 */
@Component
public class I18nUtil {

    private static MessageSource messageSource;

    public I18nUtil(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * 获取国际化配置文件指定key内容
     *
     * @param key key
     * @return String
     */
    public static String get(String key) {
        try {
            return messageSource.getMessage( key, null, LocaleContextHolder.getLocale() );
        } catch (Exception e) {
            return key;
        }
    }

    /**
     * 获取指定语言国际化配置文件指定key内容
     *
     * @param key key
     * @param locale 指定语言
     * @return String
     */
    public static String get(String key, Locale locale) {

        try {
            return messageSource.getMessage( key, null, LocaleContextHolder.getLocale() );
        } catch (Exception e) {
            return key;
        }

    }

    /**
     * 获取国际化配置文件指定key内容，并且通过args替换{0}、{1}中的内容
     *
     * @param key key
     * @param args 参数内容
     * @return
     */
    public static String get(String key,Object[] args ) {
        try {
            return messageSource.getMessage( key, args, LocaleContextHolder.getLocale() );
        } catch (Exception e) {
            return key;
        }
    }

    /**
     * 获取指定语言国际化配置文件指定key内容，并且通过args替换{0}、{1}中的内容
     *
     * @param key key
     * @param args 参数内容
     * @locale locale 指定语言
     * @return
     */
    public static String get(String key,Object[] args ,Locale locale) {
        try {
            return messageSource.getMessage( key, args, locale );
        } catch (Exception e) {
            return key;
        }
    }
}
