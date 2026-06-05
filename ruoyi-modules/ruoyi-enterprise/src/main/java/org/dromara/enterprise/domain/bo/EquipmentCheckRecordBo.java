package org.dromara.enterprise.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.enterprise.domain.EquipmentCheckRecord;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = EquipmentCheckRecord.class, reverseConvertGenerate = false)
public class EquipmentCheckRecordBo extends BaseEntity {

    private Long id;

    @Size(max = 100, message = "检查人员长度不能超过100个字符")
    private String inspector;

    @Size(max = 100, message = "记录员长度不能超过100个字符")
    private String recorder;

    private Date recordDate;

    @Size(max = 50, message = "检查类别长度不能超过50个字符")
    private String inspectionType;

    private Date inspectionStartTime;

    private Date inspectionEndTime;

    @Size(max = 200, message = "单位名称长度不能超过200个字符")
    private String unitName;

    @Size(max = 100, message = "统一社会信用代码长度不能超过100个字符")
    private String creditCode;

    @Size(max = 100, message = "法定代表人长度不能超过100个字符")
    private String legalRep;

    @Size(max = 100, message = "安全管理负责人长度不能超过100个字符")
    private String safetyManager;

    @Size(max = 500, message = "单位地址长度不能超过500个字符")
    private String unitAddress;

    @Size(max = 100, message = "联系人长度不能超过100个字符")
    private String contactPerson;

    @Size(max = 100, message = "职务长度不能超过100个字符")
    private String contactPosition;

    @Size(max = 50, message = "联系电话长度不能超过50个字符")
    private String contactPhone;

    @Size(max = 200, message = "单位类别长度不能超过200个字符")
    private String unitCategory;

    @Size(max = 200, message = "抽查设备名称长度不能超过200个字符")
    private String equipmentName;

    @Size(max = 200, message = "使用登记代码长度不能超过200个字符")
    private String registrationCode;

    private String findings;

    @Size(max = 200, message = "处理措施长度不能超过200个字符")
    private String disposition;

    private String unitOpinion;

    @Size(max = 100, message = "签名长度不能超过100个字符")
    private String unitSign;

    private Date signDate;

    @Size(max = 500, message = "备注长度不能超过500个字符")
    private String remark;
}
