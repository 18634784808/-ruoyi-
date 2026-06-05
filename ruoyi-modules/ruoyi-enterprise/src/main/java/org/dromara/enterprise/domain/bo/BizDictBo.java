package org.dromara.enterprise.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.enterprise.domain.BizDict;

@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = BizDict.class, reverseConvertGenerate = false)
public class BizDictBo extends BaseEntity {

    private Long id;

    @NotBlank(message = "字典类型不能为空")
    @Size(max = 50, message = "字典类型长度不能超过50")
    private String dictType;

    @NotBlank(message = "选项标签不能为空")
    @Size(max = 200, message = "选项标签长度不能超过200")
    private String label;

    @NotBlank(message = "选项值不能为空")
    @Size(max = 200, message = "选项值长度不能超过200")
    private String value;

    private Integer sort;

    private String status;

    @Size(max = 20, message = "标签颜色长度不能超过20")
    private String tagType;

    @Size(max = 500, message = "备注长度不能超过500")
    private String remark;
}
