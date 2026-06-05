// 查询参数
export interface InspectorQuery {
  pageNum: number;
  pageSize: number;
  name?: string;
  department?: string;
  status?: string;
  params?: Record<string, any>;
}

// 检察人员记录
export interface InspectorVO {
  id: string;
  name: string;
  department: string;
  certNo: string;
  status: string;
  remark: string;
  createTime: string;
}

// 表单数据
export interface InspectorForm {
  id?: string;
  name: string;
  department?: string;
  certNo?: string;
  status: string;
  remark?: string;
}
