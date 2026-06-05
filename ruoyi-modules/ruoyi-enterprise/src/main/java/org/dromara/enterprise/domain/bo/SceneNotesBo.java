package org.dromara.enterprise.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.enterprise.domain.SceneNotes;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = SceneNotes.class, reverseConvertGenerate = false)
public class SceneNotesBo extends BaseEntity {

    private Long id;

    private Date startTime;

    private Date endTime;

    @NotBlank(message = "地点不能为空")
    @Size(max = 300, message = "地点长度不能超过300个字符")
    private String place;

    @Size(max = 300, message = "检察人员长度不能超过300个字符")
    private String inspector;

    @Size(max = 300, message = "检查人员副本长度不能超过300个字符")
    private String inspectorCopy;

    @NotBlank(message = "当事人不能为空")
    @Size(max = 200, message = "当事人长度不能超过200个字符")
    private String party;

    @Size(max = 200, message = "主体资格证照名称长度不能超过200个字符")
    private String qualificationName;

    @Size(max = 500, message = "通知当事人到场情况长度不能超过500个字符")
    private String partyPresenceNotice;

    @Size(max = 100, message = "检查人员1长度不能超过100个字符")
    private String inspector1;

    @Size(max = 100, message = "当事人1长度不能超过100个字符")
    private String party1;

    @Size(max = 100, message = "检察人员2长度不能超过100个字符")
    private String inspector2;

    @Size(max = 100, message = "当事人2长度不能超过100个字符")
    private String party2;

    private String sceneSituation;

    private String partyStatement;

    private String status;

    private Long inspectionId;

    @Size(max = 500, message = "备注长度不能超过500个字符")
    private String remark;
}
