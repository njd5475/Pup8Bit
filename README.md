# Pup8Bit
Ludumdare #35 competition entry. Turned into game engine. 

**Tiled for maps and CoffeeScript for scripting**

## Getting Started

	git clone git@github.com:njd5475/Pup8Bit.git

## Loading Maps

You can build your maps with [Tiled](http://www.mapeditor.org/)

May add a custom editor project later to support better integration.

In script files inside the state init method

	init ->
		loadMap 'resources/puplevel1.tmx'

## Loading Images

In the main init.coffee file that gets executed place a line there to load assets

	loadImage 'resources/puppy.png'

## Spawning Entities

You can use the main `game` object. 

## Handling Input



## Movement and Mechanics

## Contributions Welcome

Find a bug fix it open pull request against offending branch and submit for 
review. Tests first also appreciated. If you want to reproduce the bug using
a failing test that is probably the best way. I might even only accept that as
as pull request in the future. Right now there are no tests but hope to get unit
testing built with mockito and junit after adding gradle. First things first 
get the basics working.
