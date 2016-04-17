print("Hello from my init script");

var timer = "timer";

game.add(function(game, dt) {
	if(game.every(2, timer)) {
		print("Waiting two seconds");
	}
});

