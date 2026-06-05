<template>
  <div class="p-2">
    <!-- 操作栏 -->
    <div class="btn-bar noprint">
      <el-button type="primary" icon="Search" @click="handleSelect">📋 选择检查记录</el-button>
      <el-button type="success" icon="Printer" @click="handlePrint" :disabled="!formData.id">🖨️ 打印输出</el-button>
      <el-button type="danger" icon="Delete" @click="handleDelete" :disabled="!formData.id">🗑️ 删除</el-button>
      <el-button icon="RefreshRight" @click="handleClear">🔄 清空</el-button>
    </div>

    <!-- 打印表单 -->
    <div class="print-area">
    <div class="page" id="printArea">
      <div class="title-line1">汾阳市市场监督管理局</div>
      <div class="title-line2">检查记录表</div>

      <table class="form-table">
        <colgroup>
          <col style="width:28px;"><col style="width:110px;">
          <col style="width:auto;"><col style="width:70px;"><col style="width:auto;">
        </colgroup>
        <tr class="h-name"><td class="vlabel" rowspan="6">被检查单位</td><td class="flabel">名称</td><td class="fvalue" colspan="3">{{ formData.inspectedUnit }}</td></tr>
        <tr class="h-legal"><td class="flabel">法定代表人<br>（负责人）</td><td class="fvalue">{{ formData.legalPerson }}</td><td class="flabel flabel-sm">联系电话</td><td class="fvalue">{{ formData.contactPhone }}</td></tr>
        <tr class="h-credit"><td class="flabel">统一社会信用代码<br>（注册号）</td><td class="fvalue" colspan="3">{{ formData.unifiedSocialCreditCode }}</td></tr>
        <tr class="h-addr"><td class="flabel">住所（住址）</td><td class="fvalue" colspan="3">{{ formData.address }}</td></tr>
        <tr class="h-time"><td class="flabel">检查时间</td><td class="fvalue" colspan="3">从 {{ formData.inspectionDate || '____年__月__日__时__分' }} 至 ____年__月__日__时__分</td></tr>
        <tr class="h-type">
          <td class="flabel">检查类别</td>
          <td class="fvalue" colspan="3">
            <span class="check-opts">
              <span v-for="t in checkTypes" :key="t" style="margin-right:18px;">
                <span class="box">{{ formData.inspectionType?.includes(t) ? '✓' : '' }}</span> {{ t }}
              </span>
            </span>
          </td>
        </tr>
      </table>

      <table class="form-table sec-rectify" style="border-top:none;">
        <tr>
          <td class="flabel" style="width:146px;">上次检查存<br>在的问题的<br>整改情况</td>
          <td style="padding:0;"><div class="content-cell" v-html="formData.rectificationReport || '&nbsp;'"></div></td>
        </tr>
      </table>

      <table class="form-table sec-findings" style="border-top:none;">
        <tr>
          <td class="flabel" style="width:146px;">本次检查情况</td>
          <td style="padding:0;"><div class="content-cell" v-html="formData.inspectionSituation || '&nbsp;'"></div></td>
        </tr>
      </table>

      <table class="form-table sec-opinion" style="border-top:none;">
        <tr>
          <td class="flabel" style="width:146px;">本次检查处<br>理意见</td>
          <td style="padding:0;"><div class="content-cell" v-html="formData.handlingOpinion || '&nbsp;'"></div></td>
        </tr>
      </table>

      <div class="personnel-row">
        <span class="personnel-label">检查人员：</span>
        <span class="personnel-value">{{ formData.inspector || '______' }}</span>
        <span class="personnel-label">&emsp;记录人员：</span>
        <span class="personnel-value">{{ formData.recorder || '______' }}</span>
      </div>

      <table class="sign-table">
        <tr>
          <td style="width:50%;"><span class="sign-label">被检查单位（签章）</span><div class="sign-date">年&emsp;&emsp;月&emsp;&emsp;日</div></td>
          <td style="width:50%;"><span class="sign-label">检查人员（签字）</span><div class="sign-date">年&emsp;&emsp;月&emsp;&emsp;日</div></td>
        </tr>
      </table>

      <div class="footer-note">本检查记录表一式两份，监管部门和被检查单位各一份。</div>
    </div>
    </div>

    <!-- 选择检查记录弹窗 -->
    <el-dialog v-model="selectVisible" title="选择检查记录" width="900px" append-to-body destroy-on-close>
      <el-table :data="tableData" border highlight-current-row @row-dblclick="selectRecord" v-loading="loading">
        <el-table-column label="序号" type="index" width="60" align="center" />
        <el-table-column label="被检查单位" prop="inspectedUnit" min-width="160" show-overflow-tooltip />
        <el-table-column label="检查日期" prop="inspectionDate" width="120" align="center" />
        <el-table-column label="检查形式" prop="inspectionType" width="120" align="center" />
        <el-table-column label="创建时间" prop="createTime" width="170" align="center" />
        <el-table-column label="操作" width="80" align="center">
          <template #default="scope"><el-button link type="primary" @click="selectRecord(scope.row)">选择</el-button></template>
        </el-table-column>
      </el-table>
      <pagination v-show="total > 0" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" :total="total" @pagination="loadList" />
    </el-dialog>
  </div>
</template>

<script setup name="InspectionForm" lang="ts">
import { ref, reactive } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { listInspection, getInspection, delInspection } from '@/api/business/inspection/index';

const loading = ref(false);
const selectVisible = ref(false);
const tableData = ref<any[]>([]);
const total = ref(0);

const queryParams = reactive({ pageNum: 1, pageSize: 10, inspectedUnit: '' });

const formData = reactive<any>({
  id: '', inspectedUnit: '', legalPerson: '', contactPhone: '',
  unifiedSocialCreditCode: '', address: '', inspectionDate: '',
  inspectionType: '', inspectionSituation: '', handlingOpinion: '',
  rectificationReport: '', inspector: '', recorder: ''
});

const checkTypes = ['随机抽查', '转办转交', '接诉检查', '专项检查', '日常检查'];

const loadList = async () => {
  loading.value = true;
  try {
    const res = await listInspection(queryParams);
    tableData.value = res.rows || [];
    total.value = res.total || 0;
  } catch { /* ignore */ }
  finally { loading.value = false; }
};

const handleSelect = () => {
  queryParams.pageNum = 1;
  loadList();
  selectVisible.value = true;
};

const selectRecord = async (row: any) => {
  try {
    const res = await getInspection(row.id);
    Object.assign(formData, res.data || {});
    selectVisible.value = false;
    ElMessage.success('已加载检查记录');
  } catch { /* ignore */ }
};

const handlePrint = () => {
  if (!formData.id) { ElMessage.warning('请先选择一条检查记录'); return; }
  window.print();
};

const handleDelete = () => {
  if (!formData.id) return;
  ElMessageBox.confirm('确认删除该检查记录？', '提示', { type: 'warning' }).then(async () => {
    await delInspection(formData.id);
    ElMessage.success('删除成功');
    clearForm();
  });
};

const clearForm = () => {
  Object.assign(formData, {
    id: '', inspectedUnit: '', legalPerson: '', contactPhone: '',
    unifiedSocialCreditCode: '', address: '', inspectionDate: '',
    inspectionType: '', inspectionSituation: '', handlingOpinion: '',
    rectificationReport: '', inspector: '', recorder: '',
  });
};

const handleClear = () => { clearForm(); ElMessage.success('已清空'); };
</script>

<style scoped>
* { margin:0; padding:0; box-sizing:border-box; }
.btn-bar { text-align:center; margin-bottom:15px; position:sticky; top:10px; z-index:100; }
.btn-bar .el-button { margin:0 6px; }

.print-area { background: #fff; max-width: 210mm; margin: 0 auto; box-shadow: 0 2px 12px rgba(0,0,0,.1); }

.page { width: 210mm; padding: 20mm 23mm; box-sizing: border-box; background: #fff; margin-bottom: 15px; font-family: "SimSun","宋体",serif; }

.title-line1 { text-align:center; font-size:18px; font-family:"SimHei","黑体",sans-serif; padding-top:4px; }
.title-line2 { text-align:center; font-size:22px; font-weight:bold; font-family:"SimHei","黑体",sans-serif; letter-spacing:8px; padding:4px 0 18px; border-bottom:1px solid #000; }

.form-table { width:100%; border-collapse:collapse; table-layout:fixed; }
.form-table td { border:1px solid #000; padding:0; vertical-align:top; font-size:16px; }

.vlabel { width:28px; text-align:center; font-weight:bold; font-size:16px; letter-spacing:4px; writing-mode:vertical-rl; text-orientation:upright; background:#fafafa; padding:8px 2px; }

.flabel { background:#fafafa; text-align:center; font-weight:bold; font-size:16px; padding:4px 6px; width:110px; line-height:2; }
.flabel-sm { width:70px; }
.fvalue { padding:6px 10px; font-size:16px; line-height:2; }

.h-name td { height:44px; }
.h-legal td { height:60px; }
.h-credit td { height:64px; }
.h-addr td { height:44px; }
.h-time td { height:44px; }
.h-type td { height:100px; }

.check-opts { padding:8px 12px; line-height:2; font-size:16px; }
.box { display:inline-block; width:16px; height:16px; border:1.5px solid #000; margin-right:3px; vertical-align:-2px; text-align:center; font-size:12px; line-height:16px; }

.sec-rectify .content-cell { min-height:90px; }
.sec-findings .content-cell { min-height:185px; }
.sec-opinion .content-cell { min-height:60px; }
.content-cell { padding:12px 15px; line-height:2; font-size:16px; border:1px solid #000; border-top:none; }
.content-cell p { text-indent:2em; margin:4px 0; }

.personnel-row { padding:16px 10px 8px; font-size:16px; line-height:2; }
.personnel-label { font-weight:bold; }
.personnel-value { display:inline-block; min-width:150px; border-bottom:1px solid #000; margin-right:20px; }

.sign-table { width:100%; border-collapse:collapse; table-layout:fixed; }
.sign-table td { border:1px solid #000; padding:18px 30px 14px; text-align:center; font-size:16px; vertical-align:top; }
.sign-label { font-weight:bold; display:block; margin-bottom:24px; }
.sign-date { letter-spacing:6px; }

.footer-note { text-align:center; font-size:14px; padding:16px 0 22px; color:#333; border-top:1px solid #000; }

@media print {
  .btn-bar, .noprint { display: none !important; }
  @page { size: A4; margin: 0; }
  .print-area { box-shadow: none; max-width: none; margin: 0; }
  .page { width: 210mm; height: 297mm; padding: 20mm 23mm; box-sizing: border-box; page-break-after: always; overflow: hidden; box-shadow: none; margin-bottom: 0; }
  .page:last-child { page-break-after: auto; }
  body { font-family: "SimSun","宋体",serif; font-size: 16px; color: #000; line-height: 2; }
  .vlabel, .flabel { background: none !important; }
}
</style>
