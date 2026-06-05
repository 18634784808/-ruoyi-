<template>
  <div class="p-2">
    <div class="btn-bar noprint">
      <el-button type="primary" icon="Search" @click="handleSelectDecision">📋 选择决定书</el-button>
      <el-button type="warning" icon="Search" @click="handleSelectChecklist">📋 选择清单</el-button>
      <el-button type="success" icon="Printer" @click="handlePrint" :disabled="!formData.id">🖨️ 打印</el-button>
      <el-button type="danger" icon="Delete" @click="handleDeleteDecision" :disabled="!formData.id">🗑️ 删除决定书</el-button>
      <el-button icon="RefreshRight" @click="handleClear">🔄 清空</el-button>
    </div>

    <div class="print-area" id="printArea">
      <!-- ==================== 第1页 ==================== -->
      <div class="page page-1">
        <!-- 红头 -->
        <div class="header-agency">汾阳市市场监督管理局</div>
        <div class="doc-title">实施行政强制措施决定书</div>

        <!-- 文书编号 -->
        <div class="doc-number">
          一市监&ensp;-〔<span class="u">{{ formData.year || '&emsp;' }}</span>〕<span class="u">{{ formData.docSeq || '&emsp;' }}</span>&ensp;第<span class="u">{{ formData.docNumber || '&emsp;' }}</span>号
        </div>

        <!-- 当事人信息 -->
        <table class="info-table">
          <tr><td class="il">当事人：</td><td class="iv">{{ formData.partyName }}</td></tr>
          <tr><td class="il">主体资格证照名称：</td><td class="iv">{{ formData.licenseName }}</td></tr>
          <tr><td class="il">统一社会信用代码（注册号）：</td><td class="iv">{{ formData.creditCode }}</td></tr>
          <tr><td class="il">住所（住址）：</td><td class="iv">{{ formData.address }}</td></tr>
          <tr><td class="il">法定代表人（负责人、经营者）：</td><td class="iv">{{ formData.legalPerson }}</td></tr>
          <tr><td class="il">身份证（其他有效证件）号码：</td><td class="iv">{{ formData.idNumber }}</td></tr>
          <tr>
            <td class="il">联系电话：</td><td class="iv" style="width:35%;">{{ formData.phone }}</td>
            <td class="il">其他联系方式：</td><td class="iv">{{ formData.otherContact }}</td>
          </tr>
        </table>

        <!-- 违法事实 -->
        <p class="body-text">经查，你（单位）涉嫌<span class="u inline">{{ formData.violationDesc || '&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;' }}</span></p>
        <p class="body-text">本局依据<span class="u inline">{{ formData.legalBasis || '&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;' }}</span>的规定，决定对有关场所/设施</p>
        <p class="body-text">/财物［详见《场所/设施/财物清单》（文书编号：<span class="u">{{ formData.facilityListNo || checklistDocNo || '&emsp;&emsp;' }}</span>）］实施<span class="u"></span></p>

        <p class="body-right">行政强制措施。</p>

        <!-- 强制措施具体内容 -->
        <p class="body-text">1.实施行政强制措施的场所/设施/财物地点：<span class="u inline">{{ formData.enforceLocation || '&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;' }}</span></p>
        <p class="body-text">2.实施行政强制措施的期限为<span class="u">{{ formData.enforceDuration || '&emsp;' }}</span>日。情况复杂，需要延长强制措施期限内，本局将书面告知。</p>
        <p class="body-text">对物品需要进行检测、检验、检疫或者技术鉴定的，查封、扣押的期间不包括检测、检验、检疫或者技术鉴定的期间，检测、检验、检疫或者技术鉴定的期间本局将书面告知。</p>
      </div>

      <!-- ==================== 第2页 ==================== -->
      <div class="page page-2">
        <p class="body-text">3.物品保存条件：</p>
        <p class="body-text">查封/扣押的场所/设施/财物应当妥善保管，不得使用或者损毁。</p>

        <p class="body-text">
          如对本决定不服，可以在收到本决定之日起<span class="u">{{ formData.appealDays || '&emsp;' }}</span>内向<span class="u">{{ formData.appealGov || '人民政府' }}</span>或者<span class="u">{{ formData.appealAgency || '&emsp;市场监督管理局' }}</span>申请行政复议；也可以在<span class="u">{{ formData.lawsuitDays || '&emsp;' }}</span>内依法向<span class="u">{{ formData.lawsuitCourt || '&emsp;' }}</span>法院提起行政诉讼。
        </p>

        <div class="contact-line">
          <span>联系人：<span class="u">{{ formData.contactPerson || '&emsp;&emsp;' }}</span></span>
          <span style="margin-left: 30px;">联系电话：<span class="u">{{ formData.contactPhone || '&emsp;&emsp;' }}</span></span>
        </div>

        <div class="attachment-line">
          附件：《场所/设施/财物清单》（文书编号：<span class="u">{{ formData.attachmentListNo || checklistDocNo || '&emsp;&emsp;' }}</span>）
        </div>

        <div class="seal-area">
          <div class="seal-agency">汾阳市市场监督管理局</div>
          <div class="seal-stamp">（印&emsp;章）</div>
          <div class="seal-date">{{ formData.signDate || '&emsp;&emsp;年&emsp;&emsp;月&emsp;&emsp;日' }}</div>
        </div>

        <div class="footer-note">
          本文书一式<span class="u">{{ formData.copyCount || '&emsp;' }}</span>份，<span class="u">{{ formData.deliveryCount || '&emsp;' }}</span>份送达，一份归档，<span class="u">{{ formData.copyNote || '&emsp;' }}</span>
        </div>
      </div>

      <!-- ==================== 第3页起：附件 场所/设施/财物清单 ==================== -->
      <div class="page page-checklist">
        <div class="cl-header">
          <div class="cl-dept">汾阳市市场监督管理局</div>
          <div class="cl-title">场所&ensp;/&ensp;设施&ensp;/&ensp;财物清单</div>
          <div class="cl-doc-no">文书编号：{{ checklistData.documentNo || '&emsp;' }}</div>
        </div>

        <table class="cl-table">
          <thead>
            <tr>
              <th class="cl-col-seq">序号</th>
              <th class="cl-col-name">标称名称/场所</th>
              <th class="cl-col-spec">规格（型号）/场所地址</th>
              <th class="cl-col-unit">单位</th>
              <th class="cl-col-qty">数量</th>
              <th class="cl-col-rmk">备注</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(item, idx) in checklistDisplayItems" :key="idx">
              <td class="cl-col-seq">{{ idx + 1 }}</td>
              <td>{{ item.nameOrVenue }}</td>
              <td>{{ item.specOrAddress }}</td>
              <td class="cl-col-unit">{{ item.unit }}</td>
              <td class="cl-col-qty">{{ item.quantity }}</td>
              <td>{{ item.remark }}</td>
            </tr>
          </tbody>
        </table>

        <div class="cl-confirm">{{ checklistData.confirmationText || '上述物品品种、数量经核对无误。' }}</div>

        <div class="cl-sign-row">
          <span>当事人（签名或者盖章）：<span class="u" style="min-width: 100px;">{{ checklistData.partySignature || '&emsp;&emsp;&emsp;' }}</span></span>
          <span>年&emsp;&emsp;月&emsp;&emsp;日</span>
        </div>
        <div class="cl-sign-row">
          <span>执法人员（签名或者盖章）：<span class="u" style="min-width: 100px;">{{ checklistData.officerSignature || '&emsp;&emsp;&emsp;' }}</span></span>
          <span>年&emsp;&emsp;月&emsp;&emsp;日</span>
        </div>
        <div class="cl-sign-row">
          <span>见证人（签名或者盖章）：<span class="u" style="min-width: 100px;">{{ checklistData.witnessSignature || '&emsp;&emsp;&emsp;' }}</span></span>
        </div>

        <div class="cl-footer">
          <span>本文书一式&ensp;{{ checklistData.copiesCount || '&emsp;' }}&ensp;份，&ensp;{{ (checklistData.copiesCount || 2) - 1 }}&ensp;份送达，一份归档，</span>
          <span>第&ensp;{{ checklistData.pageNo || '&emsp;' }}&ensp;页&ensp;共&ensp;{{ checklistData.totalPages || '&emsp;' }}&ensp;页</span>
        </div>
      </div>
    </div>

    <!-- 弹窗 -->
    <el-dialog v-model="selectDecisionVisible" title="选择实施行政强制措施决定书" width="1000px" append-to-body destroy-on-close>
      <el-table :data="decisionTableData" border highlight-current-row @row-dblclick="selectDecision" v-loading="decisionLoading">
        <el-table-column label="序号" type="index" width="60" align="center" />
        <el-table-column label="当事人" prop="partyName" min-width="140" show-overflow-tooltip />
        <el-table-column label="统一社会信用代码" prop="creditCode" width="180" show-overflow-tooltip />
        <el-table-column label="强制措施地点" prop="enforceLocation" min-width="150" show-overflow-tooltip />
        <el-table-column label="签署日期" prop="signDate" width="120" align="center" />
        <el-table-column label="创建时间" prop="createTime" width="170" align="center" />
        <el-table-column label="操作" width="80" align="center">
          <template #default="scope"><el-button link type="primary" @click="selectDecision(scope.row)">选择</el-button></template>
        </el-table-column>
      </el-table>
      <pagination v-show="decisionTotal > 0" v-model:page="decisionParams.pageNum" v-model:limit="decisionParams.pageSize" :total="decisionTotal" @pagination="loadDecisionList" />
    </el-dialog>

    <el-dialog v-model="selectChecklistVisible" title="选择场所设施财物清单" width="900px" append-to-body destroy-on-close>
      <el-table :data="checklistTableData" border highlight-current-row @row-dblclick="selectChecklist" v-loading="checklistLoading">
        <el-table-column label="序号" type="index" width="60" align="center" />
        <el-table-column label="文书编号" prop="documentNo" min-width="180" show-overflow-tooltip />
        <el-table-column label="当事人" prop="partySignature" width="100" />
        <el-table-column label="执法人员" prop="officerSignature" width="100" />
        <el-table-column label="物品数" width="70" align="center">
          <template #default="scope">{{ scope.row.items?.length || 0 }}</template>
        </el-table-column>
        <el-table-column label="创建时间" prop="createTime" width="170" />
        <el-table-column label="操作" width="80" align="center">
          <template #default="scope"><el-button link type="primary" @click="selectChecklist(scope.row)">选择</el-button></template>
        </el-table-column>
      </el-table>
      <pagination v-show="checklistTotal > 0" v-model:page="checklistParams.pageNum" v-model:limit="checklistParams.pageSize" :total="checklistTotal" @pagination="loadChecklistList" />
    </el-dialog>
  </div>
</template>

<script setup name="BusinessOutputEnforcementDecisionForm" lang="ts">
import { ref, reactive, computed } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { listEnforcementDecision, getEnforcementDecision, delEnforcementDecision } from '@/api/business/enforcement-decision/index';
import { listFacilityChecklist, getFacilityChecklist } from '@/api/business/facility-checklist/index';

const decisionLoading = ref(false); const selectDecisionVisible = ref(false);
const decisionTotal = ref(0); const decisionTableData = ref<any[]>([]);
const decisionParams = reactive({ pageNum: 1, pageSize: 10 });

const emptyForm = () => ({
  id: undefined, year: '', docSeq: '', docNumber: '',
  partyName: '', licenseName: '', creditCode: '', address: '', legalPerson: '',
  idNumber: '', phone: '', otherContact: '',
  violationDesc: '', legalBasis: '',
  facilityListNo: '', enforceLocation: '', enforceDuration: undefined,
  appealDays: '60', appealGov: '人民政府', appealAgency: '市场监督管理局',
  lawsuitDays: '', lawsuitCourt: '',
  contactPerson: '', contactPhone: '', attachmentListNo: '',
  signDate: '', copyCount: '', deliveryCount: '', copyNote: '',
});
const formData = reactive<any>(emptyForm());

const checklistLoading = ref(false); const selectChecklistVisible = ref(false);
const checklistTotal = ref(0); const checklistTableData = ref<any[]>([]);
const checklistParams = reactive({ pageNum: 1, pageSize: 10 });

const emptyChecklist = () => ({
  id: undefined, documentNo: '', confirmationText: '上述物品品种、数量经核对无误。',
  partySignature: '', officerSignature: '', witnessSignature: '',
  copiesCount: 2, pageNo: 1, totalPages: 1, items: [],
});
const checklistData = reactive<any>(emptyChecklist());
const checklistDocNo = computed(() => checklistData.documentNo || '');

const checklistDisplayItems = computed(() => {
  const items = checklistData.items || [];
  const rows = [...items];
  while (rows.length < 10) rows.push({ nameOrVenue: '', specOrAddress: '', unit: '', quantity: '', remark: '' });
  return rows;
});

const loadDecisionList = async () => {
  decisionLoading.value = true;
  try { const res = await listEnforcementDecision(decisionParams); decisionTableData.value = res.rows || []; decisionTotal.value = res.total || 0; }
  catch { /* ignore */ } finally { decisionLoading.value = false; }
};

const handleSelectDecision = () => { loadDecisionList(); selectDecisionVisible.value = true; };

const autoLoadChecklist = async (docNo: string) => {
  if (!docNo) return;
  try {
    const res = await listFacilityChecklist({ pageNum: 1, pageSize: 5, documentNo: docNo });
    if (res.rows?.length > 0) {
      const cl = await getFacilityChecklist(res.rows[0].id);
      if (cl.code === 200) { Object.assign(checklistData, cl.data); }
    }
  } catch { /* ignore */ }
};

const selectDecision = async (row: any) => {
  const res = await getEnforcementDecision(row.id);
  if (res.code === 200) {
    Object.assign(formData, res.data);
    selectDecisionVisible.value = false;
    const docNo = res.data.facilityListNo || res.data.attachmentListNo;
    if (docNo) autoLoadChecklist(docNo);
  }
};

const handleDeleteDecision = () => {
  if (!formData.id) { ElMessage.warning('请先选择决定书'); return; }
  ElMessageBox.confirm('确认删除该决定书（不影响关联清单）？', '提示', { type: 'warning' }).then(async () => {
    const res = await delEnforcementDecision(String(formData.id));
    if (res.code === 200) { ElMessage.success('删除成功'); Object.assign(formData, emptyForm()); }
    else { ElMessage.error(res.msg || '删除失败'); }
  }).catch(() => {});
};

const loadChecklistList = async () => {
  checklistLoading.value = true;
  try { const res = await listFacilityChecklist(checklistParams); checklistTableData.value = res.rows || []; checklistTotal.value = res.total || 0; }
  catch { /* ignore */ } finally { checklistLoading.value = false; }
};

const handleSelectChecklist = () => { loadChecklistList(); selectChecklistVisible.value = true; };

const selectChecklist = async (row: any) => {
  const res = await getFacilityChecklist(row.id);
  if (res.code === 200) { Object.assign(checklistData, res.data); selectChecklistVisible.value = false; }
};

const handlePrint = () => {
  const style = document.createElement('style');
  style.innerHTML = `
    @page { size: A4; margin: 0; }
    @media print {
      html, body { margin: 0; padding: 0; width: 210mm; height: 297mm; }
      .noprint { display: none !important; }
      .print-area { display: block !important; padding: 0 !important; margin: 0 !important; box-shadow: none !important; width: 210mm !important; max-width: 210mm !important; background: #fff !important; }
      .page { display: block; width: 210mm; height: 297mm; margin: 0; padding: 20mm 23mm 20mm 23mm; box-sizing: border-box; page-break-after: always; overflow: hidden; }
      .page:last-child { page-break-after: auto; }
      body { font-family: "SimSun","宋体",serif; font-size: 16px; color: #000; line-height: 1.7; }
      * { box-sizing: border-box; }

      .header-agency { text-align: center; font-size: 22px; font-weight: bold; letter-spacing: 4px; margin-bottom: 14pt; }
      .doc-title { text-align: center; font-size: 22px; font-weight: bold; letter-spacing: 6px; margin-bottom: 18pt; }
      .doc-number { font-size: 16px; margin-bottom: 18pt; }
      .u { border-bottom: 1px solid #000; display: inline-block; min-width: 2em; padding: 0 2px; text-align: center; }
      .u.inline { min-width: 5em; }

      .info-table { width: 100%; border-collapse: collapse; margin-bottom: 16pt; }
      .info-table td { padding: 4px 6px; line-height: 2; font-size: 16px; }
      .il { white-space: nowrap; text-align: right; vertical-align: top; }
      .iv { border-bottom: 1px solid #000; min-width: 60px; }

      .body-text { text-indent: 2em; line-height: 2; margin: 0 0 6pt 0; }
      .body-right { text-align: right; padding-right: 2em; line-height: 2; margin: 0 0 10pt 0; }

      .contact-line { margin-top: 14pt; line-height: 2; }
      .attachment-line { margin-top: 14pt; line-height: 2; }

      .seal-area { text-align: right; margin-top: 28pt; padding-right: 30pt; line-height: 2.5; }
      .seal-agency { font-size: 16px; }
      .seal-stamp { font-size: 14px; color: #555; }
      .seal-date { margin-top: 8pt; font-size: 16px; }

      .footer-note { margin-top: 60pt; font-size: 14px; line-height: 2; }

      /* 清单 */
      .cl-header { text-align: center; margin-bottom: 14pt; }
      .cl-dept { font-size: 16px; font-weight: bold; }
      .cl-title { font-size: 18px; font-weight: bold; margin-top: 8pt; }
      .cl-doc-no { text-align: right; font-size: 13px; margin-bottom: 8pt; }
      .cl-table { width: 100%; border-collapse: collapse; margin-bottom: 8pt; table-layout: fixed; }
      .cl-table td, .cl-table th { border: 1px solid #000; font-size: 13px; padding: 5px 4px; text-align: center; vertical-align: middle; }
      .cl-table th { background: #fafafa; font-weight: bold; }
      .cl-table tbody td { height: 30px; }
      .cl-col-seq { width: 30px; }
      .cl-col-unit { width: 32px; }
      .cl-col-qty { width: 32px; }
      .cl-col-rmk { width: 60px; }
      .cl-confirm { font-size: 13px; margin: 8pt 0; line-height: 1.8; }
      .cl-sign-row { display: flex; justify-content: space-between; font-size: 13px; margin-top: 6pt; line-height: 2.2; }
      .cl-footer { display: flex; justify-content: space-between; font-size: 12px; margin-top: 12pt; }
    }
  `;
  document.head.appendChild(style);
  window.print();
  setTimeout(() => document.head.removeChild(style), 500);
};

const handleClear = () => { Object.assign(formData, emptyForm()); Object.assign(checklistData, emptyChecklist()); };
</script>

<style scoped>
.btn-bar { margin-bottom: 12px; display: flex; gap: 10px; flex-wrap: wrap; }

/* 屏幕预览 */
.print-area { background: #fff; max-width: 210mm; margin: 0 auto; box-shadow: 0 2px 12px rgba(0,0,0,.1); }
.page { background: #fff; width: 210mm; padding: 20mm 23mm 20mm 23mm; box-sizing: border-box; margin-bottom: 20px; }
.page-1 { min-height: 297mm; }
.page-2 { min-height: 297mm; }
.page-checklist { min-height: 297mm; }

.header-agency { text-align: center; font-size: 22px; font-weight: bold; letter-spacing: 4px; margin-bottom: 14pt; }
.doc-title { text-align: center; font-size: 22px; font-weight: bold; letter-spacing: 6px; margin-bottom: 18pt; }
.doc-number { font-size: 16px; margin-bottom: 18pt; }

.u { border-bottom: 1px solid #000; display: inline-block; min-width: 2em; padding: 0 2px; text-align: center; }
.u.inline { min-width: 5em; }

.info-table { width: 100%; border-collapse: collapse; margin-bottom: 16pt; }
.info-table td { padding: 4px 6px; line-height: 2; font-size: 16px; }
.il { white-space: nowrap; text-align: right; vertical-align: top; }
.iv { border-bottom: 1px solid #000; min-width: 60px; }

.body-text { text-indent: 2em; line-height: 2; margin: 0 0 6pt 0; font-size: 16px; }
.body-right { text-align: right; padding-right: 2em; line-height: 2; margin: 0 0 10pt 0; font-size: 16px; }

.contact-line { margin-top: 14pt; line-height: 2; font-size: 16px; }
.attachment-line { margin-top: 14pt; line-height: 2; font-size: 16px; }

.seal-area { text-align: right; margin-top: 28pt; padding-right: 30pt; line-height: 2.5; }
.seal-agency { font-size: 16px; }
.seal-stamp { font-size: 14px; color: #555; }
.seal-date { margin-top: 8pt; font-size: 16px; }

.footer-note { margin-top: 60pt; font-size: 14px; line-height: 2; }

/* 清单预览 */
.cl-header { text-align: center; margin-bottom: 14pt; }
.cl-dept { font-size: 16px; font-weight: bold; }
.cl-title { font-size: 18px; font-weight: bold; margin-top: 8pt; }
.cl-doc-no { text-align: right; font-size: 13px; margin-bottom: 8pt; }
.cl-table { width: 100%; border-collapse: collapse; margin-bottom: 8pt; table-layout: fixed; }
.cl-table td, .cl-table th { border: 1px solid #000; font-size: 13px; padding: 5px 4px; text-align: center; vertical-align: middle; }
.cl-table th { background: #fafafa; font-weight: bold; }
.cl-table tbody td { height: 30px; }
.cl-col-seq { width: 30px; }
.cl-col-unit { width: 32px; }
.cl-col-qty { width: 32px; }
.cl-col-rmk { width: 60px; }
.cl-confirm { font-size: 13px; margin: 8pt 0; line-height: 1.8; }
.cl-sign-row { display: flex; justify-content: space-between; font-size: 13px; margin-top: 6pt; line-height: 2.2; }
.cl-footer { display: flex; justify-content: space-between; font-size: 12px; margin-top: 12pt; }
</style>
