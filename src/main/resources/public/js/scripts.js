function setDate() {
  var url = "/setdate/" + $(".appDate").val()
  window.location.href = url;
}

function insertHeader() {
  $("#tableHeader").html("#foreach($i in [0..6])"+
  "<th>$today.plusDays($i).format($formatter)</th>"+
  "#end");
}
$(function() {
  $("#appEdit").click(function() {
    $("#appEditForm").slideToggle();
  })
})
