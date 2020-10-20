package com.peilian.dataplatform.repository;

import com.peilian.dataplatform.entity.SmsRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author zhengshangchao
 */
@Repository
public interface SmsRecordRepository extends JpaRepository<SmsRecord, Long>, JpaSpecificationExecutor<SmsRecord> {

    /**
     * 查询10分钟内发送的有效验证码
     *
     * @param cellphone
     * @return
     */
    @Query(value = "select code from sms_record where cellphone = ?1 and send_status = 1 and update_time > DATE_SUB(NOW(), INTERVAL 10 MINUTE) order by update_time desc limit 1", nativeQuery = true)
    Integer findSms(String cellphone);
}
