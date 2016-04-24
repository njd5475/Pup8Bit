print 'Visit resources/init.coffee to modify me!'
game.loadMap "resources/puplevel1.tmx"
timer = 'timer'
seconds = 0
game.add (game, dt) ->
  if game.every(1, timer)
    seconds += 1
  
keyStates = {}
K = java.awt.event.KeyEvent

game.addEventHandler 'KeyEvent', (game, event) ->
  keyCode = event.getProps().keycode
  action = event.getProps().action
  if keyCode == K.VK_ESCAPE and 'released' == action
    game.quit()
  else
    keyStates[keyCode] = action
  x = 0
  y = 0
  y = -1 if keyStates[K.VK_UP] == 'pressed'
  y = 1 if keyStates[K.VK_DOWN] == 'pressed'
  x = -1 if keyStates[K.VK_LEFT] == 'pressed'
  x = 1 if keyStates[K.VK_RIGHT] == 'pressed'
  avatar = game.get('avatar')
  if avatar
    avatar.getProperties().SPEED = 0
    if x != 0 or y != 0
      direction = Math.atan2(y, x) * 180 / Math.PI
      avatar.getProperties().SPEED = 20
      avatar.getProperties().DIRECTION = direction
