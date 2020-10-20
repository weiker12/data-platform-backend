package com.peilian.dataplatform.util;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.RandomUtil;
import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.peilian.dataplatform.config.BizException;
import com.peilian.dataplatform.entity.SmsRecord;
import com.peilian.dataplatform.enums.SmsSendResult;
import com.peilian.dataplatform.repository.SmsRecordRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 用于短信验证码发送的工具类
 *
 * @author zhengshangchao
 */
@Slf4j
@Component
public class SmsUtil {

    /**
     * 短信国家代码 +86
     */
    private static final String COUNTRY_CODE = "+86";
    @Value("${jwt.key}")
    private String jwtKey;
    @Value("${sms.tencent.appId}")
    private String smsApiId;
    @Value("${sms.tencent.key}")
    private String smsKey;
    @Value("${sms.white.list}")
    private String whiteList;
    @Autowired
    private SmsRecordRepository smsRecordRepository;
    /**
     * 短信发送频率超限
     * 1013	下发短信/语音命中了频率限制策略	可自行到控制台调整短信频率限制策略，如有其他需求请联系
     * 1023	单个手机号30秒内下发短信条数超过设定的上限	可自行到控制台调整短信频率限制策略
     * 1024	单个手机号1小时内下发短信条数超过设定的上限	可自行到控制台调整短信频率限制策略
     * 1025	单个手机号日下发短信条数超过设定的上限	可自行到控制台调整短信频率限制策略
     * 1026	单个手机号下发相同内容超过设定的上限	可自行到控制台调整短信频率限制策略
     */
    private List<Integer> sendLimitCode = Arrays.asList(1013, 1023, 1024, 1025, 1026);

    /**
     * 发送短信验证码
     *
     * @param cellphone
     */
    public void sendSms(String cellphone) throws BizException {
        log.info("入参cellphone={}", cellphone);
        Assert.hasText(cellphone, "cellphone参数不能为空！");
        // 校验手机号码的格式
        Validator.isMobile(cellphone);
        // 校验白名单
        if (StringUtils.isNotBlank(whiteList) && !whiteList.contains(cellphone)) {
            throw new BizException("手机号不在白名单！");
        }
        String code = generateSmsCode(cellphone);
        sendSmsByTencent(cellphone, code);
    }


    /**
     * 生成6位数字验证码
     * 在缓存中保存
     *
     * @param cellphone
     * @return
     */
    public String generateSmsCode(String cellphone) {
        // 生成6位数字的随机验证码
        String code = RandomUtil.randomNumbers(6);
        return code;
    }

    /**
     * 获取最近5分钟内发的smsCode验证码
     *
     * @param cellphone
     * @return
     */
    public String getSmsCacheCode(String cellphone) {
        Integer code = smsRecordRepository.findSms(cellphone);
        return Objects.isNull(code) ? "" : code.toString();
    }


    /**
     * 通过腾讯云发送短信
     *
     * @param mobile
     * @param code
     * @return
     */
    public SmsSendResult sendSmsByTencent(String mobile, String code) {
        try {
            long start = System.currentTimeMillis();
            String smsContent = String.format("【VIP陪练】您的验证码是%s, 10分钟内有效请尽快填写。", code);
            SmsSingleSender sender = new SmsSingleSender(Integer.valueOf(smsApiId), smsKey);
            SmsSingleSenderResult result = sender.send(0, COUNTRY_CODE.replace("+", ""), mobile,
                    smsContent, "", "");
            log.info("tencent sendSMS result:{}", result);
            SmsRecord smsRecord = new SmsRecord();
            smsRecord.setCellphone(mobile);
            smsRecord.setCode(code);
            smsRecord.setUseStatus(0);
            smsRecord.setSmsType(10);
            smsRecord.setSendStatus(0);
            if (result.result == 0) {
                smsRecord.setSendStatus(1);
                long end = System.currentTimeMillis();
                long spendTime = (end - start);
                smsRecord.setSpendTime(spendTime);
                saveSmsRecord(smsRecord);
                return SmsSendResult.SUCCESS;
            } else if (sendLimitCode.contains(result.result)) {
                smsRecord.setSendStatus(0);
                long end = System.currentTimeMillis();
                long spendTime = (end - start);
                smsRecord.setSpendTime(spendTime);
                saveSmsRecord(smsRecord);
                return SmsSendResult.MAX_SEND_TIME_LIMIT;
            }
            smsRecord.setSendStatus(0);
            long end = System.currentTimeMillis();
            long spendTime = (end - start);
            smsRecord.setSpendTime(spendTime);
            saveSmsRecord(smsRecord);
        } catch (Exception e) {
            log.error("tencent sendSMS error:{}", e.toString());
        }
        return SmsSendResult.FAILED;
    }

    /**
     * 保存短信记录
     *
     * @param smsRecord
     */
    public void saveSmsRecord(SmsRecord smsRecord) {
        Assert.notNull(smsRecord, "smsRecord不能为空！");
        smsRecordRepository.save(smsRecord);
    }

}
