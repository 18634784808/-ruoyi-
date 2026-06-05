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
import org.dromara.enterprise.domain.bo.EquipmentSafetyDirectiveBo;
import org.dromara.enterprise.domain.vo.EquipmentSafetyDirectiveVo;
import org.dromara.enterprise.service.IEquipmentSafetyDirectiveService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/enterprise/equipment-safety-directive")
public class EquipmentSafetyDirectiveController extends BaseController {

    private final IEquipmentSafetyDirectiveService service;

    @SaCheckPermission("business:table:equipment-safety-directive:list")
    @GetMapping("/list")
    public TableDataInfo<EquipmentSafetyDirectiveVo> list(EquipmentSafetyDirectiveBo bo, PageQuery pageQuery) {
        return service.selectPageList(bo, pageQuery);
    }

    @GetMapping("/all")
    public R<List<EquipmentSafetyDirectiveVo>> all(EquipmentSafetyDirectiveBo bo) {
        return R.ok(service.selectList(bo));
    }

    @SaCheckPermission("business:table:equipment-safety-directive:query")
    @GetMapping("/{id}")
    public R<EquipmentSafetyDirectiveVo> getInfo(@PathVariable Long id) {
        return R.ok(service.selectById(id));
    }

    @SaCheckPermission("business:table:equipment-safety-directive:add")
    @Log(title = "特种设备安全监察指令书", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Void> add(@Validated @RequestBody EquipmentSafetyDirectiveBo bo) {
        return toAjax(service.insert(bo));
    }

    @SaCheckPermission("business:table:equipment-safety-directive:edit")
    @Log(title = "特种设备安全监察指令书", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<Void> edit(@Validated @RequestBody EquipmentSafetyDirectiveBo bo) {
        return toAjax(service.update(bo));
    }

    @SaCheckPermission("business:table:equipment-safety-directive:remove")
    @Log(title = "特种设备安全监察指令书", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@PathVariable Long[] ids) {
        return toAjax(service.deleteByIds(Arrays.asList(ids)));
    }

    @SaCheckPermission("business:table:equipment-safety-directive:export")
    @Log(title = "特种设备安全监察指令书", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(EquipmentSafetyDirectiveBo bo, HttpServletResponse response) {
        List<EquipmentSafetyDirectiveVo> list = service.selectList(bo);
        ExcelUtil.exportExcel(list, "特种设备安全监察指令书", EquipmentSafetyDirectiveVo.class, response);
    }
}
