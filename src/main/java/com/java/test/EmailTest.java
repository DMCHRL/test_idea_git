package com.java.test;

import freemarker.template.Template;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/8/6.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("qq")
public class EmailTest {
    @Autowired
    private JavaMailSender mailSender; //自动注入的Bean

    //发送邮件的模板引擎
    @Autowired
    private FreeMarkerConfigurer configurer;

    @Value("${spring.mail.username}")
    private String Sender; //读取配置文件中的参数

    @Test
    public void sendTemplateMail(){
        MimeMessage message = null;
        try {
            message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(Sender);
            helper.setTo(Sender);
            helper.setSubject("嗨");

            Map<String, Object> model = new HashMap<String, Object>();
            model.put("username", "测试");

            //修改 application.properties 文件中的读取路径
            //FreeMarkerAutoConfiguration configurer = new FreeMarkerAutoConfiguration();
            // configurer.setTemplateLoaderPath("classpath:templates");
            //读取 html 模板
            Template template = configurer.getConfiguration().getTemplate("fundInEmail2.html");
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
            helper.setText(html, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mailSender.send(message);
    }

}
