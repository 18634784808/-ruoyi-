package org.dromara.enterprise.domain.vo;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.dromara.enterprise.domain.Issue;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = Issue.class)
public class IssueVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @ExcelProperty(value = "序号")
    private Long id;

    @ExcelProperty(value = "问题选项")
    private String issueOption;

    @ExcelProperty(value = "自增编号")
    private String autoIncrementCode;

    @ExcelProperty(value = "严重程度")
    private String severity;

    @ExcelProperty(value = "状态")
    private String status;

    @ExcelProperty(value = "关联检查记录")
    private Long inspectionId;

    @ExcelProperty(value = "备注")
    private String remark;

    @ExcelProperty(value = "创建时间")
    private Date createTime;
}
