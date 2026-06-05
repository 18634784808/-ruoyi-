// 查询参数
export interface LedgerQuery {
  pageNum: number;
  pageSize: number;
  inspectedUnit?: string;
  legalPerson?: string;
  rectificationProgress?: string;
  inspectorCategory?: string;
  params?: Record<string, any>;
}

// 台账记录
export interface LedgerVO {
  id: string;
  date: string;
  inspectionDate: string;
  inspectedUnit: string;
  legalPerson: string;
  contactPhone: string;
  address: string;
  inspectionContent: string;
  rectificationMeasure: string;
  rectificationProgress: string;
  inspectorCategory: string;
  remark: string;
  unifiedSocialCreditCode: string;
  createTime: string;
}

// 表单数据
export interface LedgerForm {
  id?: string;
  date: string;
  inspectionDate: string;
  inspectedUnit: string;
  legalPerson: string;
  contactPhone: string;
  address: string;
  inspectionContent: string;
  rectificationMeasure: string;
  rectificationProgress: string;
  inspectorCategory: string;
  remark: string;
  unifiedSocialCreditCode: string;
}
