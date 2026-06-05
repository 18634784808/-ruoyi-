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
import org.dromara.enterprise.domain.bo.EnforcementDecisionBo;
import org.dromara.enterprise.domain.vo.EnforcementDecisionVo;
import org.dromara.enterprise.service.IEnforcementDecisionService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/enterprise/enforcement-decision")
public class EnforcementDecisionController extends BaseController {

    private final IEnforcementDecisionService service;

    @SaCheckPermission("business:table:enforcement:list")
    @GetMapping("/list")
    public TableDataInfo<EnforcementDecisionVo> list(EnforcementDecisionBo bo, PageQuery pageQuery) {
        return service.selectPageList(bo, pageQuery);
    }

    @GetMapping("/all")
    public R<List<EnforcementDecisionVo>> all(EnforcementDecisionBo bo) {
        return R.ok(service.selectList(bo));
    }

    @SaCheckPermission("business:table:enforcement:query")
    @GetMapping("/{id}")
    public R<EnforcementDecisionVo> getInfo(@PathVariable Long id) {
        return R.ok(service.selectById(id));
    }

    @SaCheckPermission("business:table:enforcement:add")
    @Log(title = "实施行政强制措施决定书", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Void> add(@Validated @RequestBody EnforcementDecisionBo bo) {
        return toAjax(service.insert(bo));
    }

    @SaCheckPermission("business:table:enforcement:edit")
    @Log(title = "实施行政强制措施决定书", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<Void> edit(@Validated @RequestBody EnforcementDecisionBo bo) {
        return toAjax(service.update(bo));
    }

    @SaCheckPermission("business:table:enforcement:remove")
    @Log(title = "实施行政强制措施决定书", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@PathVariable Long[] ids) {
        return toAjax(service.deleteByIds(Arrays.asList(ids)));
    }

    @SaCheckPermission("business:table:enforcement:export")
    @Log(title = "实施行政强制措施决定书", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(EnforcementDecisionBo bo, HttpServletResponse response) {
        List<EnforcementDecisionVo> list = service.selectList(bo);
        ExcelUtil.exportExcel(list, "实施行政强制措施决定书", EnforcementDecisionVo.class, response);
    }
}
