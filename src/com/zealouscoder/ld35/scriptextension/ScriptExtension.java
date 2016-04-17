package com.zealouscoder.ld35.scriptextension;

import java.io.FileNotFoundException;
import java.io.FileReader;

import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.zealouscoder.ld35.Game;

public class ScriptExtension {

	private ScriptEngine engine;

	public ScriptExtension(Game game) {
		engine = new ScriptEngineManager().getEngineByName("nashorn");
		engine.getContext().setAttribute("game", game, ScriptContext.GLOBAL_SCOPE);
	}

	public void runInitScript() throws FileNotFoundException, ScriptException {
		engine.eval(new FileReader("resources/init.js"));
	}

}
