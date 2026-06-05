<template>
  <div class="p-2">
    <div v-show="showSearch" class="mb-[10px]">
      <el-card shadow="hover">
        <el-form ref="queryFormRef" :model="queryParams" :inline="true">
          <el-form-item label="类别" prop="category">
            <el-select v-model="queryParams.category" placeholder="请选择类别" clearable>
              <el-option v-for="item in categoryOptions" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
          </el-form-item>
          <el-form-item label="被检查单位" prop="inspectedUnit">
            <el-input v-model="queryParams.inspectedUnit" placeholder="请输入被检查单位" clearable @keyup.enter="handleQuery" />
          </el-form-item>
          <el-form-item label="检查形式" prop="inspectionType">
            <el-input v-model="queryParams.inspectionType" placeholder="请输入检查形式" clearable @keyup.enter="handleQuery" />
          </el-form-item>
          <el-form-item label="状态" prop="status">
            <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
              <el-option v-for="item in inspectionStatusOptions" :key="item.value" :label="item.label" :value="item.value" />
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
        <el-table-column label="类别" align="center" prop="category" width="100" />
        <el-table-column label="被检查单位" align="center" min-width="180" show-overflow-tooltip>
          <template #default="scope">
            <el-button v-if="scope.row.merchantId" link type="primary" @click="showMerchantDetail(scope.row.inspectedUnit)">{{ scope.row.inspectedUnit }}</el-button>
            <span v-else>{{ scope.row.inspectedUnit }}</span>
          </template>
        </el-table-column>
        <el-table-column label="检查日期" align="center" prop="inspectionDate" width="120" />
        <el-table-column label="检查形式" align="center" prop="inspectionType" width="110" />
        <el-table-column label="本次检查情况" align="center" prop="inspectionSituation" min-width="180" show-overflow-tooltip />
        <el-table-column label="本次检查处理意见" align="center" prop="handlingOpinion" width="160" show-overflow-tooltip />
        <el-table-column label="被检查单位签字" align="center" prop="inspectedUnitSignature" width="130" />
        <el-table-column label="检查人员数量" align="center" prop="inspectorCount" width="110" />
        <el-table-column label="检察人员1" align="center" prop="inspector1" width="100" />
        <el-table-column label="检察人员2" align="center" prop="inspector2" width="100" />
        <el-table-column label="检察人员3" align="center" prop="inspector3" width="100" />
        <el-table-column label="检察人员4" align="center" prop="inspector4" width="100" />
        <el-table-column label="整改报告" align="center" prop="rectificationReport" min-width="150" show-overflow-tooltip />
        <el-table-column label="关联记录" align="center" width="220" fixed="right">
          <template #default="scope">
            <el-button link type="success" size="small" icon="Document" @click="goToSceneNotes(scope.row.id)">现场笔录</el-button>
            <el-button link type="warning" size="small" icon="ChatDotRound" @click="goToInquiryNotes(scope.row.id)">询问笔录</el-button>
            <el-button link type="danger" size="small" icon="Warning" @click="goToIssue(scope.row.id)">问题清单</el-button>
          </template>
        </el-table-column>
        <el-table-column label="所属台账单位" align="center" prop="ledgerUnit" min-width="150" show-overflow-tooltip />
        <el-table-column label="状态" align="center" width="80">
          <template #default="scope"><el-tag :type="getDictTagType(inspectionStatusOptions, scope.row.status)">{{ getDictLabel(inspectionStatusOptions, scope.row.status) }}</el-tag></template>
        </el-table-column>
        <el-table-column label="创建时间" align="center" prop="createTime" width="180" />
        <el-table-column label="操作" width="150" align="center" fixed="right">
          <template #default="scope">
            <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)">编辑</el-button>
            <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <pagination v-show="total > 0" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" :total="total" @pagination="getList" />
    </el-card>
    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="800px" append-to-body destroy-on-close>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="类别" prop="category">
              <el-select v-model="form.category" placeholder="请选择类别" style="width:100%">
                <el-option v-for="item in categoryOptions" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="被检查单位" prop="inspectedUnit"><el-input v-model="form.inspectedUnit" placeholder="请输入被检查单位" /></el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="检查日期" prop="inspectionDate"><el-date-picker v-model="form.inspectionDate" type="date" placeholder="请选择日期" value-format="YYYY-MM-DD" style="width:100%" /></el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="检查形式" prop="inspectionType"><el-input v-model="form.inspectionType" placeholder="请输入检查形式" /></el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="本次检查情况" prop="inspectionSituation"><el-input v-model="form.inspectionSituation" type="textarea" placeholder="请输入本次检查情况" :rows="3" /></el-form-item>
        <el-form-item label="本次检查处理意见" prop="handlingOpinion">
          <el-select v-model="form.handlingOpinion" placeholder="请选择处理意见" style="width:100%">
            <el-option v-for="item in handlingOpinionOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="被检查单位签字" prop="inspectedUnitSignature"><el-input v-model="form.inspectedUnitSignature" placeholder="请输入签字" /></el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="所属台账单位" prop="ledgerUnit"><el-input v-model="form.ledgerUnit" placeholder="请输入所属台账单位" /></el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="检察人员数量" prop="inspectorCount"><el-input-number v-model="form.inspectorCount" :min="0" :max="4" style="width:200px" /></el-form-item>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="检察人员1" prop="inspector1"><el-input v-model="form.inspector1" placeholder="请输入" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="检察人员2" prop="inspector2"><el-input v-model="form.inspector2" placeholder="请输入" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="检察人员3" prop="inspector3"><el-input v-model="form.inspector3" placeholder="请输入" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="检察人员4" prop="inspector4"><el-input v-model="form.inspector4" placeholder="请输入" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="整改报告" prop="rectificationReport"><el-input v-model="form.rectificationReport" placeholder="请输入整改报告" /></el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="现场笔录" prop="sceneNotes"><el-input v-model="form.sceneNotes" placeholder="请输入现场笔录" /></el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="状态" prop="status"><el-select v-model="form.status" placeholder="请选择状态" style="width:100%"><el-option v-for="item in inspectionStatusOptions" :key="item.value" :label="item.label" :value="item.value" /></el-select></el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="备注" prop="remark"><el-input v-model="form.remark" placeholder="请输入备注" /></el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer><el-button @click="dialogVisible = false">取消</el-button><el-button type="primary" :loading="submitLoading" @click="submitForm">确定</el-button></template>
    </el-dialog>
    <el-dialog title="商户详情" v-model="merchantDialogVisible" width="700px" append-to-body destroy-on-close>
      <el-descriptions v-if="merchantDetail" :column="2" border>
        <el-descriptions-item label="单位名称">{{ merchantDetail.unitName }}</el-descriptions-item>
        <el-descriptions-item label="分类">{{ merchantDetail.category }}</el-descriptions-item>
        <el-descriptions-item label="法定代表人">{{ merchantDetail.legalPerson }}</el-descriptions-item>
        <el-descriptions-item label="联系电话">{{ merchantDetail.contactPhone }}</el-descriptions-item>
        <el-descriptions-item label="统一社会信用代码">{{ merchantDetail.unifiedSocialCreditCode }}</el-descriptions-item>
        <el-descriptions-item label="身份证号">{{ merchantDetail.idCardNo }}</el-descriptions-item>
        <el-descriptions-item label="证件">{{ merchantDetail.certificate }}</el-descriptions-item>
        <el-descriptions-item label="食品证有效日期">{{ merchantDetail.foodCertExpiryDate }}</el-descriptions-item>
        <el-descriptions-item label="经营范围" :span="2">{{ merchantDetail.businessScope }}</el-descriptions-item>
        <el-descriptions-item label="地址" :span="2">{{ merchantDetail.address }}</el-descriptions-item>
        <el-descriptions-item label="营业执照"><el-image v-if="merchantDetail.businessLicensePhoto" :src="merchantDetail.businessLicensePhoto" style="width:100px;height:80px" :preview-src-list="[merchantDetail.businessLicensePhoto]" fit="contain" /></el-descriptions-item>
        <el-descriptions-item label="食品证"><el-image v-if="merchantDetail.foodCertPhoto" :src="merchantDetail.foodCertPhoto" style="width:100px;height:80px" :preview-src-list="[merchantDetail.foodCertPhoto]" fit="contain" /></el-descriptions-item>
        <el-descriptions-item label="门面照片"><el-image v-if="merchantDetail.storefrontPhoto" :src="merchantDetail.storefrontPhoto" style="width:100px;height:80px" :preview-src-list="[merchantDetail.storefrontPhoto]" fit="contain" /></el-descriptions-item>
        <el-descriptions-item label="店内照片"><el-image v-if="merchantDetail.interiorPhoto" :src="merchantDetail.interiorPhoto" style="width:100px;height:80px" :preview-src-list="[merchantDetail.interiorPhoto]" fit="contain" /></el-descriptions-item>
      </el-descriptions>
      <template #footer><el-button type="primary" @click="merchantDialogVisible = false">关闭</el-button></template>
    </el-dialog>
  </div>
</template>
<script setup name="BusinessTableInspection" lang="ts">
import { ref, reactive, onMounted, getCurrentInstance, ComponentInternalInstance, watch, nextTick } from 'vue'; import { ElMessage, ElMessageBox } from 'element-plus'; import { useRoute, useRouter } from 'vue-router';
import { listInspection, getInspection, addInspection, updateInspection, delInspection, exportInspection } from '@/api/business/inspection/index';
import { findMerchantByName } from '@/api/business/merchant/index';
import { getBizDictByType } from '@/api/business/dict/index';
const { proxy } = getCurrentInstance() as ComponentInternalInstance; const route = useRoute(); const router = useRouter();
const showSearch = ref(true); const loading = ref(false); const submitLoading = ref(false); const single = ref(true); const multiple = ref(true); const total = ref(0); const ids = ref<string[]>([]); const tableData = ref<any[]>([]); const formRef = ref(); const dialogVisible = ref(false); const dialogTitle = ref('');
const merchantDialogVisible = ref(false); const merchantDetail = ref<any>(null);
const categoryOptions = ref<any[]>([]);
const handlingOpinionOptions = ref<any[]>([]);
const inspectionStatusOptions = ref<any[]>([]);
const getDictLabel = (options: any[], value: string) => options.find(o => o.value === value)?.label ?? value;
const getDictTagType = (options: any[], value: string) => options.find(o => o.value === value)?.tagType ?? '';
onMounted(() => {
  getBizDictByType('category').then(res => { categoryOptions.value = res.data || []; });
  getBizDictByType('handling_opinion').then(res => { handlingOpinionOptions.value = res.data || []; });
  getBizDictByType('inspection_status').then(res => { inspectionStatusOptions.value = res.data || []; });
});
const queryParams = reactive({ pageNum: 1, pageSize: 10, category: '', inspectedUnit: '', inspectionType: '', status: '' });
const initForm: any = { category: '', inspectedUnit: '', inspectionDate: '', inspectionType: '', inspectionSituation: '', handlingOpinion: '', inspectedUnitSignature: '', inspectorCount: 0, inspector1: '', inspector2: '', inspector3: '', inspector4: '', rectificationReport: '', sceneNotes: '', ledgerUnit: '', merchantId: undefined, status: '0', remark: '' };
const form = reactive({ ...initForm });
const rules = {
  category: [{ required: true, message: '请选择类别', trigger: 'change' }],
  inspectedUnit: [{ required: true, message: '请输入被检查单位', trigger: 'blur' }],
  inspectionDate: [{ required: true, message: '请选择检查日期', trigger: 'change' }]
};
const getList = async () => { loading.value = true; try { const res = await listInspection(queryParams); tableData.value = res.rows || []; total.value = res.total || 0; } catch { /* 拦截器已处理错误 */ } finally { loading.value = false; } };
const handleQuery = () => { queryParams.pageNum = 1; getList(); };
const resetQuery = () => { Object.assign(queryParams, { pageNum: 1, pageSize: 10, category: '', inspectedUnit: '', inspectionType: '', status: '' }); handleQuery(); };
const handleSelectionChange = (sel: any[]) => { ids.value = sel.map((i: any) => i.id); single.value = sel.length !== 1; multiple.value = !sel.length; };
const handleAdd = () => { Object.assign(form, { ...initForm, id: undefined }); dialogTitle.value = '新增检查记录'; dialogVisible.value = true; };
const handleUpdate = async (row?: any) => { const id = row?.id || (ids.value.length === 1 ? ids.value[0] : null); if (!id) return; const res = await getInspection(id); Object.assign(form, res.data); dialogTitle.value = '修改检查记录'; dialogVisible.value = true; };
const handleDelete = (row?: any) => { const deleteIds = row?.id ? row.id : ids.value.join(','); const msg = row?.id ? '确认删除？' : '确认删除所选数据？'; ElMessageBox.confirm(msg, '提示', { type: 'warning' }).then(async () => { await delInspection(deleteIds); ElMessage.success('删除成功'); getList(); }); };
const handleExport = async () => { await exportInspection(queryParams); ElMessage.success('导出成功'); };
const showMerchantDetail = async (name: string) => { try { const res = await findMerchantByName(name); if (res.data) { merchantDetail.value = res.data; merchantDialogVisible.value = true; } } catch { ElMessage.warning('未找到商户信息'); } };
const goToSceneNotes = (inspectionId: string) => { router.push({ path: '/table/scene-notes', query: { inspectionId } }); };
const goToInquiryNotes = (inspectionId: string) => { router.push({ path: '/table/inquiry-notes', query: { inspectionId } }); };
const goToIssue = (inspectionId: string) => { router.push({ path: '/table/issue', query: { inspectionId } }); };
const submitForm = async () => { try { await formRef.value.validate(); } catch { return; } submitLoading.value = true; try { await (form.id ? updateInspection(form) : addInspection(form)); ElMessage.success(form.id ? '修改成功' : '新增成功'); dialogVisible.value = false; getList(); } catch (e) { console.error(e); } finally { submitLoading.value = false; } };
watch(() => route.query, async (q) => { if (q.merchantId && q.merchantName) { Object.assign(form, { ...initForm, id: undefined, inspectedUnit: q.merchantName, merchantId: Number(q.merchantId), category: q.category || '' }); dialogTitle.value = '新增检查记录'; await nextTick(); dialogVisible.value = true; router.replace({ path: '/table/inspection' }); } }, { immediate: true });
getList();
</script>
