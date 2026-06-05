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
import org.dromara.enterprise.domain.bo.FacilityChecklistBo;
import org.dromara.enterprise.domain.vo.FacilityChecklistVo;
import org.dromara.enterprise.service.IFacilityChecklistService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/enterprise/facility-checklist")
public class FacilityChecklistController extends BaseController {

    private final IFacilityChecklistService service;

    @SaCheckPermission("business:table:facility:list")
    @GetMapping("/list")
    public TableDataInfo<FacilityChecklistVo> list(FacilityChecklistBo bo, PageQuery pageQuery) {
        return service.selectPageList(bo, pageQuery);
    }

    @GetMapping("/all")
    public R<List<FacilityChecklistVo>> all(FacilityChecklistBo bo) {
        return R.ok(service.selectList(bo));
    }

    @SaCheckPermission("business:table:facility:query")
    @GetMapping("/{id}")
    public R<FacilityChecklistVo> getInfo(@PathVariable Long id) {
        return R.ok(service.selectById(id));
    }

    @SaCheckPermission("business:table:facility:add")
    @Log(title = "场所设施财物清单", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Void> add(@Validated @RequestBody FacilityChecklistBo bo) {
        return toAjax(service.insert(bo));
    }

    @SaCheckPermission("business:table:facility:edit")
    @Log(title = "场所设施财物清单", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<Void> edit(@Validated @RequestBody FacilityChecklistBo bo) {
        return toAjax(service.update(bo));
    }

    @SaCheckPermission("business:table:facility:remove")
    @Log(title = "场所设施财物清单", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@PathVariable Long[] ids) {
        return toAjax(service.deleteByIds(Arrays.asList(ids)));
    }

    @SaCheckPermission("business:table:facility:export")
    @Log(title = "场所设施财物清单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(FacilityChecklistBo bo, HttpServletResponse response) {
        List<FacilityChecklistVo> list = service.selectList(bo);
        ExcelUtil.exportExcel(list, "场所设施财物清单", FacilityChecklistVo.class, response);
    }
}
