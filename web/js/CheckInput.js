/**
 * Created on 2016/3/14.
 *
 * @author 王启航
 * @version 1.0
 */
function CheckInput() {
    /**
     * 判断输入是否为空
     * @param field 表单的一个输入框
     * @param alertText 警告的文本
     */
    this.isEmpty = function (field, alertText) {
        with (field) {
            if (value == null || value == "") {
                alert(alertText);
                field.focus();
                return false;
            }
            else {
                return true;
            }
        }
    };

    /**
     * 判断是否为Email形式
     * @param field  表单的一个输入框
     * @param alertText 警告的文本
     */
    this.isEmail = function (field, alertText) {
        with (field) {
            //JS 正则表达式要在/ /之间,不使用双引号
            var reg = /^([a-zA-Z0-9_-]){5,15}@([a-zA-Z0-9_-])+.([a-zA-Z0-9_-]){2,5}/;
            if (!reg.test(value)) {
                alert(alertText);
                field.focus();
                return false;
            }
            else
                return true;
        }
    };


}