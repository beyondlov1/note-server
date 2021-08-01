function ajax(url, data, onSuccess, onFail) {
    $.ajax({
        //请求方式
        type: "POST",
        //请求的媒体类型
        contentType: "application/json;charset=UTF-8",
        //请求地址
        url: url,
        //数据，json字符串
        data: JSON.stringify(data),
        //请求成功
        success: function (result) {
            onSuccess(result)
        },
        //请求失败，包含具体的错误信息
        error: function (e) {
            console.log(e);
            if (e.status === 401) {
                window.location = "/login.html"
            }
            if (onFail) {
                onFail(e)
            }
        }
    });
}

function postForJson(url, data, onSuccess, onException,onFail) {
    $.ajax({
        //请求方式
        type: "POST",
        //请求的媒体类型
        contentType: "application/json;charset=UTF-8",
        //请求地址
        url: url,
        //数据，json字符串
        data: JSON.stringify(data),
        //请求成功
        success: function (result) {
            if (isSuccess(result)) {
                onSuccess(result)
            }else{
                if (onException){
                    onException(result)
                }
            }
        },
        //请求失败，包含具体的错误信息
        error: function (e) {
            console.log(e);
            if (onFail) {
                onFail(e)
            }
        }
    });
}

function pushAll(sourceList, targetList) {
    for (var i = 0; i < targetList.length; i++) {
        sourceList.push(targetList[i])
    }
}

function putAll(sourceObj, targetObj) {
    for (var key in targetObj) {
        sourceObj[key] = targetObj[key]
    }
}


function setCookie(c_name, value, expiredays) {
    var exdate = new Date();
    exdate.setDate(exdate.getDate() + expiredays);
    document.cookie = c_name + "=" + escape(value) + ((expiredays == null) ? "" : ";expires=" + exdate.toGMTString());
}

function getCookie(c_name){
    if (document.cookie.length>0){　　//先查询cookie是否为空，为空就return ""
        c_start=document.cookie.indexOf(c_name + "=")　　//通过String对象的indexOf()来检查这个cookie是否存在，不存在就为 -1　　
        if (c_start!=-1){
            c_start=c_start + c_name.length+1　　//最后这个+1其实就是表示"="号啦，这样就获取到了cookie值的开始位置
            c_end=document.cookie.indexOf(";",c_start)　　//其实我刚看见indexOf()第二个参数的时候猛然有点晕，后来想起来表示指定的开始索引的位置...这句是为了得到值的结束位置。因为需要考虑是否是最后一项，所以通过";"号是否存在来判断
            if (c_end==-1) c_end=document.cookie.length
            return unescape(document.cookie.substring(c_start,c_end))　　//通过substring()得到了值。想了解unescape()得先知道escape()是做什么的，都是很重要的基础，想了解的可以搜索下，在文章结尾处也会进行讲解cookie编码细节
        }
    }
    return ""
}


function jumpUrl(url) {
    window.location.href = url;
}



function getQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    var reg_rewrite = new RegExp("(^|/)" + name + "/([^/]*)(/|$)", "i");
    var r = window.location.search.substr(1).match(reg);
    var q = window.location.pathname.substr(1).match(reg_rewrite);
    if(r != null){
        return unescape(r[2]);
    }else if(q != null){
        return unescape(q[2]);
    }else{
        return null;
    }
}

function getList(obj) {
    var array = [];
    for (var key in obj) {
        array.push({key: key, value: obj[key]})
    }
    return array
}

function getObj(list) {
    var obj = {};
    for (var i = 0; i < list.length; i++) {
        obj[list[i]["key"]] = list[i]["value"]
    }
    return obj;
}


function isSuccess(result) {
    return result["code"] === 0;
}

function notBlankCheck(obj) {
    for (var key in obj) {
        var value = obj[key];
        if (value == null || value ==="" || value  === undefined) {
            return key;
        }
    }
    return null;
}


function setToken(token) {
    setCookie("l_cok", window.btoa(token), 30);
}

function getToken() {
    var cookie = getCookie('l_cok_t');
    if (cookie !== "") {
        return window.atob(cookie);
    }
    return null;
}