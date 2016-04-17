<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>AdminLogin</title>
    <script type="text/javascript" src="/js/CheckInput.js"></script>
    <script type="text/javascript">
        //验证表单输入
        function checkInput(thisForm) {
            with (thisForm) {
                var mCheck = new CheckInput();
                return mCheck.isEmpty(username, "UserName Empty") && mCheck.isEmpty(password, "Password Empty");
            }
        }
    </script>
</head>
<body>
Admin 进行 Login 的界面

<!--Note: 表单的跳转 /admin和web.xml里面的url-pattern进行比较 并将此Action分发给Jfinal-->
<form action="/admin" method="post" onsubmit="return checkInput(this)">
    <div> UserName :<input type="text" name="username"/></div>
    <div>Password :<input type="password" name="password"/></div>
    <div><input type="hidden" name="Token" value="${Token}"/></div>
    <div><input type="submit" value="Login"></div>
</form>
</body>
</html>