//标准AJAX
var xmlHttpRequest;
var eleName;

function createXMLHttpRequest() {
    if (window.XMLHttpRequest)
    {// code for IE7+, Firefox, Chrome, Opera, Safari
        xmlHttpRequest=new XMLHttpRequest();
    }
    else if(window.ActiveXObject)
    {
        xmlHttpRequest = new ActiveXObject("Msxml2.XMLHTTP");
    }
    else
    {// code for IE6, IE5
        xmlHttpRequest=new ActiveXObject("Microsoft.XMLHTTP");
    }
}

function processResponse() {

    if(xmlHttpRequest.readyState == 4)
    {
        if(xmlHttpRequest.status == 200)
        {
            var responseInfo = xmlHttpRequest.responseXML.getElementsByTagName("msg")[0].firstChild.data;
            var str = new String(responseInfo);

            var div1 = document.getElementById(eleName);
            if(str.split('#')[0] == "NotExist") div1.parentNode.parentNode.parentNode.removeChild(div1.parentNode.parentNode);
            else div1.innerHTML = str.split('#')[0];

            var div2 = document.getElementById("subTotal");
            div2.innerHTML = str.split('#')[1];
        }
    }
}

function sendRequest(url) {
    createXMLHttpRequest();
    xmlHttpRequest.open("GET",url,true);
    xmlHttpRequest.onreadystatechange = processResponse;
    xmlHttpRequest.send(null);
}

function updateTotal(eleId) {
    var quantity = document.getElementById(eleId).value;
    eleName = eleId + "total";
    sendRequest("/cart/updateCartQuantity?eleId="+eleId+"&quantity="+quantity);
}