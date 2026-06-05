// 查询参数
export interface SignatureQuery {
  pageNum: number;
  pageSize: number;
  name?: string;
  department?: string;
  status?: string;
  params?: Record<string, any>;
}

// 签名档案记录
export interface SignatureVO {
  id: string;
  name: string;
  department: string;
  signature: string;
  status: string;
  remark: string;
  createTime: string;
}

// 表单数据
export interface SignatureForm {
  id?: string;
  name: string;
  department?: string;
  signature?: string;
  status: string;
  remark?: string;
}
