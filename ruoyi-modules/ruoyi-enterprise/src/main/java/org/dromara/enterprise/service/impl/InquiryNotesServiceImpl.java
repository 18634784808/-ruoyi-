package org.dromara.enterprise.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.enterprise.domain.InquiryNotes;
import org.dromara.enterprise.domain.bo.InquiryNotesBo;
import org.dromara.enterprise.domain.vo.InquiryNotesVo;
import org.dromara.enterprise.mapper.InquiryNotesMapper;
import org.dromara.enterprise.service.IInquiryNotesService;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class InquiryNotesServiceImpl implements IInquiryNotesService {

    private final InquiryNotesMapper baseMapper;

    private LambdaQueryWrapper<InquiryNotes> buildQueryWrapper(InquiryNotesBo bo) {
        return new LambdaQueryWrapper<InquiryNotes>()
            .like(StringUtils.isNotBlank(bo.getPlace()), InquiryNotes::getPlace, bo.getPlace())
            .like(StringUtils.isNotBlank(bo.getParty()), InquiryNotes::getParty, bo.getParty())
            .like(StringUtils.isNotBlank(bo.getInquirer()), InquiryNotes::getInquirer, bo.getInquirer())
            .eq(StringUtils.isNotBlank(bo.getStatus()), InquiryNotes::getStatus, bo.getStatus())
            .orderByDesc(InquiryNotes::getCreateTime);
    }

    @Override
    public TableDataInfo<InquiryNotesVo> selectPageInquiryNotesList(InquiryNotesBo bo, PageQuery pageQuery) {
        Page<InquiryNotesVo> page = baseMapper.selectVoPage(pageQuery.build(), buildQueryWrapper(bo));
        return TableDataInfo.build(page);
    }

    @Override
    public List<InquiryNotesVo> selectInquiryNotesList(InquiryNotesBo bo) {
        return baseMapper.selectVoList(buildQueryWrapper(bo));
    }

    @Override
    public InquiryNotesVo selectInquiryNotesById(Long id) {
        return baseMapper.selectVoById(id);
    }

    @Override
    public int insertInquiryNotes(InquiryNotesBo bo) {
        InquiryNotes entity = MapstructUtils.convert(bo, InquiryNotes.class);
        return baseMapper.insert(entity);
    }

    @Override
    public int updateInquiryNotes(InquiryNotesBo bo) {
        InquiryNotes entity = MapstructUtils.convert(bo, InquiryNotes.class);
        return baseMapper.updateById(entity);
    }

    @Override
    public int deleteInquiryNotesByIds(List<Long> ids) {
        return baseMapper.deleteByIds(ids);
    }
}
