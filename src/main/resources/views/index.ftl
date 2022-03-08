<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>餐饮后台管理-登录</title>
    <#include "common.ftl">
    <link rel="stylesheet" href="${ctx}/css/index.css" media="all">
</head>
<body>
<div class="layui-container">
    <div class="admin-login-background">
        <div class="layui-form login-form">
            <form class="layui-form" action="">
                <div class="layui-form-item logo-title">
                    <h1>餐饮系统后端登录</h1>
                </div>
                <div class="layui-form-item">
                    <label class="layui-icon layui-icon-username" for="username"></label>
                    <input type="text" name="username" lay-verify="required|account" placeholder="用户名或者邮箱" autocomplete="off" class="layui-input" >
                </div>
                <div class="layui-form-item">
                    <label class="layui-icon layui-icon-password" for="password"></label>
                    <input type="password" name="password" lay-verify="required|password" placeholder="密码" autocomplete="off" class="layui-input" >
                </div>
                <div class="row">
                    <div class="col-xs-6 pull_left">
                        <div class="form-group">
                            <input class="form-control" type="tel" id="verify_input" placeholder="请输入验证码" maxlength="4">
                        </div>
                    </div>
                    <div class="col-xs-6 pull_left">
                        <a href=" " title="点击更换验证码">
                        <img id="imgVerify" src="" alt="更换验证码" height="36" width="100%" onclick="getVerify(this);">
                        </a>
                    </div>
                </div>
                <div class="layui-form-item">
                    <button class="layui-btn layui-btn-fluid" lay-submit="" lay-filter="login">登 录</button>
                </div>
            </form>
        </div>
    </div>
</div>
<script src="${ctx}/js/index.js" charset="utf-8"></script>
</body>
</html>