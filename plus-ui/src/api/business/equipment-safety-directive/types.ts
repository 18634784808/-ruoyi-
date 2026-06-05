export interface EquipmentSafetyDirectiveQuery {
  pageNum: number;
  pageSize: number;
  inspectedUnit?: string;
  params?: Record<string, any>;
}

export interface EquipmentSafetyDirectiveForm {
  id?: string;
  issuingAgency?: string;
  docYear?: string;
  docSeq?: string;
  docNumber?: string;
  inspectedUnit?: string;
  problemsFound?: string;
  violationDesc?: string;
  violationArticle?: string;
  basisRule?: string;
  deadlineDate?: string;
  correctiveMeasures?: string;
  appealGov?: string;
  appealCourt?: string;
  inspectorSign?: string;
  unitSign?: string;
  sealInfo?: string;
  signDate?: string;
  remark?: string;
}

export interface EquipmentSafetyDirectiveVO extends EquipmentSafetyDirectiveForm {
  id: string;
  createTime: string;
}
