package org.dromara.enterprise.domain.vo;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.dromara.enterprise.domain.SceneNotes;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = SceneNotes.class)
public class SceneNotesVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @ExcelProperty(value = "序号")
    private Long id;

    @ExcelProperty(value = "开始时间")
    private Date startTime;

    @ExcelProperty(value = "截止时间")
    private Date endTime;

    @ExcelProperty(value = "地点")
    private String place;

    @ExcelProperty(value = "检察人员")
    private String inspector;

    @ExcelProperty(value = "检查人员副本")
    private String inspectorCopy;

    @ExcelProperty(value = "当事人")
    private String party;

    @ExcelProperty(value = "主体资格证照名称")
    private String qualificationName;

    @ExcelProperty(value = "通知当事人到场情况")
    private String partyPresenceNotice;

    @ExcelProperty(value = "检查人员1")
    private String inspector1;

    @ExcelProperty(value = "当事人1")
    private String party1;

    @ExcelProperty(value = "检察人员2")
    private String inspector2;

    @ExcelProperty(value = "当事人2")
    private String party2;

    @ExcelProperty(value = "现场情况")
    private String sceneSituation;

    @ExcelProperty(value = "当事人的陈述和申辩")
    private String partyStatement;

    @ExcelProperty(value = "状态")
    private String status;

    @ExcelProperty(value = "关联检查记录")
    private Long inspectionId;

    @ExcelProperty(value = "备注")
    private String remark;

    @ExcelProperty(value = "创建时间")
    private Date createTime;
}
