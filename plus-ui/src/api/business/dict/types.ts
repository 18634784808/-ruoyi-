// 查询参数
export interface BizDictQuery {
  pageNum: number;
  pageSize: number;
  dictType?: string;
  label?: string;
  status?: string;
  params?: Record<string, any>;
}

// 业务字典记录
export interface BizDictVO {
  id: string;
  dictType: string;
  label: string;
  value: string;
  sort: number;
  status: string;
  tagType: string;
  remark: string;
  createTime: string;
}

// 表单数据
export interface BizDictForm {
  id?: string;
  dictType: string;
  label: string;
  value: string;
  sort?: number;
  status: string;
  tagType?: string;
  remark?: string;
}
