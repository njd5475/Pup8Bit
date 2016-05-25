
layout = layoutBuilder.buttonsCentered().inOrder()
button = (text) ->
	button(text, layout)

@add button("Start a new game")
@add button("Quit")