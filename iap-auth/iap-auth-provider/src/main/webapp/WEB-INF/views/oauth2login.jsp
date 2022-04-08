<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<html>
<head>
    <title>登录并授权</title>
    <style>.error {
        color: red;
    }</style>
</head>
<style>
    #iap-div {
        border-radius: 10px;
        margin: 10em auto;
        padding: 30px;
        border: 1px solid #eeeeee;
        width: 25em;
        height: 20em;
        background: rgb(179, 216, 255);
    }

    #iap-form {
        margin: 64px auto;
        width: 25em;
        height: 20em;
    }

    #iap-password {
        margin-top: 32px;
    }

    #submit {
        position: relative;
        top: 5%;
        left: 18%;
        right: 50%;
        width: 23em;
        display: inline-block;
        line-height: 1;
        white-space: nowrap;
        cursor: pointer;
        background: #ecf5ff;
        border: 1px solid #dcdfe6;
        color: #409eff;
        -webkit-appearance: none;
        text-align: center;
        box-sizing: border-box;
        outline: none;
        margin: 0;
        transition: .1s;
        font-weight: 500;
        -moz-user-select: none;
        -webkit-user-select: none;
        -ms-user-select: none;
        padding: 12px 20px;
        font-size: 14px;
        border-radius: 4px;
    }

    #submit:hover {
        background-color: #409eff;
        border-color: #409eff;
        color: #fff;
    }

    .iap-label {
        text-align: right;
        vertical-align: middle;
        font-size: 15px;
        color: #606266;
        line-height: 40px;
        padding: 0 12px 0 0;
        box-sizing: border-box;
    }

    .iap-input {
        -webkit-appearance: none;
        background-color: #fff;
        background-image: none;
        border-radius: 4px;
        border: 1px solid #dcdfe6;
        box-sizing: border-box;
        color: #606266;
        display: inline-block;
        font-size: inherit;
        height: 40px;
        line-height: 40px;
        outline: none;
        padding: 0 15px;
        transition: border-color .2s cubic-bezier(.645, .045, .355, 1);
        width: 80%;
    }

    *:focus {
        outline: none;
        border: 2px solid #409eff;
        /*background-color: transparent;*/
    }

    ::selection {
        /*background:transparent;*/
    }

    ::-moz-selection {
        /*background:transparent;*/
    }

</style>
<body style="margin: 0;padding: 0">
<div id="iap-div">
    <h2 style="text-align: center">IAP3.0</h2>
    <div>Server帐号：[${client.clientName}]</div>
    <div class="error">${error}</div>
    <form action="/authorize" method="POST" id="iap-form">
        <input type="text" name="response_type"  value="code" style="display: none">
        <input type="text" name="redirect_uri"  value="${client.redirectUri}"  style="display: none">
        <input type="text" name="client_id"  value="${client.clientId}"  style="display: none">
        <input type="text" name="client_secret"  value="${client.clientSecret}"  style="display: none">
        <input type="text" name="grant_type"  value="password"  style="display: none">
        <span class="iap-label">用户名：</span><input type="text" name="username" class="iap-input" id="iap-username" value="<shiro:principal/>"><br/>
        <span class="iap-label">密&nbsp;&nbsp;&nbsp;&nbsp;码：</span><input type="password" name="password" class="iap-input" id="iap-password"><br/>
        <input type="submit" value="登录并授权" id="submit">
    </form>
</div>
</body>
</html>