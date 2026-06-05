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
import org.dromara.enterprise.domain.bo.InspectorBo;
import org.dromara.enterprise.domain.vo.InspectorVo;
import org.dromara.enterprise.service.IInspectorService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/enterprise/inspector")
public class InspectorController extends BaseController {

    private final IInspectorService inspectorService;

    @SaCheckPermission("business:table:inspector:list")
    @GetMapping("/list")
    public TableDataInfo<InspectorVo> list(InspectorBo bo, PageQuery pageQuery) {
        return inspectorService.selectPageInspectorList(bo, pageQuery);
    }

    @GetMapping("/all")
    public R<List<InspectorVo>> all(InspectorBo bo) {
        return R.ok(inspectorService.selectInspectorList(bo));
    }

    @SaCheckPermission("business:table:inspector:query")
    @GetMapping("/{id}")
    public R<InspectorVo> getInfo(@PathVariable Long id) {
        return R.ok(inspectorService.selectInspectorById(id));
    }

    @SaCheckPermission("business:table:inspector:add")
    @Log(title = "检察人员管理", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Void> add(@Validated @RequestBody InspectorBo bo) {
        return toAjax(inspectorService.insertInspector(bo));
    }

    @SaCheckPermission("business:table:inspector:edit")
    @Log(title = "检察人员管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<Void> edit(@Validated @RequestBody InspectorBo bo) {
        return toAjax(inspectorService.updateInspector(bo));
    }

    @SaCheckPermission("business:table:inspector:remove")
    @Log(title = "检察人员管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@PathVariable Long[] ids) {
        return toAjax(inspectorService.deleteInspectorByIds(Arrays.asList(ids)));
    }

    @SaCheckPermission("business:table:inspector:export")
    @Log(title = "检察人员管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(InspectorBo bo, HttpServletResponse response) {
        List<InspectorVo> list = inspectorService.selectInspectorList(bo);
        ExcelUtil.exportExcel(list, "检察人员", InspectorVo.class, response);
    }
}
