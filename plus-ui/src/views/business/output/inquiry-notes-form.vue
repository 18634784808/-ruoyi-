<template>
  <div class="p-2">
    <!-- 操作栏 -->
    <div class="btn-bar">
      <el-button type="primary" icon="Search" @click="handleSelect">📋 选择笔录</el-button>
      <el-button type="success" icon="Printer" @click="handlePrint" :disabled="!formData.id">🖨️ 打印</el-button>
      <el-button type="danger" icon="Delete" @click="handleDelete" :disabled="!formData.id">🗑️ 删除</el-button>
      <el-button icon="RefreshRight" @click="handleClear">🔄 清空</el-button>
    </div>

    <div class="print-area" id="printArea">
      <!-- ========== 第1页 ========== -->
      <div class="page page-1">
        <div class="header-agency">汾阳市市场监督管理局</div>
        <div class="doc-title">询 问 笔 录</div>

        <!-- 时间地点 -->
        <table class="info-table top-table">
          <tr>
            <td class="ilabel">时间：</td>
            <td class="ivalue">{{ formatDate(formData.startTime) }}</td>
            <td class="ilabel">至</td>
            <td class="ivalue">{{ formatDate(formData.endTime) }}</td>
            <td class="ilabel">第</td>
            <td class="ivalue" style="width:40px;min-width:40px;">{{ formData.times || '' }}</td>
            <td class="ilabel">次</td>
          </tr>
          <tr>
            <td class="ilabel">地点：</td>
            <td class="ivalue" colspan="6">{{ formData.place }}</td>
          </tr>
        </table>

        <!-- 人员信息 -->
        <table class="info-table">
          <tr>
            <td class="ilabel">询问人：</td>
            <td class="ivalue">{{ formData.inquirer }}</td>
            <td class="ilabel">执法证号：</td>
            <td class="ivalue">{{ inspectorCertNo }}</td>
          </tr>
          <tr>
            <td class="ilabel">记录人：</td>
            <td class="ivalue" colspan="3">{{ inspectorCopyCertNo }}</td>
          </tr>
          <tr>
            <td class="ilabel">被询问人：</td>
            <td class="ivalue">{{ formData.party }}</td>
            <td class="ilabel">性别：</td>
            <td class="ivalue" style="width:80px;">{{ formData.gender === '1' ? '男' : formData.gender === '2' ? '女' : '' }}</td>
          </tr>
          <tr>
            <td class="ilabel">身份证件号码：</td>
            <td class="ivalue" colspan="3">{{ formData.idNumber || '&emsp;' }}</td>
          </tr>
          <tr>
            <td class="ilabel">工作单位：</td>
            <td class="ivalue">{{ formData.workUnit || '&emsp;' }}</td>
            <td class="ilabel">职务：</td>
            <td class="ivalue">{{ formData.position }}</td>
          </tr>
          <tr>
            <td class="ilabel">联系电话：</td>
            <td class="ivalue">{{ formData.phone || '&emsp;' }}</td>
            <td class="ilabel">其他联系方式：</td>
            <td class="ivalue">{{ formData.otherContact }}</td>
          </tr>
          <tr>
            <td class="ilabel">联系地址：</td>
            <td class="ivalue" colspan="3">{{ formData.contactAddress || '&emsp;' }}</td>
          </tr>
        </table>

        <!-- 标准开场白 -->
        <div class="qa-block">
          <div class="qa-inquirer">
            询问人：你好，我们是<span class="u">{{ agencyName }}</span>市场监督管理局的执法人员，已向你出示了我们的执法证件。你是否看清楚？
          </div>
          <div class="qa-answer">
            被询问人：<span class="u inline">{{ formData.answerStd1 || '&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;' }}</span>
          </div>
        </div>

        <div class="qa-block">
          <div class="qa-inquirer">
            问：我们依法就<span class="u inline">{{ formData.investigateTopic || '&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;' }}</span>有关问题进行调查，请予配合。依照法律规定，你有权进行陈述和申辩。如果你认为调查人员与本案有直接利害关系或者有其他关系可能影响公正执法的，依法有申请回避的权利，你是否申请调查人员回避？
          </div>
          <div class="qa-answer">
            答：<span class="u inline">{{ formData.answerStd2 || '&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;' }}</span>
          </div>
        </div>

        <div class="qa-block">
          <div class="qa-inquirer">
            问：你应当如实回答询问，并协助调查，不得拒绝或者阻挠。你是否明白？
          </div>
          <div class="qa-answer">
            答：<span class="u inline">{{ formData.answerStd3 || '&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;' }}</span>
          </div>
        </div>

        <!-- 动态Q&A - 显示在首页 -->
        <template v-for="n in firstPageQa" :key="'qa1-'+n">
          <div class="qa-block" v-if="formData['question' + n] || formData['answer' + n]">
            <div class="qa-inquirer">问：{{ formData['question' + n] }}</div>
            <div class="qa-answer">答：{{ formData['answer' + n] }}</div>
          </div>
        </template>

        <!-- 页码与签名 -->
        <div class="page-footer">
          <div class="sign-row">
            <span>被询问人：<span class="u" style="min-width:80px;">&emsp;&emsp;&emsp;</span></span>
            <span style="margin-left:40px;">年&emsp;月&emsp;日</span>
          </div>
          <div class="sign-row">
            <span>询问人：<span class="u" style="min-width:60px;">&emsp;&emsp;&emsp;</span>、<span class="u" style="min-width:60px;">&emsp;&emsp;&emsp;</span></span>
            <span style="margin-left:40px;">年&emsp;月&emsp;日</span>
          </div>
          <div class="page-num">第 1 页共 {{ totalPages }} 页</div>
        </div>
      </div>

      <!-- ========== 中间页（第2页起） ========== -->
      <div class="page" v-for="pageNum in middlePages" :key="'mp'+pageNum">
        <template v-for="n in middlePageQa(pageNum)" :key="'qa'+pageNum+'-'+n">
          <div class="qa-block" v-if="formData['question' + n] || formData['answer' + n]">
            <div class="qa-inquirer">问：{{ formData['question' + n] }}</div>
            <div class="qa-answer">答：{{ formData['answer' + n] }}</div>
          </div>
        </template>
        <div class="page-footer">
          <div class="sign-row">
            <span>被询问人：<span class="u" style="min-width:80px;">&emsp;&emsp;&emsp;</span></span>
            <span style="margin-left:40px;">年&emsp;月&emsp;日</span>
          </div>
          <div class="sign-row">
            <span>询问人：<span class="u" style="min-width:60px;">&emsp;&emsp;&emsp;</span>、<span class="u" style="min-width:60px;">&emsp;&emsp;&emsp;</span></span>
            <span style="margin-left:40px;">年&emsp;月&emsp;日</span>
          </div>
          <div class="page-num">第 {{ pageNum }} 页共 {{ totalPages }} 页</div>
        </div>
      </div>

      <!-- ========== 末页 ========== -->
      <div class="page page-last" v-if="hasLastPage">
        <template v-for="n in lastPageQa" :key="'qalast-'+n">
          <div class="qa-block" v-if="formData['question' + n] || formData['answer' + n]">
            <div class="qa-inquirer">问：{{ formData['question' + n] }}</div>
            <div class="qa-answer">答：{{ formData['answer' + n] }}</div>
          </div>
        </template>

        <!-- 末页确认语 -->
        <div class="qa-block final-check">
          <div class="qa-inquirer">
            询问人：以上是本次询问情况的记录，请核对/已向你宣读。与你所述一致请签名，如果有遗漏你可以补正。
          </div>
          <div class="qa-answer">
            被询问人：<span class="u inline">{{ formData.finalConfirm || '&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;' }}</span>
          </div>
        </div>

        <div class="page-footer">
          <div class="sign-row">
            <span>被询问人：<span class="u" style="min-width:80px;">&emsp;&emsp;&emsp;</span></span>
            <span style="margin-left:40px;">年&emsp;月&emsp;日</span>
          </div>
          <div class="sign-row">
            <span>询问人：<span class="u" style="min-width:60px;">&emsp;&emsp;&emsp;</span>、<span class="u" style="min-width:60px;">&emsp;&emsp;&emsp;</span></span>
            <span style="margin-left:40px;">年&emsp;月&emsp;日</span>
          </div>
          <div class="page-num">第 {{ lastPageNum }} 页共 {{ totalPages }} 页</div>
        </div>
      </div>
    </div>

    <!-- 选择记录弹窗 -->
    <el-dialog v-model="selectVisible" title="选择询问笔录" width="1100px" append-to-body destroy-on-close>
      <el-table :data="tableData" border highlight-current-row @row-dblclick="selectRecord" v-loading="loading">
        <el-table-column label="序号" type="index" width="60" align="center" />
        <el-table-column label="地点" prop="place" min-width="120" show-overflow-tooltip />
        <el-table-column label="当事人" prop="party" width="120" />
        <el-table-column label="询问人" prop="inquirer" width="120" />
        <el-table-column label="次数" prop="times" width="60" align="center" />
        <el-table-column label="创建时间" prop="createTime" width="170" align="center" />
        <el-table-column label="操作" width="80" align="center">
          <template #default="scope"><el-button link type="primary" @click="selectRecord(scope.row)">选择</el-button></template>
        </el-table-column>
      </el-table>
      <pagination v-show="total > 0" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" :total="total" @pagination="loadList" />
    </el-dialog>
  </div>
</template>

<script setup name="BusinessOutputInquiryNotesForm" lang="ts">
import { ref, reactive, computed } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { listInquiryNotes, getInquiryNotes, delInquiryNotes } from '@/api/business/inquiry-notes/index';
import { getAllInspector } from '@/api/business/inspector/index';

const loading = ref(false); const selectVisible = ref(false);
const total = ref(0); const tableData = ref<any[]>([]);
const queryParams = reactive({ pageNum: 1, pageSize: 10 });
const agencyName = ref('汾阳市');

const emptyForm = () => ({
  id: undefined,
  startTime: '', endTime: '', times: undefined, questionCount: undefined,
  place: '', inquirer: '', inquirerCopy: '',
  party: '', gender: '', idNumber: '', workUnit: '',
  position: '', phone: '', otherContact: '', contactAddress: '',
  answerStd1: '', investigateTopic: '', answerStd2: '', answerStd3: '',
  question1: '', answer1: '', question2: '', answer2: '',
  question3: '', answer3: '', question4: '', answer4: '',
  question5: '', answer5: '', question6: '', answer6: '',
  question7: '', answer7: '', question8: '', answer8: '',
  question9: '', answer9: '', question10: '', answer10: '',
  finalConfirm: '', remark: '',
});

const formData = reactive<any>(emptyForm());

const inspectorCertNo = ref('');
const inspectorCopyCertNo = ref('');

const formatDate = (val: string) => {
  if (!val) return '';
  const d = new Date(val);
  const year = d.getFullYear();
  const month = d.getMonth() + 1;
  const day = d.getDate();
  const hour = d.getHours();
  const minute = d.getMinutes();
  return `${year}年${month}月${day}日${hour}时${minute}分`;
};

// Calculate QA distribution across pages
const firstPageQaCount = 2; // first 2 QAs on page 1
const qaPerMidPage = 3; // 3 QAs per middle page

const filledQaCount = computed(() => {
  let count = 0;
  for (let i = 1; i <= 10; i++) {
    if (formData['question' + i] || formData['answer' + i]) count++;
  }
  return count;
});

const firstPageQa = computed(() => {
  const arr = [];
  for (let i = 1; i <= Math.min(firstPageQaCount, 10); i++) arr.push(i);
  return arr;
});

const middlePageCount = computed(() => {
  if (filledQaCount.value <= firstPageQaCount) return 0;
  const remaining = filledQaCount.value - firstPageQaCount;
  const t = Math.ceil(remaining / qaPerMidPage);
  return Math.max(0, t - 1); // last of remaining is the "last page"
});

const lastPageNum = computed(() => {
  if (!hasLastPage.value) return 1;
  return 2 + middlePageCount.value; // page1(1) + middle(s) + last
});

const hasLastPage = computed(() => filledQaCount.value > firstPageQaCount);
const totalPages = computed(() => {
  if (!hasLastPage.value) return 1;
  return 2 + middlePageCount.value; // page1 + middle(s) + last
});

const middlePages = computed(() => {
  const arr = [];
  for (let i = 0; i < middlePageCount.value; i++) arr.push(2 + i);
  return arr;
});

const middlePageQa = (pageNum: number) => {
  const start = firstPageQaCount + (pageNum - 2) * qaPerMidPage + 1;
  const end = Math.min(start + qaPerMidPage - 1, 10);
  const arr = [];
  for (let i = start; i <= end; i++) arr.push(i);
  return arr;
};

const lastPageQa = computed(() => {
  if (filledQaCount.value <= firstPageQaCount) return [];
  const start = firstPageQaCount + middlePageCount.value * qaPerMidPage + 1;
  const end = 10;
  const arr = [];
  for (let i = start; i <= end; i++) arr.push(i);
  return arr;
});

const loadList = async () => {
  loading.value = true;
  try { const res = await listInquiryNotes(queryParams); tableData.value = res.rows || []; total.value = res.total || 0; }
  catch { /* ignore */ } finally { loading.value = false; }
};

const handleSelect = () => { loadList(); selectVisible.value = true; };

const selectRecord = async (row: any) => {
  const res = await getInquiryNotes(row.id);
  if (res.code === 200) {
    Object.assign(formData, emptyForm());
    Object.assign(formData, res.data);
    // Fetch cert numbers from inspector
    fetchCertNos(res.data.inquirer, res.data.inquirerCopy);
    selectVisible.value = false;
  }
};

const fetchCertNos = async (inquirer: string, inquirerCopy: string) => {
  try {
    const res = await getAllInspector();
    const inspectors = res.data || [];
    const match1 = inspectors.find((i: any) => i.name === inquirer);
    const match2 = inspectors.find((i: any) => i.name === inquirerCopy);
    inspectorCertNo.value = match1?.certNo || '';
    inspectorCopyCertNo.value = match2?.certNo || '';
  } catch { /* ignore */ }
};

const handlePrint = () => {
  window.print();
};

const handleDelete = () => {
  if (!formData.id) { ElMessage.warning('请先选择记录'); return; }
  ElMessageBox.confirm('确认删除该询问笔录？', '提示', { type: 'warning' }).then(async () => {
    const res = await delInquiryNotes(String(formData.id));
    if (res.code === 200) { ElMessage.success('删除成功'); Object.assign(formData, emptyForm()); }
    else { ElMessage.error(res.msg || '删除失败'); }
  }).catch(() => {});
};

const handleClear = () => { Object.assign(formData, emptyForm()); };
</script>

<style scoped>
/* ========== Screen Styles ========== */
.btn-bar { margin-bottom: 12px; display: flex; gap: 10px; flex-wrap: wrap; }
.print-area { background: #fff; max-width: 210mm; margin: 0 auto; box-shadow: 0 2px 12px rgba(0,0,0,.1); }
.page { width: 210mm; padding: 20mm 23mm; box-sizing: border-box; background: #fff; margin-bottom: 15px; }

/* Typography */
.header-agency { text-align: center; font-size: 22px; font-weight: bold; letter-spacing: 4px; margin: 5mm 0 8mm; }
.doc-title { text-align: center; font-size: 22px; font-weight: bold; letter-spacing: 10px; margin-bottom: 10mm; }
.info-table { width: 100%; border-collapse: collapse; margin-bottom: 6mm; }
.info-table td { padding: 2px 6px; line-height: 2; font-size: 16px; }
.ilabel { white-space: nowrap; text-align: right; font-size: 16px; }
.ivalue { border-bottom: 1px solid #000; min-width: 60px; font-size: 16px; }
.qa-block { margin-bottom: 4mm; }
.qa-inquirer { line-height: 2; padding-bottom: 2mm; font-size: 16px; }
.qa-answer { line-height: 2; padding-left: 2em; margin-bottom: 2mm; font-size: 16px; }
.u { border-bottom: 1px solid #000; display: inline-block; min-width: 30px; padding: 0 4px; }
.u.inline { min-width: 80px; }
.page-footer { margin-top: auto; padding-top: 10mm; font-size: 14px; }
.sign-row { margin-bottom: 4mm; font-size: 14px; }
.page-num { text-align: center; margin-top: 5mm; font-size: 14px; }
.final-check { margin-top: 15mm; }

/* ========== Print Styles ========== */
@media print {
  .btn-bar, .noprint { display: none !important; }
  @page { size: A4; margin: 0; }
  body { font-family: "SimSun","宋体",serif; font-size: 16px; color: #000; line-height: 2; }
  .print-area { box-shadow: none; max-width: none; margin: 0; }
  .page { width: 210mm; height: 297mm; padding: 20mm 23mm; box-sizing: border-box; page-break-after: always; overflow: hidden; margin-bottom: 0; }
  .page:last-child { page-break-after: auto; }
}
</style>
