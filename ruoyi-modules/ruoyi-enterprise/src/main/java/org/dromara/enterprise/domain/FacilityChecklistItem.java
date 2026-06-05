package org.dromara.enterprise.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@TableName("biz_facility_checklist_item")
public class FacilityChecklistItem {

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    private Long checklistId;

    private Integer seqNo;

    private String nameOrVenue;

    private String specOrAddress;

    private String unit;

    private String quantity;

    private String remark;
}
