package org.dromara.enterprise.domain.vo;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.dromara.enterprise.domain.BizDict;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = BizDict.class)
public class BizDictVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @ExcelProperty(value = "序号")
    private Long id;

    @ExcelProperty(value = "字典类型")
    private String dictType;

    @ExcelProperty(value = "选项标签")
    private String label;

    @ExcelProperty(value = "选项值")
    private String value;

    @ExcelProperty(value = "排序")
    private Integer sort;

    @ExcelProperty(value = "状态")
    private String status;

    @ExcelProperty(value = "标签颜色")
    private String tagType;

    @ExcelProperty(value = "备注")
    private String remark;

    @ExcelProperty(value = "创建时间")
    private Date createTime;
}
