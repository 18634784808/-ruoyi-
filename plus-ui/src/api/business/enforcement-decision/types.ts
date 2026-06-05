// 查询参数
export interface EnforcementDecisionQuery {
  pageNum: number;
  pageSize: number;
  partyName?: string;
  params?: Record<string, any>;
}

// 表单数据
export interface EnforcementDecisionForm {
  id?: string;
  year?: string;
  docSeq?: string;
  docNumber?: string;
  partyName?: string;
  licenseName?: string;
  creditCode?: string;
  address?: string;
  legalPerson?: string;
  idNumber?: string;
  phone?: string;
  otherContact?: string;
  violationDesc?: string;
  legalBasis?: string;
  facilityListNo?: string;
  enforceLocation?: string;
  enforceDuration?: number;
  appealDays?: string;
  appealGov?: string;
  appealAgency?: string;
  lawsuitDays?: string;
  lawsuitCourt?: string;
  contactPerson?: string;
  contactPhone?: string;
  attachmentListNo?: string;
  signDate?: string;
  copyCount?: string;
  deliveryCount?: string;
  copyNote?: string;
  status?: string;
  remark?: string;
}

// 列表/详情记录
export interface EnforcementDecisionVO extends EnforcementDecisionForm {
  id: string;
  createTime: string;
}
