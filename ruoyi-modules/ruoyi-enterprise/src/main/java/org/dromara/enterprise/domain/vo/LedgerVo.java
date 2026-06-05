package org.dromara.enterprise.domain.vo;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.dromara.enterprise.domain.Ledger;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = Ledger.class)
public class LedgerVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @ExcelProperty(value = "序号")
    private Long id;

    @ExcelProperty(value = "日期")
    private String date;

    @ExcelProperty(value = "检查日期")
    private String inspectionDate;

    @ExcelProperty(value = "被检查单位")
    private String inspectedUnit;

    @ExcelProperty(value = "法定代表人")
    private String legalPerson;

    @ExcelProperty(value = "联系方式")
    private String contactPhone;

    @ExcelProperty(value = "地址")
    private String address;

    @ExcelProperty(value = "检查内容")
    private String inspectionContent;

    @ExcelProperty(value = "整改措施")
    private String rectificationMeasure;

    @ExcelProperty(value = "整改进度")
    private String rectificationProgress;

    @ExcelProperty(value = "检查人员类别")
    private String inspectorCategory;

    @ExcelProperty(value = "备注")
    private String remark;

    @ExcelProperty(value = "统一社会信用代码")
    private String unifiedSocialCreditCode;

    @ExcelProperty(value = "创建时间")
    private Date createTime;
}
