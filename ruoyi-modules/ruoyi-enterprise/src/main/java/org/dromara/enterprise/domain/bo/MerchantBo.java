package org.dromara.enterprise.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.enterprise.domain.Merchant;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = Merchant.class, reverseConvertGenerate = false)
public class MerchantBo extends BaseEntity {

    private Long id;

    @Size(max = 100, message = "分类长度不能超过100个字符")
    private String category;

    @NotBlank(message = "单位名称不能为空")
    @Size(max = 300, message = "单位名称长度不能超过300个字符")
    private String unitName;

    @Size(max = 100, message = "证件长度不能超过100个字符")
    private String certificate;

    private Date foodCertExpiryDate;

    @Size(max = 100, message = "法定代表人长度不能超过100个字符")
    private String legalPerson;

    @Size(max = 18, message = "身份证号长度不能超过18个字符")
    private String idCardNo;

    @Size(max = 18, message = "统一社会信用代码长度不能超过18个字符")
    private String unifiedSocialCreditCode;

    @Size(max = 1000, message = "经营范围长度不能超过1000个字符")
    private String businessScope;

    @Size(max = 20, message = "联系电话长度不能超过20个字符")
    private String contactPhone;

    private String businessLicensePhoto;

    @Size(max = 500, message = "地址长度不能超过500个字符")
    private String address;

    private String foodCertPhoto;

    private String storefrontPhoto;

    private String interiorPhoto;

    private String status;

    @Size(max = 500, message = "备注长度不能超过500个字符")
    private String remark;
}
