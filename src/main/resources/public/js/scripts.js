// $(function() {
//
//
// })


function setDate() {

  // var url = "http://" + location.hostname + ":" + location.port + "/setdate/" + $("#appDate").val()
  var url = "/setdate/" + $("#appDate").val()
  console.log(url);
  window.location.href = url;
}
