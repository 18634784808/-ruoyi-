// 查询参数
export interface FacilityChecklistQuery {
  pageNum: number;
  pageSize: number;
  documentNo?: string;
  params?: Record<string, any>;
}

// 清单明细
export interface FacilityChecklistItemVO {
  id?: string;
  checklistId?: string;
  seqNo: number;
  nameOrVenue: string;
  specOrAddress: string;
  unit: string;
  quantity: string;
  remark: string;
}

// 清单记录
export interface FacilityChecklistVO {
  id: string;
  documentNo: string;
  confirmationText: string;
  partySignature: string;
  officerSignature: string;
  witnessSignature: string;
  partyDate: string;
  officerDate: string;
  copiesCount: number;
  pageNo: number;
  totalPages: number;
  status: string;
  remark: string;
  createTime: string;
  items: FacilityChecklistItemVO[];
}

// 表单数据
export interface FacilityChecklistForm {
  id?: string;
  documentNo?: string;
  confirmationText?: string;
  partySignature?: string;
  officerSignature?: string;
  witnessSignature?: string;
  partyDate?: string;
  officerDate?: string;
  copiesCount?: number;
  pageNo?: number;
  totalPages?: number;
  status: string;
  remark?: string;
  items: FacilityChecklistItemVO[];
}
