var moreBtn = document.getElementById("more");
var finishBtn = document.getElementById("finish");
var moreEle = document.getElementById("more").getElementsByTagName("a")[0];
var finishEle = document.getElementById("finish").getElementsByTagName("a")[0];
var myCardEle = document.getElementById("myCards");
var otherCardEle = document.getElementById("others");
var othersCardsEles = otherCardEle.getElementsByTagName("tr");
var myCardsEles = myCardEle.getElementsByTagName("td");
var readyBtn = document.getElementById("ready");
var nickEle = document.getElementById("nickname");
var myTotalEle = myCardsEles[6];
var myCards = new Array();
var othersCards = {};

window.onload = function(){
	moreEle.setAttribute("disabled", "");//this attribute is able to be null
	finishEle.setAttribute("disabled", "");
};

readyBtn.addEventListener("click", function() {
	if (myCards.length == 0) {
		$.ajax({
			url : "service/gameInit",
			type : "POST",
			success : function(data) {
				gameStart();
//				console.log("Game start!");
//				readyBtn.setAttribute("disabled", "disabled");//no use
				nickEle.setAttribute("disabled", "");
				readyBtn.firstChild.setAttribute("disabled", "disabled");
				moreEle.removeAttribute("disabled");
				finishEle.removeAttribute("disabled");
			}
		});
	} else {
		alert("Game has been started!");
	}
});
moreBtn.addEventListener("click", function() {
	if (myCards.length < 5) {
		$.ajax({
			url : "service/more",
			data : {
				 "nickname" : myCardsEles[0].innerText
			},
			type : "POST",
			success : function(data) {
//				console.log(data);
				var value = getCardValue(data);
				myCards[myCards.length] = value;
				showCards(myCards, myCardsEles);
				sumCards(myCards, myTotalEle);
			},
			error:function(e){
				console.error(e);
			}
		});
	} else {
		alert("Already full cards!");
	}
});
finishBtn.addEventListener("click", function() {
	if (myCards.length >= 2) {
		$.ajax({
			url : "service/finish",
			data : {
				"nickname" : myCardsEles[0].innerText
			},
			type : "POST",
			success : function(data) {
				moreEle.setAttribute("disabled", "disabled");
				finishEle.setAttribute("disabled", "disabled");
				if(data!= null && data!=""){
					alert("Winner: " + data);
				}
			}
		});
	} else {
		alert("no enough cards!");
	}
});

function gameStart() {
	$.ajax({
				url : "service/gameStart",
				data : {
					 "nickname" : nickEle.value
				},
				type : "POST",
				success : function(data, status, statecodes) {
					var obj = JSON.parse(data);
					myCards = obj.myCards;
					myCardsEles[0].innerText = obj.nickname;
					showCards(myCards, myCardsEles);
					sumCards(myCards, myTotalEle);
				}
			});
}

function getCardValue(card) {
	value = 0;
	switch (card) {
	case "A":
		value = 1;
		break;
	case "J":
		value = 11;
		break;
	case "Q":
		value = 12;
		break;
	case "K":
		value = 13;
		break;
	default:
		value = parseInt(card);
		break;
	}
	return value;
}

function showCards(cards, cardsEles) {
	for (var i = 0; i < cards.length; i++) {
		cardsEles[i + 1].innerText = cards[i];
	}
}

function sumCards(cards, totalElement) {
	var total = 0;
	for (var i = cards.length - 1; i >= 0; i--) {
		if (cards[i].innerText !== null && cards[i].innerText !== "") {
			total += getCardValue(cards[i]);
			totalElement.innerText = total;
		}
	}
}

function getOthersData() {
	if(myCards.length>=2 && myCards.length<=5){
		$.ajax({
			url : "service/getOthersData",
			data:{"nickname" : myCardsEles[0].innerText},
			type : "POST",
			success : function(data) {
				if(data!=null && data!="" && data.substr(0,1)!="w"){//not sure function starts_with
					var objs = JSON.parse(data);
					for(var i=0; i<objs.length; i++){
						if(myCardsEles[0].innerText!=objs[i].nickname && myCards.length!=5){
							othersCardsEles[i+1].getElementsByTagName("td")[0].innerText = objs[i].nickname;
							showCards(objs[i].myCards, othersCardsEles[i+1].getElementsByTagName("td"));
							sumCards(objs[i].myCards, othersCardsEles[i+1].getElementsByTagName("td")[6]);
						}else{
							return;
						}
					}
				} else if(data.substr(0,1)==("w")) {
					alert(data);
					console.log(timer);
					clearInterval(timer);
					timer = null;					
				} else {
					console.log(data);
				}
			},
			error : function(e) {
				console.log(e+"getOthersData error!");
			}
		});
	}
}

var timer = setInterval("getOthersData()", 2000);
