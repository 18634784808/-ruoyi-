package org.dromara.enterprise.domain.vo;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.dromara.enterprise.domain.EquipmentSafetyDirective;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = EquipmentSafetyDirective.class)
public class EquipmentSafetyDirectiveVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @ExcelProperty(value = "序号")
    private Long id;

    @ExcelProperty(value = "发出部门")
    private String issuingAgency;

    @ExcelProperty(value = "年份")
    private String docYear;

    @ExcelProperty(value = "文书序号")
    private String docSeq;

    @ExcelProperty(value = "文书编号")
    private String docNumber;

    @ExcelProperty(value = "被检查单位")
    private String inspectedUnit;

    @ExcelProperty(value = "存在的问题")
    private String problemsFound;

    @ExcelProperty(value = "违反规定描述")
    private String violationDesc;

    @ExcelProperty(value = "违反条款")
    private String violationArticle;

    @ExcelProperty(value = "依据规定")
    private String basisRule;

    @ExcelProperty(value = "整改截止日期")
    private Date deadlineDate;

    @ExcelProperty(value = "改正措施")
    private String correctiveMeasures;

    @ExcelProperty(value = "复议机关")
    private String appealGov;

    @ExcelProperty(value = "诉讼法院")
    private String appealCourt;

    @ExcelProperty(value = "检查人员签名")
    private String inspectorSign;

    @ExcelProperty(value = "被检查单位签名")
    private String unitSign;

    @ExcelProperty(value = "公章信息")
    private String sealInfo;

    @ExcelProperty(value = "签署日期")
    private Date signDate;

    @ExcelProperty(value = "备注")
    private String remark;

    @ExcelProperty(value = "创建时间")
    private Date createTime;
}
