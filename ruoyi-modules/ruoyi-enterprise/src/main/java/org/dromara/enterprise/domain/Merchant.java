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
@TableName("biz_merchant")
public class Merchant extends BaseEntity {

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    private String category;

    private String unitName;

    private String certificate;

    private Date foodCertExpiryDate;

    private String legalPerson;

    private String idCardNo;

    private String unifiedSocialCreditCode;

    private String businessScope;

    private String contactPhone;

    private String businessLicensePhoto;

    private String address;

    private String foodCertPhoto;

    private String storefrontPhoto;

    private String interiorPhoto;

    private String status;

    private String remark;
}
