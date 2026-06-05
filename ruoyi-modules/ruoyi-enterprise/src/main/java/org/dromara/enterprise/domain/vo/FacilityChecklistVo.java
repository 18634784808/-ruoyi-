package org.dromara.enterprise.domain.vo;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.dromara.enterprise.domain.FacilityChecklist;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = FacilityChecklist.class)
public class FacilityChecklistVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @ExcelProperty(value = "序号")
    private Long id;

    @ExcelProperty(value = "文书编号")
    private String documentNo;

    @ExcelProperty(value = "核对确认文字")
    private String confirmationText;

    @ExcelProperty(value = "当事人签名")
    private String partySignature;

    @ExcelProperty(value = "执法人员签名")
    private String officerSignature;

    @ExcelProperty(value = "见证人签名")
    private String witnessSignature;

    @ExcelProperty(value = "当事人日期")
    private Date partyDate;

    @ExcelProperty(value = "执法人员日期")
    private Date officerDate;

    @ExcelProperty(value = "份数")
    private Integer copiesCount;

    @ExcelProperty(value = "第几页")
    private Integer pageNo;

    @ExcelProperty(value = "共几页")
    private Integer totalPages;

    @ExcelProperty(value = "状态")
    private String status;

    @ExcelProperty(value = "备注")
    private String remark;

    @ExcelProperty(value = "创建时间")
    private Date createTime;

    /** 清单明细 */
    private List<FacilityChecklistItemVo> items;
}
