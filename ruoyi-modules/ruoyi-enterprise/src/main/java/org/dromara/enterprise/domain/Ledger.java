package org.dromara.enterprise.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.dromara.common.mybatis.core.domain.BaseEntity;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("biz_ledger")
public class Ledger extends BaseEntity {

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    private String date;

    private String inspectionDate;

    private String inspectedUnit;

    private String legalPerson;

    private String contactPhone;

    private String address;

    private String inspectionContent;

    private String rectificationMeasure;

    private String rectificationProgress;

    private String inspectorCategory;

    private String remark;

    private String unifiedSocialCreditCode;
}
