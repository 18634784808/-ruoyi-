package org.dromara.enterprise.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.common.excel.utils.ExcelUtil;
import org.dromara.common.log.annotation.Log;
import org.dromara.common.log.enums.BusinessType;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.web.core.BaseController;
import org.dromara.enterprise.domain.bo.EquipmentCheckRecordBo;
import org.dromara.enterprise.domain.vo.EquipmentCheckRecordVo;
import org.dromara.enterprise.service.IEquipmentCheckRecordService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/enterprise/equipment-check-record")
public class EquipmentCheckRecordController extends BaseController {

    private final IEquipmentCheckRecordService service;

    @SaCheckPermission("business:table:equipment-check-record:list")
    @GetMapping("/list")
    public TableDataInfo<EquipmentCheckRecordVo> list(EquipmentCheckRecordBo bo, PageQuery pageQuery) {
        return service.selectPageList(bo, pageQuery);
    }

    @GetMapping("/all")
    public R<List<EquipmentCheckRecordVo>> all(EquipmentCheckRecordBo bo) {
        return R.ok(service.selectList(bo));
    }

    @SaCheckPermission("business:table:equipment-check-record:query")
    @GetMapping("/{id}")
    public R<EquipmentCheckRecordVo> getInfo(@PathVariable Long id) {
        return R.ok(service.selectById(id));
    }

    @SaCheckPermission("business:table:equipment-check-record:add")
    @Log(title = "特种设备安全监督检查记录表", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Void> add(@Validated @RequestBody EquipmentCheckRecordBo bo) {
        return toAjax(service.insert(bo));
    }

    @SaCheckPermission("business:table:equipment-check-record:edit")
    @Log(title = "特种设备安全监督检查记录表", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<Void> edit(@Validated @RequestBody EquipmentCheckRecordBo bo) {
        return toAjax(service.update(bo));
    }

    @SaCheckPermission("business:table:equipment-check-record:remove")
    @Log(title = "特种设备安全监督检查记录表", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@PathVariable Long[] ids) {
        return toAjax(service.deleteByIds(Arrays.asList(ids)));
    }

    @SaCheckPermission("business:table:equipment-check-record:export")
    @Log(title = "特种设备安全监督检查记录表", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(EquipmentCheckRecordBo bo, HttpServletResponse response) {
        List<EquipmentCheckRecordVo> list = service.selectList(bo);
        ExcelUtil.exportExcel(list, "特种设备安全监督检查记录表", EquipmentCheckRecordVo.class, response);
    }
}
