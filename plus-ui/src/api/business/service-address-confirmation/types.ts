// 查询参数
export interface ServiceAddressConfirmationQuery {
  pageNum: number;
  pageSize: number;
  recipientName?: string;
  params?: Record<string, any>;
}

// 表单数据
export interface ServiceAddressConfirmationForm {
  id?: string;
  recipientName?: string;
  acceptElectronic?: string;
  faxNumber?: string;
  emailAddress?: string;
  instantAccount?: string;
  serviceAddress?: string;
  recipientPerson?: string;
  recipientPhone?: string;
  postalCode?: string;
  signName?: string;
  signDate?: string;
  remark?: string;
}

// 列表/详情记录
export interface ServiceAddressConfirmationVO extends ServiceAddressConfirmationForm {
  id: string;
  createTime: string;
}
