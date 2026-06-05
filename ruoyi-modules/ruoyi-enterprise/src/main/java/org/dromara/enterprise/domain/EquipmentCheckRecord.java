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
@TableName("biz_equipment_check_record")
public class EquipmentCheckRecord extends BaseEntity {

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    private String inspector;

    private String recorder;

    private Date recordDate;

    private String inspectionType;

    private Date inspectionStartTime;

    private Date inspectionEndTime;

    private String unitName;

    private String creditCode;

    private String legalRep;

    private String safetyManager;

    private String unitAddress;

    private String contactPerson;

    private String contactPosition;

    private String contactPhone;

    private String unitCategory;

    private String equipmentName;

    private String registrationCode;

    private String findings;

    private String disposition;

    private String unitOpinion;

    private String unitSign;

    private Date signDate;

    private String remark;
}
