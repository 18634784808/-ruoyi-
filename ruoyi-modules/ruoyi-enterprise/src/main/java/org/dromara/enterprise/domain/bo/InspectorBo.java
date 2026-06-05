package org.dromara.enterprise.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.enterprise.domain.Inspector;

@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = Inspector.class, reverseConvertGenerate = false)
public class InspectorBo extends BaseEntity {

    private Long id;

    @NotBlank(message = "姓名不能为空")
    @Size(max = 100, message = "姓名长度不能超过100个字符")
    private String name;

    @Size(max = 100, message = "部门长度不能超过100个字符")
    private String department;

    @Size(max = 50, message = "执法证号长度不能超过50个字符")
    private String certNo;

    private String status;

    @Size(max = 500, message = "备注长度不能超过500个字符")
    private String remark;
}
