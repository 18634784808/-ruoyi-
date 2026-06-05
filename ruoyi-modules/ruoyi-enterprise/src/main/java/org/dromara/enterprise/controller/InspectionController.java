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
import org.dromara.enterprise.domain.bo.InspectionBo;
import org.dromara.enterprise.domain.vo.InspectionVo;
import org.dromara.enterprise.service.IInspectionService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/enterprise/inspection")
public class InspectionController extends BaseController {

    private final IInspectionService inspectionService;

    @SaCheckPermission("business:table:inspection:list")
    @GetMapping("/list")
    public TableDataInfo<InspectionVo> list(InspectionBo bo, PageQuery pageQuery) {
        return inspectionService.selectPageInspectionList(bo, pageQuery);
    }

    @Log(title = "检查记录管理", businessType = BusinessType.EXPORT)
    @SaCheckPermission("business:table:inspection:export")
    @PostMapping("/export")
    public void export(InspectionBo bo, HttpServletResponse response) {
        List<InspectionVo> list = inspectionService.selectInspectionList(bo);
        ExcelUtil.exportExcel(list, "检查记录数据", InspectionVo.class, response);
    }

    @SaCheckPermission("business:table:inspection:query")
    @GetMapping("/{id}")
    public R<InspectionVo> getInfo(@PathVariable Long id) {
        return R.ok(inspectionService.selectInspectionById(id));
    }

    @SaCheckPermission("business:table:inspection:add")
    @Log(title = "检查记录管理", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Void> add(@Validated @RequestBody InspectionBo bo) {
        return toAjax(inspectionService.insertInspection(bo));
    }

    @SaCheckPermission("business:table:inspection:edit")
    @Log(title = "检查记录管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<Void> edit(@Validated @RequestBody InspectionBo bo) {
        return toAjax(inspectionService.updateInspection(bo));
    }

    @SaCheckPermission("business:table:inspection:remove")
    @Log(title = "检查记录管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@PathVariable Long[] ids) {
        return toAjax(inspectionService.deleteInspectionByIds(Arrays.asList(ids)));
    }
}
