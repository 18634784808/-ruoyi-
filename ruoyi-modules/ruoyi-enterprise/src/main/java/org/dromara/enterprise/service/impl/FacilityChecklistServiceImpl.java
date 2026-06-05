package org.dromara.enterprise.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.enterprise.domain.FacilityChecklist;
import org.dromara.enterprise.domain.FacilityChecklistItem;
import org.dromara.enterprise.domain.bo.FacilityChecklistBo;
import org.dromara.enterprise.domain.bo.FacilityChecklistItemBo;
import org.dromara.enterprise.domain.vo.FacilityChecklistItemVo;
import org.dromara.enterprise.domain.vo.FacilityChecklistVo;
import org.dromara.enterprise.mapper.FacilityChecklistItemMapper;
import org.dromara.enterprise.mapper.FacilityChecklistMapper;
import org.dromara.enterprise.service.IFacilityChecklistService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class FacilityChecklistServiceImpl implements IFacilityChecklistService {

    private final FacilityChecklistMapper checklistMapper;
    private final FacilityChecklistItemMapper itemMapper;

    @Override
    public TableDataInfo<FacilityChecklistVo> selectPageList(FacilityChecklistBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<FacilityChecklist> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(bo.getDocumentNo() != null, FacilityChecklist::getDocumentNo, bo.getDocumentNo());
        wrapper.orderByDesc(FacilityChecklist::getCreateTime);
        Page<FacilityChecklistVo> page = checklistMapper.selectVoPage(pageQuery.build(), wrapper);
        return TableDataInfo.build(page);
    }

    @Override
    public List<FacilityChecklistVo> selectList(FacilityChecklistBo bo) {
        LambdaQueryWrapper<FacilityChecklist> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(FacilityChecklist::getCreateTime);
        return checklistMapper.selectVoList(wrapper);
    }

    @Override
    public FacilityChecklistVo selectById(Long id) {
        FacilityChecklistVo vo = checklistMapper.selectVoById(id);
        if (vo != null) {
            LambdaQueryWrapper<FacilityChecklistItem> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(FacilityChecklistItem::getChecklistId, id);
            wrapper.orderByAsc(FacilityChecklistItem::getSeqNo);
            List<FacilityChecklistItem> items = itemMapper.selectList(wrapper);
            vo.setItems(BeanUtil.copyToList(items, FacilityChecklistItemVo.class));
        }
        return vo;
    }

    @Override
    @Transactional
    public int insert(FacilityChecklistBo bo) {
        FacilityChecklist entity = MapstructUtils.convert(bo, FacilityChecklist.class);
        int rows = checklistMapper.insert(entity);
        // 保存明细
        saveItems(entity.getId(), bo.getItems());
        return rows;
    }

    @Override
    @Transactional
    public int update(FacilityChecklistBo bo) {
        FacilityChecklist entity = MapstructUtils.convert(bo, FacilityChecklist.class);
        int rows = checklistMapper.updateById(entity);
        // 先删后增明细
        LambdaQueryWrapper<FacilityChecklistItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FacilityChecklistItem::getChecklistId, bo.getId());
        itemMapper.delete(wrapper);
        saveItems(bo.getId(), bo.getItems());
        return rows;
    }

    @Override
    @Transactional
    public int deleteByIds(List<Long> ids) {
        for (Long id : ids) {
            LambdaQueryWrapper<FacilityChecklistItem> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(FacilityChecklistItem::getChecklistId, id);
            itemMapper.delete(wrapper);
        }
        return checklistMapper.deleteByIds(ids);
    }

    private void saveItems(Long checklistId, List<FacilityChecklistItemBo> itemBos) {
        if (itemBos == null || itemBos.isEmpty()) return;
        for (FacilityChecklistItemBo itemBo : itemBos) {
            FacilityChecklistItem item = BeanUtil.toBean(itemBo, FacilityChecklistItem.class);
            item.setId(null);
            item.setChecklistId(checklistId);
            itemMapper.insert(item);
        }
    }
}
