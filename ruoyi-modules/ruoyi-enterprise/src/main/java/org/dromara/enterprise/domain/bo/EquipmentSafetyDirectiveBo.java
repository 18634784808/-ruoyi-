package org.dromara.enterprise.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.enterprise.domain.EquipmentSafetyDirective;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = EquipmentSafetyDirective.class, reverseConvertGenerate = false)
public class EquipmentSafetyDirectiveBo extends BaseEntity {

    private Long id;

    @Size(max = 200, message = "发出部门长度不能超过200个字符")
    private String issuingAgency;

    @Size(max = 10, message = "年份长度不能超过10个字符")
    private String docYear;

    @Size(max = 50, message = "文书序号长度不能超过50个字符")
    private String docSeq;

    @Size(max = 50, message = "文书编号长度不能超过50个字符")
    private String docNumber;

    @Size(max = 200, message = "被检查单位长度不能超过200个字符")
    private String inspectedUnit;

    private String problemsFound;

    @Size(max = 500, message = "违反规定描述长度不能超过500个字符")
    private String violationDesc;

    @Size(max = 200, message = "违反条款长度不能超过200个字符")
    private String violationArticle;

    @Size(max = 500, message = "依据规定长度不能超过500个字符")
    private String basisRule;

    private Date deadlineDate;

    private String correctiveMeasures;

    @Size(max = 200, message = "复议机关长度不能超过200个字符")
    private String appealGov;

    @Size(max = 200, message = "诉讼法院长度不能超过200个字符")
    private String appealCourt;

    @Size(max = 100, message = "检查人员签名长度不能超过100个字符")
    private String inspectorSign;

    @Size(max = 100, message = "被检查单位签名长度不能超过100个字符")
    private String unitSign;

    @Size(max = 200, message = "公章信息长度不能超过200个字符")
    private String sealInfo;

    private Date signDate;

    @Size(max = 500, message = "备注长度不能超过500个字符")
    private String remark;
}
