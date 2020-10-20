package com.peilian.dataplatform.util;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.dingtalk.api.response.OapiRobotSendResponse;
import com.peilian.dataplatform.dto.MailSend;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

/**
 * @author zhengshangchao
 */
@Slf4j
@Component
public class MailRest {

    @Value("${mail.api.url}")
    private String mailUrl;

    @Value("${mail.title}")
    private String mailTitle;

    @Value("${mail.msName}")
    private String msName;

    @Value("${mail.keyword}")
    private String keyword;

    @Value("${dingding.api.url}")
    private String dingdingUrl;

    @Value("${dingding.api.type}")
    private String dingdingType;

    @Value("${dingding.api.phone}")
    private String dingdingPhone;

    /**
     * 1-html
     */
    private static final int CONTENT_TYPE_HTML = 1;

    /**
     * 1-默认全部
     */
    private static final int TYPE_ALL = 1;

    /**
     * 发送报警邮件
     *
     * @param sendTo
     * @param content
     * @throws Exception
     */
    public void sendMail(String sendTo, String content) throws Exception {
        log.info(String.format("sendMail入参sendTo=%s, content=%s", sendTo, content));
        Assert.hasText(sendTo, "邮件发送的目标邮箱不能为空");
        Assert.hasText(content, "邮件内容不能为空");
        MailSend mailSend = new MailSend();
        mailSend.setContentType(CONTENT_TYPE_HTML);
        mailSend.setContent(content);
        mailSend.setType(TYPE_ALL);
        mailSend.setTo(sendTo);
        mailSend.setTitle(this.mailTitle);
        mailSend.setMsName(this.msName);
        mailSend.setKeyword(this.keyword);
        String requestJson = JSONObject.fromObject(mailSend).toString();
        log.info(String.format("邮件发送内容:%s", requestJson));
        RestTemplate template = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Src", this.msName);
        httpHeaders.set("Content-Type", "application/json;charset=UTF-8");
        HttpEntity<String> httpEntity = new HttpEntity<>(requestJson, httpHeaders);
        ResponseEntity<String> responseEntity = template.exchange(mailUrl, HttpMethod.POST, httpEntity, String.class);
        HttpStatus httpStatus = responseEntity.getStatusCode();
        String result = responseEntity.getBody();
        if (HttpStatus.OK.equals(httpStatus)) {
            log.info(String.format("邮件发送成功: %s", result));
        } else {
            log.error(String.format("邮件发送失败，返回码:%s, 失败原因:%s", httpStatus.value(), result));
            throw new Exception(String.format("邮件发送失败，返回码:%s, 失败原因:%s", httpStatus.value(), result));
        }
    }


    /**
     * 发送钉钉报警群
     *
     * @param content
     * @throws Exception
     */
    public void sendDingTalk(String content) throws Exception {
        Assert.hasText(content, "钉钉报警内容不能为空");
        log.info(String.format("钉钉发送内容:%s", content));
        DingTalkClient client = new DefaultDingTalkClient(dingdingUrl);
        OapiRobotSendRequest request = new OapiRobotSendRequest();
        request.setMsgtype(dingdingType);
        OapiRobotSendRequest.Text text = new OapiRobotSendRequest.Text();
        text.setContent(content);
        request.setText(text);
        OapiRobotSendRequest.At at = new OapiRobotSendRequest.At();
        at.setAtMobiles(Arrays.asList(dingdingPhone.split(",")));
        at.setIsAtAll(false);
        OapiRobotSendResponse response = client.execute(request);
        log.info("钉钉报警消息返回：{}", response);
    }

    /**
     * 获取邮件模板
     *
     * @return
     */
    public String getHtmlContent() {
        return "<html>\n" +
                "<head>\n" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf8\" />\n" +
                "<style type=\"text/css\">\n" +
                "TABLE {\n" +
                "    BORDER-COLLAPSE: collapse;TEXT-ALIGN: center; WIDTH:80%; margin: 0 auto;\n" +
                "}\n" +
                "TR {\n" +
                "    BACKGROUND-COLOR:#000099; TEXT-ALIGN: left; FONT-SIZE: 14px; TEXT-WEIGHT:bold; COLOR:#FFFFFF; BORDER:1px #888 solid; \n" +
                "}\n" +
                "TH {\n" +
                "    FONT-SIZE: 12px; FONT-FAMILY: Verdana, Arial; BORDER:1px #ccc solid; TEXT-ALIGN: center\n" +
                "}\n" +
                "TD {\n" +
                "    FONT-SIZE: 12px; BORDER:1px #999999 solid; BACKGROUND-COLOR: #e5ecf9; TEXT-ALIGN: center; COLOR:black;\n" +
                "}\n" +
                "</style>\n" +
                "</head>\n" +
                "<body>\n" +
                "#content\n" +
                "</body>\n" +
                "</html>\n";
    }

}
