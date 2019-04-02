function $(id){
  return document.getElementById(id);
}

var DataTrick = null;

function upData(){
  DataTrick = "%" + $('dataSend').value + "%";
  var data = {
	dataTrick: DataTrick,
  };
  //console.log(data);
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
      if(data.status == 200 && data.status != undefined){
        $("disabled").value = data.message;
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
			var item1, item2, item3, item4, item5;
			for (var i = 0; i < data.array[0].length; i++){
				console.log("sentence: " + data.array[0][i] + " found: " + data.array[1][i] + " Times.");
				item1 = document.createElement("h6");
				item1.innerHTML = "Sentence: " + data.array[0][i] + ". Has ID: " + data.array[2][i] + ". And Was Found: " + data.array[1][i] + " time(s).";
				$("mostWantedResult").appendChild(item1);
			}
		} else {
			alert("Reload the page. Something is wrong in the back.");
		}
	})
}

$("Start").addEventListener("click", upData);