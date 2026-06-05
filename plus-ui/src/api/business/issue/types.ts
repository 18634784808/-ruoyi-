// 查询参数
export interface IssueQuery {
  pageNum: number;
  pageSize: number;
  issueOption?: string;
  autoIncrementCode?: string;
  inspectionId?: string;
  severity?: string;
  status?: string;
  params?: Record<string, any>;
}

// 问题清单记录
export interface IssueVO {
  id: string;
  issueOption: string;
  autoIncrementCode: string;
  severity: string;
  status: string;
  inspectionId: string;
  remark: string;
  createTime: string;
}

// 表单数据
export interface IssueForm {
  id?: string;
  issueOption: string;
  autoIncrementCode?: string;
  severity: string;
  status: string;
  inspectionId?: string;
  remark?: string;
}
