package org.dromara.enterprise.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.dromara.common.mybatis.core.domain.BaseEntity;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("biz_dict")
public class BizDict extends BaseEntity {

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    private String dictType;

    private String label;

    private String value;

    private Integer sort;

    private String status;

    private String tagType;

    private String remark;
}
