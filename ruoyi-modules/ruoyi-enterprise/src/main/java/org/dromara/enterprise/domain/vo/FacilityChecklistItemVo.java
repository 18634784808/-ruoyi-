package org.dromara.enterprise.domain.vo;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@ExcelIgnoreUnannotated
public class FacilityChecklistItemVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long checklistId;

    @ExcelProperty(value = "序号")
    private Integer seqNo;

    @ExcelProperty(value = "标称名称/场所")
    private String nameOrVenue;

    @ExcelProperty(value = "规格型号/场所地址")
    private String specOrAddress;

    @ExcelProperty(value = "单位")
    private String unit;

    @ExcelProperty(value = "数量")
    private String quantity;

    @ExcelProperty(value = "备注")
    private String remark;
}
