package org.dromara.enterprise.domain.vo;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.dromara.enterprise.domain.Inspector;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = Inspector.class)
public class InspectorVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @ExcelProperty(value = "序号")
    private Long id;

    @ExcelProperty(value = "姓名")
    private String name;

    @ExcelProperty(value = "部门")
    private String department;

    @ExcelProperty(value = "执法证号")
    private String certNo;

    @ExcelProperty(value = "状态")
    private String status;

    @ExcelProperty(value = "备注")
    private String remark;

    @ExcelProperty(value = "创建时间")
    private Date createTime;
}
