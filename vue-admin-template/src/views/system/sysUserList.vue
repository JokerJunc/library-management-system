<template>
  <el-main>
    <!-- 搜索栏   element ui的标签，就当做普通的div就可以，只是他添加了一些属性
    :model:  form表单绑定的数据，通常是一个对象
    :rules: form表单验证的规则
    ref :  类似div的id，是唯一的
    label-width：表单文字标签的宽度
    :inline ： 是否同一行显示
    size: 表单内组件的大小
     -->
    <el-form :model="listParm" ref="searchRef" label-width="80px" :inline="true" size="small">
      <el-form-item>
        <el-input placeholder="请输入姓名" v-model="listParm.nickName"></el-input>
      </el-form-item>
      <el-form-item>
        <el-input placeholder="请输入电话" v-model="listParm.phone"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button icon="el-icon-search" @click="searchBtn">搜索</el-button>
        <el-button style="color: #ff7670" icon="el-icon-close" @click="resetBtn">重置</el-button>
        <el-button type="primary" @click="addBtn" icon="el-icon-plus">新增</el-button>
      </el-form-item>
    </el-form>
    <!-- 表格 -->
    <el-table :height='tableHeight' :data="tableData" border stripe>
      <el-table-column prop="nickName" label="姓名"></el-table-column>
      <el-table-column prop="phone" label="电话"></el-table-column>
      <el-table-column prop="email" label="邮箱"></el-table-column>
      <el-table-column label="操作" align="center" width="180">
        <template slot-scope="scope">
          <el-button type="primary" size="small" icon="el-icon-edit" @click="editBtn(scope.row)">编辑</el-button>
          <el-button type="danger" size="small" icon="el-icon-delete" @click="deleteBtn(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页 -->
    <el-pagination @size-change="sizeChange" @current-change="currentChange" :current-page.sync="listParm.currentPage"
      :page-sizes="[10, 20, 40, 80, 100]" :page-size="listParm.pageSize" layout="total, sizes, prev, pager, next, jumper"
      :total="listParm.total" background>
    </el-pagination>
    <!-- 弹框 -->
    <sys-dialog :title="addDialog.title" :visible="addDialog.visible" :width="addDialog.width" :height="addDialog.height"
      @onClose="onColse" @onConfirm="onConfirm">
      <div slot="content">

        <el-form :model="addModel" ref="addRef" :rules="rules" label-width="80px" :inline="false" size="small">
          <el-row>
            <el-col :span="12">
              <el-form-item prop="nickName" label="姓名">
                <el-input v-model="addModel.nickName"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item prop="phone" label="电话">
                <el-input v-model="addModel.phone"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item prop="email" label="邮箱">
                <el-input v-model="addModel.email"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item prop="sex" label="性别">
                <el-radio-group v-model="addModel.sex" @change="">
                  <el-radio :label="'1'">男</el-radio>
                  <el-radio :label="'0'">女</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item prop="username" label="账户">
                <el-input v-model="addModel.username"></el-input>
              </el-form-item>
            </el-col>
            <el-col v-if="addModel.type == 0" :span="12">
              <el-form-item prop="password" label="密码">
                <el-input v-model="addModel.password"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </div>
    </sys-dialog>
  </el-main>
</template>

<script>
import SysDialog from "@/components/system/SysDialog";
import { getListApi, addUserApi, editUserApi, deleteUserApi } from "@/api/user";
export default {
  components: {
    SysDialog
  },
  data() {
    return {
      //表单验证规则
      rules: {
        nickName: [
          { required: true, message: "请输入姓名", trigger: "blur" },
          // { min: 2, max: 10, message: "长度在 2 到 10 个字符", trigger: "blur" }
        ],
        phone: [
          { required: true, message: "请输入手机号", trigger: "blur" },
          // {
          //   pattern: /^1[34578]\d{9}$/,
          //   message: "手机号格式不正确",
          //   trigger: "blur"
          // }
        ],
        sex: [
          { required: true, message: "请选择性别", trigger: "change" }
        ],
        username: [
          { required: true, message: "请输入账户", trigger: "blur" }
        ],
        password: [
          { required: true, message: "请输入密码", trigger: "blur" },
        ]
      },
      //弹框的表单数据
      addModel: {
        type: '',//0 新增，1 编辑
        userId: "",
        nickName: "",
        phone: "",
        email: "",
        sex: "",
        username: "",
        password: ""
      },
      //表格高度
      tableHeight: 0,
      //列表查询的参数
      listParm: {
        phone: "",
        nickName: "",
        currentPage: 1,
        pageSize: 10,
        total: 0
      },
      //表格的数据
      tableData: [],
      addDialog: {
        title: "新增用",
        visible: false,
        width: 600,
        height: 250,
      },

    };

  },
  created() {
    this.getList();
  },
  methods: {
    //获取列表
    async getList() {
      let res = await getListApi(this.listParm);
      // console.log('成功');
      // console.log(res);
      if (res.code == 200) {
        this.tableData = res.data.records;
        this.listParm.total = res.data.total;
      }
    },
    //弹框确认事件
    onConfirm() {
      this.$refs.addRef.validate(async (valid) => {
        if (valid) {
          let res = null
          if (this.addModel.type == 0) {
            res = await addUserApi(this.addModel);
          } else {
            // console.log('编辑');
            res = await editUserApi(this.addModel);
          }
          if (res && res.code == 200) {
            this.$message({ type: "success", message: res.msg })
            this.getList();
          }
          this.addDialog.visible = false;
          // alert("提交成功");
        }
      })
    },
    //清空搜索条件按钮
    resetBtn() {
      this.listParm.nickName = "";
      this.listParm.phone = "";
      this.getList();
    },
    //搜索按钮
    searchBtn() {
      this.getList();

    },
    //新增按钮
    async addBtn() {
      // 设置为新增
      this.addModel.type = 0;
      this.addDialog.title = "新增用户";
      this.addDialog.visible = true;

    },
    //编辑按钮
    editBtn(row) {
      //设置为编辑
      this.addDialog.title = "编辑用户";
      this.addDialog.visible = true;
      this.$resetForm('addRef', this.addModel);
      // console.log(row);
      this.$objCopy(row, this.addModel)
      // console.log(row, this.addModel);
      this.addModel.type = 1;
    },
    //删除按钮
    async deleteBtn(row) {
      // console.log(row);
      let confirm = await this.$myconfirm("你确定要删除吗？")
      if (confirm) {
        // console.log(userId)
        let res = await deleteUserApi({ userId: row.userId });
        if (res && res.code == 200) {
          this.$message({ type: "success", message: res.msg })
          this.getList();
        }
      }

      // console.log(res);
    },
    //页容量改变触发
    sizeChange(val) {
      this.listParm.pageSize = val;
      this.getList();
    },
    //页码改变触发
    currentChange(val) {
      this.listParm.currentPage = val;
      this.getList();
    },
    //弹框取消事件
    onColse() {
      this.addDialog.visible = false;
    },

  },
  mounted() {
    this.$nextTick(() => {
      this.tableHeight = window.innerHeight - 220
    })
  }
};
</script>

<style lang="scss" scoped></style>
