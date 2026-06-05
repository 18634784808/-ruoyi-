export interface EquipmentCheckRecordQuery {
  pageNum: number;
  pageSize: number;
  unitName?: string;
  params?: Record<string, any>;
}

export interface EquipmentCheckRecordForm {
  id?: string;
  inspector?: string;
  recorder?: string;
  recordDate?: string;
  inspectionType?: string;
  inspectionStartTime?: string;
  inspectionEndTime?: string;
  unitName?: string;
  creditCode?: string;
  legalRep?: string;
  safetyManager?: string;
  unitAddress?: string;
  contactPerson?: string;
  contactPosition?: string;
  contactPhone?: string;
  unitCategory?: string;
  equipmentName?: string;
  registrationCode?: string;
  findings?: string;
  disposition?: string;
  unitOpinion?: string;
  unitSign?: string;
  signDate?: string;
  remark?: string;
}

export interface EquipmentCheckRecordVO extends EquipmentCheckRecordForm {
  id: string;
  createTime: string;
}
