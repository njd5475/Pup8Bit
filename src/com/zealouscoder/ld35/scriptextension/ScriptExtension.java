package com.zealouscoder.ld35.scriptextension;

import java.io.FileNotFoundException;
import java.io.FileReader;

import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.zealouscoder.ld35.Game;

public class ScriptExtension {

	protected ScriptEngine engine;

	public ScriptExtension(Game game) {
		engine = new ScriptEngineManager().getEngineByName("nashorn");
		engine.getContext().setAttribute("game", game, ScriptContext.GLOBAL_SCOPE);
	}

	public void runInitScript() throws FileNotFoundException, ScriptException {
		runScript("resources/init.js");
	}

    public void runScript(String file) {
        try {
            engine.eval(new FileReader(file));
        } catch (FileNotFoundException | ScriptException e) {
            e.printStackTrace();
        }
    }
}
