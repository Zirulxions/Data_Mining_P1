function $(id){
  return document.getElementById(id);
}

var DataTrick = null;

function upData(){
  DataTrick = "%" + $('dataSend').value + "%";
  var data = {
	dataTrick: DataTrick,
  };
  console.log(data);
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

$("Start").addEventListener("click", upData);