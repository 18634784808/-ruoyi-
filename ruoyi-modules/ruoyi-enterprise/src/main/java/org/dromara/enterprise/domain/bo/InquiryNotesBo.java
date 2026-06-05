package org.dromara.enterprise.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.enterprise.domain.InquiryNotes;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = InquiryNotes.class, reverseConvertGenerate = false)
public class InquiryNotesBo extends BaseEntity {

    private Long id;

    private Date startTime;

    private Date endTime;

    private Integer times;

    private Integer questionCount;

    @NotBlank(message = "地点不能为空")
    @Size(max = 300, message = "地点长度不能超过300个字符")
    private String place;

    @Size(max = 300, message = "询问人长度不能超过300个字符")
    private String inquirer;

    @Size(max = 300, message = "询问人副本长度不能超过300个字符")
    private String inquirerCopy;

    @NotBlank(message = "当事人不能为空")
    @Size(max = 200, message = "当事人长度不能超过200个字符")
    private String party;

    @Size(max = 20, message = "性别长度不能超过20个字符")
    private String gender;

    @Size(max = 200, message = "其他联系方式长度不能超过200个字符")
    private String otherContact;

    @Size(max = 200, message = "职务长度不能超过200个字符")
    private String position;

    @Size(max = 100, message = "询问人1长度不能超过100个字符")
    private String inquirer1;

    @Size(max = 100, message = "被询问人1长度不能超过100个字符")
    private String respondent1;

    @Size(max = 100, message = "询问人2长度不能超过100个字符")
    private String inquirer2;

    @Size(max = 100, message = "被询问人2长度不能超过100个字符")
    private String respondent2;

    private String question1;
    private String answer1;
    private String question2;
    private String answer2;
    private String question3;
    private String answer3;
    private String question4;
    private String answer4;
    private String question5;
    private String answer5;
    private String question6;
    private String answer6;
    private String question7;
    private String answer7;
    private String question8;
    private String answer8;
    private String question9;
    private String answer9;
    private String question10;
    private String answer10;

    private String status;

    private Long inspectionId;

    @Size(max = 500, message = "备注长度不能超过500个字符")
    private String remark;
}
