package org.dromara.enterprise.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.enterprise.domain.Ledger;

@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = Ledger.class, reverseConvertGenerate = false)
public class LedgerBo extends BaseEntity {

    private Long id;

    @NotBlank(message = "被检查单位不能为空")
    @Size(max = 200, message = "被检查单位长度不能超过200个字符")
    private String inspectedUnit;

    @Size(max = 100, message = "法定代表人长度不能超过100个字符")
    private String legalPerson;

    @Size(max = 50, message = "联系方式长度不能超过50个字符")
    private String contactPhone;

    @Size(max = 300, message = "地址长度不能超过300个字符")
    private String address;

    @Size(max = 18, message = "统一社会信用代码长度不能超过18个字符")
    private String unifiedSocialCreditCode;

    @Size(max = 100, message = "检查人员类别长度不能超过100个字符")
    private String inspectorCategory;

    private String date;

    private String inspectionDate;

    @Size(max = 500, message = "检查内容长度不能超过500个字符")
    private String inspectionContent;

    @Size(max = 500, message = "整改措施长度不能超过500个字符")
    private String rectificationMeasure;

    private String rectificationProgress;

    @Size(max = 500, message = "备注长度不能超过500个字符")
    private String remark;
}
