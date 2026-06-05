package org.dromara.enterprise.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.enterprise.domain.Issue;

@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = Issue.class, reverseConvertGenerate = false)
public class IssueBo extends BaseEntity {

    private Long id;

    @NotBlank(message = "问题选项不能为空")
    @Size(max = 500, message = "问题选项长度不能超过500个字符")
    private String issueOption;

    @Size(max = 50, message = "自增编号长度不能超过50个字符")
    private String autoIncrementCode;

    private String severity;

    private String status;

    private Long inspectionId;

    @Size(max = 500, message = "备注长度不能超过500个字符")
    private String remark;
}
