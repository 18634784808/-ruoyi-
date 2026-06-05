package org.dromara.enterprise.domain.bo;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class FacilityChecklistItemBo {

    private Long id;

    private Long checklistId;

    private Integer seqNo;

    @Size(max = 300, message = "标称名称/场所长度不能超过300个字符")
    private String nameOrVenue;

    @Size(max = 300, message = "规格型号/场所地址长度不能超过300个字符")
    private String specOrAddress;

    @Size(max = 50, message = "单位长度不能超过50个字符")
    private String unit;

    @Size(max = 50, message = "数量长度不能超过50个字符")
    private String quantity;

    @Size(max = 500, message = "备注长度不能超过500个字符")
    private String remark;
}
