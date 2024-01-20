<template>
  <el-main>
    <div style="margin:0px 0px 15px 0px;color:#0037ff;font-weight:600;font-size:15px;">读者信息</div>
    <el-card class="box-card">
      <div slot="header" style="padding: 0px" class="clearfix">
        <el-form :model="searchParm" label-width="80px" :inline="true" size="mini">
          <el-form-item label="学号">
            <el-input v-model="searchParm.username"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button icon="el-icon-search" @click="searchUsername">查询</el-button>
            <el-button style="color: #ff7670" icon="el-icon-close" @click="resetBtn">重置</el-button>

          </el-form-item>
          <el-form-item label="还书时间">
            <el-date-picker v-model="returnTime" type="date" placeholder="选择还书日期">
            </el-date-picker>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="borrowBtn">借书</el-button>
          </el-form-item>
        </el-form>
      </div>
      <div>
        <el-form :model="showUser" ref="form" label-width="80px" :inline="true" size="small">
          <el-form-item label="姓名">
            <el-input v-model="showUser.learnName"></el-input>
          </el-form-item>
          <el-form-item label="班级">
            <el-input v-model="showUser.className"></el-input>
          </el-form-item>
          <el-form-item label="学号">
            <el-input v-model="showUser.username"></el-input>
          </el-form-item>
          <el-form-item label="电话">
            <el-input v-model="showUser.phone"></el-input>
          </el-form-item>
          <el-form-item label="证件号">
            <el-input v-model="showUser.idCard"></el-input>
          </el-form-item>
          <el-form-item label="性别">
            <el-radio v-model="showUser.sex" label="0">男</el-radio>
            <el-radio v-model="showUser.sex" label="1">女</el-radio>
          </el-form-item>
          <el-form-item label="状态">
            <el-radio v-model="showUser.checkStatus" label="0">未审核</el-radio>
            <el-radio v-model="showUser.checkStatus" label="1">已审核</el-radio>
          </el-form-item>
        </el-form>
      </div>
    </el-card>
    <div style="margin:15px 0px;color:#0037ff;font-weight:600;font-size:15px;">图书列表</div>
    <div>
      <elt-transfer ref="eltTransfer" :show-query="true" :show-pagination="true"
        :pagination-call-back="paginationCallBack" :left-columns="leftColumns" :title-texts="['待选', '已选']"
        :button-texts="['添加', '删除']" :query-texts="['查询', '查询']" :table-row-key="(row) => row.bookId" v-model="tableData">
        <!-- 可以使用插槽获取到列信息和行信息，从而进行数据的处理 -->
        <template v-slot:leftCondition>
          <el-form-item label="图书名称">
            <el-input v-model="listParm.bookName" placeholder="图书名称"></el-input>
          </el-form-item>
          <el-form-item label="图书作者">
            <el-input v-model="listParm.bookAuther" placeholder="图书作者"></el-input>
          </el-form-item>
        </template>
        <template v-slot:rightCondition="{ scope }">
          <el-form-item label="名称">
            <el-input v-model="scope.name" placeholder="名称"></el-input>
          </el-form-item>
        </template>
      </elt-transfer>
    </div>
  </el-main>
</template>
  
<script>
import eltTransfer from 'elt-transfer/src/eltTransfer'
import { getByUserNameApi } from "@/api/reader";
import { getListApi } from "@/api/book";
import { borrowBookApi } from "@/api/borrow";
export default {
  components: {
    "elt-transfer": eltTransfer,
  },
  data() {
    return {
      returnTime: '',
      bookIds: [],
      //图书列表分页参数
      listParm: {
        currentPage: 1,
        pageSize: 10,
        bookname: '',
        bookAuther: '',
      },

      tableData: [],
      leftColumns: [
        { label: "图书名称", id: "bookName", width: "120px" },
        { label: "图书分类", id: "categoryName", width: "120px" },
        { label: "书架号", id: "bookPlaceNum" },
        { label: "作者", id: "bookAuther" },
        { label: "出版社", id: "bookProduct" },
        { label: "库存", id: "bookStore" },
      ],
      data1: [
        { name: "佘寄南", gender: "男", age: 12 },
        { name: "聊夏云", gender: "男", age: 13 },
        { name: "出凌柏", gender: "男", age: 14 },
        { name: "丛琴心", gender: "女", age: 15 },
        { name: "方琼岚", gender: "女", age: 15 },
        { name: "焉彦珺", gender: "女", age: 15 },
        { name: "第凝旋", gender: "女", age: 15 },
        { name: "续文君", gender: "女", age: 15 },
        { name: "独松雪", gender: "女", age: 15 },
        { name: "有念桃", gender: "女", age: 15 },
        { name: "闻人晶瑶", gender: "男", age: 12 },
        { name: "祖震博", gender: "男", age: 13 },
        { name: "史妞妞", gender: "男", age: 14 },
        { name: "天如风", gender: "女", age: 15 },
        { name: "隗飞昂", gender: "女", age: 15 },
        { name: "堵舒", gender: "女", age: 15 },
        { name: "菅芬", gender: "女", age: 15 },
        { name: "疏千风", gender: "女", age: 15 },
        { name: "项虹颖", gender: "女", age: 15 },
        { name: "劳念云", gender: "女", age: 15 },
        { name: "闻凝绿", gender: "男", age: 12 },
        { name: "牧冬莲", gender: "男", age: 13 },
        { name: "楚驰颖", gender: "男", age: 14 },
        { name: "奕丹山", gender: "女", age: 15 },
        { name: "姬孤晴", gender: "女", age: 15 },
        { name: "邛俊晤", gender: "女", age: 15 },
        { name: "悟瑜蓓", gender: "女", age: 15 },
        { name: "桓嘉颖", gender: "女", age: 15 },
        { name: "卿安琪", gender: "女", age: 15 },
        { name: "程宾", gender: "女", age: 15 },
        { name: "答斐", gender: "男", age: 12 },
        { name: "竺琬凝", gender: "男", age: 13 },
        { name: "谷鸿才", gender: "男", age: 14 },
        { name: "牵忆南", gender: "女", age: 15 },
        { name: "飞晓彤", gender: "女", age: 15 },
        { name: "范鸿波", gender: "女", age: 15 },
        { name: "庚经亘", gender: "女", age: 15 },
        { name: "干芮欣", gender: "女", age: 15 },
        { name: "才靖荷", gender: "女", age: 15 },
        { name: "原婧", gender: "女", age: 15 },
        { name: "士霞飞", gender: "男", age: 12 },
        { name: "首雨安", gender: "男", age: 13 },
        { name: "保皛", gender: "男", age: 14 },
        { name: "皇甫文轩", gender: "女", age: 15 },
        { name: "颛孙兴平", gender: "女", age: 15 },
        { name: "水鸾", gender: "女", age: 15 },
        { name: "仉棠", gender: "女", age: 15 },
        { name: "伦昊昊", gender: "女", age: 15 },
        { name: "速晟睿", gender: "女", age: 15 },
        { name: "万俟仪芳", gender: "女", age: 15 },
        { name: "卫孟", gender: "男", age: 12 },
        { name: "仆访波", gender: "男", age: 13 },
        { name: "奚冰海", gender: "男", age: 14 },
        { name: "褚冰香", gender: "女", age: 15 },
        { name: "布凌丝", gender: "女", age: 15 },
        { name: "苦星", gender: "女", age: 15 },
        { name: "钦忆安", gender: "女", age: 15 },
        { name: "姒晓曼", gender: "女", age: 15 },
        { name: "蒉英武", gender: "女", age: 15 },
        { name: "锺晴美", gender: "女", age: 15 },
        { name: "泥元槐", gender: "男", age: 12 },
        { name: "买初珍", gender: "男", age: 13 },
        { name: "秦乾", gender: "男", age: 14 },
        { name: "谭白山", gender: "女", age: 15 },
        { name: "登幻翠", gender: "女", age: 15 },
        { name: "蚁盼夏", gender: "女", age: 15 },
        { name: "居梓莹", gender: "女", age: 15 },
        { name: "中觅风", gender: "女", age: 15 },
        { name: "慈珺琦", gender: "女", age: 15 },
        { name: "谏听云", gender: "女", age: 15 },
        { name: "应鹏煊", gender: "男", age: 12 },
        { name: "驹霁", gender: "男", age: 13 },
        { name: "慕容智纯", gender: "男", age: 14 },
        { name: "可茹薇", gender: "女", age: 15 },
        { name: "夫冰薇", gender: "女", age: 15 },
        { name: "李朋", gender: "女", age: 15 },
        { name: "轩辕萍韵", gender: "女", age: 15 },
        { name: "简沛蓝", gender: "女", age: 15 },
        { name: "粟阳舒", gender: "女", age: 15 },
        { name: "郦高韵", gender: "女", age: 15 },
      ],
      //读者信息搜索框
      searchParm: {
        username: "",
      },
      showUser: {
        readerId:'',
        learnName: "",
        username: "",
        idCard: "",
        sex: "",
        phone: "",
        className: "",
        checkStatus: "",
      },
    };
  },
  methods: {
    //借书按钮
    async borrowBtn() {
      this.bookIds = [];
      console.log(this.returnTime,'ces',new Date(),this.returnTime < new Date());

      if(!this.returnTime){
        this.$message.warning("请选择归还时间");
        return;  
      }
      //如果归还时间在当前时间之前
      if(this.returnTime && this.returnTime < new Date()){
        this.$message.warning("归还时间不能小于当前时间");
        return;
      }
      if(!this.showUser.readerId){
        this.$message.warning("请选择借书人");
        return;
      }
      if(this.tableData.length < 1){
        this.$message.warning("请选择图书");
        return;
      }
      this.bookIds = this.tableData.map(item => item.bookId);
      let parm = {
        bookIds: this.bookIds,
        readerId: this.showUser.readerId,
        returnTime: this.returnTime,
      };
      let res = await borrowBookApi(parm);
      if(res && res.code == 200){
        this.$message.success(res.msg);
        setTimeout(() => {
          window.location.reload()
        }, 3000);
      }
    },
    //重置按钮
    resetBtn() {
      this.searchParm.username = "";
      this.showUser.learnName = "";
      this.showUser.idCard = "";
      this.showUser.username = "";
      this.showUser.phone = "";
      this.showUser.className = "";
      this.showUser.sex = "";
      this.showUser.checkStatus = "";
      this.showUser.readerId = "";

    
    },
    //学号查询
    async searchUsername() {
      const res = await getByUserNameApi(this.searchParm);
      if (res && res.code == 200) {
        this.showUser = res.data;
      }
    },
    async paginationCallBack(obj) {
      console.log(obj);
      this.listParm.currentPage = obj.pageIndex;
      this.listParm.pageSize = obj.pageSize;
      let res = await getListApi(this.listParm);
      return new Promise((resolve, reject) => {
        try {
          resolve({ total: res.data.total, data: res.data.records });
        } catch {
          reject();
        }
      });
    },
    clearTransfer() {
      this.$refs.eltTransfer.clear();
    },
  },
};
</script>
  
<style scoped>
::v-deep .el-card__header {
  padding: 15px 0px 0px 0px;
}

::v-deep .el-card__body {
  padding: 15px 0px 0px 0px;
}
</style>