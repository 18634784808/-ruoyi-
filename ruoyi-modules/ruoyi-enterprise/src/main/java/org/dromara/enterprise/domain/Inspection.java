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
@TableName("biz_inspection")
public class Inspection extends BaseEntity {

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    private String category;

    private String inspectedUnit;

    private String legalPerson;

    private String contactPhone;

    private String unifiedSocialCreditCode;

    private String address;

    private Date inspectionDate;

    private String inspectionType;

    private String inspectionSituation;

    private String handlingOpinion;

    private String inspectedUnitSignature;

    private Integer inspectorCount;

    private String inspector1;

    private String inspector2;

    private String inspector3;

    private String inspector4;

    private String rectificationReport;

    private String sceneNotes;

    private String ledgerUnit;

    private Long merchantId;

    private String status;

    private String remark;
}
