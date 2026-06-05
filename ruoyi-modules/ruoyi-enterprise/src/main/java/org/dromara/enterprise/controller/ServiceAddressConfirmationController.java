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
import org.dromara.enterprise.domain.bo.ServiceAddressConfirmationBo;
import org.dromara.enterprise.domain.vo.ServiceAddressConfirmationVo;
import org.dromara.enterprise.service.IServiceAddressConfirmationService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/enterprise/service-address-confirmation")
public class ServiceAddressConfirmationController extends BaseController {

    private final IServiceAddressConfirmationService service;

    @SaCheckPermission("business:table:service-address-confirmation:list")
    @GetMapping("/list")
    public TableDataInfo<ServiceAddressConfirmationVo> list(ServiceAddressConfirmationBo bo, PageQuery pageQuery) {
        return service.selectPageList(bo, pageQuery);
    }

    @GetMapping("/all")
    public R<List<ServiceAddressConfirmationVo>> all(ServiceAddressConfirmationBo bo) {
        return R.ok(service.selectList(bo));
    }

    @SaCheckPermission("business:table:service-address-confirmation:query")
    @GetMapping("/{id}")
    public R<ServiceAddressConfirmationVo> getInfo(@PathVariable Long id) {
        return R.ok(service.selectById(id));
    }

    @SaCheckPermission("business:table:service-address-confirmation:add")
    @Log(title = "送达地址确认书", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Void> add(@Validated @RequestBody ServiceAddressConfirmationBo bo) {
        return toAjax(service.insert(bo));
    }

    @SaCheckPermission("business:table:service-address-confirmation:edit")
    @Log(title = "送达地址确认书", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<Void> edit(@Validated @RequestBody ServiceAddressConfirmationBo bo) {
        return toAjax(service.update(bo));
    }

    @SaCheckPermission("business:table:service-address-confirmation:remove")
    @Log(title = "送达地址确认书", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@PathVariable Long[] ids) {
        return toAjax(service.deleteByIds(Arrays.asList(ids)));
    }

    @SaCheckPermission("business:table:service-address-confirmation:export")
    @Log(title = "送达地址确认书", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(ServiceAddressConfirmationBo bo, HttpServletResponse response) {
        List<ServiceAddressConfirmationVo> list = service.selectList(bo);
        ExcelUtil.exportExcel(list, "送达地址确认书", ServiceAddressConfirmationVo.class, response);
    }
}
