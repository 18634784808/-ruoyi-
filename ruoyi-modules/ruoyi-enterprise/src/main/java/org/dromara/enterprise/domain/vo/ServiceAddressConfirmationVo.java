package org.dromara.enterprise.domain.vo;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.dromara.enterprise.domain.ServiceAddressConfirmation;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = ServiceAddressConfirmation.class)
public class ServiceAddressConfirmationVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @ExcelProperty(value = "序号")
    private Long id;

    @ExcelProperty(value = "受送达人")
    private String recipientName;

    @ExcelProperty(value = "接受电子送达")
    private String acceptElectronic;

    @ExcelProperty(value = "传真号码")
    private String faxNumber;

    @ExcelProperty(value = "电子邮件地址")
    private String emailAddress;

    @ExcelProperty(value = "即时通讯账号")
    private String instantAccount;

    @ExcelProperty(value = "送达地址")
    private String serviceAddress;

    @ExcelProperty(value = "收件人")
    private String recipientPerson;

    @ExcelProperty(value = "收件人联系电话")
    private String recipientPhone;

    @ExcelProperty(value = "邮政编码")
    private String postalCode;

    @ExcelProperty(value = "签名")
    private String signName;

    @ExcelProperty(value = "签名日期")
    private Date signDate;

    @ExcelProperty(value = "备注")
    private String remark;

    @ExcelProperty(value = "创建时间")
    private Date createTime;
}
