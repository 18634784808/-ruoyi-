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
@TableName("biz_facility_checklist")
public class FacilityChecklist extends BaseEntity {

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    private String documentNo;

    private String confirmationText;

    private String partySignature;

    private String officerSignature;

    private String witnessSignature;

    private Date partyDate;

    private Date officerDate;

    private Integer copiesCount;

    private Integer pageNo;

    private Integer totalPages;

    private String status;

    private String remark;
}
