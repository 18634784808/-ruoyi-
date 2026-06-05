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
@TableName("biz_equipment_safety_directive")
public class EquipmentSafetyDirective extends BaseEntity {

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    private String issuingAgency;

    private String docYear;

    private String docSeq;

    private String docNumber;

    private String inspectedUnit;

    private String problemsFound;

    private String violationDesc;

    private String violationArticle;

    private String basisRule;

    private Date deadlineDate;

    private String correctiveMeasures;

    private String appealGov;

    private String appealCourt;

    private String inspectorSign;

    private String unitSign;

    private String sealInfo;

    private Date signDate;

    private String remark;
}
