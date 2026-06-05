package org.dromara.enterprise.domain.vo;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.dromara.enterprise.domain.EnforcementDecision;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = EnforcementDecision.class)
public class EnforcementDecisionVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @ExcelProperty(value = "序号")
    private Long id;

    @ExcelProperty(value = "年份")
    private String year;

    @ExcelProperty(value = "文书序号")
    private String docSeq;

    @ExcelProperty(value = "文书编号")
    private String docNumber;

    @ExcelProperty(value = "当事人")
    private String partyName;

    @ExcelProperty(value = "主体资格证照名称")
    private String licenseName;

    @ExcelProperty(value = "统一社会信用代码")
    private String creditCode;

    @ExcelProperty(value = "住所")
    private String address;

    @ExcelProperty(value = "法定代表人")
    private String legalPerson;

    @ExcelProperty(value = "身份证号码")
    private String idNumber;

    @ExcelProperty(value = "联系电话")
    private String phone;

    @ExcelProperty(value = "其他联系方式")
    private String otherContact;

    @ExcelProperty(value = "违法描述")
    private String violationDesc;

    @ExcelProperty(value = "法律依据")
    private String legalBasis;

    @ExcelProperty(value = "财物清单编号")
    private String facilityListNo;

    @ExcelProperty(value = "强制措施地点")
    private String enforceLocation;

    @ExcelProperty(value = "强制措施期限")
    private Integer enforceDuration;

    @ExcelProperty(value = "行政复议期限")
    private String appealDays;

    @ExcelProperty(value = "复议政府")
    private String appealGov;

    @ExcelProperty(value = "复议机关")
    private String appealAgency;

    @ExcelProperty(value = "行政诉讼期限")
    private String lawsuitDays;

    @ExcelProperty(value = "诉讼法院")
    private String lawsuitCourt;

    @ExcelProperty(value = "机关联系人")
    private String contactPerson;

    @ExcelProperty(value = "机关联系电话")
    private String contactPhone;

    @ExcelProperty(value = "附件清单编号")
    private String attachmentListNo;

    @ExcelProperty(value = "签署日期")
    private String signDate;

    @ExcelProperty(value = "份数")
    private String copyCount;

    @ExcelProperty(value = "送达份数")
    private String deliveryCount;

    @ExcelProperty(value = "份数备注")
    private String copyNote;

    @ExcelProperty(value = "状态")
    private String status;

    @ExcelProperty(value = "备注")
    private String remark;

    @ExcelProperty(value = "创建时间")
    private Date createTime;
}
