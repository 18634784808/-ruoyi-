<template>
  <div class="p-2">
    <div v-show="showSearch" class="mb-[10px]">
      <el-card shadow="hover">
        <el-form ref="queryFormRef" :model="queryParams" :inline="true">
          <el-form-item label="分类" prop="category">
            <el-select v-model="queryParams.category" placeholder="请选择分类" clearable>
              <el-option v-for="item in categoryOptions" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
          </el-form-item>
          <el-form-item label="单位名称" prop="unitName">
            <el-input v-model="queryParams.unitName" placeholder="请输入单位名称" clearable @keyup.enter="handleQuery" />
          </el-form-item>
          <el-form-item label="法定代表人" prop="legalPerson">
            <el-input v-model="queryParams.legalPerson" placeholder="请输入法定代表人" clearable @keyup.enter="handleQuery" />
          </el-form-item>
          <el-form-item label="状态" prop="status">
            <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
              <el-option v-for="item in merchantStatusOptions" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
            <el-button icon="Refresh" @click="resetQuery">重置</el-button>
          </el-form-item>
        </el-form>
      </el-card>
    </div>
    <el-card shadow="hover">
      <template #header>
        <el-row :gutter="10">
          <el-col :span="1.5"><el-button type="primary" plain icon="Plus" @click="handleAdd">新增</el-button></el-col>
          <el-col :span="1.5"><el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate()">修改</el-button></el-col>
          <el-col :span="1.5"><el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete()">删除</el-button></el-col>
          <el-col :span="1.5"><el-button type="warning" plain icon="Download" @click="handleExport">导出</el-button></el-col>
          <right-toolbar v-model:show-search="showSearch" @query-table="getList" />
        </el-row>
      </template>
      <el-table v-loading="loading" border :data="tableData" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="序号" type="index" width="60" align="center" />
        <el-table-column label="分类" align="center" prop="category" width="100" />
        <el-table-column label="单位名称" align="center" prop="unitName" min-width="180" show-overflow-tooltip />
        <el-table-column label="法定代表人" align="center" prop="legalPerson" width="100" />
        <el-table-column label="联系电话" align="center" prop="contactPhone" width="130" />
        <el-table-column label="统一社会信用代码" align="center" prop="unifiedSocialCreditCode" width="200" show-overflow-tooltip />
        <el-table-column label="状态" align="center" width="80">
          <template #default="scope"><el-tag :type="getDictTagType(merchantStatusOptions, scope.row.status)">{{ getDictLabel(merchantStatusOptions, scope.row.status) }}</el-tag></template>
        </el-table-column>
        <el-table-column label="创建时间" align="center" prop="createTime" width="180" />
        <el-table-column label="操作" width="220" align="center" fixed="right">
          <template #default="scope">
            <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)">编辑</el-button>
            <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)">删除</el-button>
            <el-button link type="success" icon="DocumentAdd" @click="handleCreateInspection(scope.row)">创建检查</el-button>
          </template>
        </el-table-column>
      </el-table>
      <pagination v-show="total > 0" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" :total="total" @pagination="getList" />
    </el-card>
    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="900px" append-to-body destroy-on-close top="2vh">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="130px">
        <el-tabs v-model="activeTab">
          <el-tab-pane label="基本信息" name="basic">
            <el-row :gutter="20">
              <el-col :span="12"><el-form-item label="分类" prop="category"><el-select v-model="form.category" placeholder="请选择分类" style="width:100%"><el-option v-for="item in categoryOptions" :key="item.value" :label="item.label" :value="item.value" /></el-select></el-form-item></el-col>
              <el-col :span="12"><el-form-item label="单位名称" prop="unitName"><el-input v-model="form.unitName" placeholder="请输入单位名称" /></el-form-item></el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12"><el-form-item label="证件" prop="certificate"><el-input v-model="form.certificate" placeholder="请输入证件" /></el-form-item></el-col>
              <el-col :span="12"><el-form-item label="食品证有效日期" prop="foodCertExpiryDate"><el-date-picker v-model="form.foodCertExpiryDate" type="date" placeholder="请选择日期" value-format="YYYY-MM-DD" style="width:100%" /></el-form-item></el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12"><el-form-item label="法定代表人" prop="legalPerson"><el-input v-model="form.legalPerson" placeholder="请输入法定代表人" /></el-form-item></el-col>
              <el-col :span="12"><el-form-item label="身份证号" prop="idCardNo"><el-input v-model="form.idCardNo" placeholder="请输入身份证号" maxlength="18" /></el-form-item></el-col>
            </el-row>
            <el-form-item label="统一社会信用代码" prop="unifiedSocialCreditCode"><el-input v-model="form.unifiedSocialCreditCode" placeholder="请输入统一社会信用代码" maxlength="18" style="width:calc(50% - 10px)" /></el-form-item>
            <el-form-item label="经营范围" prop="businessScope"><el-input v-model="form.businessScope" type="textarea" placeholder="请输入经营范围" :rows="2" /></el-form-item>
            <el-row :gutter="20">
              <el-col :span="12"><el-form-item label="联系电话" prop="contactPhone"><el-input v-model="form.contactPhone" placeholder="请输入联系电话" /></el-form-item></el-col>
              <el-col :span="12"><el-form-item label="地址" prop="address"><el-input v-model="form.address" placeholder="请输入地址" /></el-form-item></el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12"><el-form-item label="状态" prop="status"><el-select v-model="form.status" placeholder="请选择状态" style="width:100%"><el-option v-for="item in merchantStatusOptions" :key="item.value" :label="item.label" :value="item.value" /></el-select></el-form-item></el-col>
              <el-col :span="12"><el-form-item label="备注" prop="remark"><el-input v-model="form.remark" placeholder="请输入备注" /></el-form-item></el-col>
            </el-row>
          </el-tab-pane>
          <el-tab-pane label="证照图片" name="photos">
            <el-row :gutter="20">
              <el-col :span="12"><el-form-item label="营业执照照片"><ImageUpload v-model="businessLicensePhotoFiles" :limit="1" :is-show-tip="false" /></el-form-item></el-col>
              <el-col :span="12"><el-form-item label="食品证照片"><ImageUpload v-model="foodCertPhotoFiles" :limit="1" :is-show-tip="false" /></el-form-item></el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12"><el-form-item label="门面照片"><ImageUpload v-model="storefrontPhotoFiles" :limit="1" :is-show-tip="false" /></el-form-item></el-col>
              <el-col :span="12"><el-form-item label="店内照片"><ImageUpload v-model="interiorPhotoFiles" :limit="1" :is-show-tip="false" /></el-form-item></el-col>
            </el-row>
          </el-tab-pane>
        </el-tabs>
      </el-form>
      <template #footer><el-button @click="dialogVisible = false">取消</el-button><el-button type="primary" :loading="submitLoading" @click="submitForm">确定</el-button></template>
    </el-dialog>
  </div>
</template>
<script setup name="BusinessTableMerchant" lang="ts">
import { ref, reactive, onMounted, getCurrentInstance, ComponentInternalInstance } from 'vue'; import { ElMessage, ElMessageBox } from 'element-plus'; import { useRouter } from 'vue-router';
import { listMerchant, getMerchant, addMerchant, updateMerchant, delMerchant, exportMerchant } from '@/api/business/merchant/index';
import { getBizDictByType } from '@/api/business/dict/index';
const { proxy } = getCurrentInstance() as ComponentInternalInstance; const router = useRouter();
const showSearch = ref(true); const loading = ref(false); const submitLoading = ref(false); const single = ref(true); const multiple = ref(true); const total = ref(0); const ids = ref<string[]>([]); const tableData = ref<any[]>([]); const formRef = ref(); const dialogVisible = ref(false); const dialogTitle = ref(''); const activeTab = ref('basic');
const businessLicensePhotoFiles = ref<any[]>([]); const foodCertPhotoFiles = ref<any[]>([]); const storefrontPhotoFiles = ref<any[]>([]); const interiorPhotoFiles = ref<any[]>([]);
const categoryOptions = ref<any[]>([]);
const merchantStatusOptions = ref<any[]>([]);
const getDictLabel = (options: any[], value: string) => options.find(o => o.value === value)?.label ?? value;
const getDictTagType = (options: any[], value: string) => options.find(o => o.value === value)?.tagType ?? '';
onMounted(() => {
  getBizDictByType('category').then(res => { categoryOptions.value = res.data || []; });
  getBizDictByType('merchant_status').then(res => { merchantStatusOptions.value = res.data || []; });
});
const queryParams = reactive({ pageNum: 1, pageSize: 10, category: '', unitName: '', legalPerson: '', status: '' });
const initForm: any = { category: '', unitName: '', certificate: '', foodCertExpiryDate: '', legalPerson: '', idCardNo: '', unifiedSocialCreditCode: '', businessScope: '', contactPhone: '', address: '', businessLicensePhoto: '', foodCertPhoto: '', storefrontPhoto: '', interiorPhoto: '', status: '0', remark: '' };
const form = reactive({ ...initForm });
const rules = { unitName: [{ required: true, message: '请输入单位名称', trigger: 'blur' }] };
const getPhotoUrl = (files: any[]) => files.length > 0 ? (files[0].url || files[0].response?.data?.url || '') : '';
const getList = async () => { loading.value = true; try { const res = await listMerchant(queryParams); tableData.value = res.rows || []; total.value = res.total || 0; } catch { /* 拦截器已处理错误 */ } finally { loading.value = false; } };
const handleQuery = () => { queryParams.pageNum = 1; getList(); };
const resetQuery = () => { Object.assign(queryParams, { pageNum: 1, pageSize: 10, category: '', unitName: '', legalPerson: '', status: '' }); handleQuery(); };
const handleSelectionChange = (sel: any[]) => { ids.value = sel.map((i: any) => i.id); single.value = sel.length !== 1; multiple.value = !sel.length; };
const handleAdd = () => { Object.assign(form, { ...initForm, id: undefined }); businessLicensePhotoFiles.value = []; foodCertPhotoFiles.value = []; storefrontPhotoFiles.value = []; interiorPhotoFiles.value = []; activeTab.value = 'basic'; dialogTitle.value = '新增商户'; dialogVisible.value = true; };
const handleUpdate = async (row?: any) => { const id = row?.id || (ids.value.length === 1 ? ids.value[0] : null); if (!id) return; const res = await getMerchant(id); Object.assign(form, res.data); businessLicensePhotoFiles.value = res.data.businessLicensePhoto ? [{ url: res.data.businessLicensePhoto, name: 'license' }] : []; foodCertPhotoFiles.value = res.data.foodCertPhoto ? [{ url: res.data.foodCertPhoto, name: 'foodCert' }] : []; storefrontPhotoFiles.value = res.data.storefrontPhoto ? [{ url: res.data.storefrontPhoto, name: 'storefront' }] : []; interiorPhotoFiles.value = res.data.interiorPhoto ? [{ url: res.data.interiorPhoto, name: 'interior' }] : []; activeTab.value = 'basic'; dialogTitle.value = '修改商户'; dialogVisible.value = true; };
const handleDelete = (row?: any) => { const deleteIds = row?.id ? row.id : ids.value.join(','); const msg = row?.id ? '确认删除？' : '确认删除所选数据？'; ElMessageBox.confirm(msg, '提示', { type: 'warning' }).then(async () => { await delMerchant(deleteIds); ElMessage.success('删除成功'); getList(); }); };
const handleExport = async () => { await exportMerchant(queryParams); ElMessage.success('导出成功'); };
const handleCreateInspection = (row: any) => { router.push({ path: '/table/inspection', query: { merchantId: row.id, merchantName: row.unitName, category: row.category } }); };
const submitForm = async () => { try { await formRef.value.validate(); } catch { return; } submitLoading.value = true; try { form.businessLicensePhoto = getPhotoUrl(businessLicensePhotoFiles.value); form.foodCertPhoto = getPhotoUrl(foodCertPhotoFiles.value); form.storefrontPhoto = getPhotoUrl(storefrontPhotoFiles.value); form.interiorPhoto = getPhotoUrl(interiorPhotoFiles.value); await (form.id ? updateMerchant(form) : addMerchant(form)); ElMessage.success(form.id ? '修改成功' : '新增成功'); dialogVisible.value = false; getList(); } catch (e) { console.error(e); } finally { submitLoading.value = false; } };
getList();
</script>
