package org.dromara.enterprise.domain.vo;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.dromara.enterprise.domain.EquipmentCheckRecord;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = EquipmentCheckRecord.class)
public class EquipmentCheckRecordVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @ExcelProperty(value = "序号")
    private Long id;

    @ExcelProperty(value = "检查人员")
    private String inspector;

    @ExcelProperty(value = "记录员")
    private String recorder;

    @ExcelProperty(value = "日期")
    private Date recordDate;

    @ExcelProperty(value = "检查类别")
    private String inspectionType;

    @ExcelProperty(value = "检查开始时间")
    private Date inspectionStartTime;

    @ExcelProperty(value = "检查结束时间")
    private Date inspectionEndTime;

    @ExcelProperty(value = "单位名称")
    private String unitName;

    @ExcelProperty(value = "统一社会信用代码")
    private String creditCode;

    @ExcelProperty(value = "法定代表人")
    private String legalRep;

    @ExcelProperty(value = "安全管理负责人")
    private String safetyManager;

    @ExcelProperty(value = "单位地址")
    private String unitAddress;

    @ExcelProperty(value = "联系人")
    private String contactPerson;

    @ExcelProperty(value = "职务")
    private String contactPosition;

    @ExcelProperty(value = "联系电话")
    private String contactPhone;

    @ExcelProperty(value = "单位类别")
    private String unitCategory;

    @ExcelProperty(value = "抽查设备名称")
    private String equipmentName;

    @ExcelProperty(value = "使用登记代码")
    private String registrationCode;

    @ExcelProperty(value = "检查情况")
    private String findings;

    @ExcelProperty(value = "处理措施")
    private String disposition;

    @ExcelProperty(value = "被检查单位意见")
    private String unitOpinion;

    @ExcelProperty(value = "签名")
    private String unitSign;

    @ExcelProperty(value = "签名日期")
    private Date signDate;

    @ExcelProperty(value = "备注")
    private String remark;

    @ExcelProperty(value = "创建时间")
    private Date createTime;
}
