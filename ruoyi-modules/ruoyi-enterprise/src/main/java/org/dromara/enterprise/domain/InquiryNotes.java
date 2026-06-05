package org.dromara.enterprise.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.dromara.common.mybatis.core.domain.BaseEntity;

import java.util.Date;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("biz_inquiry_notes")
public class InquiryNotes extends BaseEntity {

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    private Date startTime;

    private Date endTime;

    private Integer times;

    private Integer questionCount;

    private String place;

    private String inquirer;

    private String inquirerCopy;

    private String party;

    private String gender;

    private String otherContact;

    private String position;

    private String inquirer1;

    private String respondent1;

    private String inquirer2;

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

    private String remark;
}
