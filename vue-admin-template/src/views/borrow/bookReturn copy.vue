<template>
    <el-main>
        <el-form :model="listParm" label-width="80px" :inline="true" size="small">
            <el-form-item label="学号">
                <el-input v-model="listParm.username">学号</el-input>
            </el-form-item>
            <el-form-item label="借书状态">
                <el-select v-model="listParm.borrowStatus" placeholder="请选择">
                    <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" icon="el-icon-search" @click="searchBtn">查询</el-button>
                <el-button style="color: rgb(211, 85, 85);" icon="el-icon-refresh" @click="resetBtn">重置</el-button>
                <el-button type="primary" @click="returnBook">还书</el-button>
            </el-form-item>
        </el-form>
        <el-table ref="tables" :data="tableData" :height="tableHeight" border stripe>
            <el-table-column type="selection" width="55"></el-table-column>
            <el-table-column prop="bookName" label="图书名称"></el-table-column>
            <el-table-column prop="bookPlaceNum" label="书架号"></el-table-column>
            <el-table-column prop="username" label="学号"></el-table-column>
            <el-table-column prop="learnName" label="借书人"></el-table-column>
            <el-table-column prop="className" label="班级"></el-table-column>、
            <el-table-column prop="phone" label="电话"></el-table-column>
            <el-table-column prop="borrowTime" label="借书时间"></el-table-column>
            <el-table-column prop="returnTime" label="应还书时间"></el-table-column>
            <el-table-column label="操作" align="center" width="180">
                <template slot-scope="scope">
                    <el-button size="mini" type="primary" @click="returnBookBtn(scope.row)">还书</el-button>
                    <el-button size="mini" type="danger" @click="ExceptioReturnBtn(scope.row)">异常还书</el-button>
                </template>
            </el-table-column>
        </el-table>
        <!-- 分页 -->
        <el-pagination @size-change="sizeChange" @current-change="currentChange" :current-page.sync="listParm.currentPage"
            :page-sizes="[10, 20, 40, 80, 100]" :page-size="listParm.pageSize"
            layout="total, sizes, prev, pager, next, jumper" :total="listParm.total" background>
        </el-pagination>
        <!-- 异常还书 -->
        <sys-dialog :title="dialog.title" :width="dialog.width" :height="dialog.height" :visible="dialog.visible"
            @onClose="onClose" @onConfirm="onConfirm">
            <div slot="content">
                <el-form :model="exception" ref="exceptionRef" :rules="rules" label-width="80px" size="small">
                    <el-form-item prop="type" label="异常类型">
                        <el-select v-model="exception.type" placeholder="请选择">
                            <el-option v-for="item in exoptions" :key="item.value" :label="item.label" :value="item.value">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item prop="excepionText" label="备注">
                        <el-input type="textarea" v-model="exception.excepionText"></el-input>
                    </el-form-item>
                </el-form>
            </div>
        </sys-dialog>
    </el-main>
</template>
<script>

import { returnBookList, returnBooksApi, exceptionBooksApi } from '@/api/borrow';
import SysDialog from '@/components/system/SysDialog.vue';
export default {
    components: {
        SysDialog,
    },
    data() {
        return {
            rules: {
                type: [
                    { required: true, message: "请选择异常类型", trigger: "change" },
                ],
                excepionText: [
                    { required: true, message: "请填写备注", trigger: "change" },
                ],
            },
            exoptions: [
                {
                    value: "0",
                    label: "异常还书",
                },
                {
                    value: "1",
                    label: "丢失",
                },
            ],
            exception: {
                borrowId: "",
                bookId: "",
                type: "",
                excepionText: "",
            },
            //弹框属性定义
            dialog: {
                width: 630,
                height: 150,
                title: "还书备注",
                visible: false,
            },
            bookIds: [],
            tableHeight: 0,
            tableData: [],
            bookStatus: '',
            options: [
                { value: "1", label: "在借中" },
                { value: "2", label: "已还" },
                { value: "3", label: "拒绝" }, 0
            ],
            listParm: {
                currentPage: 1,
                pageSize: 10,
                username: '',
                borrowStatus: '1',
                total: 0
            },
            returnParm: {
            }
        }
    },
    methods: {
        onConfirm() {
            this.bookIds = []
            this.$refs.exceptionRef.validate(async (valid) => {
                if (valid) {
                    let res = await exceptionBooksApi(this.exception);
                    if (res && res.code == 200) {
                        this.$message.success(res.msg)
                        this.returnBorrow();
                        this.dialog.visible = false;
                    }
                }
            });
        },
        onClose() {
            this.dialog.visible = false;
        },
        //异常还书的事件
        ExceptioReturnBtn(row) {
            this.exception.borrowId = row.borrowId
            this.exception.bookId = row.bookId
            this.dialog.visible = true;
        },
        //查询按钮
        searchBtn() {
            this.getListApi()
        },
        //重置按钮
        resetBtn() {
            this.listParm.currentPage = 1;
            this.listParm.username = '';
            this.listParm.borrowStatus = '';
            this.getListApi();
        },
        async returnBook() {
            this.bookIds = [];
            let confirm = await this.$myconfirm("确定还书吗?");
            if (confirm) {
                let datas = this.$refs.tables.selection;
                if (datas.length == 0) {
                    this.$message.error("请选择要还的书！");
                    return;
                }
                for (let i = 0; i < datas.length; i++) {
                    let obj = {};
                    obj.borrowId = datas[i].borrowId;
                    obj.bookId = datas[i].bookId;
                    this.bookIds.push(obj);
                }
                console.log('还书',this.bookIds,this.bookIds.length);
                let res = await returnBooksApi(this.bookIds);
                if (res && res.code == 200) {
                    this.$message.success(res.msg);
                    this.getListApi();
                }
            }
        },
        //还书按钮
        async returnBookBtn(row) {
            // this.bookIds = [];
            const onConfirm = await this.$myconfirm('确定要还书吗？');
            let parm = {}
            parm.bookId = row.bookId,
            parm.borrowId = row.borrowId,
            this.bookIds.push(parm);
            console.log('单独还书按钮',this.bookIds);
            if (onConfirm) {
                const res = await returnBooksApi(this.bookIds);
                if (res && res.code == 200) {
                    this.$message({ type: 'success', message: res.msg })
                    this.getListApi();
                }
            }
        },


        async getListApi() {
            const res = await returnBookList(this.listParm);
            if (res && res.code == 200) {
                this.tableData = res.data.records;
                this.listParm.total = res.data.total;
            }
            console.log(res);
        },
        //页面容量变化
        currentChange(val) {
            this.listParm.currentPage = val;
            this.getListApi();
        },
        //页数变化
        sizeChange(val) {
            this.listParm.pageSize = val;
            this.getListApi();
        },



    },
    mounted() {
        this.$nextTick(() => {
            this.tableHeight = window.innerHeight - 200;
        })
        this.getListApi();
    },
}
</script>
<style lang="scss" scoped></style>