<template>
  <el-main height="100%">
    <!-- Main content -->
    <el-form :model="listParm" label-width="80px" :inline="true" size="small">
      <el-form-item>
        <el-input v-model="listParm.categoryName" placeholder="请输入分类名称"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button icon="el-icon-search" @click="searchBtn">搜素</el-button>
        <el-button icon="el-icon-close"  style="color: #ff7670"  @click="resetBtn">重置</el-button>
        <el-button type="primary" icon="el-icon-plus" @click="addBtn"
          >新增</el-button
        >
      </el-form-item>
    </el-form>
    <el-table :height="tableHeight" :data="tableData" border stripe>
      <el-table-column prop="categoryName" label="分类名称"></el-table-column>
      <el-table-column prop="orderNum" label="序号"></el-table-column>
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button
            type="primary"
            size="small"
            @click="editBtn(scope.row)"
          >编辑</el-button>
          <el-button
            type="danger"
            size="small"
            @click="deleteBtn(scope.row)"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      @size-change="sizeChange"
      @current-change="currentChange"
      :current-page.sync="listParm.currentPage"
      :page-sizes="[10,20, 40, 80, 100]"
      :page-size="listParm.pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="listParm.total" background>
    </el-pagination>
    <!-- 弹框 -->
    <sys-dialog
      :title="dialog.title"
      :visible="dialog.visible"
      :width="dialog.width"
      :height="dialog.height"
      @onClose="onClose"
      @onConfirm="onConfirm"
      >
      <div slot="content">
        <el-form :model="addModel" ref="addRef" :rules="rules" label-width="80px" :inline="true" size="normal">
          <el-row>
            <el-col :span="12">
              <el-form-item prop="categoryName" label="分类名称">
                <el-input v-model="addModel.categoryName"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item prop="orderNum" label="序号">
                <el-input v-model="addModel.orderNum"></el-input>
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
import { getCategoryListApi,addCategoryApi,editCategoryApi,deleteCategoryApi} from "@/api/category";
import resetForm from '@/utils/resetForm';
import { logger } from 'runjs/lib/common';

export default {
  components: {
    SysDialog
  },
  data() {
    return {
      rules: {
        categoryName: [
          { required: true, message: "分类名称不能为空", trigger: "blur" },
          { min: 2, max: 20, message: "长度在 2 到 20 个字符", trigger: "blur" }
        ],
        orderNum: [
          { required: true, message: "序号不能为空", trigger: "blur" },
        ]
      }, 
      //弹框属性
      dialog: {
        title: "",
        width: 640,
        height: 230,
        visible: false,
      },
      // 搜索框数据
      listParm:{
        currentPage:1,
        pageSize:10,
        categoryName:'',
        total:0
      },
      //表格数据
      tableData: [],
      // 表单数据
      addModel:{
        type:'',
        categoryId:'',
        categoryName:'',
        orderNum:'',
      },
      //表格高度
      tableHeight:0
    };
  },
  methods: {
    //弹框关闭
    onClose(){
      this.dialog.visible = false;
    },
    //弹框确认
    onConfirm(){
      this.$refs.addRef.validate(async(valid) => {
        if (valid) {
        let res = null
          if(this.addModel.type == '0'){
            res = await addCategoryApi(this.addModel);
          }else{
            res = await editCategoryApi(this.addModel);
          }
          console.log(res);
          if (res && res.code == 200) {
                this.$message({ type: "success", message: res.msg });
                this.getList();
                this.dialog.visible = false;
          }
        } 
      });

    },
    
    //页码改变
    currentChange(val){
      this.listParm.currentPage = val;
      this.getList();
    },
    //页容量改变
    sizeChange(val){
      this.listParm.pageSize = val;
      this.getList();
    },
    searchBtn(){
      this.getList();
    },
    resetBtn(){
      this.listParm.categoryName = '';
      this.getList();
    },
    //新增按钮
    addBtn(){
      this.dialog.visible = true;
      this.dialog.title = '新增分类';
      this.addModel.type = '0'

    },
    editBtn(row){
      this.dialog.title = '编辑分类';
      this.addModel.type = '1'
      this.$resetForm('addRef',this.addModel);
      this.$objCopy(row,this.addModel);
      this.dialog.visible = true;
    },
    async deleteBtn(row){
      console.log(row);
      let confirm = await this.$myconfirm("你确定要删除吗？")
      if (confirm) {
        let res = await deleteCategoryApi({ categoryId: row.categoryId });
        if (res && res.code == 200) {
          this.$message({ type: "success", message: res.msg })
          this.getList();
        }
      }
    },

    async getList(){
      let res = await getCategoryListApi(this.listParm);
      // console.log(res);
      if (res && res.code === 200) {
        // console.log(res);
        this.tableData = res.data.records;
        this.listParm.total = res.data.total;
      }
    }
  },
  created() {
    // console.log('测试')
    this.getList();
  },
  mounted(){
    this.$nextTick(()=>{
      this.tableHeight = window.innerHeight - 220;
    }
    )
  }
    
};

</script>

<style lang="scss" scoped>
</style>
