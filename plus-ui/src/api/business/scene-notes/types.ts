export interface SceneNotesQuery {
  pageNum: number;
  pageSize: number;
  place?: string;
  party?: string;
  inspector?: string;
  inspectionId?: string;
  status?: string;
  params?: Record<string, any>;
}

export interface SceneNotesVO {
  id: string;
  startTime: string;
  endTime: string;
  place: string;
  inspector: string;
  inspectorCopy: string;
  party: string;
  qualificationName: string;
  partyPresenceNotice: string;
  inspector1: string;
  party1: string;
  inspector2: string;
  party2: string;
  sceneSituation: string;
  partyStatement: string;
  status: string;
  inspectionId: string;
  remark: string;
  createTime: string;
}

export interface SceneNotesForm {
  id?: string;
  startTime?: string;
  endTime?: string;
  place: string;
  inspector?: string;
  inspectorCopy?: string;
  party: string;
  qualificationName?: string;
  partyPresenceNotice?: string;
  inspector1?: string;
  party1?: string;
  inspector2?: string;
  party2?: string;
  sceneSituation?: string;
  partyStatement?: string;
  status: string;
  inspectionId?: string;
  remark?: string;
}
