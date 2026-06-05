package org.dromara.enterprise.domain.vo;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.dromara.enterprise.domain.Merchant;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = Merchant.class)
public class MerchantVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @ExcelProperty(value = "序号")
    private Long id;

    @ExcelProperty(value = "分类")
    private String category;

    @ExcelProperty(value = "单位名称")
    private String unitName;

    @ExcelProperty(value = "证件")
    private String certificate;

    @ExcelProperty(value = "食品证有效日期")
    private Date foodCertExpiryDate;

    @ExcelProperty(value = "法定代表人")
    private String legalPerson;

    @ExcelProperty(value = "身份证号")
    private String idCardNo;

    @ExcelProperty(value = "统一社会信用代码")
    private String unifiedSocialCreditCode;

    @ExcelProperty(value = "经营范围")
    private String businessScope;

    @ExcelProperty(value = "联系电话")
    private String contactPhone;

    @ExcelProperty(value = "营业执照照片")
    private String businessLicensePhoto;

    @ExcelProperty(value = "地址")
    private String address;

    @ExcelProperty(value = "食品证照片")
    private String foodCertPhoto;

    @ExcelProperty(value = "门面照片")
    private String storefrontPhoto;

    @ExcelProperty(value = "店内照片")
    private String interiorPhoto;

    @ExcelProperty(value = "状态")
    private String status;

    @ExcelProperty(value = "备注")
    private String remark;

    @ExcelProperty(value = "创建时间")
    private Date createTime;
}
