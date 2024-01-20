<template>
  <el-main>
    <el-form :model="listParm" label-width="80px" :inline="true" size="small">
      <el-form-item>
        <el-input v-model="listParm.roleName" placeholder="请输入角色名称"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button icon="el-icon-search" @click="searchBtn">搜索</el-button>
        <el-button icon="el-icon-refresh" style="color: red;" @click="resetBtn">重置</el-button>
        <el-button type="primary" icon="el-icon-plus" @click="addBtn">新增</el-button>
      </el-form-item>
    </el-form>
    <el-table :height="tableHeight" :data="tableData" border stripe>
      <el-table-column prop="roleName" label="角色名称"></el-table-column>
      <el-table-column prop="roleType" label="角色类型">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.roleType == 1" type="success">系统角色</el-tag>
          <el-tag v-else type="warning">读者角色</el-tag>
        </template>
      </el-table-column>
      
      <el-table-column prop="remark" label="备注"></el-table-column>
      <el-table-column label="操作" align="center" width="180px">
        <template slot-scope="scope">
          <el-button type="primary" size="small" icon="el-icon-edit" @click="editBtn(scope.row)">编辑</el-button>
          <el-button type="danger" size="small" icon="el-icon-delete" @click="deleteBtn(scope.row)">删除</el-button>
        </template>
      </el-table-column>

    </el-table>
    <el-pagination  @size-change="sizeChange" 
                    @current-change="currentChange"
                    :current-page.sync="listParm.currentPage"
                    :page-sizes="[1, 5, 10, 40]" 
                    :page-size="listParm.pageSize"
      layout="total, sizes, prev, pager, next, jumper" :total="listParm.total" background>
    </el-pagination>

    <sys-dialog :title="addDialog.title" :visible="addDialog.visible" :width="addDialog.width" :height="addDialog.height"
      @onClose="onColse" @onConfirm="onConfirm">
      <div slot="content">
        <el-form :model="addModel" ref="addRef" :rules="rules" label-width="80px" :inline="false" size="small">
          <el-row>
            <el-col :span="12">
              <el-form-item label="角色名称">
                <el-input v-model="addModel.roleName"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="角色类型">
                <el-select v-model="addModel.roleType" placeholder="请选择">
                  <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="24">
              <el-form-item label="角色备注">
                <el-input v-model="addModel.remark"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </div>
    </sys-dialog>
  </el-main>
</template>

<script>
import SysDialog from '@/components/system/SysDialog.vue'
import { getListApi,addRoleApi,editRoleApi,deleteRoleApi } from '@/api/role'
export default {
  components: {
    SysDialog
  },
  data() {
    return {
      //角色类型
      options: [
        {
          value: "1",
          label: "系统用户",
        },
        {
          value: "2",
          label: "学生",
        },
        {
          value: "3",
          label: "教师",
        },
      ],
      //表格数据
      tableData: [],
      tableHeight: 0,
      //列表查询参数
      listParm: {
        roleName: '',
        remark: '',
        currentPage: 1,
        pageSize: 10,
        total: 0,
      },
      //弹窗数据
      addDialog: {
        title: '',
        visible: false,
        // width: 600,
        height: 150,
      },
      //新增表单数据
      addModel: {
        type: '',//0新增 1修改
        roleId: '',
        roleName: '',
        roleType: '',
        remark: '',
      },
      rules: {
        roleName: [
          { required: true, message: '请输入角色名称', trigger: 'blur' },
          { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' },
        ],
        roleType: [
          { required: true, message: '请选择角色类型', trigger: 'change' },
        ],
        remark: [
          { required: true, message: '请输入角色备注', trigger: 'blur' },
          { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' },
        ],
      }
    }
  },
  methods: {
    onColse() {
      this.addDialog.visible = false
    },
    //联系
    onConfirm() {
      this.$refs.addRef.validate(async (valid) => {
        if (valid) {
          let res = null
          if (this.addModel.type == 0) {
            res = await addRoleApi(this.addModel)
          } else {
            res = await editRoleApi(this.addModel)
          }
          // console.log(res)
          if (res && res.code === 200) {
            this.$message({type: 'success',message: res.msg})
            this.getList()
          }
          this.addDialog.visible = false
        }
      })

    },
    searchBtn() {
      this.getList()
    },
    //清空搜索条件
    resetBtn() {
      this.listParm.roleName = ''
      this.getList()
    },
    //新增数据
    addBtn() {
      this.addDialog.title = '新增角色'
      this.addDialog.visible = true
      this.addModel.type = 0
    },
    //编辑数据
    editBtn(row) {
      this.addDialog.title = '编辑角色'
      this.addDialog.visible = true
      this.$resetForm('addRef',this.addModel)
      // console.log(row)
      this.$objCopy(row, this.addModel)
      this.addModel.type = 1
    },
    //删除数据
    async deleteBtn(row){
      const confirm = await this.$myconfirm('确定删除该角色吗？')
      if(confirm){
          let res = await deleteRoleApi({roleId:row.roleId})
          if(res && res.code === 200){
              this.$message({type:'success',message: res.msg})
              this.getList()
          }
      }
    },
    // 获取角色列表数据
    async getList() {
      let res = await getListApi(this.listParm)
      // console.log(res)
      if (res.code === 200) {
        this.tableData = res.data.records
        this.listParm.total = res.data.total
      }
    },
    sizeChange(val) {
      this.listParm.pageSize = val
      this.getList()

    },
    currentChange(val) {
      this.listParm.currentPage = val
      this.getList()
    },
  },
  created() {
    this.getList()
  },
  mounted() {
    this.$nextTick(() => {
      this.tableHeight = window.innerHeight - 220
    })
  },


}
</script>

<style lang="scss" scoped></style>
