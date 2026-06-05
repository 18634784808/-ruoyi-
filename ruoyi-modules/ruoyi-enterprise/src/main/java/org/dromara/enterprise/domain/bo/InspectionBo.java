package org.dromara.enterprise.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.enterprise.domain.Inspection;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = Inspection.class, reverseConvertGenerate = false)
public class InspectionBo extends BaseEntity {

    private Long id;

    @NotBlank(message = "类别不能为空")
    @Size(max = 100, message = "类别长度不能超过100个字符")
    private String category;

    @NotBlank(message = "被检查单位不能为空")
    @Size(max = 200, message = "被检查单位长度不能超过200个字符")
    private String inspectedUnit;

    @Size(max = 100, message = "法定代表人长度不能超过100个字符")
    private String legalPerson;

    @Size(max = 50, message = "联系电话长度不能超过50个字符")
    private String contactPhone;

    @Size(max = 50, message = "统一社会信用代码长度不能超过50个字符")
    private String unifiedSocialCreditCode;

    @Size(max = 300, message = "住所长度不能超过300个字符")
    private String address;

    private Date inspectionDate;

    @Size(max = 100, message = "检查形式长度不能超过100个字符")
    private String inspectionType;

    private String inspectionSituation;

    private String handlingOpinion;

    @Size(max = 100, message = "签字长度不能超过100个字符")
    private String inspectedUnitSignature;

    private Integer inspectorCount;

    @Size(max = 100, message = "检察人员姓名长度不能超过100个字符")
    private String inspector1;

    @Size(max = 100, message = "检察人员姓名长度不能超过100个字符")
    private String inspector2;

    @Size(max = 100, message = "检察人员姓名长度不能超过100个字符")
    private String inspector3;

    @Size(max = 100, message = "检察人员姓名长度不能超过100个字符")
    private String inspector4;

    @Size(max = 500, message = "整改报告长度不能超过500个字符")
    private String rectificationReport;

    @Size(max = 500, message = "现场笔录长度不能超过500个字符")
    private String sceneNotes;

    @Size(max = 200, message = "所属台账单位长度不能超过200个字符")
    private String ledgerUnit;

    private Long merchantId;

    private String status;

    @Size(max = 500, message = "备注长度不能超过500个字符")
    private String remark;
}
