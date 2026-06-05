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
@TableName("biz_service_address_confirmation")
public class ServiceAddressConfirmation extends BaseEntity {

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    private String recipientName;

    private String acceptElectronic;

    private String faxNumber;

    private String emailAddress;

    private String instantAccount;

    private String serviceAddress;

    private String recipientPerson;

    private String recipientPhone;

    private String postalCode;

    private String signName;

    private Date signDate;

    private String remark;
}
