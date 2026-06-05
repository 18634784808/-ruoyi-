package org.dromara.enterprise.domain.vo;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.dromara.enterprise.domain.InquiryNotes;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = InquiryNotes.class)
public class InquiryNotesVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @ExcelProperty(value = "序号")
    private Long id;

    @ExcelProperty(value = "开始时间")
    private Date startTime;

    @ExcelProperty(value = "截止时间")
    private Date endTime;

    @ExcelProperty(value = "次数")
    private Integer times;

    @ExcelProperty(value = "问题数")
    private Integer questionCount;

    @ExcelProperty(value = "地点")
    private String place;

    @ExcelProperty(value = "询问人")
    private String inquirer;

    @ExcelProperty(value = "询问人副本")
    private String inquirerCopy;

    @ExcelProperty(value = "当事人")
    private String party;

    @ExcelProperty(value = "性别")
    private String gender;

    @ExcelProperty(value = "其他联系方式")
    private String otherContact;

    @ExcelProperty(value = "职务")
    private String position;

    @ExcelProperty(value = "询问人1")
    private String inquirer1;

    @ExcelProperty(value = "被询问人1")
    private String respondent1;

    @ExcelProperty(value = "询问人2")
    private String inquirer2;

    @ExcelProperty(value = "被询问人2")
    private String respondent2;

    @ExcelProperty(value = "问题1")
    private String question1;
    @ExcelProperty(value = "回答1")
    private String answer1;
    @ExcelProperty(value = "问题2")
    private String question2;
    @ExcelProperty(value = "回答2")
    private String answer2;
    @ExcelProperty(value = "问题3")
    private String question3;
    @ExcelProperty(value = "回答3")
    private String answer3;
    @ExcelProperty(value = "问题4")
    private String question4;
    @ExcelProperty(value = "回答4")
    private String answer4;
    @ExcelProperty(value = "问题5")
    private String question5;
    @ExcelProperty(value = "回答5")
    private String answer5;
    @ExcelProperty(value = "问题6")
    private String question6;
    @ExcelProperty(value = "回答6")
    private String answer6;
    @ExcelProperty(value = "问题7")
    private String question7;
    @ExcelProperty(value = "回答7")
    private String answer7;
    @ExcelProperty(value = "问题8")
    private String question8;
    @ExcelProperty(value = "回答8")
    private String answer8;
    @ExcelProperty(value = "问题9")
    private String question9;
    @ExcelProperty(value = "回答9")
    private String answer9;
    @ExcelProperty(value = "问题10")
    private String question10;
    @ExcelProperty(value = "回答10")
    private String answer10;

    @ExcelProperty(value = "状态")
    private String status;

    @ExcelProperty(value = "关联检查记录")
    private Long inspectionId;

    @ExcelProperty(value = "备注")
    private String remark;

    @ExcelProperty(value = "创建时间")
    private Date createTime;
}
