function $(id){
  return document.getElementById(id);
}

var NormalData = null;
var DataTrick = null;

function upData(){
	NormalData = $('dataSend').value;
	DataTrick = "%" + $('dataSend').value + "%";
	var data = {
		dataTrick: DataTrick,
	};
	let config = {
		method: 'POST',
		body: JSON.stringify(data),
	};
	fetch("./Filter", config)
    	.then(function(response){
    		return response.json();
    	})
    	.then(function(data){
    		alert(data.message);
    		var item2;
    		if(data.status == 200 && data.status != undefined){
    			$("disabled").value = data.message;
    			for (var i = 0; i < data.arraysFound[0].length; i++){
    				console.log(data.arraysFound[0][i].includes(NormalData));
    				if((data.arraysFound[0][i].includes(" " + NormalData + " "))/* || (data.arraysFound[0][i].includes(" " + NormalData)) || (data.arraysFound[0][i].includes(NormalData + " ")) */|| (data.arraysFound[0][i].startsWith(NormalData + " ")) || (data.arraysFound[0][i].endsWith(" " + NormalData))) {
			        	item2 = document.createElement("h6");
			        	item2.innerHTML = "Found: " + data.arraysFound[0][i] + " It was found: " + data.arraysFound[1][i] + " before.";
			        	$("wordsFound").appendChild(item2);
			        	$("wordsFound").style.display = "block";
    				} /*else if(!data.arraysFound[0][i].includes(" " + NormalData)){
    					item2 = document.createElement("h6");
			        	item2.innerHTML = "Found: " + data.arraysFound[0][i] + " It was found: " + data.arraysFound[1][i] + " before.";
			        	$("wordsFound").appendChild(item2);
			        	$("wordsFound").style.display = "block";
    				}*/
		        }
    			alert("The words down are the result(s). If you want an update about most wanted, reload the page.")
    		} else {
    			alert("Oops..! Something is wrong. Reload the page. Also you can try another word");
    		}
    	})
	    .catch((e) => {
	    	console.log(e)
	    })
}

function forcedLowerCase(){
	var newValue = $("dataSend").value;
	$("dataSend").value = newValue.toLowerCase();
}

function getMostWanted(){
	let config = {
		method: 'GET'
	}
	fetch("./Filter", config)
	.then(function (response){
		return response.json();
	})
	.then(function(data){
		if(data.status == 200){
			var item1;
			for (var i = 0; i < data.array[0].length; i++){
				item1 = document.createElement("h6");
				item1.innerHTML = "Sentence: " + data.array[0][i] + ". Has ID: " + data.array[2][i] + ". And Was Found: " + data.array[1][i] + " time(s).";
				$("mostWantedResult").appendChild(item1);
			}
		} else {
			alert("Reload the page. Something is wrong in the back. Also try telling this to an administrator.");
		}
	})
}

function reloadPage(){
	location.reload();
}

$("Start").addEventListener("click", upData);