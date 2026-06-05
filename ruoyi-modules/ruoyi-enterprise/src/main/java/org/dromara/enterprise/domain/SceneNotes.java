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
@TableName("biz_scene_notes")
public class SceneNotes extends BaseEntity {

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    private Date startTime;

    private Date endTime;

    private String place;

    private String inspector;

    private String inspectorCopy;

    private String party;

    private String qualificationName;

    private String partyPresenceNotice;

    private String inspector1;

    private String party1;

    private String inspector2;

    private String party2;

    private String sceneSituation;

    private String partyStatement;

    private String status;

    private Long inspectionId;

    private String remark;
}
