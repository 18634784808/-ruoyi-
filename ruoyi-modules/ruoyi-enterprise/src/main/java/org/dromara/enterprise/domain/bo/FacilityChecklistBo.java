package org.dromara.enterprise.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.enterprise.domain.FacilityChecklist;

import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = FacilityChecklist.class, reverseConvertGenerate = false)
public class FacilityChecklistBo extends BaseEntity {

    private Long id;

    @Size(max = 100, message = "文书编号长度不能超过100个字符")
    private String documentNo;

    @Size(max = 500, message = "核对文字长度不能超过500个字符")
    private String confirmationText;

    @Size(max = 100, message = "签名长度不能超过100个字符")
    private String partySignature;

    @Size(max = 100, message = "签名长度不能超过100个字符")
    private String officerSignature;

    @Size(max = 100, message = "签名长度不能超过100个字符")
    private String witnessSignature;

    private Date partyDate;

    private Date officerDate;

    private Integer copiesCount;

    private Integer pageNo;

    private Integer totalPages;

    private String status;

    @Size(max = 500, message = "备注长度不能超过500个字符")
    private String remark;

    /** 清单明细 */
    private List<FacilityChecklistItemBo> items;
}
