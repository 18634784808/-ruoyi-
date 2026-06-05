# RuoYi-Vue-Plus 全栈开发技术笔记

> 基于「市场监管综合执法系统」14 个业务模块的真实开发经验  
> 后端：RuoYi-Vue-Plus 5.5.3 + Spring Boot 3 + MyBatis-Plus + Sa-Token  
> 前端：Vue 3 + TypeScript + Element Plus + Vite  
> 数据库：MySQL 8 + 多租户架构

---

## 目录

- [1. 项目架构总览](#1-项目架构总览)
- [2. 后端开发模式（一套模板通吃所有 CRUD）](#2-后端开发模式)
- [3. 前端开发模式](#3-前端开发模式)
- [4. 数据库设计规范](#4-数据库设计规范)
- [5. 输出表单开发](#5-输出表单开发)
- [6. 业务字典模块（BizDict）](#6-业务字典模块)
- [7. 跨模块通信](#7-跨模块通信)
- [8. 开发避坑指南](#8-开发避坑指南)
- [9. 开发工作流](#9-开发工作流)
- [10. 模块全景图](#10-模块全景图)

---

## 1. 项目架构总览

```
RuoYi-Vue-Plus-5.X/
├── ruoyi-admin/          ← 启动模块（Spring Boot 入口）
├── ruoyi-common/         ← 公共模块（工具类、异常、注解等）
├── ruoyi-extend/         ← 扩展模块
├── ruoyi-modules/
│   ├── ruoyi-system/     ← 系统模块（用户、角色、菜单等）
│   ├── ruoyi-enterprise/ ← ★ 业务模块（所有14个业务功能在此）
│   ├── ruoyi-workflow/   ← 工作流模块
│   ├── ruoyi-job/        ← 定时任务模块
│   └── ruoyi-generator/  ← 代码生成器
├── plus-ui/              ← 前端 Vue 3 项目
└── script/sql/           ← 数据库脚本
```

### 包路径规范

所有自定义业务代码必须在 `org.dromara.enterprise` 包下：

```
org.dromara.enterprise/
├── controller/    ← REST 控制器
├── domain/        ← Entity 实体（对应数据库表）
│   ├── bo/        ← Business Object（请求参数）
│   └── vo/        ← View Object（响应数据）
├── mapper/        ← MyBatis-Plus Mapper
└── service/       ← 业务逻辑
    └── impl/      ← 实现类
```

### 技术栈速览

| 层级 | 技术 | 版本 | 作用 |
|------|------|------|------|
| 后端框架 | Spring Boot 3 | 3.5.9 | 核心框架 |
| ORM | MyBatis-Plus | 3.5.16 | 数据库操作 |
| 对象映射 | MapStruct Plus | 1.5.0 | Bo ↔ Entity ↔ Vo 转换 |
| 权限认证 | Sa-Token | 1.44.0 | 登录 + 权限注解 |
| Excel | FastExcel | 1.3.0 | 导入导出 |
| 工具库 | Hutool | 5.8.43 | 通用工具 |
| 前端框架 | Vue 3 + TS | — | Composition API |
| UI 库 | Element Plus | — | 组件库 |
| 样式 | UnoCSS | — | 原子化 CSS |

---

## 2. 后端开发模式

> **核心结论：RuoYi-Vue-Plus 对 CRUD 模块提供了近乎零代码的抽象，新增一个模块只需写 5 个文件 + 1 条建表 SQL。**

### 2.1 文件清单（以 Ledger 台账为例）

| # | 文件 | 作用 | 代码量 |
|---|------|------|--------|
| 1 | `domain/Ledger.java` | Entity，映射 `biz_ledger` 表 | ~40行 |
| 2 | `domain/bo/LedgerBo.java` | 请求体，含 `@NotBlank` 校验 | ~50行 |
| 3 | `domain/vo/LedgerVo.java` | 响应体，含 `@ExcelProperty` 导出注解 | ~60行 |
| 4 | `mapper/LedgerMapper.java` | 继承 `BaseMapperPlus`，空接口即可 | ~8行 |
| 5 | `service/ILedgerService.java` | 接口定义 | ~15行 |
| 6 | `service/impl/LedgerServiceImpl.java` | 核心实现 | ~65行 |
| 7 | `controller/LedgerController.java` | REST 接口 | ~70行 |

**总计：约 310 行 Java 代码，实现完整的增删改查+分页+导出。**

### 2.2 Entity 实体类模式

```java
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("biz_ledger")        // ← 映射表名
public class Ledger extends BaseEntity {  // ← 继承 BaseEntity（含 createTime/updateBy/等）

    @TableId(value = "id", type = IdType.ASSIGN_ID)  // ← 雪花算法主键
    private Long id;

    private String inspectedUnit;   // 字段名 = 数据库列名（下划线自动转驼峰）
    private String legalPerson;
    // ... 其他字段
}
```

**关键点：**
- 必须继承 `BaseEntity`，自动获得 `createTime`, `updateTime`, `createBy`, `updateBy`, `delFlag`
- 使用 `IdType.ASSIGN_ID`（雪花算法）而非自增 ID
- 多租户场景下 `BaseEntity` 包含 `tenantId`

### 2.3 BO（请求参数）模式

```java
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = Ledger.class, reverseConvertGenerate = false)  // ← MapStruct 自动转换
public class LedgerBo extends BaseEntity {

    private Long id;

    @NotBlank(message = "被检查单位不能为空")           // ← JSR-303 校验
    @Size(max = 200, message = "被检查单位长度不能超过200个字符")
    private String inspectedUnit;

    private String legalPerson;
    // ...
}
```

**关键点：**
- `@AutoMapper(target = Ledger.class)` — 编译期自动生成 Bo → Entity 的转换代码
- `@NotBlank` / `@Size` — 需要 Controller 上标注 `@Validated`
- BO 也继承 `BaseEntity`，支持分页参数（pageNum/pageSize）

### 2.4 VO（响应数据）模式

```java
@Data
@ExcelIgnoreUnannotated                        // ← FastExcel：只导出带注解的字段
@AutoMapper(target = Ledger.class)
public class LedgerVo implements Serializable {

    @ExcelProperty(value = "序号")              // ← 导出时的列名
    private Long id;

    @ExcelProperty(value = "被检查单位")
    private String inspectedUnit;

    @ExcelProperty(value = "创建时间")
    private Date createTime;
}
```

### 2.5 Mapper 模式（最简）

```java
public interface LedgerMapper extends BaseMapperPlus<Ledger, LedgerVo> {
    // 继承 BaseMapperPlus<Entity, Vo> 后，零代码即可使用：
    // - selectVoPage(page, wrapper)   → 分页查询
    // - selectVoList(wrapper)         → 列表查询
    // - selectVoById(id)              → 单条查询
    // - insert(entity)                → 新增
    // - updateById(entity)            → 更新
    // - deleteByIds(ids)              → 批量删除
}
```

### 2.6 ServiceImpl 模式（核心逻辑）

```java
@RequiredArgsConstructor
@Service
public class LedgerServiceImpl implements ILedgerService {

    private final LedgerMapper baseMapper;

    // ★ 核心：构建查询条件
    private LambdaQueryWrapper<Ledger> buildQueryWrapper(LedgerBo bo) {
        return new LambdaQueryWrapper<Ledger>()
            .like(StringUtils.isNotBlank(bo.getInspectedUnit()),
                  Ledger::getInspectedUnit, bo.getInspectedUnit())  // 模糊匹配
            .eq(StringUtils.isNotBlank(bo.getRectificationProgress()),
                Ledger::getRectificationProgress, bo.getRectificationProgress())  // 精确匹配
            .orderByDesc(Ledger::getCreateTime);                     // 按创建时间倒序
    }

    // 分页查询
    public TableDataInfo<LedgerVo> selectPageLedgerList(LedgerBo bo, PageQuery pageQuery) {
        Page<LedgerVo> page = baseMapper.selectVoPage(pageQuery.build(), buildQueryWrapper(bo));
        return TableDataInfo.build(page);  // → 返回 { rows, total, code, msg }
    }

    // 列表查询（导出用）
    public List<LedgerVo> selectLedgerList(LedgerBo bo) {
        return baseMapper.selectVoList(buildQueryWrapper(bo));
    }

    // 新增
    public int insertLedger(LedgerBo bo) {
        Ledger ledger = MapstructUtils.convert(bo, Ledger.class);  // BO → Entity
        return baseMapper.insert(ledger);
    }

    // 更新
    public int updateLedger(LedgerBo bo) {
        Ledger ledger = MapstructUtils.convert(bo, Ledger.class);
        return baseMapper.updateById(ledger);
    }

    // 删除
    public int deleteLedgerByIds(List<Long> ids) {
        return baseMapper.deleteByIds(ids);
    }
}
```

**关键点：**
- `LambdaQueryWrapper` 用 Lambda 表达式引用字段，避免字符串硬编码
- `StringUtils.isNotBlank()` 做条件判断，实现动态 SQL
- `MapstructUtils.convert()` 做对象转换（MapStruct 编译期生成，零反射）

### 2.7 Controller 模式（REST 接口）

```java
@Validated                              // ← 启用 BO 校验
@RequiredArgsConstructor                 // ← 构造器注入
@RestController
@RequestMapping("/enterprise/ledger")    // ← 路由前缀
public class LedgerController extends BaseController {

    private final ILedgerService ledgerService;

    // ★ 查询列表（分页）
    @SaCheckPermission("business:table:ledger:list")     // ← 权限控制
    @GetMapping("/list")
    public TableDataInfo<LedgerVo> list(LedgerBo bo, PageQuery pageQuery) {
        return ledgerService.selectPageLedgerList(bo, pageQuery);
    }

    // ★ 查询详情
    @SaCheckPermission("business:table:ledger:query")
    @GetMapping("/{id}")
    public R<LedgerVo> getInfo(@PathVariable Long id) {
        return R.ok(ledgerService.selectLedgerById(id));
    }

    // ★ 新增
    @SaCheckPermission("business:table:ledger:add")
    @Log(title = "台账管理", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Void> add(@Validated @RequestBody LedgerBo bo) {
        return toAjax(ledgerService.insertLedger(bo));
    }

    // ★ 修改
    @SaCheckPermission("business:table:ledger:edit")
    @Log(title = "台账管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<Void> edit(@Validated @RequestBody LedgerBo bo) {
        return toAjax(ledgerService.updateLedger(bo));
    }

    // ★ 删除（支持批量）
    @SaCheckPermission("business:table:ledger:remove")
    @Log(title = "台账管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@PathVariable Long[] ids) {
        return toAjax(ledgerService.deleteLedgerByIds(Arrays.asList(ids)));
    }

    // ★ 导出 Excel
    @SaCheckPermission("business:table:ledger:export")
    @Log(title = "台账管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(LedgerBo bo, HttpServletResponse response) {
        List<LedgerVo> list = ledgerService.selectLedgerList(bo);
        ExcelUtil.exportExcel(list, "台账数据", LedgerVo.class, response);
    }
}
```

**REST 路由契约（全模块统一）：**

| 方法 | URL | 用途 | 权限后缀 |
|------|-----|------|----------|
| GET | `/enterprise/{module}/list` | 分页列表 | `:list` |
| GET | `/enterprise/{module}/{id}` | 查询详情 | `:query` |
| POST | `/enterprise/{module}` | 新增 | `:add` |
| PUT | `/enterprise/{module}` | 修改 | `:edit` |
| DELETE | `/enterprise/{module}/{ids}` | 删除（逗号分隔） | `:remove` |
| POST | `/enterprise/{module}/export` | 导出 | `:export` |

### 2.8 权限设计

```
business:table:{module}:list     ← 列表查询
business:table:{module}:query    ← 详情查询
business:table:{module}:add      ← 新增
business:table:{module}:edit     ← 修改
business:table:{module}:remove   ← 删除
business:table:{module}:export   ← 导出
```

权限标识存储在 `sys_menu` 表的 `perms` 字段，前端路由的 `code` 与菜单 path 对应。

---

## 3. 前端开发模式

> Vue 3 Composition API + TypeScript，每个模块 **4 个文件**。

### 3.1 文件清单

| # | 文件 | 作用 |
|---|------|------|
| 1 | `api/business/{module}/types.ts` | 类型定义（Query / VO / Form） |
| 2 | `api/business/{module}/index.ts` | API 请求函数 |
| 3 | `views/business/table/{module}/index.vue` | 表格页面（搜索+表格+弹窗） |
| 4 | `src/router/index.ts` | 路由注册 |

### 3.2 API 类型定义 (types.ts)

```typescript
// 查询参数（传给后端的）
export interface LedgerQuery {
  pageNum: number;
  pageSize: number;
  inspectedUnit?: string;
  legalPerson?: string;
  rectificationProgress?: string;
  inspectorCategory?: string;
  params?: Record<string, any>;  // ← 预留扩展
}

// 列表/详情返回的字段
export interface LedgerVO {
  id: string;
  date: string;
  inspectedUnit: string;
  // ... 与后端 Vo 一一对应
}

// 新增/修改提交的表单
export interface LedgerForm {
  id?: string;          // ← id 可选（新增时不存在）
  date: string;
  inspectedUnit: string;
  // ...
}
```

### 3.3 API 请求函数 (index.ts)

```typescript
import request from '@/utils/request';
import { LedgerQuery, LedgerForm } from './types';

export const listLedger = (query: LedgerQuery) => {
  return request({ url: '/enterprise/ledger/list', method: 'get', params: query });
};

export const getLedger = (id: string | number) => {
  return request({ url: '/enterprise/ledger/' + id, method: 'get' });
};

export const addLedger = (data: LedgerForm) => {
  return request({ url: '/enterprise/ledger', method: 'post', data });
};

export const updateLedger = (data: LedgerForm) => {
  return request({ url: '/enterprise/ledger', method: 'put', data });
};

export const delLedger = (ids: string) => {
  return request({ url: '/enterprise/ledger/' + ids, method: 'delete' });
};

export const exportLedger = (query: LedgerQuery) => {
  return request({
    url: '/enterprise/ledger/export',
    method: 'post',
    params: query,
    responseType: 'blob'   // ← 导出需要 blob 类型
  });
};
```

### 3.4 表格页面模式 (index.vue)

页面结构固定为三段式：

```
┌─────────────────────────────────┐
│ 搜索区域 (v-show="showSearch")    │
│ [输入框] [下拉框] [搜索] [重置]    │
└─────────────────────────────────┘
┌─────────────────────────────────┐
│ 工具栏 [新增] [修改] [删除] [导出] │
├─────────────────────────────────┤
│ 表格 (el-table)                  │
│  ├── 序号 / 日期 / 名称 / ...    │
│  └── 操作列 [编辑] [删除]         │
├─────────────────────────────────┤
│ 分页器                           │
└─────────────────────────────────┘
┌─────────────────────────────────┐
│ 弹窗 (el-dialog)                 │
│ [表单字段]                       │
│ [取消] [确定]                    │
└─────────────────────────────────┘
```

**script setup 标准模板（压缩版但完整）：**

```typescript
import { ref, reactive } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { listLedger, getLedger, addLedger, updateLedger, delLedger, exportLedger } from '@/api/business/ledger/index';

// ===== 状态 =====
const loading = ref(false);
const submitLoading = ref(false);
const single = ref(true);       // 是否未选中单行
const multiple = ref(true);     // 是否未多选
const total = ref(0);
const ids = ref<string[]>([]);
const tableData = ref<any[]>([]);
const dialogVisible = ref(false);
const dialogTitle = ref('');

// ===== 查询参数（reactive 响应式）=====
const queryParams = reactive({
  pageNum: 1, pageSize: 10,
  inspectedUnit: '', legalPerson: '', rectificationProgress: '', inspectorCategory: ''
});

// ===== 表单（★ initForm + Object.assign 模式）=====
const initForm: any = {
  date: '', inspectionDate: '', inspectedUnit: '', legalPerson: '',
  // ... 所有字段带默认值
};
const form = reactive({ ...initForm });

// ===== 表单校验规则 =====
const rules = {
  inspectedUnit: [{ required: true, message: '请输入被检查单位', trigger: 'blur' }]
};

// ===== 核心方法 =====
// 查询列表
const getList = async () => {
  loading.value = true;
  try {
    const res = await listLedger(queryParams);
    tableData.value = res.rows || [];    // ← 列表数据在 res.rows
    total.value = res.total || 0;
  } finally { loading.value = false; }
};

// 搜索 / 重置
const handleQuery = () => { queryParams.pageNum = 1; getList(); };
const resetQuery = () => {
  Object.assign(queryParams, { pageNum: 1, pageSize: 10, inspectedUnit: '', ... });
  handleQuery();
};

// 表格多选
const handleSelectionChange = (sel: any[]) => {
  ids.value = sel.map((i: any) => i.id);
  single.value = sel.length !== 1;
  multiple.value = !sel.length;
};

// ★ 新增（关键：id 必须设为 undefined）
const handleAdd = () => {
  Object.assign(form, { ...initForm, id: undefined });  // ← 不写这行会出大事
  dialogTitle.value = '新增台账';
  dialogVisible.value = true;
};

// ★ 修改（关键：先请求详情再用 Object.assign 填充表单）
const handleUpdate = async (row?: any) => {
  const id = row?.id || (ids.value.length === 1 ? ids.value[0] : null);
  if (!id) return;
  const res = await getLedger(id);
  Object.assign(form, res.data);     // ← 详情在 res.data（不是 res.rows!）
  dialogTitle.value = '修改台账';
  dialogVisible.value = true;
};

// 删除
const handleDelete = (row?: any) => {
  const deleteIds = row?.id ? row.id : ids.value.join(',');
  ElMessageBox.confirm('确认删除？', '提示', { type: 'warning' })
    .then(async () => { await delLedger(deleteIds); ElMessage.success('删除成功'); getList(); });
};

// 导出
const handleExport = async () => { await exportLedger(queryParams); ElMessage.success('导出成功'); };

// ★ 提交（先验证表单，再判断新增/修改）
const submitForm = async () => {
  try { await formRef.value.validate(); } catch { return; }  // 验证不通过直接返回
  submitLoading.value = true;
  try {
    await (form.id ? updateLedger(form) : addLedger(form));   // 有 id → 修改；无 id → 新增
    ElMessage.success(form.id ? '修改成功' : '新增成功');
    dialogVisible.value = false;
    getList();
  } finally { submitLoading.value = false; }
};

// 初始化
getList();
```

### 3.5 API 响应结构差异（重要！）

| 接口类型 | 响应结构 | 数据获取方式 |
|----------|----------|-------------|
| 分页列表 (`/list`) | `{ code, msg, rows, total }` | `res.rows`, `res.total` |
| 详情 (`/{id}`) | `{ code, msg, data }` | `res.data` |
| 新增/修改/删除 | `{ code, msg }` | 无数据字段 |

---

## 4. 数据库设计规范

### 4.1 建表示例

```sql
CREATE TABLE `biz_ledger` (
  `id` bigint NOT NULL,
  `date` varchar(50) DEFAULT '' COMMENT '日期',
  `inspected_unit` varchar(200) DEFAULT '' COMMENT '被检查单位',
  -- ... 其他字段 ...
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新人',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志(0正常/1删除)',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

### 4.2 必须遵守的约定

| 约定 | 说明 |
|------|------|
| 主键类型 | `bigint`，不用自增，Java 层用 `IdType.ASSIGN_ID`（雪花算法） |
| 必备字段 | `create_time`, `create_by`, `update_time`, `update_by`, `del_flag`, `tenant_id` |
| 命名规范 | 数据库用下划线 `inspected_unit`，Java 用驼峰 `inspectedUnit` |
| 逻辑删除 | `del_flag = '0'` 正常，`del_flag = '1'` 已删除（MyBatis-Plus 自动拦截） |
| 多租户列 | **每个表必须有 `tenant_id`**，除非在配置中排除 |
| 表名前缀 | 业务表统一 `biz_` 前缀（如 `biz_ledger`） |

### 4.3 枚举值设计模式

不使用字典表外键，直接在字段上用 `varchar` 存编码值，前端映射显示：

```sql
`rectification_progress` varchar(10) DEFAULT '0' COMMENT '整改进度(0未开始/1进行中/2已完成)'
`severity` varchar(10) DEFAULT '0' COMMENT '严重程度(0严重/1一般/2轻微)'
`status` varchar(10) DEFAULT '0' COMMENT '状态(0启用/1停用)'
```

前端用业务字典（BizDict）或硬编码 options 映射：

```typescript
const rectificationProgressOptions = [
  { label: '未开始', value: '0', tagType: 'info' },
  { label: '进行中', value: '1', tagType: 'warning' },
  { label: '已完成', value: '2', tagType: 'success' },
];
```

---

## 5. 输出表单开发

输出表单是项目中最复杂的部分——将数据库中的数据渲染成可打印的 PDF/纸质文书。

### 5.1 核心特征

| 特征 | 说明 |
|------|------|
| 数据来源 | 通过弹窗选择已有记录（如选择一条检查记录），自动填充到表单 |
| 布局方式 | 纯 HTML table 布局（模拟纸质文书），非 Element Plus 组件 |
| 打印支持 | `window.print()` + `@media print` CSS 控制打印样式 |
| 独立存储 | 表单数据存为独立表（如 `biz_inspection_form`），与源记录解耦 |

### 5.2 已开发的 8 个输出表单

| 表单 | 文件 | 来源模块 |
|------|------|----------|
| 检查记录表 | `inspection-form.vue` | 检查记录（Inspection） |
| 现场笔录 | `scene-notes-form.vue` | 现场笔录（SceneNotes） |
| 询问笔录 | `inquiry-notes-form.vue` | 询问笔录（InquiryNotes） |
| 场所设施财物清单 | `facility-checklist-form.vue` | 设施清单（FacilityChecklist） |
| 设备检查记录 | `equipment-check-record-form.vue` | 设备检查记录 |
| 设备安全指令 | `equipment-safety-directive-form.vue` | 设备安全指令 |
| 执法决定书 | `enforcement-decision-form.vue` | 执法决定书 |
| 服务地址确认 | `service-address-confirmation-form.vue` | 服务地址确认 |

### 5.3 表单页面结构

```
┌─────────────────────────────────┐
│ 操作栏                          │
│ [📋 选择记录] [🖨️ 打印] [🗑️ 删除] │
└─────────────────────────────────┘
┌─────────────────────────────────┐
│ 打印区域 (id="printArea")        │
│ ┌─────────────────────────────┐ │
│ │ 汾阳市市场监督管理局          │ │
│ │ 检查记录表                    │ │
│ │                              │ │
│ │ ┌──┬──────────┬──┬─────────┐│ │
│ │ │名│ 被检查单位 │电│ 联系方式 ││ │
│ │ │称│           │话│         ││ │
│ │ ├──┼──────────┼──┼─────────┤│ │
│ │ │...│         │..│         ││ │
│ │ └──┴──────────┴──┴─────────┘│ │
│ │                              │ │
│ │ 检查人员：______  记录人员：___ │ │
│ │ 被检查单位（签章） 年  月  日  │ │
│ └─────────────────────────────┘ │
└─────────────────────────────────┘
┌─────────────────────────────────┐
│ 弹窗：选择检查记录               │
│ [表格 + 双击选中]                │
└─────────────────────────────────┘
```

### 5.4 打印 CSS 关键模式

```css
/* 屏幕显示 */
.print-area { background: #f0f0f0; padding: 20px; }
.page { width: 210mm; margin: 0 auto; background: white; padding: 15mm; }

/* 打印时隐藏操作栏 */
@media print {
  .noprint { display: none !important; }
  .page { box-shadow: none; margin: 0; padding: 0; }
  @page { size: A4; margin: 0; }
}
```

### 5.5 选择记录 → 填充表单的 JS 模式

```typescript
// 双击表格行 → 根据 id 请求详情 → 填充到表单
const selectRecord = async (row: any) => {
  const res = await getInspection(row.id);
  const data = res.data;
  // 映射字段（可能名称不完全一致）
  formData.value = {
    id: data.id,
    inspectedUnit: data.inspectedUnit,
    legalPerson: data.legalPerson,
    // ... 逐个映射
  };
  selectVisible.value = false;
  ElMessage.success('已加载检查记录');
};
```

---

## 6. 业务字典模块（BizDict）

> 将所有下拉选项从硬编码迁移到数据库管理，实现动态下拉。

### 6.1 为什么要做 BizDict

开发初期，下拉选项直接硬编码在 Vue 文件中：
```typescript
// ❌ 旧方式：硬编码，改一个选项要改代码 + 重新部署
const options = [{ label: '未开始', value: '0' }, { label: '进行中', value: '1' }];
```

迁移到 BizDict 后：
```typescript
// ✅ 新方式：从数据库加载，后台管理页面直接改
import { getBizDictByType } from '@/api/business/dict/index';
const res = await getBizDictByType('rectification_progress');
options.value = res.data;  // [{ label: '未开始', value: '0', tagType: 'info' }, ...]
```

### 6.2 BizDict 数据库设计

```sql
CREATE TABLE `biz_dict` (
  `id` bigint NOT NULL,
  `dict_type` varchar(100) NOT NULL COMMENT '字典类型（如 rectification_progress）',
  `label` varchar(100) NOT NULL COMMENT '选项标签（如 未开始）',
  `value` varchar(100) NOT NULL COMMENT '选项值（如 0）',
  `sort` int DEFAULT 0 COMMENT '排序',
  `tag_type` varchar(20) DEFAULT '' COMMENT 'Element Plus Tag 类型（info/success/warning/danger）',
  `status` char(1) DEFAULT '0' COMMENT '状态（0启用/1停用）',
  `remark` varchar(500) DEFAULT '',
  -- ... BaseEntity 字段
  PRIMARY KEY (`id`)
);
```

### 6.3 前端调用模式

```typescript
// 在 onMounted 中加载字典数据
import { getBizDictByType } from '@/api/business/dict/index';

onMounted(() => {
  // 同时加载多个字典
  getBizDictByType('rectification_progress').then(res => {
    rectificationProgressOptions.value = res.data || [];
  });
  getBizDictByType('issue_severity').then(res => {
    severityOptions.value = res.data || [];
  });
});

// 辅助函数：根据 value 获取 label / tagType
const getDictLabel = (options: any[], value: string) =>
  options.find(o => o.value === value)?.label ?? value;

const getDictTagType = (options: any[], value: string) =>
  options.find(o => o.value === value)?.tagType ?? '';
```

### 6.4 BizDict 与常规 CRUD 的区别

BizDict 模块多了一个无需权限的接口，供其他页面调用：

```java
// 无需权限认证，所有登录用户均可访问
@GetMapping("/all")
public R<List<BizDictVo>> all(BizDictBo bo) {
    return R.ok(bizDictService.selectBizDictList(bo));
}
```

前端 API 设计成按类型查询的工具函数：

```typescript
// 按字典类型获取选项列表（不需要分页）
export const getBizDictByType = (dictType: string) => {
  return request({
    url: '/enterprise/dict/all',
    method: 'get',
    params: { dictType, status: '0' }  // 只查启用的
  });
};
```

---

## 7. 跨模块通信

### 7.1 方式一：路由 Query 参数传递

商户页面 → 创建检查，通过 URL 传参：

```typescript
// 商户页：点击"创建检查"
router.push({ path: '/table/inspection', query: { merchantId: row.id } });

// 检查页：接收参数
watch(() => route.query, (query) => {
  if (query.merchantId) {
    // 自动填充商户信息
    loadMerchantInfo(query.merchantId);
  }
}, { immediate: true });
```

### 7.2 方式二：弹窗选择

输出表单页面 → 弹出表格 → 双击选中 → 回填数据：

```typescript
// 打开选择弹窗
const handleSelect = () => { selectVisible.value = true; getList(); };

// 双击选中
const selectRecord = async (row: any) => {
  const res = await getDetail(row.id);
  Object.assign(formData, res.data);
  selectVisible.value = false;
};
```

### 7.3 注意事项

- **必须用完整路径**：`{ path: '/table/inspection' }` 而不是 `{ path: '/inspection' }`，因为动态菜单路由是拼出来的
- **watch 中要 `immediate: true`**：组件挂载时路由参数可能已经存在
- **弹窗内用 `nextTick`**：等 DOM 渲染完再操作

---

## 8. 开发避坑指南

### ⚠️ 坑1：多租户插件（最常见的新手错误）

**现象：** 新建表后 INSERT/SELECT 报 `Unknown column 'tenant_id'`

**原因：** RuoYi-Vue-Plus 默认开启多租户拦截器，所有 SQL 自动注入 `WHERE tenant_id = ?`

**解决：** 两个方式二选一：
- 方式A（推荐）：建表时加上 `tenant_id` 列（字段名为 `tenant_id`，类型 `bigint`）
- 方式B：在 `application.yml` 的 `tenant.excludes` 中添加表名

### ⚠️ 坑2：Maven 启动方式

**现象：** 从根目录 `mvn spring-boot:run` 报错 `No plugin found`

**解决：**
```bash
# ✅ 正确：必须从 ruoyi-admin 目录启动
cd ruoyi-admin && mvn spring-boot:run -DskipTests

# ✅ 正确：新建模块后必须 install
mvn install -DskipTests -pl ruoyi-modules/ruoyi-enterprise -am
```

### ⚠️ 坑3：包路径必须用 org.dromara

**现象：** 新模块的 Controller 无法被扫描到

**原因：** 启动类在 `org.dromara` 包下，Spring Boot 只扫描此包及子包

**解决：** 所有代码包路径必须为 `org.dromara.enterprise.*`

### ⚠️ 坑4：列表 vs 详情响应结构不同

**现象：** `res.data` 在列表页返回 undefined

**原因：** 列表 API 返回 `{ rows, total, code, msg }`（数据在顶层），详情 API 返回 `{ data: {...}, code, msg }`（数据在 data 字段内）

**解决：**
```typescript
// ✅ 列表 → res.rows
const res = await listLedger(queryParams);
tableData.value = res.rows;

// ✅ 详情 → res.data
const res = await getLedger(id);
form = res.data;
```

### ⚠️ 坑5：initForm 的 id 必须为 undefined

**现象：** 新增操作变成了修改（或后端报 id 解析错误）

**原因：** 上次编辑后 `form.id` 残留了值，且 `{ ...initForm, id: '' }` 序列化为 `id: ""`，后端解析失败

**解决：**
```typescript
// ❌ 错误
Object.assign(form, { ...initForm, id: '' });

// ✅ 正确
Object.assign(form, { ...initForm, id: undefined });
```

### ⚠️ 坑6：跨模块路由跳转用完整路径

**现象：** `router.push('/inspection')` 跳转到空白页

**原因：** Vue Router 中实际路径是 `/table/inspection`（动态菜单拼接）

**解决：**
```typescript
// ❌ 错误
router.push({ path: '/inspection', query: { id: row.id } });

// ✅ 正确
router.push({ path: '/table/inspection', query: { id: row.id } });
```

### ⚠️ 坑7：首页快捷入口不能指向父菜单

**现象：** 点击首页快捷卡片后页面空白

**原因：** 父菜单路径的 component 为 NULL，导航过去无组件渲染

**解决：** shortcuts 的 path 必须指向叶子页面，如 `/table/ledger` 而非 `/table`

### ⚠️ 坑8：多租户环境下的查询异常

**现象：** 某些查询条件不生效，返回空结果

**原因：** MyBatis-Plus 多租户插件自动注入 `tenant_id` 条件

**解决：** 如果确实需要跨租户查询，在 Mapper 方法上加 `@InterceptorIgnore(tenantLine = "true")`

---

## 9. 开发工作流

### 9.1 新模块开发步骤

```
Step 1: 数据库
   ├─ 创建 biz_xxx 表（含六大必备字段）
   └─ 如有默认枚举值，建表后 INSERT 初始数据

Step 2: 后端 (5 个文件)
   ├─ domain/Xxx.java          → 实体类
   ├─ domain/bo/XxxBo.java     → 请求体（含校验注解）
   ├─ domain/vo/XxxVo.java     → 响应体（含导出注解）
   ├─ mapper/XxxMapper.java    → Mapper（继承 BaseMapperPlus）
   ├─ service/IXxxService.java → 接口
   ├─ service/impl/            → 实现（LambdaQueryWrapper + MapstructUtils）
   └─ controller/XxxController → REST 接口

Step 3: 后端编译并安装
   └─ mvn install -DskipTests -pl ruoyi-modules/ruoyi-enterprise -am

Step 4: 前端 (4 个文件)
   ├─ api/business/xxx/types.ts  → 类型定义
   ├─ api/business/xxx/index.ts  → API 请求
   └─ views/business/table/xxx/index.vue → 表格页面

Step 5: 菜单配置
   ├─ sys_menu 表插入父菜单（如果有）
   └─ sys_menu 表插入子菜单（path + perms + component）

Step 6: 启动验证
   ├─ 启动后端：cd ruoyi-admin && mvn spring-boot:run
   ├─ 启动前端：cd plus-ui && npm run dev
   └─ 登录 → 系统管理 → 角色管理 → 分配权限 → 刷新页面
```

### 9.2 常用命令速查

```bash
# === 后端 ===
# 编译安装业务模块（改代码后必须执行）
cd /d/RuoYi-Vue-Plus-5.X
mvn install -DskipTests -pl ruoyi-modules/ruoyi-enterprise -am

# 启动后端（从 ruoyi-admin 目录）
cd ruoyi-admin
mvn spring-boot:run -DskipTests

# === 前端 ===
cd /d/RuoYi-Vue-Plus-5.X/plus-ui
npm run dev          # 启动开发服务器（默认 http://localhost:5173）

# === 数据库 ===
# 新表 SQL → script/sql/update/ 目录
# 初始数据 SQL → 同目录
```

### 9.3 关键配置文件位置

| 配置 | 文件路径 |
|------|----------|
| 数据库连接 | `ruoyi-admin/src/main/resources/application.yml` |
| 多租户排除表 | 同上文件的 `tenant.excludes` |
| Redis 配置 | `ruoyi-admin/src/main/resources/application.yml` |
| 前端代理 | `plus-ui/vite.config.ts` |
| 前端路由 | `plus-ui/src/router/index.ts` |

---

## 10. 模块全景图

### 10.1 已开发的 14 个业务模块

| # | 模块 | 后端 Controller | 前端页面 | 数据库表 | 输出表单 |
|---|------|:---:|:---:|:---:|:---:|
| 1 | 台账 | LedgerController | table/ledger | biz_ledger | — |
| 2 | 检查记录 | InspectionController | table/inspection | biz_inspection | inspection-form |
| 3 | 问题清单 | IssueController | table/issue | biz_issue | — |
| 4 | 签名档案 | SignatureController | table/signature | biz_signature | — |
| 5 | 商户信息 | MerchantController | table/merchant | biz_merchant | — |
| 6 | 现场笔录 | SceneNotesController | table/scene-notes | biz_scene_notes | scene-notes-form |
| 7 | 询问笔录 | InquiryNotesController | table/inquiry-notes | biz_inquiry_notes | inquiry-notes-form |
| 8 | 检察人员 | InspectorController | table/inspector | biz_inspector | — |
| 9 | 业务字典 | BizDictController | table/dict | biz_dict | — |
| 10 | 设施清单 | FacilityChecklistController | table/facility-checklist | biz_facility_checklist | facility-checklist-form |
| 11 | 设备检查记录 | EquipmentCheckRecordController | table/equipment-check-record | biz_equipment_check | equipment-check-record-form |
| 12 | 执法决定书 | EnforcementDecisionController | table/enforcement-decision | biz_enforcement_decision | enforcement-decision-form |
| 13 | 设备安全指令 | EquipmentSafetyDirectiveController | table/equipment-safety-directive | biz_equipment_safety | equipment-safety-directive-form |
| 14 | 服务地址确认 | ServiceAddressConfirmationController | table/service-address-confirmation | biz_service_address | service-address-confirmation-form |

### 10.2 模块间数据流向

```
                    ┌─────────────────┐
                    │   BizDict 字典   │
                    │ (动态下拉选项)    │
                    └───┬───┬───┬─────┘
                        │   │   │
        ┌───────────────┘   │   └───────────────┐
        ↓                   ↓                   ↓
┌──────────────┐  ┌──────────────┐  ┌──────────────┐
│   Merchant   │  │  Inspector   │  │   Ledger     │
│   (商户)     │  │  (检察人员)   │  │   (台账)     │
└──────┬───────┘  └──────┬───────┘  └──────┬───────┘
       │                 │                 │
       │  通过 merchantId│关联               │ 被检查主体
       ↓                 ↓                 ↓
┌──────────────────────────────────────────────────┐
│                  Inspection (检查记录)            │
│  核心枢纽——连接所有模块                           │
└──┬────────┬────────┬────────┬────────┬──────────┘
   │        │        │        │        │
   ↓        ↓        ↓        ↓        ↓
┌──────┐┌──────┐┌──────┐┌──────┐┌──────────┐
│Issue ││Scene ││Inquiry││Signa││Facility  │
│(问题)││(现场)││(询问) ││(签名)││Checklist │
└──────┘└──────┘└──────┘└──────┘└──────────┘
   │        │        │        │        │
   └────────┴────────┴────────┴────────┘
                    │
                    ↓
        ┌───────────────────┐
        │   8 个输出表单     │
        │ (PDF/打印/归档)    │
        └───────────────────┘
```

### 10.3 代码统计

| 指标 | 数量 |
|------|------|
| 后端 Controller | 14 个 |
| 后端 Java 文件 | ~70 个 |
| 前端表格页面 | 15 个（含 biz_dict + index） |
| 前端输出表单 | 8 个 |
| 前端 API 模块 | 14 个（各 2 个文件） |
| 总提交文件数 | 1342 个 |
| 总代码行数 | ~123,000 行 |

---

## 附录：写给未来的自己

1. **一个新模块 = 半小时**：熟练掌握模式后，新建一个完整 CRUD 模块只需 30 分钟
2. **先跑通再优化**：骨架代码→编译通过→接口调通→完善细节，不要一步到位
3. **BizDict 要作为基础设施**：所有固定下拉选项都应该迁移到业务字典
4. **输出表单是从零手写的**：没有模板引擎，纯 Vue + CSS 模拟纸质文书
5. **多租户是全局生效的**：任何新表都要考虑 tenant_id，除非在配置中排除
6. **GitHub 上的代码就是简历**：这个项目展示了你独立完成企业级全栈系统的能力

---

> 📅 开发周期：2026年5月27日 — 6月4日  
> 👤 开发者：lyb18634784808  
> 🔗 Gitee: https://gitee.com/lyb18634784808/ruoyi-vue-plus  
> 🔗 GitHub: https://github.com/18634784808/-ruoyi-
