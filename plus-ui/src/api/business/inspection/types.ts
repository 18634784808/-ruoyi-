// 查询参数
export interface InspectionQuery {
  pageNum: number;
  pageSize: number;
  category?: string;
  inspectedUnit?: string;
  inspectionType?: string;
  status?: string;
  params?: Record<string, any>;
}

// 检查记录
export interface InspectionVO {
  id: string;
  category: string;
  inspectedUnit: string;
  legalPerson: string;
  contactPhone: string;
  unifiedSocialCreditCode: string;
  address: string;
  inspectionDate: string;
  inspectionType: string;
  inspectionSituation: string;
  handlingOpinion: string;
  inspectedUnitSignature: string;
  inspectorCount: number;
  inspector1: string;
  inspector2: string;
  inspector3: string;
  inspector4: string;
  rectificationReport: string;
  sceneNotes: string;
  ledgerUnit: string;
  merchantId: number;
  status: string;
  remark: string;
  createTime: string;
}

// 表单数据
export interface InspectionForm {
  id?: string;
  category: string;
  inspectedUnit: string;
  legalPerson?: string;
  contactPhone?: string;
  unifiedSocialCreditCode?: string;
  address?: string;
  inspectionDate?: string;
  inspectionType?: string;
  inspectionSituation?: string;
  handlingOpinion?: string;
  inspectedUnitSignature?: string;
  inspectorCount?: number;
  inspector1?: string;
  inspector2?: string;
  inspector3?: string;
  inspector4?: string;
  rectificationReport?: string;
  sceneNotes?: string;
  ledgerUnit?: string;
  status: string;
  remark?: string;
}
