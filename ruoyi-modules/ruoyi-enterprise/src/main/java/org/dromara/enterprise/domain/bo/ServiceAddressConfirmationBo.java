package org.dromara.enterprise.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.enterprise.domain.ServiceAddressConfirmation;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = ServiceAddressConfirmation.class, reverseConvertGenerate = false)
public class ServiceAddressConfirmationBo extends BaseEntity {

    private Long id;

    @Size(max = 200, message = "受送达人长度不能超过200个字符")
    private String recipientName;

    private String acceptElectronic;

    @Size(max = 50, message = "传真号码长度不能超过50个字符")
    private String faxNumber;

    @Size(max = 200, message = "电子邮件地址长度不能超过200个字符")
    private String emailAddress;

    @Size(max = 200, message = "即时通讯账号长度不能超过200个字符")
    private String instantAccount;

    @Size(max = 500, message = "送达地址长度不能超过500个字符")
    private String serviceAddress;

    @Size(max = 100, message = "收件人长度不能超过100个字符")
    private String recipientPerson;

    @Size(max = 50, message = "收件人联系电话长度不能超过50个字符")
    private String recipientPhone;

    @Size(max = 20, message = "邮政编码长度不能超过20个字符")
    private String postalCode;

    @Size(max = 100, message = "签名长度不能超过100个字符")
    private String signName;

    private Date signDate;

    @Size(max = 500, message = "备注长度不能超过500个字符")
    private String remark;
}
