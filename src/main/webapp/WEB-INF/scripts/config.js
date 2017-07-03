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
    $('#btnDiff').on('click', diff);
    $('#btnRelease').on('click', release);

});

var permissionInfo;                                         //当前用户对所有配置文件的权限信息
var permissionOfCurrentFile;                                //用户对当前配置文件的权限信息
var configFileArr = [];                                     //配置文件数组
var configFieldArr = [];                                    //配置文件字段数组
var selectValue = $('#selectBox').val();                    //下拉框当前值

function init() {
    $.ajax({
        type: 'GET',
        url: '/getUserReadPermission'
    }).done(function (res) {
        if (!res.ret) {           //没有查看权限
            $('body').html('<p class="imporMsg">没有查看权限</p>');
        } else {                 //有查看权限，请求下拉菜单和具体权限信息
            $.ajax({
                type: 'GET',
                url: '/manageConfFiles/CurrentConfData'
            }).done(function (res) {
                if (res && res.ret) {
                    res.data.list.forEach(function (ele, index) {
                        configFileArr[index] = ele['fileId'];          //配置列表数组
                        configFieldArr[ele['fileId']] = ele['tableField'];     //字段对象数组
                    });
                    configFileArr.forEach(function (ele, index) {
                        $('#selectBox').append("<option value='" + ele + "'>" + ele + "</option>");
                    });
                }
            });

            //请求具体权限信息
            $.ajax({
                type: 'GET',
                url: '/getUserPermission'
            }).done(function (res) {
                if (res && res.ret) {
                    permissionInfo = res.data.list;
                }
            });
        }
    });
}

//隐藏除查询外所有操作按钮
function hideAll() {
    $('#btnRelease').hide();                                //发布按钮
    $('#btnDiff').hide();                                   //diff按钮
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
        return
    }
    showButton();
    initTable();
}

//选中下拉框并点击查询按钮时，控制页面显示
function showButton() {
    if (permissionInfo) {
        permissionOfCurrentFile = null;
        permissionInfo.forEach(function (ele, index) {
            if (ele.fileId === selectValue) {
                permissionOfCurrentFile = ele;
            }
        })
    } else {
        permissionOfCurrentFile = null;
    }
    if (permissionOfCurrentFile) {
        if (permissionOfCurrentFile.modifyPermission) {           //有修改权限
            $('#btnAdd').show();
            $('#btnDelete').show();
            $('#btnEdit').show();
            $('#btnDiff').show();
        } else if (permissionOfCurrentFile.publishPermission) {    //有发布权限
            $('#btnRelease').show();
            $('#btnDiff').show();
        }
    }
}

function destroyTable() {
    $('#bootstrap-table').bootstrapTable('destroy');
}

function initTable() {
    $('#bootstrap-table').bootstrapTable({

        /*向服务器发送请求*/
        url: '/manageConfFiles/CurrentConfData/query',          //请求后台的URL
        method: 'post',                                         //请求方式，默认为get
        contentType: 'application/x-www-form-urlencoded',       //发送到服务器的数据编码类型，默认为json
        queryParams: function (params) {                        //请求参数，必须与controller接收参数一致
            return {
                fileId: selectValue,
                limit: 20000,   //页面大小
                start: params.offset  //偏移量
            };
        },

        /*是否使用缓存，默认为true*/
        cache: false,

        /*分页相关*/
        pagination: true,                                       //是否显示分页.默认false
        sidePagination: 'client',                               //分页方式：client客户端分页，server服务端分页，默认client

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

        /*加载服务器数据之前的处理程序，可以用来格式化数据,
         *res为服务端返回的json对象
         *分页方式为server时，必须包含rows和total字段*/
        responseHandler: function (res) {
            return res.data.list;
        },
        // responseHandler: function (res) {
        //     if (res) {
        //         return {
        //             rows: res.data.list,
        //             total: res.data.totalCount
        //         };
        //     } else {
        //         return {
        //             rows: [],
        //             total: 0
        //         };
        //     }
        // },

        /*动态获取列信息*/
        columns: getColumns()

    });
}

function getColumns() {
    var columns = [];
    columns.push({
        checkbox: true
    });
    configFieldArr[selectValue].forEach(function (ele, index) {
        columns.push({
            field: ele['title'],
            title: ele['title'],
            sortable: true
        });
    });
    return columns;
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
    return JSON.stringify(obj);
}

//添加配置信息
function add() {

    var form = $('<form class="form-horizontal" role="form"></form>');
    form.append('<div id="addMsg" class="imporMsg"></div>');
    configFieldArr[selectValue].forEach(function (ele, index) {
        if (ele['title'] !== 'id') {
            var div = $('<div class="form-group"></div>');
            var label = $('<label class="col-sm-2 control-label"></label>');
            label.text(ele['title']);
            var div2 = $('<div class="col-sm-9"></div>');
            var input = $('<input type="text" class="form-control"/>');
            input.attr("name", ele['title']);
            div2.append(input);
            div.append(label, div2);
            form.append(div);
        }
    });

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
                    data: {model: data, fileId: selectValue},
                    url: '/manageConfFiles/CurrentConfData/add'
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
    form.append('<div id="editMsg" class="imporMsg"></div>');
    configFieldArr[selectValue].forEach(function (ele, index) {
        if (ele['title'] !== 'id') {
            var div = $('<div class="form-group"></div>');
            var label = $('<label class="col-sm-2 control-label"></label>');
            label.text(ele['title']);
            var div2 = $('<div class="col-sm-9"></div>');
            var input = $('<input type="text" class="form-control"/>');
            input.attr("name", ele['title']);
            input.attr("value", select[0][ele['title']]);
            div2.append(input);
            div.append(label, div2);
            form.append(div);
        }
    });

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
                    data: {model: data, fileId: selectValue},
                    url: '/manageConfFiles/CurrentConfData/update'
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
        message: $('<div class="imporMsg">确定要删除勾选的这些数据吗？</div>'),
        buttons: [{
            label: '确认',
            cssClass: 'btn btn-primary',
            action: function (dialog) {
                select.forEach(function (ele) {
                    var id = ele['id'];
                    $.ajax({
                        type: 'POST',
                        data: {id: id, fileId: selectValue},
                        url: '/manageConfFiles/CurrentConfData/delete'
                    });
                });
                dialog.close();
                $('#bootstrap-table').bootstrapTable('refresh');
            }
        }]
    });
}

//diff
function diff() {

    $('#btnDiff').text("正在加载中");
    $('#btnDiff').attr("disabled", "disabled");
    var diffTable = $('<table id="diffTable"></table>');
    var div = $('<div class="container-fluid"></div>');
    div.append(diffTable);
    $.ajax({
        type: 'POST',
        data: {fileId: selectValue},
        url: '/manageConfFiles/CurrentConfData/diff'
    }).done(function (res) {
        if (res.ret) {
            var data = [];
            res.data.list.forEach(function (ele) {
                ele.model.crudType = ele.crudType;
                ele.model.columnArr = ele.columnArr;
                data.push(ele.model);
            });
            var columns = [];
            configFieldArr[selectValue].forEach(function (ele, index) {
                columns.push({
                    field: ele['title'],
                    title: ele['title'],
                    sortable: true,
                    cellStyle: function (value, row, index) {
                        if ($.inArray(ele['title'], row.columnArr) !== -1) {
                            if (row.crudType === 2) {
                                return {classes: 'before'};
                            } else if (row.crudType === 3) {
                                return {classes: 'after'};
                            }
                        }
                        return {};
                    }
                });
            });
            diffTable.bootstrapTable({
                data: data,
                columns: columns,
                sortable: false,
                rowStyle: function (row, index) {
                    if (row.crudType === 0) {
                        return {classes: 'addRow'};
                    }
                    if (row.crudType === 1) {
                        return {classes: 'deleteRow'};
                    }
                    return {};
                }
            });
            BootstrapDialog.show({
                size: 'size-wide',
                title: "diff",
                message: div
            });
        } else {
            BootstrapDialog.alert('<p class="imporMsg">错误信息' + res.errmsg + '</p>');
        }
        $('#btnDiff').removeAttr("disabled");
        $('#btnDiff').text("diff");
    });
}

//发布
function release() {

    $('#btnRelease').text("正在加载中");
    $('#btnRelease').attr("disabled", "disabled");
    var diffTable = $('<table id="diffTable"></table>');
    var div = $('<div class="container-fluid"></div>');
    div.append(diffTable);
    $.ajax({
        type: 'POST',
        data: {fileId: selectValue},
        url: '/manageConfFiles/CurrentConfData/diff'
    }).done(function (res) {
        if (res.ret) {
            var data = [];
            res.data.list.forEach(function (ele) {
                ele.model.crudType = ele.crudType;
                ele.model.columnArr = ele.columnArr;
                data.push(ele.model);
            });
            var columns = [];
            configFieldArr[selectValue].forEach(function (ele, index) {
                columns.push({
                    field: ele['title'],
                    title: ele['title'],
                    sortable: true,
                    cellStyle: function (value, row, index) {
                        if ($.inArray(ele['title'], row.columnArr) !== -1) {
                            if (row.crudType === 2) {
                                return {classes: 'before'};
                            } else if (row.crudType === 3) {
                                return {classes: 'after'};
                            }
                        }
                        return {};
                    }
                });
            });
            diffTable.bootstrapTable({
                data: data,
                columns: columns,
                sortable: false,
                rowStyle: function (row, index) {
                    if (row.crudType === 0) {
                        return {classes: 'addRow'};
                    }
                    if (row.crudType === 1) {
                        return {classes: 'deleteRow'};
                    }
                    return {};
                }
            });
            BootstrapDialog.show({
                size: 'size-wide',
                title: "diff",
                message: div,
                buttons: [{
                    label: '确认发布',
                    cssClass: 'btn btn-primary',
                    action: function (dialog) {
                        var button = this;
                        button.text("正在发布中");
                        button.disable();
                        $.ajax({
                            type: 'POST',
                            data: {fileId: selectValue},
                            url: '/manageConfFiles/CurrentConfData/publish'
                        }).done(function (res) {
                            if (res.ret) {
                                dialog.close();
                                BootstrapDialog.alert('<p class="imporMsg">发布成功！</p>');
                            } else {
                                button.text("确认发布");
                                button.enable();
                                dialog.setMessage('<p class="imporMsg">' + res.errmsg + '</p>');
                            }
                        })
                    }
                }]
            });
        } else {
            BootstrapDialog.alert('错误信息' + res.errmsg);
        }
        $('#btnRelease').removeAttr("disabled");
        $('#btnRelease').text("发布");
    });
}