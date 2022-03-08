layui.use(['form', 'layer'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;

    /*添加*/
    form.on("submit(addOrUpdateSaleChance)",function(data){
        console.log(data.field);
        //提交的加载层
        var index = layer.msg("数据提交中,请稍后...",{
            icon:16, // 图标
            time:false, // 不关闭
            shade:0.8 // 设置遮罩的透明度
        });
        //提交数据url
        var url=ctx+"/sale_chance/save";
        //发送ajax添加
        $.post(url,data.field,function(data){
            //判断
            if(data.code==200){
                //成功
                layer.msg("添加成功了");
                //关闭加载层
                layer.close(index);
                //关闭所有的弹出层
                layer.closeAll("iframe");
                //重载
                window.parent.location.reload();
            }else{
                //失败
                layer.msg(data.msg);
            }
        },"json");
        //取消默认跳转
        return false;
    });

    /**
     * 关闭弹出层
     *
     */
    $("#closeBtn").click(function (){
        //先得到当前iframe层的索引
        var index=parent.layer.getFrameIndex(window.name);
        //再执行关闭
        parent.layer.close(index);
    });

    /**
     * 加载下拉框
     */
    $.get(ctx+"/user/queryAllSales",function (data){
        // 如果是修改操作，判断当前修改记录的指派人的值
        var assignMan=$("input[name='man']").val();
        for(var i=0;i<data.length;i++){
            // 当前修改记录的指派人的值 与 循环到的值 相等，下拉框则选中
            if(assignMan == data[i].id){
                $("assignMan").append('<option value="\'+data[i].id+\'"\n' + 'selected>'+data[i].uname+'</option>');
            }else{
                $("#assignMan").append('<optionvalue="'+data[i].id+'">'+data[i].uname+'</option>');
            }
        }

        // 重新渲染下拉框内容
        layui.form.render("select");
    });

});