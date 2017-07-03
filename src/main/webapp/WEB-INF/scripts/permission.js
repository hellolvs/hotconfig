/**
 * Created by lvs on 17-5-26.
 */
$(function () {
    init();                                         //页面初始化，包括获取用户权限信息，配置文件列表及相应的字段列表
    $('#selectBox').on('change', refresh);          //下拉框选项更改时刷新组件：更新下拉框值，隐藏按钮，销毁表格
    $('#btnQuery').on('click', query);              //点击查询按钮，显示用户权限对应的操作按钮，并列表显示具体配置信息
    $('#btnAdd').on('click', add);
    $('#btnEdit').on('click', edit);
    $('#btnDelete').on('click', remove);
});

var configFileArr = [];                                     //配置文件数组
var selectValue = $('#selectBox').val();                    //下拉框当前值

function init() {
    $.ajax({
        type: 'GET',
        url: '/getUserIsAdmin'
    }).done(function (res) {
        if (!res.ret) {
            $('body').html('<p class="imporMsg">没有管理员权限</p>');
        } else {
            $.ajax({
                type: 'GET',
                url: '/manageUserPermission/CurrentUserPermission'
            }).done(function (res) {
                if (res && res.ret) {
                    res.data.list.forEach(function (ele, index) {
                        configFileArr[index] = ele['fileId'];          //配置列表数组
                    });
                    configFileArr.forEach(function (ele, index) {
                        $('#selectBox').append("<option value='" + ele + "'>" + ele + "</option>");
                    });
                }
            });
        }
    });
}

//隐藏除查询外所有操作按钮
function hideAll() {
    $('#btnAdd').hide();                                    //添加按钮
    $('#btnDelete').hide();                                 //删除按钮
    $('#btnEdit').hide();                                   //修改按钮
}

function refresh() {
    selectValue = $('#selectBox').val();
    hideAll();
    destroyTable();
}

function query() {
    if (selectValue === '-') {
        return;
    }
    $('#btnAdd').show();
    $('#btnDelete').show();
    $('#btnEdit').show();
    initTable();
}

function destroyTable() {
    $('#bootstrap-table').bootstrapTable('destroy');
}

function initTable() {
    $('#bootstrap-table').bootstrapTable({

        /*向服务器发送请求*/
        url: 'manageUserPermission/CurrentUserPermission/query',//请求后台的URL
        method: 'post',                                         //请求方式，默认为get
        contentType: 'application/x-www-form-urlencoded',       //发送到服务器的数据编码类型，默认为json
        queryParams: function (params) {                        //请求参数，必须与controller接收参数一致
            return {
                fileId: selectValue
            };
        },

        /*是否使用缓存，默认为true*/
        cache: false,

        /*样式、组件相关*/
        toolbar: '#toolbar',                                    //工具按钮用哪个容器
        striped: true,                                          //是否隔行变色，默认false
        sortable: true,                                         //是否启用排序，默认true
        search: true,                                           //是否显示搜索框
        searchAlign: 'left',                                    //搜索框水平位置，默认right
        strictSearch: true,                                     //设置为true启用全匹配搜索，默认false模糊搜索
        showColumns: true,                                      //是否显示指定列下拉框
        showRefresh: true,                                      //是否显示刷新按钮
        showToggle: true,                                       //是否显示详细视图和列表视图的切换按钮
        clickToSelect: true,                                    //设置true将在点击行时，自动选择rediobox和checkbox
        rowStyle: function (row, index) {                       //隔行变色
            return {classes: (index % 2 === 0) ? '' : 'info'}
        },

        responseHandler: function (res) {
            var list = [];
            res.data.list.forEach(function (ele) {
                if (ele['modifyPermission']) {
                    ele['modifyPermission'] = "&#10003";
                } else {
                    ele['modifyPermission'] = "";
                }
                if (ele['publishPermission']) {
                    ele['publishPermission'] = "&#10003";
                } else {
                    ele['publishPermission'] = "";
                }
                list.push(ele);
            });
            return list;
        },

        columns: [{
            checkbox: true
        }, {
            field: 'id',
            title: '编号',
            visible: false
        },{
            field: 'userId',
            title: '用户名',
            sortable: true
        }, {
            field: 'modifyPermission',
            title: '编辑权限'
        }, {
            field: 'publishPermission',
            title: '发布权限'
        }]

    });
}

function getFormData(form) {
    var formData = form.serializeArray();
    var obj = {};
    $.each(formData, function () {
        if (obj[this.name] !== undefined) {
            if (!obj[this.name].push) {
                obj[this.name] = [obj[this.name]];
            }
            obj[this.name].push(this.value || '');
        } else {
            obj[this.name] = this.value || '';
        }
    });
    obj.fileId = selectValue;
    if(!obj.modifyPermission){
        obj.modifyPermission = false;
    }else {
        obj.modifyPermission = true;
    }
    if(!obj.publishPermission){
        obj.publishPermission = false;
    }else {
        obj.publishPermission = true;
    }
    return JSON.stringify(obj);
}

//添加权限信息
function add() {

    var form = $('<form class="form-horizontal" role="form"></form>');
    //language=HTML
    form.append(
        ' <div id="addMsg" class="imporMsg"></div>\
          <div class="form-group">\
              <label class="col-sm-2 control-label">用户名</label>\
              <div class="col-sm-9">\
                  <input type="text" name="userId" class="form-control"/>\
              </div>\
          </div>\
          <div class="form-group">\
              <label class="col-sm-2 control-label">请选择权限</label>\
              <div class="col-sm-9">\
                  <label class="radioPermission" for="modifyPermission">\
                      <input type="checkbox" name="modifyPermission" id="modifyPermission">编辑权限\
                  </label>\
                  <label class="radioPermission" for="publishPermission">\
                      <input type="checkbox" name="publishPermission" id="publishPermission">发布权限\
                  </label>\
              </div>\
          </div>'
    );

    BootstrapDialog.show({
        size: 'size-wide',
        title: '请填写',
        message: form,
        buttons: [{
            label: '提交',
            cssClass: 'btn btn-primary',
            action: function (dialog) {
                var data = getFormData(form);
                $.ajax({
                    type: 'POST',
                    data: data,
                    contentType: 'application/json; charset=utf-8', //发送数据类型，默认application/x-www-form-urlencoded
                    url: 'manageUserPermission/CurrentUserPermission/add'
                }).done(function (res) {
                    if (!res.ret) {//校验出错，提示错误信息
                        $('#addMsg').text(res.errmsg);
                    } else {
                        dialog.close();
                        $('#bootstrap-table').bootstrapTable('refresh');
                    }
                });
            }
        }]
    });
}

//修改配置信息
function edit() {

    var select = $('#bootstrap-table').bootstrapTable('getSelections');
    if (select.length !== 1) {
        BootstrapDialog.show({
            size: 'size-wide',
            title: '提示',
            message: $('<div class="imporMsg">请选中一条数据（不能多于一条）</div>')
        });
        return;
    }

    var form = $('<form class="form-horizontal" role="form"></form>');
    form.append(
        ' <div id="editMsg" class="imporMsg"></div>\
          <div class="form-group">\
              <label class="col-sm-2 control-label">用户名</label>\
              <div class="col-sm-9">\
                  <input type="text" name="userId" id="userId" value="+select[0][\'userId\']+" readonly class="form-control"/>\
              </div>\
          </div>\
          <div class="form-group">\
              <label class="col-sm-2 control-label">请选择权限</label>\
              <div class="col-sm-9">\
                  <label class="radioPermission" for="modifyPermission">\
                      <input type="checkbox" name="modifyPermission" id="modifyPermission">编辑权限\
                  </label>\
                  <label class="radioPermission" for="publishPermission">\
                      <input type="checkbox" name="publishPermission" id="publishPermission">发布权限\
                  </label>\
              </div>\
          </div>'
    );

    //对修改对话框表单填充当前值
    form.find("input[name='userId']").attr("value",select[0]['userId']);
    if (select[0]['modifyPermission'] === ""){
        form.find("input[name='modifyPermission']").attr("checked",false);
    }else {
        form.find("input[name='modifyPermission']").attr("checked",true);
    }
    if (select[0]['publishPermission'] === ""){
        form.find("input[name='publishPermission']").attr("checked",false);
    }else {
        form.find("input[name='publishPermission']").attr("checked",true);
    }

    BootstrapDialog.show({
        size: 'size-wide',
        title: '请填写',
        message: form,
        buttons: [{
            label: '提交',
            cssClass: 'btn btn-primary',
            action: function (dialog) {
                var formData = getFormData(form);
                var obj = JSON.parse(formData);
                obj.id = select[0]['id'];
                var data = JSON.stringify(obj);
                $.ajax({
                    type: 'POST',
                    data: data,
                    contentType: 'application/json; charset=utf-8', //发送数据类型，默认application/x-www-form-urlencoded
                    url: 'manageUserPermission/CurrentUserPermission/update'
                }).done(function (res) {
                    if (!res.ret) {//校验出错，提示错误信息
                        $('#editMsg').text(res.errmsg);
                    } else {
                        dialog.close();
                        $('#bootstrap-table').bootstrapTable('refresh');
                    }
                });
            }
        }]
    });
}

//删除配置信息
function remove() {

    var select = $('#bootstrap-table').bootstrapTable('getSelections');
    if (select.length < 1) {
        BootstrapDialog.show({
            size: 'size-wide',
            title: '提示',
            message: $('<div class="imporMsg">请选中至少一条数据</div>')
        });
        return;
    }

    BootstrapDialog.show({
        size: 'size-wide',
        title: '请确认',
        message: $('<div class="imporMsg">确定要删除这些用户权限吗？</div>'),
        buttons: [{
            label: '确认',
            cssClass: 'btn btn-primary',
            action: function (dialog) {
                select.forEach(function (ele) {
                    var id = ele['id'];
                    $.ajax({
                        type: 'POST',
                        data: JSON.stringify({"id": id}),
                        contentType: 'application/json; charset=utf-8', //发送数据类型，默认application/x-www-form-urlencoded
                        url: 'manageUserPermission/CurrentUserPermission/delete'
                    });
                });
                dialog.close();
                $('#bootstrap-table').bootstrapTable('refresh');
            }
        }]
    });
}