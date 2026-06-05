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
import org.dromara.enterprise.domain.bo.BizDictBo;
import org.dromara.enterprise.domain.vo.BizDictVo;
import org.dromara.enterprise.service.IBizDictService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/enterprise/dict")
public class BizDictController extends BaseController {

    private final IBizDictService bizDictService;

    @SaCheckPermission("business:table:dict:list")
    @GetMapping("/list")
    public TableDataInfo<BizDictVo> list(BizDictBo bo, PageQuery pageQuery) {
        return bizDictService.selectPageBizDictList(bo, pageQuery);
    }

    @GetMapping("/all")
    public R<List<BizDictVo>> all(BizDictBo bo) {
        return R.ok(bizDictService.selectBizDictList(bo));
    }

    @SaCheckPermission("business:table:dict:query")
    @GetMapping("/{id}")
    public R<BizDictVo> getInfo(@PathVariable Long id) {
        return R.ok(bizDictService.selectBizDictById(id));
    }

    @SaCheckPermission("business:table:dict:add")
    @Log(title = "业务字典管理", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Void> add(@Validated @RequestBody BizDictBo bo) {
        return toAjax(bizDictService.insertBizDict(bo));
    }

    @SaCheckPermission("business:table:dict:edit")
    @Log(title = "业务字典管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<Void> edit(@Validated @RequestBody BizDictBo bo) {
        return toAjax(bizDictService.updateBizDict(bo));
    }

    @SaCheckPermission("business:table:dict:remove")
    @Log(title = "业务字典管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@PathVariable Long[] ids) {
        return toAjax(bizDictService.deleteBizDictByIds(Arrays.asList(ids)));
    }

    @SaCheckPermission("business:table:dict:export")
    @Log(title = "业务字典管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(BizDictBo bo, HttpServletResponse response) {
        List<BizDictVo> list = bizDictService.selectBizDictList(bo);
        ExcelUtil.exportExcel(list, "业务字典", BizDictVo.class, response);
    }
}
