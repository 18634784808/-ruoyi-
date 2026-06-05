package org.dromara.enterprise.service;

import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.enterprise.domain.bo.InquiryNotesBo;
import org.dromara.enterprise.domain.vo.InquiryNotesVo;

import java.util.List;

public interface IInquiryNotesService {

    TableDataInfo<InquiryNotesVo> selectPageInquiryNotesList(InquiryNotesBo bo, PageQuery pageQuery);

    List<InquiryNotesVo> selectInquiryNotesList(InquiryNotesBo bo);

    InquiryNotesVo selectInquiryNotesById(Long id);

    int insertInquiryNotes(InquiryNotesBo bo);

    int updateInquiryNotes(InquiryNotesBo bo);

    int deleteInquiryNotesByIds(List<Long> ids);
}
