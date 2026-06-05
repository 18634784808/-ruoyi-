package org.dromara.enterprise.domain.vo;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.dromara.enterprise.domain.Inspection;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = Inspection.class)
public class InspectionVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @ExcelProperty(value = "序号")
    private Long id;

    @ExcelProperty(value = "类别")
    private String category;

    @ExcelProperty(value = "被检查单位")
    private String inspectedUnit;

    @ExcelProperty(value = "法定代表人")
    private String legalPerson;

    @ExcelProperty(value = "联系电话")
    private String contactPhone;

    @ExcelProperty(value = "统一社会信用代码")
    private String unifiedSocialCreditCode;

    @ExcelProperty(value = "住所")
    private String address;

    @ExcelProperty(value = "检查日期")
    private Date inspectionDate;

    @ExcelProperty(value = "检查形式")
    private String inspectionType;

    @ExcelProperty(value = "检查情况")
    private String inspectionSituation;

    @ExcelProperty(value = "处理意见")
    private String handlingOpinion;

    @ExcelProperty(value = "被检查单位签字")
    private String inspectedUnitSignature;

    @ExcelProperty(value = "检察人员数量")
    private Integer inspectorCount;

    @ExcelProperty(value = "检察人员1")
    private String inspector1;

    @ExcelProperty(value = "检察人员2")
    private String inspector2;

    @ExcelProperty(value = "检察人员3")
    private String inspector3;

    @ExcelProperty(value = "检察人员4")
    private String inspector4;

    @ExcelProperty(value = "整改报告")
    private String rectificationReport;

    @ExcelProperty(value = "现场笔录")
    private String sceneNotes;

    @ExcelProperty(value = "所属台账单位")
    private String ledgerUnit;

    @ExcelProperty(value = "关联商户ID")
    private Long merchantId;

    @ExcelProperty(value = "状态")
    private String status;

    @ExcelProperty(value = "备注")
    private String remark;

    @ExcelProperty(value = "创建时间")
    private Date createTime;
}
