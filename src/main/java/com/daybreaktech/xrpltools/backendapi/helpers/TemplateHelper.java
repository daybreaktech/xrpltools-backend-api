package com.daybreaktech.xrpltools.backendapi.helpers;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

public class TemplateHelper {

    private TemplateHelper() {
    }

    public static String generateFromTemplate(Class clasz, String templateName, Map<String, Object> mapping) throws IOException {
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_20);
        configuration.setClassForTemplateLoading(clasz, "/templates/");
        Template template = null;
        Writer out = null;

        try {
            template = configuration.getTemplate(templateName + ".ftl");
            out = new StringWriter();
            template.process(mapping, out);
            return out.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.close();
        }

        return null;
    }

}
