package org.dromara.enterprise.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.enterprise.domain.EnforcementDecision;

@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = EnforcementDecision.class, reverseConvertGenerate = false)
public class EnforcementDecisionBo extends BaseEntity {

    private Long id;

    @Size(max = 10, message = "年份长度不能超过10个字符")
    private String year;

    @Size(max = 50, message = "文书序号长度不能超过50个字符")
    private String docSeq;

    @Size(max = 50, message = "文书编号长度不能超过50个字符")
    private String docNumber;

    @Size(max = 200, message = "当事人长度不能超过200个字符")
    private String partyName;

    @Size(max = 200, message = "主体资格证照名称长度不能超过200个字符")
    private String licenseName;

    @Size(max = 100, message = "统一社会信用代码长度不能超过100个字符")
    private String creditCode;

    @Size(max = 500, message = "住所长度不能超过500个字符")
    private String address;

    @Size(max = 100, message = "法定代表人长度不能超过100个字符")
    private String legalPerson;

    @Size(max = 50, message = "身份证号码长度不能超过50个字符")
    private String idNumber;

    @Size(max = 30, message = "联系电话长度不能超过30个字符")
    private String phone;

    @Size(max = 100, message = "其他联系方式长度不能超过100个字符")
    private String otherContact;

    @Size(max = 2000, message = "违法描述长度不能超过2000个字符")
    private String violationDesc;

    @Size(max = 1000, message = "法律依据长度不能超过1000个字符")
    private String legalBasis;

    @Size(max = 100, message = "财物清单编号长度不能超过100个字符")
    private String facilityListNo;

    @Size(max = 500, message = "强制措施地点长度不能超过500个字符")
    private String enforceLocation;

    private Integer enforceDuration;

    @Size(max = 10, message = "行政复议期限长度不能超过10个字符")
    private String appealDays;

    @Size(max = 200, message = "复议政府长度不能超过200个字符")
    private String appealGov;

    @Size(max = 200, message = "复议机关长度不能超过200个字符")
    private String appealAgency;

    @Size(max = 10, message = "行政诉讼期限长度不能超过10个字符")
    private String lawsuitDays;

    @Size(max = 200, message = "诉讼法院长度不能超过200个字符")
    private String lawsuitCourt;

    @Size(max = 100, message = "机关联系人长度不能超过100个字符")
    private String contactPerson;

    @Size(max = 30, message = "机关联系电话长度不能超过30个字符")
    private String contactPhone;

    @Size(max = 100, message = "附件清单编号长度不能超过100个字符")
    private String attachmentListNo;

    @Size(max = 50, message = "签署日期长度不能超过50个字符")
    private String signDate;

    @Size(max = 10, message = "文书份数长度不能超过10个字符")
    private String copyCount;

    @Size(max = 10, message = "送达份数长度不能超过10个字符")
    private String deliveryCount;

    @Size(max = 200, message = "份数备注长度不能超过200个字符")
    private String copyNote;

    private String status;

    @Size(max = 500, message = "备注长度不能超过500个字符")
    private String remark;
}
