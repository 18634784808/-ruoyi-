package org.dromara.enterprise.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.dromara.common.mybatis.core.domain.BaseEntity;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("biz_enforcement_decision")
public class EnforcementDecision extends BaseEntity {

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    private String year;

    private String docSeq;

    private String docNumber;

    private String partyName;

    private String licenseName;

    private String creditCode;

    private String address;

    private String legalPerson;

    private String idNumber;

    private String phone;

    private String otherContact;

    private String violationDesc;

    private String legalBasis;

    private String facilityListNo;

    private String enforceLocation;

    private Integer enforceDuration;

    private String appealDays;

    private String appealGov;

    private String appealAgency;

    private String lawsuitDays;

    private String lawsuitCourt;

    private String contactPerson;

    private String contactPhone;

    private String attachmentListNo;

    private String signDate;

    private String copyCount;

    private String deliveryCount;

    private String copyNote;

    private String status;

    private String remark;
}
