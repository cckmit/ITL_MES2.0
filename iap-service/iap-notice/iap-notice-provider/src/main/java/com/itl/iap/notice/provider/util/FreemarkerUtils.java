package com.itl.iap.notice.provider.util;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

/**
 * freemarker工具类
 *
 * @author 曾慧任
 * @date 2020-06-29
 * @since jdk1.8
 */
public class FreemarkerUtils {
    private static String defaultCharacter = "UTF-8";
    private static Configuration cfg;

    private FreemarkerUtils() {
    }

    static {
        cfg = new Configuration(Configuration.getVersion());
        cfg.setDefaultEncoding(defaultCharacter);
        cfg.setTagSyntax(Configuration.AUTO_DETECT_TAG_SYNTAX);
    }

    /**
     * 对模板进行渲染
     *
     * @param data            数据Map
     * @param templateContent 模板内容
     * @return
     */
    public static String generateContent(
            Map<String, Object> data, String templateContent) throws IOException, TemplateException {
        if (templateContent == null) {
            return "";
        }
        StringWriter out = new StringWriter();
        Configuration config = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        config.setDefaultEncoding("UTF-8");
        // classic compatible,是${abc}允许出现空值的
        config.setClassicCompatible(true);
        Template template = new Template(null, templateContent, config);
        template.process(data, out);
        return out.toString();
    }

}
