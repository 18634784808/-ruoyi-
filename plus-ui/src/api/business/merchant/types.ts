// 查询参数
export interface MerchantQuery {
  pageNum: number;
  pageSize: number;
  category?: string;
  unitName?: string;
  legalPerson?: string;
  status?: string;
  params?: Record<string, any>;
}

// 商户记录
export interface MerchantVO {
  id: string;
  category: string;
  unitName: string;
  certificate: string;
  foodCertExpiryDate: string;
  legalPerson: string;
  idCardNo: string;
  unifiedSocialCreditCode: string;
  businessScope: string;
  contactPhone: string;
  businessLicensePhoto: string;
  address: string;
  foodCertPhoto: string;
  storefrontPhoto: string;
  interiorPhoto: string;
  status: string;
  remark: string;
  createTime: string;
}

// 表单数据
export interface MerchantForm {
  id?: string;
  category?: string;
  unitName: string;
  certificate?: string;
  foodCertExpiryDate?: string;
  legalPerson?: string;
  idCardNo?: string;
  unifiedSocialCreditCode?: string;
  businessScope?: string;
  contactPhone?: string;
  businessLicensePhoto?: string;
  address?: string;
  foodCertPhoto?: string;
  storefrontPhoto?: string;
  interiorPhoto?: string;
  status: string;
  remark?: string;
}
