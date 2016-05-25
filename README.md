# Pup8Bit
Ludumdare #35 competition entry. Turned into game engine. 

**Tiled for maps and CoffeeScript for scripting**

## Getting Started

	git clone git@github.com:njd5475/Pup8Bit.git

## Loading Maps

You can build your maps with [Tiled](http://www.mapeditor.org/)

May add a custom editor project later to support better integration.

In script files inside the state init method

	game.loadMap 'resources/puplevel1.tmx'

#### Retrieving Map Objects

You can access any loaded object properties from Tiled properties my by getting
the object by id.

	avatar = game.get 'avatar'
	avatar.getProperties().SPEED = 20 # in pixels per second
	avatar.getProperties().DIRECTION = 3.1459 # in radians

## Loading Images

In the main init.coffee file that gets executed place a line there to load 
assets

	game.loadImage 'resources/puppy.png'

## Spawning Entities

You can use the main `game` object to spawn new entities in an around different 
`views` on the screen.

Spawn an entity with

	game.add <Renderable>

## Event Handling

Pup has an event dispatching service. You can trigger events using

	props = {}
	myEvent = new GameEvent("CustomEvent", props)
	game.addEvent myEvent

And you can add listeners for events

	game.addEventHandler "CustomEvent", (game, event) ->
		print "Handling " + event.getName() + " that's all!"

## Input Handling

There are default events that the game will allow you to listen for `KeyEvent`
will be fired whenever there is a pressed.

	keyStates = {}
	K = java.awt.event.KeyEvent

	game.addEventHandler 'KeyEvent', (game, event) ->
  	  keyCode = event.getProps().keycode
  	  action = event.getProps().action
  	
  	keyStates[keyCode] = action
  
  	if keyStates[K.VK_ESCAPE] == 'released'
      game.quit()
    

## Controlling game states

TODO: coming soon!

## AI, Movement and Mechanics

I am going to integrate my behavior tree library and hopefully use this 
opportunity to add a behavior tree builder to that library.

## Todo

* Move all custom game code into script
* Add game state system
* Add asset management system

## Contributions Welcome

Find a bug fix it open pull request against offending branch and submit for 
review. Submitting failing tests first is also appreciated. In the future I 
might only accept a failing test in a pull request. Right now there are only a 
few tests but hope to add more with mockito after adding gradle. First things 
first get the basics working.
