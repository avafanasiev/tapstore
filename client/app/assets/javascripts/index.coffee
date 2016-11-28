$ ->
  setTimeout(arguments.callee, 2000)
  $.get "/responses", (responses) ->
    $("#Responses").empty()
    $.each responses, (index, response) ->
      $("#Responses").append $("<li>").text response