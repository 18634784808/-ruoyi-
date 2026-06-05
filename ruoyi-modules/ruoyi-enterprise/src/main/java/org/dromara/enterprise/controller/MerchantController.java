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
import org.dromara.enterprise.domain.bo.MerchantBo;
import org.dromara.enterprise.domain.vo.MerchantVo;
import org.dromara.enterprise.service.IMerchantService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/enterprise/merchant")
public class MerchantController extends BaseController {

    private final IMerchantService merchantService;

    @SaCheckPermission("business:table:merchant:list")
    @GetMapping("/list")
    public TableDataInfo<MerchantVo> list(MerchantBo bo, PageQuery pageQuery) {
        return merchantService.selectPageMerchantList(bo, pageQuery);
    }

    @Log(title = "商户管理", businessType = BusinessType.EXPORT)
    @SaCheckPermission("business:table:merchant:export")
    @PostMapping("/export")
    public void export(MerchantBo bo, HttpServletResponse response) {
        List<MerchantVo> list = merchantService.selectMerchantList(bo);
        ExcelUtil.exportExcel(list, "商户数据", MerchantVo.class, response);
    }

    @SaCheckPermission("business:table:merchant:query")
    @GetMapping("/{id}")
    public R<MerchantVo> getInfo(@PathVariable Long id) {
        return R.ok(merchantService.selectMerchantById(id));
    }

    @GetMapping("/findByName")
    public R<MerchantVo> findByName(@RequestParam String name) {
        MerchantVo vo = merchantService.selectMerchantByName(name);
        return vo != null ? R.ok(vo) : R.fail("未找到该商户");
    }

    @GetMapping("/all")
    public R<List<MerchantVo>> all() {
        return R.ok(merchantService.selectMerchantList(new MerchantBo()));
    }

    @SaCheckPermission("business:table:merchant:add")
    @Log(title = "商户管理", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Void> add(@Validated @RequestBody MerchantBo bo) {
        return toAjax(merchantService.insertMerchant(bo));
    }

    @SaCheckPermission("business:table:merchant:edit")
    @Log(title = "商户管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<Void> edit(@Validated @RequestBody MerchantBo bo) {
        return toAjax(merchantService.updateMerchant(bo));
    }

    @SaCheckPermission("business:table:merchant:remove")
    @Log(title = "商户管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@PathVariable Long[] ids) {
        return toAjax(merchantService.deleteMerchantByIds(Arrays.asList(ids)));
    }
}
