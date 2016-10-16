// var chatBox = $("#chatBox");
var msgPanel = $("#msgPanel");
var sndBox = $("#sndBox");
var sndBtn = sndBox.find("input").last();
var sndInput = sndBox.find("input").eq(0);

//ajax setup
sndBtn.bind("click", function () {
    if (sndInput.val()!=="" && sndInput.val()!==null) {
        $.ajax({
            url:"service/sndMsg",
            data:{"message":sndInput.val(), "uname":nickEle.value},
            type:"POST",
            success:function(data){
            	sndInput.val("");
                console.log(data);
            }
        });
    }
});

function getMsg(){
    if(nickEle.value.length!==0){
        $.ajax({
            url:"service/getMsg",
            data:{"uname":nickEle.value},
            type:"POST",
            dataType:"json",
            success:function(data){
                if(data!=null && data!=""){
                	msgPanel.html("");
                    $.each(data, function(i, obj){
                    	console.info(i, obj);
                    	var uname = obj.uname, message = obj.message, sndMsgTime=obj.sndMsgTime;
                        var msgDiv = $("<div class='well singleMsg'></div>");
                        var msgHead = $("<p>"+sndMsgTime+"\t\t"+uname+":</p>");
                        var msgContent = $("<p>"+message+"</p>");
                        msgDiv.append(msgHead).append(msgContent);
                        msgPanel.append(msgDiv);         
                    });
                }
            }
        });
    }
}

function getLocalTime(nS) {  
	 return new Date(parseFloat(nS) * 1000).toLocaleString().replace(/:\d{1,2}$/,' ');  
}

setInterval("getMsg()", 2000);