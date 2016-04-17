print("Visit resources/init.js to modify me!");

var timer = "timer";

var seconds = 0;

game.add(function(game, dt) {
	if (game.every(1, timer)) {
		seconds++;
	}
});

var keyStates = {};
var K = java.awt.event.KeyEvent;

game.addEventHandler("KeyEvent", function(game, event) {
	var keyCode = event.getProps().keycode;
	var action = event.getProps().action;
	if (keyCode == K.VK_ESCAPE && 'released' == action) {
		game.quit();
	} else {
		keyStates[keyCode] = action;
	}
});

game.add(function(game, dt) {
	var x=0,y=0;
	if (keyStates[K.VK_UP] == 'pressed') {
		y = 1;
	}
	if (keyStates[K.VK_DOWN] == 'pressed') {
		y = -1;
	}
	if (keyStates[K.VK_LEFT] == 'pressed') {
		x = -1
	}
	if (keyStates[K.VK_RIGHT] == 'pressed') {
		x = 1;
	}
	var direction = (Math.atan2(y,x)*180)/Math.PI;
	var avatar = game.get('avatar');
	if (avatar) {
		if (direction > -1) {
			avatar.getProperties().SPEED = 20;
			avatar.getProperties().DIRECTION = direction;
		} else {
			avatar.getProperties().SPEED = 0;
		}
	}
})